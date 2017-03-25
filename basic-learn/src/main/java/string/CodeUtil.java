package string;

import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by wanghb on 2016/12/9.
 */
public class CodeUtil {
    public static String encode(String s) {
        return URLEncoder.encode(s);
    }

    public static String decode(String s) {
        return URLDecoder.decode(s);
    }
}
