package com.tjsoft.webhall.ui.wsbs;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ATTBean;
import com.tjsoft.webhall.lib.PhotoView;
import com.tjsoft.webhall.lib.PhotoViewPager;
import com.tjsoft.webhall.ui.materialmanage.MaterialManageActivity;

import java.util.ArrayList;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

public class ImagePreActivity extends Activity implements OnPageChangeListener {
	/**
	 * ViewPager
	 */
	private PhotoViewPager viewPager;
	
	/**
	 * 装点点的ImageView数组
	 */
	private ImageView[] tips;

	/**
	 * 装ImageView数组
	 */
	private PhotoView[] mImageViews;

	/**
	 * 图片资源id ImageScrollActivity
	 */
	// private int[] imgIdArray ;

	private ArrayList<ATTBean> imgs;

	private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).build();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_image_preview"));
		ViewGroup group = (ViewGroup) findViewById(MSFWResource.getResourseIdByName(this, "id", "viewGroup"));
		viewPager = (PhotoViewPager) findViewById(MSFWResource.getResourseIdByName(this, "id", "viewPager"));
		imgs = (ArrayList<ATTBean>) MaterialManageActivity.listData;
		// 将点点加入到ViewGroup中
		tips = new ImageView[imgs.size()];
		for (int i = 0; i < tips.length; i++) {
			PhotoView imageView = new PhotoView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(15, 15);
			params.setMargins(10,10,10,10);
			imageView.setLayoutParams(params);
			
			tips[i] = imageView;
			if (i == 0) {
				tips[i].setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "page_indicator_focused"));
			} else {
				tips[i].setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "page_indicator_unfocused"));
			}
			group.addView(imageView);
		}
		// 将图片装载到数组中
		mImageViews = new PhotoView[imgs.size()];
		for (int i = 0; i < mImageViews.length; i++) {
			PhotoView imageView = new PhotoView(this);
			mImageViews[i] = imageView;
			
			imageView.setOnPhotoTapListener(new OnPhotoTapListener() {
				
				@Override
				public void onPhotoTap(View arg0, float arg1, float arg2) {
					// TODO Auto-generated method stub
					finish();
				}
			});
			// imageView.setBackgroundResource(imgIdArray[i]);
		}

		// 设置Adapter
		viewPager.setAdapter(new MyAdapter());
		// 设置监听，主要是设置点点的背景
		viewPager.setOnPageChangeListener(this);
		// 设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
		viewPager.setCurrentItem(getIntent().getIntExtra("index", 0));

	}
	/**
	 * 
	 * @author xiaanming
	 * 
	 */
	public class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgs.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(mImageViews[position]);

		}

		/**
		 * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
		 */
		@Override
		public Object instantiateItem(View container, int position) {
			((ViewPager) container).addView(mImageViews[position]);
			ATTBean att = imgs.get(position);
			String end = att.getATTACHNAME().substring(att.getATTACHNAME().lastIndexOf(".") + 1, att.getATTACHNAME().length()).toLowerCase(Locale.US);

			if ("jpg".equals(end) || "png".equals(end) || "gif".equals(end) || "jpeg".equals(end) || "bmp".equals(end)) {
				ImageLoader.getInstance().displayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + att.getID(), mImageViews[position], options);

			} else if ("doc".equals(end) || "docx".equals(end) || "docm".equals(end) || "dotx".equals(end) || "dotx".equals(end) || "dotm".equals(end)) {

				mImageViews[position].setImageResource(MSFWResource.getResourseIdByName(ImagePreActivity.this, "drawable", "tj_ic_word")); // word文档文件
			} else if ("pdf".equals(end)) {
				mImageViews[position].setImageResource(MSFWResource.getResourseIdByName(ImagePreActivity.this, "drawable", "tj_ic_pdf")); // pdf文件
			} else if ("xls".equals(end) || "xlsx".equals(end) || "xlsm".equals(end) || "xltx".equals(end) || "xltm".equals(end) || "xlsb".equals(end) || "xlam".equals(end)) {
				mImageViews[position].setImageResource(MSFWResource.getResourseIdByName(ImagePreActivity.this, "drawable", "tj_ic_excel")); // excel文件
			} else {
				mImageViews[position].setImageResource(MSFWResource.getResourseIdByName(ImagePreActivity.this, "drawable", "tj_ic_file_unknown"));
			}

			return mImageViews[position];
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {


		setImageBackground(position % mImageViews.length);
	}

	/**
	 * 设置选中的tip的背景
	 * 
	 * @param selectItems
	 */
	private void setImageBackground(int selectItems) {
		for (int i = 0; i < tips.length; i++) {
			if (i == selectItems) {
				tips[i].setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "page_indicator_focused"));
			} else {
				tips[i].setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "page_indicator_unfocused"));
			}
		}
	}
}
