package com.tjsoft.webhall.lib;


import org.json.JSONObject;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
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

@SuppressLint("SetJavaScriptEnabled")
public class MyText extends AutoDialogActivity {
	private WebView webView;
	private Button back;
	private String CHANNEL_ID = "";
	private String title = "";
	private TextView titleTxt;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_my_browser"));
		CHANNEL_ID = getIntent().getStringExtra("CHANNEL_ID");
		title = getIntent().getStringExtra("title");
		titleTxt = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "titleTxt"));
		webView = (WebView) findViewById(MSFWResource.getResourseIdByName(this, "id", "webView"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyText.this.finish();
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
		dialog = Background.Process(this, GetText, getString(MSFWResource.getResourseIdByName(this, "string", "tj_loding")));
	}

	final Runnable GetText = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("CHANNEL_ID", CHANNEL_ID);
				String response = HTTP.excute("getchanneltxt", "RestNewsService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					content = json.getString("ReturnValue");
					runOnUiThread(new Runnable() {
						public void run() {
							webView.loadData(content, "text/html; charset=UTF-8", null);
						}
					});
				} else {
					DialogUtil.showUIToast(MyText.this, getString(MSFWResource.getResourseIdByName(MyText.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(MyText.this, getString(MSFWResource.getResourseIdByName(MyText.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

}
