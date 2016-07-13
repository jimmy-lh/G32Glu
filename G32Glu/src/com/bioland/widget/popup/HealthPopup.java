package com.bioland.widget.popup;

import com.bioland.adapter.health.HealthSetAdapter;
import com.bioland.g32glu.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class HealthPopup extends MyPopup implements OnClickListener {

	private Context context;

	private HealthSetAdapter mSetAdapter;
	private TextView mTextViewHealthHint1, mTextViewHealthHint2;

	// 时间
	private int[] mImageViewHealthIcon = { R.drawable.health_item_icon, R.drawable.health_item_icon,
			R.drawable.health_item_icon, R.drawable.health_item_icon, R.drawable.health_item_icon,
			R.drawable.health_item_icon };
	private String[] mTextViewHint1 = new String[] { "首先，让我们认识下你", "糖友应避免认识的误区", "什么时候测血糖", "完成一次血糖检测", "你现在的病情如何",
			"如何获得最佳的疗效" };
	private String[] mTextViewHint2 = new String[] { "以便能为你提供更针对的指导", "七戒：过度依赖医生和药物", "凌晨3点血糖", "你已超过7天没有测试血糖",
			"权威题库帮你更好的了解自己", "糖尿病治疗的“五架马车" };

	public HealthPopup(Context context) {
//		super((Activity) context, 470);
		super((Activity) context, 440);
		this.context = context;
		initView();
		view();
		initListView(view);
	}

	/**
	 * 初始化弹窗组件
	 */
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		// 引入窗口配置文件 - 即弹窗的界面
		view = inflater.inflate(R.layout.popup_health, null);

		view.setFocusableInTouchMode(true);
		initData();
	}

	private void view() {
		mTextViewHealthHint1 = (TextView) ((Activity) context).findViewById(R.id.txt_health_hint1);
		mTextViewHealthHint2 = (TextView) ((Activity) context).findViewById(R.id.txt_health_hint2);
	}

	// 初始化listview
	private void initListView(View view) {
		mSetAdapter = new HealthSetAdapter(view, ((Activity) context), R.id.lv_health, mImageViewHealthIcon,
				mTextViewHint1, mTextViewHint2);
	}

	// 增加新的提醒时间
	// private void addRemindView(Intent intent) {
	//
	// // 获取最新数据加入listView
	// mSetAdapter.mList.add(0, new BaseAdapterItem(mImageViewHealthIcon[0],
	// intent.getStringExtra("1") + ":" + intent.getStringExtra("1"), "设置频率:" +
	// intent.getStringExtra("1")));
	// // 刷新listView
	// mSetAdapter.mAdapter.notifyDataSetChanged();
	// }

	/**
	 * 按钮点击事件监听
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// case R.id.btn_health_test:
		// mTextViewHealthHint1.setText("ceshi jieguo");
		// break;
		default:
			break;
		}
	}

}
