package com.tjsoft.webhall.ui.search;

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
import android.util.Log;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
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
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.ui.wsbs.PermAdapter;
import com.tjsoft.webhall.util.SharedPreferenceUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 根据关键字查找事项列表界面
 * 
 * @author Administrator
 * 
 */
public class PermListByName extends AutoDialogActivity {
	private ListView permList;
	private List<Permission> permissions = new ArrayList<Permission>();
	//只在事项编码里面的  》756的不展示
	private List<Permission> InCodepermissions=new ArrayList<Permission>();
	private LayoutInflater layoutInflater;
	private ProgressDialog mProgress;
	private MyHandler handler = new MyHandler();
	private final int GET_PERM_SUCCESS = 1;
	private String permName = "";
	private TextView typeName;
	private Button home;
	private RelativeLayout  back;
	private Intent intent;
	private TextView noData;
	private Context mContext;
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	private String DEPTID = null;
	private String[] types = { "全部", "新安", "西乡", "福永", "沙井", "松岗", "石岩" ,"航城","福海","新桥","燕罗"};
	private String[] deptIds = { null, "K31725413", "K31734854", "007550515", "007550654", "007550638", "007541985" 
			,"075801232","075801228","075801230","075801236"};
	private Button btnSearch;
	private EditText textSearch;
	private LinearLayout chooseDept;

	private ImageView voice;
	private VoiceSearchUtil mVoice;
	
	private PermAdapter permAdapter;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		mContext = this;
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_permlist"));
		Constants.getInstance().addActivity(this);
		InitView();
//		if(permName!=null&&!TextUtils.isEmpty(permName)){//从分类跳转过来不进行搜索操作
//			dialog = Background.SingleProcess(this, GetPermList, getString(MSFWResource.getResourseIdByName(PermListByName.this, "string", "tj_loding")));
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

	@Override
	protected void onDestroy() {
		StatisticsTools.end("事项搜索", null, null);
		super.onDestroy();
	}

	private void InitView() {
		btnSearch = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnSearch"));
		textSearch = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "textSearch"));
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (TextUtils.isEmpty(textSearch.getText().toString())) {
					DialogUtil.showUIToast(PermListByName.this, "事项名称不能为空！");
				} else {
//					intent = new Intent();
//					intent.setClass(PermListByName.this, PermListByName.class);
//					intent.putExtra("name", textSearch.getText().toString().trim());
//					startActivity(intent);
//					finish();
					permAdapter.clearAll();
					permName=textSearch.getText().toString().trim();
					dialog = Background.SingleProcess(PermListByName.this, GetPermList, getString(MSFWResource.getResourseIdByName(PermListByName.this, "string", "tj_loding")));
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
		permAdapter=new PermAdapter(mContext,InCodepermissions);
		permList.setAdapter(permAdapter);
		
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowPopupMoreUtil.PermListshowPopupMore(v, mContext);
			}
		});
		typeName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "typeName"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PermListByName.this.finish();
			}
		});
		// home.setOnClickListener(this);
		permName = getIntent().getStringExtra("name");
		Log.e("sps===", "permName=="+permName);
		textSearch.setText(permName);
		typeName.setText("“" + permName + "”的相关事项");

		spinner = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner"));
		chooseDept = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "chooseDept"));
		adapter = new ArrayAdapter<String>(PermListByName.this, MSFWResource.getResourseIdByName(PermListByName.this, "layout", "my_spinner_item_consult3"), types);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		chooseDept.setVisibility(View.VISIBLE);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
				permissions.clear();
				DEPTID = deptIds[position];
				if(permName!=null&&!TextUtils.isEmpty(permName)){//从分类跳转过来不进行搜索操作
					permAdapter.clearAll();
					dialog = Background.SingleProcess(PermListByName.this, GetPermList, getString(MSFWResource.getResourseIdByName(PermListByName.this, "string", "tj_loding")));
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

	}

	private class MyItemLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
			new AlertDialog.Builder(PermListByName.this).setMessage("是否将该事项加入收藏夹？").setTitle(getString(MSFWResource.getResourseIdByName(PermListByName.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					FavoriteManage.add(InCodepermissions.get(position), PermListByName.this);
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			}).show();
			return true;
		}

	}

	private class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

/*			if(!TextUtils.isEmpty(InCodepermissions.get(position).getWSSBDZ())){
				intent = new Intent();
				intent.setClass(PermListByName.this, WSBSWeb.class);
				intent.putExtra("url", InCodepermissions.get(position).getWSSBDZ());
				startActivity(intent);
			}else{
				intent = new Intent();
				intent.setClass(PermListByName.this, HistoreShare_v2.class);
				intent.putExtra("PERMID", InCodepermissions.get(position).getID());
				intent.putExtra("permission", InCodepermissions.get(position));
				startActivity(intent);
			}*/		
			permAdapter.setSelsetorItem(position);
			permAdapter.notifyDataSetInvalidated();
		}

	}

	final Runnable GetPermList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMNAME", permName);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				param.put("DEPTID", DEPTID);
				String response = HTTP.excuteAndCache("getPermByKey", "RestPermissionitemService", param.toString(),PermListByName.this);
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
					DialogUtil.showUIToast(PermListByName.this, json.getString("error"));
					finish();
				}
			} catch (Exception e) {
				DialogUtil.showUIToast(PermListByName.this, getString(MSFWResource.getResourseIdByName(PermListByName.this, "string", "tj_occurs_error_network")));
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
				permAdapter.addData(InCodepermissions);
				break;

			default:
				break;
			}
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 100) {

				String result = data.getStringExtra("result ");
				System.out.println("fuchl  得到result" + result);
				if (TextUtils.equals(result, "success")) {
					Object object = SharedPreferenceUtil.get(mContext,"file_key", "value_key");
					if (object != null) {
						Permission permission = (Permission) object;
						System.out.println("fuchl  取出permission" + permission.toString());

						permAdapter.nextActivity(1, permission);

					}

				} else {
					DialogUtil.showUIToast(mContext,
							"认证失败");
				}


			}
		}
	}

}
