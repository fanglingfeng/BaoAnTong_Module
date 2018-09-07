package com.tjsoft.util;

import org.json.JSONObject;

import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Guide;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.user.Login;
import com.tjsoft.webhall.ui.work.HistoreShareNoPre;
import com.tjsoft.webhall.ui.wsbs.WSBSWeb;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

public class NextAcitivityUtils {
	private static Permission permission;
	private static Guide guide;
	private static SharedPreferences sp;//记录版本号的
	private static ProgressDialog dialog;
	/**
	 * 
	 * @param flag 跳转的界面标志  1   申报表单页   2  我要预约界面  3 在线咨询 4 我的办件  5 我的收藏  6  我的预约
	 * @param PERMID 事项ID
	 * @param mContext
	 */
	public static void skipWSBS(final int flag,  final String PERMID,final Context mContext){
		
		
		/**
		 * 获取办事指南
		 */
		final Runnable GetGuide = new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject param = new JSONObject();
					param.put("PERMID", PERMID);
					String response = HTTP.excuteAndCache("getPermissionByPermid", "RestPermissionitemService", param.toString(),mContext);
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						guide = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), Guide.class);
						Activity activity=(Activity)mContext;
						activity.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub	
								try {
									permission=guide.getPERMISSION();
									if(flag==1&&!TextUtils.isEmpty(permission.getWSSBDZ())){
										Intent intent=new Intent();
										intent.putExtra("url", permission.getWSSBDZ());
										intent.setClass(mContext, WSBSWeb.class);
										mContext.startActivity(intent);
									}else{
										nextActivity(flag,permission,mContext);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								
							}
						});
					} else {
						DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
					}

				} catch (Exception e) {
//					DialogUtil.showUIToast(PermGuide.this.getActivity(), getString(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
		new Thread(GetGuide).start();;
//		dialog=Background.Process(mContext, GetGuide, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_loding")));
	}
	
	
	/**
	 * 判断实名信息是否完全
	 * 
	 * @param t
	 * @return
	 */
	private static boolean checkRealNameInfo(TransportEntity t) {
		if (TextUtils.isEmpty(t.getToken())) {
			return false;
		}
		return true;
	}
	/**
	 * flag 跳转的界面标志  1   申报页   2  我要预约界面  3 在线咨询 4 我的办件  5 我的收藏  6  我的预约
	 */
	public static void skipBSDT(int flag,final Context mContext){
		sp=mContext.getSharedPreferences("Version", Context.MODE_PRIVATE);
		if (!Constants.DEBUG_TOGGLE) {
			/**
			 * 判断是否登录MST
			 */
			GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
			Log.e("Login", "gloabDelegete===" + gloabDelegete);
			if (gloabDelegete != null) {
				TransportEntity transportEntity = gloabDelegete.getUserInfo();
				if (checkRealNameInfo(transportEntity)) {// 信息完整
					// 登录自己的接口
					LoginUtil loginUtil = new LoginUtil(mContext, sp);
					loginUtil.Login(flag, transportEntity);
					// Constants.user.setUSER_ID(transportEntity.getUserId());
				} else {
					new AlertDialog.Builder(mContext).setMessage("你还没有登录，是否现在登录")
							.setTitle(mContext.getString(MSFWResource
									.getResourseIdByName(mContext, "string", "tj_notify")))
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (gloabDelegete != null) {
								gloabDelegete.doActivity(mContext, 1, null);
							}
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}

					}).show();
					return;
				}
			} else {
				DialogUtil.showUIToast(mContext, "gloabDelegete is null");
				return;
			}
		} else {
			if (null == Constants.user) {
				Intent intent = new Intent();
				intent.setClass(mContext, Login.class);
				mContext.startActivity(intent);
				return;
			} else {
//				Intent intent = new Intent();
//				intent.setClass(mContext, HistoreShareNoPre.class);
//				intent.putExtra("permission", permission);
//				mContext.startActivity(intent);
			}
		}
	}
	
	
	private static void nextActivity(int flag,Permission permission,final Context mContext){
		// TODO Auto-generated method stub
		sp=mContext.getSharedPreferences("Version", Context.MODE_PRIVATE);
		if (!Constants.DEBUG_TOGGLE) {
			/**
			 * 判断是否登录MST
			 */
			GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
			Log.e("Login", "gloabDelegete===" + gloabDelegete);
			if (gloabDelegete != null) {
				TransportEntity transportEntity = gloabDelegete.getUserInfo();
				if (checkRealNameInfo(transportEntity)) {// 信息完整
					// 登录自己的接口
					LoginUtil loginUtil = new LoginUtil(mContext, sp, permission);
					loginUtil.Login(flag, transportEntity);
					// Constants.user.setUSER_ID(transportEntity.getUserId());
				} else {
					new AlertDialog.Builder(mContext).setMessage("你还没有登录，是否现在登录")
							.setTitle(mContext.getString(MSFWResource
									.getResourseIdByName(mContext, "string", "tj_notify")))
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (gloabDelegete != null) {
								gloabDelegete.doActivity(mContext, 1, null);
							}
						}
					}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}

					}).show();
					return;
				}
			} else {
				DialogUtil.showUIToast(mContext, "gloabDelegete is null");
				return;
			}
		} else {
			if (null == Constants.user) {
				Intent intent = new Intent();
				intent.setClass(mContext, Login.class);
				mContext.startActivity(intent);
				return;
			} else {
				Intent intent = new Intent();
				intent.setClass(mContext, HistoreShareNoPre.class);
				intent.putExtra("permission", permission);
				mContext.startActivity(intent);
			}
		}
	}
}
