/*
 * TimeSelectView.java
 * Create at 2015-8-26 上午9:30:15
 */
package com.bioland.view.timeselect;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.Toast;

public class TimeSelectDayView extends View implements OnTouchListener, SelectListener {

	private Context context;
	private boolean isMeasure = false;
	private Bitmap bmp;
	private Path path;// 裁减路径
	private Paint textPaint, bgPaint;
	private float viewW, viewH, viewHighHalf;

	private Paint bmpPaint, linePaint;

	private ArrayList<SelectItem> items = new ArrayList<SelectItem>();

	private float startX, startY;

	private int maxItem = 5;

	private float defaultTextSize = 80;
	private double down;
	private double[] beforeTouchTime = new double[2];

	private int addAlpha = 30;
	SelListener sl;
	OnFilingListener fl;

	private static final String TAG = "TimeSelectDayView";

	public static String mDataContext;

	private void initPaint() {
		bmpPaint = new Paint();
		bmpPaint.setAntiAlias(true);
		bmpPaint.setStyle(Paint.Style.FILL);

		textPaint = new Paint();
		textPaint.setColor(Color.rgb(120, 103, 173));
		textPaint.setTypeface(Typeface.DEFAULT);

		bgPaint = new Paint();
		bgPaint.setAntiAlias(true);
		bgPaint.setStyle(Paint.Style.FILL);
		bgPaint.setColor(Color.rgb(255, 255, 255));

		linePaint = new Paint();
		linePaint.setAntiAlias(true);
		linePaint.setStyle(Paint.Style.FILL);
		linePaint.setColor(Color.rgb(56, 123, 238));

		roundPaint = new Paint();
		roundPaint.setColor(Color.rgb(120, 103, 173)); // 设置圆环的颜�?
		roundPaint.setStyle(Paint.Style.STROKE); // 设置空心
		roundPaint.setStrokeWidth(2); // 设置圆环的宽�?
		roundPaint.setAntiAlias(true); // 消除锯齿

	}

	public TimeSelectDayView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
		initPaint();
	}

	public TimeSelectDayView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initPaint();
	}

	public TimeSelectDayView(Context context) {
		super(context);
		this.context = context;
		initPaint();
	}

	float windowWidth, windowHeight;

	@Override
	protected void onLayout(boolean changed, int mleft, int top, int right, int bottom) {
		super.onLayout(changed, mleft, top, right, bottom);
		if (!isMeasure) {
			setOnTouchListener(this);
			WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

			windowWidth = wm.getDefaultDisplay().getWidth();
			windowHeight = wm.getDefaultDisplay().getHeight();

			viewW = getWidth();
			viewH = getHeight();
			viewHighHalf = viewH / 2;

			path = new Path();
			path.addRect(0, 0, viewW, viewH, Path.Direction.CCW);

			itemHeight = viewH / (maxItem);
			defaultTextSize = (float) 36;
			left = 0;// 文本显示框的x坐标

			// fl = new OnFilingListener() {
			//
			// @Override
			// public void onFiling() {
			// onFill();
			// }
			// };
			isMeasure = true;
		}

	}

	void onFill(final float margin, final float time, final MotionEvent event) {

		t = new Thread(new Runnable() {

			@Override
			public void run() {
				isRun = true;
				float mTime = Math.abs(time) * 10;
				float percent = Math.abs(margin) / mTime;
				float dY = percent * itemHeight;
				float dTime = mTime * 10 * viewHighHalf / (windowWidth * itemHeight);
				float temp = percent * itemHeight / (time * 10);
				for (; mTime > 0;) {
					if (isRun) {
						try {
							dY = temp * mTime;
							if (margin > 0)
								sortItem(-dY);
							else
								sortItem(dY);
							mTime -= dTime;
							myHandler.sendEmptyMessage(1);
							Thread.sleep(10);

							if (items.get(0).rect.top - itemHeight > viewHighHalf
									|| items.get(items.size() - 1).rect.top + itemHeight * 2 < viewHighHalf) {
								break;
							}
						} catch (InterruptedException e) {
						}
					} else {
						break;
					}
				}

				isRun = false;
				onUp(event);
			}
		});
		t.start();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	Paint roundPaint;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.clipPath(path);
		int size = items.size();
		float sX = 0;
		float sY = viewHighHalf - itemHeight / 2;
		float stopX = viewW;

		// 当前显示项背景
		canvas.drawRect(0, sY, stopX, sY + itemHeight, bgPaint);
		canvas.drawRect(0, sY, 8, sY + itemHeight, linePaint);

		float textWidth = 0;
		// 设置字体大小,颜色和位置
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(viewH)) {
				SelectItem item = items.get(i);
				if ((item.rect.top < 271.8) && (item.rect.top > 177.2)) {
					textPaint.setTextSize((float) (defaultTextSize + 24));
					textPaint.setColor(Color.rgb(56, 123, 238));
					textWidth = (item.text.length() - 1) * (defaultTextSize + 24);
					mDataContext = item.text;
				} else if ((item.rect.top > 82.6) && (item.rect.top < 177.2)) {
					textPaint.setTextSize((float) (defaultTextSize + 8));
					textPaint.setColor(Color.rgb(130, 130, 130));
					textWidth = (item.text.length() - 1) * (defaultTextSize + 8);
				} else if (item.rect.top < 82.6) {
					textPaint.setTextSize((float) (defaultTextSize));
					textPaint.setColor(Color.rgb(200, 200, 200));
					textWidth = (item.text.length() - 1) * (defaultTextSize);
				} else if ((item.rect.top > 271.8) && (item.rect.top < 366.4)) {
					textPaint.setTextSize((float) (defaultTextSize + 8));
					textPaint.setColor(Color.rgb(130, 130, 130));
					textWidth = (item.text.length() - 1) * (defaultTextSize + 8);
				} else if (item.rect.top > 366.4) {
					textPaint.setTextSize((float) (defaultTextSize));
					textPaint.setColor(Color.rgb(200, 200, 200));
					textWidth = (item.text.length() - 1) * (defaultTextSize);
				}
				// 显示文本位置
				// Toast.makeText(context, "top:" + item.rect.top + ",bottom:" +
				// item.rect.bottom + ",text:" + item.text,
				// Toast.LENGTH_SHORT).show();
				// 设置透明度
				// textPaint.setAlpha((int) textPaint.getTextSize() +
				// addAlpha);
				// 画文本
				canvas.drawText(item.text, viewW / 2 - textWidth / 2,
						item.rect.top + itemHeight * 5 / 12 + textPaint.getTextSize() / 2, textPaint);
			}
		}

		// 当前显示项的上下两条线
		canvas.drawLine(sX, sY, stopX, sY, linePaint);
		canvas.drawLine(sX, sY + itemHeight, stopX, sY + itemHeight, linePaint);
		canvas.drawLine(stopX - 1, sY, stopX - 1, sY + itemHeight, linePaint);
	}

	float itemHeight;

	float moveLength, margin;

	private void sortItem(float move) {
		int size = items.size();
		float right, top;

		for (int i = 0; i < size; i++) {
			top = items.get(i).rect.top + move;
			right = getWidth();
			items.get(i).setRect(new RectF(left, top, right, top + itemHeight));
		}
	}

	float left;

	public void init(final ArrayList<String> lst) {
		if (lst == null || lst.size() == 0) {
			Toast.makeText(getContext(), "数据不能为空", 0).show();
			return;
		}
		new Thread(new Runnable() {

			@Override
			public void run() {

				while (!isMeasure) {
					try {
						Thread.sleep(50);
					} catch (InterruptedException e) {
					}
				}
				setItems(lst);
				setMid();
			}
		}).start();
	}

	private void setItems(ArrayList<String> lst) {

		int length = lst.size();
		for (int i = 0; i < length; i++) {
			SelectItem item = new SelectItem(new RectF(), lst.get(i));
			items.add(item);
		}
		int size = items.size();
		float top;
		float right;

		for (int i = 0; i < size; i++) {
			top = (i - size / 2 + maxItem / 2) * itemHeight;
			right = getWidth();
			items.get(i).setRect(new RectF(left, top, right, top + itemHeight));
		}

		myHandler.sendEmptyMessage(1);
	}

	@Override
	protected void onDetachedFromWindow() {
		if (bmp != null) {
			bmp.recycle();
			bmp = null;
		}
		if (t != null && t.isAlive()) {
			isRun = false;
			t = null;
		}
		super.onDetachedFromWindow();

	}

	Thread t;
	boolean isRun = false;
	boolean isMovew;
	private float downY;

	@Override
	public boolean onTouch(View arg0, MotionEvent event) {

		if (isRun) {
			isRun = false;
			return true;
		}

		// if (!isInTouchArea(event)) {
		// onUp(event);
		// return true;
		// }
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (!isInTouchArea(event)) {
				startY = -1;
			} else {
				startY = event.getY();
			}
			downY = startY;
			down = System.currentTimeMillis();
			isMovew = false;
			break;
		case MotionEvent.ACTION_MOVE:
			if (startY != -1) {
				if (Math.abs((event.getY() - startY)) >= itemHeight / 50) {
					isMovew = true;
					sortItem(event.getY() - startY);
					invalidate();
					startY = event.getY();
					double temp = beforeTouchTime[0];
					beforeTouchTime[0] = System.currentTimeMillis();
					beforeTouchTime[1] = temp;
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			if (startY != -1) {
				if (!isMovew || (System.currentTimeMillis() - down) >= 500) {
					if ((System.currentTimeMillis() - down) < 200 && !isMovew) {
						if (sl != null && getItem(event) != null) {
							sl.onClick(getItem(event).text);
						}
						if (isInTouchArea(event)) {
							select(event);
						}

						if (getItem(event) == null || !isInTouchArea(event)) {
							if (!isRun) {
								setMid();
							}
						}

						return true;
					}
					onUp(event);
				} else {
					if (items.get(0).rect.top + itemHeight / 2 > viewHighHalf
							|| items.get(items.size() - 1).rect.top + itemHeight / 2 < viewHighHalf) {
						onUp(event);
					} else {
						onFill(downY - event.getY(), (float) (System.currentTimeMillis() - down), event);
					}
				}
			}
			break;
		default:
			break;
		}
		return true;
	}

	private void onUp(MotionEvent event) {
		// else if (isMovew) {
		// setScroll(downY - event.getY(), System.currentTimeMillis() -
		// down);
		// }
		if (items.get(0).rect.top + itemHeight / 2 > viewHighHalf) {
			t = new Thread(new Runnable() {
				@Override
				public void run() {
					isRun = true;
					for (int i = (int) items.get(0).rect.top; i > viewHighHalf - itemHeight / 2; i -= itemHeight / 10) {
						try {
							if (isRun) {
								sortItem(-itemHeight / 10);
								Thread.sleep(10);
								myHandler.sendEmptyMessage(1);
							} else {
								break;
							}
						} catch (InterruptedException e) {
						}
					}
					isRun = false;
					setMid();
				}
			});
			t.start();
		} else if (items.get(items.size() - 1).rect.top + itemHeight / 2 < viewHighHalf) {
			t = new Thread(new Runnable() {
				@Override
				public void run() {
					isRun = true;
					for (int i = (int) items.get(items.size() - 1).rect.top; i < viewHighHalf
							- itemHeight / 2; i += itemHeight / 10) {
						try {
							if (isRun) {
								sortItem(itemHeight / 10);
								Thread.sleep(10);
								myHandler.sendEmptyMessage(1);
							} else {
								break;
							}
						} catch (InterruptedException e) {
						}
					}
					isRun = false;
					setMid();
				}
			});
			t.start();
		} else {
			startY = event.getY();
			if (!isRun) {
				setMid();
			}
		}
	}

	Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			invalidate();
		}

	};

	/**
	 * 点击数字，数字滚动到正中间
	 * 
	 * @param event
	 */
	private void select(MotionEvent event) {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(viewH)) {
				if (items.get(i).isInRect(event.getX(), event.getY())) {
					margin = items.get(i).rect.top - (viewHighHalf - itemHeight / 2);
					if (Math.abs(margin) >= itemHeight / 2) {

						t = new Thread(new Runnable() {

							@Override
							public void run() {
								isRun = true;
								for (float i = Math.abs(margin); i > 0; i -= Math.abs(margin) / 10) {
									try {
										if (isRun) {
											sortItem(-margin / 10);
											Thread.sleep(10);
											myHandler.sendEmptyMessage(1);
										} else {
											break;
										}
									} catch (InterruptedException e) {
									}
								}
								isRun = false;
								setMid();
							}
						});
						t.start();
					}
					break;
				}
			}
		}
	}

	private void setMid() {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			margin = items.get(i).rect.top - (viewHighHalf - itemHeight / 2);
			if (Math.abs(margin) <= itemHeight / 2 && Math.abs(margin) > 0) {

				t = new Thread(new Runnable() {

					@Override
					public void run() {
						isRun = true;
						for (float i = Math.abs(margin); i > Math.abs(margin) / 10; i -= Math.abs(margin) / 10) {
							try {
								if (isRun) {
									sortItem(-margin / 10);
									Thread.sleep(10);
									myHandler.sendEmptyMessage(1);
								} else {
									break;
								}
							} catch (InterruptedException e) {
							}
						}
						margin = 0;
						isRun = false;
					}
				});
				t.start();
				break;
			}

		}
	}

	private void setScroll(final float move, final double moveTime) {
		t = new Thread(new Runnable() {

			@Override
			public void run() {
				isRun = true;
				double threadMoveTime = moveTime / 100;
				float minMargin = (float) (Math.abs(move) / moveTime);
				for (float i = Math.abs(move); i > minMargin; i -= minMargin) {
					try {
						if (isRun) {
							threadMoveTime += moveTime / 100;
							Log.d("", "threadMoveTime  ===>" + threadMoveTime);
							Log.d("", "-move / threadMoveTime  ===>" + (-move / threadMoveTime));
							sortItem((float) (-move / threadMoveTime));
							Thread.sleep(10);
							myHandler.sendEmptyMessage(1);
						} else {
							break;
						}
					} catch (InterruptedException e) {
					}
				}
				isRun = false;
				setMid();
			}
		});
		t.start();
	}

	public SelectItem getItem(MotionEvent event) {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(viewH)) {
				if (items.get(i).isInRect(event.getX(), event.getY())) {
					return items.get(i);
				}
			}
		}
		return null;
	}

	/**
	 * 设置触摸滚动的面积
	 * 
	 * @param event
	 * @return
	 */
	private boolean isInTouchArea(MotionEvent event) {
		if (event.getX() >= 0 && event.getX() <= viewW && event.getY() > 0 && event.getY() < viewH) {
			return true;
		}
		return false;
	}

	public String getText() {
		int size = items.size();
		for (int i = 0; i < size; i++) {
			if (items.get(i).isVisible(viewH)) {
				if (items.get(i).isInRect(viewW / 2, viewHighHalf)) {
					return items.get(i).text;
				}
			}
		}
		return "";
	}

	@Override
	public void onSelectListener(SelListener sl) {
		if (sl != null) {
			this.sl = sl;
		}
	}

}
