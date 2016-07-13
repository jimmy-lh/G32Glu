package com.bioland.activity.wlan;

import java.util.ArrayList;

import com.bioland.g32glu.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiConnListAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<WifiElement> mArr;

	public WifiConnListAdapter(Context context, ArrayList<WifiElement> list) {
		this.inflater = LayoutInflater.from(context);
		this.mArr = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mArr.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mArr.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.wlan_listview_item, null);
		TextView ssid = (TextView) view.findViewById(R.id.txt_wlan_item);
		TextView wpe = (TextView) view.findViewById(R.id.signal_strenth);
		ImageView level = (ImageView) view.findViewById(R.id.img_wlan_item);
		// holder.mLayoutItem = (LinearLayout)
		// convertView.findViewById(R.id.layout_wlan_item);
		ssid.setText(mArr.get(position).getSsid());
		wpe.setText("加密类型:" + mArr.get(position).getCapabilities());
		int i = abs(mArr.get(position).getLevel());
		// 判断信号强度，显示对应的指示图标
		if (i <= 50) {
			level.setBackgroundResource(R.drawable.wlan_stat_sys_wifi_signal_0);
		} else if (i > 50 && i <= 65) {
			level.setBackgroundResource(R.drawable.wlan_stat_sys_wifi_signal_1);
		} else if (i > 65 && i <= 75) {
			level.setBackgroundResource(R.drawable.wlan_stat_sys_wifi_signal_2);
		} else if (i > 75 && i <= 90) {
			level.setBackgroundResource(R.drawable.wlan_stat_sys_wifi_signal_3);
		} else {
			level.setBackgroundResource(R.drawable.wlan_stat_sys_wifi_signal_4);
		}

		// level.setText(String.valueOf(mArr.get(position).getLevel()));
		return view;
	}

	/**
	 * 绝对值
	 * 
	 * @param num
	 * @return
	 */
	private int abs(int num) {
		return num * (1 - ((num >>> 31) << 1));
	}

}