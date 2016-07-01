package com.bioland.adapter.health;

import java.util.List;

import com.bioland.adapter.BaseAdapterItem;
import com.bioland.adapter.SBaseAdapter;
import com.bioland.g32glu.R;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Administrator Adapter类
 */
public class HealthBaseAdapter extends SBaseAdapter {

	public HealthBaseAdapter(Context context, List<?> data) {
		super(context, data, R.layout.health_listview_item);
	}

	@Override
	public int getCount() {
		if (data.size() < 20)
			return data.size();
		else
			return 20;
	}

	@Override
	protected void newView(View convertView) {
		ViewHolder holder = new ViewHolder();
		holder.mLayoutItem = (LinearLayout) convertView.findViewById(R.id.layout_health_item);
		holder.mImageView1 = (ImageView) convertView.findViewById(R.id.img_health_item_icon);
		holder.mTextView1 = (TextView) convertView.findViewById(R.id.txt_health_item_hint1);
		holder.mTextView2 = (TextView) convertView.findViewById(R.id.txt_health_item_hint2);

		convertView.setTag(holder);
	}

	@Override
	protected void holderView(View convertView, Object itemObject, int position) {
		ViewHolder holder = (ViewHolder) convertView.getTag();

		final BaseAdapterItem lBaseAdapterItem = (BaseAdapterItem) itemObject;
		// 重置图片和文本
		holder.mImageView1.setImageResource(lBaseAdapterItem.getImageView1());
		holder.mTextView1.setText(lBaseAdapterItem.getTextView1());
		holder.mTextView2.setText(lBaseAdapterItem.getTextView2());

		setWidgetVisibility(holder);

		// item按键事件
		holder.mLayoutItem.setOnClickListener(new onHistoryDeleteListener(position, holder));

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
			case R.id.layout_health_item:
				Toast.makeText(context, "position:" + position, Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	}

	class ViewHolder {
		LinearLayout mLayoutItem;
		ImageView mImageView1;
		ImageView mImageView2;
		TextView mTextView1;
		TextView mTextView2;
		TextView mTextView3;
	}

}
