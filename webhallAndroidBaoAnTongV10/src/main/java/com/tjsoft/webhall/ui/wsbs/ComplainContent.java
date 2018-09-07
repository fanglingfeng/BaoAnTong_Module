package com.tjsoft.webhall.ui.wsbs;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.entity.Complaint;

/**
 * 投诉详情页面
 * 
 * @author Administrator
 * 
 */
public class ComplainContent extends AutoDialogActivity {
	private TextView CREATETIME, CONTENT, HFNR, MODIFYTIME, DEPARTMENTNAME, MAINTITLE;
	private Button back;
	private TextView title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "consult_content"));
		InitView();
	}

	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		MAINTITLE = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "MAINTITLE"));
		CREATETIME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "CREATETIME"));
		CONTENT = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "CONTENT"));
		HFNR = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "HFNR"));
		MODIFYTIME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "MODIFYTIME"));
		DEPARTMENTNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "DEPARTMENTNAME"));
		title = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
		title.setText("我的投诉");

		Complaint complaint = (Complaint) getIntent().getSerializableExtra("complaint");

		if (null != complaint) {
			CREATETIME.setText(complaint.getCREATETIME());
			CONTENT.setText(complaint.getCONTENT());
			HFNR.setText(complaint.getHFNR());
			MODIFYTIME.setText(complaint.getMODIFYTIME());
			DEPARTMENTNAME.setText(complaint.getDEPARTMENTNAME());
			MAINTITLE.setText(complaint.getMAINTITLE());
			if (null == complaint.getHFNR() || complaint.getHFNR().equals("")) {
				HFNR.setVisibility(View.INVISIBLE);
				DEPARTMENTNAME.setVisibility(View.INVISIBLE);
				MODIFYTIME.setVisibility(View.INVISIBLE);
			}
		}

	}

}
