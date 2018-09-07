package com.tjsoft.webhall.ui.yhzx;



import java.text.DateFormat;
import java.util.Date;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.ResMgr;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.PermListByDB;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.UserDetail;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.bsdt.AdviceList;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.search.Search;
import com.tjsoft.webhall.ui.user.ChangeEnterpriseInfo;
import com.tjsoft.webhall.ui.user.ChangePassword;
import com.tjsoft.webhall.ui.user.ChangeUserInfo;
import com.tjsoft.webhall.ui.user.Login;

/**
 * 用户中心
 * 
 * @author fuchl
 * 
 */
public class YHZX extends AutoDialogActivity {
	private DisplayMetrics dm;
	private Button more, search, logout;
	private Intent intent;
	private GridView gridView;
	private int[] zmhdId = { MSFWResource.getResourseIdByName(YHZX.this, "drawable", "tj_yhzx_zbsx"),
			MSFWResource.getResourseIdByName(YHZX.this, "drawable", "tj_yhzx_xxtz"),
			MSFWResource.getResourseIdByName(YHZX.this, "drawable", "tj_yhzx_wdsc"),
			MSFWResource.getResourseIdByName(YHZX.this, "drawable", "tj_yhzx_wdyy"),
			MSFWResource.getResourseIdByName(YHZX.this, "drawable", "tj_yhzx_wdpj"),
			MSFWResource.getResourseIdByName(YHZX.this, "drawable", "tj_yhzx_xgmm"),
			MSFWResource.getResourseIdByName(YHZX.this, "drawable", "tj_yhzx_rjsz") };
	private LayoutInflater inflater;
	private TextView title;
	private TextView name, date;
	private ImageView img;
	private ImageView imgAuthor;
	private UserDetail userDetail;
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_yhzx"));
		Constants.getInstance().addActivity(this);
		mContext = this;
		dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		inflater = getLayoutInflater();
		gridView = (GridView) findViewById(MSFWResource.getResourseIdByName(this, "id", "gridView"));
		gridView.setAdapter(new GridViewAdapter(zmhdId));
		gridView.setOnItemClickListener(new GridViewItemClick());

		more = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "more"));
		search = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "search"));
		logout = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "logout"));

		title = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "title"));
		title.setText("用户中心");

		name = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "name"));
		date = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "date"));
		img = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "img"));
		imgAuthor = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "img_author"));
		initSetOnListener();
	}

	private void initSetOnListener() {
		more.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
			}
		});
		search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(YHZX.this, Search.class);
				startActivity(intent);
			}
		});
		logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Constants.user = null;
				DialogUtil.showUIToast(YHZX.this, "您已退出登陆！");
				FileUtil.Write(YHZX.this, "password", "");
				LoginBaoAnTongUtil.checkLogin(YHZX.this);
//				/**
//				 * 判断是否登录MST
//				 */
//				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//				if (gloabDelegete != null) {
//					TransportEntity transportEntity = gloabDelegete.get();
//					if (!transportEntity.isLoginStatus()) {
//						gloabDelegete.loginMST(YHZX.this);
//						return;
//					}
//					intent = new Intent();
//					intent.setClass(YHZX.this, Login.class);
//					startActivity(intent);
//					finish();
//				}
			}
		});
		img.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (userDetail != null) {
					if (userDetail.getTYPE().equals("1")) {
						intent = new Intent();
						intent.setClass(YHZX.this, ChangeUserInfo.class);
						intent.putExtra("userDetail", userDetail);
						startActivity(intent);
					} else if (userDetail.getTYPE().equals("2")) {
						intent = new Intent();
						intent.setClass(YHZX.this, ChangeEnterpriseInfo.class);
						intent.putExtra("userDetail", userDetail);
						startActivity(intent);
					}
				} else {
					dialog = Background.Process(YHZX.this, GetUserDetail, "正在加载");
				}
			}
		});
	}

	private void setValue() {
		name.setText(Constants.user.getREALNAME());
		date.setText(DateFormat.getDateInstance(DateFormat.FULL).format(new Date()));
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Background.Process(this, GetUserDetail, "正在加载");
	}

	class GridViewItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int posision, long arg3) {
			if (null == Constants.user) {
				isLoginAlert();
				return;
			}
			switch (posision) {

			case 0:// 在办事项
				intent = new Intent();
				intent.setClass(YHZX.this, WDBJ.class);
				startActivity(intent);
				break;
			case 1:// 消息通知
//				intent = new Intent(YHZX.this, MainFragment.class);
//				intent.putExtra("ID", 4);
//				startActivity(intent);
//				break;
			case 2:// 我的收藏
				intent = new Intent();
				intent.setClass(YHZX.this, PermListByDB.class);
				startActivity(intent);
				break;
			case 3:// 我的预约
				intent = new Intent();
				intent.setClass(YHZX.this, ReserveList.class);
				startActivity(intent);
				break;
			case 4:// 我的评价
				intent = new Intent();
				intent.setClass(YHZX.this, AdviceList.class);
				startActivity(intent);
				break;
			case 5:// 修改密码

				intent = new Intent();
				intent.setClass(YHZX.this, ChangePassword.class);
				startActivity(intent);
				break;
			case 6:// 软件设置
				intent = new Intent();
				intent.setClass(YHZX.this, Set.class);
				startActivity(intent);
				break;

			default:
				break;
			}

		}

	}

	class GridViewAdapter extends BaseAdapter {

		private int[] imgs;

		public GridViewAdapter(int[] ids) {
			super();
			this.imgs = ids;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgs.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return imgs[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MenuItem item;
			if (null == convertView) {
				item = new MenuItem();
				convertView = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_menu_item"), null);
				item.bg = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "bg"));
				convertView.setTag(item);
			} else {
				item = (MenuItem) convertView.getTag();
			}
			ResMgr.loadImage(getResources(), item.bg, imgs[position], 0);
			RelativeLayout.LayoutParams iconSize = new RelativeLayout.LayoutParams((dm.widthPixels - DensityUtil.dip2px(YHZX.this, 20)) / 4,
					(int) ((dm.widthPixels - DensityUtil.dip2px(YHZX.this, 20)) / 4));
			item.bg.setLayoutParams(iconSize);
			return convertView;

		}

		public final class MenuItem {
			ImageView bg;
		}

	}

	private void isLoginAlert() {
		new AlertDialog.Builder(this).setMessage("您还没有登录，是否现在登录？").setTitle(getString(MSFWResource.getResourseIdByName(this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("是的", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				LoginBaoAnTongUtil.checkLogin(YHZX.this);
//				/**
//				 * 判断是否登录MST
//				 */
//				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//				if (gloabDelegete != null) {
//					TransportEntity transportEntity = Constants.getInstance().getGloabDelegete().get();
//					if (!transportEntity.isLoginStatus()) {
//						gloabDelegete.loginMST(YHZX.this);
//						return;
//					}
//					intent = new Intent();
//					intent.setClass(YHZX.this, Login.class);
//					startActivity(intent);
//				}
				
			}
		}).setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
			}
		}).show();
	}

	final Runnable GetUserDetail = new Runnable() {
		@Override
		public void run() {
			try {

				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("ID", Constants.user.getUSER_ID());
				String response = HTTP.excute("getInfoByUserid", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					userDetail = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), UserDetail.class);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							setValue();
							if ("1".equals(userDetail.getISREALNAME())) {
								imgAuthor.setVisibility(View.VISIBLE);
							}
						}
					});

				} else {
					String error = json.getString("error");
					if (null != error) {
						DialogUtil.showUIToast(YHZX.this, error);
					}
				}
			} catch (Exception e) {
				DialogUtil.showUIToast(YHZX.this, getString(MSFWResource.getResourseIdByName(YHZX.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};
}
