package com.tjsoft.webhall.ui.wsbs;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.ShowPopupMoreUtil;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.util.VoiceSearchUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.FavoriteManage;
import com.tjsoft.webhall.entity.Dept;
import com.tjsoft.webhall.entity.Icon;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.ui.search.PermListByName;
import com.tjsoft.webhall.util.SharedPreferenceUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 根据编码查找事项列表
 * 
 * @author Administrator
 * 
 */
public class PermListByCode extends AutoDialogActivity {

	private ListView permList;
	private List<Permission> permissions=new ArrayList<Permission>();
	//只在事项编码里面的  》756的不展示
	private List<Permission> InCodepermissions=new ArrayList<Permission>();
	private LayoutInflater layoutInflater;
	private final int GET_DEPTS_SUCCESS = 2;
	private MyHandler handler = new MyHandler();
	private final int GET_PERM_SUCCESS = 1;
	private final int GET_CLASSFY_SUCCESS=3;
	private String SORTCODE = "";
	private TextView titleName;
	private Button home;
	private RelativeLayout  back;
	private Intent intent;
	private TextView noData;
	private ProgressDialog dialog;
	private Context mContext;
	private boolean SFYDSB = false;
	private Button btnSearch;
	private EditText textSearch;
	
	private ImageView voice;
	private VoiceSearchUtil mVoice;//语音搜索
	
	private PermAdapter adapter;
	
	private SharedPreferences sp;//记录版本号的
	
	private Spinner deptSpinner;
	private Spinner classfySpinner;
	private List<Dept> depts;// 部门列表
	private ArrayAdapter<String> deptAdapter;
	private ArrayAdapter<String> classfyAdapter;
	private List<String> deptNames;
	private List<String> classfyNames;
	private String deptId;
	private List<Icon> icons;
	private String childSortCode="";
	private RelativeLayout searchPremlist_rl;
	private String pictureID;
	private int currentDeptPosition=0;//当前选择的如果是全部部门全部分类  需要用老的接口，新接口处理有问题
	private int currentClassfyPosition=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "activity_tj_permlist_bycode"));
		Constants.getInstance().addActivity(this);
		Constants.WSBS_PATH=getIntent().getIntExtra("WSBSFLAG", 0);
		sp=this.getSharedPreferences("Version", MODE_PRIVATE);
		mContext = this;
		SFYDSB = getIntent().getBooleanExtra("SFYDSB", false);
		pictureID=getIntent().getStringExtra("PICTUREID");
		InitView();
		dialog = Background.Process(this, GetDeptList, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));
		dialog = Background.Process(this, InitClassfy, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));

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

	@Override
	protected void onDestroy() {
		StatisticsTools.end(getIntent().getStringExtra("type"), null, null);
		super.onDestroy();
	}

	private void InitView() {
		deptSpinner = (Spinner)findViewById(R.id.deptSpinner);
		deptSpinner.setOnItemSelectedListener(new deptSpinnerSelectedListener());
		classfySpinner = (Spinner)findViewById(MSFWResource.getResourseIdByName(this, "id", "classfySpinner"));
		classfySpinner.setOnItemSelectedListener(new classfySpinnerSelectedListener());
		deptNames=new ArrayList<String>();
		deptNames.add("全部部门");
		deptAdapter = new ArrayAdapter<String>(PermListByCode.this, MSFWResource.getResourseIdByName(PermListByCode.this, "layout", "left_spinner"), deptNames);
		deptAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		deptSpinner.setAdapter(deptAdapter);
		
		classfyNames=new ArrayList<String>();
		classfyNames.add("全部分类");
		classfyAdapter = new ArrayAdapter<String>(PermListByCode.this, MSFWResource.getResourseIdByName(PermListByCode.this, "layout", "left_spinner"), classfyNames);
		classfyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		classfySpinner.setAdapter(classfyAdapter);
		
		searchPremlist_rl=(RelativeLayout)findViewById(MSFWResource.getResourseIdByName(this, "id", "search_permlist_rl"));
		searchPremlist_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				intent = new Intent();
				intent.setClass(PermListByCode.this, PermListByName.class);
				startActivity(intent);
			}
		});
		btnSearch = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnSearch"));
		textSearch = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "textSearch"));
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (TextUtils.isEmpty(textSearch.getText().toString())) {
					DialogUtil.showUIToast(PermListByCode.this, "事项名称不能为空！");
				} else {
					intent = new Intent();
					intent.setClass(PermListByCode.this, PermListByName.class);
					intent.putExtra("name", textSearch.getText().toString().trim());
					startActivity(intent);
				}
				
			}
		});

		noData = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "noData"));
		layoutInflater = getLayoutInflater();
		permList = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "permList"));
		permList.setOnItemClickListener(new MyItemClick());
		permList.setOnItemLongClickListener(new MyItemLongClick());
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		permList.setEmptyView(empty);
		adapter=new PermAdapter(mContext,InCodepermissions);
		permList.setAdapter(adapter);
		
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowPopupMoreUtil.PermListshowPopupMore(v, mContext);
			}
		});
		titleName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "titleName"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PermListByCode.this.finish();
			}
		});
		SORTCODE = getIntent().getStringExtra("SORTCODE");
		String type = getIntent().getStringExtra("type");
		titleName.setText(type);
		if (Constants.WSBS_PATH == 1) {
			titleName.setText("选择办理事项");

		} else if (Constants.WSBS_PATH == 2) {
			titleName.setText("选择办理事项（1/3）");
		}
	}
	
	private class deptSpinnerSelectedListener implements OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			currentDeptPosition=position;
			adapter.clearAll();

			if (currentClassfyPosition == 0&&currentDeptPosition==0) {
				dialog = Background.SingleProcess(PermListByCode.this, GetPermList, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));
			} else if(position==0&&currentClassfyPosition!=0){
				deptId = "";
				dialog = Background.SingleProcess(PermListByCode.this, SearchListByCode, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));
			}else{

				deptId = depts.get(position - 1).getDEPTID();
				dialog = Background.SingleProcess(PermListByCode.this, SearchListByCode, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));
			}
		
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}		
	}
	private class classfySpinnerSelectedListener implements OnItemSelectedListener{

		public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			adapter.clearAll();

				currentClassfyPosition=position;
				if (currentClassfyPosition == 0&&currentDeptPosition==0) {	
					dialog = Background.SingleProcess(PermListByCode.this, GetPermList, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));
				} else if(position==0&&currentDeptPosition!=0){
					childSortCode = "";
					dialog = Background.SingleProcess(PermListByCode.this, SearchListByCode, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));
				}else{
					childSortCode = icons.get(position - 1).getPICTURECODE();
					dialog = Background.SingleProcess(PermListByCode.this, SearchListByCode, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_loding")));
				}

					
		}

		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
	private class MyItemLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
			new AlertDialog.Builder(PermListByCode.this).setMessage("是否将该事项加入收藏夹？").setTitle(getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					FavoriteManage.add(permissions.get(position), PermListByCode.this);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			}).show();
			return true;
		}

	}

	class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//			if(!TextUtils.isEmpty(InCodepermissions.get(position).getWSSBDZ())){
//				intent = new Intent();
//				intent.setClass(PermListByCode.this, WSBSWeb.class);
//				intent.putExtra("url", InCodepermissions.get(position).getWSSBDZ());
//				startActivity(intent);
//			}else{
//				intent = new Intent();
//				intent.setClass(PermListByCode.this, HistoreShare_v2.class);
//				intent.putExtra("PERMID", InCodepermissions.get(position).getID());
//				intent.putExtra("permission", InCodepermissions.get(position));
//				intent.putExtra("WSBSFLAG", Constants.WSBS_PATH);
//				startActivity(intent);
//			}		
			adapter.setSelsetorItem(position);
			adapter.notifyDataSetInvalidated();
		}

	}

	final Runnable GetPermList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("SORTCODE", SORTCODE);
				param.put("PAGENO", "1");
//				param.put("SFYDSB", "1");
//				param.put("AREAID", Constants.AREAID);
				param.put("PAGESIZE", "1000");
//				if (SFYDSB) {
//					param.put("SFYDSB", "1");// 是否可移动申报
//				}
				String response = HTTP.excuteAndCache("getPermlistBySortcode2", "RestUnitPortalService", param.toString(),PermListByCode.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					permissions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Permission>>() {
					}.getType());
					InCodepermissions.clear();
					
					//暂时放开这个756的限制
					InCodepermissions.addAll(permissions);
					Collections.sort(InCodepermissions,new PermissionComparator());
//					for (int i = 0; i <permissions.size(); i++) {						
//						if(!TextUtils.isEmpty(permissions.get(i).getCODE3())){
//							int CODE3=Integer.parseInt(permissions.get(i).getCODE3().substring(0, 3));
//							int permCode=756;    //目前事项只有756以下才有用，要过滤掉大于756的事项（事项编号是宝安特有的）
//							if(CODE3<=permCode){
//								InCodepermissions.add(permissions.get(i));
//								Collections.sort(InCodepermissions,new PermissionComparator());
//							}
//						}
//						
//					}
					handler.sendEmptyMessage(GET_PERM_SUCCESS);

				} else {
					DialogUtil.showUIToast(PermListByCode.this, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(PermListByCode.this, getString(MSFWResource.getResourseIdByName(PermListByCode.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	
	private class PermissionComparator implements Comparator<Permission> {
		@Override
		public int compare(Permission o1, Permission o2) {
			if(!TextUtils.isEmpty(o1.getCODE3())&&!o1.getCODE3().equals("null")&&!TextUtils.isEmpty(o2.getCODE3())&&!o2.getCODE3().equals("null")){
				return o1.getCODE3().compareTo(o2.getCODE3());
			}else{
				return o2.getCODE3().compareTo(o1.getCODE3());
			}	
			
		}
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_PERM_SUCCESS:
				adapter.addData(InCodepermissions);
				break;
			case GET_DEPTS_SUCCESS:
				deptAdapter.notifyDataSetChanged();
				break;
			case GET_CLASSFY_SUCCESS:
				classfyAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	}
	/**
	 * 初始化分类
	 */
	final Runnable InitClassfy = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("ID", "440306");
//				param.put("SFYDSB","1");
				param.put("PARENTID", pictureID);
				String response = HTTP.excuteAndCache("getPictureByID", "RestUnitPortalService", param.toString(),
						PermListByCode.this);
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					icons = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Icon>>() {
					}.getType());
					for(int i=0;i<icons.size();i++){
						classfyNames.add(icons.get(i).getPICTURENAME());
					}
					handler.sendEmptyMessage(GET_CLASSFY_SUCCESS);
				} else {
					DialogUtil.showUIToast(PermListByCode.this, json.getString("error"));
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
					for(int i=0;i<depts.size();i++){
						deptNames.add(depts.get(i).getSHORTNAME());
					}
					handler.sendEmptyMessage(GET_DEPTS_SUCCESS);

				} else {
					 DialogUtil.showUIToast(PermListByCode.this,
							 getString(MSFWResource.getResourseIdByName(PermListByCode.this,
							 "string", "tj_occurs_error_network")));				}

			} catch (Exception e) {
				 DialogUtil.showUIToast(PermListByCode.this,
				 getString(MSFWResource.getResourseIdByName(PermListByCode.this,
				 "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	/**
	 * 获取列表
	 */
	final Runnable SearchListByCode = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("SORTCODE", childSortCode);
//				param.put("AREAID", "440306");
//				param.put("SFYDSB", "1");
				param.put("PARENTID", pictureID);
				param.put("DEPTID", deptId);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");			
				String response = HTTP.excute("getPermlistByCode", "RestUnitPortalService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					permissions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Permission>>() {
					}.getType());
					InCodepermissions.clear();
					//暂时放开这个756的限制
					InCodepermissions.addAll(permissions);
					Collections.sort(InCodepermissions,new PermissionComparator());
//					for (int i = 0; i <permissions.size(); i++) {						
//						if(!TextUtils.isEmpty(permissions.get(i).getCODE3())&&!permissions.get(i).getCODE3().equals("null")){
//							int CODE3=Integer.parseInt(permissions.get(i).getCODE3().substring(0, 3));
//							int permCode=756;
////							Log.d("PermList", "permCode==="+permCode+"       CODE3======"+CODE3);
//							if(CODE3<=permCode){
//								InCodepermissions.add(permissions.get(i));
//								Collections.sort(InCodepermissions,new PermissionComparator());
//							}
//						}
//						
//					}
					handler.sendEmptyMessage(GET_PERM_SUCCESS);

				} else {
					 DialogUtil.showUIToast(PermListByCode.this,
							 getString(MSFWResource.getResourseIdByName(PermListByCode.this,
							 "string", "tj_occurs_error_network")));				}

			} catch (Exception e) {
				 DialogUtil.showUIToast(PermListByCode.this,
				 getString(MSFWResource.getResourseIdByName(PermListByCode.this,
				 "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 100) {

				String result = data.getStringExtra("result");
				System.out.println("fuchl  得到result" + result);
				if (TextUtils.equals(result, "success")) {
					Object object = SharedPreferenceUtil.get(mContext,"file_key", "value_key");
					if (object != null) {
						Permission permission = (Permission) object;
						System.out.println("fuchl  取出permission" + permission.toString());

						adapter.nextActivity(1, permission);

					}

				} else {
					DialogUtil.showUIToast(mContext,
							"认证失败");
				}


			}
		}
	}
}
