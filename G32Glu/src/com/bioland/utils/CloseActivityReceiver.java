package com.bioland.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 关闭Activity类，BaseActivity，ActivityCollector,CloseActivityReceiver一起管理Activity类
 * 
 * @author miracle 2016年5月19日
 */
public class CloseActivityReceiver extends BroadcastReceiver {

	private final String TAG = "CloseActivityReceiver";
	public final static String CLOSE_ACTIVITY = "com.my.close.activity";

	@Override
	public void onReceive(final Context context, Intent intent) {
		String action = intent.getAction();
		if (action.equals(CLOSE_ACTIVITY)) {
			LogUtil.e(TAG, "广播 close activity ，接收成功");
			ActivityCollector.finishAll(); // 销毁所有活动
		}
	}
}
