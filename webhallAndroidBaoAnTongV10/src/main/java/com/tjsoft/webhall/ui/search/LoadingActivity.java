package com.tjsoft.webhall.ui.search;

import org.json.JSONObject;

import com.google.zxing.CaptureActivity;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.imp.GloabDelegete;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class LoadingActivity extends AutoDialogActivity{
	private String ID;
	private String BSNUM;
	private String SXID;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "activity_loading"));
		ID=getIntent().getStringExtra("ID");
		BSNUM=getIntent().getStringExtra("BSNUM");
		SXID=getIntent().getStringExtra("SXID");
		SBzanCun();
	}
	private void SBzanCun(){
		GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
		if(gloabDelegete!=null){
			final TransportEntity transportEntity=gloabDelegete.getUserInfo();
			if(!TextUtils.isEmpty(transportEntity.getToken())){
				final Runnable SBZanCun=new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						try {
							JSONObject param=new JSONObject();
							param.put("ID", ID);
							param.put("USERNAME", transportEntity.getLoginPhone());
							param.put("PASSWORD", transportEntity.getPassword());
							param.put("SXID", SXID);
							param.put("BSNUM", BSNUM);
							String response=HTTP.excuteAndCache("mobileScanQRCode", "RestOnlineDeclareService", param.toString(), LoadingActivity.this);
							JSONObject json = new JSONObject(response);
							String code = json.getString("code");
							Log.e("sps==", "成功=="+code);

							if(code.equals("200")){
								LoadingActivity.this.finish();
							}
						} catch (Exception e) {
							// TODO: handle exception
							DialogUtil.showUIToast(LoadingActivity.this, "取消收藏失败，请稍后重试！");
						}
					}
				};
				new Thread(SBZanCun).start();
			}
			
		}
		
	}
}
