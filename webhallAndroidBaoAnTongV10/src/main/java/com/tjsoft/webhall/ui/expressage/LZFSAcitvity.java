package com.tjsoft.webhall.ui.expressage;

import java.util.List;

import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.PostInfo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
/**
 * 领证方式
 * @author S_Black
 *
 */
public class LZFSAcitvity extends AutoDialogActivity {
	private RelativeLayout back;
	private RadioGroup LZFS_RG;
	private RadioButton WDFW;
	private RadioButton ChooseAddress;
	private EditText RECEIVE, PHONE, ADDRESS,POSTCODE;
	private LinearLayout address_ll;
	private Button cancel,ok;
	private LinearLayout parent_ll;
	private List<PostInfo> postInfos, postInfos2;
	private String BSNUM = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_acitvity_lzfs"));
		
		initView();
	}
	private void initView(){
		BSNUM = getIntent().getStringExtra("BSNUM");
		back=(RelativeLayout)findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		LZFS_RG=(RadioGroup)findViewById(MSFWResource.getResourseIdByName(this, "id", "radioGroup"));
		WDFW=(RadioButton)findViewById(MSFWResource.getResourseIdByName(this, "id", "WDLQ"));	
		ChooseAddress=(RadioButton)findViewById(MSFWResource.getResourseIdByName(this, "id", "YJLQ"));
		RECEIVE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "RECEIVE"));
		PHONE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "PHONE"));
		ADDRESS = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "ADDRESS"));
		POSTCODE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "POSTCODE"));
		address_ll=(LinearLayout)findViewById(MSFWResource.getResourseIdByName(this, "id", "addAddress_ll"));
		ok = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "ok"));
		cancel = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "cancel"));
		parent_ll=(LinearLayout)findViewById(MSFWResource.getResourseIdByName(this, "id", "parent_ll"));
		
		if(Constants.user!=null){
			RECEIVE.setText(Constants.user.getREALNAME());
			PHONE.setText(Constants.user.getMOBILE());
			POSTCODE.setText("518000");
		}
		if(Constants.getPostInfo!=null){
			ChooseAddress.setChecked(true);
			RECEIVE.setText(Constants.getPostInfo.getRECEIVE());
			PHONE.setText(Constants.getPostInfo.getPHONE());
			ADDRESS.setText(Constants.getPostInfo.getADDRESS());
			POSTCODE.setText(Constants.getPostInfo.getPOSTCODE());
			address_ll.setVisibility(View.VISIBLE);
		}else{
			WDFW.setChecked(true);
			address_ll.setVisibility(View.GONE);
		}
		LZFS_RG.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==WDFW.getId()){
					address_ll.setVisibility(View.GONE);
					Constants.getPostInfo=null;
				}else if(checkedId==ChooseAddress.getId()){
					address_ll.setVisibility(View.VISIBLE);
				}
			}
		});
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Constants.getPostInfo=null;
				finish();
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Constants.getPostInfo=null;
				finish();
			}
		});
		parent_ll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0); 	
				}
		});
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(WDFW.isChecked()){
					Constants.getPostInfo=null;
					setResult(100);
					finish();
				}else if(ChooseAddress.isChecked()){
					String RECEIVE_STR = RECEIVE.getText().toString().trim();
					String PHONE_STR = PHONE.getText().toString().trim();
					String ADDRESS_STR = ADDRESS.getText().toString().trim();
					String POSTCODE_STR = POSTCODE.getText().toString().trim();
					if (TextUtils.isEmpty(RECEIVE_STR)) {
						DialogUtil.showUIToast(LZFSAcitvity.this, "收件人不能为空！");
						return;
					}
					if (TextUtils.isEmpty(PHONE_STR)) {
						DialogUtil.showUIToast(LZFSAcitvity.this, "手机号不能为空！");
						return;
					}
					if (TextUtils.isEmpty(ADDRESS_STR)) {
						DialogUtil.showUIToast(LZFSAcitvity.this, "收件地址不能为空！");
						return;
					}
					if (TextUtils.isEmpty(POSTCODE_STR)) {
						DialogUtil.showUIToast(LZFSAcitvity.this, "邮政编码不能为空！");
						return;
					}
					PostInfo postInfo=new PostInfo();
					postInfo.setRECEIVE(RECEIVE_STR);
					postInfo.setADDRESS(ADDRESS_STR);
					postInfo.setPHONE(PHONE_STR);
					postInfo.setPOSTCODE(POSTCODE_STR);
					Constants.getPostInfo=postInfo;
					setResult(100);
					finish();
				}
				
			}
		});
		
		if(!TextUtils.isEmpty(BSNUM)){
			ChooseAddress.setEnabled(false);								
			WDFW.setEnabled(false);
			dialog = Background.Process(LZFSAcitvity.this, GetBusiPostInfo2, "正在加载。。。");			
		}
	}

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
								ChooseAddress.setChecked(true);
								address_ll.setVisibility(View.VISIBLE);
								RECEIVE.setText(postInfos2.get(0).getRECEIVE());
								RECEIVE.setFocusable(false);
								RECEIVE.setFocusableInTouchMode(false);
								PHONE.setText(postInfos2.get(0).getPHONE());
								PHONE.setFocusable(false);
								PHONE.setFocusableInTouchMode(false);
								ADDRESS.setText(postInfos2.get(0).getADDRESS());
								ADDRESS.setFocusable(false);
								ADDRESS.setFocusableInTouchMode(false);
								POSTCODE.setText(postInfos2.get(0).getPOSTCODE());
								POSTCODE.setFocusable(false);
								POSTCODE.setFocusableInTouchMode(false);
							}
						});
					} else {
						runOnUiThread(new Runnable() {
							public void run() {
								address_ll.setVisibility(View.GONE);
								
								WDFW.setChecked(true);
							}
						});
						
						
					}

				}
			} catch (Exception e) {
				DialogUtil.showUIToast(LZFSAcitvity.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};
}
