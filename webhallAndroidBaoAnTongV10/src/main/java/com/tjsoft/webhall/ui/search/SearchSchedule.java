package com.tjsoft.webhall.ui.search;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.ShowPopupMoreUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Log;
import com.tjsoft.webhall.entity.ScheduleBean;

import org.json.JSONObject;

/**
 * 查找结果
 * 
 * @author Administrator
 * 
 */
public class SearchSchedule extends AutoDialogActivity {
	private ScheduleBean schedule;
	private TextView BSNUM, SXZXNAME, DEPTNAME, APPNAME, APPCOMPANY, APPLYTIME, STATUS, CSTATUS,LIMITDAY;
	private TextView noData;
	private LinearLayout info;
	private Button back;
	private ListView listView;
	private LayoutInflater layoutInflater;
	private Button help;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_schedule"));
		Constants.getInstance().addActivity(this);
		InitView();
		dialog = Background.Process(this, Search, "正在搜索...");
	}

	private void InitView() {
		layoutInflater = getLayoutInflater();
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchSchedule.this.finish();
			}
		});
		help=(Button)findViewById(MSFWResource.getResourseIdByName(this, "id", "help"));
		help.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowPopupMoreUtil.showPopupMore(v, SearchSchedule.this, schedule.getPERMID(),"查看进度",schedule.getSXZXNAME(),schedule.getDEPTNAME(),null);;
			}
		});
		listView = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));

		BSNUM = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "BSNUM"));
		SXZXNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "SXZXNAME"));
		DEPTNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "DEPTNAME"));
		APPNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "APPNAME"));
		APPCOMPANY = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "APPCOMPANY"));
		APPLYTIME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "APPLYTIME"));
		STATUS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "STATUS"));
		CSTATUS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "CSTATUS"));
		LIMITDAY = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "LIMIT_DAY"));

		noData = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "noData"));
		info = (LinearLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "info"));
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return schedule.getLOGS().size();
		}

		@Override
		public Object getItem(int position) {
			return schedule.getLOGS().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			PermItem item;
			if (null == convertView) {
				item = new PermItem();
				convertView = layoutInflater.inflate(MSFWResource.getResourseIdByName(SearchSchedule.this, "layout", "tj_perm_flow_item"), parent, false);
				item.NODENAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(SearchSchedule.this, "id", "NODENAME"));
				item.INFO = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(SearchSchedule.this, "id", "INFO"));
				item.imgNext = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(SearchSchedule.this, "id", "imgNext"));
				convertView.setTag(item);
			} else {
				item = (PermItem) convertView.getTag();
			}

			if (position == 0||(position == schedule.getLOGS().size() - 1)) {
				item.INFO.setVisibility(View.INVISIBLE);
			} else {
				item.INFO.setVisibility(View.VISIBLE);
			}

			//
			if ((position == schedule.getLOGS().size() - 1) && (schedule.getSTATUS().equals("1") || schedule.getSTATUS().equals("2"))) {// 结束
				item.NODENAME.setBackgroundResource(MSFWResource.getResourseIdByName(SearchSchedule.this, "drawable", "tj_ic_flow_stop"));
				item.imgNext.setVisibility(View.GONE);
			} else if ((position == schedule.getLOGS().size() - 1) && !(schedule.getSTATUS().equals("1") || schedule.getSTATUS().equals("2"))) {// 办理中
				item.NODENAME.setBackgroundResource(MSFWResource.getResourseIdByName(SearchSchedule.this, "drawable", "tj_ic_flow_ing"));
				item.imgNext.setVisibility(View.GONE);
			}

			else {
				item.NODENAME.setBackgroundResource(MSFWResource.getResourseIdByName(SearchSchedule.this, "drawable", "tj_ic_flow_pass"));// 正常流程
				item.imgNext.setVisibility(View.VISIBLE);
			}

			item.NODENAME.setText(schedule.getLOGS().get(position).getNODENAME());
			if(schedule.getLOGS().get(position).getNODENAME().equals("预受理")||schedule.getLOGS().get(position).getNODENAME().equals("预审")){
				item.INFO.setText("办理时间：" + schedule.getLOGS().get(position).getHANDLETIME()+"\n办理意见：" + schedule.getLOGS().get(position).getIDEA());
			}
			else{
				
				item.INFO.setText("办理时间：" + schedule.getLOGS().get(position).getHANDLETIME());
			}

			return convertView;
		}

		public final class PermItem {
			public TextView NODENAME;
			public TextView INFO;
			public TextView deptName;
			public RelativeLayout item_bg;
			public ImageView imgNext;

		}
	}

	final Runnable Search = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("BSNUM", getIntent().getStringExtra("BSNUM"));
				param.put("APPNAME", getIntent().getStringExtra("APPNAME"));
				String response = HTTP.excute("search", "RestBussinessService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					schedule = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), ScheduleBean.class);
					runOnUiThread(new Runnable() {
						public void run() {

							System.out.println("####  " + schedule.getLOGS().size());
							schedule.getLOGS().add(0, new Log("", "开始", "", ""));
							if (schedule.getSTATUS().equals("1") || schedule.getSTATUS().equals("2")) {
								schedule.getLOGS().add(new Log("", "结束", "", ""));
							} else {
								schedule.getLOGS().add(new Log("", "办理中", "", ""));

							}

							listView.setAdapter(new MyAdapter());

							if (null == schedule || null == schedule.getAPPNAME()) {
								noData.setVisibility(View.VISIBLE);
								info.setVisibility(View.GONE);
							} else {
								BSNUM.setText(schedule.getBSNUM());
								SXZXNAME.setText(schedule.getSXZXNAME());
								DEPTNAME.setText(schedule.getDEPTNAME());
								APPNAME.setText(schedule.getAPPNAME());
								APPCOMPANY.setText(schedule.getAPPCOMPANY());
								APPLYTIME.setText(schedule.getAPPLYTIME());
								STATUS.setText(schedule.getSTATUS());
								CSTATUS.setText(schedule.getCSTATUS());
								if(!TextUtils.isEmpty(schedule.getLIMITDAYS())){
									LIMITDAY.setText(schedule.getLIMITDAYS()+"个工作日,具体结果以短信通知为主");	
								}else{
									LIMITDAY.setText("暂无");	
								}
							}

						}
					});
				} else {
					DialogUtil.showUIToast(SearchSchedule.this, json.getString("error"));
					finish();
				}
			} catch (Exception e) {
				e.printStackTrace();				
				DialogUtil.showUIToast(SearchSchedule.this, getString(MSFWResource.getResourseIdByName(SearchSchedule.this, "string", "tj_occurs_error_network")));
				finish();
			}
		}
	};

}
