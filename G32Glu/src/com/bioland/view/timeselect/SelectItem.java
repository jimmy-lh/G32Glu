/*
 * SelectItem.java
 * classes : com.zenghui.view.SelectItem
 * @author zenghui
 * V 1.0.0
 * Create at 2015-8-26 上午10:10:46
 */
package com.bioland.view.timeselect;

import android.graphics.RectF;
import android.util.Log;

/**
 * com.zenghui.view.SelectItem
 * 
 * @author zenghui <br/>
 *         create at 2015-8-26 上午10:10:46
 */
public class SelectItem {
	public RectF rect;
	public String text;

	public boolean isVisible;

	public SelectItem(RectF rect, String text) {
		this.rect = rect;
		this.text = text;
	}

	public RectF getRect() {
		return this.rect;
	}

	public void setRect(RectF rect) {
		this.rect = rect;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public boolean isVisible(float height) {
		return (rect.bottom <= 0 || rect.top >= height) ? false : true;
	}

	public boolean isInRect(float x, float y) {
		return (rect.bottom >= y && rect.top <= y && x <= rect.right && x >= rect.left) ? true : false;
	}
}
