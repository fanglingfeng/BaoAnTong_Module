package com.tjsoft.webhall.ui.user;

import org.json.JSONObject;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.ImageLoader;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.UserDetail;
import com.tjsoft.webhall.ui.work.TakePhotos;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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
/**
 * 修改个人信息页面
 * @author Administrator
 *
 */
public class ChangeUserInfo extends AutoDialogActivity {
	private LinearLayout sex_box_lay1, sex_box_lay2;
	private ImageView sex_box_1, sex_box_2;
	private EditText name, email, identNum, mPhone, address;
	private UserDetail userDetail;
	private Spinner identType;
	private String[] identTypeList = { " 请选择", " 身份证", " 军官证", " 士兵证", " 警官证", " 港澳居民来往内地通行证", " 台湾居民来往大陆通行证", " 香港身份证", " 澳门身份证", " 台湾身份证", " 护照" };
	private int idType = 0;
	private Button back, submit;
	private String sex = "1";
	private Button photo1, photo2;
	private Intent intent;
	private ImageView img_front, img_back;
	private ImageLoader imageLoade;
	public static String frontID, backID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_change_user_info"));
		imageLoade = new ImageLoader(this);
		userDetail = (UserDetail) getIntent().getSerializableExtra("userDetail");
		InitView();
		initSetOnListener();

	}

	private void initSetOnListener() {
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChangeUserInfo.this.finish();
			}
		});		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(ChangeUserInfo.this, Submit, "正在提交");
			}
		});		
		sex_box_lay1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sex_box_1.setBackgroundResource(MSFWResource.getResourseIdByName(ChangeUserInfo.this, "drawable", "tj_select_box_on"));
				sex_box_2.setBackgroundResource(MSFWResource.getResourseIdByName(ChangeUserInfo.this, "drawable", "tj_select_box"));
				sex = "1";
			}
		});		
		sex_box_lay2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sex_box_2.setBackgroundResource(MSFWResource.getResourseIdByName(ChangeUserInfo.this, "drawable", "tj_select_box_on"));
				sex_box_1.setBackgroundResource(MSFWResource.getResourseIdByName(ChangeUserInfo.this, "drawable", "tj_select_box"));
				sex = "0";
			}
		});		
		photo1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					intent = new Intent();
					intent.putExtra("mark", 1);
					intent.putExtra("type", 1);
					intent.setClass(ChangeUserInfo.this, TakePhotos.class);
					startActivity(intent);
				} else {
					DialogUtil.showUIToast(ChangeUserInfo.this, getString(MSFWResource.getResourseIdByName(ChangeUserInfo.this, "string", "tj_sdcard_unmonted_hint")));
				}
			}
		});		
		photo2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					intent = new Intent();
					intent.putExtra("mark", 1);
					intent.putExtra("type", 2);
					intent.setClass(ChangeUserInfo.this, TakePhotos.class);
					startActivity(intent);
				} else {
					DialogUtil.showUIToast(ChangeUserInfo.this, getString(MSFWResource.getResourseIdByName(ChangeUserInfo.this, "string", "tj_sdcard_unmonted_hint")));
				}
			}
		});		
	}

	@Override
	protected void onDestroy() {
		frontID = null;
		backID = null;
		super.onDestroy();
	}

	@Override
	protected void onStart() {
		if (null != frontID) {
			imageLoade.DisplayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + frontID, img_front);
		}
		if (null != backID) {
			imageLoade.DisplayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + backID, img_back);
		}
		super.onStart();
	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			switch (index) {
			case 0:
				idType = 0;
				break;
			case 1:
				idType = 10;
				break;
			case 2:
				idType = 11;
				break;
			case 3:
				idType = 12;
				break;
			case 4:
				idType = 13;
				break;
			case 5:
				idType = 14;
				break;
			case 6:
				idType = 15;
				break;
			case 7:
				idType = 16;
				break;
			case 8:
				idType = 17;
				break;
			case 9:
				idType = 18;
				break;
			case 10:
				idType = 20;
				break;

			default:
				break;
			}

		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	private void InitView() {
		identType = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "identType"));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChangeUserInfo.this, android.R.layout.simple_spinner_item, identTypeList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		identType.setAdapter(adapter);
		identType.setOnItemSelectedListener(new SpinnerSelectedListener());
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		sex_box_lay1 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_lay1"));
		sex_box_lay2 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_lay2"));
		sex_box_1 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_1"));
		sex_box_2 = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "sex_box_2"));
		name = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		email = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "email"));
		identNum = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "identNum"));
		mPhone = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "mPhone"));
		address = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "address"));

		img_front = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "img_front"));
		img_back = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "img_back"));
		if (null != userDetail.getIMG_FRONT()) {
			imageLoade.DisplayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + userDetail.getIMG_FRONT(), img_front);
		}
		if(null != userDetail.getIMG_BACK()){
			imageLoade.DisplayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + userDetail.getIMG_BACK(), img_back);
		}

		photo1 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "photo1"));
		photo2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "photo2"));

		if (null != userDetail) {
			name.setText(userDetail.getUSER_NAME());
			email.setText(userDetail.getUSER_EMAIL());
			identNum.setText(userDetail.getUSER_PID());
			mPhone.setText(userDetail.getUSER_MOBILE());
			address.setText(userDetail.getUSER_ADDRESS());
			if (null != userDetail.getUSER_GENDER() && userDetail.getUSER_GENDER().equals("0")) {
				sex_box_2.setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "tj_select_box_on"));
				sex_box_1.setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "tj_select_box"));
				sex = "0";
			}
			if (null != userDetail.getCERTIFICATETYPE() && !userDetail.getCERTIFICATETYPE().equals("")) {
				int type = Integer.parseInt(userDetail.getCERTIFICATETYPE().trim());
				idType = type;
				switch (type) {
				case 10:
					identType.setSelection(1);
					break;
				case 11:
					identType.setSelection(2);
					break;
				case 12:
					identType.setSelection(3);
					break;
				case 13:
					identType.setSelection(4);
					break;
				case 14:
					identType.setSelection(5);
					break;
				case 15:
					identType.setSelection(6);
					break;
				case 16:
					identType.setSelection(7);
					break;
				case 17:
					identType.setSelection(8);
					break;
				case 18:
					identType.setSelection(9);
					break;
				case 20:
					identType.setSelection(10);
					break;

				default:
					break;
				}
			}
		}
	}

	final Runnable Submit = new Runnable() {
		@Override
		public void run() {
			try {

				String USER_NAME = name.getText().toString().trim();
				String USER_EMAIL = email.getText().toString().trim();
				String USER_PID = identNum.getText().toString().trim();
				String USER_MOBILE = mPhone.getText().toString().trim();
				String USER_ADDRESS = address.getText().toString().trim();
				if (null == USER_NAME || USER_NAME.equals("") || USER_NAME.length() > 20 || USER_NAME.length() < 2) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "真实姓名不合法");
					return;
				}
				if (null != USER_EMAIL && !USER_EMAIL.equals("") && !USER_EMAIL.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "邮箱地址不合法，请重新输入！");
					return;
				}
				if (idType == 0) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "请选择证件类型");
					return;
				}
				if (null == USER_PID || USER_PID.equals("")) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "证件号码不能为空！");
					return;
				}
				if (null == USER_MOBILE || USER_MOBILE.equals("")) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "手机号不能为空！");
					return;
				}
				if (!USER_MOBILE.matches("^(\\d{3,4}-?)?\\d{7,9}$")) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "手机号不合法！");
					return;
				}
				if (null == USER_ADDRESS || USER_ADDRESS.equals("")) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "联系地址不能为空！");
					return;
				}
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("ID", Constants.user.getUSER_ID());
				param.put("TYPE", "1");
				param.put("USER_GENDER", sex);
				param.put("USER_NAME", USER_NAME);
				param.put("USER_EMAIL", USER_EMAIL);
				param.put("CERTIFICATETYPE", idType + "");
				param.put("USER_PID", USER_PID);
				param.put("USER_MOBILE", USER_MOBILE);
				param.put("USER_ADDRESS", USER_ADDRESS);

				String response = HTTP.excute("modifyinfo", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ChangeUserInfo.this, "保存成功");

				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(ChangeUserInfo.this, error);
					}
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ChangeUserInfo.this, getString(MSFWResource.getResourseIdByName(ChangeUserInfo.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

}
