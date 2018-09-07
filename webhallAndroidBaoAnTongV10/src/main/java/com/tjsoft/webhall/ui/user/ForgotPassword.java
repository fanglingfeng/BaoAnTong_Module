package com.tjsoft.webhall.ui.user;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.User;

/**
 * 忘记密码页面
 * 
 * @author Administrator
 * 
 */
@SuppressLint("SetJavaScriptEnabled")
public class ForgotPassword extends AutoDialogActivity {

	private EditText MOBILE;
	private EditText USERNAME;
	private EditText USERPID;
	private Button btnFind;
	private Button back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_search_pwd"));
		Constants.getInstance().addActivity(this);

		initView();

	}

	@Override
	protected void onResume() {
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		if (!TextUtils.isEmpty(tm.getLine1Number())) {
			MOBILE.setText(tm.getLine1Number().replace("+86", ""));
		}
		super.onResume();
	}

	private void initView() {
		MOBILE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "MOBILE"));
		USERNAME = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "USERNAME"));
		USERPID = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "USERPID"));
		btnFind = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnFind"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnFind.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = Background.Process(ForgotPassword.this, forgotpassword, "正在请求...");

			}
		});

	}

	final Runnable forgotpassword = new Runnable() {
		@Override
		public void run() {
			try {
				String USERNAME_STR = USERNAME.getText().toString();
				String MOBILE_STR = MOBILE.getText().toString();
				String USERPID_STR = USERPID.getText().toString();

				if (TextUtils.isEmpty(USERNAME_STR)) {
					DialogUtil.showUIToast(ForgotPassword.this, "请输入真实姓名！");
					return;
				}
				if (TextUtils.isEmpty(USERPID_STR)) {
					DialogUtil.showUIToast(ForgotPassword.this, "请输入注册的证件号码！");
					return;
				}
				if (TextUtils.isEmpty(MOBILE_STR)) {
					DialogUtil.showUIToast(ForgotPassword.this, "请输入注册的手机号码");
					return;
				}

				JSONObject param = new JSONObject();
				param.put("USERNAME", USERNAME_STR);
				param.put("USERPID", USERPID_STR);
				param.put("MOBILE", MOBILE_STR);

				String response = HTTP.excute("forgotpassword", "RestUserService", param.toString());
				final JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ForgotPassword.this, "提交成功，请注意查收短信。");

				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(ForgotPassword.this, error);
				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};

}
