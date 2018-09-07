package com.tjsoft.webhall.ui.expressage;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.MyBrowser;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.PostInfo;

public class ExpressageChoose extends AutoDialogActivity {
	private Button window_btn, expressage_btn, add_btn, window_btn2, expressage_btn2, add_btn2;
	private Button ok,  cancel, explain;
	private RelativeLayout back;
	private ImageView line1, line2, line3, line4;
	private LinearLayout info_ll, add_ll, info_ll2, add_ll2;
	private TextView notice, notice2;
	private TextView RECEIVE, PHONE, ADDRESS, RECEIVE2, PHONE2, ADDRESS2;
	public static final int GET_EXPRESSAGE = 1;// 领取
	public static final int SEND_EXPRESSAGE = 2;// 递交
	private Intent intent;

	// private PostInfo getPostInfo, sendPostInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "expressage_choose"));
		initView1();
		initView2();

	}

	@Override
	protected void onResume() {

		System.out.println("fuchl   sendPostInfo   " + Constants.sendPostInfo);
		System.out.println("fuchl   getPostInfo   " + Constants.getPostInfo);

		if (null != Constants.sendPostInfo) {
			RECEIVE.setText(Constants.sendPostInfo.getRECEIVE());
			PHONE.setText(Constants.sendPostInfo.getPHONE());
			ADDRESS.setText(Constants.sendPostInfo.getADDRESS());
			info_ll.setVisibility(View.VISIBLE);
			add_ll.setVisibility(View.GONE);
		}

		if (null != Constants.getPostInfo) {
			RECEIVE2.setText(Constants.getPostInfo.getRECEIVE());
			PHONE2.setText(Constants.getPostInfo.getPHONE());
			ADDRESS2.setText(Constants.getPostInfo.getADDRESS());
			info_ll2.setVisibility(View.VISIBLE);
			add_ll2.setVisibility(View.GONE);
		}

		super.onResume();
	}

	/**
	 * 初始化资质材料递交
	 */
	private void initView1() {

		ok = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "ok"));
		cancel = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "cancel"));
		explain = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "explain"));
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		ok.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Constants.sendPostInfo = null;
				Constants.getPostInfo = null;
				finish();

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Constants.sendPostInfo = null;
				Constants.getPostInfo = null;
				finish();

			}
		});
		explain.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(ExpressageChoose.this, MyBrowser.class);
				intent.putExtra("title", "速递服务说明");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn/PostInfo.html");
				startActivity(intent);

			}
		});
		explain.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线

		RECEIVE = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "RECEIVE"));
		PHONE = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "PHONE"));
		ADDRESS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "ADDRESS"));

		add_btn = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "add_btn"));

		add_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(ExpressageChoose.this, ExpressageList.class);
				startActivityForResult(intent, SEND_EXPRESSAGE);

			}
		});

		window_btn = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "window_btn"));
		expressage_btn = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "expressage_btn"));

		info_ll = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "info_ll"));
		add_ll = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "add_ll"));

		notice = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "notice"));

		window_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				expressage_btn.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_normal")));
				window_btn.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_select")));
				line1.setVisibility(View.VISIBLE);
				line2.setVisibility(View.INVISIBLE);

				notice.setVisibility(View.VISIBLE);
				info_ll.setVisibility(View.GONE);
				add_ll.setVisibility(View.GONE);

				Constants.sendPostInfo = null;

			}
		});
		expressage_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				expressage_btn.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_select")));
				window_btn.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_normal")));
				line1.setVisibility(View.INVISIBLE);
				line2.setVisibility(View.VISIBLE);

				notice.setVisibility(View.GONE);
				add_ll.setVisibility(View.VISIBLE);

			}
		});
		info_ll.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(ExpressageChoose.this, ExpressageList.class);
				startActivityForResult(intent, SEND_EXPRESSAGE);

			}
		});

		line1 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "line1"));
		line2 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "line2"));

	}

	/**
	 * 初始化结果材料领取
	 */
	private void initView2() {

		RECEIVE2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "RECEIVE2"));
		PHONE2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "PHONE2"));
		ADDRESS2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "ADDRESS2"));

		add_btn2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "add_btn2"));
		add_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(ExpressageChoose.this, ExpressageList.class);
				startActivityForResult(intent, GET_EXPRESSAGE);

			}
		});

		window_btn2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "window_btn2"));
		expressage_btn2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "expressage_btn2"));

		info_ll2 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "info_ll2"));
		add_ll2 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "add_ll2"));

		notice2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "notice2"));

		window_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				expressage_btn2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_normal")));
				window_btn2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_select")));
				line3.setVisibility(View.VISIBLE);
				line4.setVisibility(View.INVISIBLE);

				notice2.setVisibility(View.VISIBLE);
				info_ll2.setVisibility(View.GONE);
				add_ll2.setVisibility(View.GONE);

				Constants.getPostInfo = null;

			}
		});
		expressage_btn2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				expressage_btn2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_select")));
				window_btn2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ExpressageChoose.this, "color", "expressage_normal")));
				line3.setVisibility(View.INVISIBLE);
				line4.setVisibility(View.VISIBLE);

				notice2.setVisibility(View.GONE);
				add_ll2.setVisibility(View.VISIBLE);

			}
		});
		info_ll2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(ExpressageChoose.this, ExpressageList.class);
				startActivityForResult(intent, GET_EXPRESSAGE);

			}
		});

		line3 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "line3"));
		line4 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "line4"));

	}

	@Override
	public void onBackPressed() {
		Constants.sendPostInfo = null;
		Constants.getPostInfo = null;
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (null == data) {
			return;
		}
		switch (requestCode) {
		case SEND_EXPRESSAGE:

			PostInfo postInfo = (PostInfo) data.getSerializableExtra("postInfo");
			if (null != postInfo) {
				RECEIVE.setText(postInfo.getRECEIVE());
				PHONE.setText(postInfo.getPHONE());
				ADDRESS.setText(postInfo.getADDRESS());
				Constants.sendPostInfo = postInfo;
				info_ll.setVisibility(View.VISIBLE);
				add_ll.setVisibility(View.GONE);
			}

			break;
		case GET_EXPRESSAGE:
			PostInfo postInfo2 = (PostInfo) data.getSerializableExtra("postInfo");
			if (null != postInfo2) {
				RECEIVE2.setText(postInfo2.getRECEIVE());
				PHONE2.setText(postInfo2.getPHONE());
				ADDRESS2.setText(postInfo2.getADDRESS());
				Constants.getPostInfo = postInfo2;
				info_ll2.setVisibility(View.VISIBLE);
				add_ll2.setVisibility(View.GONE);
			}

			break;

		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
