package com.tjsoft.webhall.ui.bsdt;

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
import com.tjsoft.webhall.entity.Advice;
import com.tjsoft.webhall.lib.XListView;
import com.tjsoft.webhall.lib.XListView.IXListViewListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 评议列表页面
 * @author Administrator
 *
 */
public class AdviceList extends AutoDialogActivity implements IXListViewListener {
	private XListView xListView;
	private int PAGENO = 1;
	private List<Advice> advices = new ArrayList<Advice>();
	private LayoutInflater layoutInflater;
	private AdviceAdapter adapter;
	private Button back;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_advice_list"));
		layoutInflater = getLayoutInflater();
		InitView();
	}

	private void InitView() {
		back = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AdviceList.this.finish();
			}
		});
		adapter = new AdviceAdapter();
		
		xListView = (XListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "xListView"));
		xListView.setPullLoadEnable(true);
		xListView.setXListViewListener(this);
		xListView.setAdapter(adapter);
		ImageView empty = (ImageView)findViewById(MSFWResource.getResourseIdByName(this, "id", "empty")); 
		xListView.setEmptyView(empty);
		dialog = Background.Process(this, GetAdviceList, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_loding")));

	}

	@Override
	protected void onStart() {
		adapter.notifyDataSetChanged();
		super.onStart();
	}

	final Runnable GetAdviceList = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("USERID", Constants.user.getUSER_ID());
				param.put("PAGENO", PAGENO + "");
				param.put("PAGESIZE", "5");

				String response = HTTP.excute("list", "RestAdviceService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final List<Advice> temp = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<Advice>>() {
					}.getType());
					runOnUiThread(new Runnable() {
						public void run() {
							if (temp.size() < 5) {
								xListView.setPullLoadEnable(false);
							}
							advices.addAll(temp);
							adapter.notifyDataSetChanged();
						}
					});

				} else {
					DialogUtil.showUIToast(AdviceList.this, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(AdviceList.this, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
				finish();
				e.printStackTrace();

			}
		}
	};

	class AdviceAdapter extends BaseAdapter {
		private Intent intent;

		@Override
		public int getCount() {
			return advices.size();
		}

		@Override
		public Object getItem(int position) {
			return advices.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			AdviceItem item;
			if (null == convertView) {
				item = new AdviceItem();
				convertView = layoutInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_advice_list_item"), parent, false);
				item.BSNUM = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "BSNUM"));
				item.APPLICANT = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "APPLICANT"));
				item.COMPANY = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "COMPANY"));
				item.PNAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "PNAME"));
				item.DEPTNAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "DEPTNAME"));
				item.RESULT = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "RESULT"));
				item.pingyi = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "pingyi"));
				convertView.setTag(item);
			} else {
				item = (AdviceItem) convertView.getTag();
			}
			final Advice advice = advices.get(position);
			item.BSNUM.setText(advice.getBSNUM());
			item.APPLICANT.setText(advice.getAPPLICANT());
			item.COMPANY.setText(advice.getCOMPANY());
			item.PNAME.setText(advice.getPNAME());
			item.DEPTNAME.setText(advice.getDEPTNAME());
			item.pingyi.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					intent = new Intent();
					intent.setClass(AdviceList.this, AdviceSubmit.class);
					intent.putExtra("advice", advice);
					startActivity(intent);
				}
			});

			if (null == advice.getRESULT()) {
				item.RESULT.setText("尚未评议");
				item.RESULT.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(mContext, "color", "tj_gray")));
				item.pingyi.setVisibility(View.VISIBLE);
			} else {
				String result = advice.getRESULT().toString().trim();
				if ("0".equals(result)) {
					item.RESULT.setText("非常满意");
				} else if ("1".equals(result)) {
					item.RESULT.setText("满意");
				} else if ("2".equals(result)) {
					item.RESULT.setText("基本满意");
				} else if ("3".equals(result)) {
					item.RESULT.setText("一般");
				} else if ("4".equals(result)) {
					item.RESULT.setText("很差");
				}
				item.RESULT.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(mContext, "color", "tj_orange")));
				item.pingyi.setVisibility(View.INVISIBLE);
			}

			return convertView;

		}

		public final class AdviceItem {
			public TextView BSNUM;
			public TextView APPLICANT;
			public TextView COMPANY;
			public TextView PNAME;
			public TextView DEPTNAME;
			public TextView RESULT;
			public Button pingyi;

		}
	}

	@Override
	public void onRefresh() {
		advices.clear();
		PAGENO = 1;
		dialog = Background.Process(this, GetAdviceList, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_loding")));
		xListView.stopRefresh();
		xListView.setPullLoadEnable(true);

	}

	@Override
	public void onLoadMore() {
		PAGENO++;
		dialog = Background.Process(this, GetAdviceList, getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_loding")));
		xListView.stopLoadMore();
	}

}
