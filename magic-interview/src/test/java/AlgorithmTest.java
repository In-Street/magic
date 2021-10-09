import org.junit.Test;

import java.util.Arrays;
import java.util.Stack;

/**
 *
 * @author Cheng Yufei
 * @create 2021-05-04 5:48 下午
 **/
public class AlgorithmTest {

    /**
     * 利用栈【后进先出】字符串反向输出
     */
    @Test
    public void reverse() {
        String s = "qwerty";
        Stack<Character> stack = new Stack<>();

        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            stack.push(chars[i]);
        }
        for (int i = 0; i < chars.length; i++) {
            System.out.print(stack.pop());
        }
    }

    /**
     * 字符串数组最长公共前缀: 先利用Arrays.sort(strs)为数组排序，再将数组第一个元素和最后一个元素的字符从前往后对比即可
     */
    @Test
    public void prefix() {
        String[] str = {"flower", "flow", "flight"};
        Arrays.sort(str);
        for (String s : str) {
            System.out.println(s);
        }
        int length = str.length;
        int num = Math.min(str[0].length(), str[length - 1].length());
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < num; i++) {
            if (str[0].charAt(i) == str[length - 1].charAt(i)) {
                builder.append(str[0].charAt(i));
                continue;
            }

        }
        System.out.println(builder.toString());


    }

    /**
     * 字符串转int
     */
    @Test
    public void str2int() {
        String s = "12312312";
        char[] chars = s.toCharArray();
        int res = 0;
        for (int i = 0; i < chars.length; i++) {
            if (!Character.isDigit(chars[i])) {
                System.out.println(res);
                return;
            }
            //转int，char char 计算会以int接收。若直接以char+int计算会以字符对应的ascii值进行计算
            int temp = chars[i] - '0';
            res = res * 10 + temp;
        }
        System.out.println(res);
    }

    /**
     * 冒泡
     */
    @Test
    public void bubblingSort() {
        int[] array = {4, 5, 6, 3, 2, 1};
        int length = array.length;
        //n个数据排序，进行n此循环
        for (int i = 0; i < length; i++) {
            //提前退出冒泡标志位
            boolean flag = false;
            //每次从第一个元素开始比较，循环i次，表示有i个元素已经确定位置，故-i；
            // 当j取到最后一个元素时，j+1已经没有可比较的元素了，所以-1
            for (int j = 0; j < length - i - 1; j++) {
                if (array[j] < array[j + 1]) {
                    continue;
                }
                int temp = array[j];
                array[j] = array[j + 1];
                array[j + 1] = temp;
                //有元素置换
                flag = true;
            }
            //没有数据交换，提前退出
            if (!flag) {
                break;
            }
        }
    }

    /**
     * 插入排序
     */
    @Test
    public void insertSort() {
        int[] array = {4, 5, 6, 3, 2, 1};
        int length = array.length;
        //第一个元素为初始已排序区间，从第二个元素开始和已排序区间元素一个个比较寻找插入位置
        for (int i = 1; i < length; i++) {
            int value = array[i];
            int j = i - 1;
            //从value的前一个元素开始找插入位置：value之前是已排序区间
            for (; j >= 0; j--) {
                if (array[j] < value) {
                    break;
                }
                //数据移动
                array[j + 1] = array[j];
            }
            //数据插入
            array[j + 1] = value;
        }
    }

    /**
     * 选择排序
     */
    @Test
    public void selectSort() {

        int[] arr = {1, 3, 2, 45, 65, 33, 12};
        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            //最小值存放，内循环中找到更小的则进行替换
            int min = arr[i];
            int minIndex = i;
            for (int j = i + 1; j < length; j++) {
                if (arr[j] >= min) {
                    continue;
                }
                min = arr[j];
                minIndex = j;
            }
            int temp = arr[i];
            arr[i] = min;
            arr[minIndex] = temp;
        }
    }

    /**
     * 快速排序
     */
    public void fastSort(int[] arr, int i, int j) {
        int left = i, right = j;
        if (left > right) {
            return;
        }
        //设定基准值
        int base = arr[left];
        while (i < j) {
            //指针j从右向左先移动，找比基准数小的元素
            while (arr[j] >= base && i < j) {
                j--;
            }
            //指针i从左向右移动，找比基准数大的元素
            while (arr[i] <= base && i < j) {
                i++;
            }
            //指针i、j找到的元素交换位置
            if (i < j) {
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        //i==j，退出循环后将基准值放到i==j的位置
        arr[left] = arr[i];
        arr[i] = base;
        //左边小于基准值数组处理
        fastSort(arr, 0, i - 1);
        //右边大于基准值数组处理
        fastSort(arr, i + 1, right);
    }

    @Test
    public void fast() {
        int[] arr = {3, 1, 2, 5, 4, 6, 9, 7, 10, 8};
        int length = arr.length;
        fastSort(arr, 0, length - 1);
    }

    /**
     * 二分查找
     */
    @Test
    public void dichotomy() {
        int[] arr = {1, 3, 5, 7, 9, 11};
        int key = 11;
        System.out.println(search(arr, key, 0, arr.length - 1));
    }

    private int search(int[] arr, int key, int left, int right) {
        if (left > right) {
            return -1;
        }
        int middle = (left + right) / 2;
        if (key == arr[middle]) {
            return arr[middle];
        }
        //搜索值小于中间值，从左边数组中找
        if (key < arr[middle]) {
            return search(arr, key, left, middle - 1);
        } else {
            return search(arr, key, middle + 1, right);
        }
    }
}
