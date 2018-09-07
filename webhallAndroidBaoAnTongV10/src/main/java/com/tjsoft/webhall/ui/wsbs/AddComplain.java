package com.tjsoft.webhall.ui.wsbs;

import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Dept;
import com.tjsoft.webhall.entity.Region;

/**
 * 我要投诉页面
 * 
 * @author Administrator
 * 
 */
public class AddComplain extends AutoDialogActivity {
	private Button submit, back;
	private EditText content, name, phone, mainTitle, beComplainPerson, job;
	private final int GET_REGION_SUCCESS = 1;
	private final int GET_DEPTS_SUCCESS = 2;
	public static List<Region> regions;
	private List<Dept> depts;
	public static String[] regionNames;
	public static String[] deptNames;
	private MyHandler handler = new MyHandler();
	private ArrayAdapter<String> adapter;
	private Spinner spinner1, spinner2;
	private int regionNum;
	private String deptId = "";
	private String regionId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "complain_submit"));
		InitView();

	}

	@Override
	protected void onStart() {
		InitView();
		super.onStart();
	}

	private void InitView() {
		content = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "content"));
		name = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		name.setText(Constants.user.getUSERNAME());
		phone = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "phone"));
		phone.setText(Constants.user.getMOBILE());
		mainTitle = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "mainTitle"));
		beComplainPerson = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "beComplainPerson"));
		job = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "job"));
		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		spinner1 = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner1"));
		spinner1.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner2 = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner2"));
		spinner2.setOnItemSelectedListener(new SpinnerSelectedListener2());
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(AddComplain.this, Submit, "正在提交数据...");
				
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});
		dialog = Background.Process(this, InitRegion, "数据加载中");

	}

	final Runnable InitRegion = new Runnable() {
		@Override
		public void run() {
			try {
				String response = HTTP.excute("getregionlist", "RestRegionService", "");
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					regions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Region>>() {
					}.getType());
					regionNames = new String[regions.size() + 1];
					regionNames[0] = "请选择区域";
					for (int i = 0; i < regionNames.length - 1; i++) {
						regionNames[i + 1] = regions.get(i).getAREANAME();
					}
					handler.sendEmptyMessage(GET_REGION_SUCCESS);
				} else {
					DialogUtil.showUIToast(AddComplain.this, "网络环境异常");
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(AddComplain.this, "网络环境异常");
				e.printStackTrace();
			}
		}
	};

	final Runnable Submit = new Runnable() {
		@Override
		public void run() {
			try {
				if (null == beComplainPerson || beComplainPerson.getText().toString().trim().equals("")) {
					DialogUtil.showUIToast(AddComplain.this, "被投诉人姓名不能为空");
					return;
				}
				if (null == regionId || regionId.equals("")) {
					DialogUtil.showUIToast(AddComplain.this, "请选择区域");
					return;
				}
				if (null == deptId || deptId.equals("")) {
					DialogUtil.showUIToast(AddComplain.this, "请选择部门");
					return;
				}

				if (null == mainTitle.getText() || mainTitle.getText().equals("")) {
					DialogUtil.showUIToast(AddComplain.this, "主题不能为空");
					return;
				}
				if (null == content.getText() || content.getText().toString().trim().equals("")) {
					DialogUtil.showUIToast(AddComplain.this, "投诉内容不能为空");
					return;
				}
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
				param.put("NAME", Constants.user.getUSERNAME());
				param.put("MOVEPHONE", Constants.user.getMOBILE());
				param.put("MAINTITLE", mainTitle.getText().toString());
				param.put("CONTENT", content.getText().toString());
				param.put("DEPARTMENTID", deptId);
				param.put("BECOMPLAINED_NAME", beComplainPerson.getText().toString().trim());
				param.put("BECOMPLAINED_DUTY", job.getText().toString());

				String response = HTTP.excute("submit", "RestComplainService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(AddComplain.this, "提交成功");
					AddComplain.this.finish();
				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(AddComplain.this, error);
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(AddComplain.this, "网络环境异常");
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
				param.put("AREAID", regions.get(regionNum).getAREAID().trim());
				String response = HTTP.excute("getDeptlistByAreaid", "RestRegionService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					depts = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());
					deptNames = new String[depts.size() + 1];
					deptNames[0] = "请选择部门";
					for (int i = 0; i < deptNames.length - 1; i++) {
						deptNames[i + 1] = depts.get(i).getNAME();
					}
					handler.sendEmptyMessage(GET_DEPTS_SUCCESS);

				} else {
					DialogUtil.showUIToast(AddComplain.this, "网络环境异常");
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(AddComplain.this, "网络环境异常");
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
				dialog = Background.Process(AddComplain.this, GetDeptList, "数据加载中");
				regionId = "not null ";
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
				deptId = depts.get(index - 1).getDEPTID();
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
			case GET_REGION_SUCCESS:
				adapter = new ArrayAdapter<String>(AddComplain.this, MSFWResource.getResourseIdByName(AddComplain.this, "layout", "my_spinner_item_consult"), regionNames);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner1.setAdapter(adapter);
				break;
			case GET_DEPTS_SUCCESS:
				adapter = new ArrayAdapter<String>(AddComplain.this, MSFWResource.getResourseIdByName(AddComplain.this, "layout", "my_spinner_item_consult"), deptNames);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner2.setAdapter(adapter);
				break;

			default:
				break;
			}
		}
	}



}
