package com.bioland.widget.dialog;

import com.bioland.g32glu.R;
import com.bioland.utils.LogUtil;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 自定义dialog，包括边框背景等样式(不设置dialog背景和边框样式的，参考RemindTimeDeleteDialog.java)
 * 
 * @author miracle 2016年6月28日
 */
public class RecordSetTimeDialog extends AlertDialog implements OnClickListener {

	private Context context;
	// dialog控件
	private LinearLayout mLinearBeforeBreakfast;
	private LinearLayout mLinearBeforeLunch;
	private LinearLayout mLinearBeforeDinner;
	private LinearLayout mLinearBeforeSleep;
	private LinearLayout mLinearAfterBreakfast;
	private LinearLayout mLinearAfterLunch;
	private LinearLayout mLinearAfterDinner;
	private TextView mTextViewDialog[] = new TextView[7];
	private ImageView mImageViewDialog[] = new ImageView[7];
	private boolean isPress[] = new boolean[7];
	public static boolean isShow = false;

	private final static int BEFORE_BREAKFAST = 0;// 早餐前
	private final static int AFTER_BREAKFAST = 1;// 早餐后
	private final static int BEFORE_LUNCH = 2;// 中餐前
	private final static int AFTER_LUNCH = 3;// 中餐后
	private final static int BEFORE_DINNER = 4;// 晚餐前
	private final static int AFTER_DINNER = 5;// 晚餐后
	private final static int BEFORE_SLEEP = 6;// 睡觉前
	private final static int TIME_MAX = 7;// 总数

	// 测试的时间段
	public static int item = 0;

	private final static String TAG = "RecordSetTimeDialog";

	public RecordSetTimeDialog(Context context, int theme, int item) {
		super(context, theme);
		this.context = context;
		RecordSetTimeDialog.item = item;
	}

	public RecordSetTimeDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_record_details_time);
		isShow = true;
		initView();
	}

	private void initView() {
		mLinearBeforeBreakfast = (LinearLayout) findViewById(R.id.layout_dialog_record_details_before_breakfast);
		mLinearAfterBreakfast = (LinearLayout) findViewById(R.id.layout_dialog_record_details_after_breakfast);
		mLinearBeforeLunch = (LinearLayout) findViewById(R.id.layout_dialog_record_details_before_lunch);
		mLinearAfterLunch = (LinearLayout) findViewById(R.id.layout_dialog_record_details_after_lunch);
		mLinearBeforeDinner = (LinearLayout) findViewById(R.id.layout_dialog_record_details_before_dinner);
		mLinearAfterDinner = (LinearLayout) findViewById(R.id.layout_dialog_record_details_after_dinner);
		mLinearBeforeSleep = (LinearLayout) findViewById(R.id.layout_dialog_record_details_before_sleep);

		mLinearBeforeBreakfast.setOnClickListener(this);
		mLinearAfterBreakfast.setOnClickListener(this);
		mLinearBeforeLunch.setOnClickListener(this);
		mLinearAfterLunch.setOnClickListener(this);
		mLinearBeforeDinner.setOnClickListener(this);
		mLinearAfterDinner.setOnClickListener(this);
		mLinearBeforeSleep.setOnClickListener(this);

		mTextViewDialog[0] = (TextView) findViewById(R.id.txt_dialog_record_details_before_breakfast);
		mTextViewDialog[1] = (TextView) findViewById(R.id.txt_dialog_record_details_after_breakfast);
		mTextViewDialog[2] = (TextView) findViewById(R.id.txt_dialog_record_details_before_lunch);
		mTextViewDialog[3] = (TextView) findViewById(R.id.txt_dialog_record_details_after_lunch);
		mTextViewDialog[4] = (TextView) findViewById(R.id.txt_dialog_record_details_before_dinner);
		mTextViewDialog[5] = (TextView) findViewById(R.id.txt_dialog_record_details_after_dinner);
		mTextViewDialog[6] = (TextView) findViewById(R.id.txt_dialog_record_details_before_sleep);

		mImageViewDialog[0] = (ImageView) findViewById(R.id.img_dialog_record_details_before_breakfast);
		mImageViewDialog[1] = (ImageView) findViewById(R.id.img_dialog_record_details_after_breakfast);
		mImageViewDialog[2] = (ImageView) findViewById(R.id.img_dialog_record_details_before_lunch);
		mImageViewDialog[3] = (ImageView) findViewById(R.id.img_dialog_record_details_after_lunch);
		mImageViewDialog[4] = (ImageView) findViewById(R.id.img_dialog_record_details_before_dinner);
		mImageViewDialog[5] = (ImageView) findViewById(R.id.img_dialog_record_details_after_dinner);
		mImageViewDialog[6] = (ImageView) findViewById(R.id.img_dialog_record_details_before_sleep);

		// 初始化dialog的选项
		setMeasureTime(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_dialog_record_details_before_breakfast:
			setMeasureTime(BEFORE_BREAKFAST);
			dismiss();
			break;
		case R.id.layout_dialog_record_details_after_breakfast:
			setMeasureTime(AFTER_BREAKFAST);
			dismiss();
			break;
		case R.id.layout_dialog_record_details_before_lunch:
			setMeasureTime(BEFORE_LUNCH);
			dismiss();
			break;
		case R.id.layout_dialog_record_details_after_lunch:
			setMeasureTime(AFTER_LUNCH);
			dismiss();
			break;
		case R.id.layout_dialog_record_details_before_dinner:
			setMeasureTime(BEFORE_DINNER);
			dismiss();
			break;
		case R.id.layout_dialog_record_details_after_dinner:
			setMeasureTime(AFTER_DINNER);
			dismiss();
			break;
		case R.id.layout_dialog_record_details_before_sleep:
			setMeasureTime(BEFORE_SLEEP);
			dismiss();
			break;
		default:
			break;
		}
	}

	// 重新设置测试时间段
	private void setMeasureTime(int position) {
		if (isPress[position]) {
		} else {
			for (int i = 0; i < TIME_MAX; i++) {
				if (isPress[i]) {
					isPress[i] = false;
					mTextViewDialog[i].setTextColor(Color.BLACK);
					mImageViewDialog[i].setImageResource(R.drawable.btn_radio_off);
				}
			}
			isPress[position] = true;
			mTextViewDialog[position].setTextColor(context.getResources().getColor(R.color.remind_blue));
			mImageViewDialog[position].setImageResource(R.drawable.btn_radio_on);
		}
		item = position;
	}

	@Override
	protected void onStop() {
		super.onStop();
		isShow = false;
	}

}
