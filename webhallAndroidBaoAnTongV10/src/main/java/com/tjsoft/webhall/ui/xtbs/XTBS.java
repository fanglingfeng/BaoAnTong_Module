package com.tjsoft.webhall.ui.xtbs;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.ui.search.Search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;

/**
 * 协同办事
 * @author Administrator
 *
 */
public class XTBS extends AutoDialogActivity {
	private Button tag1,tag2,more,search;
	private ListView deptList;
	private String[] register ={"内资有限责任公司设立登记","内资股份有限公司设立登记","内资分公司设立登记","内资非公司企业法人开业登记","内资合伙企业设立登记","内资合伙企业分支机构设立登记","个人独资企业设立登记","个人独资企业分支机构设立登记","个体工商户开业登记","农民专业合作社设立登记","农民专业合作社分支机构设立登记","经营单位营业登记","企业集团设立","外商投资企业开业登记","外商投资合伙企业分支机构开业登记","外商投资企业分支机构开业登记","外国企业常驻代表机构设立登记","外国（地区）企业在中国境内从事生产经营活动登记注册","外国（地区）企业在中国境内从事生产经营活动的登记注册"};
	private List<Map<String, Object>> deptDatas;
	private Intent intent;
	private ScrollView lay2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_xtbs"));
		Constants.getInstance().addActivity(this);
		tag1 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "tag1"));
		tag2 = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "tag2"));
		more = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "more"));
		search = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "search"));
		lay2 = (ScrollView) findViewById(MSFWResource.getResourseIdByName(this, "id", "lay2"));
		initSetOnListener();
		deptDatas = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < register.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("NAME", register[i]);
			deptDatas.add(map);
		}
		deptList = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "deptList"));
		SimpleAdapter adapter = new SimpleAdapter(XTBS.this, deptDatas, MSFWResource.getResourseIdByName(this, "layout", "tj_deptlist_item"), new String[] { "NAME" }, new int[] { MSFWResource.getResourseIdByName(XTBS.this, "id", "deptName") });
		deptList.setAdapter(adapter);
	}

	private void initSetOnListener() {
		tag1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tag2.setBackgroundColor(getResources().getColor(MSFWResource.getResourseIdByName(XTBS.this, "color", "tj_transparent")));
				tag2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(XTBS.this, "color", "tj_black")));
				tag1.setBackgroundDrawable(getResources().getDrawable(MSFWResource.getResourseIdByName(XTBS.this, "drawable", "tj_oval_btn_bg")));
				tag1.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(XTBS.this, "color", "tj_my_green")));
				deptList.setVisibility(View.VISIBLE);
				lay2.setVisibility(View.GONE);
			}
		});		
		tag2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				tag1.setBackgroundColor(getResources().getColor(MSFWResource.getResourseIdByName(XTBS.this, "color", "tj_transparent")));
				tag1.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(XTBS.this, "color", "tj_black")));
				tag2.setBackgroundDrawable(getResources().getDrawable(MSFWResource.getResourseIdByName(XTBS.this, "drawable", "tj_oval_btn_bg")));
				tag2.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(XTBS.this, "color", "tj_my_green")));
				deptList.setVisibility(View.GONE);
				lay2.setVisibility(View.VISIBLE);
			}
		});		
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});		
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(XTBS.this, Search.class);
				startActivity(intent);
			}
		});		
	}

}
