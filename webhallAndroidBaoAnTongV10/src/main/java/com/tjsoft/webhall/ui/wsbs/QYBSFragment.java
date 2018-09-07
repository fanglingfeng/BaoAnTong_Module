package com.tjsoft.webhall.ui.wsbs;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tjsoft.camera.FaceAuthActivity;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.util.VoiceSearchUtil;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.PermListByDB;
import com.tjsoft.webhall.entity.CooperateVo;
import com.tjsoft.webhall.entity.Dept;
import com.tjsoft.webhall.entity.Icon;
import com.tjsoft.webhall.entity.Region;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.lib.ListViewForScrollView;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.search.PermListByName;
import com.tjsoft.webhall.ui.search.Search;
import com.tjsoft.webhall.ui.user.LoginUtil;
import com.tjsoft.webhall.ui.xkzkj.XKZKJActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QYBSFragment extends Fragment {
	private List<Map<String, Object>> deptDatas;// 部门列表
	private List<Map<String, Object>> listItemDeclared; // 街道列表
	private List<Map<String, Object>> ZQdeptDatas; // 驻区部门列表
	private ListViewForScrollView deptList;// 部门列表list
	private ListViewForScrollView StreetList;// 街列表list
	private ListViewForScrollView ZQDeptList;// 驻区部门列表list
	private ListView xtbsList;
	public static List<Region> regions;
	public List<CooperateVo> cooperateVos;
	private List<Dept> depts;// 部门列表
	private List<Dept> ZQdepts;// 驻区部门列表
	private List<Dept> strees;// 街道列表
	public static String[] regionNames;
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView textView1, textView3;
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
	private final int GET_ZQDEPTS_SUCCESS = 4;
	private final int GET_STREETS_SUCCESS = 5;
	private Intent intent;
	private LayoutInflater inflater;
	private int viewPageNo;
	private LinearLayout chooseArea;
	private TextView areaTv;
	private DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisc(true).cacheInMemory(true)
			.build();
	public static List<Icon> icons, icons_declare, icons_declare_grbs, icons_declare_qybs, icons_grbs, icons_grbs_ztfl,
			icons_grbs_grsj, icons_grbs_tdrq, icons_qybs, icons_qybs_qyzt, icons_qybs_qysj, icons_qybs_tdqy, icons_zmhd,
			icons_bmfw;
	private Context mContext;
	private ListView declaredList;
	private Button mMenu, btnSearch;
	private EditText textSearch;
	private ImageButton btnMore;
	private RelativeLayout back;
	private RadioGroup radioGroup, radioGroup2, title_radio_group;
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
	private Dialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_wsbs_qy"), container,
				false);
		mContext = getActivity();
		InitView(view);// 初始化可申报事项和全部事项
		InitImageView(view);
		InitTextView(view);
		InitViewPager(view);

		chooseArea = (LinearLayout) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "chooseArea"));
		areaTv = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "area_tv"));
		initSetListener();
		dialog = Background.Process(getActivity(), InitParentArea, "数据加载中...");

		mVoice = new VoiceSearchUtil(getActivity(), textSearch, btnSearch);

		voice = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "voice_iv"));
		voice.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				mVoice.startListenVoice();
				initVoicePermission();

			}
		});

		return view;
	}
	private void initVoicePermission() {
		String permissions[] = {
				Manifest.permission.RECORD_AUDIO,
				Manifest.permission.READ_PHONE_STATE,
				Manifest.permission.ACCESS_NETWORK_STATE,
				Manifest.permission.READ_EXTERNAL_STORAGE,
				Manifest.permission.WRITE_EXTERNAL_STORAGE,
				Manifest.permission.MODIFY_AUDIO_SETTINGS,
				Manifest.permission.ACCESS_WIFI_STATE,
				Manifest.permission.WAKE_LOCK,
				Manifest.permission.RECEIVE_BOOT_COMPLETED
		};

		ArrayList<String> toApplyList = new ArrayList<String>();

		for (String perm : permissions) {
			if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(getActivity(), perm)) {
				toApplyList.add(perm);
				//进入到这里代表没有权限.
			} else {

			}
		}
		String tmpList[] = new String[toApplyList.size()];
		if (!toApplyList.isEmpty()) {
			ActivityCompat.requestPermissions(getActivity(), toApplyList.toArray(tmpList), 123);
		} else {
			mVoice.startListenVoice();

		}

	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
        Constants.user=null;
        mVoice.setCallClick(false);
		GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
		if (gloabDelegete != null) {
			TransportEntity transportEntity = gloabDelegete.getUserInfo();
			System.out.println("fuchl  主页面LOG getIdCardType " + transportEntity.getIdcardType());

			Login(0, transportEntity);
		}
	}
	private void Login(final int flag, final TransportEntity transportEntity) {
		final Runnable LoginNotPassW = new Runnable() {
			@Override
			public void run() {
				try {

					JSONObject param = new JSONObject();
					if (!TextUtils.isEmpty(transportEntity.getName())) {
						param.put("USERNAME", transportEntity.getName());
					} else {
//						showDialog();
						return;
					}
					param.put("PASSWORD", "C4BD1B3942F3C2ACD7657CBD0B5D952F");
					String response = HTTP.excute("login", "RestUserService", param.toString());
					final JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
						Log.e("resume获取User信息",Constants.user.toString());
						int ver = transportEntity.getVersion();
//						if(version<ver){
//							updateUserInfo(transportEntity);
//						}
						switch (flag) {
							case 1:
//							new Thread(GetGroup).start();
//							Background.Process(PermGuideContainer.this, GetGroup, getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));// 获取分组ID
								break;
//						case 2:
//							intent = new Intent();
//							intent.setClass(PermGuideContainer.this, ReserveSubmit.class);
//							intent.putExtra("permission", permission);
//							startActivity(intent);
//							break;
//						case 3:
//							Intent intent = new Intent();
//							intent.setClass(PermGuideContainer.this, AddConsult.class);
//							intent.putExtra("permission", permission);
//							startActivity(intent);
//							break;
							default:
								break;
						}
					} else if (code.equals("600")) {
//						if(transportEntity.getEnterpriseStatus().equals("3")){
//							QY_Register(flag, transportEntity);
//						}else {
//							GR_Register(flag, transportEntity);
//						}
					} else {
						String error = json.getString("error");
					}

				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		};
		new Thread(LoginNotPassW).start();
//		dialog=Background.Process(PermGuideContainer.this, LoginNotPassW,PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));
	}
	private void InitView(View view) {
		canDeclareList1 = (GridView) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "canDeclareList1"));
		canDeclareList2 = (GridView) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "canDeclareList2"));
		canDeclareLayout = (ScrollView) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "canDeclareLayout"));
		title_radio_group = (RadioGroup) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title_radio_group"));
		allPerms = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "allPerms"));

		final int title_rb1_id = MSFWResource.getResourseIdByName(getActivity(), "id", "title_rb1");
		final int title_rb2_id = MSFWResource.getResourseIdByName(getActivity(), "id", "title_rb2");
		final RadioButton trb1 = (RadioButton) view.findViewById(title_rb1_id);
		final RadioButton trb2 = (RadioButton) view.findViewById(title_rb2_id);
		title_radio_group = (RadioGroup) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title_radio_group"));
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
		home = (Button) view.findViewById(R.id.home);
		home.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPopupMore(v);
			}
		});
	}

	private void initSetListener() {
		/*
		 * chooseArea.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (null != regions &&
		 * regions.size() > 0 && null != regions.get(0)) { Intent intent = new
		 * Intent(); intent.setClass(WSBS.getActivity(), ChooseArea.class);
		 * intent.putExtra("AREAID", regions.get(0).getAREAID());
		 * intent.putExtra("parent", regions.get(0));
		 * startActivityForResult(intent, 101); } } });
		 */
		back.setVisibility(View.GONE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivity().finish();
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
					DialogUtil.showUIToast(getActivity(), getString(
							MSFWResource.getResourseIdByName(getActivity(), "string", "tj_search_empty_notice")));
				} else {
					intent = new Intent();
					intent.setClass(getActivity(), PermListByName.class);
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
		View view = LayoutInflater.from(getActivity())
				.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_popup_wsbs_more"), null);
		LinearLayout layoutSearch = (LinearLayout) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "layoutSearch"));
		LinearLayout layoutReserve = (LinearLayout) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "layoutReserve"));
		LinearLayout layoutWDBJ = (LinearLayout) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "layoutWDBJ"));
		LinearLayout layoutFavorit = (LinearLayout) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "layoutFavorit"));
		layoutSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(), Search.class));

			}
		});
		layoutReserve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == Constants.user) {
					LoginUtil util = new LoginUtil();
					util.checkLogin(getActivity());
				} else {
					startActivity(new Intent().setClass(getActivity(), ReserveList.class));
				}

			}
		});
		layoutWDBJ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (null == Constants.user) {
					LoginUtil util = new LoginUtil();
					util.checkLogin(getActivity());
				} else {
					startActivity(new Intent().setClass(getActivity(), WDBJ.class));
				}

			}
		});
		layoutFavorit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent().setClass(getActivity(), PermListByDB.class));

			}
		});
		PopupWindow popupWindow = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(v, DensityUtil.dip2px(getActivity(), -90), 0);
	}

	/**
	 * 显示菜单
	 * 
	 * @param v
	 */

	private void showPopLeft(View v) {
		View view = LayoutInflater.from(getActivity())
				.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_wsbs_menu"), null);
		Button favorite = (Button) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "wsbs_favorite"));
		Button search = (Button) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "wsbs_search"));
		Button dealwith = (Button) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "wsbs_dealwith"));
		Button reserve = (Button) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "wsbs_reserve"));
		Button erweima = (Button) view
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "wsbs_erweima"));
		reserve.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), ReserveList.class);
				startActivity(intent);
			}
		});
		favorite.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), PermListByDB.class);
				startActivity(intent);
			}
		});
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), Search.class);
				startActivity(intent);
			}
		});
		dealwith.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (null == Constants.user) {
					LoginBaoAnTongUtil.checkLogin(getActivity());
				}
				Intent intent = new Intent();
				intent.setClass(getActivity(), WDBJ.class);
				startActivity(intent);
			}
		});
		erweima.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), CaptureActivity.class);
				intent.putExtra("flag", 1);
				startActivity(intent);
			}
		});

		PopupWindow popupWindow = new PopupWindow(view, DensityUtil.dip2px(getActivity(), 268),
				DensityUtil.dip2px(getActivity(), 50));

		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置popWindow的显示和消失动画
		// popupWindow.setAnimationStyle(R.style.tj_AnimBottom);

		int[] location = new int[2];
		v.getLocationOnScreen(location);
		popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0] - popupWindow.getWidth(), location[1]);
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
				convertView = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_menu_item"),
						null);
				item.bg = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "bg"));
				item.name = (TextView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "name"));
				item.code = (TextView) convertView
						.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "code"));
				convertView.setTag(item);
			} else {
				item = (MenuItem) convertView.getTag();
			}
			
			 RelativeLayout.LayoutParams iconSize = new
			 RelativeLayout.LayoutParams((dm.widthPixels *3)/ 25,(dm.widthPixels *3)/ 25);
			 item.bg.setLayoutParams(iconSize);
			if(!TextUtils.isEmpty(icons.get(position).getPICTUREPATH())){
				ImageLoader.getInstance().displayImage(Constants.DOMAIN + getNewUrl(icons.get(position).getPICTUREPATH()),
						item.bg, options);	
			}


			item.name.setText(icons.get(position).getPICTURENAME() + "");
			if (!TextUtils.isEmpty(icons.get(position).getPERMCODE())
					&& !icons.get(position).getPERMCODE().equals("null")) {
				item.code.setText(icons.get(position).getPERMCODE() + "");
			} else {
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

	private void InitViewPager(View view) {
		viewPageNo = getActivity().getIntent().getIntExtra("viewPageNo", 0);

		viewPager = (ViewPager) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "vPager"));
		views = new ArrayList<View>();
		inflater = getActivity().getLayoutInflater();
		grbs = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_grbs"), null);
		qybs = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_qybs"), null);
		bmfw = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_department"), null);
		xtbs = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_can_declared"), null);
		views.add(qybs);
		views.add(bmfw);

		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(viewPageNo);
		Animation animation = new TranslateAnimation(0, offset + viewPageNo * (bmpW + 2 * offset), 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(1);
		imageView.startAnimation(animation);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

		gridView1 = (GridView) grbs.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "gridView"));
		radioGroup = (RadioGroup) grbs
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "radio_group"));
		radioGroup2 = (RadioGroup) qybs
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "radio_group"));
		final int rb1_id = MSFWResource.getResourseIdByName(getActivity(), "id", "rb1");
		final int rb2_id = MSFWResource.getResourseIdByName(getActivity(), "id", "rb2");
		final int rb3_id = MSFWResource.getResourseIdByName(getActivity(), "id", "rb3");

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
		gridView2 = (GridView) qybs.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "gridView"));
		radioGroup2.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == rb1_id) {
					gridView2.setAdapter(new GridViewAdapter(icons_qybs_qyzt));
					gridView2.setOnItemClickListener(new GridViewClick(icons_qybs_qyzt));
				} else if (checkedId == rb2_id) {
					gridView2.setAdapter(new GridViewAdapter(icons_qybs_qysj));
					gridView2.setOnItemClickListener(new GridViewClick(icons_qybs_qysj));

				} else if (checkedId == rb3_id) {
					gridView2.setAdapter(new GridViewAdapter(icons_qybs_tdqy));
					gridView2.setOnItemClickListener(new GridViewClick(icons_qybs_tdqy));

				}
			}
		});

		deptList = (ListViewForScrollView) bmfw
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "deptList"));
		ImageView empty = (ImageView) bmfw.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "empty"));
		deptList.setEmptyView(empty);
		StreetList = (ListViewForScrollView) bmfw
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "StreetList"));
		ImageView empty1 = (ImageView) bmfw
				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "empty1"));
		StreetList.setEmptyView(empty1);
//		ZQDeptList = (ListViewForScrollView) bmfw
//				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "QZdeptList"));
//		ImageView empty2 = (ImageView) bmfw
//				.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "empty2"));
//		ZQDeptList.setEmptyView(empty2);

		mMenu = (Button) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "wsbs_menu"));
		btnSearch = (Button) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "btnSearch"));
		textSearch = (EditText) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "textSearch"));
		btnMore = (ImageButton) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "btnMore"));
		back = (RelativeLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "back"));
	}

	/**
	 * 初始化头标
	 */

	private void InitTextView(View view) {
		textView1 = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "text1"));
		textView3 = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "text3"));

		titles = new ArrayList<TextView>();
		titles.add(textView1);
		titles.add(textView3);

		textView1.setOnClickListener(new MyOnClickListener(0));
		textView3.setOnClickListener(new MyOnClickListener(1));

	}

	/**
	 * 2 * 初始化动画 3
	 */

	private void InitImageView(View view) {
		imageView = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "cursor"));
		dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		offset = DensityUtil.dip2px(getActivity(), 14);// 计算偏移量
		bmpW = (screenW / 2 - 2 * offset);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(bmpW,
				DensityUtil.dip2px(getActivity(), 2));
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
				titles.get(i).setTextColor(MSFWResource.getResourseIdByName(getActivity(), "color", "tj_tab_text"));
			}
			titles.get(position).setTextColor(getActivity().getResources()
					.getColor(MSFWResource.getResourseIdByName(getActivity(), "color", "tj_my_green")));
			System.out.println("fuchl   onPageSelected   " + position);
			if (position == 1) {
				dialog = Background.Process(getActivity(), GetDeptList, "数据加载中");
				dialog = Background.Process(getActivity(), GetZQDeptList, "数据加载中");
				dialog = Background.Process(getActivity(), GetZJList, "数据加载中");
			}else if(position==0){
				if(icons==null||icons.size()<=0){
					dialog = Background.Process(getActivity(), InitParentArea, "数据加载中...");
				}
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
				String response = HTTP.excuteAndCache("getPictureByID", "RestUnitPortalService", param.toString(),
						getActivity());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					icons = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Icon>>() {
					}.getType());
					handler.sendEmptyMessage(GET_ICON_SUCCESS);
				} else {
					DialogUtil.showUIToast(getActivity(), json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
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
				String response = HTTP.excuteAndCache("getPictureByID", "RestUnitPortalService", param.toString(),
						getActivity());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					icons_declare = JSONUtil.getGson().fromJson(json.getString("ReturnValue"),
							new TypeToken<List<Icon>>() {
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
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							canDeclareList1.setAdapter(new GridViewAdapter(icons_declare_grbs));
							canDeclareList1.setOnItemClickListener(new GridViewClick(icons_declare_grbs));
							canDeclareList2.setAdapter(new GridViewAdapter(icons_declare_qybs));
							canDeclareList2.setOnItemClickListener(new GridViewClick(icons_declare_qybs));
						}
					});
				} else {
					DialogUtil.showUIToast(getActivity(), json.getString("error"));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
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

				String response = HTTP.excuteAndCache("getDeptlistByAreaid", "RestRegionService", param.toString(),
						getActivity());
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
						// map.put("NAME", strees.get(i).getNAME() + " (" +
						// strees.get(i).getPERMNUM() + ")");
						listItemDeclared.add(map);

					}

					handler.sendEmptyMessage(GET_STREETS_SUCCESS);

				} else {
					DialogUtil.showUIToast(getActivity(), getString(
							MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
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

				String response = HTTP.excuteAndCache("getDeptlistByAreaid", "RestRegionService", param.toString(),
						getActivity());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					ZQdepts = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());

					ZQdeptDatas = new ArrayList<Map<String, Object>>();
					for (int i = 0; i < ZQdepts.size(); i++) {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("DEPTID", ZQdepts.get(i).getDEPTID());
						map.put("SHORTNAME", ZQdepts.get(i).getSHORTNAME() + "（" + ZQdepts.get(i).getPERMNUM() + "）");
						map.put("AREAID", ZQdepts.get(i).getAREAID());
						map.put("ORDERID", ZQdepts.get(i).getORDERID());
						map.put("CNUM", ZQdepts.get(i).getCNUM());
						map.put("NAME", ZQdepts.get(i).getNAME());
						ZQdeptDatas.add(map);

					}

					handler.sendEmptyMessage(GET_ZQDEPTS_SUCCESS);

				} else {
					DialogUtil.showUIToast(getActivity(), getString(
							MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
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
				String response = HTTP.excuteAndCache("getDeptlistByAreaid", "RestRegionService", param.toString(),
						getActivity());
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
							map.put("SHORTNAME", depts.get(i).getSHORTNAME()+ "（" + depts.get(i).getPERMNUM() + "）");
							map.put("AREAID", depts.get(i).getAREAID());
							map.put("ORDERID", depts.get(i).getORDERID());
							map.put("CNUM", depts.get(i).getCNUM());
							map.put("NAME", depts.get(i).getNAME() );
							deptDatas.add(map);
						}
					}
					// else { // 可申报事项部门列表
					// listItemDeclared = new ArrayList<Map<String, Object>>();
					// for (int i = 0; i < depts.size(); i++) {
					// Map<String, Object> map = new HashMap<String, Object>();
					// map.put("DEPTID", depts.get(i).getDEPTID());
					// map.put("SHORTNAME", depts.get(i).getSHORTNAME());
					// map.put("AREAID", depts.get(i).getAREAID());
					// map.put("ORDERID", depts.get(i).getORDERID());
					// map.put("CNUM", depts.get(i).getCNUM());
					// map.put("NAME", depts.get(i).getNAME() + " (" +
					// depts.get(i).getCNUM() + ")");
					// listItemDeclared.add(map);
					// }
					// }

					handler.sendEmptyMessage(GET_DEPTS_SUCCESS);

				} else {
					DialogUtil.showUIToast(getActivity(), getString(
							MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
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
				String response = HTTP.excuteAndCache("getCooperateFlowList", "RestCooperateService", param.toString(),
						getActivity());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					cooperateVos = JSONUtil.getGson().fromJson(
							new JSONObject(json.getString("ReturnValue")).getString("Items"),
							new TypeToken<List<CooperateVo>>() {
					}.getType());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {

							List<Map<String, Object>> cooperateDatas = new ArrayList<Map<String, Object>>();
							for (int i = 0; i < cooperateVos.size(); i++) {
								Map<String, Object> map = new HashMap<String, Object>();
								map.put("CFLOWID", cooperateVos.get(i).getCFLOWID());
								map.put("CFLOWNAME", cooperateVos.get(i).getCFLOWNAME());
								map.put("REMARK", cooperateVos.get(i).getREMARK());
								cooperateDatas.add(map);
							}

							SimpleAdapter adapter = new SimpleAdapter(getActivity(), cooperateDatas,
									MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"),
									new String[] { "CFLOWNAME" },
									new int[] { MSFWResource.getResourseIdByName(getActivity(), "id", "deptName") });
							xtbsList.setAdapter(adapter);
							xtbsList.setOnItemClickListener(new MyItemClick());
						}
					});

				} else {
					DialogUtil.showUIToast(getActivity(), getString(
							MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
					getActivity().finish();
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
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
				String response = HTTP.excuteAndCache("getRegionlistByParentid", "RestRegionService", param.toString(),
						getActivity());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					regions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Region>>() {
					}.getType());
					handler.sendEmptyMessage(GET_REGION_SUCCESS);
				} else {
					DialogUtil.showUIToast(getActivity(), getString(
							MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

	private class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			if (currIndex == 1) {
				intent.setClass(getActivity(), PermListByDept.class);
				intent.putExtra("name", deptDatas.get(position).get("NAME").toString());
				intent.putExtra("DEPTID", deptDatas.get(position).get("DEPTID").toString());
				intent.putExtra("SFYDSB", "0");
				intent.putExtra("WSBSFLAG", 3);
			} else {
				intent.setClass(getActivity(), DeptList.class);
				intent.putExtra("WSBSFLAG", 3);
				intent.putExtra("dept", strees.get(position));
			}
			startActivity(intent);

		}

	}

	/**
	 * 驻区部门点击事件
	 * 
	 * @author S_Black
	 *
	 */
	private class MyItemClick1 implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), PermListByDept.class);
			intent.putExtra("name", ZQdeptDatas.get(position).get("NAME").toString());
			intent.putExtra("DEPTID", ZQdeptDatas.get(position).get("DEPTID").toString());
			intent.putExtra("SFYDSB", "0");
			intent.putExtra("fromflag", 1);
			intent.putExtra("WSBSFLAG", 3);

			startActivity(intent);

		}

	}

	/**
	 * 街道点击事件
	 * 
	 * @author S_Black
	 *
	 */
	private class MyItemClick2 implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), DeptList.class);
			intent.putExtra("dept", strees.get(position));
			intent.putExtra("WSBSFLAG", 3);

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

				dialog = Background.Process(getActivity(), InitIcon, "数据加载中");// 更新图标
				dialog = Background.Process(getActivity(), InitDeclareIcon, "数据加载中");// 更新图标
				break;
			case GET_DEPTS_SUCCESS:

				// if (currIndex == 1) {
				SimpleAdapter adapter = new SimpleAdapter(getActivity(), deptDatas,
						MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"),
						new String[] { "SHORTNAME" },
						new int[] { MSFWResource.getResourseIdByName(mContext, "id", "deptName") });
				deptList.setAdapter(adapter);
				deptList.setOnItemClickListener(new MyItemClick());
				// } else {
				// SimpleAdapter adapter = new SimpleAdapter(getActivity(),
				// listItemDeclared, MSFWResource.getResourseIdByName(mContext,
				// "layout", "tj_deptlist_item"), new String[] { "NAME" }, new
				// int[] { MSFWResource.getResourseIdByName(mContext, "id",
				// "deptName") });
				// declaredList.setAdapter(adapter);
				// declaredList.setOnItemClickListener(new MyItemClick());
				//
				// }

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
					gridView2.setAdapter(new GridViewAdapter(icons_qybs));
					gridView2.setOnItemClickListener(new GridViewClick(icons_qybs));

				}

				break;
			case GET_ZQDEPTS_SUCCESS:
//				SimpleAdapter adapter2 = new SimpleAdapter(getActivity(), ZQdeptDatas,
//						MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"),
//						new String[] { "SHORTNAME" },
//						new int[] { MSFWResource.getResourseIdByName(mContext, "id", "deptName") });
//				ZQDeptList.setAdapter(adapter2);
//				ZQDeptList.setOnItemClickListener(new MyItemClick1());
				break;
			case GET_STREETS_SUCCESS:
				SimpleAdapter adapter1 = new SimpleAdapter(getActivity(), listItemDeclared,
						MSFWResource.getResourseIdByName(mContext, "layout", "tj_deptlist_item"),
						new String[] { "NAME" },
						new int[] { MSFWResource.getResourseIdByName(mContext, "id", "deptName") });
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

	private void startPermList(String sortCode, String type, String PermCode,String pictureID) {
		intent = new Intent();
		intent.setClass(getActivity(), PermListByCode.class);
		intent.putExtra("SORTCODE", sortCode);
		intent.putExtra("type", type);
		intent.putExtra("SFYDSB", SFYDSB);
		intent.putExtra("PermCode", PermCode);
		intent.putExtra("PICTUREID", pictureID);
		intent.putExtra("WSBSFLAG", 3);
		startActivity(intent);
	}


	final Runnable Login = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("USERNAME", username);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(username));
				String response = HTTP.excuteAndCache("login", "RestUserService", param.toString(), getActivity());
				final JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
					DialogUtil.showUIToast(getActivity(), "登录成功！");
				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(getActivity(), error);
				}

			} catch (Exception e) {
				// DialogUtil.showUIToast(getActivity(),
				// getString(MSFWResource.getResourseIdByName(getActivity(),
				// "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	
	/**
     * 更多
     * @param view
     */
    private void showPopupMore(View view){
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(getActivity()).inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "main_popup_more"), null);
        LinearLayout jdcx_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "popup_more_jdcx"));
        LinearLayout wdfw_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "popup_more_wdfw"));
		LinearLayout qysq_ll = (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "popup_more_qysq"));
		LinearLayout scxkz_ll = (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "popup_more_scxkz"));
		GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
		final TransportEntity userInfo = gloabDelegete.getUserInfo();
//		if (userInfo!= null && TextUtils.equals("2", userInfo.getUserType())) {
//			scxkz_ll.setVisibility(View.VISIBLE);
//
//		} else {
//			scxkz_ll.setVisibility(View.GONE);
//		}
        scxkz_ll.setVisibility(View.GONE);
        if (gloabDelegete!=null&&Constants.user!=null&&Constants.user.getTYPE()!=null&&TextUtils.equals("2", Constants.user.getTYPE())) {
            scxkz_ll.setVisibility(View.VISIBLE);

        } else {
            scxkz_ll.setVisibility(View.GONE);
        }
//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels)*0.35); //屏幕宽
        int height = dm.heightPixels; //屏幕高
        final PopupWindow popupWindow = new PopupWindow(contentView,
                width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(MSFWResource.getResourseIdByName(getActivity(), "style", "popwin_anim_style"));
        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
        params.alpha=0.6f;
        getActivity(). getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
                params.alpha=1f;
                getActivity().getWindow().setAttributes(params);
            }
        });
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(MSFWResource.getResourseIdByName(getActivity(), "color", "tj_transparent"))));

        // 设置好参数之后再show
//        popupWindow.showAsDropDown(view,-popupWindow.getWidth()+110,10);
        popupWindow.showAtLocation(getActivity().findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "root_parent")), Gravity.RIGHT | Gravity.TOP, DensityUtil.dip2px(getActivity(), 5), DensityUtil.dip2px(getActivity(), 70));
        
        jdcx_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent=new Intent();
                intent.setClass(getActivity(),Search.class);
                startActivity(intent);
            }
        });
        wdfw_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent=new Intent();
                intent.setClass(getActivity(),NetworkListActivity.class);
                startActivity(intent);
            }
        });
		qysq_ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				Intent intent = new Intent(QYBSFragment.this.getActivity(), FaceAuthActivity.class);
				startActivity(intent);



			}
		});
		scxkz_ll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
				Intent intent = new Intent(QYBSFragment.this.getActivity(), XKZKJActivity.class);
				startActivity(intent);
			}
		});
    }
}
