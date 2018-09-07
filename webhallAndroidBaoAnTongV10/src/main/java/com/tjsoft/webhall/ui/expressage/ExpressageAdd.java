package com.tjsoft.webhall.ui.expressage;

import org.json.JSONObject;

import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;

/**
 * 
 * com.tjsoft.webhall.ui.expressage.ExpressageAdd
 * 
 * @author 傅成龙 <br/>
 *         create at 2016-6-3 下午5:09:49 添加速递信息
 */
public class ExpressageAdd extends AutoDialogActivity{
	private EditText RECEIVE, PHONE, ADDRESS, POSTCODE;
	private Button save;
	private RelativeLayout back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "expressage_add"));
		initView();

	}

	/**
	 * 
	 */
	private void initView() {
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
		POSTCODE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "POSTCODE"));

		save = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "save"));
		save.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(ExpressageAdd.this, AddUserPostInfo, "正在保存");
				
			}
		});

	}

	final Runnable AddUserPostInfo = new Runnable() {
		@Override
		public void run() {
			try {
				String RECEIVE_STR = RECEIVE.getText().toString().trim();
				String PHONE_STR = PHONE.getText().toString().trim();
				String ADDRESS_STR = ADDRESS.getText().toString().trim();
				String POSTCODE_STR = POSTCODE.getText().toString().trim();
				if (TextUtils.isEmpty(RECEIVE_STR)) {
					DialogUtil.showUIToast(ExpressageAdd.this, "收件人不能为空！");
					return;
				}
				if (TextUtils.isEmpty(PHONE_STR)) {
					DialogUtil.showUIToast(ExpressageAdd.this, "手机号不能为空！");
					return;
				}
				if (TextUtils.isEmpty(ADDRESS_STR)) {
					DialogUtil.showUIToast(ExpressageAdd.this, "收件地址不能为空！");
					return;
				}
				if (TextUtils.isEmpty(POSTCODE_STR)) {
					DialogUtil.showUIToast(ExpressageAdd.this, "邮政编码不能为空！");
					return;
				}

				JSONObject param = new JSONObject();
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("RECEIVE", RECEIVE_STR);
				param.put("PHONE", PHONE_STR);
				param.put("ADDRESS", ADDRESS_STR);
				param.put("POSTCODE", POSTCODE_STR);
				String response = HTTP.excute("addUserPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ExpressageAdd.this, "保存成功！");
					finish();

				} else {
					DialogUtil.showUIToast(ExpressageAdd.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageAdd.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};



}
