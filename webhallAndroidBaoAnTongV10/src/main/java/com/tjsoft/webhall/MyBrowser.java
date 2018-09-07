package com.tjsoft.webhall;

import com.tjsoft.util.MSFWResource;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义浏览器
 * @author Administrator
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class MyBrowser extends AutoDialogActivity {
	private WebView webView;
	private RelativeLayout back;
	private String url = "";
	private String title = "";
	private TextView titleTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_my_browser"));
		url = getIntent().getStringExtra("url");
		title = getIntent().getStringExtra("title");
		titleTxt = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "titleTxt"));
		webView = (WebView) findViewById(MSFWResource.getResourseIdByName(this, "id", "webView"));
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyBrowser.this.finish();				
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
		titleTxt.setText(title);
		webView.loadUrl(url);
	}
}
