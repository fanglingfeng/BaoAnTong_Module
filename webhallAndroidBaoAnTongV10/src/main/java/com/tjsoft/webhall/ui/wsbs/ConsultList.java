package com.tjsoft.webhall.ui.wsbs;

import java.util.List;

import org.json.JSONObject;

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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.commonsware.cwac.endless.EndlessAdapter;
import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Consult;
import com.tjsoft.webhall.lib.XListView;
import com.tjsoft.webhall.lib.XListView.IXListViewListener;
/**
 * 咨询列表页面
 * @author Administrator
 *
 */
public class ConsultList extends AutoDialogActivity implements IXListViewListener {
	private XListView consultList;
	private List<Consult> conversations, conversationPlus;
	private MyHandler handler = new MyHandler();
	private final int GET_CONVERSATION_SUCCESS = 1;
	private final int SEARCH_CONVERSATION_SUCCESS = 2;
	private LayoutInflater layoutInflater;
	private Button  addConsult, search;
	private RelativeLayout back;
	private int PAGENO = 1;
	private EditText MAINTITLE;
	private MyAdapter myAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "consult_list_v2"));
		Constants.getInstance().addActivity(this);
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		addConsult = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "addConsult"));
		search = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "search"));
		MAINTITLE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "MAINTITLE"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		addConsult.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(ConsultList.this, AddConsult.class);
				startActivity(intent);
				
			}
		});
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = Background.Process(ConsultList.this, SearchConversations, getString(MSFWResource.getResourseIdByName(ConsultList.this, "string", "tj_loding")));
				
			}
		});
		consultList = (XListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "consultList"));
		consultList.setPullLoadEnable(false);
		consultList.setXListViewListener(this);
		ImageView empty = (ImageView)findViewById(MSFWResource.getResourseIdByName(this, "id", "empty")); 
		consultList.setEmptyView(empty);
		layoutInflater = getLayoutInflater();
//		dialog = Background.Process(this, GetConversations, getString(MSFWResource.getResourseIdByName(this, "string", "tj_loding")));
	}

	@Override
	protected void onResume() {
		PAGENO = 1;
		dialog = Background.Process(this, GetConversations, getString(MSFWResource.getResourseIdByName(this, "string", "tj_loding")));
		super.onResume();
	}

	final Runnable GetConversations = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
				param.put("PAGENO", PAGENO + "");
				param.put("PAGESIZE", "1000");

				String response = HTTP.excute("list", "RestAdvisoryService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					conversations = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Consult>>() {
					}.getType());
					handler.sendEmptyMessage(GET_CONVERSATION_SUCCESS);

				} else {
					DialogUtil.showUIToast(ConsultList.this, getString(MSFWResource.getResourseIdByName(ConsultList.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ConsultList.this, getString(MSFWResource.getResourseIdByName(ConsultList.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	final Runnable SearchConversations = new Runnable() {
		@Override
		public void run() {
			try {
				String title = MAINTITLE.getText().toString().trim();
				if (null == title || title.trim().equals("")) {
					DialogUtil.showUIToast(ConsultList.this, "搜索内容不能为空");
					return;
				}
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
				param.put("MAINTITLE", title);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");

				String response = HTTP.excute("search", "RestAdvisoryService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					conversations = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Consult>>() {}.getType());
					if (null != conversations && conversations.size() != 0) {
						handler.sendEmptyMessage(SEARCH_CONVERSATION_SUCCESS);
					} else {
						handler.sendEmptyMessage(SEARCH_CONVERSATION_SUCCESS);
						DialogUtil.showUIToast(ConsultList.this, "没有查找到相关数据");
					}
				} else {
					DialogUtil.showUIToast(ConsultList.this, json.getString("error"));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ConsultList.this, getString(MSFWResource.getResourseIdByName(ConsultList.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class MyAdapter extends EndlessAdapter {

		public MyAdapter() {
			super(new ConversationAdapter());

		}

		// 底部控件
		@Override
		protected View getPendingView(ViewGroup parent) {
			View row = LayoutInflater.from(parent.getContext()).inflate(MSFWResource.getResourseIdByName(ConsultList.this, "layout", "list_end"), null);
			return row;
		}

		// 添加数据
		@Override
		protected void appendCachedData() {
			conversations.addAll(conversationPlus);
		}

		// 后台获取数据
		@Override
		protected boolean cacheInBackground() throws Exception {
			try {
				PAGENO++;
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
				param.put("PAGENO", PAGENO + "");
				param.put("PAGESIZE", "20");
				String response = HTTP.excute("list", "RestAdvisoryService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					conversationPlus = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Consult>>() {
					}.getType());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return conversationPlus.size() != 0; // true 继续 false 停止获取
		}

	}

	class ConversationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return conversations.size();
		}

		@Override
		public Object getItem(int position) {
			return conversations.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			if (null == convertView) {
				view = layoutInflater.inflate(MSFWResource.getResourseIdByName(ConsultList.this, "layout", "consult_list_item"), null);
			} else {
				view = convertView;
			}
			ConversationItem item = new ConversationItem();
			item.MAINTITLE = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ConsultList.this, "id", "MAINTITLE"));
			item.CREATETIME = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ConsultList.this, "id", "CREATETIME"));
			item.SFHF = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(ConsultList.this, "id", "SFHF"));

			item.MAINTITLE.setText(conversations.get(position).getMAINTITLE());
			item.CREATETIME.setText(conversations.get(position).getCREATETIME());
			if (null != conversations.get(position) && conversations.get(position).getSFHF().equals("0")) {
				item.SFHF.setVisibility(View.INVISIBLE);
			} else {
				item.SFHF.setVisibility(View.VISIBLE);
			}
			return view;
		}

		public final class ConversationItem {
			public TextView MAINTITLE;
			public TextView CREATETIME;
			public ImageView SFHF;

		}
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_CONVERSATION_SUCCESS:
				myAdapter = new MyAdapter();
				consultList.setAdapter(myAdapter);
				consultList.setOnItemClickListener(new MyItemClick());
				break;
			case SEARCH_CONVERSATION_SUCCESS:
				myAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	}

	class MyItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			Intent intent = new Intent();
			intent.setClass(ConsultList.this, ConsultContent.class);
			intent.putExtra("conversation", conversations.get(position-1));
			startActivity(intent);
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		PAGENO = 1;
		dialog = Background.Process(this, GetConversations, getString(MSFWResource.getResourseIdByName(this, "string", "tj_loding")));
		consultList.stopRefresh();
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}



}
