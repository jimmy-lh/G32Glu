package com.bioland.adapter;

public class BaseAdapterItem {
	private int imageView1;
	private int imageView2;

	private String textView1;
	private String textView2;
	private String textView3;
	private String textView4;
	private String textView5;
	private String textView6;

	// 三个txt
	public BaseAdapterItem(String textView1, String textView2, String textView3) {
		super();
		this.textView1 = textView1;
		this.textView2 = textView2;
		this.textView3 = textView3;
	}

	// 四个txt
	public BaseAdapterItem(String textView1, String textView2, String textView3, String textView4) {
		super();
		this.textView1 = textView1;
		this.textView2 = textView2;
		this.textView3 = textView3;
		this.textView4 = textView4;
	}

	// 六个txt
	public BaseAdapterItem(String textView1, String textView2, String textView3, String textView4, String textView5,
			String textView6) {
		super();
		this.textView1 = textView1;
		this.textView2 = textView2;
		this.textView3 = textView3;
		this.textView4 = textView4;
		this.textView5 = textView5;
		this.textView6 = textView6;
	}

	// 一个img 一个txt
	public BaseAdapterItem(int imageView1, String textView1) {
		super();
		this.imageView1 = imageView1;
		this.textView1 = textView1;
	}

	// 一个img 两个txt
	public BaseAdapterItem(int imageView1, String textView1, String textView2) {
		super();
		this.imageView1 = imageView1;
		this.textView1 = textView1;
		this.textView2 = textView2;
	}

	// 一个img 三个txt
	public BaseAdapterItem(int imageView1, String textView1, String textView2, String textView3) {
		super();
		this.imageView1 = imageView1;
		this.textView1 = textView1;
		this.textView2 = textView2;
		this.textView3 = textView3;
	}

	// 一个img 四个txt
	public BaseAdapterItem(int imageView1, String textView1, String textView2, String textView3, String textView4) {
		super();
		this.imageView1 = imageView1;
		this.textView1 = textView1;
		this.textView2 = textView2;
		this.textView3 = textView3;
		this.textView4 = textView4;
	}

	// 两个img 一个txt
	public BaseAdapterItem(int imageView1, int imageView2, String textView1) {
		super();
		this.imageView1 = imageView1;
		this.imageView2 = imageView2;
		this.textView1 = textView1;
	}

	public int getImageView1() {
		return imageView1;
	}

	public void setImageView1(int imageView1) {
		this.imageView1 = imageView1;
	}

	public int getImageView2() {
		return imageView2;
	}

	public void setImageView2(int imageView2) {
		this.imageView2 = imageView2;
	}

	public String getTextView1() {
		return textView1;
	}

	public void setTextView1(String textView1) {
		this.textView1 = textView1;
	}

	public String getTextView2() {
		return textView2;
	}

	public void setTextView2(String textView2) {
		this.textView2 = textView2;
	}

	public String getTextView3() {
		return textView3;
	}

	public void setTextView3(String textView3) {
		this.textView3 = textView3;
	}

	public String getTextView4() {
		return textView4;
	}

	public void setTextView4(String textView4) {
		this.textView4 = textView4;
	}

	public String getTextView5() {
		return textView5;
	}

	public void setTextView5(String textView5) {
		this.textView5 = textView5;
	}

	public String getTextView6() {
		return textView6;
	}

	public void setTextView6(String textView6) {
		this.textView6 = textView6;
	}

}
