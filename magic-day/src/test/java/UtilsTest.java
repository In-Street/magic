import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.*;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import com.magic.base.dto.FrameworkVo2;
import com.magic.base.dto.FrameworkVo3;
import com.magic.dao.model.Framework;
import com.magic.dao.model.User;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Cheng Yufei
 * @create 2024-04-18 18:25
 **/
@Slf4j
public class UtilsTest {


    @Test
    public void rangeMap() {

        TreeRangeMap<Integer, String> treeRangeMap = TreeRangeMap.create();
        treeRangeMap.put(Range.closed(1, 3), "A");
        treeRangeMap.put(Range.closed(4, 7), "B");

        String s = treeRangeMap.get(3);
        System.out.println(s);
    }

    /**
     * 可变类实例Map
     */
    @Test
    public void instanceMap() {

        User user = new User("Bonus Track");
        Framework framework = new Framework("技术部");

        MutableClassToInstanceMap<Object> instanceMap = MutableClassToInstanceMap.create();
        instanceMap.putInstance(User.class, user);
        instanceMap.putInstance(Framework.class, framework);

        System.out.println(((User) instanceMap.getOrDefault(User.class, new User())).getUsername());
    }

    @Test
    public void bitSet() {

        // 创建一个BitSet实例  【非线程安全】
        BitSet bitmap = new BitSet();

        // 设置第5个位置为1，表示第5个元素存在
        bitmap.set(5);

        // 检查第5个位置是否已设置
        boolean exists = bitmap.get(5);
        System.out.println("Element at position 5 exists:" + exists);

        // 设置从索引10到20的所有位置为1
        // 参数是包含起始点和不包含终点的区间
        bitmap.set(10, 21);
        System.out.println("Element at position 21 exists: "+bitmap.get(21));

        // 计算bitset中所有值为1的位的数量，相当于计算设置了的元素个数
        int count = bitmap.cardinality();
        System.out.println("Number of set bits: " + count);
        // 清除第5个位置
        bitmap.clear(5);
        // 判断位图是否为空
        boolean isEmpty = bitmap.isEmpty();
        System.out.println("Is the bitset empty after clearing some bits?" + isEmpty);

    }

    @Test
    public void lombokBuilder() throws JsonProcessingException {

        // @Builder：
        //  1. 创建一个全参构造器；
        //  2.为每个属性创建一个同名方法用于赋值，替代setter
        //  3. 没有getter方法
        FrameworkVo2 vo = FrameworkVo2.builder()
                .frameworkId(1L)
                .frameworkName("技术部")
                .aliasNameList(Lists.newArrayList("A","B"))
                .S1("C")
                .clearAliasNameList()
                .S1("D")
                .S1("E")
                .build();
        System.out.println(" >> vo :  "+vo);

        // @With:  返回一个新对象
        FrameworkVo2 vo3 = vo.withFrameworkName("蒲公英");
        System.out.println(" >> vo3 :  "+vo3);
        // @With:  创建对象赋值时 可直接链式调用
        FrameworkVo2 vo4 = new FrameworkVo2().withFrameworkName("晴天");
        System.out.println(" >> vo4 :  "+vo4);


        //序列化失败： No serializer found for class com.magic.base.dto.FrameworkVo2 and no properties discovered to create BeanSerializer
        // 解决：添加 @Getter注解，序列化成功
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(vo);
        System.out.println(" >> 序列化 :  "+s);

        //反序列化失败：  Cannot construct instance of `com.magic.base.dto.FrameworkVo2` (no Creators, like default constructor, exist)
        // 1. 手动添加无参构造器或添加 @NoArgsConstructor 的话，@Builder 会报错
        // 2. 添加@Data 仍反序列化失败，因为@Builder存在全参构造器时，无参构造器无效
        // 3. 解决： 添加 @NoArgsConstructor、@AllArgsConstructor(access = AccessLevel.PRIVATE)，注意级别
        String str = "{\"name\":null,\"frameworkId\":2,\"parentId\":1,\"frameworkName\":\"一起长大\",\"treeName\":null,\"childrenList\":null,\"aliasNameList\":[\"F\",\"G\"]}";
        FrameworkVo2 deserializationFrameworkVo2 = objectMapper.readValue(str, FrameworkVo2.class);
        System.out.println(" >> 反序列化 :  "+deserializationFrameworkVo2);

        //参数设置默认值无效：默认值是在Bean上，Builder上没赋值 也就不能把值赋值给Bean，从而null覆盖默认值
        // 解决： 设置默认值的属性上添加 @Builder.Default

    }

    @Test
    public void lombokAccessors() throws JsonProcessingException {

        // fluent = true，就只会生成普通的函数去访问字段，没有真正意义上的getter/setter
        FrameworkVo3 vo = new FrameworkVo3()
                .frameworkName("一路向北")
                .aliasNameList(Lists.newArrayList("Jetbrains"));
        System.out.println(vo);

        //序列化/反序列化失败： @Accessors(fluent = true) + @Getter + @Setter
        // 解决：给需要参与序列化和反序列化的属性添加 @JsonProperty("xxx")，此时没有添加 @JsonProperty 注解的属性，在序列化后不会展示，反序列化会报错：Unrecognized field "xxx"。此时通过添加 @JsonIgnoreProperties(ignoreUnknown = true) 来进行忽略，可成功反序列化
        ObjectMapper objectMapper = new ObjectMapper();
        String s = objectMapper.writeValueAsString(vo);
        System.out.println(" >> 序列化 :  "+s);

        String str = "{\"name\":null,\"frameworkId\":2,\"parentId\":1,\"frameworkName\":\"一起长大\",\"treeName\":null,\"childrenList\":null,\"aliasNameList\":[\"F\",\"G\"]}";
        FrameworkVo3 deserializationFrameworkVo3 = objectMapper.readValue(str, FrameworkVo3.class);
        System.out.println(" >> 反序列化 :  "+deserializationFrameworkVo3);

    }
}

