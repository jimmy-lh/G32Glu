package com.bioland.widget.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences存储简化方法类
 * 
 * @author Administrator
 *
 */
public class MyPreference {
	public static final String IS_FIRST = "is_first";
	public static final String ERROR_CODE = "error_code";
	// share数据保存
	private final String SHARED_PREFERENCE_NAME = "miracle.G32Glu";
	private static MyPreference catche;
	private SharedPreferences spf;

	public static MyPreference instance(Context context) {
		if (catche == null) {
			catche = new MyPreference(context);
		}
		return catche;
	}

	public static MyPreference getInstance() {
		if (catche != null) {
			return catche;
		}
		return null;
	}

	public MyPreference(Context context) {
		spf = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
	}

	public void putBoolean(String key, boolean value) {
		spf.edit().putBoolean(key, value).commit();
	}

	public boolean getBoolean(String key) {
		return spf.getBoolean(key, false);
	}

	public boolean getBoolean(String key, Boolean defaultValue) {
		return spf.getBoolean(key, defaultValue);
	}

	public void putString(String key, String value) {
		spf.edit().putString(key, value).commit();
	}

	public String getString(String key) {
		return spf.getString(key, "");
	}

	public void putInt(String key, int value) {
		spf.edit().putInt(key, value).commit();
	}

	public void putLong(String key, long value) {
		spf.edit().putLong(key, value).commit();
	}

	public int getInt(String key) {
		return spf.getInt(key, 0);
	}

	public int getInt(String key, int defaultValue) {
		return spf.getInt(key, defaultValue);
	}

	public long getLong(String key) {
		return spf.getLong(key, 0);
	}

	public long getLong(String key, long def) {
		return spf.getLong(key, def);
	}

	public void clearData() {
		spf.edit().clear().commit();
	}

	public void remove(String key) {
		spf.edit().remove(key).commit();
	}

	public void commit() {
		spf.edit().commit();
	}

}
