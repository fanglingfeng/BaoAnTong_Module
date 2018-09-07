package com.tjsoft.webhall;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.AddInfoCallBack;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;

//import com.hxsoft.bat.openapi.impl.DefaultActionListener;



	/**
	 * 软件全局配置文件
	 * @author Administrator
	 *
	 */
	public class AppConfig extends Application {
		private static AppConfig instance;

	    public static AppConfig getInstance() {
	        return instance;
	    }
//		@Override
		public void onCreate() {
			super.onCreate();
			System.out.println("fuchl AppConfig onCreate");
			final Constants constants = new Constants();
			final TransportEntity transportEntity=new TransportEntity();
			GloabDelegete delegete = new GloabDelegete() {
				
				@Override
				public TransportEntity getUserInfo() {
					// TODO Auto-generated method stub
					return transportEntity;
				}

				@Override
				public void doActivity(Context context, int tag, TransportCallBack callback) {
					// TODO Auto-generated method stub
					
				}

//				/**
//				 * @param context
//				 * @param name
//				 * @param id
//				 * @param token
//				 * @param callback
//				 */
//				@Override
//				public void faceRecognition(Context context, String name, String id, String token, DefaultActionListener<String> callback) {
//
//				}

				@Override
				public void addInfo(Activity activity, AddInfoCallBack back) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void doBoundMSTAccount(Context context, String todoAcountName, String todoAccountPwd,
						TransportCallBack transportCallBack) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void loginMST(Context context) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void modifyPWD(String todoAcountName, String todoAccountPwd) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void startStatistics() {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void endStatistics(String ParentName, String CategoryName, String ModelName) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void login(String user, String pwd) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void Registered(String strName, String strNewpwd, String strMobile) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void contactCustomer(Activity activity, String modelName, String eventName, String departName) {
					// TODO Auto-generated method stub
					
				}
				
			};
			System.out.println("fuchl  transportEntity===="+transportEntity.getToken());
			constants.setGloabDelegete(delegete);
			if (!TextUtils.isEmpty(transportEntity.getToken())) {
				Constants.user=new User();
				Constants.user.setUSER_ID(transportEntity.getUserId());
				Constants.user.setUSERNAME(transportEntity.getName());
				Constants.user.setREALNAME(transportEntity.getRealName());
				Constants.user.setMOBILE(transportEntity.getLoginPhone());
				Constants.user.setTYPE(transportEntity.getIdcardType());// 证件类型
				Constants.user.setCODE(transportEntity.getIdcardNum());// 证件号码
			}
//			instance = this;
			Constants.initImageLoader(getApplicationContext());//初始化ImageLoader
	//		SDKInitializer.initialize(getApplicationContext());//百度地图初始化
			//CrashHandler crashHandler = CrashHandler.getInstance();  //程序全局异常捕获
			//crashHandler.init(getApplicationContext());

		}

	}
