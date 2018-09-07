package com.tjsoft.webhall.ui.wsbs;

import java.io.File;
import java.net.URISyntaxException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.entity.Dept;
import com.tjsoft.webhall.entity.NetWorkBean;

/**
 * 
 * 网点详情
 * 
 * @author long
 * 
 */
public class NetworkDetail extends AutoDialogActivity {
	private RelativeLayout getList;
	private RelativeLayout layoutAdd;
	private TextView NETWORKNAME;
	private TextView NETWORKADDRESS;
	private TextView OFFICEHOURS;
	private TextView NETWORKPHONE;
	private TextView TRAFFIC;
	private TextView PERMNUM;
	private RelativeLayout back;
	private ImageButton location;
	private NetWorkBean netWorkBean;
	private Intent intent;
	private double longitude = 108.95108518068;
	private double latitude = 34.264642646862;
	private LocationClient locationClient = null;
	private static final int UPDATE_TIME = 5000;
	private static int LOCATION_COUTNS = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_network_detail"));

		initView();
		initLocation();
	}

	private void initLocation() {
		locationClient = new LocationClient(this);
		// 设置定位条件
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 是否打开GPS
		option.setCoorType("bd09ll"); // 设置返回值的坐标类型。
		option.setProdName("LocationDemo"); // 设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
		option.setScanSpan(UPDATE_TIME);// 设置定时定位的时间间隔。单位毫秒
		locationClient.setLocOption(option);

		// 注册位置监听器
		locationClient.registerLocationListener(new BDLocationListener() {

			@Override
			public void onReceiveLocation(BDLocation location) {
				// TODO Auto-generated method stub
				if (location == null) {
					return;
				}
				StringBuffer sb = new StringBuffer(256);
				sb.append("Time : ");
				sb.append(location.getTime());
				sb.append("\nError code : ");
				sb.append(location.getLocType());
				sb.append("\nLatitude : ");
				sb.append(location.getLatitude());
				sb.append("\nLontitude : ");
				sb.append(location.getLongitude());
				sb.append("\nRadius : ");
				sb.append(location.getRadius());
				if (location.getLocType() == BDLocation.TypeGpsLocation) {
					sb.append("\nSpeed : ");
					sb.append(location.getSpeed());
					sb.append("\nSatellite : ");
					sb.append(location.getSatelliteNumber());
				} else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
					sb.append("\nAddress : ");
					sb.append(location.getAddrStr());
				}
				LOCATION_COUTNS++;
				sb.append("\n检查位置更新次数：");
				sb.append(String.valueOf(LOCATION_COUTNS));

				System.out.println("########    " + location.getLatitude());
				System.out.println("########    " + location.getLongitude());
				latitude = location.getLatitude();
				longitude = location.getLongitude();
			}

		});

		locationClient.start();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
	}

	private void startBaiduMap() {
		try {
			// intent =
			// Intent.getIntent("intent://map/direction?origin=latlng:34.264642646862,108.95108518068|name:我家&destination=大雁塔&mode=driving®ion=西安&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
			intent = Intent.getIntent("intent://map/direction?origin=latlng:" + latitude + "," + longitude + "|name:我的位置&destination=latlng:" + Double.parseDouble(netWorkBean.getLATITUDE()) + "," + Double.parseDouble(netWorkBean.getLONGITUDE()) + "|name:办事处&mode=driving&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
			if (isInstallByread("com.baidu.BaiduMap")) {
				startActivity(intent); // 启动调用
				Log.e("GasStation", "百度地图客户端已经安装");
			} else {
				Log.e("GasStation", "没有安装百度地图客户端");
				intent = new Intent(NetworkDetail.this, MapNavigation.class);
				intent.putExtra("LONGITUDE", netWorkBean.getLONGITUDE());
				intent.putExtra("LATITUDE", netWorkBean.getLATITUDE());
				startActivity(intent);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
	}

	private void initView() {

		netWorkBean = (NetWorkBean) getIntent().getSerializableExtra("netWorkBean");
		getList = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "getList"));
		layoutAdd = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "layoutAdd"));
		
		layoutAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startBaiduMap();
				
			}
		});
		getList.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (netWorkBean.getAREAID().equals("440306")) {
					intent = new Intent();
					intent.putExtra("isBAOAN", true);
					intent.setClass(NetworkDetail.this, DeptList.class);
					startActivity(intent);

				} else {
					intent = new Intent();
					Dept dept = new Dept();
					dept.setDEPTID(netWorkBean.getPARENTID());
					intent.putExtra("dept", dept);
					intent.setClass(NetworkDetail.this, DeptList.class);
					startActivity(intent);

				}

			}
		});
		NETWORKNAME = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "NETWORKNAME"));
		NETWORKADDRESS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "NETWORKADDRESS"));
		OFFICEHOURS = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "OFFICEHOURS"));
		NETWORKPHONE = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "NETWORKPHONE"));
		TRAFFIC = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "TRAFFIC"));
		PERMNUM = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "PERMNUM"));
		PERMNUM.setText("服务中心可办理" + netWorkBean.getPERMNUM() + "项事项");
		NETWORKNAME.setText(netWorkBean.getNETWORKNAME());
		NETWORKADDRESS.setText(netWorkBean.getNETWORKADDRESS());
		TRAFFIC.setText(netWorkBean.getTRAFFIC());
		if (TextUtils.isEmpty(netWorkBean.getOFFICEHOURS())) {
			OFFICEHOURS.setText("暂无");
		} else {
			OFFICEHOURS.setText(netWorkBean.getOFFICEHOURS());
		}
		NETWORKPHONE.setText(netWorkBean.getNETWORKPHONE());

		back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
		location = (ImageButton) findViewById(MSFWResource.getResourseIdByName(this, "id", "location"));
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 调起百度地图客户端
				startBaiduMap();


			}

		});

		NETWORKPHONE.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new AlertDialog.Builder(NetworkDetail.this).setMessage("是否要拨打联系电话：" + netWorkBean.getNETWORKPHONE()).setTitle(getString(MSFWResource.getResourseIdByName(NetworkDetail.this, "string", "tj_notify"))).setCancelable(false).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + netWorkBean.getNETWORKPHONE()));
						NetworkDetail.this.startActivity(intent);
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				}).show();

			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

	}

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	private boolean isInstallByread(String packageName) {
		return new File("/data/data/" + packageName).exists();
	}
}
