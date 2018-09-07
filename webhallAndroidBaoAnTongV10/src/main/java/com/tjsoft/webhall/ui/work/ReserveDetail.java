package com.tjsoft.webhall.ui.work;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ReserveByBS;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * 预约详情
 * @author Administrator
 *
 */
public class ReserveDetail extends AutoDialogActivity {
	private TextView BSNUM;
	private TextView SXZXNAME;
	private TextView DEPTNAME;
	private TextView APPNAME;
	private TextView RESERVEDATE;
	private TextView RESERVETIME;
	private TextView STATUS;
	private ReserveByBS reserveByBS;
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_reserve_detail"));
		Constants.getInstance().addActivity(this);
		reserveByBS = (ReserveByBS) getIntent().getSerializableExtra("reserveByBS");
		InitView();
	}

	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReserveDetail.this.finish();				
			}
		});
		BSNUM = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "BSNUM"));
		SXZXNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "SXZXNAME"));
		DEPTNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "DEPTNAME"));
		APPNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "APPNAME"));
		RESERVEDATE = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "RESERVEDATE"));
		RESERVETIME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "RESERVETIME"));
		STATUS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "STATUS"));
		
		BSNUM.setText(reserveByBS.getBSNUM());
		SXZXNAME.setText(reserveByBS.getPNAME());
		DEPTNAME.setText(reserveByBS.getDEPTNAME());
		APPNAME.setText(reserveByBS.getAPPLICANT());
		RESERVEDATE.setText(reserveByBS.getRESERVEDATE());
		RESERVETIME.setText(reserveByBS.getRESERVETIME());
		if(null != reserveByBS.getSTATUS()){
			if(reserveByBS.getSTATUS().equals("5")){
				STATUS.setText("尚未确认");
			}
			if(reserveByBS.getSTATUS().equals("6")){
				STATUS.setText("预约成功");
			}
			if(reserveByBS.getSTATUS().equals("7")){
				STATUS.setText("预约撤销");
			}
			
		}
		
		
	}

}
