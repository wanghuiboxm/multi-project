package string;

/**
 * Created by wanghb on 2017/1/17.
 */
public class NumberUtils {

    public static int getNumLength(int number) {
        int count=0;
        while(number != 0) {
            ++count;
            number = number/10;
        }
        return count;
    }

    public static String formatInt2Str(int i) {
        String result = null;
        if (i > 0 && i < 9) { //一位数
            result = "000" + i;
        } else if (i >= 10 && i < 99) { //两位数
            result = "00" + i;
        } else if (i >= 100 && i < 999) { //位数
            result = "0" + i;
        } else if (i >= 1000 && i < 9999) {
            result = "" + i;
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println((int)(Long.parseLong("1492095581000") > Integer.MAX_VALUE ? (Long.parseLong("1492095581000")/1000):Long.parseLong("1492095581000")));
    }
}
