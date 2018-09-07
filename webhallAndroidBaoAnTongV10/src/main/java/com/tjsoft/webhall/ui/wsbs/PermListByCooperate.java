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
 * 协同办事事项列表
 * 
 * @author Administrator
 * 
 */
public class PermListByCooperate extends AutoDialogActivity {
	private ListView permList;
	private List<Permission> permissions;
	private LayoutInflater layoutInflater;
	private ProgressDialog mProgress;
	private MyHandler handler = new MyHandler();
	private final int GET_PERM_SUCCESS = 1;
	private String CFLOWID = "";
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
		dialog = Background.Process(this, GetCooperatePermList, getString(MSFWResource.getResourseIdByName(PermListByCooperate.this, "string", "tj_loding")));
	}

	private void InitView() {
		noData = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "noData"));
		layoutInflater = getLayoutInflater();
		permList = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "permList"));
		permList.setOnItemClickListener(new MyItemClick());
		permList.setOnItemLongClickListener(new MyItemLongClick());
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		permList.setEmptyView(empty);
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "home"));
		typeName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "typeName"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PermListByCooperate.this.finish();				
			}
		});
		CFLOWID = getIntent().getStringExtra("CFLOWID");
		SFYDSB = getIntent().getStringExtra("SFYDSB");
		String name = getIntent().getStringExtra("name");
		typeName.setText("您选择的是" + name + "，请继续选择办理事项");

	}

	private class MyItemLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
			new AlertDialog.Builder(PermListByCooperate.this).setMessage("是否将该事项加入收藏夹？").setTitle(getString(MSFWResource.getResourseIdByName(PermListByCooperate.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					FavoriteManage.add(permissions.get(position), PermListByCooperate.this);
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
			intent = new Intent();
			intent.setClass(PermListByCooperate.this, PermGuideContainer.class);
			intent.putExtra("PERMID", permissions.get(position).getID());
			intent.putExtra("permission", permissions.get(position));
			startActivity(intent);
		}

	}

	final Runnable GetCooperatePermList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("CFLOWID", CFLOWID);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				String response = HTTP.excute("getCooperatePermList", "RestCooperateService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					permissions = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<Permission>>() {}.getType());
					handler.sendEmptyMessage(GET_PERM_SUCCESS);

				} else {
					DialogUtil.showUIToast(PermListByCooperate.this, getString(MSFWResource.getResourseIdByName(PermListByCooperate.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(PermListByCooperate.this, getString(MSFWResource.getResourseIdByName(PermListByCooperate.this, "string", "tj_occurs_error_network")));
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
			
			Permission permission = permissions.get(position);
			item.name.setText(permission == null ? "" : permission.getSXZXNAME());
			item.deptName.setText(permission == null ? "" : permission.getDEPTNAME());
			if (null != permission && null != permission.getSFYDSB() && permission.getSFYDSB().equals("1")) {// 支持手机申报
				item.isApply.setVisibility(View.VISIBLE);
			} else {
				item.isApply.setVisibility(View.INVISIBLE);
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
