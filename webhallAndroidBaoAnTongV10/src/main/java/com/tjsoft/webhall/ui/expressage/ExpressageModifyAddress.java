package com.tjsoft.webhall.ui.expressage;

import org.json.JSONObject;

import com.tjsoft.util.AddressPickerHelper;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.InputMethodUtils;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.PostInfo;
import com.tjsoft.webhall.ui.work.HistoreShare;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ExpressageModifyAddress extends AutoDialogActivity{
	private EditText RECEIVE, PHONE, ADDRESS;
	private Button save;
	private RelativeLayout back;
	private TextView title;
	private int flag;
	private int type;//0为默认  1.寄件人
	private Button deleteAddress;
	
	private PostInfo postInfo;
	private EditText edtAddress;
	private AddressPickerHelper addressPickerHelper;
	private TextView tvName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "activity_modify_address"));
		flag=getIntent().getIntExtra("flag", 0);
		type=getIntent().getIntExtra("type", 0);
		postInfo=(PostInfo) getIntent().getSerializableExtra("POSTINFO");
		addressPickerHelper = new AddressPickerHelper(this);
		initView();
	}
	/**
	 * 
	 */
	private void initView() {
		
		title=(TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
		tvName =(TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "tv_name"));

		tvName.setText(type == 0 ? "收件人":"寄件人");
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		RECEIVE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "RECEIVE"));
		PHONE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "PHONE"));
		ADDRESS = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "ADDRESS"));
		edtAddress = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "edt_address"));

		save = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "save"));
//		save.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(flag==0){
					dialog = Background.Process(ExpressageModifyAddress.this, AddUserPostInfo, "正在保存");
				}else{
					dialog = Background.Process(ExpressageModifyAddress.this, ModifyPostInfo, "正在保存");
				}
				
			}
		});
		deleteAddress=(Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "deleteAddress"));
		deleteAddress.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = Background.Process(ExpressageModifyAddress.this, DeleteUserPostInfo,
						getString(MSFWResource.getResourseIdByName(ExpressageModifyAddress.this, "string", "tj_loding")));
			}
		});
		edtAddress.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodUtils.hideInputMethod(edtAddress);
				addressPickerHelper.ShowPickerView(new AddressPickerHelper.onSelectedListener() {
					@Override
					public void onSelected(String province,String city,String country) {
//						Toast.makeText(ExpressageModifyAddress.this,address,Toast.LENGTH_LONG).show();
						edtAddress.setText(province+city+country);
						postInfo.setPROVINCE(province);
						postInfo.setCITY(city);
						postInfo.setCOUNTRY(country);
					}
				},edtAddress,"广东","深圳");
			}
		});
		if(flag==0){
			title.setText("新增地址");
			deleteAddress.setVisibility(View.GONE);
			if(postInfo == null) {
				postInfo = new PostInfo();
			}
		}else{
			deleteAddress.setVisibility(View.VISIBLE);
			title.setText("修改地址");
			RECEIVE.setText(postInfo.getRECEIVE());
			PHONE.setText(postInfo.getPHONE());
			ADDRESS.setText(postInfo.getADDRESS());
			edtAddress.setText(checkText(postInfo.getPROVINCE())+checkText(postInfo.getCITY())+checkText(postInfo.getCOUNTRY()));
		}

	}
	final Runnable AddUserPostInfo = new Runnable() {
		@Override
		public void run() {
			try {
				String RECEIVE_STR = RECEIVE.getText().toString().trim();
				String PHONE_STR = PHONE.getText().toString().trim();
				String ADDRESS_STR = ADDRESS.getText().toString().trim();
				String POSTCODE_STR = "";
				if (TextUtils.isEmpty(RECEIVE_STR)) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this,type == 0 ? "收件人不能为空！":"寄件人不能为空！");
					return;
				}
				if (TextUtils.isEmpty(PHONE_STR)) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, "手机号不能为空！");
					return;
				}
				if (TextUtils.isEmpty(postInfo.getPROVINCE()) || TextUtils.isEmpty(ADDRESS_STR)) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, type == 0 ? "收件地址不能为空！" : "寄件地址不能为空！");
					return;
				}

				JSONObject param = new JSONObject();
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("RECEIVE", RECEIVE_STR);
				param.put("PHONE", PHONE_STR);
				param.put("ADDRESS", ADDRESS_STR);
				param.put("POSTCODE", POSTCODE_STR);
				param.put("PROVINCE", postInfo.getPROVINCE());
				param.put("CITY", postInfo.getCITY());
				param.put("COUNTRY", postInfo.getCOUNTRY());
				String response = HTTP.excute("addUserPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, "保存成功！");
					finish();

				} else {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageModifyAddress.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};
	
	final Runnable ModifyPostInfo = new Runnable() {
		@Override
		public void run() {
			try {
				String RECEIVE_STR = RECEIVE.getText().toString().trim();
				String PHONE_STR = PHONE.getText().toString().trim();
				String ADDRESS_STR = ADDRESS.getText().toString().trim();
				String POSTCODE_STR = "";
				if (TextUtils.isEmpty(RECEIVE_STR)) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, type == 0 ? "收件人不能为空！":"寄件人不能为空！");
					return;
				}
				if (TextUtils.isEmpty(PHONE_STR)) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, "手机号不能为空！");
					return;
				}
				if (TextUtils.isEmpty(postInfo.getPROVINCE()) || TextUtils.isEmpty(ADDRESS_STR)) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, type == 0 ? "收件地址不能为空！" : "寄件地址不能为空！");
					return;
				}

				JSONObject param = new JSONObject();
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("RECEIVE", RECEIVE_STR);
				param.put("PHONE", PHONE_STR);
				param.put("ADDRESS", ADDRESS_STR);
				param.put("POSTID", postInfo.getPOSTID());
				param.put("PROVINCE", postInfo.getPROVINCE());
				param.put("CITY", postInfo.getCITY());
				param.put("COUNTRY", postInfo.getCOUNTRY());
				String response = HTTP.excute("modifyUserPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, "保存成功！");
					finish();

				} else {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageModifyAddress.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};
	
	/**
	 * 删除速递信息
	 */
	final Runnable DeleteUserPostInfo = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("POSTID", postInfo.getPOSTID());
				String response = HTTP.excute("deleteUserPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, "删除成功！");					
					finish();
				} else {
					DialogUtil.showUIToast(ExpressageModifyAddress.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageModifyAddress.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};
	private static String checkText(String text) {
		return TextUtils.isEmpty(text) ? "" : text;
	}
}
