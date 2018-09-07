package com.tjsoft.webhall.ui.wsbs;

import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
/**
 * 申报步骤一页面
 * @author Administrator
 *
 */
public class ApplyOL_Step1 extends AutoDialogActivity {
	private Button back,home,ok;
	private ImageButton agree;
	private boolean agreeFlag =  false;
	private Intent intent;
	private String PERMID = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_apply_online_step1"));
		Constants.getInstance().addActivity(this);
		PERMID = getIntent().getStringExtra("PERMID");
		InitView();
		initSetOnListener();
	}
	private void initSetOnListener() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ApplyOL_Step1.this.finish();
			}
		});
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(agreeFlag){
					intent = new Intent();
					intent.setClass(ApplyOL_Step1.this, ApplyOL_Step2.class);
					intent.putExtra("PERMID", PERMID);
					startActivity(intent);
					finish();
				}
				else{
					DialogUtil.showUIToast(ApplyOL_Step1.this, "请认真阅读《网上申报注意事项》");
				}
			}
		});
		agree.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(agreeFlag){
					agreeFlag = false;
					agree.setBackgroundResource(MSFWResource.getResourseIdByName(ApplyOL_Step1.this, "drawable", "tj_select_box"));
				}
				else{
					agreeFlag = true;
					agree.setBackgroundResource(MSFWResource.getResourseIdByName(ApplyOL_Step1.this, "drawable", "tj_select_box_on"));
				}
			}
		});
	}
	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		ok = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "ok"));
		
		agree = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "agree"));
		
	}

}
