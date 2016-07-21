package com.bioland.widget.popup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.bioland.activity.remind.AlarmActivity;
import com.bioland.activity.remind.SetDayFrequencyActivity;
import com.bioland.adapter.BaseAdapterItem;
import com.bioland.adapter.remind.RemindAddTimeBaseAdapter;
import com.bioland.db.bean.AlarmValue;
import com.bioland.db.dao.AlarmValueDao;
import com.bioland.g32glu.R;
import com.bioland.singleton.SingletonApplication;
import com.bioland.utils.LogUtil;
import com.bioland.utils.record.GapDateUtil;
import com.bioland.view.timeselect.TimeSelectDayView;
import com.bioland.view.timeselect.TimeSelectHourView;
import com.bioland.view.timeselect.TimeSelectMinuteView;
import com.bioland.widget.sharedpreferences.MyPreference;

import android.R.integer;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class RemindPopup extends MyPopup implements OnClickListener {

	private Context context;
	private Button mAddButton;
	private TextView mTextRemindHint;

	// 时间
	private int[] mImageViewOnOff = { R.drawable.remind_on, R.drawable.remind_off };
	private String[] mTextViewTime = new String[] {};
	private String[] mTextViewFrequency = new String[] {};
	// 数据库相关参数
	AlarmValueDao mAlarmValueDao;
	private int mAlarmCount = 0;
	// 保存获取到的数据库全部数据
	private List<AlarmValue> alarmValueAll;
	/**
	 * ListView相关参数 ;ListView的适配器。
	 */
	private RemindAddTimeBaseAdapter mAdapter;
	private List<BaseAdapterItem> mList;
	private ListView mListView;

	private final String ACTION_NAME = "发送增加提醒时间的广播";
	private final String INTENT_TIME_DAY = "intent_time_day";
	private final String INTENT_TIME_HOUR = "intent_time_hour";
	private final String INTENT_TIME_MINUTE = "intent_time_minute";

	private final String TAG = "RemindPopup";

	public RemindPopup(Context context) {
		// super((Activity) context, 650);
		super((Activity) context, 620);
		this.context = context;
		// 注册广播
		registerBoradcastReceiver();
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
		view = inflater.inflate(R.layout.popup_remind, null);

		view.setFocusableInTouchMode(true);

		initData();
	}

	private void view() {
		mAddButton = (Button) view.findViewById(R.id.btn_remind_add);
		mAddButton.setOnClickListener(this);

		mTextRemindHint = (TextView) view.findViewById(R.id.txt_remind_hint);
	}

	private void initListView(View view) {
		// 关于历史数据的listView的配置
		mList = new ArrayList<BaseAdapterItem>();
		mListView = (ListView) view.findViewById(R.id.lv_remind_add_view);
		mAdapter = new RemindAddTimeBaseAdapter(context, mList, view);
		mListView.setAdapter(mAdapter);
		getSaveDate();
	}

	// 获取数据库数据，获取SharePreference数据
	private void getSaveDate() {
		// 获取数据库alarm操作类
		mAlarmValueDao = SingletonApplication.getAlarmValueDao();
		alarmValueAll = mAlarmValueDao.queryAll();
		// 此处要判断list是否为空,否则报错
		if (alarmValueAll.size() > 0) {
			for (int i = 0; i < alarmValueAll.size(); i++) {
				// 获取最新数据加入listView
				if (alarmValueAll.get(i).isSetting()) {
					mList.add(0, new BaseAdapterItem(mImageViewOnOff[0], alarmValueAll.get(i).getTime(),
							alarmValueAll.get(i).getDate(), alarmValueAll.get(i).getCount()));
				} else {
					mList.add(0, new BaseAdapterItem(mImageViewOnOff[1], alarmValueAll.get(i).getTime(),
							alarmValueAll.get(i).getDate(), alarmValueAll.get(i).getCount()));
				}
			}
			mAlarmCount = Integer.valueOf(alarmValueAll.get(alarmValueAll.size() - 1).getCount());
			LogUtil.e(TAG, "mAlarmCount == " + mAlarmCount);
			// 刷新listView
			mAdapter.notifyDataSetChanged();
			mTextRemindHint.setVisibility(View.GONE);
		} else {
			LogUtil.e(TAG, "数据库没有数据...");
		}
	}

	// 设置数据库的实体类,并保存
	private void saveAlarmDB() {
		AlarmValue alarmValue = new AlarmValue("设置频率:" + TimeSelectDayView.mDataContext,
				TimeSelectHourView.mDataContext + ":" + TimeSelectMinuteView.mDataContext, true,
				String.valueOf(mAlarmCount), "", "", "", "", "", "", "", "", "", "");
		mAlarmValueDao.insert(alarmValue);
	}

	// 增加新的提醒时间
	private void addRemindView(Intent intent) {
		mAlarmCount++;
		saveAlarmDB();
		// 获取最新数据加入listView
		mList.add(0,
				new BaseAdapterItem(mImageViewOnOff[0],
						intent.getStringExtra(INTENT_TIME_HOUR) + ":" + intent.getStringExtra(INTENT_TIME_MINUTE),
						"设置频率:" + intent.getStringExtra(INTENT_TIME_DAY), String.valueOf(mAlarmCount)));
		// 刷新listView
		mAdapter.notifyDataSetChanged();
		setAlarmClock(Integer.parseInt(TimeSelectHourView.mDataContext),
				Integer.parseInt(TimeSelectMinuteView.mDataContext));

		if (mTextRemindHint.getVisibility() != View.GONE)
			mTextRemindHint.setVisibility(View.GONE);
	}

	AlarmManager aManager;
	MediaPlayer alarmMusic;

	private void setAlarmClock(int hourOfDay, int minute) {
		// 获取AlarmManager对象
		aManager = (AlarmManager) context.getSystemService(Service.ALARM_SERVICE);
		// 指定启动AlarmActivity组件
		Intent intent = new Intent(context, AlarmActivity.class);
		// 创建PendingIntent对象
		PendingIntent pi = PendingIntent.getActivity(context, 0, intent, 0);
		Calendar c = Calendar.getInstance();
		// 根据用户选择时间来设置Calendar对象
		c.set(Calendar.HOUR, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		// 设置AlarmManager将在Calendar对应的时间启动指定组件
		aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
		// 显示闹铃设置成功的提示信息
		Toast.makeText(context, "闹铃设置成功啦.." + hourOfDay + ":" + minute, Toast.LENGTH_SHORT).show();
//		alarmMusic = MediaPlayer.create(context, R.raw.alarm);
//		alarmMusic.setLooping(true);
//		// 播放音乐
//		alarmMusic.start();
//		// 创建一个对话框
//		new AlertDialog.Builder(context).setTitle("闹钟")
//		.setMessage("闹钟响了,Go！Go！Go！")
//		.setPositiveButton("确定", new DialogInterface.OnClickListener()
//		{
//			@Override
//			public void onClick(DialogInterface dialog, int which)
//			{
//				// 停止音乐
//				alarmMusic.stop();
//				// 结束该Activity
////				AlarmActivity.this.finish();
//			}
//		}).show();
	}

	// 广播接收
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_NAME)) {
				// 增加新的提醒时间
				addRemindView(intent);
			}
		}
	};

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);
		// 注册广播
		((Activity) context).registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	/**
	 * 按钮点击事件监听
	 * 
	 * @param v
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_remind_add:
			SetDayFrequencyActivity.actionStart(context, 0);
			break;
		}
	}
}
