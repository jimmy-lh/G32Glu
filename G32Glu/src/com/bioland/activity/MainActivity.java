package com.bioland.activity;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bioland.db.bean.GluValue;
import com.bioland.db.dao.GluValueDao;
import com.bioland.g32glu.R;
import com.bioland.serial.SerialPortActivity;
import com.bioland.serial.Unpack;
import com.bioland.singleton.SingletonApplication;
import com.bioland.utils.CloseActivityReceiver;
import com.bioland.utils.LogUtil;
import com.bioland.utils.VoiceUtil;
import com.bioland.utils.record.GapDateUtil;
import com.bioland.widget.animation.WaveAnimation;
import com.bioland.widget.popup.HealthPopup;
import com.bioland.widget.popup.MeasurePopup;
import com.bioland.widget.popup.MorePopup;
import com.bioland.widget.popup.RecordPopup;
import com.bioland.widget.popup.RemindPopup;
import com.bioland.widget.sharedpreferences.MyPreference;
import com.bioland.widget.telephonymanager.MyTelephonyManager;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends SerialPortActivity implements OnClickListener {

	private final String TAG = "MainActivity";

	private WaveAnimation myWaveAnimation;
	private MeasurePopup myMeasurePopup;
	public static RecordPopup myRecordPopup;
	private HealthPopup myHealthPopup;
	private RemindPopup myRemindPopup;
	private MorePopup myMorePopup;

	private View measureLayout, menuLayout, resultLayout, performLayout, healthPhasesLayout;
	private TextView titleTextView, hintTextView;
	private ImageView arrowReturnImageView;
	int numPopup;
	private static final int MAIN_POPUP = 0;
	private static final int MEASURE_POPUP = 1;
	private static final int RECORD_POPUP = 2;
	private static final int HEALTH_POPUP = 3;
	private static final int REMIND_POPUP = 4;
	private static final int MORE_POPUP = 5;

	private LinearLayout recordInclude, healthInclude, remindInclude, moreInclude;
	private ImageView measureImage;
	private Button mButtonFinish;
	private TextView resultTextView;
	private ImageView mHintPointImageView;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, MainActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// getWindow().getDecorView().setSystemUiVisibility(10); // 平板全屏
		setContentView(R.layout.activity_main);
		initView();
		SingletonApplication.getInstance(this);// 创建单例类的实例
		MyPreference.instance(this);// 创建或者获取Preference存储对象
		// 初始化算法部分
		initService();
		initPopup();
	}

	private void initView() {
		measureLayout = findViewById(R.id.layout_measure);

		menuLayout = findViewById(R.id.layout_main_menu);
		resultLayout = findViewById(R.id.layout_main_result);
		performLayout = findViewById(R.id.layout_main_perform);
		healthPhasesLayout = findViewById(R.id.layout_health_phases);

		titleTextView = (TextView) findViewById(R.id.title_text);
		hintTextView = (TextView) findViewById(R.id.hint_text);
		resultTextView = (TextView) findViewById(R.id.txt_main_value);
		mHintPointImageView = (ImageView) findViewById(R.id.img_main_record_hint_point);

		measureImage = (ImageView) findViewById(R.id.img_main_measure);
		recordInclude = (LinearLayout) findViewById(R.id.include_main_record);
		healthInclude = (LinearLayout) findViewById(R.id.include_main_health);
		remindInclude = (LinearLayout) findViewById(R.id.include_main_remind);
		moreInclude = (LinearLayout) findViewById(R.id.include_main_more);
		arrowReturnImageView = (ImageView) findViewById(R.id.arrow_return);
		mButtonFinish = (Button) findViewById(R.id.btn_main_measure_finish);
		measureImage.setOnClickListener(this);
		recordInclude.setOnClickListener(this);
		healthInclude.setOnClickListener(this);
		remindInclude.setOnClickListener(this);
		moreInclude.setOnClickListener(this);
		arrowReturnImageView.setOnClickListener(this);
		mButtonFinish.setOnClickListener(this);
	}

	private void initPopup() {
		// 水波动画
		myWaveAnimation = new WaveAnimation(this);
		// 按测量键后弹出菜单
		myMeasurePopup = new MeasurePopup(this);
		// 按记录后弹出菜单
		myRecordPopup = new RecordPopup(this);
		// 按健康指导后弹出菜单
		myHealthPopup = new HealthPopup(this);
		// 按提醒后弹出菜单
		myRemindPopup = new RemindPopup(this);
		// 按更多后弹出菜单
		myMorePopup = new MorePopup(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 测量按键
		case R.id.img_main_measure:
			entryMeasurePopup();
			break;
		// 记录按键
		case R.id.include_main_record:
			mHintPointImageView.setVisibility(View.GONE);
			hideView(getResources().getString(R.string.record_title));
			myRecordPopup.changePopupWindowState();
			numPopup = RECORD_POPUP;
			break;
		// 健康指导按键
		case R.id.include_main_health:
			hideViewHealth(getResources().getString(R.string.main_health));
			myHealthPopup.changePopupWindowState();
			numPopup = HEALTH_POPUP;
			break;
		// 提醒按键
		case R.id.include_main_remind:
			hideView(getResources().getString(R.string.remind_time));
			myRemindPopup.changePopupWindowState();
			numPopup = REMIND_POPUP;
			break;
		// 更多按键
		case R.id.include_main_more:
			hideView(getResources().getString(R.string.main_more));
			myMorePopup.changePopupWindowState();
			numPopup = MORE_POPUP;
			break;
		// popup_window中的返回按键
		case R.id.arrow_return:
			exitPopup();
			break;
		// 测量完成，点击回到主界面
		case R.id.btn_main_measure_finish:
			showView(getResources().getString(R.string.title_name));
			break;
		}
	}

	private void entryMeasurePopup() {
		hideMeasureView();
		myMeasurePopup.changePopupWindowState();
		numPopup = MEASURE_POPUP;
	}

	private void exitPopup() {
		switch (numPopup) {
		case MEASURE_POPUP:
			showView(getResources().getString(R.string.title_name));
			myMeasurePopup.changePopupWindowState();
			break;
		case RECORD_POPUP:
			showView(getResources().getString(R.string.title_name));
			myRecordPopup.changePopupWindowState();
			break;
		case HEALTH_POPUP:
			showView(getResources().getString(R.string.title_name));
			myHealthPopup.changePopupWindowState();
			break;
		case REMIND_POPUP:
			showView(getResources().getString(R.string.title_name));
			myRemindPopup.changePopupWindowState();
			break;
		case MORE_POPUP:
			showView(getResources().getString(R.string.title_name));
			myMorePopup.changePopupWindowState();
			break;
		}
		numPopup = MAIN_POPUP;
	}

	// 点击测量键后隐藏的主界面控件（隐藏了title）
	public void hideMeasureView() {
		measureLayout.setVisibility(View.GONE);
		menuLayout.setVisibility(View.INVISIBLE);
		hintTextView.setVisibility(View.GONE);
		titleTextView.setVisibility(View.GONE);
		arrowReturnImageView.setVisibility(View.VISIBLE);
	}

	// 点击其他菜单隐藏的主界面控件（没有隐藏title）
	public void hideView(String str) {
		measureLayout.setVisibility(View.GONE);
		menuLayout.setVisibility(View.INVISIBLE);
		hintTextView.setVisibility(View.GONE);
		arrowReturnImageView.setVisibility(View.VISIBLE);
		titleTextView.setText(str);
	}

	// 点击健康菜单隐藏的主界面控件（没有隐藏title）
	public void hideViewHealth(String str) {
		measureLayout.setVisibility(View.GONE);
		menuLayout.setVisibility(View.INVISIBLE);
		hintTextView.setVisibility(View.GONE);
		arrowReturnImageView.setVisibility(View.VISIBLE);
		healthPhasesLayout.setVisibility(View.VISIBLE);
		titleTextView.setText(str);
	}

	// 显示被隐藏的主界面控件
	public void showView(String str) {
		measureLayout.setVisibility(View.VISIBLE);
		menuLayout.setVisibility(View.VISIBLE);
		hintTextView.setVisibility(View.VISIBLE);
		titleTextView.setVisibility(View.VISIBLE);
		titleTextView.setText(str);
		arrowReturnImageView.setVisibility(View.GONE);
		healthPhasesLayout.setVisibility(View.GONE);
		// 隐藏测量结果界面
		resultLayout.setVisibility(View.GONE);
		performLayout.setVisibility(View.GONE);
	}

	// 显示测量结果界面
	public void showResult() {
		numPopup = MAIN_POPUP;// 解决测试完结果回到主界面后，在插入试纸，measurePopup不弹出来的问题
		myMeasurePopup.changePopupWindowState();
		titleTextView.setVisibility(View.VISIBLE);
		titleTextView.setText(getResources().getString(R.string.title_name));
		resultLayout.setVisibility(View.VISIBLE);
		performLayout.setVisibility(View.VISIBLE);
		mHintPointImageView.setVisibility(View.VISIBLE);// 记录按键旁显示红点，提示有新的结果
		// 数值显示到界面
		if ((mResultZ < 10) || (mResultX < 10)) {
			resultTextView.setText("Lo");
		} else if ((mResultZ > 600) || (mResultX > 600)) {
			resultTextView.setText("Hi");
		} else {
			resultTextView.setText(String.valueOf(mResult));
		}
	}

	/**
	 * 算法部分
	 */
	private Unpack myUnpack;
	// 软件是否第一次运行
	public static boolean isFirst = true;
	// 试纸插入正确,不报错
	private boolean isRight = false;
	// 数据库参数
	private GluValueDao mGluValueDao;
	// SharedPreferences存储
	private MyPreference mPreference;
	// TelephonyManager参数
	private MyTelephonyManager mTelephony;
	// 声音相关参数
	private VoiceUtil mVoiceUtil;
	private int mVoiceCount = 0;// 声音播放定时器，当值为n是播放声音
	// 判断试纸是否插入
	private boolean isKeyDown = false;
	// 保存结果的变量
	private float mResult;
	private float mResultX, mResultZ, mResultF;
	// 0:血糖模式；1：值控模式;2:电阻片模式;3:生化模式
	private static int measureMode = 0;
	// 广播相关参数
	private CloseActivityReceiver mCloseActivityReceiver;
	// 保存数据的时间处理对象
	private GapDateUtil mGapDateUtil;

	private void initService() {
		myUnpack = Unpack.getInstance(this);
		// 初始化数据库
		initDB();
		// 初始化Telephony
		initTelephony();
		// 初始化声音
		initVoice();
		// 注册广播
		getbroadcast();
		initOther();
	}

	// 初始化数据库和SharedPreferences
	private void initDB() {
		mGluValueDao = SingletonApplication.getGluValueDao();
		mPreference = MyPreference.getInstance();
		isFirst = mPreference.getBoolean(MyPreference.IS_FIRST, true);
	}

	// 初始化Telephony
	private void initTelephony() {
		mTelephony = new MyTelephonyManager(this);
	}

	// 初始化声音
	private void initVoice() {
		mVoiceUtil = new VoiceUtil(this);
	}

	// 初始化
	private void initOther() {
		mGapDateUtil = new GapDateUtil();
	}

	/**
	 * 串口接收数据
	 */
	@Override
	protected void onDataReceived(final byte[] buffer, final int size) {
		runOnUiThread(new Runnable() {
			public void run() {
				int i = 0, j = 0;
				byte[] buf = buffer;
				// 打印接收到的数据
				// for (int k = 0; k < buf.length; k++) {
				// if (buf[k] != 0)
				// LogUtil.e(TAG, "156 buf[k]=" + String.valueOf(buf[k]));
				// }
				while (i < size) {
					if (buf[j] == 53) {
						buf = myUnpack.bufShifting(buffer, size, j);
						j = 0;
						// 判断是否为true，解决没有收到有效数据但是收到53时，重复发送信号
						if (myUnpack.setData(buf)) {
							Message msg = new Message();
							msg.what = myUnpack.step;
							mHandler.sendMessage(msg);
						}
					}
					i++;
					j++;
				}
			}
		});
	}

	// 收到数据后对应处理
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Unpack.GAIN_INSERT:
				LogUtil.e(TAG, "试纸已经插入...");
				isRight = true;
				myMeasurePopup.isInsert = true;// 判断是否插入试纸的标志位,用来判断是跳转到插入试纸动画还是滴血动画。
				if (numPopup != MEASURE_POPUP) {
					LogUtil.e(TAG, "在。非插入试纸演示界面插入的试纸");
					exitPopup();
					entryMeasurePopup();
					myMeasurePopup.showStaxisDemon();
				} else {
					LogUtil.e(TAG, "在。。插入试纸演示界面插入的试纸");
					myMeasurePopup.showStaxisDemon();
				}
				break;
			case Unpack.GAIN_GETSN:
				// Toast.makeText(MainActivity.this, "sn:" + myUnpack.sn,
				// Toast.LENGTH_SHORT).show();
				break;
			// 获取倒计时
			case Unpack.GAIN_COUNTDOWN:
				// 开始滴血测量
				break;
			// 收到读取模式命令，则发送当前模式数据
			case Unpack.GAIN_GETMODE:
				// 收到读取模式命令，则发送当前模式数据
				// sendDataMode();
				break;
			// 收到测量完成数值，发送命令，休眠血糖小板
			case Unpack.GAIN_GETVALUE:
				// 收到测量完成数值，发送命令，休眠血糖小板
				// sendDataSleep();
				break;
			// 获取时间
			case Unpack.GAIN_GETTIME:
				// 版本0.03增加显示数值和提示信息
				// if (measureMode != 3) {
				// getResult();
				// } else {
				getBiochemistryResult();
				// 显示测量结果
				showResult();
				// }
				// 测试完成,保存数据
				saveUserDB(String.valueOf(mResult));
				break;
			default:
				// Toast.makeText(ReceiveActivity.this, "step=" + myUnpack.step
				// + ",错误!", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	// 设置数据库的实体类,并保存
	private void saveUserDB(String result) {
		mGapDateUtil.getSystemTime();
		GluValue gluValue = new GluValue(mGapDateUtil.getHideCurrentDate(), mGapDateUtil.getTimePeriod(), 0, "", result,
				mTelephony.getmLac(), mTelephony.getmCid(), "", "", "", "", "", "", "", "", "", "");
		mGluValueDao.delete(mGapDateUtil.getHideCurrentDate(), mGapDateUtil.getTimePeriod());
		mGluValueDao.insert(gluValue);
	}

	private int[] mNode = { 10, 35, 39, 47, 70, 117, 194, 253, 305, 600 };

	private float[][] mFactor = { { (float) 0, (float) 0 }, { (float) 0.6, (float) 0.95 },
			{ (float) 0.95, (float) 0.95 }, { (float) 0.95, (float) 1.45 }, { (float) 1.45, (float) 1.65 },
			{ (float) 1.65, (float) 1.65 }, { (float) 1.65, (float) 1.65 }, { (float) 1.65, (float) 1.65 },
			{ (float) 1.65, (float) 1.65 }, { (float) 1.65, (float) 1.65 } };

	private int[] mNodeBio = { 10, 35, 39, 47, 70, 117, 194, 253, 305, 600 };
	private float[][] mFactorBio = { { (float) 0, (float) 0 }, { (float) 0.6, (float) 1.3 },
			{ (float) 1.3, (float) 1.3 }, { (float) 1.3, (float) 1.77 }, { (float) 1.77, (float) 1.95 },
			{ (float) 1.95, (float) 1.95 }, { (float) 1.95, (float) 1.95 }, { (float) 1.95, (float) 1.95 },
			{ (float) 1.95, (float) 1.95 }, { (float) 1.95, (float) 1.95 } };

	/**
	 * (X的范围20-600;X的节点为（39,47,70,117,194,253,305）);
	 * 先通过五个值算出X的值，然后根据X的值得出F系数的值；X小于等于39，F为0.8；
	 * 因为在20-600这个范围内，F的值基本是线性增加的，所以用下面方法求F：
	 * X大于39小于等于47，F为(0.8+(1.3-0.8)/(47-39)*(X-39));
	 * X大于47小于等于70，F为(1.3+(1.6-0.3)/(70-47)*(X-47)); X大于70小于等于117， F为1.6;
	 * X大于117小于等于194， F为1.6; X大于194小于等于253， F为1.6; X大于253小于等于305， F为1.6;
	 * X大于305小于等于600， F为1.6;
	 */
	/**
	 * 血液模式下，X为70-200正常，否则报血液异常;值控液模式下，X为100以下正常，否则报值控液异常
	 */
	private void getResult() {
		mResultX = myUnpack.value[0] * (float) 0.0 + myUnpack.value[1] * (float) 0.2 + myUnpack.value[2] * (float) 0.2
				+ myUnpack.value[3] * (float) 0.3 + myUnpack.value[4] * (float) 0.3;
		if ((mResultX - mNode[0]) <= 0) {
			mResultF = getResultF(0, mNode[0], mFactor[0][0], mFactor[0][1]);
		} else if ((mResultX - mNode[1]) <= 0) {
			mResultF = getResultF(mNode[0], mNode[1], mFactor[1][0], mFactor[1][1]);
		} else if ((mResultX - mNode[2]) <= 0) {
			mResultF = getResultF(mNode[1], mNode[2], mFactor[2][0], mFactor[2][1]);
		} else if (mResultX - mNode[3] <= 0) {
			mResultF = getResultF(mNode[2], mNode[3], mFactor[3][0], mFactor[3][1]);
		} else if (mResultX - mNode[4] <= 0) {
			mResultF = getResultF(mNode[3], mNode[4], mFactor[4][0], mFactor[4][1]);
		} else if (mResultX - mNode[5] <= 0) {
			mResultF = getResultF(mNode[4], mNode[5], mFactor[5][0], mFactor[5][1]);
		} else if (mResultX - mNode[6] <= 0) {
			mResultF = getResultF(mNode[5], mNode[6], mFactor[6][0], mFactor[6][1]);
		} else if (mResultX - mNode[7] <= 0) {
			mResultF = getResultF(mNode[6], mNode[7], mFactor[7][0], mFactor[7][1]);
		} else if (mResultX - mNode[8] <= 0) {
			mResultF = getResultF(mNode[7], mNode[8], mFactor[8][0], mFactor[8][1]);
		} else if (mResultX - mNode[9] <= 0) {
			mResultF = getResultF(mNode[8], mNode[9], mFactor[9][0], mFactor[9][1]);
		}
		mResultZ = mResultX * mResultF;
		mResult = (float) Math.round((mResultZ / 18.0) * 10) / 10;
		// // 数值显示到界面
		// if ((mResultZ < 10) || (mResultX < 10)) {
		// mTextViewVersion3Value.setText("Lo");
		// } else if ((mResultZ > 600) || (mResultX > 600)) {
		// mTextViewVersion3Value.setText("Hi");
		// } else {
		// mTextViewVersion3Value.setText(String.valueOf(mResult));
		// }
	}

	private void getBiochemistryResult() {
		mResultX = myUnpack.value[0] * (float) 0.0 + myUnpack.value[1] * (float) 0.2 + myUnpack.value[2] * (float) 0.2
				+ myUnpack.value[3] * (float) 0.3 + myUnpack.value[4] * (float) 0.3;
		if ((mResultX - mNodeBio[0]) <= 0) {
			mResultF = getResultF(0, mNodeBio[0], mFactorBio[0][0], mFactorBio[0][1]);
		} else if ((mResultX - mNodeBio[1]) <= 0) {
			mResultF = getResultF(mNodeBio[0], mNodeBio[1], mFactorBio[1][0], mFactorBio[1][1]);
		} else if ((mResultX - mNodeBio[2]) <= 0) {
			mResultF = getResultF(mNodeBio[1], mNodeBio[2], mFactorBio[2][0], mFactorBio[2][1]);
		} else if (mResultX - mNodeBio[3] <= 0) {
			mResultF = getResultF(mNodeBio[2], mNodeBio[3], mFactorBio[3][0], mFactorBio[3][1]);
		} else if (mResultX - mNodeBio[4] <= 0) {
			mResultF = getResultF(mNodeBio[3], mNodeBio[4], mFactorBio[4][0], mFactorBio[4][1]);
		} else if (mResultX - mNodeBio[5] <= 0) {
			mResultF = getResultF(mNodeBio[4], mNodeBio[5], mFactorBio[5][0], mFactorBio[5][1]);
		} else if (mResultX - mNodeBio[6] <= 0) {
			mResultF = getResultF(mNodeBio[5], mNodeBio[6], mFactorBio[6][0], mFactorBio[6][1]);
		} else if (mResultX - mNodeBio[7] <= 0) {
			mResultF = getResultF(mNodeBio[6], mNodeBio[7], mFactorBio[7][0], mFactorBio[7][1]);
		} else if (mResultX - mNodeBio[8] <= 0) {
			mResultF = getResultF(mNodeBio[7], mNodeBio[8], mFactorBio[8][0], mFactorBio[8][1]);
		} else if (mResultX - mNodeBio[9] <= 0) {
			mResultF = getResultF(mNodeBio[8], mNodeBio[9], mFactorBio[9][0], mFactorBio[9][1]);
		}
		mResultZ = mResultX * mResultF;
		mResult = (float) Math.round((mResultZ / 18.0) * 10) / 10;
		// // 数值显示到界面
		// if ((mResultZ < 10) || (mResultX < 10)) {
		// mTextViewVersion3Value.setText("Lo");
		// } else if ((mResultZ > 600) || (mResultX > 600)) {
		// mTextViewVersion3Value.setText("Hi");
		// } else {
		// mTextViewVersion3Value.setText(String.valueOf(mResult));
		// }
	}

	private float getResultF(float m0, float m1, float n0, float n1) {
		return (float) (n0 + (n1 - n0) / (m1 - m0) * (mResultX - m0));
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 插入试纸对应的按键响应
		if (keyCode == KeyEvent.KEYCODE_MUTE && !isKeyDown) {
			isKeyDown = true;
			LogUtil.e(TAG + "411", "插入试纸");
			// // 显示试纸图标
			// mImageViewPaper.setVisibility(ImageView.VISIBLE);
			// // 清空最终值的显示；
			// mTextViewVersion3Value.setText("--.-");
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// 拔出试纸对应的按键响应
		if (keyCode == KeyEvent.KEYCODE_MUTE) {
			LogUtil.e(TAG + "424", "拔出试纸");
			isKeyDown = false;
			mVoiceCount = 0;// 初始化声音定时器
			myMeasurePopup.isInsert = false;// 判断是否插入试纸的标志位,用来判断是跳转到插入试纸动画还是滴血动画。
			if (isRight) {
				isRight = false;
				// 如果正在滴血演示动画界面，则返回到插入试纸演示动画界面
				if (myMeasurePopup.isStaxisDemon) {
					myMeasurePopup.exitStaxisDemon();
					myMeasurePopup.showInsertDemon();
				}
			}
			// // 隐藏试纸图标的显示
			// mImageViewPaper.setVisibility(ImageView.INVISIBLE);
		}
		return super.onKeyUp(keyCode, event);
	}

	// 注册广播
	public void getbroadcast() {
		mCloseActivityReceiver = new CloseActivityReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(CloseActivityReceiver.CLOSE_ACTIVITY);
		// filter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
		// filter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
		registerReceiver(mCloseActivityReceiver, filter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCloseActivityReceiver != null) {
			unregisterReceiver(mCloseActivityReceiver);
		}
	}

}
