package com.tjsoft.webhall.ui.work;


import java.util.ArrayList;
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
import com.tjsoft.webhall.entity.Examine;
import com.tjsoft.webhall.lib.XListView;
import com.tjsoft.webhall.lib.XListView.IXListViewListener;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 审查交互
 * @author Administrator
 *
 */
public class ExamineMSG extends AutoDialogActivity implements IXListViewListener{
	private XListView xListView;
	private String BSNUM ="";
	private List<Examine> examines = new ArrayList<Examine>(); 
	private LayoutInflater inflater;
	private Button back;
	private TextView title;
	private Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_examine_list"));
		inflater = getLayoutInflater();
		mContext = this;
		xListView = (XListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "xListView"));
		ImageView empty = (ImageView)findViewById(MSFWResource.getResourseIdByName(this, "id", "empty")); 
		xListView.setEmptyView(empty);
		xListView.setPullLoadEnable(false);
		xListView.setXListViewListener(this);
		xListView.setOnItemClickListener(new MyItemListener());
		BSNUM = getIntent().getStringExtra("BSNUM");
		if(null == BSNUM){
			title =(TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
			title.setText("消息通知");
		}
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ExamineMSG.this.finish();
			}
		});
		dialog = Background.Process(ExamineMSG.this, GetMSG, "数据加载中...");
	}
	final Runnable GetMSG = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("BSNUM", BSNUM);
				param.put("PAGENO", "1");
				param.put("PAGESIZE", "1000");
				String response = HTTP.excute("list", "RestMessageService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (("200".equals(code))) {
					examines = JSONUtil.getGson().fromJson(json.getString("ReturnValue"),  new TypeToken<List<Examine>>() {}.getType());
					runOnUiThread(new  Runnable() {
						public void run() {
							if(null != examines){
								xListView.setAdapter(new ExamineAdapter(examines));
							}
						}
					});
					
				} else {
					DialogUtil.showUIToast(ExamineMSG.this, getString(MSFWResource.getResourseIdByName(ExamineMSG.this, "string", "tj_occurs_error_network")));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ExamineMSG.this, getString(MSFWResource.getResourseIdByName(ExamineMSG.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();
			}
		}
	};
	class ExamineAdapter extends BaseAdapter {
		private List<Examine> examines;
		



		public ExamineAdapter(List<Examine> examines) {
			super();
			this.examines = examines;
		}

		@Override
		public int getCount() {
			return examines.size();
		}

		@Override
		public Object getItem(int position) {
			return examines.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			Item item;
			if (null == convertView) {
				item = new Item();
				convertView = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_examine_list_item"), parent, false);
				item.MSG_SEND_USER_NAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "MSG_SEND_USER_NAME"));
				item.SEND_TIME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "SEND_TIME"));
				item.MSG_CONTENT = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "MSG_CONTENT"));
				convertView.setTag(item);
			} else {
				item = (Item) convertView.getTag();
			}
			
			Examine examine = examines.get(position);
			item.MSG_SEND_USER_NAME.setText(examine.getMSG_SEND_USER_NAME());
			item.SEND_TIME.setText(examine.getSEND_TIME());
			item.MSG_CONTENT.setText(examine.getMSG_CONTENT());

			return convertView;
		}

		public final class Item {
			public TextView MSG_SEND_USER_NAME;
			public TextView SEND_TIME;
			public TextView MSG_CONTENT;
		}
	}
	class MyItemListener implements OnItemClickListener{
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

			
		}
		
	}
	@Override
	public void onRefresh() {
		dialog = Background.Process(ExamineMSG.this, GetMSG, "数据加载中...");
		xListView.stopRefresh();
		xListView.setPullLoadEnable(true);
		
	}
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		
	}

}
