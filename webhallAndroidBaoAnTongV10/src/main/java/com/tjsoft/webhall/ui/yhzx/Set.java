package com.tjsoft.webhall.ui.yhzx;

import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.UpdateManager;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.set.About;
import com.tjsoft.webhall.set.Introductions;
import com.tjsoft.webhall.ui.user.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
/**
 * 设置页面
 * @author Administrator
 *
 */
public class Set extends AutoDialogActivity {
	private Button back;
	private RelativeLayout set1,set2,set3,set4;
	private ImageButton set5;
	private Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_set"));
		Constants.getInstance().addActivity(this);
		set1 = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "set1"));
		set2 = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "set2"));
		set3 = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "set3"));
		set4 = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "set4"));
		set5 = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "set5"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		initSetOnListener();
		
		SharedPreferences config = this.getSharedPreferences("config", MODE_PRIVATE);
		boolean dtzgn = config.getBoolean("dtzgn", false);
		if(dtzgn){
			set5.setBackgroundResource(MSFWResource.getResourseIdByName(Set.this, "drawable", "tj_open"));
		}
		else{
			set5.setBackgroundResource(MSFWResource.getResourseIdByName(Set.this, "drawable", "tj_close"));
		}
	}

	private void initSetOnListener() {
		set1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(Set.this, Introductions.class);
				startActivity(intent);
			}
		});		
		set2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(Set.this, About.class);
				startActivity(intent);
			}
		});		
		set3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				UpdateManager mUpdateManager = new UpdateManager(Set.this,false);
		        mUpdateManager.checkUpdateInfo();
			}
		});		
		set4.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Constants.user = null;
				DialogUtil.showUIToast(Set.this, "您已退出登陆！");
				FileUtil.Write(Set.this, "password", "");
				LoginBaoAnTongUtil.checkLogin(Set.this);
//				/**
//				 * 判断是否登录MST
//				 */
//				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//				if (gloabDelegete != null) {
//					TransportEntity transportEntity = gloabDelegete.get();
//					if (!transportEntity.isLoginStatus()) {
//						gloabDelegete.loginMST(Set.this);
//						return;
//					}
//					intent = new Intent();
//					intent.setClass(Set.this, Login.class);
//					startActivity(intent);
//					Set.this.finish();
//				}
			}
		});		
		set5.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SharedPreferences config = Set.this.getSharedPreferences("config", MODE_PRIVATE);
				boolean dtzgn = config.getBoolean("dtzgn", false);
				Editor editor = config.edit();
				if(dtzgn){
					dtzgn = false;
					editor.putBoolean("dtzgn", false);
					editor.commit();
					set5.setBackgroundResource(MSFWResource.getResourseIdByName(Set.this, "drawable", "tj_close"));
				}else{
					dtzgn = true;
					editor.putBoolean("dtzgn", true);
					editor.commit();
					set5.setBackgroundResource(MSFWResource.getResourseIdByName(Set.this, "drawable", "tj_open"));
				}
			}
		});		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Set.this.finish();
			}
		});		
	}

}
