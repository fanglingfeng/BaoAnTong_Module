package com.tjsoft.webhall.ui.expressage;

import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.MyBrowser;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.PostInfo;

public class ExpressageProgress extends AutoDialogActivity {

	private TextView RECEIVE, PHONE, ADDRESS, RECEIVE2, PHONE2, ADDRESS2;
	private String BSNUM = "";
	private LinearLayout postInfo1, postInfo2;
	private Button back, search1, search2;
	private Intent intent;
	private List<PostInfo> postInfos, postInfos2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "expressage_progress"));
		BSNUM = getIntent().getStringExtra("BSNUM");
		initView();
		dialog = Background.Process(ExpressageProgress.this, GetBusiPostInfo, "正在加载。。。");
		dialog = Background.Process(ExpressageProgress.this, GetBusiPostInfo2, "正在加载。。。");

	}

	/**
	 * 
	 */
	private void initView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		search1 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "search1"));
		search2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "search2"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		search1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();

				intent.putExtra("url", "http://bsdt.baoan.gov.cn/EmsList.html?bsnum=" + postInfos.get(0).getPOSTID());
				System.out.println("fuchl    "+ postInfos.get(0).getPOSTID());
				intent.putExtra("title", "速递详情");
				intent.setClass(ExpressageProgress.this, MyBrowser.class);
				startActivity(intent);

			}
		});
		search2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.putExtra("url", "http://bsdt.baoan.gov.cn/EmsList.html?bsnum=" + postInfos2.get(0).getPOSTID());
				System.out.println("fuchl    "+ postInfos2.get(0).getPOSTID());
				intent.putExtra("title", "速递详情");
				intent.setClass(ExpressageProgress.this, MyBrowser.class);
				startActivity(intent);

			}
		});
		search1.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		search2.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		RECEIVE = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "RECEIVE"));
		PHONE = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "PHONE"));
		ADDRESS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "ADDRESS"));
		RECEIVE2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "RECEIVE2"));
		PHONE2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "PHONE2"));
		ADDRESS2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "ADDRESS2"));
		postInfo1 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "postInfo1"));
		postInfo2 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "postInfo2"));

	}

	/**
	 * 获取递交速递
	 */
	final Runnable GetBusiPostInfo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("TYPE", "1");// 速递类型（1递交纸质材料，2领取结果）
				param.put("BSNUM", BSNUM);
				String response = HTTP.excute("getBusiPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					postInfos = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<PostInfo>>() {
					}.getType());
					if (null != postInfos && postInfos.size() > 0) {
						runOnUiThread(new Runnable() {
							public void run() {
								RECEIVE.setText(postInfos.get(0).getRECEIVE());
								PHONE.setText(postInfos.get(0).getPHONE());
								ADDRESS.setText(postInfos.get(0).getADDRESS());
							}
						});
					} else {
						postInfo1.setVisibility(View.GONE);
					}

				}
			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageProgress.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};

	/**
	 * 获取结果材料速递
	 */
	final Runnable GetBusiPostInfo2 = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("TYPE", "2");// 速递类型（1递交纸质材料，2领取结果）
				param.put("BSNUM", BSNUM);
				String response = HTTP.excute("getBusiPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					postInfos2 = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<PostInfo>>() {
					}.getType());
					if (null != postInfos2 && postInfos2.size() > 0) {
						runOnUiThread(new Runnable() {
							public void run() {
								RECEIVE2.setText(postInfos2.get(0).getRECEIVE());
								PHONE2.setText(postInfos2.get(0).getPHONE());
								ADDRESS2.setText(postInfos2.get(0).getADDRESS());
							}
						});
					} else {
						postInfo2.setVisibility(View.GONE);
					}

				}
			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageProgress.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null == data) {
			return;
		}
		switch (requestCode) {
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
