package com.tjsoft.webhall.ui.wsbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Dept;

/**
 * 部门列表界面
 * 
 * @author Administrator
 * 
 */
public class DeptList extends AutoDialogActivity {
	private ListView deptList;
	private Dept dept;
	private List<Dept> depts;
	private List<Map<String, Object>> deptDatas;
	private RelativeLayout back;
	private String AREAID = "440306";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_dept_list"));
		Constants.getInstance().addActivity(this);
		Constants.WSBS_PATH=getIntent().getIntExtra("WSBSFLAG", 0);

		initView();
		if (getIntent().getBooleanExtra("isBAOAN", false)) {//区

			dialog = Background.Process(this, GetDeptList, "数据加载中");
		} else {//街道
			dept = (Dept) getIntent().getSerializableExtra("dept");
			dialog = Background.Process(this, GetDeptlistByParentid, "数据加载中");
		}

	}

	/**
	 * 根据areaid获取部门
	 */
	final Runnable GetDeptList = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("AREAID", AREAID);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				param.put("RESERVEONE", "4");
				String response = HTTP.excute("getDeptlistByAreaid", "RestRegionService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					depts = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());
					if (null != depts && depts.size() > 0) {

						runOnUiThread(new Runnable() {
							public void run() {
								deptDatas = new ArrayList<Map<String, Object>>();
								for (int i = 0; i < depts.size(); i++) {
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("DEPTID", depts.get(i).getDEPTID());
									map.put("SHORTNAME", depts.get(i).getSHORTNAME()+"（"+depts.get(i).getPERMNUM()+"）");
									map.put("AREAID", depts.get(i).getAREAID());
									map.put("ORDERID", depts.get(i).getORDERID());
									map.put("CNUM", depts.get(i).getCNUM());
									map.put("NAME", depts.get(i).getNAME()+"（"+depts.get(i).getPERMNUM()+"）");
									deptDatas.add(map);
								}

								SimpleAdapter adapter = new SimpleAdapter(DeptList.this, deptDatas, MSFWResource.getResourseIdByName(DeptList.this, "layout", "tj_deptlist_item"), new String[] { "SHORTNAME" }, new int[] { MSFWResource.getResourseIdByName(DeptList.this, "id", "deptName") });
								deptList.setAdapter(adapter);
								deptList.setOnItemClickListener(new MyItemClick());
							}
						});

					}
				} else {
					DialogUtil.showUIToast(DeptList.this, getString(MSFWResource.getResourseIdByName(DeptList.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(DeptList.this, getString(MSFWResource.getResourseIdByName(DeptList.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	/**
	 * 获取部门
	 */
	final Runnable GetDeptlistByParentid = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("PARENTID", dept.getDEPTID());
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				param.put("RESERVEONE", "5");

				String response = HTTP.excute("getDeptlistByParentid", "RestSysDeptService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					depts = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Dept>>() {
					}.getType());
					if (null != depts && depts.size() > 0) {

						runOnUiThread(new Runnable() {
							public void run() {
								deptDatas = new ArrayList<Map<String, Object>>();
								for (int i = 0; i < depts.size(); i++) {
									Map<String, Object> map = new HashMap<String, Object>();
									map.put("DEPTID", depts.get(i).getDEPTID());
									map.put("SHORTNAME", depts.get(i).getSHORTNAME()+"（"+depts.get(i).getPERMNUM()+"）");
									map.put("AREAID", depts.get(i).getAREAID());
									map.put("ORDERID", depts.get(i).getORDERID());
									map.put("CNUM", depts.get(i).getCNUM());
									map.put("NAME", depts.get(i).getNAME()+"（"+depts.get(i).getPERMNUM()+"）");
									deptDatas.add(map);
								}

								SimpleAdapter adapter = new SimpleAdapter(DeptList.this, deptDatas, MSFWResource.getResourseIdByName(DeptList.this, "layout", "tj_deptlist_item"), new String[] { "SHORTNAME" }, new int[] { MSFWResource.getResourseIdByName(DeptList.this, "id", "deptName") });
								deptList.setAdapter(adapter);
								deptList.setOnItemClickListener(new MyItemClick());
							}
						});

					}
				} else {
					DialogUtil.showUIToast(DeptList.this, getString(MSFWResource.getResourseIdByName(DeptList.this, "string", "tj_occurs_error_network")));
					finish();

				}

			} catch (Exception e) {
				DialogUtil.showUIToast(DeptList.this, getString(MSFWResource.getResourseIdByName(DeptList.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	private class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(DeptList.this, PermListByDept.class);
			intent.putExtra("name", deptDatas.get(position).get("SHORTNAME").toString());
			intent.putExtra("DEPTID", deptDatas.get(position).get("DEPTID").toString());
			intent.putExtra("SFYDSB", "0");
			intent.putExtra("WSBSFLAG", Constants.WSBS_PATH);
			startActivity(intent);

		}

	}

	private void initView() {
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		deptList = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "deptList"));
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		deptList.setEmptyView(empty);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

}
