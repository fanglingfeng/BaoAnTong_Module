package com.tjsoft.webhall.ui.work;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Business;
import com.tjsoft.webhall.entity.ScheduleBean;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 进度查询
 * @author Administrator
 *
 */
public class Find extends AutoDialogActivity {
	private ScheduleBean schedule;
	private TextView BSNUM, SXZXNAME, DEPTNAME, APPNAME, APPCOMPANY, APPLYTIME, CSTATUS;
	private TextView noData;
	private LinearLayout info;
	private Button back;
	private Business business;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_schedule"));
		Constants.getInstance().addActivity(this);
		business = (Business) getIntent().getSerializableExtra("business");
		InitView();

	}
	/**
	 * 初始化页面
	 */
	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Find.this.finish();				
			}
		});
		BSNUM = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "BSNUM"));
		SXZXNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "SXZXNAME"));
		DEPTNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "DEPTNAME"));
		APPNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "APPNAME"));
		APPCOMPANY = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "APPCOMPANY"));
		APPLYTIME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "APPLYTIME"));
		CSTATUS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "CSTATUS"));
		noData = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "noData"));
		info = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "info"));

		BSNUM.setText(business.getBUSINESSID());
		SXZXNAME.setText(business.getSMALLITEMNAME());
		DEPTNAME.setText(business.getDEPTNAME());
		APPNAME.setText(business.getAPPNAME());
		APPCOMPANY.setText(business.getAPPCOMPANY());
		APPLYTIME.setText(business.getAPPLYTIME());
		CSTATUS.setText(business.getCSTATUS());

	}

}
