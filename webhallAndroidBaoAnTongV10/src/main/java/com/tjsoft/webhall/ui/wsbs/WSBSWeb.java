package com.tjsoft.webhall.ui.wsbs;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import android.annotation.SuppressLint;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("JavascriptInterface")
public class WSBSWeb extends AutoDialogActivity {
	private RelativeLayout back;
	private WebView mWebView;
	private String url;
	private ProgressBar web_progress;
	private TextView titleName;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout",
				"tj_activity_wsbs_web"));
		titleName = (TextView) findViewById(MSFWResource.getResourseIdByName(
				this, "id", "titleName"));
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(
				this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		web_progress = (ProgressBar) findViewById(MSFWResource
				.getResourseIdByName(this, "id", "web_progress"));
		url = getIntent().getStringExtra("url");
		mWebView = (WebView) findViewById(MSFWResource.getResourseIdByName(
				this, "id", "webwiew"));
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true); // 支持js
		webSettings.setUseWideViewPort(true);// 设置此属性，可任意比例缩放
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setSupportZoom(true);// 支持缩放
		mWebView.requestFocusFromTouch();
		mWebView.setWebChromeClient(new MyWebChromeClient());
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {// 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
				return false;
			}
			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				handler.proceed(); // 接受所有网站的证书
			}
		});
		mWebView.loadUrl(url);
	}

	public class MyWebChromeClient extends android.webkit.WebChromeClient {
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			onReceivedTitle(view, view.getTitle());
			if (newProgress == 100) {
				web_progress.setVisibility(View.GONE);
			} else {
				if (web_progress.getVisibility() == View.GONE)
					web_progress.setVisibility(View.VISIBLE);
				Log.e("newProgress", "newProgress:" + newProgress);
				web_progress.setProgress(newProgress);
			}
			super.onProgressChanged(view, newProgress);
		}

		@Override
		public void onReceivedTitle(WebView view, String title) {
			super.onReceivedTitle(view, title);
			// titleName.setText(title);
			Log.d("WSBSWEB", title);
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	class JavaScriptInterface {
		JavaScriptInterface() {
		}
	}

}
