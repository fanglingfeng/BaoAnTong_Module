package com.tjsoft.webhall.ui.bsdt;

import java.util.List;

import org.json.JSONObject;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.DateBean;
import com.tjsoft.webhall.entity.Dept;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.Region;
import com.tjsoft.webhall.entity.ReserveTime;

public class ReserveSubmit extends AutoDialogActivity {
	private RadioGroup radioGroup;
	private Button submit;
	private RelativeLayout back;
	private Button date;
	private Spinner spinner1, spinner2, spinner3, spinner4;
	private String deptId = "";
	private String regionId = "";
	private String permId = "";
	public static String[] regionNames;
	public static String[] reserveDates;
	private int regionNum;
	public static List<Region> regions;
	private List<Dept> depts;
	private List<Permission> permissions;
	public static String[] permissionsNames;
	public static String[] deptNames;
	private MyHandler handler = new MyHandler();
	private final int GET_REGION_SUCCESS = 1;
	private final int GET_DEPTS_SUCCESS = 2;
	private final int GET_PERMISSION_SUCCESS = 3;
	private final int GET_DATE_SUCCESS = 4;
	private ArrayAdapter<String> adapter;
	private EditText name, phone, code;
	private String dateStr = "";
	private String selectTime = "";
	private List<DateBean> dateBeans;
	private Permission permission;
	private LinearLayout lay1;
	private EditText text2, text3;
	private String BSNUM;
	private Context mContext;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		mContext = this;
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_reserve_submit"));
		permission = (Permission) getIntent().getSerializableExtra("permission");
		BSNUM = getIntent().getStringExtra("BSNUM");
		InitView();
		initSetOnListener();
		if (null != permission) {
			lay1.setVisibility(View.GONE);
			spinner2.setVisibility(View.GONE);
			spinner3.setVisibility(View.GONE);

			text2.setVisibility(View.VISIBLE);
			text3.setVisibility(View.VISIBLE);

			text2.setText("  " + permission.getDEPTNAME());
			text3.setText("  " + permission.getSXZXNAME());
			regionId = "not null";
			deptId = permission.getDEPTID();
			permId = permission.getID();
			dialog = Background.Process(ReserveSubmit.this, InitTime, "数据加载中");
		}
	}
	
	@Override
	protected void onDestroy() {
		StatisticsTools.end("预约页面", null, null);
		super.onDestroy();
	}

	private void initSetOnListener() {
		submit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = Background.Process(ReserveSubmit.this, Submit, "正在提交");
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ReserveSubmit.this.finish();
			}
		});
	}

	private void InitView() {

		radioGroup = (RadioGroup) findViewById(MSFWResource.getResourseIdByName(this, "id", "radioGroup"));
		radioGroup.setOnCheckedChangeListener(new MyChangeListener());

		lay1 = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay1"));
		text2 = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "text2"));
		text3 = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "text3"));

		spinner1 = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner1"));
		spinner2 = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner2"));
		spinner3 = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner3"));
		spinner4 = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner4"));
		spinner1.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner2.setOnItemSelectedListener(new SpinnerSelectedListener2());
		spinner3.setOnItemSelectedListener(new SpinnerSelectedListener3());
		spinner4.setOnItemSelectedListener(new SpinnerSelectedListener4());

		dialog = Background.Process(this, InitRegion, "数据加载中...");

		name = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		phone = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "phone"));
		code = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "code"));
		submit = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "submit"));
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		name.setText(TextUtils.isEmpty(Constants.user.getREALNAME()) ? "" : Constants.user.getREALNAME());
		phone.setText(TextUtils.isEmpty(Constants.user.getMOBILE()) ? "" : Constants.user.getMOBILE());
		code.setText(TextUtils.isEmpty(Constants.user.getCODE()) ? "" : Constants.user.getCODE());
	}

	final Runnable InitTime = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("PERMID", permId);
				param.put("DEPTID", deptId);
				String response = HTTP.excute("getReserveDay", "RestOnlineReserveService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					dateBeans = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<DateBean>>() {
					}.getType());
					reserveDates = new String[dateBeans.size() + 1];
					reserveDates[0] = "请选择日期";
					for (int i = 0; i < reserveDates.length - 1; i++) {
						reserveDates[i + 1] = dateBeans.get(i).getRESERVEDATE();
					}
					handler.sendEmptyMessage(GET_DATE_SUCCESS);

				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(ReserveSubmit.this, error);
					}
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

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
					DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "occurs_error_network")));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

	class SpinnerSelectedListener implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			if (index == 0) {
				regionId = "";
				// adapter = new ArrayAdapter<String>(ReserveSubmit.this,
				// R.layout.my_spinner_item_consult, new String[] { "" });
				// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				// spinner2.setAdapter(adapter);
			} else {
				regionNum = index - 1;
				dialog = Background.Process(ReserveSubmit.this, GetDeptList, "数据加载中");
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
				dialog = Background.Process(ReserveSubmit.this, GetPermList, "数据加载中");
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener3 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			if (index == 0) {
				permId = "";
			} else {
				permId = permissions.get(index - 1).getID();
				dialog = Background.Process(ReserveSubmit.this, InitTime, "数据加载中");
			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	class SpinnerSelectedListener4 implements OnItemSelectedListener {
		public void onItemSelected(AdapterView<?> arg0, View arg1, int index, long arg3) {
			if (index == 0) {
				dateStr = "";
			} else {
				dateStr = dateBeans.get(index - 1).getRESERVEDATE();

				dialog = Background.Process(ReserveSubmit.this, GetReserveDayTime, "数据加载中");

			}
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	/**
	 * 3.11.7 获取当前日期可预约时间段
	 */
	final Runnable GetReserveDayTime = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DEPTID", deptId);
				param.put("PERMID", permId);
				param.put("DAY", dateStr);
				String response = HTTP.excute("GetReserveDayTime", "RestOnlineReserveService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final List<ReserveTime> reserveTimes = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<ReserveTime>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {
							radioGroup.removeAllViews();
							if(reserveTimes!=null&&reserveTimes.size()>0){
								for (int i = 0; i < reserveTimes.size(); i++) {
									RadioButton radioButton = new RadioButton(ReserveSubmit.this);
									if (reserveTimes.get(i).getNOWNUM().equals(reserveTimes.get(i).getRESERVENUM())) {
										radioButton.setText(reserveTimes.get(i).getRESERVETIME() + " (已满)");
										radioButton.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(mContext, "color", "tj_red")));
										radioButton.setClickable(false);
									} else {
										radioButton.setText(reserveTimes.get(i).getRESERVETIME() + "  (" + reserveTimes.get(i).getNOWNUM() + "/" + reserveTimes.get(i).getRESERVENUM() + ")");
									}
									radioGroup.addView(radioButton);

								}
							}else{
								DialogUtil.showUIToast(ReserveSubmit.this, "当前无可预约时间段，暂不可预约");
							}

						}
					});

				} else {
					DialogUtil.showUIToast(ReserveSubmit.this, "当前无可预约时间段，暂不可预约");
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	final Runnable GetPermList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("DEPTID", deptId);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				String response = HTTP.excute("getPermlistByDeptid", "RestSysDeptService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					permissions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Permission>>() {
					}.getType());
					permissionsNames = new String[permissions.size() + 1];
					permissionsNames[0] = "请选择预约事项";
					for (int i = 0; i < permissionsNames.length - 1; i++) {
						permissionsNames[i + 1] = permissions.get(i).getSXZXNAME();
					}
					handler.sendEmptyMessage(GET_PERMISSION_SUCCESS);

				} else {
					DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	final Runnable GetDeptList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("AREAID", regions.get(regionNum).getAREAID().trim());
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
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
					DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	final Runnable Submit = new Runnable() {
		@Override
		public void run() {
			try {

				if (null == regionId || regionId.equals("")) {
					DialogUtil.showUIToast(ReserveSubmit.this, "请选择区域");
					return;
				}
				if (null == deptId || deptId.equals("")) {
					DialogUtil.showUIToast(ReserveSubmit.this, "请选择部门");
					return;
				}
				if (null == permId || permId.equals("")) {
					DialogUtil.showUIToast(ReserveSubmit.this, "请选择预约事项");
					return;
				}
				if (null == dateStr || dateStr.equals("")) {
					DialogUtil.showUIToast(ReserveSubmit.this, "请选择预约时间");
					return;
				}
				if (null == selectTime || selectTime.equals("")) {
					DialogUtil.showUIToast(ReserveSubmit.this, "请选择预约时间段");
					return;
				}

				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("APPLICANTID", Constants.user.getUSER_ID());
				param.put("DEPTID", deptId);
				param.put("PERMID", permId);
				param.put("RESERVEDATE", dateStr);
				param.put("RESERVETIME", selectTime);
				param.put("BSNUM", BSNUM);

				String response = HTTP.excute("submit", "RestOnlineReserveService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ReserveSubmit.this, "预约成功！");
					finish();
				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(ReserveSubmit.this, error);
					}
				}
			} catch (Exception e) {
				DialogUtil.showUIToast(ReserveSubmit.this, getString(MSFWResource.getResourseIdByName(ReserveSubmit.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

	class MyChangeListener implements OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			RadioButton tempButton = (RadioButton) findViewById(checkedId);
			String temp = tempButton.getText().toString();
			selectTime = ((String) temp.subSequence(0, temp.indexOf("("))).trim();
			System.out.println(selectTime);
		}

	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_REGION_SUCCESS:
				adapter = new ArrayAdapter<String>(ReserveSubmit.this, MSFWResource.getResourseIdByName(mContext, "layout", "tj_my_spinner_item_consult"), regionNames);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner1.setAdapter(adapter);
				break;
			case GET_DEPTS_SUCCESS:
				adapter = new ArrayAdapter<String>(ReserveSubmit.this, MSFWResource.getResourseIdByName(mContext, "layout", "tj_my_spinner_item_consult"), deptNames);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner2.setAdapter(adapter);
				break;
			case GET_PERMISSION_SUCCESS:
				adapter = new ArrayAdapter<String>(ReserveSubmit.this, MSFWResource.getResourseIdByName(mContext, "layout", "tj_my_spinner_item_consult"), permissionsNames);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner3.setAdapter(adapter);
				break;
			case GET_DATE_SUCCESS:
				adapter = new ArrayAdapter<String>(ReserveSubmit.this, MSFWResource.getResourseIdByName(mContext, "layout", "tj_my_spinner_item_consult"), reserveDates);
				adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner4.setAdapter(adapter);
				break;

			default:
				break;
			}
		}
	}

	class DateListener implements OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			monthOfYear++;
			if (monthOfYear < 10) {
				String month = "0" + monthOfYear;
				dateStr = year + "-" + month + "-" + dayOfMonth;
			} else {
				dateStr = year + "-" + monthOfYear + "-" + dayOfMonth;
			}
			date.setText(dateStr);
			dialog = Background.Process(ReserveSubmit.this, InitTime, "初始化预约时间...");
		}

	}

}
