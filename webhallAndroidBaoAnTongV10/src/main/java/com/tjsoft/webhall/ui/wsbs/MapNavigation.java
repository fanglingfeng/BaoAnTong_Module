package com.tjsoft.webhall.ui.wsbs;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

/**
 * 地图导航
 * @author fuchl
 *
 */
public class MapNavigation extends AutoDialogActivity {
	private WebView mWebView;
	private Button mBack;
	private ProgressDialog mProgressDialog;
//	private MapView mMapView; 
//	private BaiduMap mBaiduMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_map_navigation"));
		Constants.getInstance().addActivity(this);
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getResources().getString(MSFWResource.getResourseIdByName(MapNavigation.this, "string", "tj_loding")));
		mProgressDialog.show();
		mWebView = (WebView) findViewById(MSFWResource.getResourseIdByName(this, "id", "webview"));
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSettings.setSavePassword(false);
		webSettings.setSaveFormData(false);
		webSettings.setJavaScriptEnabled(false);
		webSettings.setAllowFileAccess(true);
		webSettings.setSupportZoom(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setLoadsImagesAutomatically(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		String longitudeString = getIntent().getStringExtra("LONGITUDE");
		String latitudestring = getIntent().getStringExtra("LATITUDE");
		StringBuilder sb = new StringBuilder();
		sb.append("http://api.map.baidu.com/staticimage?center=")
		.append(longitudeString)
		.append(",")
		.append(latitudestring)
		.append("&width=500&height=500&zoom=14")
		.append("&markers=")
		.append(longitudeString)
		.append(",")
		.append(latitudestring);
		System.out.println("########url   "+sb.toString());
		mWebView.loadUrl(sb.toString());
		mWebView.setWebViewClient(new WebViewClient(){
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mProgressDialog.dismiss();
			}
		});
		mBack = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		mBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MapNavigation.this.finish();				
			}
		});
		

//		mMapView = (MapView) findViewById(R.id.bmapView);
//		mBaiduMap  = mMapView.getMap();
//		mMapView.setVisibility(View.GONE);


	}
	
	  @Override  
	    protected void onDestroy() {  
	        super.onDestroy();  
	        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
//	        mMapView.onDestroy();  
	    }  
	    @Override  
	    protected void onResume() {  
	        super.onResume();  
	        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理  
//	        mMapView.onResume();  
	        }  
	    @Override  
	    protected void onPause() {  
	        super.onPause();  
	        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理  
//	        mMapView.onPause();  
	        }  
	    
}
