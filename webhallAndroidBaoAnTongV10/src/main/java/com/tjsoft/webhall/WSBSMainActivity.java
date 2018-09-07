package com.tjsoft.webhall;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.PermListByDB;
import com.tjsoft.webhall.entity.BanJian;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.lib.VerticalSwitchTextView;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.search.PermListByName;
import com.tjsoft.webhall.ui.search.Search;
import com.tjsoft.webhall.ui.user.ChangeEnterpriseInfo;
import com.tjsoft.webhall.ui.user.Login;
import com.tjsoft.webhall.ui.user.LoginUtil;
import com.tjsoft.webhall.ui.user.Register;
import com.tjsoft.webhall.ui.work.MaterialManage;
import com.tjsoft.webhall.ui.work.PermGuideContainer;
import com.tjsoft.webhall.ui.wsbs.AddComplain;
import com.tjsoft.webhall.ui.wsbs.AddConsult;
import com.tjsoft.webhall.ui.wsbs.ApplyGuideActivity;
import com.tjsoft.webhall.ui.wsbs.ComplainList;
import com.tjsoft.webhall.ui.wsbs.ConsultList;
import com.tjsoft.webhall.ui.wsbs.NetworkListActivity;
import com.tjsoft.webhall.ui.wsbs.OrderGuideActivity;
import com.tjsoft.webhall.ui.wsbs.PermInfoActivity;
import com.tjsoft.webhall.ui.wsbs.WSBS;
import com.tjsoft.webhall.ui.wsbs.WSBS1;
import com.tjsoft.webhall.ui.wsbs.WSBS2;

/**
 * 网上办事功能首页
 * 
 * @author long  
 * 
 */
public class WSBSMainActivity extends AutoDialogActivity {
	private LinearLayout lay1, lay2, lay3, lay4, lay5, lay6, lay7, lay8, lay9, lay10, lay11, lay12, lay13, lay14;
	private Intent intent;
	private RelativeLayout back;
	private Button home;
	private Button btnSearch;// 搜索
	private EditText textSearch;
	private TextView notice_wdbj;
	private TransportEntity transportEntity;
	private SharedPreferences sp;
	private int version;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_main"));
		Constants.initImageLoader(this);
		initView();
		sp=this.getSharedPreferences("Version", MODE_PRIVATE);
		version=sp.getInt("version", 0);
	}

	final Runnable autoLogin = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("USERNAME", FileUtil.Load(WSBSMainActivity.this, "username"));
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(FileUtil.Load(WSBSMainActivity.this, "password")));
				String response = HTTP.excute("login", "RestUserService", param.toString());
				final JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
					new Thread(GetWDBJ).start();

				} else {
				}

			} catch (Exception e) {
				e.printStackTrace();

			}
		}
	};

	@Override
	protected void onResume() {
//		SharedPreferences config = getSharedPreferences("config", MODE_PRIVATE);
//		boolean autoLogon = config.getBoolean("autoLogon", false);
//		if (autoLogon) {
//			new Thread(autoLogin).start();
//		}
		if (!Constants.DEBUG_TOGGLE) {
			GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
			if (gloabDelegete != null) {
				transportEntity = gloabDelegete.getUserInfo();
				System.out.println("fuchl  主页面LOG getIdCardType " + transportEntity.getIdcardType());
				if (checkRealNameInfo(transportEntity)) {
					notice_wdbj.setVisibility(View.GONE);
				} else {
					Login(0, transportEntity);
				}
			}
		}else{
			if(Constants.user!=null){
				new Thread(GetWDBJ).start();
			}
		}
		super.onResume();
	}

	private void initView() {
		btnSearch = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnSearch"));
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		textSearch = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "textSearch"));
		notice_wdbj = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "notice_wdbj"));

		lay1 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay1"));
		lay2 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay2"));
		lay3 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay3"));
		lay4 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay4"));
		lay5 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay5"));
		lay6 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay6"));
		lay7 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay7"));
		lay8 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay8"));
		lay9 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay9"));
		lay10 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay10"));
		lay11 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay11"));
		lay12 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay12"));
		lay13 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay13"));
		lay14 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay14"));

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(textSearch.getText().toString())) {
					DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_search_empty_notice")));
				} else {
					Constants.WSBS_PATH=0;
					intent = new Intent();
					intent.setClass(WSBSMainActivity.this, PermListByName.class);
					intent.putExtra("name", textSearch.getText().toString().trim());
					startActivity(intent);
				}

			}
		});

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(WSBSMainActivity.this, PermInfoActivity.class));

			}
		});

		lay1.setOnClickListener(new OnClickListener() {// 企业办事

			@Override
			public void onClick(View v) {
				Constants.WSBS_PATH = 1;
				intent = new Intent();
				intent.putExtra("flag", 2);
				intent.setClass(WSBSMainActivity.this, ApplyGuideActivity.class);
				startActivity(intent);
			}
		});
		lay2.setOnClickListener(new OnClickListener() {// 个人办事

			@Override
			public void onClick(View v) {
				Constants.WSBS_PATH = 1;
				intent = new Intent();
				intent.putExtra("flag", 1);
				intent.setClass(WSBSMainActivity.this, ApplyGuideActivity.class);
				startActivity(intent);
				
			}
		});
		lay3.setOnClickListener(new OnClickListener() {// 我要预约

			@Override
			public void onClick(View v) {
				Constants.WSBS_PATH = 2;
				startActivity(new Intent().setClass(WSBSMainActivity.this, OrderGuideActivity.class));
			}
		});
		lay4.setOnClickListener(new OnClickListener() {// 进度查询

			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(WSBSMainActivity.this, Search.class));

			}
		});
		lay5.setOnClickListener(new OnClickListener() {// 服务网点

			@Override
			public void onClick(View v) {
				Constants.WSBS_PATH = 0;
				startActivity(new Intent().setClass(WSBSMainActivity.this, NetworkListActivity.class));

			}
		});
		lay6.setOnClickListener(new OnClickListener() {// 我要咨询

			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(WSBSMainActivity.this);
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(WSBSMainActivity.this);
						} else {
							Login(1, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
					}

				}else{
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(WSBSMainActivity.this, Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(WSBSMainActivity.this, AddConsult.class));
					}
				}

			}
		});
		lay7.setOnClickListener(new OnClickListener() {// 我要投诉

			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(WSBSMainActivity.this);
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(WSBSMainActivity.this);
						} else {
							startActivity(new Intent().setClass(WSBSMainActivity.this, AddComplain.class));
						}
					} else {
						DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(WSBSMainActivity.this, Login.class);
						startActivity(intent);
						return;
					}
				}
			}
		});
		lay8.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

		lay9.setOnClickListener(new OnClickListener() {// 我的办件

			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						// DialogUtil.showUIToast(WSBSMainActivity.this,
						// transportEntity.getToken());
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(WSBSMainActivity.this);
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(WSBSMainActivity.this);
						} else {
							Constants.WSBS_PATH = 0;
							Login(2, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(WSBSMainActivity.this, Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(WSBSMainActivity.this, WDBJ.class));
					}
				}
			}
		});
		lay10.setOnClickListener(new OnClickListener() {// 我的预约

			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(WSBSMainActivity.this);
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(WSBSMainActivity.this);
						} else {
							Login(3, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(WSBSMainActivity.this, Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(WSBSMainActivity.this, ReserveList.class));
					}
				}
			}
		});
		lay11.setOnClickListener(new OnClickListener() {// 我的收藏

			@Override
			public void onClick(View v) {
				Constants.WSBS_PATH=0;
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(WSBSMainActivity.this);
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(WSBSMainActivity.this);
						} else {
							Login(5, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(WSBSMainActivity.this, Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(WSBSMainActivity.this, PermListByDB.class));
					}
				}
			}
		});
		lay12.setOnClickListener(new OnClickListener() {// 我的咨询

			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {
							LoginBaoAnTongUtil.checkLogin(WSBSMainActivity.this);
							// LoginUtil util = new LoginUtil();
							// util.checkLogin(WSBSMainActivity.this);
						} else {
							Login(4, transportEntity);
						}
					} else {
						DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
					}
				} else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(WSBSMainActivity.this, Login.class);
						startActivity(intent);
						return;
					}else{
						startActivity(new Intent().setClass(WSBSMainActivity.this, ConsultList.class));
					}
				}
			}
		});
		lay13.setOnClickListener(new OnClickListener() {// 我的投诉

			@Override
			public void onClick(View v) {
				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
				if(gloabDelegete!=null){
					TransportEntity transportEntity=gloabDelegete.getUserInfo();
					if (checkRealNameInfo(transportEntity)) {
						LoginBaoAnTongUtil.checkLogin(WSBSMainActivity.this);
//						LoginUtil util = new LoginUtil();
//						util.checkLogin(WSBSMainActivity.this);
					} else {
						Constants.user=new User();
						Constants.user.setUSER_ID(transportEntity.getUserId());
						Constants.user.setUSERNAME(transportEntity.getName());
						Constants.user.setREALNAME(transportEntity.getRealName());
						Constants.user.setMOBILE(transportEntity.getLoginPhone());
						Constants.user.setTYPE(transportEntity.getIdcardType());// 证件类型
						Constants.user.setCODE(transportEntity.getIdcardNum());// 证件号码
						startActivity(new Intent().setClass(WSBSMainActivity.this, ComplainList.class));
					}
				}else{
					DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
				}

			}
		});
		lay14.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});

	}

	final Runnable GetWDBJ = new Runnable() {
		@Override
		public void run() {
			try {
				if (null == Constants.user) {
					return;
				}
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("APPLICANTID", Constants.user.getUSER_ID());
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				String response = HTTP.excute("zaibanjian", "RestOnlineDeclareService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final List<BanJian> temp = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<BanJian>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {

							ArrayList<String> list = new ArrayList<String>();
							VerticalSwitchTextView adView = (VerticalSwitchTextView) findViewById(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "id", "adView"));
							if (null != temp && temp.size() > 0) {
								notice_wdbj.setText(temp.size() + "");
								notice_wdbj.setVisibility(View.VISIBLE);

								for (int i = 0; i < temp.size(); i++) {
									String pName = "";
									if (temp.get(i).getPNAME().length() > 13) {
										pName = temp.get(i).getPNAME().substring(0, 13) + "...";
									} else {
										pName = temp.get(i).getPNAME();
									}
									list.add("您办理的(" + pName + ")" + temp.get(i).getCSTATUS());
								}
								adView.setVisibility(View.GONE);
								adView.setTextContent(list);
								adView.setCbInterface(new VerticalSwitchTextView.VerticalSwitchTextViewCbInterface() {
									@Override
									public void showNext(int index) {

									}

									@Override
									public void onItemClick(int index) {
										startActivity(new Intent().setClass(WSBSMainActivity.this, WDBJ.class));

									}
								});
							} else {
								notice_wdbj.setVisibility(View.GONE);
								adView.setVisibility(View.GONE);
							}

						}
					});

				}else{
					Log.d("WSBSMAIN","访问出错2222===="+code);
					runOnUiThread(new Runnable() {						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							notice_wdbj.setVisibility(View.GONE);
						}
					});					
				}

			} catch (Exception e) {
				Log.d("WSBSMAIN","访问出错11111");
				runOnUiThread(new Runnable() {					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						notice_wdbj.setVisibility(View.GONE);
					}
				});
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
		if(!TextUtils.isEmpty(t.getToken())){
			return false;
		}
		return true;
	}
	/**
	 * 万能密码登录
	 * @param flag 1 我要咨询  2 我的申报  3 我的预约 4我的咨询
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
						new Thread(GetWDBJ).start();
						int ver=transportEntity.getVersion();
						Log.e("sps====", "华讯的version===="+ver+"自己的版本==="+version);
						if(version<ver){
							updateUserInfo(transportEntity);
						
						}
						switch (flag) {
						case 1:
							startActivity(new Intent().setClass(WSBSMainActivity.this, AddConsult.class));
							break;
						case 2:
							startActivity(new Intent().setClass(WSBSMainActivity.this, WDBJ.class));
							break;
						case 3:
							startActivity(new Intent().setClass(WSBSMainActivity.this, ReserveList.class));
							break;
						case 4:
							startActivity(new Intent().setClass(WSBSMainActivity.this, ConsultList.class));
							break;
						case 5:
							startActivity(new Intent().setClass(WSBSMainActivity.this, PermListByDB.class));
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
						DialogUtil.showUIToast(WSBSMainActivity.this,error);					}

				} catch (Exception e) {
//					DialogUtil.showUIToast(WSBSMainActivity.this, WSBSMainActivity.this.getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
//		if(flag==0){
			new Thread(LoginNotPassW).start();	
//		}else {
//			dialog=Background.Process(WSBSMainActivity.this, LoginNotPassW,WSBSMainActivity.this.getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_loding")));	
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
//						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						DialogUtil.showUIToast(WSBSMainActivity.this, error);
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource
							.getResourseIdByName(WSBSMainActivity.this, "string", "tj_occurs_error_network")));
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
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						return;
						
					}
					if(!TextUtils.isEmpty(transportEntity.getPassword())&&!transportEntity.getPassword().equals("null")){
						param.put("PASSWORD", transportEntity.getPassword());	
					}else{
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						return;
						
					}
					param.put("EMAIL", transportEntity.getEMAIL());
					if(!TextUtils.isEmpty(transportEntity.getINC_NAME())&&!transportEntity.getINC_NAME().equals("null")){
						param.put("INC_NAME", transportEntity.getINC_NAME());	
					}else{
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						return;
						
					}
					if(!TextUtils.isEmpty(transportEntity.getINC_TYPE())&&!transportEntity.getINC_TYPE().equals("null")){
						param.put("INC_TYPE", transportEntity.getINC_TYPE());
					}else{
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						return;
							
					}
					if(!TextUtils.isEmpty(transportEntity.getINC_DEPUTY())&&!transportEntity.getINC_DEPUTY().equals("null")){
						param.put("INC_DEPUTY", transportEntity.getINC_DEPUTY());				
					}else{
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
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
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						return;
					}
					
					if(!TextUtils.isEmpty(transportEntity.getINC_PID())&&!transportEntity.getINC_PID().equals("null")){
						param.put("INC_PID", transportEntity.getINC_PID());

					}else {
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
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
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						return;
					}
					if(!TextUtils.isEmpty(transportEntity.getIdcardNum())&&!transportEntity.getIdcardNum().equals("null")){
						param.put("AGE_PID", transportEntity.getIdcardNum());
					}else {
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						return;
					}
					if(!TextUtils.isEmpty(transportEntity.getLoginPhone())&&!transportEntity.getLoginPhone().equals("null")){
						param.put("AGE_MOBILE", transportEntity.getLoginPhone());
					}else {
						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
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
//						DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_update_user_error")));
						DialogUtil.showUIToast(WSBSMainActivity.this, error);
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(WSBSMainActivity.this, getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_occurs_error_network")));
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
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				new AlertDialog.Builder(WSBSMainActivity.this).setMessage("信息不完全，是否去完善信息？").setTitle(getString(MSFWResource.getResourseIdByName(WSBSMainActivity.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GloabDelegete gloabDelegete=Constants.getInstance().getGloabDelegete();
						if (gloabDelegete!=null) {
							gloabDelegete.doActivity(WSBSMainActivity.this, 3, null);
						}else{
							DialogUtil.showUIToast(WSBSMainActivity.this, "gloabDelegete is null");
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
