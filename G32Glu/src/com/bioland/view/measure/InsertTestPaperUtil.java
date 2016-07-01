package com.bioland.view.measure;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.bioland.g32glu.R;
import com.bioland.utils.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

public class InsertTestPaperUtil {
	private Context context;
	private final static String TAG = "InsertTestPaperUtil";

	private ImageView mImagePaper, mImageMobile, mImageInsertMoveMobile, mImageInsertStaxis;
	private Thread paperThread, staxisThread;
	private Animation paperAnimation, insertPaperAnimation, insertMoveMobileAnimation, insertStaxisAnimation;
	private boolean isPaperThread, isStaxisThread;
	private View teView;
	private ImageView mArrowReturn;

	public InsertTestPaperUtil(Context context) {
		super();
		this.context = context;
		initView();
	}

	private void initView() {
		mImagePaper = (ImageView) ((Activity) context).findViewById(R.id.img_main_measure_testpaper);
		mImageMobile = (ImageView) ((Activity) context).findViewById(R.id.img_main_measure_mobile);
		mImageInsertMoveMobile = (ImageView) ((Activity) context).findViewById(R.id.img_main_measure_insert_mobile);
		mImageInsertStaxis = (ImageView) ((Activity) context).findViewById(R.id.img_main_measure_insert_staxis);
		teView = ((Activity) context).findViewById(R.id.main_dynamic_wave);
		mArrowReturn = (ImageView) ((Activity) context).findViewById(R.id.arrow_return);
	}

	class loadPaperThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(2300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (isPaperThread) {
				try {
					Message msg = new Message();
					msg.what = 1;
					mHandler.sendMessage(msg);
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			paperThread = null;
		}
	}

	class loadStaxisThread extends Thread {
		@Override
		public void run() {
			super.run();
			try {
				Thread.sleep(2300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			while (isStaxisThread) {
				try {
					Message msg = new Message();
					msg.what = 2;
					mHandler.sendMessage(msg);
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			staxisThread = null;
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				// 开始插入试纸的动画
				mImagePaper.startAnimation(paperAnimation);
				break;
			case 2:
				// 开始滴血的动画
				mImageInsertStaxis.startAnimation(insertStaxisAnimation);
				break;
			default:
				break;
			}
		}
	};
	private Future controlPaperThread, controlStaxisThread;

	// 演示试纸插入动画
	private void loadPaperAnimation() {
		paperAnimation = AnimationUtils.loadAnimation(context, R.anim.measure_testpaper);
		paperAnimation.setAnimationListener(new VisibilityAnimationListener(mImagePaper, 2));
		paperAnimation.setFillAfter(true);
		// 注意：此处解决快速进出导致试纸图片不显示问题(为什么这儿加句就解决了，暂时不知道原因，按我的理解这句话有没有都应该没问题才对)
		mImagePaper.startAnimation(paperAnimation);
		// 启动试纸加载的线程
		if (paperThread == null) {
			paperThread = new loadPaperThread();
		}
		// 增加线程控制器,线程没有结束也可以直接终止线程
		ExecutorService transThread = Executors.newSingleThreadExecutor();
		// 自动运行线程
		controlPaperThread = transThread.submit(paperThread);
	}

	// 血糖仪移动动画
	private void loadMobileAnimation() {
		Animation mobileAnimation = AnimationUtils.loadAnimation(context, R.anim.measure_mobile);
		mobileAnimation.setAnimationListener(new VisibilityAnimationListener(mImageMobile, 1));
		// 此处放xml中处理不知道为什么没用，所以放这儿处理
		mobileAnimation.setFillAfter(true);
		mImageMobile.startAnimation(mobileAnimation);
	}

	// 试纸插入血糖仪动画
	private void loadInsertPaper() {
		insertPaperAnimation = AnimationUtils.loadAnimation(context, R.anim.measure_insert_testpaper);
		insertPaperAnimation.setAnimationListener(new VisibilityAnimationListener(mImagePaper, 3));
		// 此处放xml中处理不知道为什么没用，所以放这儿处理
		insertPaperAnimation.setFillAfter(true);
		mImagePaper.startAnimation(insertPaperAnimation);
	}

	// 插入试纸的血糖仪移动动画
	private void loadInsertMoveMobile() {
		insertMoveMobileAnimation = AnimationUtils.loadAnimation(context, R.anim.measure_insert_move_mobile);
		insertMoveMobileAnimation.setAnimationListener(new VisibilityAnimationListener(mImageInsertMoveMobile, 4));
		// 此处放xml中处理不知道为什么没用，所以放这儿处理
		insertMoveMobileAnimation.setFillAfter(true);
		mImageInsertMoveMobile.startAnimation(insertMoveMobileAnimation);
	}

	// 滴血动画
	private void loadInsertStaxis() {
		insertStaxisAnimation = AnimationUtils.loadAnimation(context, R.anim.measure_insert_staxis);
		insertStaxisAnimation.setAnimationListener(new VisibilityAnimationListener(mImageInsertStaxis, 5));
		// 此处放xml中处理不知道为什么没用，所以放这儿处理
		insertStaxisAnimation.setFillAfter(true);
		mImageInsertStaxis.startAnimation(insertStaxisAnimation);
		// 启动试纸加载的线程
		if (staxisThread == null) {
			staxisThread = new loadStaxisThread();
		}
		// 增加线程控制器,线程没有结束也可以直接终止线程
		ExecutorService transThread = Executors.newSingleThreadExecutor();
		// 自动运行线程
		controlStaxisThread = transThread.submit(staxisThread);
	}

	// 开始插入试纸界面的animation
	public void showPaperUi() {
		// 显示返回键
		mArrowReturn.setVisibility(View.VISIBLE);
		// teView.setVisibility(View.VISIBLE);
		isPaperThread = true;

		loadMobileAnimation();
		loadPaperAnimation();
	}

	// 退出插入试纸界面的animation
	public void exitPaperUi() {
		isPaperThread = false;
		mImagePaper.setVisibility(View.GONE);
		mImageMobile.setVisibility(View.GONE);
		// 无论线程现在是否运行中，立刻终止
		controlPaperThread.cancel(true);
		paperAnimation.cancel();
		mImagePaper.clearAnimation();
		mImageMobile.clearAnimation();
	}

	private void hidePaperUi() {
		mImagePaper.setVisibility(View.GONE);
		mImageMobile.setVisibility(View.GONE);
		mImagePaper.clearAnimation();
		mImageMobile.clearAnimation();
	}

	// 结束插入试纸动画
	private void overPaperUi() {
		isPaperThread = false;
		// 无论线程现在是否运行中，立刻终止
		controlPaperThread.cancel(true);
		paperAnimation.cancel();
		mImagePaper.clearAnimation();
	}

	public void showInsertStaxis() {
		isStaxisThread = true;
		// 隐藏返回键
		mArrowReturn.setVisibility(View.GONE);
		// 如果插入试纸动画正在运行，则加载loadInsertPaper();否则直接加载loadInsertMoveMobile();
		if (isPaperThread) {
			overPaperUi();
			loadInsertPaper();
		} else {
			loadInsertMoveMobile();
		}
	}

	public void exitInsertStaxis() {
		isStaxisThread = false;
		mImageInsertMoveMobile.setVisibility(View.GONE);
		mImageInsertStaxis.setVisibility(View.GONE);

		controlStaxisThread.cancel(true);
		insertStaxisAnimation.cancel();
		mImageInsertStaxis.clearAnimation();
		mImageInsertMoveMobile.clearAnimation();
		// teView.setVisibility(View.GONE);
	}

	class VisibilityAnimationListener implements AnimationListener {
		private View mVisibilityView;
		private int step = 0;

		public VisibilityAnimationListener(View view, int step) {
			mVisibilityView = view;
			this.step = step;
		}

		public void setVisibilityView(View view) {
			mVisibilityView = view;
		}

		@Override
		public void onAnimationStart(Animation animation) {
			if (mVisibilityView != null) {
				mVisibilityView.setVisibility(View.VISIBLE);
			}
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (mVisibilityView != null) {
				switch (step) {
				case 1:
					break;
				case 2:
					break;
				case 3:
					// 进入条件为当试纸插入血糖仪的动画结束;启动已经插入试纸的血糖仪的移动动画
					hidePaperUi();
					loadInsertMoveMobile();
					break;
				case 4:
					// 进入条件为当插入试纸的血糖仪移动动画结束;启动滴血动画
					loadInsertStaxis();
					break;
				case 5:
					break;
				default:
					break;
				}
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}
}
