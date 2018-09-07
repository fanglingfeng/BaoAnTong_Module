package com.tjsoft.util;


import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
/**
 * @author Li JinFeng
 * @date 2010-05-07
 * @version 1.0
 * 字母 日期或时间元素 
 * G Era
 * y 年
 * M 年中的月份 
 * w 年中的周数 
 * W 月份中的周数 
 * D 年中的天数 
 * d 月份中的天数 
 * F 月份中的星期 
 * E 星期中的天数 
 * a Am/pm 标记 
 * H 一天中的小时数（0-23）
 * k 一天中的小时数（1-24） 
 * K am/pm 中的小时数（0-11） 
 * h am/pm 中的小时数（1-12） 
 * m 小时中的分钟数 
 * s 分钟中的秒数 
 * S 毫秒数
 * z 时区 
 * Z 时区 
*/

public final class DateUtils {
	/**系统默认日期格式**/
	public static final String SYSTEM_DATE_FORMAT = "yyyy-MM-dd";
	/**系统中文日期格式**/
	public static final String SYSTEM_DATE_FORMAT_CN = "yyyy年MM月dd日";
	/**系统默认时间格式**/
	public static final String SYSTEM_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	/**系统中文时间格式**/
	public static final String SYSTEM_DATETIME_FORMAT_CN = "yyyy年MM月dd日 HH:mm:ss";
	/**
     * 获取现在的日期
     * @return 时间类型：yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate(String formater) {
    	if(null == formater){
    		formater = DateUtils.SYSTEM_DATE_FORMAT;
    	}
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat(formater);
        String dateString = format.format(currentTime);
        return dateString;
    }
	/**
     * 获取现在的时间
     * @return 时间类型：yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDateTime(String formater) {
    	if(null == formater){
    		formater = DateUtils.SYSTEM_DATETIME_FORMAT;
    	}
        Date currentTime = new Date();
        SimpleDateFormat format = new SimpleDateFormat(formater);
        String dateString = format.format(currentTime);
        return dateString;
    }
    /**
     * 得到现在小时 08 表示当前是八点
     * @return 小时
     */
    public static String getHour() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        String hour = dateString.substring(11, 13);
        return hour;
    }
     
     /**
     * 得到现在分钟
     * @return 分钟
     */
    public static String getTime() {
	     Date currentTime = new Date();
	     SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	     String dateString = formatter.format(currentTime);
	     String min = dateString.substring(14, 16);
	     return min;
     }
    
     /**
      * 得到二个日期间的间隔天数
      * @param sj1 字符串时间格式
      * @param sj2 字符串时间格式
      * @return
      */
     public static long getTwoDayString(String sj1, String sj2) {
         SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
         long day = 0;
         try {
	         java.util.Date date = myFormatter.parse(sj1);
	         java.util.Date mydate = myFormatter.parse(sj2);
	         day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return day;
     }
     /**
      * 得到二个日期间的间隔天数
      * @param sj1 字符串时间格式
      * @param sj2 字符串时间格式
      * @return
      */
     public static long getTwoDay(Date sj1, Date sj2) {
         long day = 0;
         try {
	         day = (sj1.getTime() - sj2.getTime()) / (24 * 60 * 60 * 1000);
         } catch (Exception e) {
             e.printStackTrace();
         }
         return day;
     }
     
     /**
     * 时间前推或后推分钟,其中JJ表示分钟.
     * 调用示例：System.out.println(getPreTime("2010-06-30 10:50:20","10"));
     */
     public static String getPreTime(String sj1, String jj) {
         SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
         String mydate = "";
         try {
	         Date date = format.parse(sj1);
	         long Time = (date.getTime() / 1000) + Integer.parseInt(jj) * 60;
	         date.setTime(Time * 1000);
	         mydate = format.format(date);
         } catch (Exception e) {
             System.out.println(e.toString());
         }
         return mydate;
     }
     /**
     * 判断是否润年
     *
     * @param ddate
     * @return
     * 调用格式为：System.out.println(isLeapYear("2005-01-01"));
     */
     public static boolean isLeapYear(String ddate) {
         /**
         * 详细设计： 
         * 1.被400整除是闰年，否则平年： 
         * 2.不能被4整除则不是闰年 
         * 3.能被4整除同时不能被100整除则是闰年
         * 4.能被4整除同时能被100整除则不是闰年
         */
         Date d = strToDate(ddate);
         GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
         gc.setTime(d);
         int year = gc.get(Calendar.YEAR);
         if ((year % 400) == 0)
             return true;
         else if ((year % 4) == 0) {
             if ((year % 100) == 0)
             return false;
             else
             return true;
         } else
             return false;
     }
     /**
      * 将短时间格式字符串转换为时间 yyyy-MM-dd
      * @param strDate
      * @return
      */
     public static Date strToDate(String strDate) {
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         ParsePosition pos = new ParsePosition(0);
         Date strtodate = formatter.parse(strDate, pos);
         return strtodate;
    }
     /**
      * 将短时间格式字符串转换为时间 yyyy-MM-dd
      * @param strDate
      * @return
      */
     public static String dateToStr(Date date) {
         SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
         String strtodate = formatter.format(date);
         return strtodate;
    }
     /**
     * 获取一个月的最后一天
     *
     * @param dat
     * @return
     * 调用示例：System.out.println(getEndDateOfMonth("2005-02-01"));28
     */
     public static String getEndDateOfMonth(String dat) {// yyyy-MM-dd

         String str = dat.substring(0, 8);
         String month = dat.substring(5, 7);
         int mon = Integer.parseInt(month);
         if (mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) {
         str += "31";
         } else if (mon == 4 || mon == 6 || mon == 9 || mon == 11) {
         str += "30";
         } else {
         if (isLeapYear(dat)) {
         str += "29";
         } else {
         str += "28";
         }
         }
         return str;
     }
     /**
     * 判断二个时间是否在同一个周
     *
     * @param date1
     * @param date2
     * @return
     * Date a = strToDate("2010-05-02");
         Date b = strToDate("2010-05-01");
         if(isSameWeekDates(a,b)) {
             System.out.println("yes");
         } else {
             System.out.println("no");
         }
         要注意一点：周日与周一是划到同一周了
     */
     public static boolean isSameWeekDates(Date date1, Date date2) {
         Calendar cal1 = Calendar.getInstance();
         Calendar cal2 = Calendar.getInstance();
         cal1.setTime(date1);
         cal2.setTime(date2);
         int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
         if (0 == subYear) {
         if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
         return true;
         } else if (1 == subYear && 11 == cal2.get(Calendar.MONTH)) {
         // 如果12月的最后一周横跨来年第一周的话则最后一周即算做来年的第一周

         if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
         return true;
         } else if (-1 == subYear && 11 == cal1.get(Calendar.MONTH)) {
         if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
         return true;
         }
         return false;
     }
     /**
     * 产生周序列,即得到当前时间所在的年度是第几周
     * 这个方法不错可以知道当前时间是当年的第几周
     * @return
     */
     public static String getSeqWeek() {
     Calendar c = Calendar.getInstance(Locale.CHINA);
     String week = Integer.toString(c.get(Calendar.WEEK_OF_YEAR));
     if (week.length() == 1)
     week = "0" + week;
     String year = Integer.toString(c.get(Calendar.YEAR));
     return year + week;
     }
     
     /**
     * 获得一个日期所在的周的星期几的日期，如要找出2002年2月3日所在周的星期一是几号
     *
     * @param sdate
     * @param num
     * @return
     * System.out.println(getWeek("2010-05-01","1")); 表示2010-05-01所在这一周中星期一是几号
     */
     public static String getWeek(String sdate, String num) {
    	// 再转换为时间
	    Date dd = DateUtils.strToDate(sdate);
	    Calendar c = Calendar.getInstance();
	    c.setTime(dd);
	    // 返回星期一所在的日期
	    if (num.equals("1")){
	    	c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	    } else if (num.equals("2")){ // 返回星期二所在的日期
	    	c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
	    }else if (num.equals("3")){ // 返回星期三所在的日期
	    	c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
	    }else if (num.equals("4")){// 返回星期四所在的日期
		    c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
	    }else if (num.equals("5")){ // 返回星期五所在的日期
	    	c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
	    }else if (num.equals("6")){ // 返回星期六所在的日期
	    	c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	    }else if (num.equals("0")){ // 返回星期日所在的日期
	    	c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	    }
	    return new SimpleDateFormat("yyyy-MM-dd").format(c.getTime());
     }
     
     /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
     public static String getWeek(String sdate) {
         // 再转换为时间
         Date date = DateUtils.strToDate(sdate);
         Calendar c = Calendar.getInstance();
         c.setTime(date);
         // int hour=c.get(Calendar.DAY_OF_WEEK);
         // hour中存的就是星期几了，其范围 1~7
         // 1=星期日 7=星期六，其他类推
         return new SimpleDateFormat("EEEE").format(c.getTime());
     }
     /**
      * 获取星期几的名称
      * @param sdate
      * @return
      */
     public static String getWeekName(String sdate){
         String str = "";
         str = DateUtils.getWeek(sdate);
         if("1".equals(str)){
        	 str = "星期日";
         }else if("2".equals(str)){
        	 str = "星期一";
         }else if("3".equals(str)){
        	 str = "星期二";
         }else if("4".equals(str)){
        	 str = "星期三";
         }else if("5".equals(str)){
        	 str = "星期四";
         }else if("6".equals(str)){
        	 str = "星期五";
         }else if("7".equals(str)){
        	 str = "星期六";
         }
         return str;
     }
     /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     * System.out.println(getDays("2010-06-02","2010-05-02"));
     */
     public static long getDays(String date1, String date2) {
	     if (date1 == null || date1.equals(""))
	     return 0;
	     if (date2 == null || date2.equals(""))
	     return 0;
	     // 转换为标准时间
	
	     SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
	     java.util.Date date = null;
	     java.util.Date mydate = null;
	     try {
	     date = myFormatter.parse(date1);
	     mydate = myFormatter.parse(date2);
	     } catch (Exception e) {
	     }
	     long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
	     return day;
     }
 	/**
 	 * 当前月份所属季度
 	 * @return
 	 */
 	public static int getCurrentSeason(int month) {
 		int season = 1;
 		if (month >= 1 && month <= 3) {
 			season = 1;
 		}
 		if (month >= 4 && month <= 6) {
 			season = 2;
 		}
 		if (month >= 7 && month <= 9) {
 			season = 3;
 		}
 		if (month >= 10 && month <= 12) {
 			season = 4;
 		}
 		return season;
 	}
 	/**
	 * 获取某年某月的最后一天
	 * 
	 * @param year
	 *            年
	 * @param month
	 *            月
	 * @return 最后一天
	 */
	public static int getLastDayOfMonth(int year, int month) {
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			return 31;
		}
		if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		}
		if (month == 2) {
			if (isLeapYear(year)) {
				return 29;
			} else {
				return 28;
			}
		}
		return 0;
	}
	/**
	 * 是否闰年
	 * 
	 * @param year
	 *            年
	 * @return
	 */
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}
    
	public static String getTimeInMillis(Date sDate, Date eDate){
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(sDate); 
		long timethis = calendar.getTimeInMillis();  
		
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(eDate); 
		long timeend = calendar2.getTimeInMillis(); 
		long thedaymillis = timeend-timethis; 
		return thedaymillis < 1000 ? thedaymillis + "毫秒!" : (thedaymillis/1000) + "秒!";
	}
}
