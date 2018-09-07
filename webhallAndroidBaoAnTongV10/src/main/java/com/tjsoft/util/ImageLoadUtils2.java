package com.tjsoft.util;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import net.liang.appbaselibrary.R;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 图片加载工具类
 * 这里用的glid
 * Created by Dino on 11/17 0017.
 */

public class ImageLoadUtils2 {
    private static int defultImageID = R.mipmap.default_back_picture;

    /**
     * 加载原图片
     *
     * @param image
     * @param imageView
     */
    public static void loadImage(Object image, ImageView imageView) {
        loadImage(image,imageView,defultImageID);
    }

    public static void loadImage(Object image, ImageView imageView, int defultImage) {

        RequestOptions options = new RequestOptions();
        options.centerCrop()
                .placeholder(defultImage);

        Glide.with(imageView.getContext())
                .load(image)
                .apply(options)
                .into(imageView);


  /*      Glide.with(imageView.getContext())
                .load(image)
                .placeholder(defultImage)
                .crossFade()
                .into(imageView);*/
    }



}
