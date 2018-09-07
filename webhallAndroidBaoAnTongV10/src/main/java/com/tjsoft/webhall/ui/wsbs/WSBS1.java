package com.tjsoft.webhall.ui.wsbs;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.CaptureActivity;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.JsonParser;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.util.VoiceSearchUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.PermListByDB;
import com.tjsoft.webhall.entity.CooperateVo;
import com.tjsoft.webhall.entity.Dept;
import com.tjsoft.webhall.entity.Icon;
import com.tjsoft.webhall.entity.Region;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;
import com.tjsoft.webhall.lib.ListViewForScrollView;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.search.PermListByName;
import com.tjsoft.webhall.ui.search.Search;
import com.tjsoft.webhall.ui.user.Login;
import com.tjsoft.webhall.ui.user.LoginUtil;
import com.tjsoft.webhall.ui.user.Register;


/**
 * // 个人办事主页面
 * 
 * @author Administrator
 * 
 */
public class WSBS1 extends AutoDialogActivity {
	private List<Map<String, Object>> deptDatas;//部门列表
	private List<Map<String, Object>> listItemDeclared; // 街道列表
	private List<Map<String, Object>> ZQdeptDatas; // 驻区部门列表
	private ListViewForScrollView deptList;//部门列表list
	private ListViewForScrollView StreetList;//街道列表list
	private ListViewForScrollView ZQDeptList;//驻区部门列表list
	private ListView xtbsList;
	public static List<Region> regions;
	public List<CooperateVo> cooperateVos;
	private List<Dept> depts;//区值部门列表
	private List<Dept> ZQdepts;//驻区部门列表
	private List<Dept> strees;//街道列表
	public static String[] regionNames;
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView  textView2, textView3;
	private List<TextView> titles;
	private List<View> views;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW = 0;// 动画图片宽度
	private View grbs, qybs, bmfw, xtbs;// 各个页卡
	private GridView gridView1, gridView2;
	private DisplayMetrics dm;// 设备管理
	// private Button search;
	private MyHandler handler = new MyHandler();
	private final int GET_REGION_SUCCESS = 1;
	private final int GET_DEPTS_SUCCESS = 2;
	private final int GET_ICON_SUCCESS = 3;
	private final int GET_STREETS_SUCCESS=5;
	private final int GET_ZQDEPTS_SUCCESS=4;
	private Intent intent;
	private LayoutInflater inflater;
	private int viewPageNo;
	private LinearLayout chooseArea;
	private TextView areaTv;
	private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true).build();
	public static List<Icon> icons, icons_declare, icons_declare_grbs, icons_declare_qybs, icons_grbs, icons_grbs_ztfl, icons_grbs_grsj, icons_grbs_tdrq, icons_qybs, icons_qybs_qyzt, icons_qybs_qysj, icons_qybs_tdqy, icons_zmhd, icons_bmfw;
	private Context mContext;
	
//	private ListView declaredList;
	private Button mMenu, btnSearch;
	private EditText textSearch;
	private ImageButton btnMore;
	private RelativeLayout back;
	private RadioGroup radioGroup,  title_radio_group;
	private ProgressDialog progressDialog;
	private final static int SHOW_DIALOG = 1; // 登录时显示进度对话框
	private final static int CLOSE_DIALOG = 2; // 关闭进度对话框
	private final static int SHOW_TOAST = 3; // 显示提示框
	private MyHandler myHandler;
	private String username;
	private String password;
	private GridView canDeclareList1, canDeclareList2;// 可申报gridView
	private ScrollView canDeclareLayout;
	private LinearLayout allPerms;// 全部事项
	private boolean SFYDSB = true;

	private ImageView voice;
	private VoiceSearchUtil mVoice;
	private Button home;
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_wsbs_gr"));
		Constants.getInstance().addActivity(this);
		mContext = this;
		InitView();// 初始化可申报事项和全部事项
		InitImageView();
		InitTextView();
		InitViewPager();
		chooseArea = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "chooseArea"));
		areaTv = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "area_tv"));
		initSetListener();
		dialog = Background.Process(WSBS1.this, InitParentArea, "数据加载中...");

//		if (Constants.WSBS_PATH == 1) {
//			areaTv.setText("选择办理事项（1/5）");
//
//		} else if (Constants.WSBS_PATH == 2) {
//			areaTv.setText("选择办理事项（1/3）");
//		}
		mVoice=new VoiceSearchUtil(this, textSearch,btnSearch);

		voice=(ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "voice_iv"));
		voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mVoice.startListenVoice();
			}
		});
        
	}

	private void InitView() {
		canDeclareList1 = (GridView) findViewById(MSFWResource.getResourseIdByName(this, "id", "canDeclareList1"));
		canDeclareList2 = (GridView) findViewById(MSFWResource.getResourseIdByName(this, "id", "canDeclareList2"));
		canDeclareLayout = (ScrollView) findViewById(MSFWResource.getResourseIdByName(this, "id", "canDeclareLayout"));
		title_radio_group = (RadioGroup) findViewById(MSFWResource.getResourseIdByName(this, "id", "title_radio_group"));
		allPerms = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "allPerms"));

		final int title_rb1_id = MSFWResource.getResourseIdByName(this, "id", "title_rb1");
		final int title_rb2_id = MSFWResource.getResourseIdByName(this, "id", "title_rb2");
		final RadioButton trb1 = (RadioButton) findViewById(title_rb1_id);
		final RadioButton trb2 = (RadioButton) findViewById(title_rb2_id);
		title_radio_group = (RadioGroup) findViewById(MSFWResource.getResourseIdByName(this, "id", "title_radio_group"));
		title_radio_group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				if (checkedId == title_rb1_id) {
					SFYDSB = true;
					trb1.setTextColor(Color.BLACK);
					trb2.setTextColor(Color.WHITE);
					allPerms.setVisibility(View.GONE);
					canDeclareLayout.setVisibility(View.VISIBLE);
				}

				else if (checkedId == title_rb2_id) {
					SFYDSB = false;
					trb1.setTextColor(Color.WHITE);
					trb2.setTextColor(Color.BLACK);
					allPerms.setVisibility(View.VISIBLE);
					canDeclareLayout.setVisibility(View.GONE);
				}
			}
		});
		trb2.setChecked(true);// 默认选中

	}

	private void initSetListener() {
		/*
		 * chooseArea.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (null != regions &&
		 * regions.size() > 0 && null != regions.get(0)) { Intent intent = new
		 * Intent(); intent.setClass(WSBS.this, ChooseArea.class);
		 * intent.putExtra("AREAID", regions.get(0).getAREAID());
		 * intent.putExtra("parent", regions.get(0));
		 * startActivityForResult(intent, 101); } } });
		 */
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WSBS1.this.finish();
			}
		});
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		home.setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "tj_home"));
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.getInstance().exit();
			}
		});
		mMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopLeft(mMenu);
			}
		});
		btnMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showMore(btnMore);
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(textSearch.getText().toString())) {
					DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_search_empty_notice")));
				} else {
					intent = new Intent();
					intent.setClass(WSBS1.this, PermListByName.class);
					intent.putExtra("name", textSearch.getText().toString().trim());
					startActivity(intent);
				}

			}
		});
	}

	/**
	 * 显示更多
	 * 
	 * @param v
	 */

	private void showMore(View v) {
		View view = LayoutInflater.from(this).inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_popup_wsbs_more"), null);
		LinearLayout layoutSearch = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "layoutSearch"));
		LinearLayout layoutReserve = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "layoutReserve"));
		LinearLayout layoutWDBJ = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "layoutWDBJ"));
		LinearLayout layoutFavorit = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "layoutFavorit"));
		layoutSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(WSBS1.this, Search.class));

			}
		});
		layoutReserve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == Constants.user) {
					LoginUtil util = new LoginUtil();
					util.checkLogin(WSBS1.this);
				} else {
					startActivity(new Intent().setClass(WSBS1.this, ReserveList.class));
				}

			}
		});
		layoutWDBJ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == Constants.user) {
					LoginUtil util = new LoginUtil();
					util.checkLogin(WSBS1.this);
				} else {
					startActivity(new Intent().setClass(WSBS1.this, WDBJ.class));
				}

			}
		});
		layoutFavorit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(WSBS1.this, PermListByDB.class));

			}
		});
		PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(v, DensityUtil.dip2px(this, -90), 0);
	}

	/**
	 * 显示菜单
	 * 
	 * @param v
	 */

	private void showPopLeft(View v) {
		View view = LayoutInflater.from(this).inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_wsbs_menu"), null);
		Button favorite = (Button) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "wsbs_favorite"));
		Button search = (Button) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "wsbs_search"));
		Button dealwith = (Button) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "wsbs_dealwith"));
		Button reserve = (Button) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "wsbs_reserve"));
		Button erweima = (Button) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "wsbs_erweima"));
		reserve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WSBS1.this, ReserveList.class);
				startActivity(intent);
			}
		});
		favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WSBS1.this, PermListByDB.class);
				startActivity(intent);
			}
		});
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WSBS1.this, Search.class);
				startActivity(intent);
			}
		});
		dealwith.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LoginBaoAnTongUtil.checkLogin(WSBS1.this);
//				if (!Constants.DEBUG_TOGGLE) {
//					/**
//					 * 判断是否登录MST
//					 */
//					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//					if (gloabDelegete != null) {
//						TransportEntity transportEntity = gloabDelegete.get();
//						if (!transportEntity.isLoginStatus()) {
//							gloabDelegete.loginMST(WSBS1.this);
//							return;
//						} else if (TextUtils.isEmpty(transportEntity.getAccount())) {
//							isLoginAlert();
//							return;
//						} else if (TextUtils.isEmpty(transportEntity.getPassword())) {
//							intent = new Intent();
//							intent.setClass(WSBS1.this, Login.class);
//							intent.putExtra("account", transportEntity.getAccount());
//							startActivity(intent);
//							return;
//						} else {
//							if (null == Constants.user || !Constants.user.getUSERNAME().equals(transportEntity.getAccount())) {
//								username = transportEntity.getAccount();
//								password = transportEntity.getPassword();
//								Background.Process(WSBS1.this, Login, "正在登录...");
//								return;
//							}
//						}
//
//					} else {
//						return;
//					}
//					if (null == Constants.user) {
//						isLoginAlert();
//						return;
//					}
//				} else {
//					if (null == Constants.user) {
//						intent = new Intent();
//						intent.setClass(WSBS1.this, Login.class);
//						startActivity(intent);
//						return;
//					}
//				}

				Intent intent = new Intent();
				intent.setClass(WSBS1.this, WDBJ.class);
				startActivity(intent);
			}
		});
		erweima.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(WSBS1.this, CaptureActivity.class);
				intent.putExtra("flag", 1);
				startActivity(intent);
			}
		});

		PopupWindow popupWindow = new PopupWindow(view, DensityUtil.dip2px(this, 268), DensityUtil.dip2px(this, 50));

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置popWindow的显示和消失动画
		// popupWindow.setAnimationStyle(R.style.tj_AnimBottom);

		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - popupWindow.getWidth(), location[1]);
	}

	@Override
	protected void onStop() {

		super.onStop();
	}

	@Override
	public void onBackPressed() {
		this.onDestroy();
		System.gc();
		super.onBackPressed();
	}

	class GridViewAdapter extends BaseAdapter {

		private List<Icon> icons;

		public GridViewAdapter(List<Icon> icons) {
			super();
			this.icons = icons;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return icons.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return icons.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuItem item;
			if (null == convertView) {
				item = new MenuItem();
				convertView = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_menu_item"), null);
				item.bg = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "bg"));
				item.name = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "name"));
				item.code=(TextView)convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "code"));
				convertView.setTag(item);
			} else {
				item = (MenuItem) convertView.getTag();
			}

			ImageLoader.getInstance().displayImage(Constants.DOMAIN + getNewUrl(icons.get(position).getPICTUREPATH()), item.bg, options);
//			RelativeLayout.LayoutParams iconSize = new RelativeLayout.LayoutParams(dm.widthPixels / 12, dm.widthPixels / 12);
//			item.bg.setLayoutParams(iconSize);
			item.name.setText(icons.get(position).getPICTURENAME() + "");
			if(!TextUtils.isEmpty(icons.get(position).getPERMCODE())&&!icons.get(position).getPERMCODE().equals("null")){
				item.code.setText(icons.get(position).getPERMCODE()+"");
			}else{
				item.code.setVisibility(View.GONE);
			}
			convertView.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "bg_gv"));
			return convertView;

		}

		public final class MenuItem {

			ImageView bg;
			TextView name;
			TextView code;
		}

	}

	/**
	 * 将版本路径改成新版本路径 2016-4-21 fuchl
	 */
	private String getNewUrl(String old) {
		String[] oldUrl = old.split("/");
		String newUrl = "";
		for (int i = 0; i < oldUrl.length; i++) {
			if (i == oldUrl.length - 1) {
				oldUrl[i] = "new/" + oldUrl[i];
			}
			newUrl = newUrl + "/" + oldUrl[i];
		}
		newUrl = newUrl.substring(1, newUrl.length());
		
		return newUrl;
	}

	private void InitViewPager() {
		viewPageNo = getIntent().getIntExtra("viewPageNo", 0);

		viewPager = (ViewPager) findViewById(MSFWResource.getResourseIdByName(this, "id", "vPager"));
		views = new ArrayList<View>();
		inflater = getLayoutInflater();
		grbs = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_grbs"), null);
		bmfw = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "tj_department"), null);
	
		views.add(grbs);
		views.add(bmfw);
	

		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(viewPageNo);
		Animation animation = new TranslateAnimation(0, offset + viewPageNo * (bmpW + 2 * offset), 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(1);
		imageView.startAnimation(animation);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		gridView1 = (GridView) grbs.findViewById(MSFWResource.getResourseIdByName(this, "id", "gridView"));
		radioGroup = (RadioGroup) grbs.findViewById(MSFWResource.getResourseIdByName(this, "id", "radio_group"));
		final int rb1_id = MSFWResource.getResourseIdByName(this, "id", "rb1");
		final int rb2_id = MSFWResource.getResourseIdByName(this, "id", "rb2");
		final int rb3_id = MSFWResource.getResourseIdByName(this, "id", "rb3");

		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb1_id) {
					gridView1.setAdapter(new GridViewAdapter(icons_grbs_ztfl));
					gridView1.setOnItemClickListener(new GridViewClick(icons_grbs_ztfl));
				} else if (checkedId == rb2_id) {
					gridView1.setAdapter(new GridViewAdapter(icons_grbs_grsj));
					gridView1.setOnItemClickListener(new GridViewClick(icons_grbs_grsj));

				} else if (checkedId == rb3_id) {
					gridView1.setAdapter(new GridViewAdapter(icons_grbs_tdrq));
					gridView1.setOnItemClickListener(new GridViewClick(icons_grbs_tdrq));

				}
			}
		});
		
		deptList = (ListViewForScrollView) bmfw.findViewById(MSFWResource.getResourseIdByName(this, "id", "deptList"));
		ImageView empty = (ImageView) bmfw.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		deptList.setEmptyView(empty);
		StreetList = (ListViewForScrollView) bmfw.findViewById(MSFWResource.getResourseIdByName(this, "id", "StreetList"));
		ImageView empty1 = (ImageView) bmfw.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty1"));
		StreetList.setEmptyView(empty1);
		
		ZQDeptList = (ListViewForScrollView) bmfw.findViewById(MSFWResource.getResourseIdByName(this, "id", "QZdeptList"));
		ImageView empty2 = (ImageView) bmfw.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty2"));
		ZQDeptList.setEmptyView(empty2);
		mMenu = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "wsbs_menu"));
		btnSearch = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnSearch"));
		textSearch = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "textSearch"));
		btnMore = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnMore"));
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
	}

	/**
	 * 初始化头标
	 */

	private void InitTextView() {
		textView2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text2"));
		textView3 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "text3"));

		titles = new ArrayList<TextView>();
		titles.add(textView2);
		titles.add(textView3);
		

		textView2.setOnClickListener(new MyOnClickListener(0));
		textView3.setOnClickListener(new MyOnClickListener(1));

	}

	/**
	 * 2 * 初始化动画 3
	 */

	private void InitImageView() {
		imageView = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "cursor"));
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = DensityUtil.dip2px(this, 14);// 计算偏移量
		bmpW = (screenW / 2 - 2 * offset);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bmpW, DensityUtil.dip2px(this, 2));
		imageView.setLayoutParams(params);
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);

	}

	/**
	 * 
	 * 头标点击监听 3
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}

	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;
		int two = one * 2;

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int position) {
			Animation animation = new TranslateAnimation(one * currIndex + offset, one * position + offset, 0, 0);
			currIndex = position;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			for (int i = 0; i < titles.size(); i++) {
				titles.get(i).setTextColor(MSFWResource.getResourseIdByName(WSBS1.this, "color", "tj_tab_text"));
			}
			titles.get(position).setTextColor(WSBS1.this.getResources().getColor(MSFWResource.getResourseIdByName(WSBS1.this, "color", "tj_my_green")));
			System.out.println("fuchl   onPageSelected   " + position);
			if (position == 1) {
				dialog = Background.Process(WSBS1.this, GetDeptList, "数据加载中");
				dialog = Background.Process(WSBS1.this, GetZQDeptList, "数据加载中");
				dialog = Background.Process(WSBS1.this, GetZJList, "数据加载中");
			}
		}

	}

	/**
	 * 初始化全部事项图标
	 */
	final Runnable InitIcon = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("ID", Constants.AREAID);
				String response = HTTP.excuteAndCache("getPictureByID", "RestUnitPortalService", param.toString(),WSBS1.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					icons = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Icon>>() {
					}.getType());
					handler.sendEmptyMessage(GET_ICON_SUCCESS);
				} else {
					DialogUtil.showUIToast(WSBS1.this, json.getString("error"));
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};
	/**
	 * 初始化可申报图标
	 */
	final Runnable InitDeclareIcon = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("ID", Constants.AREAID);
				param.put("SFYDSB", "1");
				String response = HTTP.excuteAndCache("getPictureByID", "RestUnitPortalService", param.toString(),WSBS1.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					icons_declare = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Icon>>() {
					}.getType());
					if (null != icons_declare) {
						icons_declare_grbs = new ArrayList<Icon>();
						icons_declare_qybs = new ArrayList<Icon>();
						for (int i = 0; i < icons_declare.size(); i++) {
							Icon temp = icons_declare.get(i);
							if (temp.getPICTURETYPE().equals("1")) {
								icons_declare_grbs.add(temp);
							} else if (temp.getPICTURETYPE().equals("2")) {
								icons_declare_qybs.add(temp);
							}

						}
					}
					runOnUiThread(new Runnable() {
						public void run() {
							canDeclareList1.setAdapter(new GridViewAdapter(icons_declare_grbs));
							canDeclareList1.setOnItemClickListener(new GridViewClick(icons_declare_grbs));
							canDeclareList2.setAdapter(new GridViewAdapter(icons_declare_qybs));
							canDeclareList2.setOnItemClickListener(new GridViewClick(icons_declare_qybs));
						}
					});
				} else {
					DialogUtil.showUIToast(WSBS1.this, json.getString("error"));
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

	/**
	 * 获取镇街服务
	 */
	final Runnable GetZJList = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("AREAID", Constants.AREAID);
				param.put("PAGENO", "1");
				param.put("RESERVEONE", "5");
				param.put("PAGESIZE", "1000");

				String response = HTTP.excuteAndCache("getDeptlistByAreaid", "RestRegionService", param.toString(),WSBS1.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					strees = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());

					listItemDeclared = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < strees.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DEPTID", strees.get(i).getDEPTID());
						map.put("SHORTNAME", strees.get(i).getSHORTNAME());
						map.put("AREAID", strees.get(i).getAREAID());
						map.put("ORDERID", strees.get(i).getORDERID());
						map.put("CNUM", strees.get(i).getCNUM());
						map.put("NAME", strees.get(i).getNAME());
//						map.put("NAME", strees.get(i).getNAME()+"（"+strees.get(i).getPERMNUM()+"）");
						listItemDeclared.add(map);

					}

					handler.sendEmptyMessage(GET_STREETS_SUCCESS);

				} else {
					DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	/**
	 * 获取驻区部门服务
	 */
	final Runnable GetZQDeptList = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("AREAID", Constants.AREAID);
				param.put("PAGENO", "1");
				param.put("RESERVEONE", "3");
				param.put("PAGESIZE", "1000");

				String response = HTTP.excuteAndCache("getDeptlistByAreaid", "RestRegionService", param.toString(),WSBS1.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					ZQdepts = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());

					ZQdeptDatas = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < ZQdepts.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DEPTID", ZQdepts.get(i).getDEPTID());
						map.put("SHORTNAME", ZQdepts.get(i).getSHORTNAME()+"（"+ZQdepts.get(i).getPERMNUM()+"）");
						map.put("AREAID", ZQdepts.get(i).getAREAID());
						map.put("ORDERID", ZQdepts.get(i).getORDERID());
						map.put("CNUM", ZQdepts.get(i).getCNUM());
						map.put("NAME", ZQdepts.get(i).getNAME());
						ZQdeptDatas.add(map);

					}

					handler.sendEmptyMessage(GET_ZQDEPTS_SUCCESS);

				} else {
					DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	/**
	 * 获取部门
	 */
	final Runnable GetDeptList = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("AREAID", Constants.AREAID);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				param.put("RESERVEONE", "4");
				if (currIndex == 1) {
					param.put("SFYDSB", "0");
				} else {
					param.put("SFYDSB", "1");
				}
				String response = HTTP.excuteAndCache("getDeptlistByAreaid", "RestRegionService", param.toString(),WSBS1.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					depts = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());

					if (currIndex == 1) { // 部门事项部门列表
						deptDatas = new ArrayList<Map<String, Object>>();
						for (int i = 0; i < depts.size(); i++) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("DEPTID", depts.get(i).getDEPTID());
							map.put("SHORTNAME", depts.get(i).getSHORTNAME());
							map.put("AREAID", depts.get(i).getAREAID());
							map.put("ORDERID", depts.get(i).getORDERID());
							map.put("CNUM", depts.get(i).getCNUM());
							map.put("NAME", depts.get(i).getNAME()+"（"+depts.get(i).getPERMNUM()+"）");
							deptDatas.add(map);
						}
					} 
//					else { // 可申报事项部门列表
//						listItemDeclared = new ArrayList<Map<String, Object>>();
//						for (int i = 0; i < depts.size(); i++) {
//							Map<String, Object> map = new HashMap<String, Object>();
//							map.put("DEPTID", depts.get(i).getDEPTID());
//							map.put("SHORTNAME", depts.get(i).getSHORTNAME());
//							map.put("AREAID", depts.get(i).getAREAID());
//							map.put("ORDERID", depts.get(i).getORDERID());
//							map.put("CNUM", depts.get(i).getCNUM());
//							map.put("NAME", depts.get(i).getNAME() + "	(" + depts.get(i).getCNUM() + ")");
//							listItemDeclared.add(map);
//						}
//					}

					handler.sendEmptyMessage(GET_DEPTS_SUCCESS);

				} else {
					DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	/**
	 * 获取协同流程列表
	 */
	final Runnable GetCooperateFlowList = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				String response = HTTP.excuteAndCache("getCooperateFlowList", "RestCooperateService", param.toString(),WSBS1.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					cooperateVos = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<CooperateVo>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {

							List<Map<String, Object>> cooperateDatas = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < cooperateVos.size(); i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("CFLOWID", cooperateVos.get(i).getCFLOWID());
								map.put("CFLOWNAME", cooperateVos.get(i).getCFLOWNAME());
								map.put("REMARK", cooperateVos.get(i).getREMARK());
								cooperateDatas.add(map);
							}

							SimpleAdapter adapter = new SimpleAdapter(WSBS1.this, cooperateDatas, MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"), new String[] { "CFLOWNAME" }, new int[] { MSFWResource.getResourseIdByName(WSBS1.this, "id", "deptName") });
							xtbsList.setAdapter(adapter);
							xtbsList.setOnItemClickListener(new MyItemClick());
						}
					});

				} else {
					DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {

		case 101:
			// areaTv.setText(Constants.AREANAME);
			dialog = Background.Process(WSBS1.this, GetDeptList, "数据加载中");
			dialog = Background.Process(WSBS1.this, InitIcon, "数据加载中");
			dialog = Background.Process(WSBS1.this, InitDeclareIcon, "数据加载中");
			break;

		default:
			break;
		}
	};

	/**
	 * 初始化区域父节点
	 */
	final Runnable InitParentArea = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PARENTID", "");
				String response = HTTP.excuteAndCache("getRegionlistByParentid", "RestRegionService", param.toString(),WSBS1.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					regions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Region>>() {
					}.getType());
					handler.sendEmptyMessage(GET_REGION_SUCCESS);
				} else {
					DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};
	/**
	 * 部门点击事件
	 * @author S_Black
	 *
	 */
	private class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			if (currIndex == 1) {
				intent.setClass(WSBS1.this, PermListByDept.class);
				intent.putExtra("name", deptDatas.get(position).get("NAME").toString());
				intent.putExtra("DEPTID", deptDatas.get(position).get("DEPTID").toString());
				intent.putExtra("SFYDSB", "0");
				intent.putExtra("WSBSFLAG", 1);
			} else {
				intent.setClass(WSBS1.this, DeptList.class);
				intent.putExtra("dept", strees.get(position));

			}
			startActivity(intent);

		}

	}
	/**
	 * 驻区部门点击事件
	 * @author S_Black
	 *
	 */
	private class MyItemClick1 implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
				intent.setClass(WSBS1.this, PermListByDept.class);
				intent.putExtra("name", ZQdeptDatas.get(position).get("NAME").toString());
				intent.putExtra("DEPTID", ZQdeptDatas.get(position).get("DEPTID").toString());
				intent.putExtra("SFYDSB", "0");
				intent.putExtra("WSBSFLAG", 1);
				intent.putExtra("fromflag", 1);
			startActivity(intent);

		}

	}
	/**
	 * 街道点击事件
	 * @author S_Black
	 *
	 */
	private class MyItemClick2 implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(WSBS1.this, DeptList.class);
			intent.putExtra("dept", strees.get(position));
			intent.putExtra("WSBSFLAG", 1);

			startActivity(intent);

		}

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_REGION_SUCCESS:
				if (null != regions && regions.size() > 0 && null != regions.get(0) && "".equals(Constants.AREAID)) {// Constants.AREAID.endsWith("")
																														// 判断是否初始化过
					Constants.AREAID = regions.get(0).getAREAID();
					Constants.AREANAME = regions.get(0).getAREANAME();
				}
				// areaTv.setText(Constants.AREANAME);

				dialog = Background.Process(WSBS1.this, InitIcon, "数据加载中");// 更新图标
				dialog = Background.Process(WSBS1.this, InitDeclareIcon, "数据加载中");// 更新图标
				break;
			case GET_DEPTS_SUCCESS:

				if (currIndex == 1) {
					SimpleAdapter adapter = new SimpleAdapter(WSBS1.this, deptDatas, MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"), new String[] { "NAME" }, new int[] { MSFWResource.getResourseIdByName(mContext, "id", "deptName") });
					deptList.setAdapter(adapter);
					deptList.setOnItemClickListener(new MyItemClick());
					
				}

				break;
			case GET_ICON_SUCCESS:
				if (null != icons && icons.size() > 0) {
					icons_grbs = new ArrayList<Icon>();
					icons_grbs_ztfl = new ArrayList<Icon>();
					icons_grbs_grsj = new ArrayList<Icon>();
					icons_grbs_tdrq = new ArrayList<Icon>();
					icons_qybs = new ArrayList<Icon>();
					icons_qybs_qyzt = new ArrayList<Icon>();
					icons_qybs_qysj = new ArrayList<Icon>();
					icons_qybs_tdqy = new ArrayList<Icon>();
					icons_zmhd = new ArrayList<Icon>();
					icons_bmfw = new ArrayList<Icon>();
					for (int i = 0; i < icons.size(); i++) {
						String picturetype = icons.get(i).getPICTURETYPE();
						Icon icon = icons.get(i);
						if ("1".equals(picturetype)) {// 个人办事
							icons_grbs.add(icon);
						} else if ("2".equals(picturetype)) {// 企业办事
							icons_qybs.add(icon);
						} else if ("3".equals(picturetype)) {// 政民互动
							icons_zmhd.add(icon);
						} else if ("4".equals(picturetype)) {// 便民服务
							icons_bmfw.add(icon);
						}
					}

					for (int i = 0; i < icons_grbs.size(); i++) {
						if (icons_grbs.get(i).getPICTURECODE().contains("root_gr_ztfl")) {// 个人主题
							icons_grbs_ztfl.add(icons_grbs.get(i));
						} else if (icons_grbs.get(i).getPICTURECODE().contains("root_gr_sjfl")) {// 个人事件
							icons_grbs_grsj.add(icons_grbs.get(i));
						} else if (icons_grbs.get(i).getPICTURECODE().contains("root_gr_qtfl")) {// 特定人群
							icons_grbs_tdrq.add(icons_grbs.get(i));
						}
					}
					for (int i = 0; i < icons_qybs.size(); i++) {
						if (icons_qybs.get(i).getPICTURECODE().contains("root_qy_ztfl")) {// 企业主题
							icons_qybs_qyzt.add(icons_qybs.get(i));
						} else if (icons_qybs.get(i).getPICTURECODE().contains("root_qy_sjfl")) {// 企业事件
							icons_qybs_qysj.add(icons_qybs.get(i));
						} else if (icons_qybs.get(i).getPICTURECODE().contains("root_qy_qtfl")) {// 特定企业
							icons_qybs_tdqy.add(icons_qybs.get(i));
						}
					}
					gridView1.setAdapter(new GridViewAdapter(icons_grbs));
					gridView1.setOnItemClickListener(new GridViewClick(icons_grbs));
					

				}

				break;
			case GET_ZQDEPTS_SUCCESS:
				SimpleAdapter adapter2 = new SimpleAdapter(WSBS1.this, ZQdeptDatas, MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"), new String[] { "SHORTNAME" }, new int[] { MSFWResource.getResourseIdByName(mContext, "id", "deptName") });
				ZQDeptList.setAdapter(adapter2);
				ZQDeptList.setOnItemClickListener(new MyItemClick1());
				break;
			case GET_STREETS_SUCCESS:
				SimpleAdapter adapter1 = new SimpleAdapter(WSBS1.this, listItemDeclared, MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"), new String[] { "NAME" }, new int[] { MSFWResource.getResourseIdByName(mContext, "id", "deptName") });
				StreetList.setAdapter(adapter1);
				StreetList.setOnItemClickListener(new MyItemClick2());
				break;
			default:
				break;
			}
		}
	}

	class GridViewClick implements OnItemClickListener {
		private List<Icon> icons;

		public GridViewClick(List<Icon> icons) {
			this.icons = icons;
		}

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			startPermList(icons.get(position).getPICTURECODE(), icons.get(position).getPICTURENAME(),
					icons.get(position).getPERMCODE(),icons.get(position).getPICTUREID());
		}

	}

	private void startPermList(String sortCode, String type,String PermCode,String pictureID) {
		intent = new Intent();
		intent.setClass(this, PermListByCode.class);
		intent.putExtra("SORTCODE", sortCode);
		intent.putExtra("PICTUREID", pictureID);
		intent.putExtra("type", type);
		intent.putExtra("SFYDSB", SFYDSB);
		intent.putExtra("PermCode", PermCode);
		intent.putExtra("WSBSFLAG", 1);
		startActivity(intent);
	}

	private void isLoginAlert() {
		new AlertDialog.Builder(this).setMessage("你还没有绑定网上办事大厅账号，是否现在绑定？").setTitle(getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("绑定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
				if (gloabDelegete != null) {
					gloabDelegete.doBoundMSTAccount(WSBS1.this, username, password, new TransportCallBack() {

						@Override
						public void onStart() {
							myHandler.sendEmptyMessage(SHOW_DIALOG);
						}

						@Override
						public void onResult(int status) {
							myHandler.sendEmptyMessage(CLOSE_DIALOG);
							if (status == 0) {
								Message msg = Message.obtain();
								msg.obj = "绑定成功！";
								msg.what = SHOW_TOAST;
								myHandler.sendMessage(msg);
								try {
									Background.Process(WSBS1.this, Login, "正在登录...");
								} catch (Exception e) {
									e.printStackTrace();
								}
								finish();
							} else {
								Message msg = Message.obtain();
								msg.obj = "绑定失败！";
								msg.what = SHOW_TOAST;
								myHandler.sendMessage(msg);
							}
						}

						@Override
						public void onFinish() {
							myHandler.sendEmptyMessage(CLOSE_DIALOG);
						}
					});
				}
			}
		}).setNegativeButton("注册网上办事账号", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				intent = new Intent();
				intent.setClass(WSBS1.this, Register.class);
				startActivity(intent);
			}
		}).show();
	}

	static class MyHandler2 extends Handler {
		private WeakReference<WSBS1> logins;

		public MyHandler2(WSBS1 login) {
			logins = new WeakReference<WSBS1>(login);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			WSBS1 login = logins.get();
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

	final Runnable Login = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("USERNAME", username);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(username));
				String response = HTTP.excuteAndCache("login", "RestUserService", param.toString(),WSBS1.this);
				final JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
					DialogUtil.showUIToast(WSBS1.this, "登录成功！");
				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(WSBS1.this, error);
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	
}
