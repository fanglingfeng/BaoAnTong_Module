package com.tjsoft.webhall.ui.work;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.entity.PracticalGuideEntity;

/**
 * 实用指南
 * 
 * @author fuchl
 * 
 */
public class PracticalGuide extends Fragment{
	private String PERMID = "";
	private List<PracticalGuideEntity> mPracticalGuide;
	private ListView mListView;
	private PracticalGuideAdapter mPracticalGuideAdapter;
	private LayoutInflater mInflater;
	private Context mContext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPracticalGuide = new ArrayList<PracticalGuideEntity>();
		mInflater = LayoutInflater.from(getActivity());
		mContext = this.getActivity();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_practical_guide"), container, false);
		mListView = (ListView) view.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "xListView"));
		ImageView empty = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "empty"));
		mListView.setEmptyView(empty);
		mPracticalGuideAdapter = new PracticalGuideAdapter();
		mListView.setAdapter(mPracticalGuideAdapter);
		PERMID = this.getActivity().getIntent().getStringExtra("PERMID");
		Background.Process(PracticalGuide.this.getActivity(), getPracticalByPermid, getString(MSFWResource.getResourseIdByName(PracticalGuide.this.getActivity(), "string", "tj_loding")));
		return view;
	}

	/**
	 * 获取办事指南
	 */
	final Runnable getPracticalByPermid = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				String response = HTTP.excuteAndCache("getPracticalByPermid", "RestPermissionitemService", param.toString(),getActivity());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					final List<PracticalGuideEntity> items = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<PracticalGuideEntity>>() {
					}.getType());
					if (items != null && items.size() > 0) {
						PracticalGuide.this.getActivity().runOnUiThread(new Runnable() {
							public void run() {
								mPracticalGuide.addAll(items);
								mPracticalGuideAdapter.notifyDataSetChanged();
							}
						});
					}

				} else {
					DialogUtil.showUIToast(PracticalGuide.this.getActivity(), getString(MSFWResource.getResourseIdByName(PracticalGuide.this.getActivity(), "string", "tj_occurs_error_network")));
					PracticalGuide.this.getActivity().finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(PracticalGuide.this.getActivity(), getString(MSFWResource.getResourseIdByName(PracticalGuide.this.getActivity(), "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
	
	private class PracticalGuideAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mPracticalGuide.size();
		}

		@Override
		public Object getItem(int position) {
			return mPracticalGuide.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = mInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_practical_guide_item"), parent, false);
				viewHolder.title = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "title_id"));
				viewHolder.practical = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "practical"));
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.title.setText(new StringBuilder().append("实用指南").append(mPracticalGuide.get(position).getORDERID()).append(":").toString());
			viewHolder.practical.setText(mPracticalGuide.get(position).getPRACTICAL());

			return convertView;
		}

		class ViewHolder {
			private TextView title;
			private TextView practical;
		}

	}

}
