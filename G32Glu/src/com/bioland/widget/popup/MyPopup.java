package com.bioland.widget.popup;

import com.bioland.g32glu.R;
import com.bioland.utils.LogUtil;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public abstract class MyPopup {

	private Activity activity;
	PopupWindow pop;
	TextView hideView;
	View view;
	int height;
	private final static String TAG = "MyPopup";

	public MyPopup(Activity activity, int height) {
		super();
		this.activity = activity;
		this.height = height;
	}

	/**
	 * 初始化数据
	 */
	protected void initData() {
		hideView = (TextView) activity.findViewById(R.id.hideView);

		// PopupWindow实例化
		pop = new PopupWindow(view, LayoutParams.MATCH_PARENT, height, true);
		/**
		 * PopupWindow 设置
		 */
		pop.setFocusable(false); // 设置PopupWindow可获得焦点,设置为false则非PopupWindow可以产生点击事件
		// pop.setTouchable(true); // 设置PopupWindow可触摸
		// pop.setOutsideTouchable(true); // 设置非PopupWindow区域可触摸
		// 设置PopupWindow显示和隐藏时的动画
		pop.setAnimationStyle(R.style.MenuAnimationFade);
		/**
		 * 点击窗口外弹出窗口消失,改变背景可拉的弹出窗口。后台可以设置为null。 这句话必须有，否则按返回键
		 * 
		 * popwindow不能消失 或者加入这句话 ColorDrawable dw = new
		 * ColorDrawable(-00000);pop.setBackgroundDrawable(dw);
		 */
		// pop.setBackgroundDrawable(new BitmapDrawable());

	}

	/**
	 * 改变 PopupWindow 的显示和隐藏
	 */
	public void changePopupWindowState() {
		if (pop.isShowing()) {
			// 隐藏窗口，如果设置了点击窗口外消失，则不需要此方式隐藏
			pop.dismiss();
		} else {
			// 弹出窗口显示内容视图,默认以锚定视图的左下角为起点，这里为点击按钮
			pop.showAtLocation(hideView, Gravity.BOTTOM, 0, 0);
		}
	}
}
