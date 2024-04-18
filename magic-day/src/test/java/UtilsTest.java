import com.google.common.collect.*;
import com.magic.dao.model.Framework;
import com.magic.dao.model.User;
import org.junit.jupiter.api.Test;

import java.util.BitSet;

/**
 * @author Cheng Yufei
 * @create 2024-04-18 18:25
 **/
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

        // 创建一个BitSet实例
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
}
