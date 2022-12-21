package com.myapi.common.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	
	public static final String DELIMETER = "/";
	
	/**
     * 取得目前日期時間
     *
     * @return EX:2010/01/01 14:34:52
     */
    public static String getCurrentDateTime() {
        return new SimpleDateFormat("yyyy" + DELIMETER + "MM" + DELIMETER + "dd HH:mm:ss").format(new Date());
    }

	public static String addMinuteForSystemDate(int minute, String formate) {
        SimpleDateFormat sdf = new SimpleDateFormat(formate);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE, minute);
        return sdf.format(cal.getTime());
    }
}
