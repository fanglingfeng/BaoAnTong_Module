package com.tjsoft.webhall.ui.work;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
/**
 * 申报结果
 * @author Administrator
 *
 */
public class ApplyResult extends AutoDialogActivity {
	private TextView info;
	private String BSNUM;
	private Button back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_apply_result"));
		info = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "info"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ApplyResult.this.finish();
			}
		});
		BSNUM = getIntent().getStringExtra("BSNUM");
		if(null != BSNUM){
			info.setText("您的业务（编号："+BSNUM+"）已办结，请您带齐相关证件到行政服务中心领取办理结果。");
		}else{
			info.setText("暂无信息");
		}
	}

}
