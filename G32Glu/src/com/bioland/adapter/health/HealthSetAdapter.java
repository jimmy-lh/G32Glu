package com.bioland.adapter.health;

import java.util.ArrayList;
import java.util.List;

import com.bioland.adapter.BaseAdapterItem;

import android.app.Activity;
import android.view.View;
import android.widget.ListView;

/**
 * @author Administrator 配置参数的历史数据的 listView
 */
public class HealthSetAdapter {
	private View mView;
	private Activity mActivity;
	/**
	 * listView 的 id
	 */
	private int lvId;

	/**
	 * listView 的元素集合
	 */
	public List<BaseAdapterItem> mList;

	/**
	 * 菜单界面中只包含了一个ListView。
	 */
	public ListView mListView;

	/**
	 * ListView的适配器。
	 */
	public HealthBaseAdapter mAdapter;

	// 一个img，三个txt
	public HealthSetAdapter(View aView, Activity aActivity, int lvId, int[] aImageViewHealthIcon,
			String[] aSingleTextView1, String[] aSingleTextView2) {
		mView = aView;
		mActivity = aActivity;
		this.lvId = lvId;
		initData(aImageViewHealthIcon, aSingleTextView1, aSingleTextView2);
	}

	private void initData(int[] aImageViewHealthIcon, String[] aSingleTextView1, String[] aSingleTextView2) {
		mList = new ArrayList<BaseAdapterItem>();
		for (int i = 0; i < aSingleTextView1.length; i++) {
			BaseAdapterItem lBaseAdapterItem = new BaseAdapterItem(aImageViewHealthIcon[i], aSingleTextView1[i],
					aSingleTextView2[i]);
			mList.add(lBaseAdapterItem);
		}
		initView();
	}

	/**
	 * 初始化ListView,配置Adapter
	 */
	private void initView() {
		mListView = (ListView) mView.findViewById(lvId);
		mAdapter = new HealthBaseAdapter(mActivity, mList);
		mListView.setAdapter(mAdapter);
	}
}
