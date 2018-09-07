package com.tjsoft.webhall.ui.wsbs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.ui.search.Search;
import com.tjsoft.webhall.ui.search.SearchSchedule;

/**
 * 申报指南
 * 
 * @author long
 * 
 */
public class ApplyGuideActivity extends Activity {
	private RelativeLayout back;
	private ImageView btn1;
	private ImageView btn2;
	private Button next;
	private Intent intent;
	private int flag;//1个人办事   2 企业办事
	private RelativeLayout parent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_apply_guide"));
		flag=getIntent().getIntExtra("flag", 1);
		initView();
	}

	private void initView() {
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		next = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "next"));
		btn1 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "btn1"));
		btn2 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "btn2"));
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		int scrrenH = dm.heightPixels;
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(screenW/2+20, scrrenH/7);
		params.setMargins(screenW/2-20, scrrenH/7, 0, 0);
		btn1.setLayoutParams(params);	
		RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams(screenW/2+20, scrrenH/7);
		params2.setMargins(0, scrrenH*3/5, screenW/2-20, 0);
		btn2.setLayoutParams(params2);
		btn1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				nextActivity();
			}
		});
		btn2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(ApplyGuideActivity.this, Search.class);
				startActivity(intent);
				finish();
				
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextActivity();
			}
		});
	}
	private void nextActivity(){
		switch(flag){
		case 1:
			intent = new Intent();
			intent.setClass(ApplyGuideActivity.this, WSBS1.class);
			startActivity(intent);
			finish();
			break;
		case 2:
			intent = new Intent();
			intent.setClass(ApplyGuideActivity.this, WSBS2.class);
			startActivity(intent);
			finish();
			break;
		}
	}
}
