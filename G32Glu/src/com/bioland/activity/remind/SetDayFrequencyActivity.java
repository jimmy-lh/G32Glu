package com.bioland.activity.remind;

import java.util.ArrayList;

import com.bioland.g32glu.R;
import com.bioland.utils.BaseActivity;
import com.bioland.utils.CloseActivityReceiver;
import com.bioland.view.timeselect.SelListener;
import com.bioland.view.timeselect.TimeSelectDayView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class SetDayFrequencyActivity extends BaseActivity implements OnClickListener {

	private ImageView mReturnImage;
	private Button mNextButton;
	public String frequency[] = new String[] { "1天1次", "2天1次", "3天1次", "5天1次", "7天1次" };
	private TimeSelectDayView timeDayView;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, SetDayFrequencyActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().getDecorView().setSystemUiVisibility(10); // 平板全屏
		setContentView(R.layout.activity_remind_day);
		initView();
		initFrequency();
	}

	private void initFrequency() {

		timeDayView = (TimeSelectDayView) findViewById(R.id.timeDayView);
		ArrayList<String> lst = new ArrayList<String>();
		for (int i = 0; i < frequency.length; i++) {
			lst.add(frequency[i]);
		}
		timeDayView.init(lst);
		timeDayView.onSelectListener(new SelListener() {

			@Override
			public void onClick(String text) {
				Toast.makeText(SetDayFrequencyActivity.this, text, 0).show();
			}
		});

	}

	private void initView() {
		mReturnImage = (ImageView) findViewById(R.id.arrow_return);
		mNextButton = (Button) findViewById(R.id.btn_remind_next);

		mReturnImage.setOnClickListener(this);
		mNextButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.arrow_return:
			finish();
			break;
		case R.id.btn_remind_next:
			SetTimeActivity.actionStart(this, 0);
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
