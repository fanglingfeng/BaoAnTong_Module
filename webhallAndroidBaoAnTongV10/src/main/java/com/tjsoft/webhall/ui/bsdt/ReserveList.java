package com.tjsoft.webhall.ui.bsdt;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Reserve;
import com.tjsoft.webhall.lib.XListView;
import com.tjsoft.webhall.lib.XListView.IXListViewListener;

public class ReserveList extends AutoDialogActivity implements IXListViewListener {
	private XListView xListView;
	private List<Reserve> reserves = new ArrayList<Reserve>();
	private LayoutInflater layoutInflater;
	private int PAGENO = 1;
	private ReserveAdapter adapter;
	private RelativeLayout back;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		StatisticsTools.start();
		mContext = this;
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_reserve_list"));
		layoutInflater = getLayoutInflater();
		if (null == Constants.user) {
			DialogUtil.showUIToast(this, "您还没有登录");
			finish();
		} else {
			initView();
		}
	}

	@Override
	protected void onDestroy() {
		StatisticsTools.end("我的预约", null, null);
		super.onDestroy();
	}

	private void initView() {
		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ReserveList.this.finish();
			}
		});
		xListView = (XListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "xListView"));
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
		xListView.setEmptyView(empty);
		adapter = new ReserveAdapter();
		xListView.setAdapter(adapter);
		dialog = Background.Process(this, GetReserveList, getString(MSFWResource.getResourseIdByName(ReserveList.this, "string", "tj_loding")));

	}

	final Runnable GetReserveList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("APPLICANTID", Constants.user.getUSER_ID());
				param.put("PAGENO", PAGENO + "");
				param.put("PAGESIZE", "20");

				String response = HTTP.excute("list", "RestOnlineReserveService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final List<Reserve> temp = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Reserve>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {
							if (temp.size() < 20) {
								xListView.setPullLoadEnable(false);
							}
							reserves.addAll(temp);
							adapter.notifyDataSetChanged();
						}
					});

				} else {
					DialogUtil.showUIToast(ReserveList.this, getString(MSFWResource.getResourseIdByName(ReserveList.this, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(ReserveList.this, getString(MSFWResource.getResourseIdByName(ReserveList.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class ReserveAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return reserves.size();
		}

		@Override
		public Object getItem(int position) {
			return reserves.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			ReserveItem item;
			if (null == convertView) {
				item = new ReserveItem();
				convertView = layoutInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_reserve_list_item"), parent, false);
				item.BSNUM = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "BSNUM"));
				item.APPLICANT = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "APPLICANT"));
				item.RESERVETIME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "RESERVETIME"));
				item.CREATETIME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "CREATETIME"));
				item.PNAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "PNAME"));
				item.DEPTNAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "DEPTNAME"));
				item.reason = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "reason"));
				item.item_bg = (LinearLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "item_bg"));
				convertView.setTag(item);
			} else {
				item = (ReserveItem) convertView.getTag();
			}

			Reserve reserve = reserves.get(position);
			if (null != reserves && reserves.size() != 0) {
				item.BSNUM.setText(reserve.getBSNUM());
				item.APPLICANT.setText(reserve.getAPPLICANT());
				item.RESERVETIME.setText(reserve.getRESERVETIME());
				item.CREATETIME.setText(reserve.getRESERVEDATE());
				item.PNAME.setText(reserve.getPNAME());
				item.DEPTNAME.setText(reserve.getDEPTNAME());

				String status = reserve.getSTATUS();
				if (null != reserve.getSTATUS()) {
					if ("5".equals(status)) {
						item.item_bg.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_reserve_backlog"));
						item.reason.setText("预约时间段：");
					} else if ("6".equals(status)) {
						item.item_bg.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_reserve_succes"));
						item.reason.setText("预约时间段：");
					} else if ("7".equals(status)) {
						item.item_bg.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_reserve_cancel"));
						item.reason.setText("取消原因：");
					}
				}
			}
			return convertView;

		}

		public final class ReserveItem {
			public TextView BSNUM, APPLICANT, RESERVETIME, CREATETIME, PNAME, DEPTNAME, reason;
			public LinearLayout item_bg;
		}
	}

	@Override
	public void onRefresh() {
		reserves.clear();
		PAGENO = 1;
		dialog = Background.Process(this, GetReserveList, getString(MSFWResource.getResourseIdByName(ReserveList.this, "string", "tj_loding")));
		xListView.stopRefresh();
		xListView.setPullLoadEnable(true);
	}

	@Override
	public void onLoadMore() {
		PAGENO++;
		dialog = Background.Process(this, GetReserveList, getString(MSFWResource.getResourseIdByName(ReserveList.this, "string", "tj_loding")));
		xListView.stopLoadMore();

	}

}
