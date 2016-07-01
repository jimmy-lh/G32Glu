package com.bioland.utils.record;

import com.bioland.g32glu.R;

import android.content.Context;

public class TextStatusUtil {
	private Context context;

	public TextStatusUtil(Context context) {
		super();
		this.context = context;
	}

	public int getColor(String str) {
		int color = 0;
		if (Float.parseFloat(str) - 8 > 0) {
			// 血糖过高
			color = getHighColor();
		} else if (Float.parseFloat(str) - 4 < 0) {
			// 血糖过低
			color = getLowColor();
		} else {
			// 血糖正常
			color = getNormalColor();
		}
		return color;
	}

	public int getNormalColor() {
		return 0xFF0072BB;
	}

	public int getHighColor() {
		return 0xFFCC0000;
	}

	public int getLowColor() {
		return 0xFFEEC900;
	}

	public String getTextStatus(String str) {
		String result;
		if (Float.parseFloat(str) - 8 > 0) {
			// 血糖过高
			result = context.getResources().getString(R.string.record_details_value_status_high);
		} else if (Float.parseFloat(str) - 4 < 0) {
			// 血糖过低
			result = context.getResources().getString(R.string.record_details_value_status_low);
		} else {
			// 血糖正常
			result = context.getResources().getString(R.string.record_details_value_status_normal);
		}
		return result;
	}

	public String getTextReason(String str) {
		String result;
		if (Float.parseFloat(str) - 8 > 0) {
			// 血糖过高
			result = context.getResources().getString(R.string.record_details_value_status_high_reason);
		} else if (Float.parseFloat(str) - 4 < 0) {
			// 血糖过低
			result = context.getResources().getString(R.string.record_details_value_status_low_reason);
		} else {
			// 血糖正常
			result = context.getResources().getString(R.string.record_details_value_status_normal);
		}
		return result;
	}

	public boolean getIsNormal(String str) {
		if ((Float.parseFloat(str) - 4 >= 0) && (Float.parseFloat(str) - 8 <= 0)) {
			// 血糖值正常
			return true;
		} else {
			return false;
		}
	}
}
