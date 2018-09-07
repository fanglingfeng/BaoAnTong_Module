package com.tjsoft.webhall.ui.user;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.AddInfoCallBack;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;

public class LoginUtil {
	private Activity activity;
	private String BtnTxt = Constants.ADD_INFO;;
	private String username = "";
	private String password = "";
	private Intent intent;
	private String REALNAME = "";
	private String CERTIFICATETYPE = "";
	private String USER_PID = "";
	private String USER_MOBILE = "";

	public void checkLogin(final Activity activity) {
		if (!Constants.DEBUG_TOGGLE) {
			/**
			 * 判断是否登录MST
			 */
			this.activity = activity;
			GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
			if (gloabDelegete != null) {
				TransportEntity transportEntity = gloabDelegete.getUserInfo();
				if (checkRealNameInfo(transportEntity)) {// 信息完整
					BtnTxt = Constants.AUTO_LOGIN;
					Constants.user=new User();
					Constants.user.setUSER_ID(transportEntity.getUserId());
					Constants.user.setUSERNAME(transportEntity.getName());
					Constants.user.setREALNAME(transportEntity.getName());
					Constants.user.setMOBILE(transportEntity.getLoginPhone());
					Constants.user.setTYPE(transportEntity.getIdcardType());// 证件类型
					Constants.user.setCODE(transportEntity.getIdcardNum());// 证件号码
					// Constants.user.setUSER_ID(transportEntity.getUserId());
				}else {
					new AlertDialog.Builder(activity).setMessage("你还没有登录，是否现在登录")
					.setTitle(activity.getString(MSFWResource.getResourseIdByName(activity, "string", "tj_notify")))
					.setPositiveButton("确定", new OnClickListener() {
						GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							if (gloabDelegete != null) {
								gloabDelegete.doActivity(activity, 1, new TransportCallBack() {

									@Override
									public void onStart() {
										// TODO Auto-generated method stub

									}

									@Override
									public void onResult(int status) {
										// TODO Auto-generated method stub
										Log.e("result====", "status==" + status);
										if(status==1){
											TransportEntity transportEntity = gloabDelegete.getUserInfo();
											if (checkRealNameInfo(transportEntity)) {// 信息完整
												BtnTxt = Constants.AUTO_LOGIN;
												Constants.user=new User();
												Constants.user.setUSER_ID(transportEntity.getUserId());
												Constants.user.setUSERNAME(transportEntity.getName());
												Constants.user.setREALNAME(transportEntity.getName());
												Constants.user.setMOBILE(transportEntity.getLoginPhone());
												Constants.user.setTYPE(transportEntity.getIdcardType());// 证件类型
												Constants.user.setCODE(transportEntity.getIdcardNum());// 证件号码
												// Constants.user.setUSER_ID(transportEntity.getUserId());
											}
										}
									}

									@Override
									public void onFinish() {
										// TODO Auto-generated method stub

									}
								});
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
			
			}
		}else {
			if (null == Constants.user) {
				intent = new Intent();
				intent.setClass(activity, Login.class);
				activity.startActivity(intent);
				return;
			}
		}
	}

//	private void isLoginAlert() {
//		new AlertDialog.Builder(activity).setMessage("你还没有绑定网上办事账号，是否现在绑定？").setTitle(activity.getString(MSFWResource.getResourseIdByName(activity, "string", "tj_notify"))).setCancelable(true).setPositiveButton("手动绑定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//				if (gloabDelegete != null) {
//					gloabDelegete.doBoundMSTAccount(activity, username, password, new TransportCallBack() {
//
//						@Override
//						public void onStart() {
//
//						}
//
//						@Override
//						public void onResult(int status) {
//							if (status == 0) {
//								DialogUtil.showUIToast(activity, "绑定成功");
//								Background.Process(activity, Login, "正在登录...");
//								activity.finish();
//							} else {
//								DialogUtil.showUIToast(activity, "绑定失败");
//
//							}
//						}
//
//						@Override
//						public void onFinish() {
//
//						}
//					});
//				}
//			}
//		}).setNegativeButton(BtnTxt, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//
//				if (BtnTxt.equals(Constants.AUTO_LOGIN)) {//自动绑定
//					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//					if (gloabDelegete != null) {
//
//						TransportEntity transportEntity = gloabDelegete.get();
//						username = transportEntity.getBatUserName();
//						REALNAME = transportEntity.getRealName();
//						CERTIFICATETYPE = transportEntity.getIdCardType() + "";
//						USER_PID = transportEntity.getIdCardNo();
//						USER_MOBILE = transportEntity.getMobile();
//						password = USER_PID.substring(USER_PID.length() - 6, USER_PID.length());
//						isAutoRegisterAlert();
//					}
//
//				} else if (BtnTxt.equals(Constants.ADD_INFO)) {//完善个人信息
//
//					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//					gloabDelegete.addInfo(activity, new AddInfoCallBack() {
//
//						@Override
//						public void success(TransportEntity t) {
//							username = t.getBatUserName();
//							REALNAME = t.getRealName();
//							CERTIFICATETYPE = t.getIdCardType() + "";
//							USER_PID = t.getIdCardNo();
//							USER_MOBILE = t.getMobile();
//							password = USER_PID.substring(USER_PID.length() - 6, USER_PID.length());
//							isAutoRegisterAlert();
//
//						}
//					});
//					return;
//				}
//			}
//		}).show();
//	}

	final Runnable Login = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("USERNAME", username);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(password));
				String response = HTTP.excute("login", "RestUserService", param.toString());
				final JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
				} else {
					DialogUtil.showUIToast(activity, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(activity, activity.getString(MSFWResource.getResourseIdByName(activity, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	/**
	 * 是否自动注册
	 */
	private void isAutoRegisterAlert() {
		new AlertDialog.Builder(activity).setMessage("是否自动注册网上办事账号，账号为宝安通账号。密码默认为身份证后6位？").setTitle(activity.getString(MSFWResource.getResourseIdByName(activity, "string", "tj_notify"))).setCancelable(true).setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Background.Process(activity, Register, "正在登录...");
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		}).show();
	}

	final Runnable Register = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();

				param.put("USERNAME", username);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(password));
				param.put("REALNAME", REALNAME);
				param.put("CERTIFICATETYPE", CERTIFICATETYPE);
				param.put("USER_PID", USER_PID);
				param.put("USER_MOBILE", USER_MOBILE);

				String response = HTTP.excute("registerUser", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(activity, "注册成功！");
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//					if (gloabDelegete != null) {
//						gloabDelegete.doBoundMSTAccount(activity, username, password, new TransportCallBack() {
//
//							@Override
//							public void onStart() {
//							}
//
//							@Override
//							public void onResult(int status) {// 0成功
//								if (status == 0) {
//									new Thread(Login).start();
//								} else {
//									DialogUtil.showUIToast(activity, "绑定宝安通失败！");
//								}
//							}
//
//							@Override
//							public void onFinish() {
//							}
//						});
//					}
//					activity.finish();

				} else {
					Intent intent = new Intent();
					intent.setClass(activity, com.tjsoft.webhall.ui.user.Register.class);
					activity.startActivity(intent);
					String error = json.getString("error");
					DialogUtil.showUIToast(activity, error + ",请手动注册网上办事大厅账号!");
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(activity, activity.getString(MSFWResource.getResourseIdByName(activity, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	/**
	 * 判断实名信息是否完全
	 * 
	 * @param t
	 * @return
	 */
	private boolean checkRealNameInfo(TransportEntity t) {
		if(TextUtils.isEmpty(t.getToken())){
			return false;
		}
		return true;
	}

}
