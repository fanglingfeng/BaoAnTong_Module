package com.tjsoft.webhall.ui.wsbs;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.MyBrowser;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.PermInfo;
import com.tjsoft.webhall.entity.PermInfoBean;

public class PermInfoActivity extends AutoDialogActivity {

	private ListView listView;
	private TextView DESCRIPT;

	private RelativeLayout back; // 返回按钮
	private Intent intent;
	private LayoutInflater layoutInflater;
	private String[] urls = {"http://bsdt.baoan.gov.cn:8090/statistics/qu.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/xinan.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/xixiang.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/fuyong.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/shajing.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/songgang.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/shiyan.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/hangcheng.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/fuhai.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/xinqiao.html",
			"http://bsdt.baoan.gov.cn:8090/statistics/yanluo.html"
			};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_perms_info"));
		Constants.getInstance().addActivity(this);
		layoutInflater = getLayoutInflater();
		listView = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));
		DESCRIPT = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "DESCRIPT"));
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));

		Background.Process(PermInfoActivity.this, getPermCountList, "正在登录...");
		/*layQu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(PermInfoActivity.this, MyBrowser.class);
				intent.putExtra("title", "事项清单");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn:8090/statistics/qu.html");
				startActivity(intent);

			}
		});
		layXA.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(PermInfoActivity.this, MyBrowser.class);
				intent.putExtra("title", "事项清单");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn:8090/statistics/xinan.html");
				startActivity(intent);

			}
		});
		layXX.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(PermInfoActivity.this, MyBrowser.class);
				intent.putExtra("title", "事项清单");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn:8090/statistics/xixiang.html");
				startActivity(intent);

			}
		});
		layFY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(PermInfoActivity.this, MyBrowser.class);
				intent.putExtra("title", "事项清单");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn:8090/statistics/fuyong.html");
				startActivity(intent);

			}
		});
		laySJ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(PermInfoActivity.this, MyBrowser.class);
				intent.putExtra("title", "事项清单");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn:8090/statistics/shajing.html");
				startActivity(intent);

			}
		});
		laySG.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(PermInfoActivity.this, MyBrowser.class);
				intent.putExtra("title", "事项清单");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn:8090/statistics/songgang.html");
				startActivity(intent);

			}
		});
		laySY.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(PermInfoActivity.this, MyBrowser.class);
				intent.putExtra("title", "事项清单");
				intent.putExtra("url", "http://bsdt.baoan.gov.cn:8090/statistics/shiyan.html");
				startActivity(intent);

			}
		});*/
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	final Runnable getPermCountList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();

				String response = HTTP.excute("getPermCountList", "RestPermissionitemService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final PermInfo info = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), PermInfo.class);
					runOnUiThread(new Runnable() {
						public void run() {
							listView.setAdapter(new MyAdapter(info));
							DESCRIPT.setText(info.getDESCRIPT());
						}
					});

				} else {
					DialogUtil.showUIToast(PermInfoActivity.this, getString(MSFWResource.getResourseIdByName(PermInfoActivity.this, "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(PermInfoActivity.this, getString(MSFWResource.getResourseIdByName(PermInfoActivity.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class MyAdapter extends BaseAdapter {
		PermInfo info;

		public MyAdapter(PermInfo info) {
			this.info = info;
		}

		@Override
		public int getCount() {
			return info.getItems().size();
		}

		@Override
		public Object getItem(int position) {
			return info.getItems().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			PermItem item;
			if (null == convertView) {
				item = new PermItem();
				convertView = layoutInflater.inflate(MSFWResource.getResourseIdByName(PermInfoActivity.this, "layout", "tj_perm_info_list_item"), parent, false);
				item.AREANAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(PermInfoActivity.this, "id", "AREANAME"));
				item.PERMNUM = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(PermInfoActivity.this, "id", "PERMNUM"));
				item.MOBILENUM = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(PermInfoActivity.this, "id", "MOBILENUM"));
				convertView.setTag(item);
			} else {
				item = (PermItem) convertView.getTag();
			}

			PermInfoBean bean = info.getItems().get(position);
			item.AREANAME.setText(bean.getAREANAME());
			item.PERMNUM.setText(bean.getPERMNUM());
			item.MOBILENUM.setText(bean.getMOBILENUM());

			if (position % 2 == 0) {
				convertView.setBackgroundColor(Color.parseColor("#F7F7F7"));
			}
			else{
				convertView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					intent = new Intent();
					intent.setClass(PermInfoActivity.this, MyBrowser.class);
					intent.putExtra("title", "事项清单");
					intent.putExtra("url", urls[position]);
					startActivity(intent);
					
				}
			});
			
			

			return convertView;
		}
		
		
		

		public final class PermItem {
			public TextView AREANAME;
			public TextView PERMNUM;
			public TextView MOBILENUM;

		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
