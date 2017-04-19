package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * @author dolphin 2016年7月20日 下午2:13:07
 */
public class DateUtil {

	public static final String DEFAULT_FORMAT_STYLE = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_FORMAT_STYLE_NOSPACE = "yyyy-MM-dd_HH:mm:ss";
	public static final String DEFAULT_MINUTE_STYLE = "yyyy-MM-dd HH:mm:ss";
	public static final String DEFAULT_MINUTE_STYLE_NOSPACE = "yyyy-MM-dd_HH:mm";
	public static final String DEFAULT_HOUR_STYLE = "yyyy-MM-dd HH";
	public static final String DEFAULT_HOUR_STYLE_NOSPACE = "yyyy-MM-dd_HH";
	public static final String DEFAULT_HOUR_STYLE_NOYEAR = "MM-dd HH:mm";
	public static final String DEFAULT_ONLYHOUR_STYLE = "HH:mm";
	public static final String DEFAULT_DAY_STYLE = "yyyy-MM-dd";
	public static final String DEFAULT_DAY_STYLE_NOYEAR = "MM-dd";
	public static final String DEFAULT_DAY_STYLE_NOSPACE = "yyyyMMdd";
	public static final String DEFAULT_MILLISECOND_STYLE = "yyyy-MM-dd HH:mm:ss:SSS";
	public static final String DEFAULT_MILLISECOND_STYLE_1 = "yyyy-MM-dd HH:mm:ss.SSS";
	public static final String DEFAULT_ONLYSEC_STYLE = "mm:ss";
	public static final String DEFAULT_ONLY_HOUR_STYLE = "HH";
	public static final String DEFAULT_YEAR_DAY_STYLE = "yyyyMM";

	public static List<Date> fetchAllHours(String day) throws ParseException {

		Date startDate = DateUtil.parseDate(day, DateUtil.DEFAULT_DAY_STYLE);

		List<Date> results = new ArrayList<Date>();

		results.add(startDate);

		for (int i = 1; i < 24; i++) {
			results.add(DateUtil.after(i, startDate, TimeUnit.HOURS));
		}

		return results;
	}

	public static int diff(Date startDate, Date endDate, TimeUnit timeUnit) {
		long diff = endDate.getTime() - startDate.getTime();
		if (timeUnit == TimeUnit.SECONDS) {
			return (int) (diff / 1000);
		} else if (timeUnit == TimeUnit.MINUTES) {
			return (int) (diff / 1000 / 60);
		} else if (timeUnit == TimeUnit.HOURS) {
            return (int) (diff / 1000 / 60 / 60);
        } else if (timeUnit == TimeUnit.DAYS) {
            return (int) (diff / 1000 / 60 / 60 / 24);
			
		} else {
			throw new RuntimeException("only support second/minute/hour!");
		}
	}

	public static List<Date> getBetweenMin(Date start, Date end)
			throws ParseException {
		if (!start.before(end)) {
			return null;
		}
		start = DateUtil.format2Min(start);
		List<Date> list = new ArrayList<Date>();
		while (start.before(end)) {
			list.add(start);
			start = after(1, start, TimeUnit.MINUTES);
		}
		return list;
	}

	public static List<Date> getBetweenHours(Date start, Date end) {
		if (!start.before(end)) {
			return null;
		}
		try {
			start = DateUtil.format2Hour(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Date> list = new ArrayList<Date>();
		while (start.before(end)) {
			list.add(start);
			start = after(1, start, TimeUnit.HOURS);
		}
		return list;
	}

	public static List<Date> getBetweenDays(Date start, Date end)
			throws ParseException {
		if (!start.before(end)) {
			return null;
		}
		List<Date> list = new ArrayList<Date>();
		Date date = format2Day(start);
		while (!date.after(end)) {
			list.add(date);
			date = after(1, date, TimeUnit.DAYS);
		}
		return list;
	}

	public static List<Date> getPastHours(int hours) throws ParseException {
		Date d = format2Hour(before(hours, HOURS));
		List<Date> pastHours = new ArrayList<Date>();
		for (int i = 0; i < hours; i++) {
			pastHours.add(d);
			d = before(-1, d, HOURS);
		}
		return pastHours;
	}

	public static List<Date> getPast15Days() throws ParseException {
		Date d = format2Day(before(15, DAYS));
		List<Date> past15Days = new ArrayList<Date>();
		for (int i = 0; i < 15; i++) {
			past15Days.add(d);
			d = before(-1, d, DAYS);
		}
		return past15Days;
	}

	/**
	 * @return
	 * @throws ParseException
	 */
	public static List<Date> getPast30Minutes() throws ParseException {
		Date d = format2Min(before(30, MINUTES));
		List<Date> past30Minutes = new ArrayList<Date>();
		for (int i = 0; i < 30; i++) {
			past30Minutes.add(d);
			d = before(-1, d, MINUTES);
		}
		return past30Minutes;
	}

	public static List<Date> getPast12Hours() throws ParseException {
		Date d = format2Hour(before(12, HOURS));
		List<Date> past12Hours = new ArrayList<Date>();
		for (int i = 0; i < 12; i++) {
			past12Hours.add(d);
			d = before(-1, d, HOURS);
		}
		return past12Hours;
	}

	public static String formatDate(Date date) {
		return formatDate(date, DEFAULT_FORMAT_STYLE);
	}

	public static String formatDate(Date date, String formatStyle) {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
		return sdf.format(date);
	}

	public static Date parseDate(String dateString) {
		try {
			return parseDate(dateString, DEFAULT_FORMAT_STYLE);
		} catch (ParseException e) {
			return null;
		}
	}

	public static Date parseDate(long ldate) {
		Date d = new Date(ldate);
		return d;
	}

	public static Date parse(String longTime) {
		try {
			long ltime = Long.parseLong(longTime);
			return new Date(ltime);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date parseDate(String dateString, String formatStyle)
			throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formatStyle);
		return sdf.parse(dateString);
	}

	public static Date before(int times, TimeUnit timeUnit) {
		return before(times, new Date(), timeUnit);
	}

	public static Date before(int times, Date date, TimeUnit timeUnit) {
		long d = date.getTime() / 1000;
		if (timeUnit == SECONDS) {
			return new Date((d - times) * 1000);
		} else if (timeUnit == MINUTES) {
			return new Date((d - times * 60) * 1000);
		} else if (timeUnit == HOURS) {
			return new Date((d - times * 60 * 60) * 1000);
		} else if (timeUnit == DAYS) {
			return new Date((d - times * 60 * 60 * 24) * 1000);
		}
		throw new RuntimeException(
				"only support TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS!");
	}

	public static Date after(int times, Date date, TimeUnit timeUnit) {
		long d = date.getTime() / 1000;
		if (timeUnit == SECONDS) {
			return new Date((d + times) * 1000);
		} else if (timeUnit == MINUTES) {
			return new Date((d + times * 60) * 1000);
		} else if (timeUnit == HOURS) {
			return new Date((d + times * 60 * 60) * 1000);
		} else if (timeUnit == DAYS) {
			return new Date((d + times * 60 * 60 * 24) * 1000);
		}
		throw new RuntimeException(
				"only support TimeUnit.SECONDS, TimeUnit.MINUTES, TimeUnit.HOURS, TimeUnit.DAYS !");
	}

	public static Date getTomorrow() {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) + 1);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		return rightNow.getTime();
	}

	public static Date getMorning() {
		return getMorning(new Date());
	}

	public static Date getYesterday() {
		return getMorning(DateUtil.before(1, TimeUnit.DAYS));
	}

	public static Date getMorning(Date d) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(d);

		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		return rightNow.getTime();
	}

	public static Date getDateWithOffset(int offset) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) + offset);
		rightNow.set(Calendar.HOUR_OF_DAY, 0);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		return rightNow.getTime();
	}

	public static Date getHourWithOffset(int offset) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.set(Calendar.HOUR_OF_DAY, rightNow.get(Calendar.HOUR_OF_DAY)
				+ offset);
		rightNow.set(Calendar.MINUTE, 0);
		rightNow.set(Calendar.SECOND, 0);
		rightNow.set(Calendar.MILLISECOND, 0);
		return rightNow.getTime();
	}

	public static Date addSeconds(Date date, long secs) {
		return new Date(date.getTime() + (secs * 1000));
	}

	public static Date addHours(Date date, long hours) {
		return new Date(date.getTime() + (hours * 60 * 60 * 1000));
	}
    
    public static Date addDays(Date date, long days) {
        return new Date(date.getTime() + (days * 24 * 60 * 60 * 1000));
    }
	public static Date addMonth(Date date, int month) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + month);
		return calendar.getTime();
	}

	/**
	 * 获取当前时间的月份
	 * @param date
	 * @return
	 */
	public static String currentTimeOfMonth(Date date){
		if(date==null)
			return null;
		Calendar cal = Calendar.getInstance();  
		cal.setTime(date);
		return new SimpleDateFormat("MM").format(cal.getTime());
	}
	
	/**
	 * @param d
	 * @return
	 */
	public static List<String> roundOneMinute(Date d) {
		return roundOneMinute(d, DEFAULT_FORMAT_STYLE_NOSPACE);
	}

	/**
	 * @param d
	 * @param formatStyle
	 * @return
	 */
	public static List<String> roundOneMinute(Date d, String formatStyle) {
		List<String> dateStringList = new ArrayList<String>(60);

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		for (int i = 0; i < 60; i++) {
			calendar.set(Calendar.SECOND, i);
			dateStringList.add(formatDate(calendar.getTime(), formatStyle));
		}

		return dateStringList;
	}

	public static String formatDay(Date d) {
		return formatDate(d, DEFAULT_DAY_STYLE);
	}

	public static Date format2Hour(Date date) throws ParseException {
		return parseDate(formatDate(date, DEFAULT_HOUR_STYLE),
				DEFAULT_HOUR_STYLE);
	}

	public static Date format2Min(Date date) throws ParseException {
		return parseDate(formatDate(date, DEFAULT_MINUTE_STYLE),
				DEFAULT_MINUTE_STYLE);
	}

	public static Date format2Second(Date date) throws ParseException {
		return parseDate(formatDate(date, DEFAULT_FORMAT_STYLE),
				DEFAULT_FORMAT_STYLE);
	}

	public static Date format2Day(Date d) throws ParseException {
		return parseDate(formatDate(d), DEFAULT_DAY_STYLE);
	}

	public static long getSystemTime() {
		return System.currentTimeMillis();
	}

	public static int getDifDay(Date date1,Date date2) {
		int day = 0;
		if(date1==null||date2==null){
			return day;
		}
		try {
			date1 = DateUtil.parseDate(DateUtil.formatDate(date1, DEFAULT_DAY_STYLE)+" 00:00:00", DateUtil.DEFAULT_MINUTE_STYLE);
			date2=DateUtil.parseDate(DateUtil.formatDate(date2, DEFAULT_DAY_STYLE)+" 00:00:00", DateUtil.DEFAULT_MINUTE_STYLE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		double dif=0;
		if(date1.after(date2)){
			dif=date1.getTime()-date2.getTime();
		}else{
			dif=date2.getTime()-date1.getTime();
		}
		day=Double.valueOf(dif/1000/60/60/24).intValue();
		return day;
	}
	/**上个月第一天*/
	public static Date getPreMonthFirstDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.MONTH, -1);    //加一个月
        String day_first = formatDate(calendar.getTime(),DateUtil.DEFAULT_DAY_STYLE);
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();
        Date re=null;
        try {
			re=parseDate(day_first, DateUtil.DEFAULT_MINUTE_STYLE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return re;
	}
	
	/**上个月第一天*/
	public static Date getPreMonthFirstDay(Date date,String dateFormat){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.MONTH, -1);    //加一个月
        String day_first = formatDate(calendar.getTime(),DateUtil.DEFAULT_DAY_STYLE);
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();
        Date re=null;
        try {
//			re=parseDate(day_first, DateUtil.DEFAULT_MINUTE_STYLE);
        	re=parseDate(day_first, dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return re;
	}
	
	/**上个月最后一天*/
	public static Date getPreMonthLastDay(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//上个月最后一天
        calendar.add(Calendar.MONTH, 0);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = formatDate(calendar.getTime(),DateUtil.DEFAULT_DAY_STYLE);
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();
		Date re=null;
		try {
			re=parseDate(day_last, DateUtil.DEFAULT_MINUTE_STYLE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re;
	}
	
	/**上个月最后一天*/
	public static Date getPreMonthLastDay(Date date,String dateFormat){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		//上个月最后一天
        calendar.add(Calendar.MONTH, 0);    //加一个月
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        calendar.add(Calendar.DATE, -1);    //再减一天即为上个月最后一天
        String day_last = formatDate(calendar.getTime(),DateUtil.DEFAULT_DAY_STYLE);
        StringBuffer endStr = new StringBuffer().append(day_last).append(" 23:59:59");
        day_last = endStr.toString();
		Date re=null;
		try {
//			re=parseDate(day_last, DateUtil.DEFAULT_MINUTE_STYLE);
			re=parseDate(day_last, dateFormat);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return re;
	}
	
	/**把字符串格式化成日期
	 * MM/dd/yyyy to Date
	 * */
	public static Date stringToDate(String beginTimeStr,int type) {
    	if(beginTimeStr==null||"".equals(beginTimeStr.trim())){
    		return null;
    	}
    	if(type==0){//开始时间
    		beginTimeStr=beginTimeStr+" 00:00:00";
    	}else if(type==1){
    		beginTimeStr=beginTimeStr+" 23:59:59";
    	}
    	Date date=null;
    	try {
    		date=DateUtil.parseDate(beginTimeStr, "MM/dd/yyyy HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	/**把字符串格式化成日期字符串
	 * MM/dd/yyyy to Date
	 * */
	public static String stringToDateStr(String beginTimeStr,int type) {
        if(beginTimeStr==null||"".equals(beginTimeStr.trim())){
            return null;
        }
        if(type==0){//开始时间
    		beginTimeStr=beginTimeStr+" 00:00:00";
    	}else if(type==1){
    		beginTimeStr=beginTimeStr+" 23:59:59";
    	}
        String dateStr=null;
        try {
        	Date date=DateUtil.parseDate(beginTimeStr, "MM/dd/yyyy HH:mm:ss");
        	dateStr=DateUtil.formatDate(date, DateUtil.DEFAULT_MINUTE_STYLE);
        } catch (ParseException e) {
        	e.printStackTrace();
        }
        return dateStr;
    }


	
	   /**
     * 获取当前日期的指定几周前的周一日期
     * @param
     * @return
     */
    public static Date getWeekMonday(Integer interval) {   
        if(interval < 0){
            return null;
        }
        Calendar calendar = Calendar.getInstance();//当前时间
        System.out.println(calendar);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);//在日期上减一天
        }
        System.out.println(calendar);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        System.out.println(dayOfWeek);
        int offset = 1 - dayOfWeek;
        calendar.add(Calendar.DATE, offset - 7*interval);
        String day_first = formatDate(calendar.getTime(),DateUtil.DEFAULT_DAY_STYLE) + " 00:00:00";
        Date re=null;
        try {
             re=parseDate(day_first, DateUtil.DEFAULT_MINUTE_STYLE);
         } catch (ParseException e) {
             e.printStackTrace();
         }
        return re;   
   }
    
    public static Date getMondayOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            calendar.add(Calendar.DAY_OF_WEEK, -1);//在日期上减一天
        }
        return calendar.getTime();
    }
    
    public static Date getSundayOfDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);//在日期上减一天
        }
        return calendar.getTime();
    }
    
    public static Date getAgoMondayOfDate(Date date, int inteval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intevalDay = inteval*7;
        calendar.add(Calendar.DAY_OF_YEAR, -intevalDay);
        return getMondayOfDate(calendar.getTime());
    }
    
    public static Date getAgoSundayOfDate(Date date, int inteval) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intevalDay = inteval*7;
        calendar.add(Calendar.DAY_OF_YEAR, -intevalDay);
        return getSundayOfDate(calendar.getTime());
    }
    
    /**指定月第一天*/
	public static Date getMonthFirstDay(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        calendar.set(Calendar.DATE, 1);        //设置为该月第一天
        String day_first = formatDate(calendar.getTime(),DateUtil.DEFAULT_DAY_STYLE);
        StringBuffer str = new StringBuffer().append(day_first).append(" 00:00:00");
        day_first = str.toString();
        Date re=null;
        try {
			re=parseDate(day_first, DateUtil.DEFAULT_MINUTE_STYLE);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        return re;
	}

	public static void main(String[] args) throws ParseException {
		// System.out.println(formatDate(getYesterday()));
		Date date1 = DateUtil.parseDate("2016-10-18 00:00:00");
		Date date2 = DateUtil.parseDate("2016-10-18 23:59:59");
//		while (date2.after(date1)) {
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
//			String Cmonth = sdf.format(date1);
//			System.out.println(Cmonth.toString());
//			date1 = DateUtil.addMonth(date1, 1);
//		}
//        System.out.println(formatDate(getPreMonthFirstDay(date1),DateUtil.DEFAULT_MINUTE_STYLE));
//        
//        System.out.println(formatDate(getPreMonthLastDay(date1),DateUtil.DEFAULT_MINUTE_STYLE));
//		System.out.println(getLastWeekSunday(new Date()));
//		System.out.println(getWeekMonday(2));
//	    Date date = parseDate("2016-01-2", "yyyy-MM-dd");
//	    Calendar c = Calendar.getInstance();
//	    c.setTime(date);
//	    System.out.println(getMondayOfDate(new Date()));
//	    System.out.println(getSundayOfDate(new Date()));
//	    System.out.println(getAgoMondayOfDate(new Date(), 2));
//	    System.out.println(getAgoSundayOfDate(new Date(), 2));
//	    try{
//	    JSONObject obj=new JSONObject();
//	    obj.put("a", "1");
//	    obj.put("a", "2");
//	    System.out.println(obj.toString());
//	    }catch(Exception e){
//	        
//	    }
	    System.out.println(getBetweenDays(date1, date2).size());
	}
}
