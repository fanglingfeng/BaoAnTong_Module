package com.tjsoft.webhall.ui.wsbs;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

import org.json.JSONObject;

import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.XMLUtil;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Guide;
import com.tjsoft.webhall.entity.Table;
import com.tjsoft.webhall.ui.work.PermGuideContainer;
import com.tjsoft.webhall.ui.wsbs.PermGuide.MyHandler;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class PermGuideByFavorite extends Fragment{
	private String PERMID = "";
	private TextView txt_SPTJ, txt_SQCL, txt_WORKFLOW, txt_WLBLLC, txt_LIMITDAYS, txt_SPSF, txt_CJWTJD, txt_SDYJ;
	private LinearLayout title1, title2, title3, title4, title5, title6, title7, title8, title9, title10;
	private ImageView status1, status2, status3, status4, status5, status6, status7, status8, status9, status10;
//	private ImageView favorite1,favorite2,favorite3,favorite4,favorite5,favorite6,favorite7,favorite8,favorite9,favorite10;
	private MyHandler handler;
	private Guide guide;
	private final int GET_GUIDE_SUCCESS = 1;
	private Button apply;
	private Intent intent;
	private TextView title;
	private LinearLayout mLinearLayout, tj_table_download;
	private double longitude;
	private double latitude;
	private ImageView[] imageViews;
	
//	private ImageView[] favoritesViews;
	protected ProgressDialog dialog;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_perm_guide_open"), container, false);
		Constants.getInstance().addActivity(this.getActivity());
		title = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title"));
		apply = (Button) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "apply"));
		apply.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				intent = new Intent();
				intent.setClass(getActivity(), ApplyOL_Step2.class);
				intent.putExtra("PERMID", PERMID);
				startActivity(intent);
			}
		});

		txt_SPTJ = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SPTJ"));
		txt_SQCL = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SQCL"));
		txt_WORKFLOW = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_WORKFLOW"));
		txt_WLBLLC = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_WLBLLC"));
		txt_LIMITDAYS = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_LIMITDAYS"));
		txt_SPSF = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SPSF"));
		txt_CJWTJD = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_CJWTJD"));
		txt_SDYJ = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SDYJ"));
		mLinearLayout = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "work_window"));
		tj_table_download = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "tj_table_download"));

		handler = new MyHandler();
		PERMID = this.getActivity().getIntent().getStringExtra("PERMID");
		Background.Process(getActivity(), GetGuide, getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_loding")));

		title1 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title1"));
		title2 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title2"));
		title3 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title3"));
		title4 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title4"));
		title5 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title5"));
		title6 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title6"));
		title7 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title7"));
		title8 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title8"));
		title9 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title9"));
		title10 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title10"));

		status1 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status1"));
		status2 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status2"));
		status3 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status3"));
		status4 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status4"));
		status5 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status5"));
		status6 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status6"));
		status7 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status7"));
		status8 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status8"));
		status9 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status9"));
		status10 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status10"));

//		favorite1 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite1"));
//		favorite2 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite2"));
//		favorite3 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite3"));
//		favorite4 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite4"));
//		favorite5 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite5"));
//		favorite6 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite6"));
//		favorite7 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite7"));
//		favorite8 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite8"));
//		favorite9 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite9"));
//		favorite10 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "addFavorite10"));

		
		initTitleOnclick();
		initTitleLongClick();
		imageViews = new ImageView[] { status1, status2, status3, status4, status5, status6, status7, status8, status9, status10 };
//		favoritesViews=new ImageView[]{favorite1,favorite2,favorite3,favorite4,favorite5,favorite6,favorite7,favorite8,favorite9,favorite10};
		super.onCreate(savedInstanceState);
		return view;
	}

	/**
	 * 获取办事指南
	 */
	final Runnable GetGuide = new Runnable() {
		@Override
		public void run() {
			try {
				JSONObject param = new JSONObject();
				param.put("PERMID", PERMID);
				param.put("USERID",Constants.user.getUSER_ID());
				String response = HTTP.excuteAndCache("getPermFavoriteKey", "RestPermissionitemService", param.toString(),getActivity());
				JSONObject json = new JSONObject(response);
				String code = json.getString("code");
				if (code.equals("200")) {
					guide = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), Guide.class);
					handler.sendEmptyMessage(GET_GUIDE_SUCCESS);
				} else {
					DialogUtil.showUIToast(getActivity(), getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_occurs_error_network")));
					getActivity().finish();
				}

			} catch (Exception e) {
//				DialogUtil.showUIToast(PermGuide.this.getActivity(), getString(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "string", "tj_occurs_error_network")));
				e.printStackTrace();

			}
		}
	};

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case GET_GUIDE_SUCCESS:
				title.setText(guide.getSXZXNAME());
				GuideSetText(txt_SPTJ,guide.getSPTJ(),title1);
//				if(!TextUtils.isEmpty(guide.getSPTJ())){
//					txt_SPTJ.setText(guide.getSPTJ());
//				}else{
//					title1.setVisibility(View.GONE);
//					txt_SPTJ.setVisibility(View.GONE);
//				}
				GuideSetText(txt_SQCL,guide.getSQCL(),title2);

//				if(!TextUtils.isEmpty(guide.getSQCL())){
//					txt_SQCL.setText(guide.getSQCL());
//				}else {
//					title2.setVisibility(View.GONE);
//					txt_SQCL.setVisibility(View.GONE);
//				}
				GuideSetText(txt_WORKFLOW,guide.getSPCX(),title3);
				GuideSetText(txt_WLBLLC,guide.getWLBLLC(),title4);
				GuideSetText(txt_LIMITDAYS,guide.getLIMITDAYS(),title5);
				GuideSetText(txt_SPSF,guide.getCHARGE(),title6);
				GuideSetText(txt_CJWTJD,guide.getCJWTJD(),title7);
				GuideSetText(txt_SDYJ,guide.getLAWPRODUCE(),title8);

				
//				txt_WORKFLOW.setText(guide.getSPCX());
//				txt_WLBLLC.setText(guide.getWLBLLC());
//				txt_LIMITDAYS.setText(guide.getLIMITDAYS());
//				txt_SPSF.setText(guide.getCHARGE());
//				txt_CJWTJD.setText(guide.getCJWTJD());
//				txt_SDYJ.setText(guide.getLAWPRODUCE());
				if (null != guide.getWINDOWS()&&guide.getWINDOWS().size()!=0) {
					for (int i = 0; i < guide.getWINDOWS().size(); i++) {
						LinearLayout container_root = new LinearLayout(getActivity());
						container_root.setOrientation(LinearLayout.HORIZONTAL);
						LinearLayout container = new LinearLayout(getActivity());
						container.setOrientation(LinearLayout.VERTICAL);
						container.addView(createMyTextView("窗口名称：" + guide.getWINDOWS().get(i).getNAME()));
						container.addView(createMyTextView("工作时间：" + guide.getWINDOWS().get(i).getOFFICEHOURS()));
						TextView mapView = createMyTextView("地址：" + guide.getWINDOWS().get(i).getADDRESS());
						container.addView(mapView);
						
						TextView phoneView = createMyTextView("联系电话：" + guide.getWINDOWS().get(i).getPHONE());
						phoneView.setTextColor(Color.RED);
						phoneView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
						final int position = i;
						phoneView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								new AlertDialog.Builder(getActivity()).setMessage("是否要拨打联系电话：" + guide.getWINDOWS().get(position).getPHONE()).setTitle(getString(MSFWResource.getResourseIdByName(getActivity(), "string", "tj_notify"))).setCancelable(false).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
										Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + guide.getWINDOWS().get(position).getPHONE()));
										startActivity(intent);
									}
								}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog, int id) {
									}
								}).show();

							}
						});
						container.addView(phoneView);
						container.addView(createMyTextView("交通指引：" + guide.getWINDOWS().get(i).getTRAFFIC() + "\n"));
						LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
						lParams.weight = 1;
						container_root.addView(container, lParams);
						LinearLayout.LayoutParams lp_bt = new LinearLayout.LayoutParams(DensityUtil.dip2px(getActivity(), 20), DensityUtil.dip2px(getActivity(), 25));
						lp_bt.setMargins(0, 0, 40, 0);
						Button button = new Button(getActivity());
						button.setBackgroundResource(MSFWResource.getResourseIdByName(getActivity(), "drawable", "tj_map_img"));
						container_root.addView(button, lp_bt);
						if (null == guide.getWINDOWS().get(position).getLATITUDE()) {
							button.setVisibility(View.INVISIBLE);
						}
						button.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								// 调起百度地图客户端
								try {

									intent = Intent.getIntent("intent://map/direction?origin=latlng:" + latitude + "," + longitude + "|name:我的位置&destination=latlng:" + Double.parseDouble(guide.getWINDOWS().get(position).getLATITUDE()) + "," + Double.parseDouble(guide.getWINDOWS().get(position).getLONGITUDE()) + "|name:办事处&mode=driving&referer=Autohome|GasStation#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
									if (isInstallByread("com.baidu.BaiduMap")) {
										startActivity(intent); // 启动调用
										Log.e("GasStation", "百度地图客户端已经安装");
									} else {
										Log.e("GasStation", "没有安装百度地图客户端");
										intent = new Intent(getActivity(), MapNavigation.class);
										intent.putExtra("LONGITUDE", guide.getWINDOWS().get(position).getLONGITUDE());
										intent.putExtra("LATITUDE", guide.getWINDOWS().get(position).getLATITUDE());
										startActivity(intent);
									}
								} catch (URISyntaxException e) {
									e.printStackTrace();
								}

							}
						});
						mLinearLayout.addView(container_root);
						mLinearLayout.setVisibility(View.VISIBLE);
					}
				} else {
					title9.setVisibility(View.GONE);
//					TextView txt_WINDOWS = new TextView(getActivity());
//					txt_WINDOWS.setText("暂无");
//					mLinearLayout.addView(txt_WINDOWS);
					mLinearLayout.setVisibility(View.GONE);
				}
				String xml = guide.getFORMS();
				if (null != xml) {
					List<Table> tables = XMLUtil.getTables(xml);
					if (null != tables) {
						for (int i = 0; i < tables.size(); i++) {
							LinearLayout root = new LinearLayout(getActivity());
							root.setOrientation(LinearLayout.VERTICAL);
							TextView tv = createMyTextViewForTable(tables.get(i).getName());
							tv.setTag(tables.get(i).getId());
							tv.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// DialogUtil.showUIToast(PermGuide.this.getActivity(),
									Uri uri = Uri.parse(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + v.getTag().toString());
									Intent intent = new Intent(Intent.ACTION_VIEW, uri);
									startActivity(intent);
								}
							});
							root.addView(tv);
							if (null == tj_table_download) {
								return;
							}

							tj_table_download.addView(root);
						}
					} else {

						TextView txt_WINDOWS = new TextView(getActivity());
						txt_WINDOWS.setText("暂无");
						if (null == tj_table_download) {
							return;
						}
						tj_table_download.addView(txt_WINDOWS);
					}
				}else{
					title10.setVisibility(View.GONE);
				}

				break;
			default:
				break;
			}
		}
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

	private TextView createMyTextView(String str) {
		TextView textView = new TextView(getActivity());
		textView.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(getActivity(), "color", "tj_perm_guide_txt")));
		textView.setTextSize(15);
		int px = DensityUtil.dip2px(getActivity(), 10);
		textView.setPadding(px, 0, px, 0);
		textView.setText(str);
		return textView;
	}

	private TextView createMyTextViewForTable(String str) {
		TextView textView = new TextView(getActivity());
		textView.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(getActivity(), "color", "tj_perm_guide_txt")));
		textView.setTextSize(15);
		textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		textView.getPaint().setAntiAlias(true);// 抗锯齿
		int px = DensityUtil.dip2px(getActivity(), 10);
		textView.setPadding(px, px, px, 0);
		textView.setText(str);
		return textView;
	}

	private void changeStatus(View v, int index) {
		int visibility = v.getVisibility();
		if (visibility == View.GONE) {
			v.setVisibility(View.VISIBLE);
			imageViews[index - 1].setBackgroundResource(MSFWResource.getResourseIdByName(getActivity(), "drawable", "tj_arrows_open"));
//			favoritesViews[index-1].setVisibility(View.VISIBLE);
		} else {
			v.setVisibility(View.GONE);
			imageViews[index - 1].setBackgroundResource(MSFWResource.getResourseIdByName(getActivity(), "drawable", "tj_arrows"));
//			favoritesViews[index-1].setVisibility(View.GONE);
		}
	}
	

	/**
	 * 初始化item点击动作
	 */
	private void initTitleOnclick() {
		title1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_SPTJ, 1);
			}
		});
		title2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_SQCL, 2);
			}
		});
		title3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_WORKFLOW, 3);
			}
		});
		title4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_WLBLLC, 4);
			}
		});
		title5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_LIMITDAYS, 5);
			}
		});
		title6.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_SPSF, 6);
			}
		});
		title7.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_CJWTJD, 7);
			}
		});
		title8.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(txt_SDYJ, 8);
			}
		});
		title9.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(mLinearLayout, 9);
			}
		});
		title10.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				changeStatus(tj_table_download, 10);
			}
		});
	}
	
	private void initTitleLongClick(){
		title1.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("1");
				return false;
			}
		});
		title2.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("2");
				return false;
			}
		});
		title3.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("3");
				return false;
			}
		});
		title4.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("4");
				return false;
			}
		});
		title5.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("5");
				return false;
			}
		});
		title6.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("6");
				return false;
			}
		});
		title7.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("10");
				return false;
			}
		});
		title8.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("7");
				return false;
			}
		});
		title9.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("8");
				return false;
			}
		});
		title10.setOnLongClickListener(new OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub
				deleteFavorite("9");
				return false;
			}
		});
	}
	private void deleteFavorite(final String spys){
		final Runnable deleteFavorite=new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					JSONObject param=new JSONObject();
					param.put("PERMID", PERMID);
					param.put("USERID", Constants.user.getUSER_ID());
					param.put("PERMKEY", spys);
					String response = HTTP.excute("cancelPermFavorite", "RestPermissionitemService", param.toString());
					JSONObject json = new JSONObject(response);
					String code = json.getString("code");
					if(code.equals("200")){
						DialogUtil.showUIToast(getActivity(), "取消收藏成功！");
						getActivity().runOnUiThread( new Runnable() {
							public void run() {
								dialog.dismiss();
								int caseValue = 0;
								try {
									caseValue = Integer.parseInt(spys);
								} catch (Exception e) {
									// TODO: handle exception
								}					
								switch (caseValue) {
								case 1:
									title1.setVisibility(View.GONE);
									txt_SPTJ.setVisibility(View.GONE);
									break;
								case 2:
									title2.setVisibility(View.GONE);
									txt_SQCL.setVisibility(View.GONE);
									break;
								case 3:
									title3.setVisibility(View.GONE);
									txt_WORKFLOW.setVisibility(View.GONE);
									break;
								case 4:
									title4.setVisibility(View.GONE);
									txt_WLBLLC.setVisibility(View.GONE);
									break;
								case 5:
									title5.setVisibility(View.GONE);
									txt_LIMITDAYS.setVisibility(View.GONE);
									break;
								case 6:
									title6.setVisibility(View.GONE);
									txt_SPSF.setVisibility(View.GONE);
									break;
								case 7:
									title8.setVisibility(View.GONE);
									txt_SDYJ.setVisibility(View.GONE);
									break;
								case 8:
									title9.setVisibility(View.GONE);
									mLinearLayout.setVisibility(View.GONE);
									break;
								case 9:
									title10.setVisibility(View.GONE);
									tj_table_download.setVisibility(View.GONE);
									break;
								case 10:
									title7.setVisibility(View.GONE);
									txt_CJWTJD.setVisibility(View.GONE);
									break;
								default:
									break;
								}
//								FavoriteManage.add(permission, PermGuideContainer.this);
							}
						});
					}else{
						dialog.dismiss();
						DialogUtil.showUIToast(getActivity(), "取消收藏失败，请稍后重试！");
					}
				} catch (Exception e) {
					dialog.dismiss();
					// TODO Auto-generated catch block
					DialogUtil.showUIToast(getActivity(), "取消收藏失败，请稍后重试！");
					e.printStackTrace();
				}
			}
		};
		dialog=Background.Process(getActivity(), deleteFavorite, "正在取消收藏审批要素……");
	}
	
	
	
    private void GuideSetText(TextView textView,String s,LinearLayout linearLayout){
        if (!TextUtils.isEmpty(s)){
            textView.setText(s);
        }else {
            textView.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }
    }
}
