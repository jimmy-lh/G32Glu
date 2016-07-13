package com.bioland.widget.popup;

import com.bioland.g32glu.R;
import com.bioland.utils.LogUtil;
import com.bioland.view.measure.InsertTestPaperUtil;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

public class MeasurePopup extends MyPopup implements OnClickListener {

	private Context context;

	private final static String TAG = "MeasurePopup";

	private InsertTestPaperUtil mInsertTestPaperUtil;

	public MeasurePopup(Context context) {
		// super((Activity) context, 270);
		super((Activity) context, 240);
		this.context = context;
		initView();
		initMainUi();
	}

	/**
	 * 初始化弹窗组件
	 */
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		// 引入窗口配置文件 - 即弹窗的界面
		view = inflater.inflate(R.layout.popup_measure, null);

		view.setFocusableInTouchMode(true);
		initData();
	}

	// 主界面的ui
	private void initMainUi() {
		mInsertTestPaperUtil = new InsertTestPaperUtil(context);
	}

	/**
	 * 按钮点击事件监听
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		}
	}

	/**
	 * 改变 PopupWindow 的显示和隐藏
	 */
	public void changePopupWindowState() {
		if (pop.isShowing()) {
			if (isInsertDemon) {
				exitInsertDemon();
			}
			if (isStaxisDemon) {
				exitStaxisDemon();
			}
			// 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
			pop.dismiss();
		} else {
			if (isInsert) {
				showStaxisDemon();
			} else {
				showInsertDemon();
			}
			// 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
			pop.showAtLocation(hideView, Gravity.BOTTOM, 0, 0);
		}
	}

	// 显示演示插入试纸动画
	public void showInsertDemon() {
		isInsertDemon = true;
		mInsertTestPaperUtil.showPaperUi();
	}

	// 退出演示插入试纸动画
	public void exitInsertDemon() {
		isInsertDemon = false;
		mInsertTestPaperUtil.exitPaperUi();
	}

	public boolean isInsertDemon = false;// 是否正在滴血演示动画界面
	public boolean isStaxisDemon = false;// 是否正在滴血演示动画界面
	public boolean isInsert = false;// 是否插入试纸

	// 显示演示滴血动画
	public void showStaxisDemon() {
		isStaxisDemon = true;
		mInsertTestPaperUtil.showInsertStaxis();
	}

	// 退出演示滴血动画
	public void exitStaxisDemon() {
		isStaxisDemon = false;
		mInsertTestPaperUtil.exitInsertStaxis();
	}

}
