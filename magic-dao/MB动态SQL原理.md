MyBatis 的动态 SQL 功能允许开发者根据条件灵活地拼接 SQL 语句，其实现基于 **XML 标签解析**、**OGNL 表达式** 和 **运行时参数处理**。以下是动态 SQL 的核心实现原理和流程：

---

### **1. 动态 SQL 的核心标签**
MyBatis 提供以下常用动态 SQL 标签：
- **`<if>`**：条件判断。
- **`<choose>/<when>/<otherwise>`**：多条件分支选择。
- **`<foreach>`**：遍历集合生成 SQL 片段。
- **`<trim>/<where>/<set>`**：处理 SQL 前缀/后缀，避免语法错误。
- **`<bind>`**：绑定变量到上下文。

---

### **2. 实现原理**
#### **2.1 XML 解析与 SqlNode 树**
- **解析阶段**：
    - MyBatis 在启动时加载 XML 映射文件，解析动态 SQL 标签。
    - 每个动态标签（如 `<if>`、`<foreach>`）会被转换为对应的 **`SqlNode` 对象**（如 `IfSqlNode`、`ForEachSqlNode`）。
    - 最终形成一个 **`SqlNode` 树形结构**，表示动态 SQL 的逻辑关系。

- **示例解析过程**：
  ```xml
  <select id="findUser">
    SELECT * FROM user
    <where>
      <if test="name != null">
        AND name = #{name}
      </if>
      <if test="age != null">
        AND age = #{age}
      </if>
    </where>
  </select>
  ```
    - 解析后生成 `WhereSqlNode`，内部包含两个 `IfSqlNode`。

#### **2.2 动态 SQL 生成（运行时）**
- **运行时处理**：
    - 当调用 Mapper 方法时，MyBatis 根据传入的参数，遍历 `SqlNode` 树。
    - 通过 **OGNL（Object-Graph Navigation Language）** 解析条件表达式（如 `test="name != null"`），判断是否包含某段 SQL。
    - 动态拼接最终的 SQL 语句，并处理参数占位符（`#{...}`）。

- **关键类**：
    - **`DynamicSqlSource`**：处理动态 SQL 的入口，遍历 `SqlNode` 生成可执行的 SQL。
    - **`BoundSql`**：封装最终生成的 SQL 语句和参数映射信息。

#### **2.3 参数绑定与 SQL 执行**
- **参数处理**：
    - 动态生成的 SQL 中的 `#{...}` 占位符会被替换为 `?`（JDBC 参数占位符）。
    - 通过 `ParameterHandler` 将 Java 对象属性绑定到 JDBC 参数。
- **执行 SQL**：
    - `Executor` 使用 `StatementHandler` 执行 SQL，返回结果。

---

### **3. 动态 SQL 的底层实现**
#### **3.1 OGNL 表达式**
- **作用**：解析动态标签中的条件表达式（如 `test="age > 18"`）。
- **原理**：
    - OGNL 从参数对象（`parameterObject`）中获取属性值。
    - 例如，`test="name != null"` 会调用 `parameterObject.getName()` 并判断是否为 `null`。

#### **3.2 SqlNode 的实现类**
| **SqlNode 类型**      | **功能**                                                                 |
|------------------------|--------------------------------------------------------------------------|
| `IfSqlNode`            | 处理 `<if test="...">`，根据条件判断是否包含 SQL 片段。                   |
| `ChooseSqlNode`        | 处理 `<choose>/<when>/<otherwise>`，实现多分支选择。                    |
| `ForEachSqlNode`       | 处理 `<foreach>`，遍历集合并拼接 SQL（如 `IN` 语句）。                   |
| `TrimSqlNode`          | 处理 `<trim>`、`<where>`、`<set>`，自动添加/移除前缀/后缀（如 `WHERE`）。 |
| `VarDeclSqlNode`       | 处理 `<bind>`，将变量绑定到上下文。                                      |

#### **3.3 动态 SQL 的拼接流程**
1. **初始化上下文**：创建 `DynamicContext`，存储运行时参数和生成的 SQL 片段。
2. **遍历 SqlNode 树**：递归处理每个 `SqlNode`，根据条件拼接 SQL。
3. **处理占位符**：将 `#{...}` 转换为 `?`，并记录参数映射关系。
4. **生成 BoundSql**：最终得到完整的 SQL 和参数列表。

---

### **4. 示例解析**
#### **场景：动态条件查询**
**Mapper XML**：
```xml
<select id="findUser" resultType="User">
  SELECT * FROM user
  <where>
    <if test="name != null">
      AND name = #{name}
    </if>
    <if test="age != null">
      AND age = #{age}
    </if>
  </where>
</select>
```

**执行过程**：
1. **参数 `{name: "Alice", age: 20}`**：
    - 生成 SQL：`SELECT * FROM user WHERE name = ? AND age = ?`。
    - 参数值：`"Alice"`, `20`。

2. **参数 `{name: null, age: 20}`**：
    - 生成 SQL：`SELECT * FROM user WHERE age = ?`。
    - 参数值：`20`。

3. **参数 `{}`**：
    - 生成 SQL：`SELECT * FROM user`（`<where>` 标签自动移除多余的 `AND`）。

---

### **5. 性能优化**
- **预解析与缓存**：
    - MyBatis 在启动时解析 XML 并构建 `SqlNode` 树，运行时直接复用。
    - 动态生成的 SQL 会被缓存（基于参数条件的哈希值），避免重复解析。

- **避免过度动态化**：
    - 动态 SQL 的灵活性会带来解析开销，高频查询尽量使用静态 SQL。

---

### **6. 总结**
MyBatis 的动态 SQL 实现基于以下核心机制：
1. **XML 标签解析**：将动态标签转换为 `SqlNode` 树。
2. **OGNL 表达式**：运行时评估条件逻辑。
3. **动态 SQL 拼接**：根据参数生成最终 SQL。
4. **参数绑定与执行**：通过 JDBC 执行并返回结果。

通过这一机制，开发者可以编写灵活的条件查询，同时 MyBatis 在底层保障了 SQL 的安全性和高效性。