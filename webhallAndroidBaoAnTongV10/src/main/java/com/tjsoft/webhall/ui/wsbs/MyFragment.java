package com.tjsoft.webhall.ui.wsbs;

import java.util.Date;

import org.json.JSONObject;

import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.PermListByDB;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.search.Search;
import com.tjsoft.webhall.ui.user.Login;
import com.tjsoft.webhall.ui.xkzkj.XKZKJActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

public class MyFragment extends Fragment{
	private RelativeLayout back;
	private RelativeLayout JDCC;
	private RelativeLayout WDFW;
	private RelativeLayout WDSB;
	private RelativeLayout WDYY;
	private RelativeLayout WDSC;
	private RelativeLayout ZXZX;
	private RelativeLayout XKZKJX;

	private SharedPreferences sp;
	private int version;
	private Button home;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view =inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "activity_main_more"), container,false);
		sp=getActivity().getSharedPreferences("Version", Context.MODE_PRIVATE);
		version=sp.getInt("version", 0);
		initView(view);
		return view;
	
	}

	private void initView(View view){
		home = (Button) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "home"));
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.getInstance().exit();
			}
		});
		back = (RelativeLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().finish();
			}
		});
		JDCC=(RelativeLayout)view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "more_secher_sechedule"));
		JDCC.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent().setClass(getActivity(), Search.class));
			}
		});
		WDFW=(RelativeLayout)view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "more_network"));
		WDFW.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent().setClass(getActivity(), WSBS.class));
			}
		});
		WDSB=(RelativeLayout)view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "more_wdsb_rl"));
		WDSB.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						// DialogUtil.showUIToast(getActivity(),
						// transportEntity.getToken());
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(getActivity());
						} else {
							Login(2, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(getActivity(), "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(getActivity(), WDBJ.class));
					}
				}
			}
		});
		WDYY=(RelativeLayout)view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "more_wdyy_rl"));
		WDYY.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(getActivity());
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(getActivity());
						} else {
							Login(3, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(getActivity(), "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(getActivity(), ReserveList.class));
					}
				}
			}
		});
		WDSC=(RelativeLayout)view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "more_wdsc_rl"));
		WDSC.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Constants.WSBS_PATH=0;
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(getActivity());
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(getActivity());
						} else {
							Login(5, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(getActivity(), "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(getActivity(), PermListByDB.class));
					}
				}
			}
		});
		ZXZX=(RelativeLayout)view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "more_zxzx_rl"));
		ZXZX.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(getActivity());
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(getActivity());
						} else {
							Login(4, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(getActivity(), "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(getActivity(), Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(getActivity(), ConsultList.class));
					}
				}
			}
		});
		XKZKJX=(RelativeLayout)view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "rl_xkzkj"));
		XKZKJX.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


				startActivity(new Intent().setClass(getActivity(), XKZKJActivity.class));


			}
		});

	}

	
	/**
	 * 判断实名信息是否完全
	 * 
	 * @param t
	 * @return
	 */
	private boolean checkRealNameInfo(TransportEntity t) {
		if(!TextUtils.isEmpty(t.getToken())){
			return false;
		}
		return true;
	}
	/**
	 * 万能密码登录
	 * @param flag 1 我要咨询  2 我的申报  3 我的预约 4在线咨询 5 我的收藏
//	 * @param username
	 */
	private void Login(final int flag,final TransportEntity transportEntity){
		final Runnable LoginNotPassW = new Runnable() {
			@Override
			public void run() {
				try {

					JSONObject param = new JSONObject();
					if(!TextUtils.isEmpty(transportEntity.getName())){
						param.put("USERNAME", transportEntity.getName());
					}else{
						showDialog();
						return;
					}
					param.put("PASSWORD", "C4BD1B3942F3C2ACD7657CBD0B5D952F");
					String response = HTTP.excute("login", "RestUserService", param.toString());
					final JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
//						new Thread(GetWDBJ).start();
						int ver=transportEntity.getVersion();
						Log.e("sps====", "华讯的version===="+ver+"自己的版本==="+version);
						if(version<ver){
							updateUserInfo(transportEntity);
						
						}
						switch (flag) {
						case 1:
							startActivity(new Intent().setClass(getActivity(), AddConsult.class));
							break;
						case 2:
							startActivity(new Intent().setClass(getActivity(), WDBJ.class));
							break;
						case 3:
							startActivity(new Intent().setClass(getActivity(), ReserveList.class));
							break;
						case 4:
							startActivity(new Intent().setClass(getActivity(), AddConsult.class));
							break;
						case 5:
							startActivity(new Intent().setClass(getActivity(), PermListByDB.class));
							break;
						default:
							break;
						}
					} else if(code.equals("600")){	
						if(flag!=0){
							Log.e("WSBSMain", "是否企业用户==="+transportEntity.getEnterpriseStatus());
							if(transportEntity.getEnterpriseStatus().equals("3")){							
								QY_Register(flag, transportEntity);							
							}else {
								GR_Register(flag, transportEntity);
							}	
						}						
					}else{
						String error = json.getString("error");
						DialogUtil.showUIToast(getActivity(),error);					}

				} catch (Exception e) {
//					DialogUtil.showUIToast(getActivity(), getActivity().getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
//		if(flag==0){
			new Thread(LoginNotPassW).start();	
//		}else {
//			dialog=Background.Process(getActivity(), LoginNotPassW,getActivity().getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_loding")));	
//		}
		
	}
	/**
	 * 万能密码登录--用户名不存在---个人注册
	 * @param flag 跳转标志
	 * @param transportEntity  登录用户信息实体
	 */
	private void GR_Register(final int flag,final TransportEntity transportEntity) {
		
		final Runnable Submit1 = new Runnable() {
			
			@Override
			public void run() {
				try {
					Log.d("PermGuide", "身份类型==="+transportEntity.getIdcardType());	
					String TypeId="";			
					JSONObject param = new JSONObject();
					param.put("USERNAME", transportEntity.getName());
					param.put("PASSWORD", transportEntity.getPassword());
					if((!TextUtils.isEmpty(transportEntity.getIdcardType())&&!transportEntity.getIdcardType().equals("null"))&&
							(!TextUtils.isEmpty(transportEntity.getRealName())&&!transportEntity.getRealName().equals("null")&&
							(!TextUtils.isEmpty(transportEntity.getIdcardNum())&&!transportEntity.getIdcardNum().equals("null"))&&
							(!TextUtils.isEmpty(transportEntity.getLoginPhone())&&!transportEntity.getLoginPhone().equals("null"))
							)){
							if(transportEntity.getIdcardType().equals("1")){
								TypeId="10";
							}else if(transportEntity.getIdcardType().equals("2")){
								TypeId="20";
							}else if(transportEntity.getIdcardType().equals("3")){
								TypeId="14";
							}else if(transportEntity.getIdcardType().equals("4")){
								TypeId="15";
							}
							param.put("USER_PID", transportEntity.getIdcardNum());	
							param.put("USER_MOBILE", transportEntity.getLoginPhone());	
							param.put("REALNAME", transportEntity.getRealName());	
							param.put("CERTIFICATETYPE", TypeId);	
							Log.d("PermGuide", "转换后身份类型==="+TypeId);
					}else{
						showDialog();
						return;
					}
					param.put("EMAIL", transportEntity.getEMAIL());
					param.put("REGISTER_TIME", new Date());
					param.put("USER_GENDER", transportEntity.getSex());
					param.put("USER_SOURCE", "2");

					String response = HTTP.excute("registerUser", "RestUserService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						Login(flag, transportEntity);					
					} else {
						String error = json.getString("error");
						Log.d("WSBSMAIN", error);
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						// DialogUtil.showUIToast(Register.getActivity(), error);
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(getActivity(), getString(MSFWResource
							.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
		
		new Thread(Submit1).start();
	}
	/**
	 * 万能密码登录--用户名不存在---企业注册
	 * @param flag 跳转标志
	 * @param transportEntity  登录用户信息实体
	 */	
	private void QY_Register(final int flag,final TransportEntity transportEntity){
		final Runnable Submit2 = new Runnable() {
			@Override
			public void run() {
				try {
					
					JSONObject param = new JSONObject();
					param.put("USER_SOURCE", "2");
					if(!TextUtils.isEmpty(transportEntity.getName())&&!transportEntity.getName().equals("null")){
						param.put("USERNAME", transportEntity.getName());	
					}else{
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
						
					}
					if(!TextUtils.isEmpty(transportEntity.getPassword())&&!transportEntity.getPassword().equals("null")){
						param.put("PASSWORD", transportEntity.getPassword());	
					}else{
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
						
					}
					param.put("EMAIL", transportEntity.getEMAIL());
					if(!TextUtils.isEmpty(transportEntity.getINC_NAME())&&!transportEntity.getINC_NAME().equals("null")){
						param.put("INC_NAME", transportEntity.getINC_NAME());	
					}else{
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
						
					}
					if(!TextUtils.isEmpty(transportEntity.getINC_TYPE())&&!transportEntity.getINC_TYPE().equals("null")){
						param.put("INC_TYPE", transportEntity.getINC_TYPE());
					}else{
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
							
					}
					if(!TextUtils.isEmpty(transportEntity.getINC_DEPUTY())&&!transportEntity.getINC_DEPUTY().equals("null")){
						param.put("INC_DEPUTY", transportEntity.getINC_DEPUTY());				
					}else{
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
							
					}
					if((!TextUtils.isEmpty(transportEntity.getINC_ZZJGDM())&&!transportEntity.getINC_ZZJGDM().equals("null"))
							||(!TextUtils.isEmpty(transportEntity.getTYSHXYDM())&&!transportEntity.getTYSHXYDM().equals("null"))
							||(!TextUtils.isEmpty(transportEntity.getINC_PERMIT())&&!transportEntity.getINC_PERMIT().equals("null"))){
						if(!TextUtils.isEmpty(transportEntity.getINC_ZZJGDM())&&!transportEntity.getINC_ZZJGDM().equals("null")){
							param.put("INC_ZZJGDM", transportEntity.getINC_ZZJGDM());
						}else {
							param.put("INC_ZZJGDM", "");
						}
						if (!TextUtils.isEmpty(transportEntity.getTYSHXYDM())&&!transportEntity.getTYSHXYDM().equals("null")) {
							param.put("TYSHXYDM", transportEntity.getTYSHXYDM());
						} else {
							param.put("TYSHXYDM", "");
						}
						if (!TextUtils.isEmpty(transportEntity.getINC_PERMIT())&&!transportEntity.getINC_PERMIT().equals("null")) {
							param.put("INC_PERMIT", transportEntity.getINC_PERMIT());
						} else {
							param.put("INC_PERMIT", "");
						}
					}else {
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
					}
					
					if(!TextUtils.isEmpty(transportEntity.getINC_PID())&&!transportEntity.getINC_PID().equals("null")){
						param.put("INC_PID", transportEntity.getINC_PID());

					}else {
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
					}
					
					param.put("INC_ADDR", transportEntity.getINC_ADDR());
					param.put("INC_INDICIA", transportEntity.getINC_INDICIA());
//					param.put("INC_PHONE", incPHONE);
//					param.put("INC_FAX", incFAX);
//					param.put("INC_NETWORK", incNETWORK);
//					param.put("INC_EMAIL", incEMAIL);
					if(!TextUtils.isEmpty(transportEntity.getRealName())&&!transportEntity.getRealName().equals("null")){
						param.put("AGE_NAME", transportEntity.getRealName());

					}else {
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
					}
					if(!TextUtils.isEmpty(transportEntity.getIdcardNum())&&!transportEntity.getIdcardNum().equals("null")){
						param.put("AGE_PID", transportEntity.getIdcardNum());
					}else {
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
					}
					if(!TextUtils.isEmpty(transportEntity.getLoginPhone())&&!transportEntity.getLoginPhone().equals("null")){
						param.put("AGE_MOBILE", transportEntity.getLoginPhone());
					}else {
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
						return;
					}
//					param.put("AGE_EMAIL", ageEMAIL);
//					param.put("AGE_PHONE", agePHONE);
//					param.put("AGE_INDICIA", ageINDICIA);
//					param.put("AGE_ADDR", ageADDR);

					String response = HTTP.excute("registerInc", "RestUserService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						Login(flag, transportEntity);
							
					} else {
						String error = json.getString("error");
						Log.d("WSBSMAIN", error);
						DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_update_user_error")));
//						DialogUtil.showUIToast(Register.getActivity(), error);
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
		new Thread(Submit2).start();
	}
	
	private void updateUserInfo(final TransportEntity transportEntity){
		final Runnable updateInfo = new Runnable() {
			@Override
			public void run() {
				try {


					JSONObject param = new JSONObject();

					param.put("token", Constants.user.getTOKEN());
					param.put("ID", Constants.user.getUSER_ID());
					param.put("PSW", transportEntity.getPassword());
					if(transportEntity.getEnterpriseStatus().equals("3")){
						param.put("TYPE", "2");
						param.put("INC_NAME", transportEntity.getINC_NAME());
						param.put("INC_TYPE", transportEntity.getINC_TYPE());
						if((!TextUtils.isEmpty(transportEntity.getINC_ZZJGDM())&&!transportEntity.getINC_ZZJGDM().equals("null"))
								||(!TextUtils.isEmpty(transportEntity.getTYSHXYDM())&&!transportEntity.getTYSHXYDM().equals("null"))
								||(!TextUtils.isEmpty(transportEntity.getINC_PERMIT())&&!transportEntity.getINC_PERMIT().equals("null"))){
							param.put("INC_ZZJGDM", transportEntity.getINC_ZZJGDM());
							param.put("TYSHXYDM", transportEntity.getTYSHXYDM());
							param.put("INC_PERMIT", transportEntity.getINC_PERMIT());
						}else {
							Log.e("ddddddd", "不能同时为空");
							return;
						}
						param.put("INC_DEPUTY", transportEntity.getINC_DEPUTY());
						param.put("INC_PID", transportEntity.getINC_PID());
						param.put("AGE_NAME", transportEntity.getRealName());
						param.put("AGE_PID", transportEntity.getIdcardNum());
//						param.put("AGE_EMAIL", ageEMAIL);						
//						param.put("AGE_PHONE", agePHONE);
//						param.put("AGE_INDICIA", ageINDICIA);
//						param.put("AGE_ADDR", ageADDR);
						param.put("AGE_MOBILE", transportEntity.getLoginPhone());
					}else{
						String TypeId="";
						if(transportEntity.getIdcardType().equals("1")){
							TypeId="10";
						}else if(transportEntity.getIdcardType().equals("2")){
							TypeId="20";
						}else if(transportEntity.getIdcardType().equals("3")){
							TypeId="14";
						}else if(transportEntity.getIdcardType().equals("4")){
							TypeId="15";
						}
						param.put("TYPE", "1");
//						param.put("USER_GENDER", transportEntity.getSex());
						param.put("USER_NAME", transportEntity.getRealName());
//						param.put("USER_EMAIL", USER_EMAIL);
						param.put("CERTIFICATETYPE", TypeId);
						param.put("USER_PID", transportEntity.getIdcardNum());
						param.put("USER_MOBILE", transportEntity.getLoginPhone());
//						param.put("USER_ADDRESS", transportEntity.get);
					}
					
					String response = HTTP.excute("modifyinfo", "RestUserService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						Log.e("error", "修改成功");
						Editor editor=sp.edit();
						editor.putInt("version", transportEntity.getVersion());
						editor.commit();
					} else {
						String error = json.getString("error");
						Log.e("error", error);
					}

				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		};
		new Thread(updateInfo).start();
	}
	private void showDialog(){
		getActivity().runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				new AlertDialog.Builder(getActivity()).setMessage("信息不完全，是否去完善信息？").setTitle(getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GloabDelegete gloabDelegete=Constants.getInstance().getGloabDelegete();
						if (gloabDelegete!=null) {
							gloabDelegete.doActivity(getActivity(), 3, null);
						}else{
							DialogUtil.showUIToast(getActivity(), "gloabDelegete is null");
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				}).show();
			}
		});
		
	}
}
