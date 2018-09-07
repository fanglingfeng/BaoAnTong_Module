package com.tjsoft.webhall.ui.wsbs;

import java.util.List;

import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.FileUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.MyBrowser;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Business;
import com.tjsoft.webhall.entity.PermGroup;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.ReserveByBS;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.entity.WSFWSD;
import com.tjsoft.webhall.ui.bsdt.AdviceSubmit;
import com.tjsoft.webhall.ui.bsdt.ReserveSubmit;
import com.tjsoft.webhall.ui.expressage.ExpressageChoose;
import com.tjsoft.webhall.ui.expressage.ExpressageProgress;
import com.tjsoft.webhall.ui.work.ApplyResult;
import com.tjsoft.webhall.ui.work.ExamineMSG;
import com.tjsoft.webhall.ui.work.Find;
import com.tjsoft.webhall.ui.work.HistoreShare;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;
import com.tjsoft.webhall.ui.work.PermGuideContainer;
import com.tjsoft.webhall.ui.work.PreacceptNotice;
import com.tjsoft.webhall.ui.work.ReserveDetail;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
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

/**
 * 事项办理页面（16键盘）
 * 
 * @author Administrator
 * 
 */
public class WorkSpace extends AutoDialogActivity {

	private int[] menuId = null;
	private int[] menuGoneId = null;
	private GridView gridView;
	private LayoutInflater layoutInflater;
	private Button back, ems;
	private Intent intent;
	private Permission permission;
	private Business business;
	private TextView name, deptName, timeLimit, type, bsnum;
	private int STATUS = -1;// 新申办
	private String BSNUM = "";// 业务流水号
	private String P_GROUP_ID ;// 分组id
	private String P_GROUP_NAME;// 分组名称
	private List<PermGroup> groups;
	private final static int REGISTER_REQUEST = 102;
	private String userName;
	private String password;
	public static boolean isCooperate = false;// 是否有协同
	private Context mContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_workspace"));
		mContext = this;
		STATUS = getIntent().getIntExtra("STATUS", -1);
		BSNUM = getIntent().getStringExtra("BSNUM");
		permission = (Permission) getIntent().getSerializableExtra("permission");
		business = (Business) getIntent().getSerializableExtra("business");
		if (null != business && !TextUtils.isEmpty(business.getCBUSINESSID())) {
			isCooperate = true;
		} else {
			isCooperate = false;
		}
		layoutInflater = getLayoutInflater();
		initMenu();
		back = (Button) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "back"));
		name = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "name"));
		deptName = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "deptName"));
		timeLimit = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "timeLimit"));
		type = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "type"));
		bsnum = (TextView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "bsnum"));
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				WorkSpace.this.finish();
			}
		});
		ems = (Button) findViewById(MSFWResource.getResourseIdByName(this, "id", "ems"));
		ems.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (STATUS == -1||STATUS == 9) {//新申报
					Intent intent = new Intent();
					intent.setClass(WorkSpace.this, ExpressageChoose.class);
					startActivity(intent);
				} else {//已申报
					Intent intent = new Intent();
					intent.setClass(WorkSpace.this, MyBrowser.class);
					intent.putExtra("url", Constants.IP+"/EmsList.html?bsnum="+BSNUM);
					intent.putExtra("title", "速递详情");
					startActivity(intent);
				}
				
			}
		});
		ems.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		new Thread(GetWsfwsdByPermid).start();
		gridView = (GridView) findViewById(MSFWResource.getResourseIdByName(mContext, "id", "gridView"));
		gridView.setAdapter(new GridViewAdapter());
		gridView.setOnItemClickListener(new ItemClick());

		if (null != permission && STATUS == -1) {

			dialog = Background.Process(this, GetGroup, getString(MSFWResource.getResourseIdByName(WorkSpace.this, "string", "tj_loding")));
			name.setText("办理事项：" + permission.getSXZXNAME());
			deptName.setText("办理部门：" + permission.getDEPTNAME());
			timeLimit.setText("办理时限：" + permission.getITEMLIMITTIME() + "工作日");
			String typeStr = "";
			if (null != permission.getXZXK() && permission.getXZXK().equals("1")) {
				typeStr = "行政许可";
			}
			if (null != permission.getXZXK() && permission.getXZXK().equals("2")) {
				typeStr = "非行政许可";
			}
			if (null != permission.getXZXK() && permission.getXZXK().equals("3")) {
				typeStr = "公共服务";
			}
			if (null != permission.getXZXK() && permission.getXZXK().equals("4")) {
				typeStr = "其他事项";
			}
			type.setText("事项类型：" + typeStr);

		} else {
			if (null != business) {
				bsnum.setText("业务流水号：" + business.getBUSINESSID());
				name.setText("办理事项：" + business.getSMALLITEMNAME());
				deptName.setText("办理部门：" + business.getDEPTNAME());
				timeLimit.setText("申请人：" + (null == business.getAPPNAME() ? "无" : business.getAPPNAME()));
				type.setText("申请单位：" + (null == business.getAPPCOMPANY() ? "无" : business.getAPPCOMPANY()));
			}
		}
		
		goHistoreShare();
		finish();
		
	}
	
	/**
	 * 获取网上服务深度
	 */
	final Runnable GetWsfwsdByPermid = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", permission.getID());
				String response = HTTP.excute("getWsfwsdByPermid", "RestEMSService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {

					WSFWSD wsfwsd = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), WSFWSD.class);
					if (null != wsfwsd.getDJZZCL() && wsfwsd.getDJZZCL().equals("2"))// 速递服务
					{
						runOnUiThread(new Runnable() {
							public void run() {
								ems.setVisibility(View.VISIBLE);
							}
						});

					}
					if (null != wsfwsd.getLQSPJG() && wsfwsd.getLQSPJG().equals("2")) {// 速递服务

						runOnUiThread(new Runnable() {
							public void run() {
								ems.setVisibility(View.VISIBLE);
							}
						});
					}

				} else {
					DialogUtil.showUIToast(WorkSpace.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(WorkSpace.this, getString(MSFWResource.getResourseIdByName(WorkSpace.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	private void initMenu() {
		menuId = new int[8];
		menuId[0] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work1");
		menuId[1] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work2");
		menuId[2] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work3");
		menuId[3] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work4");
		menuId[4] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work5");
		menuId[5] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work6");
		menuId[6] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work7");
		menuId[7] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work8");

		menuGoneId = new int[8];
		menuGoneId[0] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work1_gone");
		menuGoneId[1] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work2_gone");
		menuGoneId[2] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work3_gone");
		menuGoneId[3] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work4_gone");
		menuGoneId[4] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work5_gone");
		menuGoneId[5] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work6_gone");
		menuGoneId[6] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work7_gone");
		menuGoneId[7] = MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_work8_gone");
	}

	@Override
	protected void onDestroy() {
		System.out.println("fuchl       onDestroy ");
//		Constants.material.clear();
		super.onDestroy();
	}

	final Runnable GetGroup = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", permission.getID());
				String response = HTTP.excute("getClxxGroupByPermid", "RestPermissionitemService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					groups = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<PermGroup>>() {
					}.getType());

					if (null != groups && groups.size() > 0) {
						runOnUiThread(new Runnable() {
							public void run() {
								String[] groupNames = new String[groups.size()];
								for (int i = 0; i < groupNames.length; i++) {
									groupNames[i] = groups.get(i).getP_GROUP_NAME();
								}
								new AlertDialog.Builder(WorkSpace.this).setTitle("请选择材料分组").setIcon(MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_ic_dialog_info")).setSingleChoiceItems(groupNames, 0, new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog, int index) {
										P_GROUP_ID = groups.get(index).getP_GROUP_ID();
										P_GROUP_NAME = groups.get(index).getP_GROUP_NAME();
										dialog.dismiss();
									}
								}).show();
							}
						});
					}

				} else {
					DialogUtil.showUIToast(WorkSpace.this, json.getString("error"));
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(WorkSpace.this, getString(MSFWResource.getResourseIdByName(WorkSpace.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class ItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			if (null == view.getTag()) {
				DialogUtil.showUIToast(WorkSpace.this, "不能进行当前操作");
				return;
			}

			switch (position) {
			case 0:// 办事指南
				intent = new Intent();
				intent.setClass(WorkSpace.this, PermGuideContainer.class);
				intent.putExtra("PERMID", permission.getID());
				intent.putExtra("permission", permission);
				intent.putExtra("position", 0);
				intent.putExtra("from", "WorkSpace");
				startActivity(intent);
				break;
			case 1:// 申报详情
				
				goHistoreShare();
	
				break;
			case 2:// 进度查询
				intent = new Intent();
				intent.setClass(WorkSpace.this, Find.class);
				intent.putExtra("business", business);
				intent.putExtra("APPNAME", Constants.user.getREALNAME());
				startActivity(intent);
				break;
			case 3:// 予受理通知
				intent = new Intent();
				intent.putExtra("BSNUM", BSNUM);
				intent.setClass(WorkSpace.this, PreacceptNotice.class);
				startActivity(intent);
				break;

			case 4:// 网上预约
				dialog = Background.Process(WorkSpace.this, GetReserve, getString(MSFWResource.getResourseIdByName(WorkSpace.this, "string", "tj_loding")));
				break;

			case 5:// 互动交流
				intent = new Intent();
				intent.putExtra("BSNUM", BSNUM);
				intent.setClass(WorkSpace.this, ExamineMSG.class);
				startActivity(intent);
				break;

			case 6:// 结果通知
				intent = new Intent();
				intent.setClass(WorkSpace.this, ApplyResult.class);
				intent.putExtra("BSNUM", BSNUM);
				startActivity(intent);

				break;
			case 7:// 服务评价
				intent = new Intent();
				intent.setClass(WorkSpace.this, AdviceSubmit.class);
				intent.putExtra("permission", permission);
				intent.putExtra("BSNUM", BSNUM);
				startActivity(intent);
				break;
			case 8:// 结果通知

				break;
			case 9:// 服务评价

				break;
			case 10:// 网上付费
				DialogUtil.showUIToast(WorkSpace.this, "暂未开通!");
				break;
			case 11:// 协同计算
				// DialogUtil.showUIToast(WorkSpace.this, "暂未开通!");
				intent = new Intent();
				intent.setClass(WorkSpace.this, PermListByBusinessId.class);
				intent.putExtra("CBUSINESSID", business.getCBUSINESSID());
				intent.putExtra("CFLOWID", business.getCITEMID());
				intent.putExtra("CITEMVERSION", business.getCITEMVERSION());
				startActivity(intent);
				break;

			default:
				break;
			}
		}

	}
	
	private void goHistoreShare() {
		intent = new Intent();
		intent.putExtra("permission", permission);
		intent.putExtra("mark", "4");
		intent.putExtra("BSNUM", BSNUM);
		intent.putExtra("STATUS", STATUS);
		intent.putExtra("P_GROUP_ID", P_GROUP_ID);
		intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
		intent.setClass(WorkSpace.this, HistoreShare_v2.class);
		startActivity(intent);
		
	}

	final Runnable GetReserve = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("token", Constants.user.getTOKEN());
				param.put("BSNUM", BSNUM);
				String response = HTTP.excute("search", "RestOnlineReserveService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					ReserveByBS reserveByBS = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), ReserveByBS.class);
					if (null != reserveByBS && null != reserveByBS.getRESERVEDATE() && !reserveByBS.getRESERVEDATE().equals("")) {
						intent = new Intent();
						intent.setClass(WorkSpace.this, ReserveDetail.class);
						intent.putExtra("reserveByBS", reserveByBS);
						intent.putExtra("BSNUM", BSNUM);
						startActivity(intent);
					} else {
						intent = new Intent();
						intent.setClass(WorkSpace.this, ReserveSubmit.class);
						intent.putExtra("permission", permission);
						intent.putExtra("BSNUM", BSNUM);
						startActivity(intent);
					}
				}

				else {
					DialogUtil.showUIToast(WorkSpace.this, json.getString("error"));
					finish();
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(WorkSpace.this, getString(MSFWResource.getResourseIdByName(WorkSpace.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return menuId.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return menuId[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view;
			if (null == convertView) {
				view = layoutInflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_work_menu_item"), null);
			} else {
				view = convertView;
			}
			MenuItem item = new MenuItem();
			item.bg = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "bg"));

			// 初始化单元格长宽
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dm.widthPixels / 4, dm.widthPixels / 4);
			item.bg.setLayoutParams(params);

			item.bg.setBackgroundResource(MSFWResource.getResourseIdByName(WorkSpace.this, "drawable", "tj_bg_gv"));
			item.bg.setImageDrawable(getResources().getDrawable(menuGoneId[position]));

			// 新申报 不支持移动申报
			if (STATUS == -1 && null != permission && null != permission.getSFYDSB() && !permission.getSFYDSB().equals("1") && (position == 0 || position == 1 || position == 2 || position == 15)) {
				item.bg.setImageDrawable(getResources().getDrawable(menuId[position]));
				view.setTag("clickable");
			}
			// 不予受理 ,预审不通过 1. 2.（不能操作），3
			if ((STATUS == 8 || STATUS == 10) && (position == 0 || position == 1 || position == 2)) {
				item.bg.setImageDrawable(getResources().getDrawable(menuId[position]));
				view.setTag("clickable");
			}
			// 新申报 支持移动申报
			if (STATUS == -1 && null != permission && null != permission.getSFYDSB() && permission.getSFYDSB().equals("1") && (position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5 || position == 6 || (position == 11 && isCooperate))) {
				item.bg.setImageDrawable(getResources().getDrawable(menuId[position]));
				view.setTag("clickable");
			}
			// 尚未预审 1.2不.3
			if (STATUS == 0 && (position == 0 || position == 1 || position == 2)) {
				item.bg.setImageDrawable(getResources().getDrawable(menuId[position]));
				view.setTag("clickable");
			}
			// 预审通过，办理中123456
			if ((STATUS == 1 || STATUS == 2) && (position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5)) {
				item.bg.setImageDrawable(getResources().getDrawable(menuId[position]));
				view.setTag("clickable");
			}
			// 已办结123.45678
			if ((STATUS == 3) && (position == 0 || position == 1 || position == 2 || position == 3 || position == 4 || position == 5 || position == 6 || position == 7)) {
				item.bg.setImageDrawable(getResources().getDrawable(menuId[position]));
				view.setTag("clickable");
			}
			// 不予受理 ,预审不通过 1. 2.（能操作），3
			if ((STATUS == 4 || STATUS == 9) && (position == 0 || position == 1 || position == 2)) {
				item.bg.setImageDrawable(getResources().getDrawable(menuId[position]));
				view.setTag("clickable");
			}

			return view;

		}

		public final class MenuItem {
			ImageView bg;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REGISTER_REQUEST && data != null && Constants.user == null) {
			String nameDemp = data.getStringExtra("userName");
			String passwordDemp = data.getStringExtra("password");
			if (nameDemp != null && !"".equals(nameDemp)) {
				userName = nameDemp;
			}
			if (passwordDemp != null && !"".equals(passwordDemp)) {
				password = passwordDemp;
			}

			dialog = Background.Process(this, Login, "正在登录...");
		}
	}

	final Runnable Login = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("USERNAME", userName);
				param.put("PASSWORD", Md5PwdEncoder.encodePassword(password));
				String response = HTTP.excute("login", "RestUserService", param.toString());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
					DialogUtil.showUIToast(WorkSpace.this, "登录成功！");
					FileUtil.Write(WorkSpace.this, "username", userName);
					FileUtil.Write(WorkSpace.this, "password", password);

				} else {
					String error = json.getString("error");
					DialogUtil.showUIToast(WorkSpace.this, error);
				}

			} catch (Exception e) {
				DialogUtil.showUIToast(WorkSpace.this, getString(MSFWResource.getResourseIdByName(WorkSpace.this, "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

}
