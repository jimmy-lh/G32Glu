package com.bioland.widget.popup;

import java.util.ArrayList;
import java.util.List;

import com.bioland.activity.record.RecordDetailsActivity;
import com.bioland.db.bean.GluValue;
import com.bioland.db.dao.GluValueDao;
import com.bioland.entity.expandable.Group;
import com.bioland.entity.expandable.People;
import com.bioland.g32glu.R;
import com.bioland.singleton.SingletonApplication;
import com.bioland.utils.LogUtil;
import com.bioland.utils.record.GapDateUtil;
import com.bioland.utils.record.TextStatusUtil;
import com.bioland.view.expandable.PinnedHeaderExpandableListView;
import com.bioland.view.expandable.PinnedHeaderExpandableListView.OnHeaderUpdateListener;
import com.bioland.view.roundprogressbar.RoundProgressBar;
import com.bioland.widget.sharedpreferences.MyPreference;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.LayoutParams;

/**
 * 记录
 * 
 * @author Administrator
 *
 */
public class RecordPopup extends MyPopup implements ExpandableListView.OnChildClickListener,
		ExpandableListView.OnGroupClickListener, OnHeaderUpdateListener {

	private Context context;

	private PinnedHeaderExpandableListView expandableListView;
	private ArrayList<Group> groupList;
	private ArrayList<List<People>> childList;

	private MyexpandableListAdapter adapter;

	private final static String TAG = "RecordPopup";

	// roundProgress参数
	private RoundProgressBar mRoundProgressBar1, mRoundProgressBar2;
	private int normalCount, highCount, lowCount;
	private String strNormalCount = "00", strHighCount = "00", strLowCount = "00";

	// 数据库参数
	private GluValueDao mGluValueDao;
	// preference存储
	private MyPreference mPreference;
	// 加载动画的线程相关参数
	private LoadingThread mLoadingThread = null;
	private ProgressBar mProgressLoading;
	// 日期相关类
	private GapDateUtil mGapDate;

	public RecordPopup(Context context) {
//		super((Activity) context, 650);
		super((Activity) context, 620);
		this.context = context;
		initView();
		getSaveDate();
		initExpandableView();
		// 获取时间并判断属于那段时间
		// getSystemTime();
		// saveUserDB("12.5", "2016-03-15", "3", "");
		// saveUserDB("6.5", "2016-03-15", "5", "");
		// saveUserDB("2.5", "2016-03-18", "2", "");
		// saveUserDB("12.5", "2016-03-20", "5", "");
		// saveUserDB("8.5", "2016-03-20", "3", "");
		// saveUserDB("12.5", "2016-04-10", "6", "");
		// saveUserDB("7.5", "2016-04-10", "4", "");
		// saveUserDB("12.5", "2016-04-15", "0", "");
		// saveUserDB("9.5", "2016-04-15", "3", "");
		// saveUserDB("4.5", "2016-04-19", "3", "");
		// saveUserDB("22.5", mGapDate.getHideCurrentDate(),
		// mGapDate.getShowCurrentDate(), "5");
	}

	/**
	 * 初始化弹窗组件
	 */
	private void initView() {
		LayoutInflater inflater = LayoutInflater.from(context);
		// 引入窗口配置文件 - 即弹窗的界面
		view = inflater.inflate(R.layout.popup_record, null);

		view.setFocusableInTouchMode(true);
		initData();
		initUi();
	}

	private void initUi() {
		mProgressLoading = (ProgressBar) view.findViewById(R.id.record_progress_loading);
	}

	private void initExpandableView() {
		expandableListView = (PinnedHeaderExpandableListView) view.findViewById(R.id.expandablelist);
		initExpandableData();

		adapter = new MyexpandableListAdapter(context);
		expandableListView.setAdapter(adapter);
		// 从第i个开始，展开所有group
		// for (int i = 1, count = expandableListView.getCount(); i < count;
		// i++) {
		// expandableListView.expandGroup(i);
		// }
		expandableListView.setOnHeaderUpdateListener(this);
		expandableListView.setOnChildClickListener(this);
		expandableListView.setOnGroupClickListener(this, false);
	}

	/***
	 * InitData,初始化listGroup和listChild
	 */
	void initExpandableData() {
		groupList = new ArrayList<Group>();
		Group group = null;
		for (int i = 0; i < 2; i++) {
			group = new Group();
			group.setNormal(strNormalCount);
			group.setHighSide(strHighCount);
			group.setLowSide(strLowCount);
			groupList.add(group);
		}

		childList = new ArrayList<List<People>>();
		for (int i = 0; i < groupList.size(); i++) {
			ArrayList<People> childTemp;
			// 第一个group的child为空，第二个group的child为1时会出错
			if (i == 0) {
				childTemp = new ArrayList<People>();
				for (int j = 0; j < 1; j++) {
					People people = new People();
					people.setDate("yy-" + j);
					people.setHideDate("yy-" + j);

					childTemp.add(people);
				}
			} else {
				childTemp = new ArrayList<People>();
				for (int j = 0; j <= dayCount; j++) {
					People people = new People();
					people.setDate(mGapDate.getBeforeDate(j));
					people.setHideDate(mGapDate.getBeforeHideDate(j));

					childTemp.add(people);
				}
			}
			childList.add(childTemp);
		}
	}

	// 保存获取到的数据库全部数据
	private List<GluValue> gluValueAll;
	// 数据库第一个和最后一个数据的日期，以及现在的日期
	private int dayCount = 0;

	// 获取数据库数据，获取SharePreference数据
	private void getSaveDate() {
		// 获取SharePreference数据;正常，偏高，偏低，这几个状态都有几次结果
		mPreference = MyPreference.getInstance();
		// 获取数据库表
		mGluValueDao = SingletonApplication.getGluValueDao();
		gluValueAll = mGluValueDao.queryAll();
		// 初始化自定义日期类
		mGapDate = new GapDateUtil();
		// 此处要判断list是否为空,否则报错
		if (gluValueAll.size() > 0) {
			// 获取第一次测量到现在的总天数
			mGapDate.getSystemTime();
			dayCount = mGapDate.getGapDate(gluValueAll.get(0).getDate(), mGapDate.getHideCurrentDate());
			LogUtil.e(TAG, "dayCount = " + dayCount);
		} else {
			LogUtil.e(TAG, "数据库没有数据...");
		}
	}

	// value值小于10时，前面补“0”，使显示更好看
	private String getStringShow(int value) {
		String str;
		if (value < 10) {
			str = "0" + String.valueOf(value);
		} else {
			str = String.valueOf(value);
		}
		return str;
	}

	/***
	 * 数据源
	 * 
	 * @author Administrator
	 * 
	 */
	class MyexpandableListAdapter extends BaseExpandableListAdapter {
		private Context context;
		private LayoutInflater inflater;
		TextStatusUtil mColorUtils = new TextStatusUtil(context);

		public MyexpandableListAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
		}

		// 返回父列表个数
		@Override
		public int getGroupCount() {
			return groupList.size();
		}

		// 返回子列表个数
		@Override
		public int getChildrenCount(int groupPosition) {
			return childList.get(groupPosition).size();
		}

		@Override
		public Object getGroup(int groupPosition) {

			return groupList.get(groupPosition);
		}

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return childList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {

			return true;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			GroupHolder groupHolder = null;
			if (convertView == null) {
				groupHolder = new GroupHolder();
				convertView = inflater.inflate(R.layout.record_expandable_group, null);
				groupHolder.layout1 = (LinearLayout) convertView.findViewById(R.id.layout_record_group1);
				groupHolder.layout2 = (LinearLayout) convertView.findViewById(R.id.layout_record_group2);
				convertView.setTag(groupHolder);
			} else {
				groupHolder = (GroupHolder) convertView.getTag();
			}

			// 分别设置group需要显示和隐藏的界面
			if (groupPosition == 0) {
				groupHolder.layout1.setVisibility(View.VISIBLE);
				groupHolder.layout2.setVisibility(View.GONE);
			} else if (groupPosition == 1) {
				groupHolder.layout1.setVisibility(View.GONE);
				groupHolder.layout2.setVisibility(View.VISIBLE);
			}
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ChildHolder childHolder = null;
			if (convertView == null) {
				childHolder = new ChildHolder();
				convertView = inflater.inflate(R.layout.record_expandable_child, null);

				childHolder.textView0 = (TextView) convertView.findViewById(R.id.txt_record_child_hide_time);
				childHolder.textView1 = (TextView) convertView.findViewById(R.id.txt_record_child_time);
				childHolder.textView2 = (TextView) convertView.findViewById(R.id.txt_record_child_beforebreakfast);
				childHolder.textView3 = (TextView) convertView.findViewById(R.id.txt_record_child_afterbreakfast);
				childHolder.textView4 = (TextView) convertView.findViewById(R.id.txt_record_child_beforelunch);
				childHolder.textView5 = (TextView) convertView.findViewById(R.id.txt_record_child_afterlunch);
				childHolder.textView6 = (TextView) convertView.findViewById(R.id.txt_record_child_beforedinner);
				childHolder.textView7 = (TextView) convertView.findViewById(R.id.txt_record_child_afterdinner);
				childHolder.textView8 = (TextView) convertView.findViewById(R.id.txt_record_child_sleep);

				convertView.setTag(childHolder);
			} else {
				childHolder = (ChildHolder) convertView.getTag();
			}

			childHolder.textView0.setText(((People) getChild(groupPosition, childPosition)).getHideDate());
			childHolder.textView1.setText(((People) getChild(groupPosition, childPosition)).getDate());

			// 取出数据库中对应日期的数据并显示
			List<GluValue> gluValues = mGluValueDao.searchDate(childHolder.textView0.getText().toString());
			// 处理上下滚动时重复显示
			childHolder.textView2.setText("");
			childHolder.textView3.setText("");
			childHolder.textView4.setText("");
			childHolder.textView5.setText("");
			childHolder.textView6.setText("");
			childHolder.textView7.setText("");
			childHolder.textView8.setText("");
			// 显示对应的数值
			if (gluValues.size() > 0) {
				for (int i = 0; i < gluValues.size(); i++) {
					String result = gluValues.get(i).getResult();
					switch (Integer.parseInt(gluValues.get(i).getTimePeriod())) {
					case 0:
						childHolder.textView2.setTextColor(mColorUtils.getColor(result));
						childHolder.textView2.setText(result);
						childHolder.textView2
								.setOnClickListener(new onRecordDetailsListener(childPosition, childHolder));
						break;
					case 1:
						childHolder.textView3.setTextColor(mColorUtils.getColor(result));
						childHolder.textView3.setText(result);
						childHolder.textView3
								.setOnClickListener(new onRecordDetailsListener(childPosition, childHolder));
						break;
					case 2:
						childHolder.textView4.setTextColor(mColorUtils.getColor(result));
						childHolder.textView4.setText(result);
						childHolder.textView4
								.setOnClickListener(new onRecordDetailsListener(childPosition, childHolder));
						break;
					case 3:
						childHolder.textView5.setTextColor(mColorUtils.getColor(result));
						childHolder.textView5.setText(result);
						childHolder.textView5
								.setOnClickListener(new onRecordDetailsListener(childPosition, childHolder));
						break;
					case 4:
						childHolder.textView6.setTextColor(mColorUtils.getColor(result));
						childHolder.textView6.setText(result);
						childHolder.textView6
								.setOnClickListener(new onRecordDetailsListener(childPosition, childHolder));
						break;
					case 5:
						childHolder.textView7.setTextColor(mColorUtils.getColor(result));
						childHolder.textView7.setText(result);
						childHolder.textView7
								.setOnClickListener(new onRecordDetailsListener(childPosition, childHolder));
						break;
					case 6:
						childHolder.textView8.setTextColor(mColorUtils.getColor(result));
						childHolder.textView8.setText(result);
						childHolder.textView8
								.setOnClickListener(new onRecordDetailsListener(childPosition, childHolder));
						break;

					default:
						break;
					}
				}
			}

			return convertView;
		}

		class onRecordDetailsListener implements OnClickListener {
			private int position;
			private ChildHolder childHolder;

			public onRecordDetailsListener(int position, ChildHolder childHolder) {
				this.position = position;
				this.childHolder = childHolder;
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.txt_record_child_beforebreakfast:
					if (childHolder.textView2.getText() != "") {
						RecordDetailsActivity.actionStart(context, childHolder.textView0.getText().toString(), 0);
					}
					break;
				case R.id.txt_record_child_afterbreakfast:
					if (childHolder.textView3.getText() != "") {
						RecordDetailsActivity.actionStart(context, childHolder.textView0.getText().toString(), 1);
					}
					break;
				case R.id.txt_record_child_beforelunch:
					if (childHolder.textView4.getText() != "") {
						RecordDetailsActivity.actionStart(context, childHolder.textView0.getText().toString(), 2);
					}
					break;
				case R.id.txt_record_child_afterlunch:
					if (childHolder.textView5.getText() != "") {
						RecordDetailsActivity.actionStart(context, childHolder.textView0.getText().toString(), 3);
					}
					break;
				case R.id.txt_record_child_beforedinner:
					if (childHolder.textView6.getText() != "") {
						RecordDetailsActivity.actionStart(context, childHolder.textView0.getText().toString(), 4);
					}
					break;
				case R.id.txt_record_child_afterdinner:
					if (childHolder.textView7.getText() != "") {
						RecordDetailsActivity.actionStart(context, childHolder.textView0.getText().toString(), 5);
					}
					break;
				case R.id.txt_record_child_sleep:
					if (childHolder.textView8.getText() != "") {
						RecordDetailsActivity.actionStart(context, childHolder.textView0.getText().toString(), 6);
					}
					break;
				default:
					break;
				}
			}
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	@Override
	public boolean onGroupClick(final ExpandableListView parent, final View v, int groupPosition, final long id) {

		return true;
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Toast.makeText(context, childList.get(groupPosition).get(childPosition).getDate(), Toast.LENGTH_SHORT).show();

		return false;
	}

	class GroupHolder {
		LinearLayout layout1;
		LinearLayout layout2;
		TextView textView1;
		TextView textView2;
		TextView textView3;
		ImageView imageView;
	}

	class ChildHolder {
		TextView textView0;
		TextView textView1;
		TextView textView2;
		TextView textView3;
		TextView textView4;
		TextView textView5;
		TextView textView6;
		TextView textView7;
		TextView textView8;
	}

	@Override
	public View getPinnedHeader() {
		View headerView = (ViewGroup) ((Activity) context).getLayoutInflater().inflate(R.layout.record_expandable_group,
				null);
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

		return headerView;
	}

	/**
	 * 更换pinnedHeader
	 */
	@Override
	public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {
		Group firstVisibleGroup = (Group) adapter.getGroup(firstVisibleGroupPos);
		LinearLayout layout1, layout2;
		layout1 = (LinearLayout) headerView.findViewById(R.id.layout_record_group1);
		layout2 = (LinearLayout) headerView.findViewById(R.id.layout_record_group2);
		if (firstVisibleGroupPos == 0) {
			layout1.setVisibility(View.VISIBLE);
			layout2.setVisibility(View.GONE);
			// 获取正常，偏高，偏低，这几个状态都有几次结果
			getValueCount();
			// 设置group组的值;jimmy
			TextView textView1 = (TextView) headerView.findViewById(R.id.txt_record_normal);
			TextView textView2 = (TextView) headerView.findViewById(R.id.txt_record_head_high);
			TextView textView3 = (TextView) headerView.findViewById(R.id.txt_record_low);
			textView1.setText(strNormalCount);
			textView2.setText(strHighCount);
			textView3.setText(strLowCount);
			// textView1.setText(firstVisibleGroup.getNormal());
			// textView2.setText(firstVisibleGroup.getHighSide());
			// textView3.setText(firstVisibleGroup.getLowSide());
		} else {
			layout1.setVisibility(View.GONE);
			layout2.setVisibility(View.VISIBLE);
		}
		// 重新设置layout的大小
		headerView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		initRoundProgress(headerView);
	}

	// 初始化RoundProgress
	private void initRoundProgress(View headerView) {
		mRoundProgressBar1 = (RoundProgressBar) headerView.findViewById(R.id.roundProgressBar1);
		mRoundProgressBar2 = (RoundProgressBar) headerView.findViewById(R.id.roundProgressBar2);
		if (highCount != 0 || lowCount != 0) {
			mRoundProgressBar1.setMax(highCount + lowCount + normalCount);
			mRoundProgressBar2.setMax(highCount + lowCount + normalCount);
		}
		// 画血糖值偏低的圈
		mRoundProgressBar1.setProgress(lowCount + highCount);
		// 画血糖值偏高的圈
		mRoundProgressBar2.setProgress(highCount);
	}

	// 判断dialog是否显示
	class LoadingThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(2300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			// 发送信号，改变当前显示的时间段
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			mLoadingThread = null;
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				// 隐藏progressBars
				if (mProgressLoading.getVisibility() == View.VISIBLE) {
					mProgressLoading.setVisibility(View.GONE);
				}

				// 展开1组group，第几组是从0开始算
				expandableListView.expandGroup(1);

			}
		}
	};

	@Override
	public void changePopupWindowState() {
		super.changePopupWindowState();
		// 如果child是显示的，则隐藏
		if (expandableListView.isGroupExpanded(1)) {
			expandableListView.collapseGroup(1);
		} else {
			// 显示progressBar
			if (mProgressLoading.getVisibility() == View.GONE) {
				mProgressLoading.setVisibility(View.VISIBLE);
			}
			// 创建加载线程
			if (mLoadingThread == null) {
				mLoadingThread = new LoadingThread();
			}
			if (!mLoadingThread.isAlive()) {
				mLoadingThread.start();
			}
		}
	}

	private void getValueCount() {
		highCount = 0;
		lowCount = 0;
		normalCount = 0;
		// 获取数据库表
		mGluValueDao = SingletonApplication.getGluValueDao();
		gluValueAll = mGluValueDao.queryAll();
		// 正常，偏高，偏低，这几个状态都有几次结果
		for (int i = 0; i < gluValueAll.size(); i++) {
			String result = gluValueAll.get(i).getResult();
			if (Float.parseFloat(result) - 8 > 0) {
				// 血糖过高
				highCount++;
			} else if (Float.parseFloat(result) - 4 < 0) {
				// 血糖过低
				lowCount++;
			} else {
				// 血糖正常
				normalCount++;
			}
		}
		// 正常，偏高，偏低，这几个状态都有几次结果
		strHighCount = getStringShow(highCount);
		strLowCount = getStringShow(lowCount);
		strNormalCount = getStringShow(normalCount);
	}

	// 设置数据库的实体类,并保存
	private void saveUserDB(String result, String HideDate, String TimePeriod, String ReasonString) {
		GluValue gluValue = new GluValue(HideDate, TimePeriod, 0, ReasonString, result, 1, 1, "", "", "", "", "", "",
				"", "", "", "");
		mGluValueDao.insert(gluValue);
	}

}
