package com.bioland.activity.wlan;

import java.util.List;

import com.bioland.adapter.wlan.WlanBaseAdapter;
import com.bioland.g32glu.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class WLanActivity extends Activity implements OnClickListener {

	private WifiManager wifiManager;
	List<ScanResult> list;
	private ImageView mImageViewArrow;

	/**
	 * 调用此活动的类需要用此方法调用此活动
	 * 
	 * @param context
	 * @param data1
	 */
	public static void actionStart(Context context, int data1) {
		Intent intent = new Intent(context, WLanActivity.class);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_wlan);
		initView();
		init();
	}

	private void initView() {
		mImageViewArrow = (ImageView) findViewById(R.id.arrow_return);
		mImageViewArrow.setOnClickListener(this);
	}

	private void init() {
		wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);// 获取wifi管理
		openWifi();
		list = wifiManager.getScanResults();// 通过wifi管理获取扫描结果
		ListView listView = (ListView) findViewById(R.id.wlan_listView);
		if (list == null) {
			Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
		} else {
			// listView.setAdapter(new MyAdapter(this, list));
			listView.setAdapter(new WlanBaseAdapter(this, list));
		}
	}

	/**
	 * 打开WIFI
	 */
	private void openWifi() {
		if (!wifiManager.isWifiEnabled()) {
			wifiManager.setWifiEnabled(true);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.arrow_return:
			finish();
			break;
		default:
			break;
		}
	}

	// public class MyAdapter extends BaseAdapter {
	//
	// LayoutInflater inflater;
	// List<ScanResult> list;
	//
	// public MyAdapter(Context context, List<ScanResult> list) {
	// // TODO Auto-generated constructor stub
	// this.inflater = LayoutInflater.from(context);
	// this.list = list;
	// }
	//
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return list.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return position;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// // TODO Auto-generated method stub
	// View view = null;
	// view = inflater.inflate(R.layout.wlan_listview_item, null);
	// ScanResult scanResult = list.get(position);
	// TextView textView = (TextView) view.findViewById(R.id.txt_wlan_item);
	// textView.setText(scanResult.SSID);// scanResult.SSID为 wifi名称
	// TextView signalStrenth = (TextView)
	// view.findViewById(R.id.signal_strenth);
	// signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
	// ImageView imageView = (ImageView) view.findViewById(R.id.img_wlan_item);
	// // 判断信号强度，显示对应的指示图标
	// if (Math.abs(scanResult.level) > 100) {
	// imageView.setImageDrawable(getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_0));
	// } else if (Math.abs(scanResult.level) > 80) {
	// imageView.setImageDrawable(getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_1));
	// } else if (Math.abs(scanResult.level) > 70) {
	// imageView.setImageDrawable(getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_1));
	// } else if (Math.abs(scanResult.level) > 60) {
	// imageView.setImageDrawable(getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_2));
	// } else if (Math.abs(scanResult.level) > 50) {
	// imageView.setImageDrawable(getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_3));
	// } else {
	// imageView.setImageDrawable(getResources().getDrawable(R.drawable.wlan_stat_sys_wifi_signal_4));
	// }
	// return view;
	// }
	//
	// }

}
