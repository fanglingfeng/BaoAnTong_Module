package com.tjsoft.webhall.ui.user;

import java.util.Date;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.entity.UserDetail;
import com.tjsoft.webhall.imp.GloabDelegete;

public class Register extends Activity {
	private Intent intent;
	private EditText userName, password, password2, name, identNum, mPhone, email, address;
	private EditText INC_NAME, INC_PERMIT, INC_ZZJGDM, INC_DEPUTY, INC_PID, INC_ADDR, INC_INDICIA, INC_PHONE, INC_FAX, INC_NETWORK, INC_EMAIL, AGE_NAME, AGE_PID, AGE_EMAIL, AGE_MOBILE, AGE_PHONE, AGE_INDICIA, AGE_ADDR;
	private Button back, home, submit;
	private ImageView sex_box_1, sex_box_2;
	private LinearLayout sex_box_lay1, sex_box_lay2;
	private LinearLayout persion_form, enterprise_form;
	private Spinner identType, INC_TYPE;
	private int type = 0;
	private String sex = "1";
	private String idType = null;
	private String incType = "0";
	private String[] identTypeList = { " 请选择", " 身份证", " 军官证", " 士兵证", " 警官证", " 港澳居民来往内地通行证", " 台湾居民来往大陆通行证", " 香港身份证", " 澳门身份证", " 台湾身份证", " 护照" };
	private String[] incTypeList = { " 请选择", " 国有", " 民营", " 外资", " 港澳台资", " 其他" };
	private Button type_box_1, type_box_2;
	private final int PERSONAGE = 0; // 个人
	private final int ENTERPRISE = 1; // 企业
	private UserDetail userDetail;
	private final static int SHOW_DIALOG = 2; // 登录时显示进度对话框
	private final static int CLOSE_DIALOG = 3; // 关闭进度对话框
	private final static int SHOW_TOAST = 4; // 显示提示框
	private ProgressDialog progressDialog;
	private TransportEntity transportEntity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_register"));
		Constants.getInstance().addActivity(this);

		InitView();
		initData();
		initSetOnListener();
	}

	@Override
	protected void onResume() {
		TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		if (!TextUtils.isEmpty(tm.getLine1Number())) {
			userName.setText(tm.getLine1Number().replace("+86", ""));
			mPhone.setText(tm.getLine1Number().replace("+86", ""));
//			userName.setText(tm.getLine1Number());
//			userName.setText(tm.getLine1Number());
		}
		super.onResume();
	}

	/**
	 * 将宝安通的数据填充到注册页面
	 */
	private void initData() {
		transportEntity = (TransportEntity) getIntent().getSerializableExtra("transportEntity");
//		if (null != transportEntity) {
//			if (!TextUtils.isEmpty(transportEntity.getBatUserName())) {
//				userName.setText(transportEntity.getBatUserName());
//			}
//			if (!TextUtils.isEmpty(transportEntity.getRealName())) {
//				name.setText(transportEntity.getRealName());
//				INC_NAME.setText(transportEntity.getRealName());
//				AGE_NAME.setText(transportEntity.getRealName());
//			}
//			if (!TextUtils.isEmpty(transportEntity.getIdCardNo())) {
//				identNum.setText(transportEntity.getIdCardNo());
//			}
//			if (!TextUtils.isEmpty(transportEntity.getMobile())) {
//				mPhone.setText(transportEntity.getMobile());
//				INC_PHONE.setText(transportEntity.getMobile());
//				AGE_PHONE.setText(transportEntity.getMobile());
//			}
//		}

	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PERSONAGE:
			case ENTERPRISE:
				Background.Process(Register.this, Login, getString(MSFWResource.getResourseIdByName(Register.this, "string", "tj_loding")));
				break;
			case SHOW_DIALOG:
				if (progressDialog != null) {
					progressDialog.show();
				}
				break;
			case CLOSE_DIALOG:
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				break;
			case SHOW_TOAST:
				DialogUtil.showMyToast(Register.this, "绑定宝安通失败！");
				break;
			default:
				break;
			}

		}
	};

	final Runnable Login = new Runnable() {
		@Override
		public void run() {
			try {
				String USERNAME = userName.getText().toString().trim();
				String PASSWORD = password.getText().toString().trim();
				JSONObject param = new JSONObject();
				param.put("USERNAME", USERNAME);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(PASSWORD));
				String response = HTTP.excute("login", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
					// DialogUtil.showUIToast(Register.this, "登录成功！");
					// FileUtil.Write(Register.this, "username", USERNAME);
					// FileUtil.Write(Register.this, "password", PASSWORD);

				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(Register.this, error);
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(Register.this, getString(MSFWResource.getResourseIdByName(Register.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

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
							if (userDetail.getTYPE().equals("1")) {
								intent = new Intent();
								intent.setClass(Register.this, ChangeUserInfo.class);
								intent.putExtra("userDetail", userDetail);
								startActivity(intent);
							} else if (userDetail.getTYPE().equals("2")) {
								intent = new Intent();
								intent.setClass(Register.this, ChangeEnterpriseInfo.class);
								intent.putExtra("userDetail", userDetail);
								startActivity(intent);
							}
						}
					});
					Register.this.finish();
				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(Register.this, error);
					}
					Register.this.finish();
				}
			} catch (Exception e) {
				DialogUtil.showUIToast(Register.this, getString(MSFWResource.getResourseIdByName(Register.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
				Register.this.finish();
			}
		}
	};

	private void initSetOnListener() {
		userName.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
		type_box_1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type_box_1.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_left_tag_press"));
				type_box_1.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(Register.this, "color", "tj_main_color")));
				type_box_2.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_right_tag"));
				type_box_2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(Register.this, "color", "tj_white")));
				persion_form.setVisibility(View.VISIBLE);
				enterprise_form.setVisibility(View.GONE);
				type = 0;
			}
		});
		type_box_2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				type_box_1.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_left_tag"));
				type_box_2.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_right_tag_press"));
				type_box_1.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(Register.this, "color", "tj_white")));
				type_box_2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(Register.this, "color", "tj_main_color")));
				persion_form.setVisibility(View.GONE);
				enterprise_form.setVisibility(View.VISIBLE);
				type = 1;
			}
		});
		sex_box_lay1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex_box_1.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_select_box_on"));
				sex_box_2.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_select_box"));
				sex = "1";
			}
		});
		sex_box_lay2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sex_box_2.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_select_box_on"));
				sex_box_1.setBackgroundResource(MSFWResource.getResourseIdByName(Register.this, "drawable", "tj_select_box"));
				sex = "0";
			}
		});
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type == 0) {
					Background.Process(Register.this, Submit1, getString(MSFWResource.getResourseIdByName(Register.this, "string", "tj_loding")));
				} else {
					Background.Process(Register.this, Submit2, getString(MSFWResource.getResourseIdByName(Register.this, "string", "tj_loding")));
				}
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Register.this.finish();
			}
		});
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});
	}

	private void InitView() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("数据加载中...");
		persion_form = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "persion_form"));
		enterprise_form = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "enterprise_form"));
		userName = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "userName"));
		password = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "password"));
		password2 = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "password2"));
		name = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		identNum = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "identNum"));
		mPhone = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "mPhone"));
		email = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "email"));
		address = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "address"));

		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));

		type_box_1 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "type_box_1"));
		type_box_2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "type_box_2"));
		sex_box_1 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_1"));
		sex_box_2 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_2"));

		sex_box_lay1 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_lay1"));
		sex_box_lay2 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_lay2"));

		identType = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "identType"));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, identTypeList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		identType.setAdapter(adapter);
		identType.setOnItemSelectedListener(new SpinnerSelectedListener());

		// 企业form
		INC_TYPE = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_TYPE"));
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(Register.this, android.R.layout.simple_spinner_item, incTypeList);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		INC_TYPE.setAdapter(adapter2);
		INC_TYPE.setOnItemSelectedListener(new SpinnerSelectedListener2());
		INC_NAME = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_NAME"));
		INC_PERMIT = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_PERMIT"));
		INC_ZZJGDM = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_ZZJGDM"));
		INC_DEPUTY = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_DEPUTY"));
		INC_PID = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_PID"));
		INC_ADDR = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_ADDR"));
		INC_INDICIA = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_INDICIA"));
		INC_PHONE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_PHONE"));
		INC_FAX = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_FAX"));
		INC_NETWORK = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_NETWORK"));
		INC_EMAIL = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_EMAIL"));
		AGE_NAME = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_NAME"));
		AGE_PID = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_PID"));
		AGE_EMAIL = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_EMAIL"));
		AGE_MOBILE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_MOBILE"));
		AGE_PHONE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_PHONE"));
		AGE_INDICIA = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_INDICIA"));
		AGE_ADDR = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_ADDR"));

	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			switch (index) {
			case 0:
				idType = null;
				break;
			case 1:
				idType = "10";
				break;
			case 2:
				idType = "11";
				break;
			case 3:
				idType = "12";
				break;
			case 4:
				idType = "13";
				break;
			case 5:
				idType = "14";
				break;
			case 6:
				idType = "15";
				break;
			case 7:
				idType = "16";
				break;
			case 8:
				idType = "17";
				break;
			case 9:
				idType = "18";
				break;
			case 10:
				idType = "20";
				break;

			default:
				break;
			}

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener2 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			incType = index + "";

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	final Runnable Submit1 = new Runnable() {
		@Override
		public void run() {
			try {
				final String USERNAME = userName.getText().toString().trim();
				final String PASSWORD = password.getText().toString().trim();
				String PASSWORD2 = password2.getText().toString().trim();
				final String REALNAME = name.getText().toString().trim();
				String EMAIL = email.getText().toString().trim();
				String USER_GENDER = sex;
				String CERTIFICATETYPE = idType;
				String USER_PID = identNum.getText().toString().trim();
				String USER_ADDRESS = address.getText().toString().trim();
				final String USER_MOBILE = mPhone.getText().toString().trim();
				JSONObject param = new JSONObject();
				if (!USERNAME.matches("^[a-zA-Z]\\w{2,13}$")) {
					DialogUtil.showUIToast(Register.this, "账号不合法，账号需以字母开头，且只能是由字母、数字或下划线组成的长度为3-14个字符的字符串！");
					return;
				}
				if (null == PASSWORD || PASSWORD.equals("") || null == PASSWORD2 || PASSWORD2.equals("")) {
					DialogUtil.showUIToast(Register.this, "密码不能为空！");
					return;
				}
				if (PASSWORD.length() < 3 || PASSWORD.length() > 16) {
					DialogUtil.showUIToast(Register.this, "密码格式不正确，请输入3-16位有效密码！");
					return;
				}
				if (!PASSWORD2.equals(PASSWORD)) {
					DialogUtil.showUIToast(Register.this, "两次输入密码不一致！");
					return;
				}
/*				if (null != EMAIL && !EMAIL.equals("") && !EMAIL.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					DialogUtil.showUIToast(Register.this, "邮箱地址不合法，请重新输入！");
					return;
				}*/
				if (null == REALNAME || REALNAME.equals("")) {
					DialogUtil.showUIToast(Register.this, "真实姓名不能为空");
					return;
				}
				if (!REALNAME.matches("^[A-Za-z0-9\u4e00-\u9fa5]+$")) {
					DialogUtil.showUIToast(Register.this, "真实姓名不合法");
					return;
				}
				if (null == CERTIFICATETYPE || CERTIFICATETYPE.equals("0")) {
					DialogUtil.showUIToast(Register.this, "请选择证件类型！");
					return;
				}
				if (null == USER_PID || USER_PID.equals("")) {
					DialogUtil.showUIToast(Register.this, "证件号码不能为空！");
					return;
				}
				if (CERTIFICATETYPE.equals("10") && !USER_PID.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z]))$")) {
					DialogUtil.showUIToast(Register.this, "证件号不合法！");
					return;
				}
				if (null == USER_MOBILE || USER_MOBILE.equals("")) {
					DialogUtil.showUIToast(Register.this, "手机号不能为空！");
					return;
				}
				if (!USER_MOBILE.matches("^[1][3-8]\\d{9}$")) {
					DialogUtil.showUIToast(Register.this, "手机号不合法！");
					return;
				}
/*
				if (null == USER_ADDRESS || USER_ADDRESS.equals("")) {
					DialogUtil.showUIToast(Register.this, "联系地址不能为空！");
					return;
				}
				if (!USER_ADDRESS.matches("^[A-Za-z0-9\u4e00-\u9fa5]+$")) {
					DialogUtil.showUIToast(Register.this, "联系地址输入不合法");
					return;
				}*/

				param.put("USERNAME", USERNAME);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(PASSWORD));
				param.put("REALNAME", REALNAME);
//				param.put("EMAIL", EMAIL);
				param.put("REGISTER_TIME", new Date());
//				param.put("USER_GENDER", USER_GENDER);
				param.put("CERTIFICATETYPE", CERTIFICATETYPE);
				param.put("USER_PID", USER_PID);
				param.put("USER_MOBILE", USER_MOBILE);
//				param.put("USER_ADDRESS", USER_ADDRESS);

				String response = HTTP.excute("registerUser", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(Register.this, "注册成功！");
					FileUtil.Write(Register.this, "username", USERNAME);
					FileUtil.Write(Register.this, "password", PASSWORD);
					new Thread(autoLogin).start();// 自动登陆
					runOnUiThread(new Runnable() {
						public void run() {
							GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
							if (gloabDelegete != null) {// 宝安通注册
								gloabDelegete.Registered(USERNAME, PASSWORD, USER_MOBILE);
							}
						}
					});
					finish();

				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(Register.this, error);
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(Register.this, getString(MSFWResource.getResourseIdByName(Register.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	final Runnable Submit2 = new Runnable() {
		@Override
		public void run() {
			try {
				final String USERNAME = userName.getText().toString().trim();
				final String PASSWORD = password.getText().toString().trim();
				String PASSWORD2 = password2.getText().toString().trim();
				String EMAIL = email.getText().toString().trim();
				final String incNAME = INC_NAME.getText().toString().trim();
				String incPERMIT = INC_PERMIT.getText().toString().trim();
				String incZZJGDM = INC_ZZJGDM.getText().toString().trim();
				String incDEPUTY = INC_DEPUTY.getText().toString().trim();
				String incPID = INC_PID.getText().toString().trim();
				String incADDR = INC_ADDR.getText().toString().trim();
				String incINDICIA = INC_INDICIA.getText().toString().trim();
				String incPHONE = INC_PHONE.getText().toString().trim();
				String incFAX = INC_FAX.getText().toString().trim();
				String incNETWORK = INC_NETWORK.getText().toString().trim();
				String incEMAIL = INC_EMAIL.getText().toString().trim();
				String ageNAME = AGE_NAME.getText().toString().trim();
				String agePID = AGE_PID.getText().toString().trim();
				String ageEMAIL = AGE_EMAIL.getText().toString().trim();
				final String ageMOBILE = AGE_MOBILE.getText().toString().trim();
				String agePHONE = AGE_PHONE.getText().toString().trim();
				String ageINDICIA = AGE_INDICIA.getText().toString().trim();
				String ageADDR = AGE_ADDR.getText().toString().trim();

				JSONObject param = new JSONObject();
				if (!USERNAME.matches("^[a-zA-Z]\\w{2,13}$")) {
					DialogUtil.showUIToast(Register.this, "账号不合法，账号需以字母开头，且只能是由字母、数字或下划线组成的长度为3-14个字符的字符串！");
					return;
				}
				if (null == PASSWORD || PASSWORD.equals("") || null == PASSWORD2 || PASSWORD2.equals("")) {
					DialogUtil.showUIToast(Register.this, "密码不能为空！");
					return;
				}
				if (PASSWORD.length() < 3 || PASSWORD.length() > 16) {
					DialogUtil.showUIToast(Register.this, "密码格式不正确，请输入3-16位有效密码！");
					return;
				}
				if (!PASSWORD2.equals(PASSWORD)) {
					DialogUtil.showUIToast(Register.this, "两次输入密码不一致！");
					return;
				}
				if (null == EMAIL || EMAIL.equals("")) {
					DialogUtil.showUIToast(Register.this, "邮箱地址不能为空");
					return;
				}
				if (null != EMAIL && !EMAIL.equals("") && !EMAIL.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					DialogUtil.showUIToast(Register.this, "邮箱地址不合法，请重新输入！");
					return;
				}

				if (null == incNAME || incNAME.equals("")) {
					DialogUtil.showUIToast(Register.this, "企业名称不能为空");
					return;
				}
				if (!incNAME.matches("^[A-Za-z0-9\u4e00-\u9fa5]+$")) {
					DialogUtil.showUIToast(Register.this, "企业名称不合法");
					return;
				}

				if (null == incType || incType.equals("0")) {
					DialogUtil.showUIToast(Register.this, "请选择企业类型");
					return;
				}
				if (null == incZZJGDM || incZZJGDM.equals("")) {
					DialogUtil.showUIToast(Register.this, "组织机构代码不能为空");
					return;
				}
				if (null == incDEPUTY || incDEPUTY.equals("")) {
					DialogUtil.showUIToast(Register.this, "法人代表不能为空");
					return;
				}
				if (!incDEPUTY.matches("^[A-Za-z0-9\u4e00-\u9fa5]+$")) {
					DialogUtil.showUIToast(Register.this, "法人代表输入不合法");
					return;
				}
				if (null == incPID || incPID.equals("")) {
					DialogUtil.showUIToast(Register.this, "法人身份证号码不能为空");
					return;
				}
				if (!incPID.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z]))$")) {
					DialogUtil.showUIToast(Register.this, "法人身份证格式不合法！");
					return;
				}
				if (null == ageNAME || ageNAME.equals("")) {
					DialogUtil.showUIToast(Register.this, "联系人姓名不能为空");
					return;
				}
				if (!ageNAME.matches("^[A-Za-z0-9\u4e00-\u9fa5]+$")) {
					DialogUtil.showUIToast(Register.this, "联系人姓名不合法");
					return;
				}
				if (null == agePID || agePID.equals("")) {
					DialogUtil.showUIToast(Register.this, "联系人身份证号码不能为空");
					return;
				}
				if (!agePID.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z]))$")) {
					DialogUtil.showUIToast(Register.this, "联系人身份证格式不合法！");
					return;
				}
				if (null == ageEMAIL || ageEMAIL.equals("")) {
					DialogUtil.showUIToast(Register.this, "联系人电子邮箱不能为空");
					return;
				}
				if (null != ageEMAIL && !ageEMAIL.equals("") && !ageEMAIL.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					DialogUtil.showUIToast(Register.this, "邮箱地址不合法，请重新输入！");
					return;
				}
				if (null == ageMOBILE || ageMOBILE.equals("")) {
					DialogUtil.showUIToast(Register.this, "联系人手机号码不能为空");
					return;
				}
				if (!ageMOBILE.matches("^[1][3-8]\\d{9}$")) {
					DialogUtil.showUIToast(Register.this, "联系人手机号不合法！");
					return;
				}
				if (null == ageADDR || ageADDR.equals("")) {
					DialogUtil.showUIToast(Register.this, "联系人地址不能为空");
					return;
				}
				if (!ageADDR.matches("^[A-Za-z0-9\u4e00-\u9fa5]+$")) {
					DialogUtil.showUIToast(Register.this, "联系人地址不合法");
					return;
				}

				param.put("USERNAME", USERNAME);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(PASSWORD));
				param.put("EMAIL", EMAIL);
				param.put("INC_NAME", incNAME);
				param.put("INC_TYPE", incType);
				param.put("INC_PERMIT", incPERMIT);
				param.put("INC_ZZJGDM", incZZJGDM);
				param.put("INC_DEPUTY", incDEPUTY);
				param.put("INC_PID", incPID);
				param.put("INC_ADDR", incADDR);
				param.put("INC_INDICIA", incINDICIA);
				param.put("INC_PHONE", incPHONE);
				param.put("INC_FAX", incFAX);
				param.put("INC_NETWORK", incNETWORK);
				param.put("INC_EMAIL", incEMAIL);
				param.put("AGE_NAME", ageNAME);
				param.put("AGE_PID", agePID);
				param.put("AGE_EMAIL", ageEMAIL);
				param.put("AGE_MOBILE", ageMOBILE);
				param.put("AGE_PHONE", agePHONE);
				param.put("AGE_INDICIA", ageINDICIA);
				param.put("AGE_ADDR", ageADDR);

				String response = HTTP.excute("registerInc", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(Register.this, "注册成功！");

					FileUtil.Write(Register.this, "username", USERNAME);
					FileUtil.Write(Register.this, "password", PASSWORD);
					new Thread(autoLogin).start();
					runOnUiThread(new Runnable() {
						public void run() {
							GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
							System.out.println("#########gloabDelegete      " + gloabDelegete);
							if (gloabDelegete != null) {// 宝安通注册
								gloabDelegete.Registered(USERNAME, PASSWORD, ageMOBILE);
							}
						}
					});
					finish();

				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(Register.this, error);
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(Register.this, getString(MSFWResource.getResourseIdByName(Register.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	final Runnable autoLogin = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("USERNAME", FileUtil.Load(Register.this, "username"));
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(FileUtil.Load(Register.this, "password")));
				String response = HTTP.excute("login", "RestUserService", param.toString());
				final JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
					// DialogUtil.showUIToast(Register.this, "登录成功！");

				} else {
				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};

}
