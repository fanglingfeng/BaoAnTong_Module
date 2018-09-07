package com.tjsoft.webhall.imp;

import android.app.Activity;
import android.content.Context;

import com.tjsoft.webhall.entity.TransportEntity;

//import com.hxsoft.bat.openapi.impl.DefaultActionListener;

//import com.hxsoft.bat.openapi.impl.DefaultActionListener;

public interface GloabDelegete {
	/**
	 * TODO:获取是实体信息
	 * 
	 * */
	public TransportEntity getUserInfo();
	/**
	 * 
	 * @param context
	 * @param tag 响应对应的activity  1.登录 2.注册
	 * @param callback 返回响应回调
	 */
	
	public void doActivity(Context context,int  tag, TransportCallBack callback);
		/**
//	 *
//	 * @param context
//	 * @param
//	 * @param
//	 */
//
//	public void faceRecognition(Context context,String  name, String id,String token,DefaultActionListener<String> callback);

	
	/**
	 * 通知完善个人信息
	 * 
	 * @return
	 */
	public void addInfo(Activity activity, AddInfoCallBack back);

	/**
	 * 是否绑定网上办事账号
	 * 
	 * @param todoAcountName
	 *            网上办事账号
	 * @param todoAccountPwd
	 *            网上办事密码
	 * @param transportCallBack
	 *            回调接口
	 */
	public void doBoundMSTAccount(Context context, String todoAcountName, String todoAccountPwd, TransportCallBack transportCallBack);

	/**
	 * 获得当前context后，跳转到宝安通登录界面进行登录。
	 * 通知MST登录
	 */
	public void loginMST(Context context);

	/**
	 * 修改密码
	 * @param todoAcountName 用户账号
	 * @param todoAccountPwd 密码
	 */
	public void modifyPWD(String todoAcountName, String todoAccountPwd);

	/**
	 * 开始统计
	 */
	public void startStatistics();

	/**
	 * 结束统计
	 * 
	 * @param ParentName
	 *            企业办事
	 * @param CategoryName
	 *            人力资源
	 * @param ModelName
	 *            创业补贴
	 */
	public void endStatistics(String ParentName, String CategoryName, String ModelName);

	/**
	 * 宝安通登录
	 * 
	 * @param user 用户账号
	 * @param pwd 密码
	 */
	public void login(final String user, final String pwd);
	
	
	/**
	 * 宝安通注册
	 * 
	 * @param strName 用户账号
	 * @param strNewpwd 密码
	 * @param strMobile 电话
	 */
	public void Registered(String strName, String strNewpwd, String strMobile);
	
	/**
     * 联系客服接口
     *
     * @param activity
     * @param modelName  咨询的模块名称
     * @param eventName  咨询的事项名称
     * @param departName 咨询的部门名称
     */
    public void contactCustomer(Activity activity, String modelName, String eventName, String departName);

}
