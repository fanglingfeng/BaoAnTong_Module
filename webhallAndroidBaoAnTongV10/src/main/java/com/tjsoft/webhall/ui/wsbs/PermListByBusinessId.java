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
import com.tjsoft.webhall.db.FavoriteManage;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.ui.work.PermGuideContainer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 *3.21.3	根据协同业务流水号获取事项列表
 * 
 * @author Administrator
 * 
 */
public class PermListByBusinessId extends AutoDialogActivity {
	private ListView permList;
	private List<Permission> permissions;
	private LayoutInflater layoutInflater;
	private ProgressDialog mProgress;
	private MyHandler handler = new MyHandler();
	private final int GET_PERM_SUCCESS = 1;
	private String CBUSINESSID = "";
	private String CFLOWID = "";
	private String CITEMVERSION = "";
	private TextView typeName;
	private Button home;
	private RelativeLayout  back;
	private Intent intent;
	private TextView noData;
	private String SFYDSB = ""; // 是否可申报项列表：1为可申报列表
	private Context mContext;
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_permlist"));
		Constants.getInstance().addActivity(this);
		mContext = this;
		InitView();
		dialog = Background.Process(this, GetCInsBussinessList, getString(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "string", "tj_loding")));
	}

	private void InitView() {
		noData = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "noData"));
		layoutInflater = getLayoutInflater();
		permList = (ListView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "permList"));
		permList.setOnItemClickListener(new MyItemClick());
		permList.setOnItemLongClickListener(new MyItemLongClick());
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "empty"));
		permList.setEmptyView(empty);
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "home"));
		typeName = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "typeName"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PermListByBusinessId.this.finish();				
			}
		});
		CBUSINESSID = getIntent().getStringExtra("CBUSINESSID");
		CFLOWID = getIntent().getStringExtra("CFLOWID");
		CITEMVERSION = getIntent().getStringExtra("CITEMVERSION");
		SFYDSB = getIntent().getStringExtra("SFYDSB");
		String name = getIntent().getStringExtra("name");
		typeName.setText("您选择的是" + name + "，请继续选择办理事项");

	}

	private class MyItemLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
			new AlertDialog.Builder(PermListByBusinessId.this).setMessage("是否将该事项加入收藏夹？").setTitle(getString(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					FavoriteManage.add(permissions.get(position), PermListByBusinessId.this);
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
			Permission permission = permissions.get(position);
			intent = new Intent();
			intent.setClass(PermListByBusinessId.this, PermGuideContainer.class);
			intent.putExtra("PERMID", permission.getID());
			permission.setCBUSINESSID(CBUSINESSID);
			permission.setCITEMID(CFLOWID);
			permission.setCITEMVERSION(CITEMVERSION);
			intent.putExtra("permission", permission);
			startActivity(intent);
		}

	}

	final Runnable GetCInsBussinessList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("CBUSINESSID", CBUSINESSID);
				param.put("CFLOWID", CFLOWID);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				String response = HTTP.excute("getCInsBussinessList", "RestCooperateService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					permissions = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<Permission>>() {}.getType());
					handler.sendEmptyMessage(GET_PERM_SUCCESS);

				} else {
					DialogUtil.showUIToast(PermListByBusinessId.this, getString(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(PermListByBusinessId.this, getString(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_PERM_SUCCESS:
				permList.setAdapter(new PermAdapter());
				typeName.setText("您选择的是" + getIntent().getStringExtra("type") + "，事项总数："+permissions.size());
				break;

			default:
				break;
			}
		}
	}

	class PermAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return permissions.size();
		}

		@Override
		public Object getItem(int position) {
			return permissions.get(position);
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
				convertView = layoutInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_permlist_item"), parent, false);
				item.name = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "name"));
				item.deptName = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "deptName"));
				item.item_bg = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "item_bg"));
				item.isApply = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "isApply"));
				convertView.setTag(item);
			} else {
				item = (PermItem) convertView.getTag();
			}
			
			final Permission permission = permissions.get(position);
			String status = permission.getSTATUS();
			item.name.setText(permission.getSXZXNAME());
			item.deptName.setText(permission.getDEPTNAME());
			if (null != permission && null != status) {
				if(status.equals("")){//未申报
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_wsb"));
				}else if(status.equals("0")){//已申报
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_ysb"));
				}else if(status.equals("1")){//预审通过
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_ystg"));
				}else if(status.equals("2")){//办理中
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_blz"));
				}else if(status.equals("3")){//已办结
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_ybj"));
				}else if(status.equals("4")){//预审不通过
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_clbz"));
				}else if(status.equals("8")){//不予受理
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_bysl"));
				}else if(status.equals("9")){//暂存
					item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_zc"));
				}
			} else{
				item.isApply.setBackgroundResource(MSFWResource.getResourseIdByName(PermListByBusinessId.this, "drawable", "tj_item_status_wsb"));
			}

			return convertView;
		}

		public final class PermItem {
			public TextView name;
			public TextView deptName;
			public RelativeLayout item_bg;
			public ImageView isApply;

		}
	}

}
