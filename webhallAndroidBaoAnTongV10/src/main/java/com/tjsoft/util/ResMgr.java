package com.tjsoft.util;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 图片加载类
 * 
 * @author tjsoft
 * 
 */
public final class ResMgr {
	static ResMgr resMgr;
	
	
	

	public static ResMgr getResMgr() {
		if (resMgr == null) {
			resMgr = new ResMgr();
		}
		return resMgr;
	}

	/**
	 * 图片缓存
	 */
	@SuppressLint("UseSparseArrays")
	public static Map<Object, SoftReference<Drawable>> imageCache = Collections
			.synchronizedMap(new HashMap<Object, SoftReference<Drawable>>());
	public static Map<Object, SoftReference<View>> viewCache = Collections
			.synchronizedMap(new HashMap<Object, SoftReference<View>>());
	public static ArrayList<Activity> activities = new ArrayList<Activity>();

	/**
	 * 从缓存加载图片
	 * 
	 * @param resources
	 * @param imageView
	 * @param res
	 * @param flag
	 *            0:res for key 1:imageView for key
	 */
	public static void loadImage(Resources resources, ImageView imageView,
			int res, int flag) {
		SoftReference<Drawable> bmpSr = null;
		if (flag == 0) {
			bmpSr = imageCache.get(res);
		} else {
			bmpSr = imageCache.get(imageView);
		}
		if (bmpSr != null) {
			Drawable drawable = bmpSr.get();
			if (drawable != null) {
				imageView.setImageDrawable(drawable);
				return;
			}
			doLoadImage(resources, imageView, res, flag);
		}
		doLoadImage(resources, imageView, res, flag);
	}

	/**
	 * 从缓存加载图片
	 * 
	 * @param resources
	 * @param imageView
	 * @param res
	 * @param flag
	 *            0:res for key 1:imageView for key
	 */
	public static void loadBackgroundImage(Resources resources, View view,
			int res) {
		SoftReference<Drawable> bmpSr = imageCache.get(res);
		if (bmpSr != null) {
			Drawable drawable = bmpSr.get();
			if (drawable != null) {
				view.setBackgroundDrawable(drawable);
				return;
			}
			doLoadImage(resources, view, res);
		}
		doLoadImage(resources, view, res);
	}

	/**
	 * 直接加载图片放入缓存
	 * 
	 * @param resources
	 * @param imageView
	 * @param res
	 */
	public static void doLoadImage(Resources resources, View view, int res) {
		Bitmap bitmap = readBitMap(resources, res);
		Drawable drawable = new BitmapDrawable(bitmap);
		view.setBackgroundDrawable(drawable);
		imageCache.put(res, new SoftReference<Drawable>(drawable));

	}


	public static Bitmap readBitMap(Resources resources, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = resources.openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/**
	 * 直接加载图片放入缓存
	 * 
	 * @param resources
	 * @param imageView
	 * @param res
	 */
	public static void doLoadImage(Resources resources, ImageView imageView,
			int res, int flag) {
		Bitmap bitmap = readBitMap(resources, res);
		Drawable drawable = new BitmapDrawable(bitmap);
		imageView.setImageDrawable(drawable);
		if (flag == 0) {
			imageCache.put(res, new SoftReference<Drawable>(drawable));
		} else {
			imageCache.put(imageView, new SoftReference<Drawable>(drawable));
		}

	}

	/**
	 * 图片销毁方法
	 */
	public static void destroy() {
		Set<Object> keys = imageCache.keySet();
		for (Object object : keys) {
			if (object instanceof View) {
				View view = (View) object;
				if (view instanceof ImageView) {
					ImageView imageView = (ImageView) view;
					imageView.setImageBitmap(null);
					imageView.setImageDrawable(null);
					imageView = null;
				}
				Drawable drawable = view.getBackground();
				if (drawable != null) {
					drawable = null;
					view.setBackgroundDrawable(null);
				}
			}

			SoftReference<Drawable> sf = imageCache.get(object);
			if (sf != null) {
				Drawable drawable = sf.get();
				if (drawable != null) {
					drawable = null;
					// System.gc();
				}
			}
		}
		imageCache.clear();
	}

	public static void findImageView(View view) {
		if (view == null) {
			return;
		}
		ViewGroup viewGroup = (ViewGroup) view;
		Drawable drawable = viewGroup.getBackground();
		if (drawable != null) {
			drawable = null;
			viewGroup.setBackgroundDrawable(null);
		}
		int count = viewGroup.getChildCount();
		ImageView imageView = null;
		for (int i = 0; i < count; i++) {
			View child = viewGroup.getChildAt(i);
			drawable = child.getBackground();
			if (drawable != null) {
				drawable = null;
				child.setBackgroundDrawable(null);
			}
			if (child instanceof TextView) {
				TextView textView = (TextView) child;
				textView.setText("");
				textView = null;
			}
			if (child instanceof ImageView) {
				imageView = (ImageView) child;
				imageView.setImageDrawable(null);
				imageView.setImageBitmap(null);
				imageView.setBackgroundDrawable(null);
				// System.gc();
			} else if (child instanceof ViewGroup) {
				findImageView((ViewGroup) child);
			}
		}
		// view = null;

	}

	public static void destroyViews() {
		for (Activity activity : activities) {
			if (activity != null) {
				findImageView(activity.getWindow().getDecorView());
				// activity.finish();
			}
		}

	}
}
