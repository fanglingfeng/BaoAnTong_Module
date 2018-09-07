package com.tjsoft.webhall.ui.wsbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputFilter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Consult;
import com.tjsoft.webhall.entity.Dept;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.Region;
import com.tjsoft.webhall.lib.XListView;
import com.tjsoft.webhall.lib.XListView.IXListViewListener;
import com.tjsoft.webhall.ui.wsbs.ConsultList.ConversationAdapter;
import com.tjsoft.webhall.ui.wsbs.ConsultList.MyAdapter;
import com.tjsoft.webhall.ui.wsbs.ConsultList.MyHandler;
import com.tjsoft.webhall.ui.wsbs.ConsultList.MyItemClick;
import com.tjsoft.webhall.ui.wsbs.ConsultList.ConversationAdapter.ConversationItem;
import com.tjsoft.webhall.ui.wsbs.WSBS1.MyOnPageChangeListener;
import com.tjsoft.webhall.ui.wsbs.WSBS1.MyViewPagerAdapter;

/**
 * 我要咨询页面
 * 
 * @author Administrator
 * 
 */
public class AddConsult extends AutoDialogActivity implements OnTouchListener, IXListViewListener {
	
	private Button submit;
	private RelativeLayout back;
	private TextView txtDept, txtArea;
	private EditText content, name, phone, mainTitle;
	private final int GET_DEPTS_SUCCESS = 2;
	private final int GET_STREETS_SUCCESS = 3;
	private final int SUBMINT_SUCCESS=4;
	public static List<Region> regions;
	private List<Dept> depts;
	public static String[] regionNames;
	public static List<String> deptNames;
	private MyHandler handler = new MyHandler();
	private ArrayAdapter<String> adapter;
	private Spinner spinner1, spinner2;
	private int regionNum;
	private String deptId = "";
	private String regionId = "";
	private Permission permission;
	private List<Dept> strees;//街道列表
	private List<Dept> AllStrees;
	
	private ImageView imageView;
	private DisplayMetrics dm;// 设备管理
	private int bmpW = 0;// 动画图片宽度
	private int offset = 0;// 动画图片偏移量
	private TextView textView2;
	private TextView textView3;
	private List<TextView> titles;
	private ViewPager viewPager;// 页卡内容
	private LayoutInflater inflater;
	private List<View> views;// Tab页面列表
	private View wyzx, lszx;// 各个页卡
	private int currIndex = 0;// 当前页卡编号

	//历史咨询
	private XListView consultList;
	private List<Consult> conversations, conversationPlus;
	private final int GET_CONVERSATION_SUCCESS = 6;
	private final int SEARCH_CONVERSATION_SUCCESS = 5;
	private LayoutInflater layoutInflater;
	private Button  search;
	private int PAGENO = 1;
	private EditText MAINTITLE;
	private MyAdapter myAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "activity_zxzz"));
		InitTextView();
		InitImageView();
		InitViewPage();
		InitZXZXView(wyzx);
		InitLSZXView(lszx);
		initViewStatus();

	}

	private void InitViewPage(){
		back = (RelativeLayout)findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));

		viewPager = (ViewPager) findViewById(MSFWResource.getResourseIdByName(this, "id", "vPager"));
		views = new ArrayList<View>();
		inflater = getLayoutInflater();
		wyzx = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "consult_submit"), null);
		lszx = inflater.inflate(MSFWResource.getResourseIdByName(this, "layout", "consult_list"), null);
	
		views.add(wyzx);
		views.add(lszx);
	

		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);

		titles.get(0).setTextColor(MSFWResource.getResourseIdByName(AddConsult.this, "color", "tj_tab_text"));
		titles.get(0).setTextColor(AddConsult.this.getResources().getColor(MSFWResource.getResourseIdByName(AddConsult.this, "color", "tj_my_green")));
		Animation animation = new TranslateAnimation((offset * 2 + bmpW) * currIndex + offset, offset, 0, 0);
		animation.setFillAfter(true);
		animation.setDuration(1);
		imageView.startAnimation(animation);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
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
	 * 初始化头标
	 */

	private void InitTextView() {
		textView2 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "wyzx_title_tv"));
		textView3 = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "lszx_title_tv"));

		titles = new ArrayList<TextView>();
		titles.add(textView2);
		titles.add(textView3);
		

		textView2.setOnClickListener(new MyOnClickListener(0));
		textView3.setOnClickListener(new MyOnClickListener(1));

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
				titles.get(i).setTextColor(MSFWResource.getResourseIdByName(AddConsult.this, "color", "tj_tab_text"));
			}
			titles.get(position).setTextColor(AddConsult.this.getResources().getColor(MSFWResource.getResourseIdByName(AddConsult.this, "color", "tj_my_green")));
		}

	}
	/**
	 * 区分首页和办事指南进入的状态
	 */
	private void initViewStatus() {
		if (null != getIntent().getSerializableExtra("permission")) {
			permission = (Permission) getIntent().getSerializableExtra("permission");
			txtArea.setText("宝安区");
			regionId = "440306";
			deptId = permission.getDEPTID();
			txtDept.setText(permission.getDEPTNAME());
			mainTitle.setText(permission.getSXZXNAME());
		}

	}

	private void InitLSZXView(View view){
		search = (Button)view. findViewById(MSFWResource.getResourseIdByName(this, "id", "search"));
		MAINTITLE = (EditText) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "MAINTITLE"));

		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(AddConsult.this, SearchConversations, getString(MSFWResource.getResourseIdByName(AddConsult.this, "string", "tj_loding")));
				
			}
		});
		consultList = (XListView)view. findViewById(MSFWResource.getResourseIdByName(this, "id", "consultList"));
		consultList.setPullLoadEnable(false);
		consultList.setXListViewListener(this);
		ImageView empty = (ImageView)view.findViewById(MSFWResource.getResourseIdByName(this, "id", "empty")); 
		consultList.setEmptyView(empty);
		layoutInflater = getLayoutInflater();
		PAGENO = 1;
		dialog = Background.Process(this, GetConversations, getString(MSFWResource.getResourseIdByName(this, "string", "tj_loding")));
	}
	

	private void InitZXZXView(View view) {
		content = (EditText) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "content"));
		content.setFilters(new InputFilter[]{new InputFilter.LengthFilter(500)}); //最大输入长度  
		content.setOnTouchListener(this);
		name = (EditText) view.findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		name.setText(Constants.user.getREALNAME());
		phone = (EditText)view. findViewById(MSFWResource.getResourseIdByName(this, "id", "phone"));
		phone.setText(Constants.user.getMOBILE());
		mainTitle = (EditText)view.  findViewById(MSFWResource.getResourseIdByName(this, "id", "mainTitle"));
		submit = (Button)view.  findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		txtDept = (TextView) view. findViewById(MSFWResource.getResourseIdByName(this, "id", "txtDept"));
		txtArea = (TextView) view. findViewById(MSFWResource.getResourseIdByName(this, "id", "txtArea"));
		spinner1 = (Spinner)view.  findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner1"));
		spinner1.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner2 = (Spinner) view. findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner2"));
		spinner2.setOnItemSelectedListener(new SpinnerSelectedListener2());
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = Background.Process(AddConsult.this, Submit, "正在提交数据...");

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		dialog = Background.Process(this, GetDeptList, "数据加载中");
		deptNames=new ArrayList<String>();
		adapter = new ArrayAdapter<String>(AddConsult.this, MSFWResource.getResourseIdByName(AddConsult.this, "layout", "my_spinner_item_consult"), deptNames);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner2.setAdapter(adapter);
	}



	final Runnable Submit = new Runnable() {
		@Override
		public void run() {
			try {
			
				if (null == deptId || deptId.equals("")) {
					DialogUtil.showUIToast(AddConsult.this, "请选择部门");
					return;
				}
				if (null == mainTitle.getText() || mainTitle.getText().equals("")) {
					DialogUtil.showUIToast(AddConsult.this, "主题不能为空");
					return;
				}
				if (null == content.getText() || content.getText().toString().trim().equals("")) {
					DialogUtil.showUIToast(AddConsult.this, "咨询内容不能为空");
					return;
				}
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
				param.put("NAME", Constants.user.getREALNAME());
				param.put("MOVEPHONE", Constants.user.getMOBILE());
				param.put("MAINTITLE", mainTitle.getText().toString());
				param.put("CONTENT", content.getText().toString());
				param.put("DEPARTMENTID", deptId);

				String response = HTTP.excute("submit", "RestAdvisoryService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(AddConsult.this, "提交成功");
					handler.sendEmptyMessage(SUBMINT_SUCCESS);
				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(AddConsult.this, error);
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(AddConsult.this, "网络环境异常");
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
				param.put("AREAID", "440306");
				param.put("PAGENO", "1");
				param.put("RESERVEONE", "5");
				param.put("PAGESIZE", "1000");

				String response = HTTP.excuteAndCache("getDeptlistByAreaid", "RestRegionService", param.toString(),AddConsult.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					strees = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());
					for(int i=0;i<strees.size();i++){
						deptNames.add(strees.get(i).getNAME());
					}
					for(Dept dept:strees){
						AllStrees.add(dept);
					}
					handler.sendEmptyMessage(GET_STREETS_SUCCESS);

				} else {
//					DialogUtil.showUIToast(AddConsult.this, getString(MSFWResource.getResourseIdByName(AddConsult.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(WSBS1.this, getString(MSFWResource.getResourseIdByName(WSBS1.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	final Runnable GetDeptList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				param.put("AREAID", "440306");
				param.put("RESERVEONE", "4");
				String response = HTTP.excute("getDeptlistByAreaid", "RestRegionService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					depts = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());
					deptNames.add("请选择部门");
					for(int i=0;i<depts.size();i++){
						deptNames.add(depts.get(i).getNAME());
					}
					AllStrees=depts;
					handler.sendEmptyMessage(GET_DEPTS_SUCCESS);

				} else {
					DialogUtil.showUIToast(AddConsult.this, "网络环境异常");
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(AddConsult.this, "网络环境异常");
				e.printStackTrace();

			}
		}
	};

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			if (index == 0) {
				regionId = "";
			} else {
				regionNum = index - 1;
//				dialog = Background.Process(AddConsult.this, GetDeptList, "数据加载中");
				regionId = "not null ";
				txtArea.setVisibility(View.INVISIBLE);
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener2 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			if (index == 0) {
				deptId = "";
			} else {
				deptId = AllStrees.get(index - 1).getDEPTID();
				txtDept.setVisibility(View.INVISIBLE);
			}
			
			
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_DEPTS_SUCCESS:
				adapter.notifyDataSetChanged();
				new Thread(GetZJList).start();
				break;
			case GET_STREETS_SUCCESS:		
				adapter.notifyDataSetChanged();
				break;
			case GET_CONVERSATION_SUCCESS:
				myAdapter = new MyAdapter();
				consultList.setAdapter(myAdapter);
				consultList.setOnItemClickListener(new MyItemClick());
				break;
			case SEARCH_CONVERSATION_SUCCESS:
				myAdapter.notifyDataSetChanged();
				break;
			case SUBMINT_SUCCESS:
				new Thread(GetConversations).start();
				deptId = "";
				txtDept.setVisibility(View.VISIBLE);
				content.setText("");
				mainTitle.setText("");
				viewPager.setCurrentItem(1);
				break;
			default:
				break;
			}
		}
	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		// TODO Auto-generated method stub
	    //触摸的是EditText并且当前EditText可以滚动则将事件交给EditText处理；否则将事件交由其父类处理
	    if ((view.getId() == MSFWResource.getResourseIdByName(this, "id", "content") && canVerticalScroll(content))) {
	      view.getParent().requestDisallowInterceptTouchEvent(true);
	      if (event.getAction() == MotionEvent.ACTION_UP) {
	        view.getParent().requestDisallowInterceptTouchEvent(false);
	      }
	    }
		return false;
	}
	
	 /**
	   * EditText竖直方向是否可以滚动
	   * @param editText 需要判断的EditText
	   * @return true：可以滚动  false：不可以滚动
	   */
	  private boolean canVerticalScroll(EditText editText) {
	    //滚动的距离
	    int scrollY = editText.getScrollY();
	    //控件内容的总高度
	    int scrollRange = editText.getLayout().getHeight();
	    //控件实际显示的高度
	    int scrollExtent = editText.getHeight() - editText.getCompoundPaddingTop() -editText.getCompoundPaddingBottom();
	    //控件内容总高度与实际显示高度的差值
	    int scrollDifference = scrollRange - scrollExtent;
	 
	    if(scrollDifference == 0) {
	      return false;
	    }
	 
	    return (scrollY > 0) || (scrollY < scrollDifference - 1);
	  }
	  
	  
	  final Runnable GetConversations = new Runnable() {
			@Override
			public void run() {
				try {
					JSONObject param = new JSONObject();
					param.put("token", Constants.user.getTOKEN());
					param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
					param.put("PAGENO", PAGENO + "");
					param.put("PAGESIZE", "1000");

					String response = HTTP.excute("list", "RestAdvisoryService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						conversations = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Consult>>() {
						}.getType());
						Log.e("sps==", "conversations=="+conversations.size());
						handler.sendEmptyMessage(GET_CONVERSATION_SUCCESS);

					} else {
						DialogUtil.showUIToast(AddConsult.this, getString(MSFWResource.getResourseIdByName(AddConsult.this, "string", "tj_occurs_error_network")));
						finish();
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(AddConsult.this, getString(MSFWResource.getResourseIdByName(AddConsult.this, "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};
		final Runnable SearchConversations = new Runnable() {
			@Override
			public void run() {
				try {
					String title = MAINTITLE.getText().toString().trim();
					if (null == title || title.trim().equals("")) {
						DialogUtil.showUIToast(AddConsult.this, "搜索内容不能为空");
						return;
					}
					JSONObject param = new JSONObject();
					param.put("token", Constants.user.getTOKEN());
					param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
					param.put("MAINTITLE", title);
					param.put("PAGENO", "1");
					param.put("PAGESIZE", "1000");

					String response = HTTP.excute("search", "RestAdvisoryService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						conversations = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Consult>>() {}.getType());
						if (null != conversations && conversations.size() != 0) {
							handler.sendEmptyMessage(SEARCH_CONVERSATION_SUCCESS);
						} else {
							handler.sendEmptyMessage(SEARCH_CONVERSATION_SUCCESS);
							DialogUtil.showUIToast(AddConsult.this, "没有查找到相关数据");
						}
					} else {
						DialogUtil.showUIToast(AddConsult.this, json.getString("error"));
						finish();
					}

				} catch (Exception e) {
					DialogUtil.showUIToast(AddConsult.this, getString(MSFWResource.getResourseIdByName(AddConsult.this, "string", "tj_occurs_error_network")));
					e.printStackTrace();

				}
			}
		};

		class MyAdapter extends EndlessAdapter {

			public MyAdapter() {
				super(new ConversationAdapter());

			}

			// 底部控件
			@Override
			protected View getPendingView(ViewGroup parent) {
				View row = LayoutInflater.from(parent.getContext()).inflate(MSFWResource.getResourseIdByName(AddConsult.this, "layout", "list_end"), null);
				return row;
			}

			// 添加数据
			@Override
			protected void appendCachedData() {
				conversations.addAll(conversationPlus);
			}

			// 后台获取数据
			@Override
			protected boolean cacheInBackground() throws Exception {
				try {
					PAGENO++;
					JSONObject param = new JSONObject();
					param.put("token", Constants.user.getTOKEN());
					param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
					param.put("PAGENO", PAGENO + "");
					param.put("PAGESIZE", "20");
					String response = HTTP.excute("list", "RestAdvisoryService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if (code.equals("200")) {
						conversationPlus = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Consult>>() {
						}.getType());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				return conversationPlus.size() != 0; // true 继续 false 停止获取
			}

		}

		class ConversationAdapter extends BaseAdapter {

			@Override
			public int getCount() {
				return conversations.size();
			}

			@Override
			public Object getItem(int position) {
				return conversations.get(position);
			}

			@Override
			public long getItemId(int position) {
				return position;
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View view;
				if (null == convertView) {
					view = layoutInflater.inflate(MSFWResource.getResourseIdByName(AddConsult.this, "layout", "consult_list_item"), null);
				} else {
					view = convertView;
				}
				ConversationItem item = new ConversationItem();
				item.MAINTITLE = (TextView) view.findViewById(MSFWResource.getResourseIdByName(AddConsult.this, "id", "MAINTITLE"));
				item.CREATETIME = (TextView) view.findViewById(MSFWResource.getResourseIdByName(AddConsult.this, "id", "CREATETIME"));
				item.SFHF = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(AddConsult.this, "id", "SFHF"));

				item.MAINTITLE.setText(conversations.get(position).getMAINTITLE());
				item.CREATETIME.setText(conversations.get(position).getCREATETIME());
				if (null != conversations.get(position) && conversations.get(position).getSFHF().equals("0")) {
					item.SFHF.setVisibility(View.GONE);
				} else {
					item.SFHF.setVisibility(View.VISIBLE);
				}
				return view;
			}

			public final class ConversationItem {
				public TextView MAINTITLE;
				public TextView CREATETIME;
				public ImageView SFHF;

			}
		}
		class MyItemClick implements OnItemClickListener {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.setClass(AddConsult.this, ConsultContent.class);				
				intent.putExtra("conversation", conversations.get(position-1));
				startActivity(intent);
			}

		}
		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			PAGENO = 1;
			dialog = Background.Process(this, GetConversations, getString(MSFWResource.getResourseIdByName(this, "string", "tj_loding")));
			consultList.stopRefresh();
		}

		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			
		}
}
