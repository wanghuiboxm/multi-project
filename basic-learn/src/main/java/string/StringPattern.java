package string;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wanghb on 2017/3/30.
 */
public class StringPattern {
    @Test
    public void testLastIndex() {
        String s = "walilive://recommend/sublist?id=7157&title=tttt&channelid=100084&recommend=r-0-0-0-0";
        String recommond = "r-1-1-1-1-1";
        int lastIndex = s.lastIndexOf("recommend");
        System.out.println(lastIndex);
        System.out.println(s.substring(0, lastIndex));
        int[] arr = new int[0];
    }

    @Test
    public void testIsMatch() {
        String qua = "v1-and-wali_live-2.3.212-meng_1254_9_android_50001-zh_cn";
        System.out.println(isMatch(qua, 50001));
    }

    private static boolean isMatch(String qua, int liveType) {
        Pattern pattern = Pattern.compile("\\S+("+liveType+")$");
        Matcher matcher = pattern.matcher(qua);
        int end = qua.lastIndexOf("-");
        matcher = matcher.region(0, end);
        return matcher.matches();
    }

    @Test
    public void matchTime() {
        String s = "13:11:61-14:22:33";
        String regex = "[0-2][0-9]:[0-5][0-9]:[0-5][0-9]-[0-2][0-9]:[0-5][0-9]:[0-5][0-9]";
        boolean matches = Pattern.matches(regex, s);
        System.out.println(matches);
    }
}
