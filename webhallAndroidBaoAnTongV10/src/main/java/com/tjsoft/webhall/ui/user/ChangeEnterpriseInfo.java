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
import android.widget.Spinner;
/**
 * 修改企业信息页面
 * @author Administrator
 *
 */
public class ChangeEnterpriseInfo extends AutoDialogActivity {
	private EditText INC_NAME, INC_PERMIT, INC_ZZJGDM, INC_DEPUTY, INC_PID, AGE_NAME, AGE_PID, AGE_EMAIL, AGE_MOBILE, AGE_PHONE, AGE_INDICIA, AGE_ADDR;
	private UserDetail userDetail;
	private Spinner INC_TYPE;
	private int incType = 0;
	private Button back, submit;
	private String[] incTypeList = { " 请选择", " 国有", " 民营", " 外资", " 港澳台资", " 其他" };
	private Button photo1, photo2;
	private Intent intent;
	private ImageView img_front, img_back;
	private ImageLoader imageLoade;
	public static String frontID, backID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_change_enterprise_info"));
		imageLoade = new ImageLoader(this);
		InitView();
		initSetOnListener();
	}
	private void initSetOnListener() {
		photo1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
					intent = new Intent();
					intent.putExtra("mark", 1);
					intent.putExtra("type", 1);
					intent.setClass(ChangeEnterpriseInfo.this, TakePhotos.class);
					startActivity(intent);
				} else {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, getString(MSFWResource.getResourseIdByName(ChangeEnterpriseInfo.this, "string", "tj_sdcard_unmonted_hint")));
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
					intent.setClass(ChangeEnterpriseInfo.this, TakePhotos.class);
					startActivity(intent);
				} else {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, getString(MSFWResource.getResourseIdByName(ChangeEnterpriseInfo.this, "string", "tj_sdcard_unmonted_hint")));
				}
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChangeEnterpriseInfo.this.finish();
			}
		});
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(ChangeEnterpriseInfo.this, Submit, "正在提交...");
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

	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		photo1 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "photo1"));
		photo2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "photo2"));
		img_front = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "img_front"));
		img_back = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "img_back"));
		INC_NAME = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_NAME"));
		INC_PERMIT = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_PERMIT"));
		INC_ZZJGDM = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_ZZJGDM"));
		INC_DEPUTY = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_DEPUTY"));
		INC_PID = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_PID"));
		AGE_NAME = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_NAME"));
		AGE_PID = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_PID"));
		AGE_EMAIL = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_EMAIL"));
		AGE_MOBILE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_MOBILE"));
		AGE_PHONE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_PHONE"));
		AGE_ADDR = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_ADDR"));
		AGE_INDICIA = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "AGE_INDICIA"));
		INC_TYPE = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "INC_TYPE"));
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChangeEnterpriseInfo.this, android.R.layout.simple_spinner_item, incTypeList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		INC_TYPE.setAdapter(adapter);
		INC_TYPE.setOnItemSelectedListener(new SpinnerSelectedListener());
		userDetail = (UserDetail) getIntent().getSerializableExtra("userDetail");
		
	
		if (null != userDetail.getIMG_FRONT()) {
			imageLoade.DisplayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + userDetail.getIMG_FRONT(), img_front);
		}
		if(null != userDetail.getIMG_BACK()){
			imageLoade.DisplayImage(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + userDetail.getIMG_BACK(), img_back);
		}

		
		if (null != userDetail) {
			INC_NAME.setText(userDetail.getINC_NAME());
			INC_PERMIT.setText(userDetail.getINC_PERMIT());
			INC_ZZJGDM.setText(userDetail.getINC_ZZJGDM());
			INC_DEPUTY.setText(userDetail.getINC_DEPUTY());
			INC_PID.setText(userDetail.getINC_PID());

			AGE_NAME.setText(userDetail.getAGE_NAME());
			AGE_PID.setText(userDetail.getAGE_PID());
			AGE_EMAIL.setText(userDetail.getAGE_EMAIL());
			AGE_MOBILE.setText(userDetail.getAGE_MOBILE());
			AGE_PHONE.setText(userDetail.getAGE_PHONE());
			AGE_ADDR.setText(userDetail.getAGE_ADDR());
			AGE_INDICIA.setText(userDetail.getAGE_INDICIA());

			if (null != userDetail.getINC_TYPE() && userDetail.getINC_TYPE().trim().equals("1")) {
				INC_TYPE.setSelection(1);
				incType = 1;
			}
			if (null != userDetail.getINC_TYPE() && userDetail.getINC_TYPE().trim().equals("2")) {
				INC_TYPE.setSelection(2);
				incType = 2;
			}
			if (null != userDetail.getINC_TYPE() && userDetail.getINC_TYPE().trim().equals("3")) {
				INC_TYPE.setSelection(3);
				incType = 3;
			}
			if (null != userDetail.getINC_TYPE() && userDetail.getINC_TYPE().trim().equals("4")) {
				INC_TYPE.setSelection(4);
				incType = 4;
			}
			if (null != userDetail.getINC_TYPE() && userDetail.getINC_TYPE().trim().equals("5")) {
				INC_TYPE.setSelection(5);
				incType = 5;
			}
		}

	}

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			incType = index;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	final Runnable Submit = new Runnable() {
		@Override
		public void run() {
			try {

				String incNAME = INC_NAME.getText().toString().trim();
				String incPERMIT = INC_PERMIT.getText().toString().trim();
				String incZZJGDM = INC_ZZJGDM.getText().toString().trim();
				String incDEPUTY = INC_DEPUTY.getText().toString().trim();
				String incPID = INC_PID.getText().toString().trim();
				String ageNAME = AGE_NAME.getText().toString().trim();
				String agePID = AGE_PID.getText().toString().trim();
				String ageEMAIL = AGE_EMAIL.getText().toString().trim();
				String ageMOBILE = AGE_MOBILE.getText().toString().trim();
				String agePHONE = AGE_PHONE.getText().toString().trim();
				String ageINDICIA = AGE_INDICIA.getText().toString().trim();
				String ageADDR = AGE_ADDR.getText().toString().trim();

				JSONObject param = new JSONObject();

				if (null == incNAME || incNAME.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "企业名称不能为空");
					return;
				}
				if (incType == 0) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "请选择企业类型");
					return;
				}
				if (null == incDEPUTY || incDEPUTY.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "法人代表不能为空");
					return;
				}
				if (null == incPID || incPID.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "法人身份证号码不能为空");
					return;
				}
				if (!incPID.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z]))$")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "法人身份证格式不合法！");
					return;
				}
				if (null == ageNAME || ageNAME.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "联系人姓名不能为空");
					return;
				}
				if (null == agePID || agePID.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "联系人身份证号码不能为空");
					return;
				}
				if (!agePID.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z]))$")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "联系人身份证格式不合法！");
					return;
				}
				if (null == ageEMAIL || ageEMAIL.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "联系人电子邮箱不能为空");
					return;
				}
				if (null != ageEMAIL && !ageEMAIL.equals("") && !ageEMAIL.matches("^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "邮箱地址不合法，请重新输入！");
					return;
				}
				if (null == ageMOBILE || ageMOBILE.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "联系人手机号码不能为空");
					return;
				}
				if (!ageMOBILE.matches("^(\\d{3,4}-?)?\\d{7,9}$")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "联系人手机号不合法！");
					return;
				}
				if (null == ageADDR || ageADDR.equals("")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "联系人地址不能为空");
					return;
				}

				param.put("token", Constants.user.getTOKEN());
				param.put("ID", Constants.user.getUSER_ID());
				param.put("TYPE", "2");
				param.put("INC_NAME", incNAME);
				param.put("INC_TYPE", incType+"");
				param.put("INC_PERMIT", incPERMIT);
				param.put("INC_ZZJGDM", incZZJGDM);
				param.put("INC_DEPUTY", incDEPUTY);
				param.put("INC_PID", incPID);
				param.put("AGE_NAME", ageNAME);
				param.put("AGE_PID", agePID);
				param.put("AGE_EMAIL", ageEMAIL);
				param.put("AGE_MOBILE", ageMOBILE);
				param.put("AGE_PHONE", agePHONE);
				param.put("AGE_INDICIA", ageINDICIA);
				param.put("AGE_ADDR", ageADDR);

				String response = HTTP.excute("modifyinfo", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ChangeEnterpriseInfo.this, "修改成功！");

				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(ChangeEnterpriseInfo.this, error);
					}
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ChangeEnterpriseInfo.this, getString(MSFWResource.getResourseIdByName(ChangeEnterpriseInfo.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

}
