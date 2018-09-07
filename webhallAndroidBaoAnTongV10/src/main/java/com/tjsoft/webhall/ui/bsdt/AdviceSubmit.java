package com.tjsoft.webhall.ui.bsdt;



import org.json.JSONObject;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Advice;
import com.tjsoft.webhall.entity.Permission;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
/**
 * 我要评议页面
 * @author Administrator
 *
 */
public class AdviceSubmit extends AutoDialogActivity {
	private EditText remark;
	private RadioGroup radioGroup;
	private Button submit, back;
	private int RESULT = 0;
	private String BSNUM ="";
	private TextView pName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_advice_submit"));
		pName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "pName"));
		Advice advice = (Advice) getIntent().getSerializableExtra("advice");
		Permission permission = (Permission) getIntent().getSerializableExtra("permission");
		if(null == advice){
			BSNUM = getIntent().getStringExtra("BSNUM");	
			pName.setText(null==permission?"":permission.getSXZXNAME());
		}
		else{
			BSNUM = advice.getBSNUM();
			pName.setText(advice.getPNAME());
		}
		InitView();
		dialog = Background.Process(this, GetAdvice, "正在加载...");
	}

	
	private void InitView() {
		remark = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "remark"));
		radioGroup = (RadioGroup) findViewById(MSFWResource.getResourseIdByName(this, "id", "radioGroup"));
		radioGroup.setOnCheckedChangeListener(new MyChangeListener());

		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(AdviceSubmit.this, Submit, "正在提交");
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AdviceSubmit.this.finish();
			}
		});
	}

	final Runnable GetAdvice = new Runnable() {
		@Override
		public void run() {

				try {
					if(null == BSNUM){
						return ;
					}
					JSONObject param = new JSONObject();
					param.put("token", Constants.user.getTOKEN());
					param.put("BSNUM", BSNUM);
					String response = HTTP.excute("search", "RestAdviceService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						final Advice advice = JSONUtil.getGson().fromJson(json.getString("ReturnValue"),Advice.class);
						if(null != advice && null != advice.getBSNUM()){
							runOnUiThread(new  Runnable() {
								public void run() {
									remark.setText(advice.getREMARK());
									submit.setVisibility(View.GONE);
									
									if(advice.getRESULT().equals("0")){
										radioGroup.check(MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw1"));
									}
									if(advice.getRESULT().equals("1")){
										radioGroup.check(MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw2"));
									}
									if(advice.getRESULT().equals("2")){
										radioGroup.check(MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw3"));
									}
									if(advice.getRESULT().equals("3")){
										radioGroup.check(MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw4"));
									}
									if(advice.getRESULT().equals("4")){
										radioGroup.check(MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw5"));
										
									}

									
								}
							});
						}
					} else {
						String error = json.getString("error");
						if (null != error) {
							DialogUtil.showUIToast(AdviceSubmit.this, error);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} 
		};
	
	final Runnable Submit = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("CREATERID", Constants.user.getUSER_ID());
				param.put("BSNUM", BSNUM);
				param.put("RESULT", RESULT + "");
				param.put("REMARK", remark.getText().toString().trim());

				String response = HTTP.excute("submit", "RestAdviceService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(AdviceSubmit.this, "评价成功！");
					finish();
				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(AdviceSubmit.this, error);
					}
				}
			} catch (Exception e) {
				DialogUtil.showUIToast(AdviceSubmit.this, getString(MSFWResource.getResourseIdByName(AdviceSubmit.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class MyChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int ataaw1 = MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw1");
			int ataaw2 = MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw2");
			int ataaw3 = MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw3");
			int ataaw4 = MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw4");
			int ataaw5 = MSFWResource.getResourseIdByName(AdviceSubmit.this, "id", "ataaw5");
			if (checkedId == ataaw1) {
				RESULT = 0;
			} else if (checkedId == ataaw2) {
				RESULT = 1;
			} else if (checkedId == ataaw3) {
				RESULT = 2;
			} else if (checkedId == ataaw4) {
				RESULT = 3;
			} else if (checkedId == ataaw5) {
				RESULT = 4;
			}
//			switch (checkedId) {
//			case R.id.ataaw1:
//				RESULT = 0;
//				break;
//			case R.id.ataaw2:
//				RESULT = 1;
//				break;
//			case R.id.ataaw3:
//				RESULT = 2;
//				break;
//			case R.id.ataaw4:
//				RESULT = 3;
//				break;
//			case R.id.ataaw5:
//				RESULT = 4;
//				break;
//
//			default:
//				break;
//			}
		}

	}

}
