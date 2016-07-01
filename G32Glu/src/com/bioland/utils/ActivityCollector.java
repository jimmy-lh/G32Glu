package com.bioland.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;

/**
 * 用于管理Activity类
 * BaseActivity，ActivityCollector,CloseActivityReceiver一起管理Activity类
 * 
 * @author miracle 2016年5月19日
 */
public class ActivityCollector {
	public static List<Activity> activities = new ArrayList<Activity>();

	public static void addActivity(Activity activity) {
		activities.add(activity);
	}

	public static void removeActivity(Activity activity) {
		activities.remove(activity);
	}

	public static void finishAll() {
		for (Activity activity : activities) {
			if (!activity.isFinishing()) {
				activity.finish();
			}
		}
	}
}
