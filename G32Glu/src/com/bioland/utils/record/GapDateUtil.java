package com.bioland.utils.record;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.bioland.utils.LogUtil;

import android.annotation.SuppressLint;

public class GapDateUtil {
	private static final String TAG = "GapDate";

	public GapDateUtil() {
		super();
	}

	// 返回当前日期前n天的日期
	public String getBeforeDate(int n) {
		String date = "";
		Calendar calendar = Calendar.getInstance();
		// 此处一定要记得转换为long型
		Date aDate = new Date(calendar.getTimeInMillis() - ((long) n * (1000 * 60 * 60 * 24)));
		calendar.setTime(aDate);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// month与day小于10则在前面增加0，如2016-4-3日则显示为“2016-04-03”
		if (month < 10) {
			if (day < 10) {
				date = "0" + String.valueOf(month) + "/" + "0" + String.valueOf(day);
			} else {
				date = "0" + String.valueOf(month) + "/" + String.valueOf(day);
			}
		} else {
			if (day < 10) {
				date = String.valueOf(month) + "/" + "0" + String.valueOf(day);
			} else {
				date = String.valueOf(month) + "/" + String.valueOf(day);
			}
		}
		return date;
	}

	// 返回当前日期前n天的日期
	public String getBeforeHideDate(int n) {
		String hideDate = "";
		Calendar calendar = Calendar.getInstance();
		// 此处一定要记得转换为long型
		Date aDate = new Date(calendar.getTimeInMillis() - ((long) n * (1000 * 60 * 60 * 24)));
		calendar.setTime(aDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		// month与day小于10则在前面增加0，如2016-4-3日则显示为“2016-04-03”
		if (month < 10) {
			if (day < 10) {
				hideDate = String.valueOf(year) + "-" + "0" + String.valueOf(month) + "-" + "0" + String.valueOf(day);
			} else {
				hideDate = String.valueOf(year) + "-" + "0" + String.valueOf(month) + "-" + String.valueOf(day);
			}
		} else {
			if (day < 10) {
				hideDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + "0" + String.valueOf(day);
			} else {
				hideDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
			}
		}
		return hideDate;
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @return
	 */
	public int getGapDate(String start, String end) {
		Date startDate = getDate(start);
		Date endDate = getDate(end);
		return getGapCount(startDate, endDate);
	}

	// String对象转换为Date对象
	@SuppressLint("SimpleDateFormat")
	private Date getDate(String date) {
		try {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
			Date aDate = dateFormatter.parse(date);
			return aDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取两个日期之间的间隔天数
	 * 
	 * @return
	 */
	private static int getGapCount(Date startDate, Date endDate) {
		Calendar fromCalendar = Calendar.getInstance();
		fromCalendar.setTime(startDate);

		Calendar toCalendar = Calendar.getInstance();
		toCalendar.setTime(endDate);

		return (int) ((toCalendar.getTimeInMillis() - fromCalendar.getTimeInMillis()) / (1000 * 60 * 60 * 24));
	}

	private final static String BEFORE_BREAKFAST = "0";// 早餐前
	private final static String AFTER_BREAKFAST = "1";// 早餐后
	private final static String BEFORE_LUNCH = "2";// 中餐前
	private final static String AFTER_LUNCH = "3";// 中餐后
	private final static String BEFORE_DINNER = "4";// 晚餐前
	private final static String AFTER_DINNER = "5";// 晚餐后
	private final static String BEFORE_SLEEP = "6";// 睡觉前
	private String showCurrentDate;
	private String hideCurrentDate;
	private String timePeriod;

	// 获取时间并判断属于那段时间
	public void getSystemTime() {
		// 获取当前时间，并且设置时间显示格式
		// SimpleDateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm
		// ");
		// String str = mFormatter.format(new Date());
		// LogUtil.e(TAG, "...." + str);

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		// month与day小于10则在前面增加0，如2016-4-3日则显示为“2016-04-03”
		if (month < 10) {
			if (day < 10) {
				showCurrentDate = "0" + String.valueOf(month) + "/" + "0" + String.valueOf(day);
				hideCurrentDate = String.valueOf(year) + "-" + "0" + String.valueOf(month) + "-" + "0"
						+ String.valueOf(day);
			} else {
				showCurrentDate = "0" + String.valueOf(month) + "/" + String.valueOf(day);
				hideCurrentDate = String.valueOf(year) + "-" + "0" + String.valueOf(month) + "-" + String.valueOf(day);
			}
		} else {
			if (day < 10) {
				showCurrentDate = String.valueOf(month) + "/" + "0" + String.valueOf(day);
				hideCurrentDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + "0" + String.valueOf(day);
			} else {
				showCurrentDate = String.valueOf(month) + "/" + String.valueOf(day);
				hideCurrentDate = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);
			}
		}
		if (hour < 8) {
			timePeriod = BEFORE_BREAKFAST;
		} else if (hour < 9) {
			timePeriod = AFTER_BREAKFAST;
		} else if (hour < 12) {
			timePeriod = BEFORE_LUNCH;
		} else if (hour < 13) {
			timePeriod = AFTER_LUNCH;
		} else if (hour < 18) {
			timePeriod = BEFORE_DINNER;
		} else if (hour < 19) {
			timePeriod = AFTER_DINNER;
		} else {
			timePeriod = BEFORE_SLEEP;
		}
	}

	public String getShowCurrentDate() {
		return showCurrentDate;
	}

	public String getHideCurrentDate() {
		return hideCurrentDate;
	}

	public String getTimePeriod() {
		return timePeriod;
	}

}
