package com.bioland.adapter.remind;

import java.util.List;

import com.bioland.adapter.BaseAdapterItem;
import com.bioland.adapter.SBaseAdapter;
import com.bioland.db.bean.AlarmValue;
import com.bioland.db.dao.AlarmValueDao;
import com.bioland.g32glu.R;
import com.bioland.singleton.SingletonApplication;
import com.bioland.widget.dialog.RemindTimeDeleteDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Administrator Adapter类
 */
public class RemindAddTimeBaseAdapter extends SBaseAdapter {

	// 数据库相关参数
	AlarmValueDao mAlarmValueDao;
	private List<AlarmValue> alarmValue;
	private List<AlarmValue> alarmValueAll;
	// remind界面提示文本
	private TextView mTextViewRecordHint;

	public RemindAddTimeBaseAdapter(Context context, List<?> data) {
		super(context, data, R.layout.remind_listview_item);
	}

	public RemindAddTimeBaseAdapter(Context context, List<?> data, View view) {
		super(context, data, R.layout.remind_listview_item);
		mTextViewRecordHint = (TextView) view.findViewById(R.id.txt_remind_hint);
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	protected void newView(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.mLayoutItem = (LinearLayout) convertView.findViewById(R.id.layout_remind_add);
		holder.mImageView = (ImageView) convertView.findViewById(R.id.img_remind_add_on_off);
		holder.mTextView1 = (TextView) convertView.findViewById(R.id.txt_remind_time);
		holder.mTextView2 = (TextView) convertView.findViewById(R.id.txt_remind_frequency);
		holder.mTextView3 = (TextView) convertView.findViewById(R.id.txt_remind_count);

		convertView.setTag(holder);
	}

	private void initDB() {
		// 获取数据库alarm操作类
		mAlarmValueDao = SingletonApplication.getAlarmValueDao();
		alarmValueAll = mAlarmValueDao.queryAll();
	}

	@Override
	protected void holderView(View convertView, Object itemObject, int position) {
		ViewHolder holder = (ViewHolder) convertView.getTag();

		final BaseAdapterItem lBaseAdapterItem = (BaseAdapterItem) itemObject;

		initDB();

		// 重置图片和文本
		if (alarmValueAll.get(alarmValueAll.size() - 1 - position).isSetting()) {
			holder.mImageView.setImageResource(R.drawable.remind_on);
		} else {
			holder.mImageView.setImageResource(R.drawable.remind_off);
		}
		holder.mTextView1.setText(alarmValueAll.get(alarmValueAll.size() - 1 - position).getTime());
		holder.mTextView2.setText(alarmValueAll.get(alarmValueAll.size() - 1 - position).getDate());
		holder.mTextView3.setText(alarmValueAll.get(alarmValueAll.size() - 1 - position).getCount());

		setWidgetVisibility(holder);

		// item按键事件
		holder.mLayoutItem.setOnClickListener(new onHistoryDeleteListener(position, holder));
		holder.mLayoutItem.setOnLongClickListener(new OnDeleteItem(position, holder));

		// 设置Item背景色
		// super.setItemColor(context.getResources().getColor(R.color.Gainsboro),
		// context.getResources().getColor(R.color.LightGrey), true);
	}

	/**
	 * 设置控件是否显示
	 * 
	 * @param holder
	 */
	private void setWidgetVisibility(ViewHolder holder) {
	}

	// 短按事件响应
	class onHistoryDeleteListener implements OnClickListener {
		private int position;
		private ViewHolder holder;

		public onHistoryDeleteListener(int position, ViewHolder holder) {
			this.position = position;
			this.holder = holder;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.layout_remind_add:
				changeAlarmDB(holder);
				break;
			default:
				break;
			}
		}
	}

	private static int mPosition;
	private static String mCount;

	// 长按事件响应
	class OnDeleteItem implements OnLongClickListener {
		private int position;
		private ViewHolder holder;

		public OnDeleteItem(int position, ViewHolder holder) {
			this.position = position;
			this.holder = holder;
		}

		@Override
		public boolean onLongClick(View v) {
			switch (v.getId()) {
			case R.id.layout_remind_add:
				// 显示是否删除闹钟的dialog
				mCount = String.valueOf(holder.mTextView3.getText());
				mPosition = position;
				showAlertDialog();
				break;
			default:
				break;
			}
			return true;
		}
	}

	public void showAlertDialog() {
		RemindTimeDeleteDialog.Builder builder = new RemindTimeDeleteDialog.Builder(context);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项
				// 从数据库删除选中项
				deleteAlarmDB(RemindAddTimeBaseAdapter.mCount, RemindAddTimeBaseAdapter.mPosition);
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	// 从数据库删除选中项
	public void deleteAlarmDB(String count, int position) {
		mAlarmValueDao.delete(count);
		// 删除对应listView 的 Item项
		data.remove(position);
		notifyDataSetChanged();
		// 判断数据库数据是否为空
		alarmValueAll = mAlarmValueDao.queryAll();
		if (alarmValueAll.size() == 0) {
			mTextViewRecordHint.setVisibility(View.VISIBLE);
		}
	}

	// 改变数据库中闹钟是否设置
	private void changeAlarmDB(ViewHolder holder) {
		alarmValue = mAlarmValueDao.searchDate(String.valueOf(holder.mTextView3.getText()));
		if (alarmValue.get(0).isSetting()) {
			alarmValue.get(0).setSetting(false);
			holder.mImageView.setImageResource(R.drawable.remind_off);
		} else {
			alarmValue.get(0).setSetting(true);
			holder.mImageView.setImageResource(R.drawable.remind_on);
		}
		mAlarmValueDao.update(alarmValue.get(0));
	}

	class ViewHolder {
		LinearLayout mLayoutItem;
		ImageView mImageView;
		TextView mTextView1;
		TextView mTextView2;
		TextView mTextView3;
	}

}
