package com.tjsoft.webhall.ui.grkj;

import java.text.DateFormat;
import java.util.Date;

import org.json.JSONObject;

import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.UserDetail;
import com.tjsoft.webhall.ui.bsdt.AdviceList;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.ReserveSubmit;
import com.tjsoft.webhall.ui.user.ChangeEnterpriseInfo;
import com.tjsoft.webhall.ui.user.ChangePassword;
import com.tjsoft.webhall.ui.user.ChangeUserInfo;
import com.tjsoft.webhall.ui.work.ExamineMSG;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * 个人空间页面
 * @author Administrator
 *
 */
public class GRKJ extends AutoDialogActivity {
	private ImageButton grkj_xxtz, grkj_xgmm, grkj_yhxx,bsdt_wyyy,bsdt_wdyy,bsdt_wypy;
	private DisplayMetrics dm;
	private Button back, home, logout;
	private Intent intent;
	private TextView name, date;
	private UserDetail userDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_grkj"));
		Constants.getInstance().addActivity(this);
		InitView();
		initSetOnListener();
		dialog = Background.Process(this, GetUserDetail, "正在加载");
	}

	private void initSetOnListener() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GRKJ.this.finish();
			}
		});
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GRKJ.this.finish();
			}
		});
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Constants.user = null;
				DialogUtil.showUIToast(GRKJ.this, "您已退出登陆！");
				FileUtil.Write(GRKJ.this, "password", "");
			}
		});
		grkj_xxtz.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GRKJ.this, ExamineMSG.class);
				startActivity(intent);
			}
		});
		grkj_xgmm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GRKJ.this, ChangePassword.class);
				startActivity(intent);
			}
		});
		grkj_yhxx.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (userDetail != null) {
					if (userDetail.getTYPE().equals("1")) {
						intent = new Intent();
						intent.setClass(GRKJ.this, ChangeUserInfo.class);
						intent.putExtra("userDetail", userDetail);
						startActivity(intent);
					} else if (userDetail.getTYPE().equals("2")) {
						intent = new Intent();
						intent.setClass(GRKJ.this, ChangeEnterpriseInfo.class);
						intent.putExtra("userDetail", userDetail);
						startActivity(intent);
					}
				}
				else {
					dialog = Background.Process(GRKJ.this, GetUserDetail, "正在加载");
				}
			}
		});
		bsdt_wyyy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GRKJ.this, ReserveSubmit.class);
				startActivity(intent);
			}
		});
		bsdt_wdyy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GRKJ.this, ReserveList.class);
				startActivity(intent);
			}
		});
		bsdt_wypy.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(GRKJ.this, AdviceList.class);
				startActivity(intent);
			}
		});
	}

	private void InitView() {

		name = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		date = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "date"));
		name.setText(Constants.user.getREALNAME());
		date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));

		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		logout = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "logout"));
		grkj_xxtz = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "grkj_xxtz"));
		grkj_xgmm = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "grkj_xgmm"));
		grkj_yhxx = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "grkj_yhxx"));
		bsdt_wyyy = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wyyy"));
		bsdt_wdyy = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wdyy"));
		bsdt_wypy = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "bsdt_wypy"));

		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		LinearLayout.LayoutParams iconSize = new LinearLayout.LayoutParams((dm.widthPixels - DensityUtil.dip2px(this, 20)) / 3, (dm.widthPixels - DensityUtil.dip2px(this, 20)) / 3);
		ImageButton[] btnList = { grkj_xxtz, grkj_xgmm, grkj_yhxx,bsdt_wyyy,bsdt_wdyy,bsdt_wypy};
		for (int i = 0; i < btnList.length; i++) {
			btnList[i].setLayoutParams(iconSize);
//			btnList[i].setOnClickListener(this);
		}

	}


	final Runnable GetUserDetail = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("ID", Constants.user.getUSER_ID());
				String response = HTTP.excute("getInfoByUserid", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					userDetail = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), UserDetail.class);
					runOnUiThread(new Runnable() {
						public void run() {
						}
					});
				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(GRKJ.this, error);
					}
				}
			} catch (Exception e) {
				DialogUtil.showUIToast(GRKJ.this, getString(MSFWResource.getResourseIdByName(GRKJ.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

}
