package com.bioland.adapter.wlan;

import java.util.List;

import com.bioland.adapter.SBaseAdapter;
import com.bioland.adapter.remind.RemindAddTimeBaseAdapter;
import com.bioland.g32glu.R;
import com.bioland.widget.dialog.RemindTimeDeleteDialog;
import com.bioland.widget.dialog.WlanDialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator Adapter类
 */
public class WlanBaseAdapter extends SBaseAdapter {

	public WlanBaseAdapter(Context context, List<?> data) {
		super(context, data, R.layout.wlan_listview_item);
	}

	// 设置最大增加Item数
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	protected void newView(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.mLayoutItem = (LinearLayout) convertView.findViewById(R.id.layout_wlan_item);
		holder.mImageView = (ImageView) convertView.findViewById(R.id.img_wlan_item);
		holder.mTextView1 = (TextView) convertView.findViewById(R.id.txt_wlan_item);
		holder.mTextView2 = (TextView) convertView.findViewById(R.id.signal_strenth);
		convertView.setTag(holder);
	}

	@Override
	protected void holderView(View convertView, Object itemObject, int position) {
		ViewHolder holder = (ViewHolder) convertView.getTag();

		ScanResult scanResult = (ScanResult) data.get(position);
		// 判断信号强度，显示对应的指示图标
		if (Math.abs(scanResult.level) > 100) {
			holder.mImageView.setImageDrawable(
					((Activity) context).getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_0));
		} else if (Math.abs(scanResult.level) > 80) {
			holder.mImageView.setImageDrawable(
					((Activity) context).getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_1));
		} else if (Math.abs(scanResult.level) > 70) {
			holder.mImageView.setImageDrawable(
					((Activity) context).getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_1));
		} else if (Math.abs(scanResult.level) > 60) {
			holder.mImageView.setImageDrawable(
					((Activity) context).getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_2));
		} else if (Math.abs(scanResult.level) > 50) {
			holder.mImageView.setImageDrawable(
					((Activity) context).getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_3));
		} else {
			holder.mImageView.setImageDrawable(
					((Activity) context).getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_4));
		}

		// 重置图片和文本
		holder.mTextView1.setText(scanResult.SSID);// scanResult.SSID为 wifi名称
		holder.mTextView2.setText(getCapabilitiesString(scanResult.capabilities));// scanResult.level是信号强度

		setWidgetVisibility(holder);

		// item按键事件
		holder.mLayoutItem.setOnClickListener(new onHistoryDeleteListener(position, holder));
		// holder.mLayoutItem.setOnLongClickListener(new OnDeleteItem(position,
		// holder));

		// 设置Item背景色
		// super.setItemColor(context.getResources().getColor(R.color.Gainsboro),
		// context.getResources().getColor(R.color.LightGrey), true);
	}

	public String getCapabilitiesString(String str) {
		int i = 0;
		String returnStr = "";
		if (str.contains("WPA-PSK")) {
			i = i | 1;
		}
		if (str.contains("WPA2-PSK")) {
			i = i | 2;
		}
		if (str.contains("WPS")) {
			i = i | 4;
		}
		switch (i) {
		case 1:
			returnStr = "通过 WPA进行保护";
			break;
		case 2:
			returnStr = "通过 WPA2进行保护";
			break;
		case 4:
			returnStr = "可使用WPS";
			break;
		case 3:
			returnStr = "通过 WPA/WPA2进行保护";
			break;
		case 5:
			returnStr = "通过 WPA进行保护（可使用WPS）";
			break;
		case 6:
			returnStr = "通过 WPA2进行保护（可使用WPS）";
			break;
		case 7:
			returnStr = "通过 WPA/WPA2进行保护（可使用WPS）";
			break;
		default:
			break;
		}
		return returnStr;
	}

	/**
	 * 设置控件是否显示
	 * 
	 * @param holder
	 */
	private void setWidgetVisibility(ViewHolder holder) {
		// holder.mbtn1.setVisibility(View.VISIBLE);
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
			case R.id.layout_wlan_item:
				showAlertDialog(String.valueOf(((ScanResult) data.get(position)).SSID));
				Toast.makeText(context, "position = " + position, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}

	public void showAlertDialog(String str) {
		WlanDialog.Builder builder = new WlanDialog.Builder(context);
		builder.setMessage(str);
		builder.setPositiveButton("连接", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				// 设置你的操作事项
			}
		});

		builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});

		builder.create().show();
	}

	// 长按事件响应
	// class OnDeleteItem implements OnLongClickListener {
	// private int position;
	// private ViewHolder holder;
	//
	// public OnDeleteItem(int position, ViewHolder holder) {
	// this.position = position;
	// this.holder = holder;
	// }
	//
	// @Override
	// public boolean onLongClick(View v) {
	// switch (v.getId()) {
	// case R.id.layout_remind_add:
	// // 删除对应listView 的 Item项
	// data.remove(position);
	// notifyDataSetChanged();
	// break;
	// default:
	// break;
	// }
	// return true;
	// }
	// }

	class ViewHolder {
		LinearLayout mLayoutItem;
		ImageView mImageView;
		TextView mTextView1;
		TextView mTextView2;
	}

}
