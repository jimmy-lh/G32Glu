package com.bioland.widget.animation;

import com.bioland.g32glu.R;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

public class WaveAnimation implements Runnable {
	private ImageView mWave1, mWave2, mWave3;
	private Context context;

	private AnimationSet mAnimationSet1, mAnimationSet2, mAnimationSet3;
	private static final int OFFSET = 800; // 每个动画的播放时间间隔,OFFSET=OFFSET_HALF/3*4;
	private static final int OFFSET_HALF = 600; // 每个动画的播放时间间隔
	private static final int MSG_WAVE2_ANIMATION = 2;
	private static final int MSG_WAVE3_ANIMATION = 3;
	private static final int MSG_WAVE_START = 4;

	public WaveAnimation(Context context) {
		super();
		this.context = context;
		init();
	}

	private void init() {
		mWave1 = (ImageView) ((Activity) context).findViewById(R.id.wave1);
		mWave2 = (ImageView) ((Activity) context).findViewById(R.id.wave2);
		mWave3 = (ImageView) ((Activity) context).findViewById(R.id.wave3);

		mAnimationSet1 = initAnimationSet(OFFSET, 1.6f);
		mAnimationSet2 = initAnimationSet(OFFSET, 1.6f);
		mAnimationSet3 = initAnimationSet(OFFSET_HALF, 1.3f);
		// 开启触发水波纹的线程
		Thread mThread = new Thread(this);
		mThread.start();
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_WAVE2_ANIMATION:
				mWave2.startAnimation(mAnimationSet2);
				break;
			case MSG_WAVE3_ANIMATION:
				mWave3.startAnimation(mAnimationSet3);
				break;
			case MSG_WAVE_START:
				showWaveAnimation();
				clearWaveAnimation();
				break;
			}
		}
	};

	@Override
	public void run() {
		while (true) {
			try {
				Message msg = new Message();
				msg.what = MSG_WAVE_START;
				mHandler.sendMessage(msg);
				Thread.sleep(4000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private AnimationSet initAnimationSet(long durationMillis, float toValue) {
		AnimationSet as = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1f, toValue, 1f, toValue, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		sa.setDuration(durationMillis * 3);
		sa.setRepeatCount(Animation.ABSOLUTE);// 设置循环
		AlphaAnimation aa = new AlphaAnimation(1, 0.1f);
		aa.setDuration(durationMillis * 3);
		aa.setRepeatCount(Animation.ABSOLUTE);// 设置循环
		as.addAnimation(sa);
		as.addAnimation(aa);
		return as;
	}

	private void showWaveAnimation() {
		mWave1.startAnimation(mAnimationSet1);
		mHandler.sendEmptyMessageDelayed(MSG_WAVE2_ANIMATION, OFFSET_HALF);
		mHandler.sendEmptyMessageDelayed(MSG_WAVE3_ANIMATION, OFFSET_HALF * 2);
	}

	private void clearWaveAnimation() {
		mWave1.clearAnimation();
		mWave2.clearAnimation();
		mWave3.clearAnimation();
	}
}
