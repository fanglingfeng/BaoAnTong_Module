package com.tjsoft.webhall.ui.bsdt;

import java.text.DateFormat;
import java.util.Date;

import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BSDT extends AutoDialogActivity {

	private ImageButton bsdt_wysb,bsdt_wdbj,bsdt_wyyy,bsdt_wdyy,bsdt_wypy,bsdt_yhxx,bsdt_xgmm;
	private DisplayMetrics dm;
	private Button back, home,logout;
	private Intent intent;
	private TextView name,date;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_bsdt"));
		Constants.getInstance().addActivity(this);
		InitView();
		initSetOnListener();

	}
	private void initSetOnListener() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				BSDT.this.finish();
			}
		});
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Constants.user = null;
				DialogUtil.showUIToast(BSDT.this, "您已退出登陆！");
				FileUtil.Write(BSDT.this, "password", "");
			}
		});
		bsdt_wysb.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				Intent intent = new Intent();
//				intent.setClass(BSDT.this, Main.class);
//				startActivity(intent);
			}
		});
		bsdt_wdbj.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BSDT.this, WDBJ.class);
				startActivity(intent);
			}
		});
		bsdt_wyyy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BSDT.this, ReserveSubmit.class);
				startActivity(intent);
			}
		});
		bsdt_wdyy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BSDT.this, ReserveList.class);
				startActivity(intent);
			}
		});
		bsdt_wypy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(BSDT.this, AdviceList.class);
				startActivity(intent);
			}
		});
		bsdt_yhxx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		bsdt_xgmm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			}
		});
	}
	private void InitView() {
		
		name = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		date = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "date"));
		name.setText(Constants.user.getREALNAME());
		date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		logout = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "logout"));
		bsdt_wysb = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wysb"));
		bsdt_wdbj = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wdbj"));
		bsdt_wyyy = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wyyy"));
		bsdt_wdyy = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wdyy"));
		bsdt_wypy = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wypy"));
		bsdt_yhxx = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_yhxx"));
		bsdt_xgmm = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_xgmm"));
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		LinearLayout.LayoutParams iconSize = new LinearLayout.LayoutParams((dm.widthPixels-DensityUtil.dip2px(this, 20))/3, (dm.widthPixels-DensityUtil.dip2px(this, 20))/3);
		ImageButton[]	btnList = {bsdt_wysb,bsdt_wdbj,bsdt_wyyy,bsdt_wdyy,bsdt_wypy,bsdt_yhxx,bsdt_xgmm};
			for (int i = 0; i < btnList.length; i++) {
				btnList[i].setLayoutParams(iconSize);
//				btnList[i].setOnClickListener(this);
			}
		
	}
		
}
