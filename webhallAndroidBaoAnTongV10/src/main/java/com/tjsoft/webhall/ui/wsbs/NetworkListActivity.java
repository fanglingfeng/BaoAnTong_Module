package com.tjsoft.webhall.ui.wsbs;

import java.util.List;

import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.NetWorkBean;
import com.tjsoft.webhall.ui.user.Login;

/**
 * 服务网点列表
 * 
 * @author Administrator
 * 
 */
public class NetworkListActivity extends AutoDialogActivity {

	private ListView permList;
	private List<NetWorkBean> networks;
	private LayoutInflater layoutInflater;
	private MyHandler handler = new MyHandler();
	private final int GET_NETWORK_SUCCESS = 1;
	private String SORTCODE = "";
	private TextView typeName;
	private Button  home;
	private RelativeLayout back;
	private Intent intent;
	private TextView noData;
	private ProgressDialog dialog;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_network_list"));
		Constants.getInstance().addActivity(this);
		mContext = this;
		InitView();
		dialog = Background.Process(this, getNetworkList, getString(MSFWResource.getResourseIdByName(NetworkListActivity.this, "string", "tj_loding")));

	}

	@Override
	protected void onDestroy() {
		StatisticsTools.end(getIntent().getStringExtra("type"), null, null);
		dialog.dismiss();
		super.onDestroy();
	}

	private void InitView() {
		noData = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "noData"));
		layoutInflater = getLayoutInflater();
		permList = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "permList"));
		permList.setOnItemClickListener(new MyItemClick());
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		permList.setEmptyView(empty);
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		typeName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "typeName"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				NetworkListActivity.this.finish();
			}
		});
//		home.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Constants.user = null;
//				DialogUtil.showUIToast(NetworkListActivity.this, "您已退出登陆！");
//				FileUtil.Write(NetworkListActivity.this, "password", "");
//				SharedPreferences config = NetworkListActivity.this.getSharedPreferences("config", MODE_PRIVATE);
//				Editor editor = config.edit();
//				editor.putBoolean("autoLogon", false);
//				editor.commit();
//
//			}
//		});
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Constants.getInstance().exit();
			}
		});
		SORTCODE = getIntent().getStringExtra("SORTCODE");

	}

	class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			intent = new Intent();
			intent.putExtra("netWorkBean", networks.get(position));
			intent.setClass(NetworkListActivity.this, NetworkDetail.class);
			startActivity(intent);
		}

	}

	final Runnable getNetworkList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();

				String response = HTTP.excute("getNetworkList", "RestNetworkService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					networks = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<NetWorkBean>>() {
					}.getType());
					handler.sendEmptyMessage(GET_NETWORK_SUCCESS);

				} else {
					DialogUtil.showUIToast(NetworkListActivity.this, getString(MSFWResource.getResourseIdByName(NetworkListActivity.this, "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(NetworkListActivity.this, getString(MSFWResource.getResourseIdByName(NetworkListActivity.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_NETWORK_SUCCESS:
				permList.setAdapter(new MyAdapter());
				break;

			default:
				break;
			}
		}
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return networks.size();
		}

		@Override
		public Object getItem(int position) {
			return networks.get(position);
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
				convertView = layoutInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_network_list_item"), parent, false);
				item.NETWORKPHONE = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "NETWORKPHONE"));
				item.NETWORKNAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "NETWORKNAME"));
				item.NETWORKADDRESS = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "NETWORKADDRESS"));
				item.type = (LinearLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "type"));
				item.typeContent = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "typeContent"));

				convertView.setTag(item);
			} else {
				item = (PermItem) convertView.getTag();
			}

			NetWorkBean bean = networks.get(position);
			item.NETWORKPHONE.setText(bean.getNETWORKPHONE());
			item.NETWORKNAME.setText(bean.getNETWORKNAME());
			item.NETWORKADDRESS.setText(bean.getNETWORKADDRESS());
			if (position == 0) {
				item.type.setVisibility(View.VISIBLE);
				item.typeContent.setText("区级行政服务中心");

			} else if (position == 1) {
				item.type.setVisibility(View.VISIBLE);
				item.typeContent.setText("街道行政服务中心");
			} else {
				item.type.setVisibility(View.GONE);
			}

			return convertView;
		}

		public final class PermItem {
			public LinearLayout type;
			public TextView typeContent;
			public TextView NETWORKPHONE;
			public TextView NETWORKNAME;
			public TextView NETWORKADDRESS;
			public RelativeLayout item_bg;
			public ImageView isApply;
		}
	}

}
