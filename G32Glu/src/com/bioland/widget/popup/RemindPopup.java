package com.bioland.widget.popup;

import java.util.ArrayList;
import java.util.List;

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

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
//		super((Activity) context, 650);
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

		if (mTextRemindHint.getVisibility() != View.GONE)
			mTextRemindHint.setVisibility(View.GONE);
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
