package com.bioland.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class SBaseAdapter extends BaseAdapter {

	protected Context context;
	private LayoutInflater inflater;
	protected List<?> data;
	private int resouceId;
	// item颜色是否交替标志位，true交替，false不交替
	private boolean mItemColorAlternate = false;
	private int noSelectItemColor1;
	private int noSelectItemColor2;

	public SBaseAdapter(Context context, List<?> data, int resourceId) {
		this.context = context;
		this.data = data;
		this.inflater = LayoutInflater.from(context);
		this.resouceId = resourceId;
	}

	// 设置最大Item数
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return convertId(position, data.get(position));
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(resouceId, null, false);
			newView(convertView);
		}
		holderView(convertView, data.get(position), position);

		setSelectItemColor(position, convertView);
		return convertView;
	}

	public void setData(List<?> data) {
		this.data = data;
	}

	public List<?> getData() {
		return data;
	}

	/**
	 * 用于覆盖，在各个其他adapter里边返回id,默认返回position
	 * 
	 * @param position
	 * @param object
	 * @return
	 */
	protected long convertId(int position, Object object) {
		return position;
	}

	/**
	 * 设置选中和没选中的Item颜色
	 * 
	 * @param position
	 * @param convertView
	 */
	protected void setSelectItemColor(int position, View convertView) {
		// 设置item背景色
		if (mItemColorAlternate) {
			if (position % 2 == 0) {
				convertView.setBackgroundColor(getNoSelectItemColor1());
			} else {
				convertView.setBackgroundColor(getNoSelectItemColor2());
			}
		}
	}

	/**
	 * listview设置成两种颜色交替显示效果
	 * 
	 * @param noSelectItemColor1
	 * @param noSelectItemColor2
	 */
	public void setItemColor(int noSelectItemColor1, int noSelectItemColor2, boolean itemColorAlternate) {
		this.noSelectItemColor1 = noSelectItemColor1;
		this.noSelectItemColor2 = noSelectItemColor2;
		mItemColorAlternate = itemColorAlternate;
	}

	protected int getNoSelectItemColor1() {
		return noSelectItemColor1;
	}

	protected int getNoSelectItemColor2() {
		return noSelectItemColor2;
	}

	/**
	 * 第一次创建的时�?调用
	 * 
	 * @param convertView
	 */
	protected abstract void newView(View convertView);

	/**
	 * 用于数据赋�?等等
	 * 
	 * @param convertView
	 * @param itemObject
	 */
	protected abstract void holderView(View convertView, Object itemObject, int position);
}
