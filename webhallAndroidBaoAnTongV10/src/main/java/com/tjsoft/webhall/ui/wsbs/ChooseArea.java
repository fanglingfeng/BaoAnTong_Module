package com.tjsoft.webhall.ui.wsbs;

import java.util.List;

import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Region;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 区域选择页面
 * @author Administrator
 *
 */
public class ChooseArea extends AutoDialogActivity {
	private ListView listView;
	private List<Region> regions;
	private String AREAID;
	private Button back;
	private Region parent;
	private LayoutInflater inflater;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_choose_area"));
		Constants.areaActivityList.add(this);
		mContext = this;
		inflater = getLayoutInflater();
		listView = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ChooseArea.this.finish();				
			}
		});
		AREAID = getIntent().getStringExtra("AREAID");
		parent = (Region) getIntent().getSerializableExtra("parent");
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				AREAID = regions.get(position).getAREAID();
				if (position == 0) {
					Constants.AREAID = AREAID;
					Constants.AREANAME = regions.get(position).getAREANAME();
					for (Activity activity : Constants.areaActivityList) {
						activity.finish();
					}
				} else {
					Intent intent = new Intent();
					intent.putExtra("AREAID", AREAID);
					parent = regions.get(position);
					intent.putExtra("parent", parent);
					intent.setClass(ChooseArea.this, ChooseArea.class);
					startActivity(intent);
				}
			}
		});
		dialog = Background.Process(ChooseArea.this, InitRegion, "数据加载中");
	}
	class AreaAdapter extends BaseAdapter {


		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return regions.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return regions.size();
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
				convertView = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_arealist_item"), null);
				item.deptName = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "deptName"));
				convertView.setTag(item);
			} else {
				item = (MenuItem) convertView.getTag();
			}
			item.deptName.setText(regions.get(position).getAREANAME());
			if(0 == position){
				item.deptName.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ChooseArea.this, "color", "tj_orange")));;
			}
			else{
				item.deptName.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(ChooseArea.this, "color", "tj_black")));;
			}
			return convertView;

		}

		public final class MenuItem {
			TextView deptName;
		}

	}
	final Runnable InitRegion = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PARENTID", AREAID);
				String response = HTTP.excute("getRegionlistByParentid", "RestRegionService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					regions = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Region>>() {
					}.getType());
					if (null != parent) {
						regions.add(0, parent);
					}
					
					runOnUiThread(new Runnable() {
						public void run() {
							listView.setAdapter(new AreaAdapter());
						}
					});
				} else {
					DialogUtil.showUIToast(ChooseArea.this, getString(MSFWResource.getResourseIdByName(ChooseArea.this, "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ChooseArea.this, getString(MSFWResource.getResourseIdByName(ChooseArea.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};

}
