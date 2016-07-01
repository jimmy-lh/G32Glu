package com.bioland.activity.remind;

import java.util.ArrayList;

import com.bioland.db.bean.AlarmValue;
import com.bioland.db.bean.GluValue;
import com.bioland.db.dao.AlarmValueDao;
import com.bioland.g32glu.R;
import com.bioland.singleton.SingletonApplication;
import com.bioland.utils.BaseActivity;
import com.bioland.utils.CloseActivityReceiver;
import com.bioland.utils.LogUtil;
import com.bioland.view.timeselect.SelListener;
import com.bioland.view.timeselect.TimeSelectDayView;
import com.bioland.view.timeselect.TimeSelectHourView;
import com.bioland.view.timeselect.TimeSelectMinuteView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetTimeActivity extends BaseActivity implements OnClickListener {
	private ImageView mReturnImage;
	private Button mFinishButton;
	private TimeSelectHourView timeHourView;
	private TimeSelectMinuteView timeMinuteView;

	private final String ACTION_NAME = "发送增加提醒时间的广播";
	private final String INTENT_TIME_DAY = "intent_time_day";
	private final String INTENT_TIME_HOUR = "intent_time_hour";
	private final String INTENT_TIME_MINUTE = "intent_time_minute";

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, SetTimeActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().getDecorView().setSystemUiVisibility(10); // 平板全屏
		setContentView(R.layout.activity_remind_time);
		initView();
		initTimeHourSelect();
		initTimeMinuteSelect();
	}

	private void initView() {
		mReturnImage = (ImageView) findViewById(R.id.arrow_return);
		mFinishButton = (Button) findViewById(R.id.btn_remind_finish);

		mReturnImage.setOnClickListener(this);
		mFinishButton.setOnClickListener(this);
	}

	private void initTimeHourSelect() {
		timeHourView = (TimeSelectHourView) findViewById(R.id.timeHourView);
		ArrayList<String> lst = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			lst.add("0" + i);
		}
		for (int i = 10; i < 24; i++) {
			lst.add("" + i);
		}
		timeHourView.init(lst);
		timeHourView.onSelectListener(new SelListener() {

			@Override
			public void onClick(String text) {
				// Toast.makeText(SetTimeActivity.this, text, 0).show();
			}
		});
	}

	private void initTimeMinuteSelect() {
		timeMinuteView = (TimeSelectMinuteView) findViewById(R.id.timeMinuteView);
		ArrayList<String> lst = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			lst.add("0" + i);
		}
		for (int i = 10; i < 60; i++) {
			lst.add("" + i);
		}
		timeMinuteView.init(lst);
		timeMinuteView.onSelectListener(new SelListener() {

			@Override
			public void onClick(String text) {
				// Toast.makeText(SetTimeActivity.this, text, 0).show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回上一层天数设置界面
		case R.id.arrow_return:
			SetDayFrequencyActivity.actionStart(this, 0);
			finish();
			break;
		// 退出当前菜单，回到添加提醒界面
		case R.id.btn_remind_finish:
			Intent mIntent = new Intent(ACTION_NAME);
			mIntent.putExtra(INTENT_TIME_DAY, TimeSelectDayView.mDataContext);
			mIntent.putExtra(INTENT_TIME_HOUR, TimeSelectHourView.mDataContext);
			mIntent.putExtra(INTENT_TIME_MINUTE, TimeSelectMinuteView.mDataContext);

			// 发送广播
			sendBroadcast(mIntent);

			finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 插入试纸对应的按键响应
		if (keyCode == KeyEvent.KEYCODE_MUTE) {
			Intent intent = new Intent(CloseActivityReceiver.CLOSE_ACTIVITY);
			sendBroadcast(intent);
		}
		return super.onKeyDown(keyCode, event);
	}
}
