package com.tjsoft.webhall.ui.user;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Paint;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.User;

public class Login extends Activity {

	private Button back, register, login;
	private Intent intent;
	private EditText userName, password;
	private LinearLayout autoLogon;
	private ImageView autoLogonBox;
	private TextView forgotPwd;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_login"));
		Constants.getInstance().addActivity(this);
		InitView();
		initSetOnListener();
	}

	@Override
	protected void onResume() {
		SharedPreferences config = Login.this.getSharedPreferences("config", MODE_PRIVATE);
		Editor editor = config.edit();
		editor.putBoolean("autoLogon", true);
		editor.commit();
		autoLogonBox.setBackgroundResource(MSFWResource.getResourseIdByName(Login.this, "drawable", "tj_select_box_on"));

		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		if (!TextUtils.isEmpty(tm.getLine1Number())&&TextUtils.isEmpty(FileUtil.Load(this, "username"))) {//手机号码不为空且账号为空
			userName.setText(tm.getLine1Number().replace("+86", ""));
		}
		
		super.onResume();
	}

	private void initSetOnListener() {
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Login.this.finish();
			}
		});
		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(Login.this, Register.class);
				startActivity(intent);
				Login.this.finish();
			}
		});
		login.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Background.Process(Login.this, Login, "正在登录...");

			}
		});
		autoLogon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences config = Login.this.getSharedPreferences("config", MODE_PRIVATE);
				boolean autoLogon = config.getBoolean("autoLogon", false);
				Editor editor = config.edit();
				if (!autoLogon) {
					autoLogon = true;
					editor.putBoolean("autoLogon", true);
					editor.commit();
					autoLogonBox.setBackgroundResource(MSFWResource.getResourseIdByName(Login.this, "drawable", "tj_select_box_on"));
				} else {
					autoLogon = false;
					editor.putBoolean("autoLogon", false);
					editor.commit();
					autoLogonBox.setBackgroundResource(MSFWResource.getResourseIdByName(Login.this, "drawable", "tj_select_box"));
					FileUtil.Write(Login.this, "password", "");
				}
			}
		});
		forgotPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(Login.this, ForgotPassword.class);
				startActivity(intent);
			}
		});
	}

	private void InitView() {

		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		register = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "register"));
		login = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "login"));
		userName = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "userName"));
		password = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "password"));
		autoLogon = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "autoLogon"));
		autoLogonBox = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "autoLogonBox"));
		forgotPwd = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "forgotPwd"));
		forgotPwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		register.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线

		userName.setText(FileUtil.Load(this, "username"));
		SharedPreferences config = Login.this.getSharedPreferences("config", MODE_PRIVATE);
		boolean autoLogon = config.getBoolean("autoLogon", false);
		if (autoLogon) {
			autoLogonBox.setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "tj_select_box_on"));
			if (TextUtils.isEmpty(FileUtil.Load(this, "username"))) {
				return;
			}
			if (TextUtils.isEmpty(FileUtil.Load(this, "password"))) {
				return;
			}
			password.setText(FileUtil.Load(this, "password"));
			Background.Process(Login.this, Login, "正在登录...");
		}

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("数据加载中...");

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	final Runnable Login = new Runnable() {
		@Override
		public void run() {
			try {
				final String USERNAME = userName.getText().toString();
				final String PASSWORD = password.getText().toString();
				if (null == USERNAME || USERNAME.equals("")) {
					DialogUtil.showUIToast(Login.this, "账户名不能为空");
					return;
				}
				if (null == PASSWORD || PASSWORD.equals("")) {
					DialogUtil.showUIToast(Login.this, "密码不能为空");
					return;
				}

				JSONObject param = new JSONObject();
				param.put("USERNAME", USERNAME);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(PASSWORD));
				String response = HTTP.excute("login", "RestUserService", param.toString());
				final JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {

					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
					DialogUtil.showUIToast(Login.this, "登录成功！");
					FileUtil.Write(Login.this, "username", USERNAME);
					FileUtil.Write(Login.this, "password", PASSWORD);
					finish();
					/*
					 * runOnUiThread(new Runnable() { public void run() {
					 * GloabDelegete gloabDelegete =
					 * Constants.getInstance().getGloabDelegete(); if
					 * (gloabDelegete != null) {// 宝安通登录
					 * gloabDelegete.login(USERNAME, PASSWORD); } } });
					 */

				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(Login.this, error);
				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};

}
