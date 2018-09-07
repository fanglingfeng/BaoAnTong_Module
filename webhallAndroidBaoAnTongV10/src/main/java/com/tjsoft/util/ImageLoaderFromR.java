package com.tjsoft.util;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
/**
 * 图片加载类
 * @author tjsoft
 *
 */
public final class ImageLoaderFromR {
	/**
	 * 图片缓存
	 */
	@SuppressLint("UseSparseArrays")
	public static Map<Integer, SoftReference<Bitmap>> imageCache=Collections.synchronizedMap(new HashMap<Integer, SoftReference<Bitmap>>());
    
	/**
	 * 从缓存加载图片
	 * @param resources
	 * @param imageView
	 * @param res
	 */
	public static void loadImage(Resources resources, ImageView imageView,int res){
		SoftReference<Bitmap> bmpSr = imageCache.get(res);
		if (bmpSr != null) {
			Bitmap bitmap = bmpSr.get();
			if (bitmap != null) {
				imageView.setImageBitmap(bitmap);
				return;
			}
			doLoadImage(resources,imageView, res);
		}
		doLoadImage(resources,imageView, res);
	}
	/**
	 * 直接加载图片放入缓存
	 * @param resources
	 * @param imageView
	 * @param res
	 */
	public static void doLoadImage(Resources resources, ImageView imageView,int res) {
		Bitmap bitmap = BitmapFactory.decodeResource(resources, res);
		imageView.setImageBitmap(bitmap);
		imageCache.put(res, new SoftReference<Bitmap>(bitmap));
	}
	/**
	 * 图片销毁方法
	 */
	public static void  destroy() {
		Set<Integer> keys =  imageCache.keySet();
		for (Integer integer : keys) {
			SoftReference<Bitmap> bmpSr = imageCache.get(integer);
			if (bmpSr != null) {
				Bitmap bitmap = bmpSr.get();
				if (bitmap != null) {
					bitmap.recycle();
					bitmap = null;
					System.gc();
				}
			}
		}
		imageCache.clear();
	}
}
