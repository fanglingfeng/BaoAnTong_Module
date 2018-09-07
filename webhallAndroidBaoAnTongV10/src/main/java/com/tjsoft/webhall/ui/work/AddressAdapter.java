package com.tjsoft.webhall.ui.work;

import java.util.List;

import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.entity.NetWorkBean;
import com.tjsoft.webhall.ui.wsbs.NetworkDetail;
import com.tjsoft.webhall.ui.wsbs.NetworkListActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AddressAdapter extends BaseAdapter{
	private Context mContext;
	private List<NetWorkBean> listItems;
	public AddressAdapter(Context context,List<NetWorkBean> listItems) {
		// TODO Auto-generated constructor stub
		this.mContext=context;
		this.listItems=listItems;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		AddressViewHodler viewHodler=null;
		if(convertView==null){
			viewHodler=new AddressViewHodler();
			convertView=LayoutInflater.from(mContext).inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_item_address"), parent, false);
			viewHodler.address_title=(TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "address_title"));
			viewHodler.address_detail=(TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "address_detail"));
			viewHodler.address_phone=(TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "address_phone"));
			convertView.setTag(viewHodler);
		}else {
			viewHodler=(AddressViewHodler) convertView.getTag();
		}
		viewHodler.address_title.setText("网点名称:"+listItems.get(position).getNETWORKNAME());
		viewHodler.address_detail.setText("网点地址:"+listItems.get(position).getNETWORKADDRESS());
		viewHodler.address_phone.setText("网点电话:"+listItems.get(position).getNETWORKPHONE());
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.putExtra("netWorkBean", listItems.get(position));
				intent.setClass(mContext, NetworkDetail.class);
				mContext.startActivity(intent);
			}
		});
		return convertView;
	}
	
	class AddressViewHodler{
		public TextView address_title;
		public TextView address_detail;
		public TextView address_phone;
	}
}
