package com.tjsoft.webhall.ui.wsbs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.constants.Constants;

/**
 * 操作指南
 * 
 * @author yifan
 * @version 1.0
 * @date 2014-08-13
 * 
 */
public class OperateGuide extends Activity {

	private Button back;		// 返回按钮
	private WebView operate;	// webview
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_operation_guide"));
		Constants.getInstance().addActivity(this);

		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		operate = (WebView)	findViewById(MSFWResource.getResourseIdByName(this, "id", "operate"));
		operate.loadUrl("file:///android_asset/operate.html");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
