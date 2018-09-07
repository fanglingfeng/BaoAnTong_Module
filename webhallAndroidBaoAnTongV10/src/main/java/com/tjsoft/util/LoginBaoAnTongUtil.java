package com.tjsoft.util;

import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;
import com.tjsoft.webhall.ui.user.Login;
import com.tjsoft.webhall.ui.work.PermGuideContainer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.text.TextUtils;
import android.util.Log;

public class LoginBaoAnTongUtil {
	public static void checkLogin(final Activity activity){
		if (!Constants.DEBUG_TOGGLE) {
			/**
			 * 判断是否登录MST
			 */
			GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
			Log.e("Login", "gloabDelegete==="+gloabDelegete);
			if (gloabDelegete != null) {
				TransportEntity transportEntity = gloabDelegete.getUserInfo();
				if (checkRealNameInfo(transportEntity)) {// 信息完整
					// Constants.user.setUSER_ID(transportEntity.getUserId());
				}else {
					new AlertDialog.Builder(activity).setMessage("你还没有登录，是否现在登录")
					.setTitle(activity.getString(MSFWResource.getResourseIdByName(activity, "string", "tj_notify")))
					.setPositiveButton("确定", new OnClickListener() {
						GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
						@Override
						public void onClick(DialogInterface dialog, int which) {
//							DialogUtil.showUIToast(activity, "gloabDelegete=if.out==="+gloabDelegete);
							// TODO Auto-generated method stub
							if (gloabDelegete != null) {
//								DialogUtil.showUIToast(activity, "gloabDelegete==if.in====="+gloabDelegete);
								gloabDelegete.doActivity(activity, 1, null);
							}else{
								DialogUtil.showUIToast(activity, "gloabDelegete is null");
							}
						}
					}).setNegativeButton("取消", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}).show();
					return;
				}
				// if (!transportEntity.isLoginStatus()) {// 是否登录民生通，没有的话去登录
				// gloabDelegete.loginMST(activity);
				// return;
				// } else if (TextUtils.isEmpty(transportEntity.getAccount()))
				// {// 没有绑定网上办事大厅账号提示提示去绑定
				// isLoginAlert();
				// return;
				// } else if (TextUtils.isEmpty(transportEntity.getPassword()))
				// {// 没有网上办事大厅密码提示去登录
				// intent = new Intent();
				// intent.setClass(activity, Login.class);
				// intent.putExtra("account", transportEntity.getAccount());
				// activity.startActivity(intent);
				// return;
				// } else {
				// if (null == Constants.user ||
				// !Constants.user.getUSERNAME().equals(transportEntity.getAccount()))
				// {
				// username = transportEntity.getAccount();
				// password = transportEntity.getPassword();
				// Background.Process(activity, Login, "正在登录...");
				// return;
				// }
				// }
				// }
			} else {
				DialogUtil.showUIToast(activity, "gloabDelegete is null");
				return;
			}
		}else {
			if (null == Constants.user) {
				Intent intent = new Intent();
				intent.setClass(activity, Login.class);
				activity.startActivity(intent);
				return;
			}
		}
	}
	
	/**
	 * 判断实名信息是否完全
	 * 
	 * @param t
	 * @return
	 */
	private static boolean checkRealNameInfo(TransportEntity t) {
		if(TextUtils.isEmpty(t.getToken())){
			Log.d("login", "false");
			return false;
		}
		return true;
	}
}
