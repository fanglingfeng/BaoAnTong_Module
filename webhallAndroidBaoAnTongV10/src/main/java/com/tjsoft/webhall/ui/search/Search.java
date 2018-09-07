package com.tjsoft.webhall.ui.search;

import com.google.zxing.CaptureActivity;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
/**
 *进度查询页面
 * @author Administrator
 *
 */
public class Search extends AutoDialogActivity {
	private Button findByName,findProgress,more;
	private RelativeLayout back;
	private EditText permName,bsNumm,appName;
	private Intent intent;
	private ImageButton erweima;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_search"));
		Constants.getInstance().addActivity(this);
		InitView();
		initSetOnListener();

	}
	@Override
	protected void onDestroy() {
		StatisticsTools.end("进度查询", null, null);
		super.onDestroy();
	}
	private void initSetOnListener() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Search.this.finish();
			}
		});
		findByName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == permName.getText().toString().trim() || permName.getText().toString().trim().equals("")){
					DialogUtil.showUIToast(Search.this, "事项名称不能为空！");
				}
				else {
					intent = new Intent();
					intent.setClass(Search.this, PermListByName.class);
					intent.putExtra("name", permName.getText().toString().trim());
					startActivity(intent);
				}
			}
		});
		findProgress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(null == bsNumm.getText().toString().trim() || bsNumm.getText().toString().trim().equals("") ){
					DialogUtil.showUIToast(Search.this, "业务流水号不能为空！");
					return;
				}
//				if(null == appName.getText().toString().trim() || appName.getText().toString().trim().equals("") ){
//					DialogUtil.showUIToast(Search.this, "申请人姓名不能为空！");
//					return;
//				}
				else{
				intent = new Intent();
				intent.setClass(Search.this, SearchSchedule.class);
				intent.putExtra("BSNUM", bsNumm.getText().toString().trim());
				intent.putExtra("APPNAME", "erweima");
				startActivity(intent);
				}
			}
		});
		erweima.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(Search.this, CaptureActivity.class);
				startActivityForResult(intent, 100);// 二维码扫描;
			}
		});
	}
	private void InitView() {
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		findByName = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "findByName"));
		findProgress = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "findProgress"));
		permName = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "permName"));
		bsNumm = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsNum"));
		appName = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "appName"));
		erweima = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "erweima"));
		more = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "more"));
		appName.setVisibility(View.GONE);
	}
}
