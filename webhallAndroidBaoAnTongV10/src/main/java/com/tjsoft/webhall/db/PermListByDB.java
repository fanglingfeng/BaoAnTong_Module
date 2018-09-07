package com.tjsoft.webhall.db;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.ShowPopupMoreUtil;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.util.VoiceSearchUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.ui.search.PermListByName;
import com.tjsoft.webhall.ui.wsbs.PermAdapter;
import com.tjsoft.webhall.util.SharedPreferenceUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的搜藏页面
 * 
 * @author Administrator
 * 
 */
public class PermListByDB extends AutoDialogActivity {
	private ListView permList;
	private List<Permission> permissions=new ArrayList<Permission>();
	private TextView typeName, titleName;
	private Button home;
	private RelativeLayout  back;
	private Intent intent;
	private ProgressDialog dialog;
	private Context mContext;
	private Button btnSearch;
	private EditText textSearch;

	private ImageView voice;
	private VoiceSearchUtil mVoice;
	private PermAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		this.mContext = this;
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_permlist"));
		Constants.getInstance().addActivity(this);
		InitView();
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PermListByDB.this.finish();
			}
		});
		mVoice=new VoiceSearchUtil(this, textSearch,btnSearch);

		voice=(ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "voice_iv"));
		voice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mVoice.startListenVoice();
			}
		});
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		dialog = Background.Process(this, GetPermList, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_loding")));

	}
	@Override
	protected void onDestroy() {
		StatisticsTools.end("我的收藏", null, null);
		dialog.dismiss();
		super.onDestroy();
	}

	private void InitView() {
		btnSearch = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "btnSearch"));
		textSearch = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "textSearch"));
		btnSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (TextUtils.isEmpty(textSearch.getText().toString())) {
					DialogUtil.showUIToast(PermListByDB.this, getString(MSFWResource.getResourseIdByName(PermListByDB.this, "string", "tj_search_empty_notice")));
				} else {
					intent = new Intent();
					intent.setClass(PermListByDB.this, PermListByName.class);
					intent.putExtra("name", textSearch.getText().toString().trim());
					startActivity(intent);
				}
				
			}
		});

		permList = (ListView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "permList"));
		permList.setOnItemClickListener(new MyItemClick());
		permList.setOnItemLongClickListener(new MyItemLongClick());
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "empty"));
		permList.setEmptyView(empty);

		
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "back"));
		home = (Button) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "home"));
		home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ShowPopupMoreUtil.PermListshowPopupMore(v, mContext);
			}
		});
		typeName = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "typeName"));
		typeName.setText("您可以长按服务事项中的事项将其加入我的收藏！");
		titleName = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "titleName"));
		titleName.setText("我的收藏");
	}

	private class MyItemLongClick implements OnItemLongClickListener {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int position, long arg3) {
			new AlertDialog.Builder(PermListByDB.this).setMessage("是否从收藏夹中移除该事项？").setTitle(getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_notify"))).setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					FavoriteManage.delete(permissions.get(position).getID(), PermListByDB.this);
					PermListByDB.this.dialog = Background.Process(PermListByDB.this, GetPermList, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_loding")));
//					deleteFavorite(permissions.get(position));
				}
			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
				}
			}).show();
			return true;
		}

	}

	class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			/*
			 * intent = new Intent(); intent.setClass(PermListByDB.this,
			 * WorkSpace.class); intent.putExtra("permission",
			 * permissions.get(position)); startActivity(intent);
			 * overridePendingTransition(R.anim.tj_push_up_in,
			 * R.anim.tj_push_up_out);
			 */
			/*if(!TextUtils.isEmpty(permissions.get(position).getWSSBDZ())){
				intent = new Intent();
				intent.setClass(PermListByDB.this, WSBSWeb.class);
				intent.putExtra("url", permissions.get(position).getWSSBDZ());
				startActivity(intent);
			}else{
				intent = new Intent();
				intent.setClass(PermListByDB.this, PermGuideContainer.class);
				intent.putExtra("PERMID", permissions.get(position).getID());
				intent.putExtra("permission", permissions.get(position));
				intent.putExtra("flag", 0);
				intent.putExtra("WSBSFLAG", Constants.WSBS_PATH);
				startActivity(intent);
			}		*/
			adapter.setSelsetorItem(position);
			adapter.notifyDataSetInvalidated();
		}

	}

	final Runnable GetPermList = new Runnable() {
		@Override
		public void run() {
			try {
				permissions = FavoriteManage.selectAll(PermListByDB.this);
				if (null != permissions) {
					runOnUiThread(new Runnable() {
						public void run() {
							Log.e("sps===", "permission===="+permissions.size());
							adapter=new PermAdapter(mContext,permissions);
							permList.setAdapter(adapter);						}
					});
				}
//				JSONObject param=new JSONObject();
//				param.put("USERID", Constants.user.getUSER_ID());
//				String respons=HTTP.excuteAndCache("getPermFavoriteByUserid", "RestPermissionitemService", param.toString(), PermListByDB.this);
//				JSONObject json=new JSONObject(respons);
//				String code=json.getString("code");
//				String perm=json.optString("ReturnValue");
//				if(code.equals("200")){					
//					permissions=JSONUtil.getGson().fromJson(new JSONObject(perm).optString("Items"), new TypeToken<List<Permission>>() {
//					}.getType());
//				
//				}else {
//					DialogUtil.showUIToast(PermListByDB.this, getString(MSFWResource.getResourseIdByName(mContext, "string", json.optString("error").toString())));
//				}
			} catch (Exception e) {
				DialogUtil.showUIToast(PermListByDB.this, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	private void deleteFavorite(final Permission permission){
		
	
	final Runnable deleteMyFavorite = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", permission.getID());
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("PERMKEY", "");
				String response = HTTP.excute("cancelPermFavorite", "RestPermissionitemService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					dialog.dismiss();
					DialogUtil.showUIToast(PermListByDB.this, "取消收藏成功！");
					runOnUiThread(new Runnable() {
						public void run() {
							FavoriteManage.delete(permission.getID(), PermListByDB.this);
				
						}
					});
				} else {
					dialog.dismiss();
					DialogUtil.showUIToast(PermListByDB.this, "取消收藏失败，请稍后重试！");
				}
			} catch (Exception e) {
				dialog.dismiss();
				// TODO Auto-generated catch block
				e.printStackTrace();
				DialogUtil.showUIToast(PermListByDB.this, "取消收藏失败，请稍后重试！");
			}
		}
	};
	dialog = Background.Process(PermListByDB.this, deleteMyFavorite, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_loding")));

	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == 100) {

				String result = data.getStringExtra("result ");
				System.out.println("fuchl  得到result" + result);
				if (TextUtils.equals(result, "success")) {
					Object object = SharedPreferenceUtil.get(mContext,"file_key", "value_key");
					if (object != null) {
						Permission permission = (Permission) object;
						System.out.println("fuchl  取出permission" + permission.toString());

						adapter.nextActivity(1, permission);

					}

				} else {
					DialogUtil.showUIToast(mContext,
							"认证失败");
				}


			}
		}
	}
}
