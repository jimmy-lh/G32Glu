package com.bioland.widget.popup;

import com.bioland.activity.wlan.WLanActivity;
import com.bioland.g32glu.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MorePopup extends MyPopup implements OnClickListener {

	private Context context;
	LinearLayout twoInclude, wlanInclude, systemInclude, updateInclude, recoverInclude, feedbackInclude, aboutInclude;

	public MorePopup(Context context) {
//		super((Activity) context, 650);
		super((Activity) context, 620);
		this.context = context;
		initView();
		view();
	}

	/**
	 * 初始化弹窗组件
	 */
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		// 引入窗口配置文件 - 即弹窗的界面
		view = inflater.inflate(R.layout.popup_more, null);

		view.setFocusableInTouchMode(true);
		initData();
	}

	private void view() {
		twoInclude = (LinearLayout) view.findViewById(R.id.include_more_two_dimension);
		wlanInclude = (LinearLayout) view.findViewById(R.id.include_more_wlan);
		systemInclude = (LinearLayout) view.findViewById(R.id.include_more_system);
		updateInclude = (LinearLayout) view.findViewById(R.id.include_more_update);
		recoverInclude = (LinearLayout) view.findViewById(R.id.include_more_recover);
		feedbackInclude = (LinearLayout) view.findViewById(R.id.include_more_feedback);
		aboutInclude = (LinearLayout) view.findViewById(R.id.include_more_about);

		twoInclude.setOnClickListener(this);
		wlanInclude.setOnClickListener(this);
		systemInclude.setOnClickListener(this);
		updateInclude.setOnClickListener(this);
		recoverInclude.setOnClickListener(this);
		feedbackInclude.setOnClickListener(this);
		aboutInclude.setOnClickListener(this);
	}

	/**
	 * 按钮点击事件监听
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.include_more_two_dimension:
			Toast.makeText(context, "two dimension code", Toast.LENGTH_SHORT).show();
			break;
		case R.id.include_more_wlan:
			WLanActivity.actionStart(context, 0);
			Toast.makeText(context, "wlan", Toast.LENGTH_SHORT).show();
			break;
		case R.id.include_more_system:
			Toast.makeText(context, "system", Toast.LENGTH_SHORT).show();
			break;
		case R.id.include_more_update:
			Toast.makeText(context, "update", Toast.LENGTH_SHORT).show();
			break;
		case R.id.include_more_recover:
			Toast.makeText(context, "recover", Toast.LENGTH_SHORT).show();
			break;
		case R.id.include_more_feedback:
			Toast.makeText(context, "feedback", Toast.LENGTH_SHORT).show();
			break;
		case R.id.include_more_about:
			Toast.makeText(context, "about", Toast.LENGTH_SHORT).show();
			break;
		}
	}

}
