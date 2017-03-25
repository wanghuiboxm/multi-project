package algorithm;

import java.util.Arrays;

/**
 * Created by wanghb on 2017/3/19.
 */
public class Kmp {

    /**
     * @param s 查找匹配的字符串
     * @param m 匹配的字符串
     * @return  匹配出现的位置下标
     */
    public static int getIndexOf(String s, String m) {
        if (s == null || m == null || m.length() < 1 || s.length() < m.length()) {
            return -1;
        }
        char[] ss = s.toCharArray();
        char[] ms = m.toCharArray();
        int si = 0; //字串指针
        int mi = 0;  //匹配字串指针
        int[] next = getNextArray(ms);
        while (si < ss.length && mi < ms.length) {
            if (ss[si] == ms[mi]) {
                si++;
                mi++;
            } else if (next[mi] == -1) {
                si++;
            } else {
                mi = next[mi];
            }
        }
        return mi == ms.length ? si - mi : -1;
    }

    // 根据要匹配的字符串得到next数组
    public static int[] getNextArray(char[] ms) {
        if (ms.length == 1) {
            return new int[] { -1 };
        }
        int[] next = new int[ms.length];
        next[0] = -1;
        next[1] = 0;
        int pos = 2;
        int cn = 0; //回溯的位置,表示pos位置直接退回比较的位置
        while (pos < next.length) {
            if (ms[pos - 1] == ms[cn]) {  //如果当前回溯位置与pos-1位置相当，则好回溯位置加一，pos++
                next[pos++] = ++cn;
            } else if (cn > 0) { //如果不想等，但是cn>0，则向前找即next[cn]指向的下标
                cn = next[cn]; //向前找
            } else { //其他直接为0
                next[pos++] = 0;
            }
        }
        return next;
    }


    public static void main(String[] args) {
        String s = "abcabcdabcabcad";
        String match = "abcabcad";
        System.out.println(Arrays.toString(getNextArray(match.toCharArray())));
        System.out.println(getIndexOf(s, match));
//        System.out.println(match(s, match));
    }
}
