package com.bioland.activity.wlan;

import java.util.ArrayList;
import java.util.List;

import com.bioland.activity.wlan.WifiAdmin.WifiCipherType;
import com.bioland.g32glu.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class WLanActivity extends Activity implements OnItemClickListener, OnClickListener {

	private ImageView mImageViewArrow;
	private ImageView mImageViewSwitch;

	private ListView wifiList;
	private List<ScanResult> list;
	private ScanResult mScanResult;
	private WifiAdmin mWifiAdmin;
	private WifiConnListAdapter mConnList;
	private ArrayList<WifiElement> wifiElement = new ArrayList<WifiElement>();
	private boolean isOpen = false;

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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more_wlan);
		mWifiAdmin = new WifiAdmin(WLanActivity.this);
		initView();
	}

	private void initView() {
		wifiList = (ListView) this.findViewById(R.id.wlan_listView);
		mImageViewArrow = (ImageView) findViewById(R.id.arrow_return);
		mImageViewSwitch = (ImageView) findViewById(R.id.img_wlan_switch);
		if (mWifiAdmin.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
			mImageViewSwitch.setBackgroundResource(R.drawable.wifi_off);
		} else {
			mImageViewSwitch.setBackgroundResource(R.drawable.wifi_on);
			isOpen = true;
		}
		mImageViewArrow.setOnClickListener(this);
		mImageViewSwitch.setOnClickListener(this);
		wifiList.setOnItemClickListener(this);
		// new ScanWifiThread().start();
		mConnList = new WifiConnListAdapter(WLanActivity.this, getAllNetWorkList());
		// mConnList = new WifiConnListAdapter(getApplicationContext(),
		// getAllNetWorkList());
		wifiList.setAdapter(mConnList);
	}

	// private void init() {
	// // wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);//
	// // 获取wifi管理
	// mWifiAdmin = new WifiAdmin(this);
	// mWifiAdmin.OpenWifi();
	// mWifiAdmin.startScan();
	// list = mWifiAdmin.getWifiList();// 通过wifi管理获取扫描结果
	// // list = wifiManager.getScanResults();// 通过wifi管理获取扫描结果
	// ListView listView = (ListView) findViewById(R.id.wlan_listView);
	// if (list == null) {
	// Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
	// } else {
	// // listView.setAdapter(new MyAdapter(this, list));
	// listView.setAdapter(new WlanBaseAdapter(this, list, mWifiAdmin));
	// }
	// }

	/**
	 * 扫描wifi线程
	 * 
	 * @author passing
	 * 
	 */
//	class ScanWifiThread extends Thread {
//
//		@Override
//		public void run() {
//			while (true) {
//				currentWifiInfo = wifiManager.getConnectionInfo();
//				startScan();
//				try {
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					break;
//				}
//			}
//		}
//	}

	/**
	 * 扫描wifi
	 */
//	public void startScan() {
//		wifiManager.startScan();
//		// 获取扫描结果
//		wifiList = wifiManager.getScanResults();
//		str = new String[wifiList.size()];
//		String tempStr = null;
//		for (int i = 0; i < wifiList.size(); i++) {
//			tempStr = wifiList.get(i).SSID;
//			if (null != currentWifiInfo && tempStr.equals(currentWifiInfo.getSSID())) {
//				tempStr = tempStr + "(已连接)";
//			}
//			str[i] = tempStr;
//		}
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.arrow_return:
			finish();
			break;
		// case R.id.wifi_conn_scan_btn:
		// mConnList = new WifiConnListAdapter(this, getAllNetWorkList());
		// // mConnList = new WifiConnListAdapter(getApplicationContext(),
		// // getAllNetWorkList());
		// wifiList.setAdapter(mConnList);
		// break;
		case R.id.img_wlan_switch:
			if (isOpen) {
				Toast.makeText(getApplicationContext(), "正在关闭wifi", Toast.LENGTH_SHORT).show();
				if (mWifiAdmin.closeWifi()) {
					Toast.makeText(getApplicationContext(), "wifi关闭成功", Toast.LENGTH_SHORT).show();
					mImageViewSwitch.setBackgroundResource(R.drawable.wifi_off);
					isOpen = false;
				} else {
					Toast.makeText(getApplicationContext(), "wifi关闭失败", Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(getApplicationContext(), "正在打开wifi", Toast.LENGTH_SHORT).show();
				if (mWifiAdmin.OpenWifi()) {
					Toast.makeText(getApplicationContext(), "wifi打开成功", Toast.LENGTH_SHORT).show();
					mImageViewSwitch.setBackgroundResource(R.drawable.wifi_on);
					isOpen = true;
				} else {
					Toast.makeText(getApplicationContext(), "wifi打开失败", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		default:
			break;
		}
	}

	private String initShowConn() {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		String s = wifiInfo.getSSID() + "    IP地址:" + mWifiAdmin.ipIntToString(wifiInfo.getIpAddress()) + "    Mac地址："
				+ wifiInfo.getMacAddress();
		return s;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
		// TODO Auto-generated method stub
		final String ssid = wifiElement.get(position).getSsid();
		Builder dialog = new AlertDialog.Builder(this);
		// Builder dialog = new AlertDialog.Builder(WLanActivity.this);
		final WifiConfiguration wifiConfiguration = mWifiAdmin.IsExsits(ssid);
		dialog.setTitle("是否连接");
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (null == wifiConfiguration) {
					setMessage(ssid);
				} else {
					mWifiAdmin.Connect(wifiConfiguration);
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		}).setNeutralButton("移除", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if (null != wifiConfiguration) {
					int id = wifiConfiguration.networkId;
					System.out.println("id>>>>>>>>>>" + id);
					mWifiAdmin.removeNetworkLink(id);
				}
			}
		}).create();
		dialog.show();
	}

	private void setMessage(final String ssid) {
		Builder dialog = new AlertDialog.Builder(WLanActivity.this);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		LinearLayout lay = (LinearLayout) inflater.inflate(R.layout.wlan_pwd, null);
		dialog.setView(lay);
		final EditText pwd = (EditText) lay.findViewById(R.id.wifi_pwd_edit);
		dialog.setTitle(ssid);
		dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				String pwdStr = pwd.getText().toString();
				Log.e("WifiAdmin", "按键总进来了把");
				boolean flag = mWifiAdmin.Connect(ssid, pwdStr, WifiCipherType.WIFICIPHER_WPA);
				if (flag) {
					Toast.makeText(getApplicationContext(), "正在连接，请稍后", Toast.LENGTH_SHORT).show();
				} else {
					showLog("链接错误");
				}
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		}).create();
		dialog.show();
	}

	private ArrayList<WifiElement> getAllNetWorkList() {
		// 每次点击扫描之前清空上一次的扫描结果
		wifiElement.clear();
		// 开始扫描网络
		mWifiAdmin.startScan();
		list = mWifiAdmin.getWifiList();
		WifiElement element;
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				// 得到扫描结果
				mScanResult = list.get(i);
				element = new WifiElement();
				element.setSsid(mScanResult.SSID);
				element.setBssid(mScanResult.BSSID);
				element.setCapabilities(mScanResult.capabilities);
				element.setFrequency(mScanResult.frequency);
				element.setLevel(mScanResult.level);
				wifiElement.add(element);
			}
		}
		return wifiElement;
	}

	/**
	 * 提示信息对话框
	 * 
	 * @param msg
	 */
	private void showLog(final String msg) {
		new AsyncTask<Void, Void, String>() {

			@Override
			protected String doInBackground(Void... params) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				Dialog dialog = new AlertDialog.Builder(WLanActivity.this).setTitle("提示").setMessage(msg)
						.setNegativeButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub

					}
				}).create();// 创建
				// 显示对话框
				dialog.show();
			}

		}.execute();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter ins = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		registerReceiver(netConnReceiver, ins);
	}

	private BroadcastReceiver netConnReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {

				if (checknet()) {
					Log.e("111111>>>>>>>>>>", "成功");
					// showConn.setText("已连接： " + initShowConn());
				} else {
					Log.e("22222222>>>>>>>>>>", "失败");
					// showConn.setText("正在尝试连接： " + initShowConn());

				}
			}
		}

	};

	/**
	 * 获取网络
	 */
	private NetworkInfo networkInfo;

	/**
	 * 监测网络链接
	 * 
	 * @return true 链接正常 false 链接断开
	 */
	private boolean checknet() {
		ConnectivityManager connManager = (ConnectivityManager) this.getSystemService(CONNECTIVITY_SERVICE);
		// 获取代表联网状态的NetWorkInfo对象
		networkInfo = connManager.getActiveNetworkInfo();
		if (null != networkInfo) {
			return networkInfo.isAvailable();
		}
		return false;
	}
}
