package com.tjsoft.webhall.ui.expressage;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.ObjectSaveUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.PostInfo;

/**
 * 速递地址
 * 
 * @author Administrator
 * 
 */
public class ExpressageList extends AutoDialogActivity {
	private ListView listView;
	private static List<PostInfo> postInfos = new ArrayList<PostInfo>();
	private LayoutInflater layoutInflater;
	private PostInfoAdapter adapter = new PostInfoAdapter();
	private Button  add;
	private RelativeLayout back;
	private static int position;
	private int flag = 0;//0为默认值   1，是寄件人信息
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "ems_add_list"));
		layoutInflater = getLayoutInflater();
		flag = getIntent().getIntExtra("flag",0);
		InitView();
	}

	private void InitView() {
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		add = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "add"));

		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.putExtra("flag", 0);
				intent.putExtra("type", flag);
				intent.setClass(ExpressageList.this, ExpressageModifyAddress.class);
				startActivity(intent);
				
			}
		});
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();				
			}
		});

		listView = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "listView"));

		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		listView.setEmptyView(empty);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("postInfo", postInfos.get(position));
				setResult(101, intent);
				finish();
			}
		});

	}

	@Override
	protected void onResume() {

		dialog = Background.Process(this, GetUserPostInfo, "正在加载");
		super.onResume();
	}

	final Runnable GetUserPostInfo = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("USERID", Constants.user.getUSER_ID());

				String response = HTTP.excute("getUserPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final List<PostInfo> postInfos = JSONUtil.getGson().fromJson(new JSONObject(json.getString("ReturnValue")).getString("Items"), new TypeToken<List<PostInfo>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {
							ExpressageList.postInfos = postInfos;
							adapter.notifyDataSetChanged();
						}
					});

				} else {
					DialogUtil.showUIToast(ExpressageList.this, "网络环境不稳定！");
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageList.this, "网络环境不稳定");
				finish();
				e.printStackTrace();

			}
		}
	};

	class PostInfoAdapter extends BaseAdapter {
		private int SelectorPosition=-1;
		@Override
		public int getCount() {
			return postInfos.size();
		}

		@Override
		public Object getItem(int position) {
			return postInfos.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			View view;
			if (null == convertView) {
				view = layoutInflater.inflate(MSFWResource.getResourseIdByName(ExpressageList.this, "layout", "ems_list_item"), null);
			} else {
				view = convertView;
			}
			ExpressageItem item = new ExpressageItem();
			item.RECEIVE = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "RECEIVE"));
			item.PHONE = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "PHONE"));
			item.ADDRESS = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "ADDRESS"));
			item.delete = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "ems_delete_info"));
			item.item = (RelativeLayout) view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "item"));
			item.modify_info=(LinearLayout)view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "ems_modify_info"));
			item.defaut_address=(LinearLayout)view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "set_default_address"));
			item.default_img = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "set_default_img"));
			item.default_tv = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ExpressageList.this, "id", "set_default_tv"));

			if(flag == 0) {
				item.RECEIVE.setText("收货人："+postInfos.get(position).getRECEIVE());
				item.ADDRESS.setText("收货地址："+
						checkText(postInfos.get(position).getPROVINCE())+
						checkText(postInfos.get(position).getCITY())+
						checkText(postInfos.get(position).getCOUNTRY())+
						checkText(postInfos.get(position).getADDRESS()));
			}else {
				item.RECEIVE.setText("寄件人："+postInfos.get(position).getRECEIVE());
				item.ADDRESS.setText("寄件地址："+
						checkText(postInfos.get(position).getPROVINCE())+
						checkText(postInfos.get(position).getCITY())+
						checkText(postInfos.get(position).getCOUNTRY())+
						checkText(postInfos.get(position).getADDRESS()));
			}
			item.PHONE.setText(postInfos.get(position).getPHONE());
			item.delete.setFocusable(false);
			item.delete.setFocusableInTouchMode(false);
			
//			item.defaut_address.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					ObjectSaveUtil.saveObject(ExpressageList.this, postInfos.get(position));
//					SelectorPosition=position;
//					notifyDataSetInvalidated();
//					
//				}
//			});
			item.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ExpressageList.position = position;
					dialog = Background.Process(ExpressageList.this, DeleteUserPostInfo, "正在加载");
				}
			});
			item.modify_info.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent();
					intent.putExtra("POSTINFO", postInfos.get(position));
					intent.putExtra("flag", 1);
					intent.putExtra("type", flag);
					intent.setClass(ExpressageList.this, ExpressageModifyAddress.class);
					startActivity(intent);
				}
			});
			// item.item.seton
			return view;

		}

		public final class ExpressageItem {
			private RelativeLayout item;
			public TextView RECEIVE;
			public TextView PHONE;
			public TextView ADDRESS;
			public LinearLayout delete;
			public LinearLayout modify_info;
			public LinearLayout defaut_address;
			public ImageView default_img;
			public TextView default_tv;
		}
	}

	/**
	 * 删除速递信息
	 */
	final Runnable DeleteUserPostInfo = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("POSTID", postInfos.get(position).getPOSTID());
				String response = HTTP.excute("deleteUserPostInfo", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					DialogUtil.showUIToast(ExpressageList.this, "删除成功！");
					postInfos.remove(position);
					runOnUiThread(new Runnable() {
						public void run() {
							adapter.notifyDataSetChanged();
						}
					});

				} else {
					DialogUtil.showUIToast(ExpressageList.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ExpressageList.this, "网络环境不稳定");
				e.printStackTrace();

			}
		}
	};

	private String checkText(String text) {
		return TextUtils.isEmpty(text) ? "" : text;
	}

}
