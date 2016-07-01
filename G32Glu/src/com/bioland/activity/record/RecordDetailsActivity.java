package com.bioland.activity.record;

import java.util.List;

import com.bioland.activity.MainActivity;
import com.bioland.db.bean.GluValue;
import com.bioland.db.dao.GluValueDao;
import com.bioland.g32glu.R;
import com.bioland.singleton.SingletonApplication;
import com.bioland.utils.BaseActivity;
import com.bioland.utils.CloseActivityReceiver;
import com.bioland.utils.LogUtil;
import com.bioland.utils.record.TextStatusUtil;
import com.bioland.widget.dialog.RecordSetTimeDialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RecordDetailsActivity extends BaseActivity implements OnClickListener {
	private static final String INTENT_TIME_PERIODA = "intent_time_period";
	private static final String INTENT_DATE = "intent_date";
	private View includeReason[] = new View[6];
	private ImageView mImageViewRight[] = new ImageView[6];
	private ImageView mImageViewPicture[] = new ImageView[6];
	private boolean isPress[] = new boolean[6];
	private EditText edtDetailsReason;
	private ImageView mImageViewArrowReturn;
	private LinearLayout mLayoutTime;
	private ImageView mImageTimeIcon;
	private TextView mTextViewTimeHint, mTextViewTime;
	private TextView mTextViewResult, mTextViewResultStatus;
	private LinearLayout mLayoutReason1, mLayoutReason2;
	private TextView mTextViewLayout;
	private Button mButtonSave;
	private boolean isCanPress = false;
	private TextStatusUtil mStatusUtils;
	// 数据库参数
	private GluValueDao mGluValueDao;
	private List<GluValue> gluValues;
	private int mReason = 0;
	private int mTimePeriod = 0;
	private int temporaryTimePeriod = 0;
	private String mDate;
	private String mResult;

	// dialog是否显示线程相关参数
	private DialogThread mThreadDialog;
	String dialogTime[];

	private final static String TAG = "RecordDetailsActivity";

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, String data1, int data2) {
		Intent intent = new Intent(context, RecordDetailsActivity.class);
		intent.putExtra(INTENT_DATE, data1);
		intent.putExtra(INTENT_TIME_PERIODA, data2);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_details);
		initView();
	}

	private void initView() {
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		mDate = bundle.getString(INTENT_DATE);
		mTimePeriod = bundle.getInt(INTENT_TIME_PERIODA);
		temporaryTimePeriod = mTimePeriod;
		mGluValueDao = SingletonApplication.getGluValueDao();
		gluValues = mGluValueDao.searchDate(mDate, String.valueOf(mTimePeriod));
		mResult = gluValues.get(0).getResult();
		mReason = gluValues.get(0).getReason();

		// 测量值状态文本
		mTextViewResult = (TextView) findViewById(R.id.txt_record_details_result);
		mTextViewResult.setText(mResult);
		mStatusUtils = new TextStatusUtil(this);
		mTextViewResult.setTextColor(mStatusUtils.getColor(mResult));
		mTextViewResultStatus = (TextView) findViewById(R.id.txt_record_details_result_status);
		mTextViewResultStatus.setText(mStatusUtils.getTextStatus(mResult));

		// 返回按键
		mImageViewArrowReturn = (ImageView) findViewById(R.id.arrow_return);
		mImageViewArrowReturn.setOnClickListener(this);

		// 六个原因选择
		mLayoutReason1 = (LinearLayout) findViewById(R.id.layout_record_details_reason1);
		mLayoutReason2 = (LinearLayout) findViewById(R.id.layout_record_details_reason2);
		mTextViewLayout = (TextView) findViewById(R.id.txt_record_details_normal_layout);

		includeReason[0] = findViewById(R.id.include_record_details_reason1);
		includeReason[0].setOnClickListener(this);
		mImageViewRight[0] = (ImageView) findViewById(R.id.record_details_reason_right1);
		mImageViewPicture[0] = (ImageView) findViewById(R.id.record_details_reason_pic1);

		includeReason[1] = findViewById(R.id.include_record_details_reason2);
		includeReason[1].setOnClickListener(this);
		mImageViewRight[1] = (ImageView) findViewById(R.id.record_details_reason_right2);
		mImageViewPicture[1] = (ImageView) findViewById(R.id.record_details_reason_pic2);

		includeReason[2] = findViewById(R.id.include_record_details_reason3);
		includeReason[2].setOnClickListener(this);
		mImageViewRight[2] = (ImageView) findViewById(R.id.record_details_reason_right3);
		mImageViewPicture[2] = (ImageView) findViewById(R.id.record_details_reason_pic3);

		includeReason[3] = findViewById(R.id.include_record_details_reason4);
		includeReason[3].setOnClickListener(this);
		mImageViewRight[3] = (ImageView) findViewById(R.id.record_details_reason_right4);
		mImageViewPicture[3] = (ImageView) findViewById(R.id.record_details_reason_pic4);

		includeReason[4] = findViewById(R.id.include_record_details_reason5);
		includeReason[4].setOnClickListener(this);
		mImageViewRight[4] = (ImageView) findViewById(R.id.record_details_reason_right5);
		mImageViewPicture[4] = (ImageView) findViewById(R.id.record_details_reason_pic5);

		includeReason[5] = findViewById(R.id.include_record_details_reason6);
		includeReason[5].setOnClickListener(this);
		mImageViewRight[5] = (ImageView) findViewById(R.id.record_details_reason_right6);
		mImageViewPicture[5] = (ImageView) findViewById(R.id.record_details_reason_pic6);

		// 其他原因编辑框
		edtDetailsReason = (EditText) findViewById(R.id.edt_record_details_reason);
		edtDetailsReason.setText(gluValues.get(0).getReasonString());
		edtDetailsReason.addTextChangedListener(new MyTextWatcher());

		// 设置时间段相关参数
		mLayoutTime = (LinearLayout) findViewById(R.id.layout_record_details_time);
		mLayoutTime.setOnClickListener(this);
		mImageTimeIcon = (ImageView) findViewById(R.id.img_record_details_time_icon);
		mTextViewTimeHint = (TextView) findViewById(R.id.txt_record_details_time_hint);
		mTextViewTime = (TextView) findViewById(R.id.txt_record_details_time);

		// 初始化测试时间段
		dialogTime = getResources().getStringArray(R.array.record_details_dialog);
		mTextViewTime.setText(dialogTime[mTimePeriod]);
		// 保存按钮
		mButtonSave = (Button) findViewById(R.id.btn_record_details_save);
		mButtonSave.setOnClickListener(this);
		// 如果血糖值的颜色是正常值的颜色，则记录详情界面显示为正常值的界面

		if (mStatusUtils.getIsNormal(mResult)) {
			mLayoutReason1.setVisibility(View.GONE);
			mLayoutReason2.setVisibility(View.GONE);
			mTextViewLayout.setVisibility(View.VISIBLE);
			// 显示编辑框
			edtDetailsReason.setVisibility(View.VISIBLE);
			if (!edtDetailsReason.getText().toString().equals("")) {
				setNormalDisEnableView();
			}
		} else {
			if ((!edtDetailsReason.getText().toString().equals("")) || (mReason != 0)) {
				setDisEnableView();
			}
		}
	}

	// 血糖值正常时，原因已设置，进来后初始化方法
	private void setNormalDisEnableView() {
		edtDetailsReason.setFocusableInTouchMode(false);
		// 设置时间段按键为DisEnable
		mLayoutTime.setEnabled(false);
		// 按键设置为"修改"按键
		mButtonSave.setText(getResources().getString(R.string.record_details_change));
		mButtonSave.setEnabled(true);
	}

	// 血糖值正常时，点击“修改”按键后，调用的方法
	private void setNormalEnableView() {
		// 编辑框设置焦点
		edtDetailsReason.setFocusableInTouchMode(true);
		// 设置时间段按键为Enable
		mLayoutTime.setEnabled(true);
		// 按键设置为"保存"按键
		mButtonSave.setText(getResources().getString(R.string.record_details_save));
	}

	// 原因已设置，进来后初始化方法
	private void setDisEnableView() {
		// 原因按键设置为对应图片
		if ((mReason & 1) != 0) {
			isPress[0] = true;
			mImageViewPicture[0].setImageResource(R.drawable.record_details_press1);
		}
		if ((mReason & 2) != 0) {
			isPress[1] = true;
			mImageViewPicture[1].setImageResource(R.drawable.record_details_press2);
		}
		if ((mReason & 4) != 0) {
			isPress[2] = true;
			mImageViewPicture[2].setImageResource(R.drawable.record_details_press3);
		}
		if ((mReason & 8) != 0) {
			isPress[3] = true;
			mImageViewPicture[3].setImageResource(R.drawable.record_details_press4);
		}
		if ((mReason & 16) != 0) {
			isPress[4] = true;
			mImageViewPicture[4].setImageResource(R.drawable.record_details_press5);
		}
		// 原因按键设置为DisEnable
		for (int i = 0; i < 6; i++) {
			includeReason[i].setEnabled(false);
			mImageViewRight[i].setVisibility(View.INVISIBLE);
		}
		// 如果编辑框不为空，则设置其他原因按键为已选，编辑框显示且设置焦点为false,此处不能用edtDetailsReason.setFocusable(false)
		if (!edtDetailsReason.getText().toString().equals("")) {
			isPress[5] = true;
			mImageViewPicture[5].setImageResource(R.drawable.record_details_press6);
			edtDetailsReason.setVisibility(View.VISIBLE);
			edtDetailsReason.setFocusableInTouchMode(false);
		}
		// 设置时间段按键为DisEnable
		mLayoutTime.setEnabled(false);

		// 按键设置为"修改"按键
		mButtonSave.setText(getResources().getString(R.string.record_details_change));
		mButtonSave.setEnabled(true);
	}

	// 点击“修改”按键后，调用的方法
	private void setEnableView() {
		// 原因按键设置为Enable,显示对应right图标
		for (int i = 0; i < 6; i++) {
			includeReason[i].setEnabled(true);
			mImageViewRight[i].setVisibility(View.VISIBLE);
			if (isPress[i]) {
				mImageViewRight[i].setImageResource(R.drawable.record_details_right_press);
			}
		}
		// 编辑框设置焦点
		edtDetailsReason.setFocusableInTouchMode(true);
		// 设置时间段按键为Enable
		mLayoutTime.setEnabled(true);
		// 按键设置为"保存"按键
		mButtonSave.setText(getResources().getString(R.string.record_details_save));
	}

	class MyTextWatcher implements TextWatcher {
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			// 当编辑框中有字符时，保存按钮改变状态
			setSaveStatue();
		}
	}

	// 判断dialog是否显示
	class DialogThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (RecordSetTimeDialog.isShow) {
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// 发送信号，改变当前显示的时间段
			Message msg = new Message();
			msg.what = 1;
			mHandler.sendMessage(msg);
			mThreadDialog = null;
		}
	}

	Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				// 设置测试时间段
				temporaryTimePeriod = RecordSetTimeDialog.item;
				// 如果时间段改变了，且保存按键为无效才进入if语句
				if ((temporaryTimePeriod != mTimePeriod) && (!isCanPress)) {
					isCanPress = true;
					mButtonSave.setEnabled(true);
				}
				if ((temporaryTimePeriod == mTimePeriod) && (edtDetailsReason.getText().toString().equals(""))
						&& (mReason == 0) && (isCanPress)) {
					isCanPress = false;
					mButtonSave.setEnabled(false);
				}
				mTextViewTime
						.setText(getResources().getStringArray(R.array.record_details_dialog)[temporaryTimePeriod]);
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.arrow_return:
			// 结束当前activity
			finish();
			break;
		// 点击弹窗dialog按键
		case R.id.layout_record_details_time:
			// 弹窗设置测量时间段的dialog
			RecordSetTimeDialog setTimeDialog = new RecordSetTimeDialog(this, R.style.record_details_time_dialog,
					temporaryTimePeriod);// 创建Dialog并设置样式主题
			Window win = setTimeDialog.getWindow();
			LayoutParams params = new LayoutParams();
			params.x = 0;// 设置x坐标
			params.y = -25;// 设置y坐标
			win.setAttributes(params);
			setTimeDialog.setCanceledOnTouchOutside(true);// 设置点击Dialog外部任意区域关闭Dialog
			setTimeDialog.show();
			if (mThreadDialog == null) {
				mThreadDialog = new DialogThread();
			}
			if (!mThreadDialog.isAlive()) {
				mThreadDialog.start();
			}
			break;
		case R.id.include_record_details_reason1:
			// 设置按键背景色
			setBackGround(0, R.drawable.record_details_normal1, R.drawable.record_details_press1);
			break;
		case R.id.include_record_details_reason2:
			// 设置按键背景色
			setBackGround(1, R.drawable.record_details_normal2, R.drawable.record_details_press2);
			break;
		case R.id.include_record_details_reason3:
			// 设置按键背景色
			setBackGround(2, R.drawable.record_details_normal3, R.drawable.record_details_press3);
			break;
		case R.id.include_record_details_reason4:
			// 设置按键背景色
			setBackGround(3, R.drawable.record_details_normal4, R.drawable.record_details_press4);
			break;
		case R.id.include_record_details_reason5:
			// 设置按键背景色
			setBackGround(4, R.drawable.record_details_normal5, R.drawable.record_details_press5);
			break;
		case R.id.include_record_details_reason6:
			if (edtDetailsReason.getVisibility() == View.GONE) {
				// 显示原因编辑框和输入软键盘
				edtDetailsReason.setVisibility(View.VISIBLE);
				edtDetailsReason.requestFocus();
				showSoftInput(edtDetailsReason);
			} else {
				// 隐藏原因编辑框和输入软键盘
				hideSoftInput(edtDetailsReason);
				edtDetailsReason.setVisibility(View.GONE);
			}
			// 设置按键背景色
			setBackGround(5, R.drawable.record_details_normal6, R.drawable.record_details_press6);
			break;
		case R.id.btn_record_details_save:
			if (mButtonSave.getText().equals(getResources().getString(R.string.record_details_change))) {
				if (mStatusUtils.getIsNormal(mResult)) {
					setNormalEnableView();
				} else {
					setEnableView();
				}
			} else {
				// 重置GluValue改变的值
				gluValues.get(0).setReason(mReason);
				gluValues.get(0).setReasonString(edtDetailsReason.getText().toString());
				gluValues.get(0).setTimePeriod(String.valueOf(temporaryTimePeriod));
				// 如果改变了时间段，则先删除那个时间段的值，再更新改变的值到数据库
				if (temporaryTimePeriod != mTimePeriod) {
					mGluValueDao.delete(mDate, String.valueOf(temporaryTimePeriod));
				}
				mGluValueDao.update(gluValues.get(0));
				// 此处用于刷新"测试记录"界面
				MainActivity.myRecordPopup.changePopupWindowState();
				MainActivity.myRecordPopup.changePopupWindowState();
				finish();
			}
			break;
		default:
			break;
		}
	}

	// 设置按键点击与否的效果变化
	private void setBackGround(int position, int normalPicture, int pressPicture) {
		if (isPress[position]) {
			isPress[position] = false;
			mImageViewRight[position].setImageResource(R.drawable.record_details_right_normal);
			mImageViewPicture[position].setImageResource(normalPicture);
		} else {
			isPress[position] = true;
			mImageViewRight[position].setImageResource(R.drawable.record_details_right_press);
			mImageViewPicture[position].setImageResource(pressPicture);
		}
		setSaveStatue();
	}

	/**
	 * 根据选择的原因来设置保存按键的状态;其他原因按键就算按下也没用，所以isPress[5]的状态不管
	 */
	private void setSaveStatue() {
		int i = 0;
		if (!edtDetailsReason.getText().toString().equals("")) {
			isCanPress = true;
		} else {
			for (; i < 5; i++) {
				if (isPress[i] == true) {
					isCanPress = true;
					break;
				}
			}
			if ((i == 5) && (temporaryTimePeriod == mTimePeriod)) {
				isCanPress = false;
			}
		}
		mReason = 0;
		if (isCanPress) {
			mButtonSave.setEnabled(true);
			for (int j = 0; j < 5; j++) {
				if (isPress[j] == true) {
					mReason = mReason | (1 << j);
				}
			}
		} else {
			mButtonSave.setEnabled(false);
		}
	}

	// 显示软件盘
	public void showSoftInput(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
	}

	// 隐藏软件盘
	public void hideSoftInput(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
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
