package com.bioland.singleton;

import com.bioland.db.dao.AlarmValueDao;
import com.bioland.db.dao.GluValueDao;

import android.content.Context;
import android.provider.Settings.Secure;

public class SingletonApplication {

	private volatile static SingletonApplication instance = null;
	public static Context contexts;
	// 机器码
	private static String MachineCode = null;
	// 数据库参数
	private static GluValueDao gluValueDao;
	private static AlarmValueDao alarmValueDao;

	private SingletonApplication() {
	};

	public static SingletonApplication getInstance(Context context) {
		if (instance == null) {
			synchronized (SingletonApplication.class) {
				if (instance == null) {
					instance = new SingletonApplication();
					contexts = context;
					instance.setMachineCode();
					instance.setGluValueDao();
					instance.setAlarmValueDao();
				}
			}
		}
		return instance;
	}

	public static String getMachineCode() {
		return MachineCode;
	}

	// 设置机器码
	private void setMachineCode() {
		MachineCode = Secure.getString(contexts.getContentResolver(), Secure.ANDROID_ID);
	}

	/**
	 * 数据库相关方法
	 * 
	 * @return
	 */
	public static GluValueDao getGluValueDao() {
		return gluValueDao;
	}

	private void setGluValueDao() {
		gluValueDao = new GluValueDao(contexts);
	}

	public static AlarmValueDao getAlarmValueDao() {
		return alarmValueDao;
	}

	private void setAlarmValueDao() {
		alarmValueDao = new AlarmValueDao(contexts);
	}

}
