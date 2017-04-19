package date;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wanghb on 2017/4/5.
 */
public class StringToDate {
    @Test
    public void string2Date() throws ParseException {
        String s = "13:11:11";
        String format_style = "HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format_style);
        Date date = sdf.parse(s);
        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
        System.out.println(calendar);
        System.out.println(calendar.get(Calendar.HOUR_OF_DAY)*10000+calendar.get(Calendar.MINUTE)*100+calendar.get(Calendar.SECOND));
    }
}
