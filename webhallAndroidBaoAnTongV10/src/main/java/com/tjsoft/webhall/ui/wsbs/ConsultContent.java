package com.tjsoft.webhall.ui.wsbs;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.entity.Consult;
/**
 * 咨询详情页面
 * @author Administrator
 *
 */
public class ConsultContent extends AutoDialogActivity {
	private TextView CREATETIME, CONTENT, HFNR, MODIFYTIME, DEPARTMENTNAME,MAINTITLE;
	private RelativeLayout back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "consult_content"));
		InitView();
	}

	private void InitView() {
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		MAINTITLE =  (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "MAINTITLE"));
		CREATETIME =  (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "CREATETIME"));
		CONTENT =  (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "CONTENT"));
		HFNR =  (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "HFNR"));
		MODIFYTIME =  (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "MODIFYTIME"));
		DEPARTMENTNAME =  (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "DEPARTMENTNAME"));
		
		
		Consult conversation = (Consult) getIntent().getSerializableExtra("conversation");

		if(null!= conversation){
			CREATETIME.setText(conversation.getCREATETIME());
			CONTENT.setText(conversation.getCONTENT());
			HFNR.setText(conversation.getHFNR());
			MODIFYTIME.setText(conversation.getMODIFYTIME());
			DEPARTMENTNAME.setText(conversation.getDEPARTMENTNAME());
			MAINTITLE.setText(conversation.getMAINTITLE());
			
		 	if(null==conversation.getHFNR()||conversation.getHFNR().equals("")){
				HFNR.setVisibility(View.INVISIBLE);
				DEPARTMENTNAME.setVisibility(View.INVISIBLE);
				MODIFYTIME.setVisibility(View.INVISIBLE);
			}
		}

		
	}


}
