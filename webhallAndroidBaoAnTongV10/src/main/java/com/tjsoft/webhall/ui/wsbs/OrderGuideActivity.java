package com.tjsoft.webhall.ui.wsbs;

import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.MSFWResource;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * 预约指南
 * 
 * @author long
 * 
 */
public class OrderGuideActivity extends Activity {
	private RelativeLayout back;
	private ImageView btn1;
	private Button next;
	private Intent intent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_order_guide"));
		initView();
	}

	private void initView() {
		btn1 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "btn1"));
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		int scrrenH = dm.heightPixels;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenW/2+20, scrrenH/7);
		params.setMargins(screenW/2-20, scrrenH/6, 0, 0);
		btn1.setLayoutParams(params);	
		btn1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(OrderGuideActivity.this, WSBS.class);
				startActivity(intent);
				finish();

			}
		});
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		next = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "next"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(OrderGuideActivity.this, WSBS.class);
				startActivity(intent);
				finish();

			}
		});
	}

}
