package com.tjsoft.webhall.set;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
/**
 * 软件说明页面
 * @author Administrator
 *
 */
public class Introductions extends AutoDialogActivity {
	private WebView webView;
	private Button back, submit;
	private TextView titleTxt;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_survey"));
		titleTxt = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "titleTxt"));
		titleTxt.setText("使用说明");
		webView = (WebView) findViewById(MSFWResource.getResourseIdByName(this, "id", "webView"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		submit.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Introductions.this.finish();
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.loadUrl("javascript:androidSubmit()");				
			}
		});
		WebSettings webSettings = webView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAllowFileAccess(true);
		webSettings.setSupportZoom(false);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webView.loadUrl("file:///android_asset/user_help.html");
	}

}
