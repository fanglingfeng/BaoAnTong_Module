package com.tjsoft.webhall.ui.work;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.util.ResMgr;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.webhall.WSBSMainActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.FavoriteManage;
import com.tjsoft.webhall.entity.Guide;
import com.tjsoft.webhall.entity.PermGroup;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.AddInfoCallBack;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;
import com.tjsoft.webhall.lib.NoTouchViewPage;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.ReserveSubmit;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.user.Login;
import com.tjsoft.webhall.ui.user.LoginUtil;
import com.tjsoft.webhall.ui.wsbs.AddConsult;
import com.tjsoft.webhall.ui.wsbs.ConsultList;
import com.tjsoft.webhall.ui.wsbs.PermGuide;
import com.tjsoft.webhall.ui.wsbs.PermGuideByFavorite;
//办事指南页面
public class PermGuideContainer extends FragmentActivity {
	protected ProgressDialog dialog;
	private NoTouchViewPage m_vp;
	private Button addFavorite;
	private RelativeLayout  back;
	private Button btnNotify;
	private final int GET_GUIDE_SUCCESS = 1;

	// 页面列表
	private ArrayList<Fragment> fragmentList;
	// 标题列表
	ArrayList<RadioButton> radioButtons = new ArrayList<RadioButton>();
	// 通过pagerTabStrip可以设置标题的属性
	private Fragment fragment1;
	private Fragment fragment2;
	private Fragment fragment3;
	private RadioButton radioBtn1, radioBtn2, radioBtn3;
	private RadioGroup radioGroup;
	private Button reserve, declare;
	private Button btnWSZX;
	private Intent intent;
	private String BSNUM = "";// 业务流水号
	public static Permission permission;
	private String from = "";
	private LinearLayout applyLay;
	private List<PermGroup> groups;
	public  String P_GROUP_ID;// 分组id
	private String P_GROUP_NAME;// 分组名称

	private String username;
	private String password;
	private ProgressDialog progressDialog;
	private final static int SHOW_DIALOG = 1; // 登录时显示进度对话框
	private final static int CLOSE_DIALOG = 2; // 关闭进度对话框
	private final static int SHOW_TOAST = 3; // 显示提示框
	private TransportEntity transportEntity;
	private TextView titleTV;
	private String REALNAME = "";
	private String CERTIFICATETYPE = "";
	private String USER_PID = "";
	private String USER_MOBILE = "";
	private String BtnTxt = "";

	private int flag=0;//0 其他地方进入   1我的收藏进入
	
	private SharedPreferences sp;//记录版本号的
	private int version;
	
	private String PERMID = "";

	public static Guide guide;
	private MyHandler2 handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		sp=this.getSharedPreferences("Version", MODE_PRIVATE);
		version=sp.getInt("version", 0);
		handler = new MyHandler2();
		PERMID = getIntent().getStringExtra("PERMID");
		Constants.getInstance().addActivity(this);
		Constants.WSBS_PATH=getIntent().getIntExtra("WSBSFLAG", 0);
//		permission = (Permission) getIntent().getSerializableExtra("permission");
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_perm_guide_container"));
		Background.Process(PermGuideContainer.this, GetGuide, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));

		
	}
	
	private void initView(){
		BSNUM = permission.getBSNUM();
		from = getIntent().getStringExtra("from");
		
		applyLay = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "applyLay"));
		reserve = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "reserve"));
		declare = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "declare"));
		btnWSZX = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnWSZX"));
		btnWSZX.setOnClickListener(new OnClickListener() {//咨询

			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					/**
					 * 判断是否登录MST
					 */
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					Log.e("Login", "gloabDelegete==="+gloabDelegete);
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {// 信息完整
							//登录自己的接口			
							Login(3, transportEntity);					
							// Constants.user.setUSER_ID(transportEntity.getUserId());
						}else {
							new AlertDialog.Builder(PermGuideContainer.this).setMessage("你还没有登录，是否现在登录")
							.setTitle(PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_notify")))
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									if (gloabDelegete != null) {
										gloabDelegete.doActivity(PermGuideContainer.this, 1, null);
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
						DialogUtil.showUIToast(PermGuideContainer.this, "gloabDelegete is null");
						return;
					}
				}else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(PermGuideContainer.this, Login.class);
						startActivity(intent);
						return;
					}else{
						Intent intent = new Intent();
						intent.setClass(PermGuideContainer.this, AddConsult.class);
						intent.putExtra("permission", permission);
						startActivity(intent);	
					}
				}

			}
		});

		btnNotify = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnNotify"));
		initButton();
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		addFavorite = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "addFavorite"));
		titleTV = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "titleTV"));
		titleTV.setText("办事指南");
//		if (Constants.WSBS_PATH == 1) {
//			titleTV.setText("事项详情");
//
//		} else if (Constants.WSBS_PATH == 2) {
//			
//		}
		titleTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
				if (gloabDelegete != null) {
					transportEntity = gloabDelegete.getUserInfo();
					if (checkRealNameInfo(transportEntity)) {
						System.out.println("fuchl   getEnterpriseStatus " + transportEntity.getEnterpriseStatus());
//						if(transportEntity.getEnterpriseStatus().equals("3")){
//							DialogUtil.showUIToast(PermGuideContainer.this, transportEntity.getAGE_NAME() + "\n" + 
//						transportEntity.getAGE_PID() + "\n" + transportEntity.getIdcardType() + "\n" + transportEntity.getAGE_MOBILE()
//						+ "\n" + transportEntity.getINC_NAME());
							System.out.println("fuchl   getName " + transportEntity.getName());
							System.out.println("fuchl   getIdCardNo " + transportEntity.getIdcardNum());
							System.out.println("fuchl   getIdCardType " + transportEntity.getIdcardType());
							System.out.println("fuchl   getMobile " + transportEntity.getLoginPhone());
							System.out.println("fuchl   getPassword " + transportEntity.getPassword());
							System.out.println("fuchl   getRealName " + transportEntity.getRealName());
							System.out.println("fuchl   getAGE_NAME " + transportEntity.getAGE_NAME());
							System.out.println("fuchl   getAGE_MOBILE " + transportEntity.getAGE_MOBILE());
							System.out.println("fuchl   getINC_NAME " + transportEntity.getINC_NAME());
							System.out.println("fuchl   getAGE_PID " + transportEntity.getAGE_PID());
							System.out.println("fuchl   getINC_DEPUTY " + transportEntity.getINC_DEPUTY());
							System.out.println("fuchl   getINC_PERMIT " + transportEntity.getINC_PERMIT());
							System.out.println("fuchl   getTYSHXYDM " + transportEntity.getTYSHXYDM());
							System.out.println("fuchl   getINC_ZZJGDM " + transportEntity.getINC_ZZJGDM());
							System.out.println("fuchl   getINC_INDICIA " + transportEntity.getINC_INDICIA());
							System.out.println("fuchl   getINC_TYPE " + transportEntity.getINC_TYPE());
							System.out.println("fuchl   getversion " + transportEntity.getVersion());

//						}
						
					}
				}

			}
		});

		m_vp = (NoTouchViewPage) findViewById(MSFWResource.getResourseIdByName(this, "id", "viewpager"));
		m_vp.setScrollble(false);
		initSetOnListener();
		
		flag=getIntent().getIntExtra("flag", 0);
		if(flag==1){
			fragment1 = new PermGuideByFavorite();
		}else {
			fragment1 = new PermGuide();
		}		
		fragment2 = new PracticalGuide();
		fragment3 = new ApplyList();

		fragmentList = new ArrayList<Fragment>();
		fragmentList.add(fragment1);
		fragmentList.add(fragment2);
		fragmentList.add(fragment3);

		radioBtn1 = (RadioButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "radioBtn1"));
		radioBtn2 = (RadioButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "radioBtn2"));
		radioBtn3 = (RadioButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "radioBtn3"));

		radioButtons.add(radioBtn1);
		radioButtons.add(radioBtn2);
		radioButtons.add(radioBtn3);

		radioGroup = (RadioGroup) findViewById(MSFWResource.getResourseIdByName(this, "id", "radio_group"));
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int radioBtn1 = MSFWResource.getResourseIdByName(PermGuideContainer.this, "id", "radioBtn1");
				int radioBtn2 = MSFWResource.getResourseIdByName(PermGuideContainer.this, "id", "radioBtn2");
				int radioBtn3 = MSFWResource.getResourseIdByName(PermGuideContainer.this, "id", "radioBtn3");
				if (checkedId == radioBtn1) {
					m_vp.setCurrentItem(0);
					radioGroup.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_legal_conditions"));
				} else if (checkedId == radioBtn2) {
					m_vp.setCurrentItem(1);
					radioGroup.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_practical_guide"));
				} else if (checkedId == radioBtn3) {
					m_vp.setCurrentItem(2);
					radioGroup.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_electronic_materials"));
				}

			}
		});
		m_vp.setAdapter(new MyViewPagerAdapter(getSupportFragmentManager()));
		int position = getIntent().getIntExtra("position", 0);
		m_vp.setCurrentItem(position);
		radioButtons.get(position).setChecked(true);
		m_vp.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				radioButtons.get(position).setChecked(true);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		if (FavoriteManage.exist(permission.getID(), PermGuideContainer.this)) {
			addFavorite.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_ic_perm_favorite_have"));
		}
	}
	@Override
	protected void onResume() {		
		super.onResume();
	}

	/**
	 * 初始化申报、预约按钮
	 */
	private void initButton() {
		if ("WorkSpace".equals(from)) {
			applyLay.setVisibility(View.GONE);
		}
		if(Constants.WSBS_PATH==3){
			applyLay.setVisibility(View.GONE);
		}
		if (null != permission && null != permission.getSFYDSB() && permission.getSFYDSB().equals("1")) {// 不支持移动申报隐藏申报预约按钮
			declare.setVisibility(View.VISIBLE);
		}

		if (null != permission && null != permission.getISRESERVE() && permission.getISRESERVE().equals("1")) {// 是否提供网上预约服务，0否，1是"
			reserve.setVisibility(View.VISIBLE);
		}
		switch (Constants.WSBS_PATH) {
		case 0:
			LinearLayout.LayoutParams params = new LayoutParams(DensityUtil.dip2px(this, 0), DensityUtil.dip2px(this, 60), 1);
			LinearLayout.LayoutParams params2 = new LayoutParams(DensityUtil.dip2px(this, 0), DensityUtil.dip2px(this, 60), 1);
			params.setMargins(0, 0, DensityUtil.dip2px(this, 1), 0);
			reserve.setLayoutParams(params);
			declare.setLayoutParams(params);
			btnWSZX.setLayoutParams(params2);
			declare.setBackgroundColor(Color.parseColor("#8DC73F"));
			reserve.setBackgroundColor(Color.parseColor("#2AABE4"));
			btnWSZX.setVisibility(View.VISIBLE);

			break;
		case 1:// 我要申报
			reserve.setVisibility(View.GONE);
			declare.setVisibility(View.GONE);
//			declare.setText("下一步，填写表单");
//			if (declare.getVisibility() != View.VISIBLE) {
//				btnNotify.setVisibility(View.VISIBLE);
//				btnNotify.setText("该事项暂不提供申报服务");
//			}

			break;
		case 2:// 我要预约
			declare.setVisibility(View.GONE);
			reserve.setVisibility(View.GONE);
//			reserve.setText("下一步，选择预约时间");
//			if (reserve.getVisibility() != View.VISIBLE) {
//				btnNotify.setVisibility(View.VISIBLE);
//				btnNotify.setText("该事项暂不提供预约服务");
//			}

			break;

		default:
			break;
		}

	}

	@Override
	protected void onDestroy() {
//		StatisticsTools.end("", permission.getSXZXNAME(), "我的预约");
		if(null != dialog){
			dialog.dismiss();
		}
		super.onDestroy();
	}

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
//		if (TextUtils.isEmpty(t.getBatUserName())) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getRealName())) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getIdCardType() + "")) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getIdCardNo())) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getMobile())) {
//			return false;
//		}
		return true;
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

				String response = HTTP.excuteAndCache("registerUser", "RestUserService", param.toString(),PermGuideContainer.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(PermGuideContainer.this, "注册成功！");
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					if (gloabDelegete != null) {
						gloabDelegete.doBoundMSTAccount(PermGuideContainer.this, username, password, new TransportCallBack() {

							@Override
							public void onStart() {
							}

							@Override
							public void onResult(int status) {// 0成功
								if (status == 0) {
									new Thread(Login).start();
								} else {
									DialogUtil.showUIToast(PermGuideContainer.this, "绑定宝安通失败！");
								}
							}

							@Override
							public void onFinish() {
							}
						});
					}
					finish();

				} else {
					Intent intent = new Intent();
					intent.setClass(PermGuideContainer.this, com.tjsoft.webhall.ui.user.Register.class);
					startActivity(intent);
					String error = json.getString("error");
					DialogUtil.showUIToast(PermGuideContainer.this, error + ",请手动注册网上办事大厅账号!");
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	private void initSetOnListener() {
		reserve.setOnClickListener(new OnClickListener() {//预约
			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					/**
					 * 判断是否登录MST
					 */
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					Log.e("Login", "gloabDelegete==="+gloabDelegete);
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {// 信息完整
							
//							DialogUtil.showUIToast(PermGuideContainer.this, "信息完整==token=="+transportEntity.getToken());						
							Login(2, transportEntity);					
					
							// Constants.user.setUSER_ID(transportEntity.getUserId());
						}else {
							new AlertDialog.Builder(PermGuideContainer.this).setMessage("你还没有登录，是否现在登录")
							.setTitle(PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_notify")))
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									if (gloabDelegete != null) {
										gloabDelegete.doActivity(PermGuideContainer.this, 1, null);
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
						DialogUtil.showUIToast(PermGuideContainer.this, "gloabDelegete is null");
						return;
					}
				}else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(PermGuideContainer.this, Login.class);
						startActivity(intent);
						return;
					}else{
						intent = new Intent();
						intent.setClass(PermGuideContainer.this, ReserveSubmit.class);
						intent.putExtra("permission", permission);
						startActivity(intent);
					}
				}
				
				
			

			}
		});

		declare.setOnClickListener(new OnClickListener() {//申报
			@Override
			public void onClick(View v) {
				if (!Constants.DEBUG_TOGGLE) {
					/**
					 * 判断是否登录MST
					 */
					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
					Log.e("Login", "gloabDelegete==="+gloabDelegete);
					if (gloabDelegete != null) {
						TransportEntity transportEntity = gloabDelegete.getUserInfo();
						if (checkRealNameInfo(transportEntity)) {// 信息完整
							//登录自己的接口
							Login(1, transportEntity);					
							// Constants.user.setUSER_ID(transportEntity.getUserId());
						}else {
							new AlertDialog.Builder(PermGuideContainer.this).setMessage("你还没有登录，是否现在登录")
							.setTitle(PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_notify")))
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {
								GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									if (gloabDelegete != null) {
										gloabDelegete.doActivity(PermGuideContainer.this, 1, null);
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
						DialogUtil.showUIToast(PermGuideContainer.this, "gloabDelegete is null");
						return;
					}
				}else {
					if (null == Constants.user) {
						Intent intent = new Intent();
						intent.setClass(PermGuideContainer.this, Login.class);
						startActivity(intent);
						return;
					}else{
						new Thread(GetGroup).start();
					}
				}

//				Background.Process(PermGuideContainer.this, GetGroup, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));// 获取分组ID

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PermGuideContainer.this.finish();
			}
		});
		addFavorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if(FavoriteManage.add(permission, PermGuideContainer.this)){
//					dialog=Background.Process(PermGuideContainer.this, addMyFavorite, "正在添加收藏……");
//				}else{
//					dialog=Background.Process(PermGuideContainer.this, deleteMyFavorite, "正在取消收藏……");
//				}
				if(FavoriteManage.add(permission, PermGuideContainer.this)){
					DialogUtil.showToast(PermGuideContainer.this, "收藏成功！");
					addFavorite.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_ic_perm_favorite_have"));
				}else{
					DialogUtil.showToast(PermGuideContainer.this, "取消收藏！");
					FavoriteManage.delete(permission.getID(), PermGuideContainer.this);
					addFavorite.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_ic_perm_favorite"));
				}
			}
		});
	}

	public class MyViewPagerAdapter extends FragmentPagerAdapter {
		public MyViewPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
		}

		@Override
		public Fragment getItem(int position) {
			return fragmentList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (position != 0) {
				ResMgr.findImageView(fragmentList.get(position).getView());
			}
			super.destroyItem(container, position, object);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
			return position + "";
		}

	}

	/**
	 * 是否自动注册
	 */
	private void isAutoRegisterAlert() {
		new AlertDialog.Builder(this).setMessage("是否自动注册网上办事账号，账号为宝安通账号。密码默认为身份证后6位？").setTitle(getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_notify"))).setCancelable(true).setPositiveButton("是", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Background.Process(PermGuideContainer.this, Register, "正在登录...");
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

			}
		}).show();
	}

//	private void isLoginAlert() {
//		new AlertDialog.Builder(this).setMessage("你还没有绑定网上办事账号，是否现在绑定？").setTitle(getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_notify"))).setCancelable(true).setPositiveButton("手动绑定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//				if (gloabDelegete != null) {
//					gloabDelegete.doBoundMSTAccount(PermGuideContainer.this, username, password, new TransportCallBack() {
//
//						@Override
//						public void onStart() {
//							// myHandler.sendEmptyMessage(SHOW_DIALOG);
//						}
//
//						@Override
//						public void onResult(int status) {
//							// myHandler.sendEmptyMessage(CLOSE_DIALOG);
//							if (status == 0) {
//								Message msg = Message.obtain();
//								msg.obj = "绑定成功！";
//								msg.what = SHOW_TOAST;
//								DialogUtil.showUIToast(PermGuideContainer.this, "绑定成功");
//								try {
//									Background.Process(PermGuideContainer.this, Login, "正在登录...");
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								finish();
//							} else {
//								Message msg = Message.obtain();
//								msg.obj = "绑定失败！";
//								msg.what = SHOW_TOAST;
//								DialogUtil.showUIToast(PermGuideContainer.this, "绑定失败");
//								// myHandler.sendMessage(msg);
//							}
//						}
//
//						@Override
//						public void onFinish() {
//							// myHandler.sendEmptyMessage(CLOSE_DIALOG);
//						}
//					});
//				}
//			}
//		}).setNegativeButton(BtnTxt, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//
//				if (BtnTxt.equals(Constants.AUTO_LOGIN)) {
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
//				} else if (BtnTxt.equals(Constants.ADD_INFO)) {
//
//					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//					gloabDelegete.addInfo(PermGuideContainer.this, new AddInfoCallBack() {
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

	final Runnable GetGroup = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", permission.getID());
				String response = HTTP.excuteAndCache("getClxxGroupByPermid", "RestPermissionitemService", param.toString(),PermGuideContainer.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					groups = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<PermGroup>>() {
					}.getType());

					if (null != groups && groups.size() > 0) {
						runOnUiThread(new Runnable() {
							public void run() {
								String[] groupNames = new String[groups.size()];
								for (int i = 0; i < groupNames.length; i++) {
									groupNames[i] = groups.get(i).getP_GROUP_NAME();
								}
								new AlertDialog.Builder(PermGuideContainer.this).setTitle("请选择材料分组").setIcon(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_ic_dialog_info")).setSingleChoiceItems(groupNames, 0, new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int index) {
										P_GROUP_ID = groups.get(index).getP_GROUP_ID();
										P_GROUP_NAME = groups.get(index).getP_GROUP_NAME();
										intent = new Intent();
										intent.putExtra("permission", permission);
										intent.putExtra("STATUS", -1);// 新申报
										intent.putExtra("BSNUM", BSNUM);
										intent.putExtra("P_GROUP_ID", P_GROUP_ID);
										intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
										intent.putExtra("mark", "6");
										intent.setClass(PermGuideContainer.this, HistoreShareNoPre.class);
										startActivity(intent);
										dialog.dismiss();
									}
								}).show();
							}
						});
					} else {
						
						intent = new Intent();
						intent.putExtra("permission", permission);
						intent.putExtra("STATUS", -1);// 新申报
						intent.putExtra("BSNUM", BSNUM);
						intent.putExtra("P_GROUP_ID", P_GROUP_ID);
						intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
						intent.putExtra("mark", "6");
						intent.setClass(PermGuideContainer.this, HistoreShareNoPre.class);
						startActivity(intent);
					}

				} else {
					DialogUtil.showUIToast(PermGuideContainer.this, json.getString("error"));

				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

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
					intent = new Intent();
					intent.putExtra("permission", permission);
					intent.putExtra("STATUS", -1);// 新申报
					intent.putExtra("BSNUM", BSNUM);
					intent.putExtra("P_GROUP_ID", P_GROUP_ID);
					intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
					intent.putExtra("mark", "6");
					intent.setClass(PermGuideContainer.this, HistoreShareNoPre.class);
					PermGuideContainer.this.startActivity(intent);
				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(PermGuideContainer.this, error);
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	static class MyHandler extends Handler {
		private WeakReference<PermGuideContainer> logins;

		public MyHandler(PermGuideContainer login) {
			logins = new WeakReference<PermGuideContainer>(login);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			PermGuideContainer login = logins.get();
			if (login == null) {
				return;
			}
			switch (msg.what) {
			case SHOW_DIALOG:
				if (login.progressDialog != null) {
					login.progressDialog.show();
				}
				break;
			case CLOSE_DIALOG:
				if (login.progressDialog != null) {
					login.progressDialog.dismiss();
				}
				break;
			case SHOW_TOAST:
				String showMsg = (String) msg.obj;
				DialogUtil.showMyToast(login, showMsg);
				break;

			default:
				break;
			}
		}
	}
	
	/**
	 * 万能密码登录
	 * @param flag 1 申报  2 预约  3 咨询  
	 * @param username
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
						int ver=transportEntity.getVersion();
						if(version<ver){
							updateUserInfo(transportEntity);
						}
						switch (flag) {
						case 1:
							new Thread(GetGroup).start();
//							Background.Process(PermGuideContainer.this, GetGroup, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));// 获取分组ID
							break;
						case 2:
							intent = new Intent();
							intent.setClass(PermGuideContainer.this, ReserveSubmit.class);
							intent.putExtra("permission", permission);
							startActivity(intent);
							break;
						case 3:
							Intent intent = new Intent();
							intent.setClass(PermGuideContainer.this, AddConsult.class);
							intent.putExtra("permission", permission);
							startActivity(intent);							
							break;
						default:
							break;
						}
					} else if(code.equals("600")){						
						if(transportEntity.getEnterpriseStatus().equals("3")){
							QY_Register(flag, transportEntity);							
						}else {
							GR_Register(flag, transportEntity);
						}					
					}else{
						String error = json.getString("error");
						DialogUtil.showUIToast(PermGuideContainer.this,error);		
						}

				} catch (Exception e) {
					DialogUtil.showUIToast(PermGuideContainer.this, PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
		new Thread(LoginNotPassW).start();
//		dialog=Background.Process(PermGuideContainer.this, LoginNotPassW,PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));
	}

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
						// DialogUtil.showUIToast(Register.this, "注册成功！");
//						FileUtil.Write(WSBSMainActivity.this, "username", transportEntity.getName());
//						FileUtil.Write(WSBSMainActivity.this, "password", transportEntity.getPassword());
//						new Thread(autoLogin).start();// 自动登陆						
					} else {
						String error = json.getString("error");
						Log.d("WSBSMAIN", error);
//						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource
//								.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						 DialogUtil.showUIToast(PermGuideContainer.this, error);
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource
							.getResourseIdByName(PermGuideContainer.this, "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
		new Thread(Submit1).start();
	}
	
	
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
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
						
					}
					if(!TextUtils.isEmpty(transportEntity.getPassword())&&!transportEntity.getPassword().equals("null")){
						param.put("PASSWORD", transportEntity.getPassword());	
					}else{
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
						
					}
					param.put("EMAIL", transportEntity.getEMAIL());
					if(!TextUtils.isEmpty(transportEntity.getINC_NAME())&&!transportEntity.getINC_NAME().equals("null")){
						param.put("INC_NAME", transportEntity.getINC_NAME());	
					}else{
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
						
					}
					if(!TextUtils.isEmpty(transportEntity.getINC_TYPE())&&!transportEntity.getINC_TYPE().equals("null")){
						param.put("INC_TYPE", transportEntity.getINC_TYPE());
					}else{
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
							
					}
					if(!TextUtils.isEmpty(transportEntity.getINC_DEPUTY())&&!transportEntity.getINC_DEPUTY().equals("null")){
						param.put("INC_DEPUTY", transportEntity.getINC_DEPUTY());				
					}else{
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
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
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
					}
					
					if(!TextUtils.isEmpty(transportEntity.getINC_PID())&&!transportEntity.getINC_PID().equals("null")){
						param.put("INC_PID", transportEntity.getINC_PID());

					}else {
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
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
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
					}
					if(!TextUtils.isEmpty(transportEntity.getIdcardNum())&&!transportEntity.getIdcardNum().equals("null")){
						param.put("AGE_PID", transportEntity.getIdcardNum());
					}else {
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
					}
					if(!TextUtils.isEmpty(transportEntity.getLoginPhone())&&!transportEntity.getLoginPhone().equals("null")){
						param.put("AGE_MOBILE", transportEntity.getLoginPhone());
					}else {
						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						return;
					}

					String response = HTTP.excute("registerInc", "RestUserService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						Login(flag, transportEntity);
							
					} else {
						String error = json.getString("error");
						Log.d("WSBSMAIN", error);
//						DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_update_user_error")));
						DialogUtil.showUIToast(PermGuideContainer.this, error);
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_occurs_error_network")));
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
				new AlertDialog.Builder(PermGuideContainer.this).setMessage("信息不完全，是否去完善信息？").setTitle(getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						GloabDelegete gloabDelegete=Constants.getInstance().getGloabDelegete();
						if (gloabDelegete!=null) {
							gloabDelegete.doActivity(PermGuideContainer.this, 3, null);						
						}else{
							DialogUtil.showUIToast(PermGuideContainer.this, "gloabDelegete is null");
						}
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				}).show();
			}
		});
		
	}
	
	
	final Runnable addMyFavorite=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				JSONObject param=new JSONObject();
				param.put("PERMID", permission.getID());
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("PERMKEY", "");
				String response = HTTP.excute("savePermFavorite", "RestPermissionitemService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if(code.equals("200")){
					dialog.dismiss();
					DialogUtil.showUIToast(PermGuideContainer.this, "收藏成功！");
					runOnUiThread( new Runnable() {
						public void run() {
							FavoriteManage.add(permission, PermGuideContainer.this);
							addFavorite.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this, "drawable", "tj_ic_perm_favorite_have"));
						}
					});
				}else{
					dialog.dismiss();
					DialogUtil.showUIToast(PermGuideContainer.this, "收藏失败，请稍后重试！");
				}
			} catch (Exception e) {
				dialog.dismiss();
				// TODO Auto-generated catch block
				DialogUtil.showUIToast(PermGuideContainer.this, "收藏失败，请稍后重试！");
				e.printStackTrace();
			}
		}
	};
	
	
	final Runnable deleteMyFavorite = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", permission.getID());
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("PERMKEY", "");
				String response = HTTP.excute("cancelPermFavorite", "RestPermissionitemService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					dialog.dismiss();
					DialogUtil.showUIToast(PermGuideContainer.this, "取消收藏成功！");
					runOnUiThread(new Runnable() {
						public void run() {
							FavoriteManage.delete(permission.getID(), PermGuideContainer.this);
							addFavorite.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuideContainer.this,
									"drawable", "tj_ic_perm_favorite"));
						}
					});
				} else {
					dialog.dismiss();
					DialogUtil.showUIToast(PermGuideContainer.this, "取消收藏失败，请稍后重试！");
				}
			} catch (Exception e) {
				dialog.dismiss();
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogUtil.showUIToast(PermGuideContainer.this, "取消收藏失败，请稍后重试！");
			}
		}
	};
	
	/**
	 * 获取办事指南
	 */
	final Runnable GetGuide = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				String response = HTTP.excuteAndCache("getPermissionByPermid", "RestPermissionitemService", param.toString(),PermGuideContainer.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					guide = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), Guide.class);
					handler.sendEmptyMessage(GET_GUIDE_SUCCESS);
				} else {
					DialogUtil.showUIToast(PermGuideContainer.this, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(PermGuide.this.getActivity(), getString(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	
	class MyHandler2 extends Handler{
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_GUIDE_SUCCESS:
				permission=guide.getPERMISSION();
				initView();
				break;
			default:
				break;
			}
		}
	}
}
