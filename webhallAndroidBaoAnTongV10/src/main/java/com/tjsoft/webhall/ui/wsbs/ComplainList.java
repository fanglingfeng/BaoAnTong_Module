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
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Complaint;

/**
 * 投诉列表页面
 * 
 * @author Administrator
 * 
 */
public class ComplainList extends AutoDialogActivity  {
	private ListView consultList;
	private List<Complaint> complaints;
	private MyHandler handler = new MyHandler();
	private final int GET_CONVERSATION_SUCCESS = 1;
	private final int SEARCH_CONVERSATION_SUCCESS = 2;
	private LayoutInflater layoutInflater;
	private Button back, addConsult, search;
	private TextView title;
	private EditText MAINTITLE;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "consult_list"));
		Constants.getInstance().addActivity(this);
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		addConsult = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "addConsult"));
		search = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "search"));
		MAINTITLE = (EditText) findViewById(MSFWResource.getResourseIdByName(this, "id", "MAINTITLE"));
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog = Background.Process(ComplainList.this, SearchComplaints, "正在加载。。。");
				
			}
		});
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
				intent.setClass(ComplainList.this, AddComplain.class);
				startActivity(intent);
				
			}
		});
		addConsult.setBackgroundResource(MSFWResource.getResourseIdByName(this, "drawable", "tj_complaint_btn_style"));
		consultList = (ListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "consultList"));
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		consultList.setEmptyView(empty);
		title = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
		title.setText("我的投诉");
		layoutInflater = getLayoutInflater();
		dialog = Background.Process(this, GetConversations, getString(MSFWResource.getResourseIdByName(ComplainList.this, "string", "tj_loding")));
	}

	@Override
	protected void onStart() {
		dialog = Background.Process(this, GetConversations, getString(MSFWResource.getResourseIdByName(ComplainList.this, "string", "tj_loding")));
		super.onStart();
	}

	final Runnable GetConversations = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");

				String response = HTTP.excute("list", "RestComplainService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					complaints = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Complaint>>() {
					}.getType());
					handler.sendEmptyMessage(GET_CONVERSATION_SUCCESS);

				} else {
					DialogUtil.showUIToast(ComplainList.this, getString(MSFWResource.getResourseIdByName(ComplainList.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ComplainList.this, getString(MSFWResource.getResourseIdByName(ComplainList.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	final Runnable SearchComplaints = new Runnable() {
		@Override
		public void run() {
			try {
				String title = MAINTITLE.getText().toString().trim();
				if (null == title || title.trim().equals("")) {
					DialogUtil.showUIToast(ComplainList.this, "搜索内容不能为空");
					return;
				}
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("DB_CREATE_ID", Constants.user.getUSER_ID());
				param.put("MAINTITLE", title);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");

				String response = HTTP.excute("search", "RestComplainService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					complaints = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Complaint>>() {
					}.getType());
					if (null != complaints && complaints.size() != 0) {
						handler.sendEmptyMessage(SEARCH_CONVERSATION_SUCCESS);
					} else {
						handler.sendEmptyMessage(SEARCH_CONVERSATION_SUCCESS);
						DialogUtil.showUIToast(ComplainList.this, "没有查找到相关数据");
					}

				} else {
					DialogUtil.showUIToast(ComplainList.this, getString(MSFWResource.getResourseIdByName(ComplainList.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ComplainList.this, getString(MSFWResource.getResourseIdByName(ComplainList.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class ConversationAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return complaints.size();
		}

		@Override
		public Object getItem(int position) {
			return complaints.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			if (null == convertView) {
				view = layoutInflater.inflate(MSFWResource.getResourseIdByName(ComplainList.this, "layout", "consult_list_item"), null);
			} else {
				view = convertView;
			}
			ConversationItem item = new ConversationItem();
			item.MAINTITLE = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ComplainList.this, "id", "MAINTITLE"));
			item.CREATETIME = (TextView) view.findViewById(MSFWResource.getResourseIdByName(ComplainList.this, "id", "CREATETIME"));
			item.SFHF = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(ComplainList.this, "id", "SFHF"));

			item.MAINTITLE.setText(complaints.get(position).getMAINTITLE());
			item.CREATETIME.setText(complaints.get(position).getCREATETIME());
			if (null != complaints.get(position) && complaints.get(position).getSFHF().equals("0")) {
				item.SFHF.setVisibility(View.INVISIBLE);
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
				consultList.setAdapter(new ConversationAdapter());
				consultList.setOnItemClickListener(new MyItemClick());
				break;
			case SEARCH_CONVERSATION_SUCCESS:
				consultList.setAdapter(new ConversationAdapter());
				consultList.setOnItemClickListener(new MyItemClick());
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
			intent.setClass(ComplainList.this, ComplainContent.class);
			intent.putExtra("complaint", complaints.get(position));
			startActivity(intent);

		}

	}


}
