package com.bioland.utils;

import android.app.Activity;
import android.os.Bundle;

/**
 * 让所有Activity类继承，从而可以统一管理
 * BaseActivity，ActivityCollector,CloseActivityReceiver一起管理Activity类
 * 
 * @author miracle 2016年5月19日
 */
public class BaseActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActivityCollector.addActivity(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		ActivityCollector.removeActivity(this);
	}
}
