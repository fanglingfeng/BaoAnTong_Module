package com.tjsoft.webhall.ui.user;


import org.json.JSONObject;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.imp.GloabDelegete;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
/**
 * 修改密码页面
 * @author Administrator
 *
 */
public class ChangePassword extends AutoDialogActivity {
	private EditText OLDPSW,NEWPSW1,NEWPSW2;
	private Button submit,back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_change_password"));
		
		OLDPSW = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "OLDPSW"));
		NEWPSW1 = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "NEWPSW1"));
		NEWPSW2 = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "NEWPSW2"));
		
		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(ChangePassword.this, Change, "正在提交...");				
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChangePassword.this.finish();				
			}
		});
		
	}
	
	final Runnable Change = new Runnable() {
		@Override
		public void run() {
			try {
				String OLDPSW_TXT = OLDPSW.getText().toString().trim();
				String NEWPSW1_TXT = NEWPSW1.getText().toString().trim();
				String NEWPSW2_TXT = NEWPSW2.getText().toString().trim();
				
				if(null == OLDPSW_TXT ||OLDPSW_TXT.equals("")){
					DialogUtil.showUIToast(ChangePassword.this, "请输入原始密码！");
					return;
				}
				if(!OLDPSW_TXT.equals(FileUtil.Load(ChangePassword.this, "password"))){
					DialogUtil.showUIToast(ChangePassword.this, "原始密码错误，请重新输入！");
					return;
				}
				if(null == NEWPSW1_TXT ||NEWPSW1_TXT.equals("")){
					DialogUtil.showUIToast(ChangePassword.this, "请输入新密码");
					return;
				}
				if(NEWPSW1_TXT.length()<3||NEWPSW1_TXT.length()>16){
					DialogUtil.showUIToast(ChangePassword.this, "密码格式不正确，请输入3-16位有效密码！");
					return;
				}
				if(null == NEWPSW2_TXT ||NEWPSW2_TXT.equals("")){
					DialogUtil.showUIToast(ChangePassword.this, "请确认新密码");
					return;
				}
				if(!NEWPSW1_TXT.equals(NEWPSW2_TXT)){
					DialogUtil.showUIToast(ChangePassword.this, "两次输入密码不一致！");
					return;
				}
				
				
				
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("ID", Constants.user.getUSER_ID());
				param.put("OLDPSW",  Md5PwdEncoder.encodePassword(OLDPSW_TXT));
				param.put("NEWPSW", Md5PwdEncoder.encodePassword(NEWPSW1_TXT));
				String response = HTTP.excute("modifypassword", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if(code.equals("200")){
					DialogUtil.showUIToast(ChangePassword.this, "修改成功！");
					FileUtil.Write(ChangePassword.this, "password", NEWPSW1_TXT);
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						gloabDelegete.modifyPWD(Constants.user.getUSERNAME(), NEWPSW1_TXT);
					}
					ChangePassword.this.finish();
				}
				else{
					String error = json.getString("error");
					DialogUtil.showUIToast(ChangePassword.this, error);
				}

				
			} catch (Exception e) {
				DialogUtil.showUIToast(ChangePassword.this, getString(MSFWResource.getResourseIdByName(ChangePassword.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
				
			}
		}
	};
	
}
