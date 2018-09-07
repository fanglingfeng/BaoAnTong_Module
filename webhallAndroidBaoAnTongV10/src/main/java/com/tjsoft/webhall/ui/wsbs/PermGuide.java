package com.tjsoft.webhall.ui.wsbs;

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
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.tjsoft.util.Background;
import com.tjsoft.util.DensityUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.XMLUtil;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.BLMS;
import com.tjsoft.webhall.entity.Guide;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.Table;
import com.tjsoft.webhall.lib.ListViewForScrollView;
import com.tjsoft.webhall.ui.work.PermGuideContainer;

import org.json.JSONObject;

import java.io.File;
import java.net.URISyntaxException;
import java.util.List;

/**
 * 办事指南
 *
 * @author Administrator
 */

public class PermGuide extends Fragment {
    private String PERMID = "";
    private ListViewForScrollView txt_SQCL;
    private TextView txt_SPTJ, txt_WORKFLOW, txt_WLBLLC, txt_LIMITDAYS, txt_SPSF, txt_CJWTJD, txt_SDYJ, txt_blms;
    private LinearLayout title1, title2, title3, title4, title5, title6, title7, title8, title9, title10, title11;
    private ImageView status1, status2, status3, status4, status5, status6, status7, status8, status9, status10, status11;

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
    SQCLAdapter sqclAdapter;
    protected ProgressDialog dialog;

    public static Permission permission;
//	private ImageView favorite1,favorite2,favorite3,favorite4,favorite5,favorite6,favorite7,favorite8,favorite9,favorite10;
//	private ImageView[] favoritesViews;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(MSFWResource.getResourseIdByName(getActivity(), "layout", "tj_perm_guide"), container, false);
        Constants.getInstance().addActivity(this.getActivity());
        title = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title"));
        apply = (Button) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "apply"));
        apply.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setClass(PermGuide.this.getActivity(), ApplyOL_Step2.class);
                intent.putExtra("PERMID", PERMID);
                startActivity(intent);
            }
        });

        txt_SPTJ = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SPTJ"));
        txt_SQCL = (ListViewForScrollView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SQCL"));
        txt_WORKFLOW = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_WORKFLOW"));
        txt_WLBLLC = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_WLBLLC"));
        txt_LIMITDAYS = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_LIMITDAYS"));
        txt_SPSF = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SPSF"));
        txt_CJWTJD = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_CJWTJD"));
        txt_blms = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_blms"));
        txt_SDYJ = (TextView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "txt_SDYJ"));
        mLinearLayout = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "work_window"));
        tj_table_download = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "tj_table_download"));

        handler = new MyHandler();
//		PERMID = this.getActivity().getIntent().getStringExtra("PERMID");
//		Background.Process(PermGuide.this.getActivity(), GetGuide, getString(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "string", "tj_loding")));
        guide = PermGuideContainer.guide;
        if (guide != null) {
            sqclAdapter = new SQCLAdapter(getActivity(), guide.getSQCLSM());
        }

        txt_SQCL.setAdapter(sqclAdapter);
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
        title11 = (LinearLayout) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "title11"));

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
        status11 = (ImageView) view.findViewById(MSFWResource.getResourseIdByName(getActivity(), "id", "status11"));


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
//		initTitleLongClick();
        imageViews = new ImageView[]{status1, status2, status3, status4, status5, status6, status7, status8, status9, status10, status11};

//		favoritesViews=new ImageView[]{favorite1,favorite2,favorite3,favorite4,favorite5,favorite6,favorite7,favorite8,favorite9,favorite10};
        initView();
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
                String response = HTTP.excuteAndCache("getPermissionByPermid", "RestPermissionitemService", param.toString(), getActivity());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    guide = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), Guide.class);
                    handler.sendEmptyMessage(GET_GUIDE_SUCCESS);
                } else {
                    DialogUtil.showUIToast(PermGuide.this.getActivity(), getString(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "string", "tj_occurs_error_network")));
                    getActivity().finish();
                }

            } catch (Exception e) {
//				DialogUtil.showUIToast(PermGuide.this.getActivity(), getString(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };

    private String setIsNullString(String tip) {
        String value = "暂无数据";
        if (!TextUtils.isEmpty(tip) && !tip.equals("null")) {
            return tip;
        } else {
            return value;
        }
    }

    private void initView() {
//        Log.e("sps===", guide.getPERMISSION().toString());
        if (guide != null) {
            permission = guide.getPERMISSION();

        }
//        Log.e("sps-==", "permission===" + permission.getDEPTNAME());
        title.setText(guide.getSXZXNAME());
        txt_SPTJ.setText(setIsNullString(guide.getSPTJ()));
        txt_WORKFLOW.setText(setIsNullString(guide.getSPCX()));
        txt_WLBLLC.setText(setIsNullString(guide.getWLBLLC()));
        txt_LIMITDAYS.setText(setIsNullString(guide.getLIMITDAYS()));
        txt_SPSF.setText(setIsNullString(guide.getCHARGE()));
        txt_CJWTJD.setText(setIsNullString(guide.getCJWTJD()));
        txt_SDYJ.setText(setIsNullString(guide.getLAWPRODUCE()));
        if (guide.getBLMS() != null) {
            BLMS blms = guide.getBLMS();
            if (blms == null) {
                blms = new BLMS();
            }
            String txtBlmsStr = "", txtDjStr = "", txtLqStr = "";
            if (!TextUtils.isEmpty(blms.getBLMS()) && blms.getBLMS().contains("1")) {
                txtBlmsStr = "零次到现场全流程网上办理";
            } else if (!TextUtils.isEmpty(blms.getBLMS()) && blms.getBLMS().contains("2")) {
                txtBlmsStr = "线上申请、线上提交(受理环节递交纸质申请材料)";
            } else if (!TextUtils.isEmpty(blms.getBLMS()) && blms.getBLMS().contains("3")) {
                txtBlmsStr = "线上申请、线上提交(领证环节递交纸质申请材料)";
            } else if (!TextUtils.isEmpty(blms.getBLMS()) && blms.getBLMS().contains("5")) {
                txtBlmsStr = "线上预约、线下提交";
            } else if (!TextUtils.isEmpty(blms.getBLMS()) && blms.getBLMS().contains("4")) {
                txtBlmsStr = "线上申请、线上提交(现场审查环节递交纸质申请材料)";
            }
            if (!TextUtils.isEmpty(blms.getDJZZCL()) && blms.getDJZZCL().contains("1")) {
                txtDjStr = "\n纸质材料递交方式：网点递交";
            } else if (!TextUtils.isEmpty(blms.getDJZZCL()) && blms.getDJZZCL().contains("2")) {
                txtDjStr = "\n纸质材料递交方式：速递服务";
            } else if (!TextUtils.isEmpty(blms.getDJZZCL()) && blms.getDJZZCL().contains("3")) {
                txtDjStr = "\n纸质材料递交方式：无需递交";
            }
            if (!TextUtils.isEmpty(blms.getLQSPJG()) && blms.getLQSPJG().contains("1")) {
                txtDjStr = "\n结果证件领取方式：网点领取";
            } else if (!TextUtils.isEmpty(blms.getLQSPJG()) && blms.getLQSPJG().contains("2")) {
                txtDjStr = "\n结果证件领取方式：速递服务";
            } else if (!TextUtils.isEmpty(blms.getLQSPJG()) && blms.getLQSPJG().contains("3")) {
                txtDjStr = "\n结果证件领取方式：自行打印";
            }
            txt_blms.setText(setIsNullString(txtBlmsStr + txtDjStr + txtLqStr));
        }
        if (null != guide.getWINDOWS() && guide.getWINDOWS().size() != 0) {
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
                                PermGuide.this.startActivity(intent);
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
                button.setBackgroundResource(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "drawable", "tj_map_img"));
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
            }
        } else {
            TextView txt_WINDOWS = new TextView(getActivity());
            int px = DensityUtil.dip2px(getActivity(), 10);
            txt_WINDOWS.setPadding(px, px, px, px);
            txt_WINDOWS.setText("暂无数据");
            mLinearLayout.addView(txt_WINDOWS);
        }
        String xml = guide.getFORMS();
        if (!xml.equals("null") && !TextUtils.isEmpty(xml)) {
            List<Table> tables = XMLUtil.getTables(xml);
            if (null != tables) {
                for (int i = 0; i < tables.size(); i++) {
                    LinearLayout root = new LinearLayout(getActivity());
                    root.setOrientation(LinearLayout.VERTICAL);
                    TextView tv = createMyTextViewForTable(tables.get(i).getName());
                    tv.setTag(tables.get(i));
                    tv.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // DialogUtil.showUIToast(PermGuide.this.getActivity(),
                            Table bean = (Table) v.getTag();
                            Uri uri = null;
                            if (TextUtils.isEmpty(bean.getId())) {
                                Toast.makeText(getActivity(), "该文件暂未提供预览功能", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!TextUtils.isEmpty(bean.getSource()) && bean.getSource().equals("sz")) {
//								uri = Uri.parse("http://sxsl.sz.gov.cn/qz"+bean.getId().toString());
                                    if (bean.getId().toString().contains("upload")) {
                                        uri = Uri.parse("http://sxsl.sz.gov.cn/qz" + bean.getId().toString());
                                    } else {
                                        uri = Uri.parse("http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + bean.getId().toString());
                                    }
                                } else if (!TextUtils.isEmpty(bean.getSource()) && bean.getSource().equals("pro")) {
                                    uri = Uri.parse("http://sxsl.sz.gov.cn/qzapi/qzqd/attach/upload/" + bean.getId().toString());
                                } else {
                                    uri = Uri.parse(Constants.DOMAIN + "servlet/downloadFileServlet?fileNo=" + v.getTag().toString());
                                }
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }

                        }
                    });
                    root.addView(tv);
                    if (null == tj_table_download) {
                        return;
                    }

                    tj_table_download.addView(root);
                }
            }
        } else {
            TextView txt_WINDOWS = new TextView(getActivity());
            int px = DensityUtil.dip2px(getActivity(), 10);
            txt_WINDOWS.setPadding(px, px, px, px);
            txt_WINDOWS.setText("暂无数据");
            if (null == tj_table_download) {
                return;
            }
            tj_table_download.addView(txt_WINDOWS);
        }

    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_GUIDE_SUCCESS:


                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }

    private TextView createMyTextView(String str) {
        TextView textView = new TextView(getActivity());
        textView.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "color", "tj_perm_guide_txt")));
        textView.setTextSize(15);
        int px = DensityUtil.dip2px(getActivity(), 10);
        textView.setPadding(px, 0, px, 0);
        textView.setText(str);
        return textView;
    }

    private TextView createMyTextViewForTable(String str) {
        TextView textView = new TextView(getActivity());
        textView.setTextColor(getResources().getColor(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "color", "tj_perm_guide_txt")));
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
            imageViews[index - 1].setBackgroundResource(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "drawable", "tj_arrows_open"));
        } else {
            v.setVisibility(View.GONE);
            imageViews[index - 1].setBackgroundResource(MSFWResource.getResourseIdByName(PermGuide.this.getActivity(), "drawable", "tj_arrows"));
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
        title11.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeStatus(txt_blms, 11);
            }
        });
    }

    //	private void initFavoriteClick(){
//		favorite1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(1);
//			}
//		});
//		favorite2.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(2);
//			}
//		});
//		favorite3.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(3);
//			}
//		});
//		favorite4.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(4);
//			}
//		});
//		favorite5.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(5);
//			}
//		});
//		favorite6.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(6);
//			}
//		});
//		favorite7.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(7);
//			}
//		});
//		favorite8.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(8);
//			}
//		});
//		favorite9.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(9);
//			}
//		});
//		favorite10.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				addFavorite(10);
//			}
//		});
//	}
//		
    private void initTitleLongClick() {
        title1.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("1");
                return false;
            }
        });
        title2.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("2");
                return false;
            }
        });
        title3.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("3");
                return false;
            }
        });
        title4.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("4");
                return false;
            }
        });
        title5.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("5");
                return false;
            }
        });
        title6.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("6");
                return false;
            }
        });
        title7.setOnLongClickListener(new OnLongClickListener() {
            //常见问题
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("10");
                return false;
            }
        });
        title8.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("7");
                return false;
            }
        });
        title9.setOnLongClickListener(new OnLongClickListener() {
            //办理窗口
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("8");
                return false;
            }
        });
        title10.setOnLongClickListener(new OnLongClickListener() {
            //审批表格
            @Override
            public boolean onLongClick(View v) {
                // TODO Auto-generated method stub
                addFavorite("9");
                return false;
            }
        });
    }

    private void addFavorite(final String spys) {
        final Runnable addMyFavorite = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    JSONObject param = new JSONObject();
                    param.put("PERMID", PERMID);
                    param.put("USERID", Constants.user.getUSER_ID());
                    param.put("PERMKEY", spys);
                    String response = HTTP.excute("savePermFavorite", "RestPermissionitemService", param.toString());
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    if (code.equals("200")) {
                        DialogUtil.showUIToast(getActivity(), "收藏成功！");
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                dialog.dismiss();
//								FavoriteManage.add(permission, PermGuideContainer.this);
                            }
                        });
                    } else {
                        dialog.dismiss();
                        DialogUtil.showUIToast(getActivity(), "收藏失败，请稍后重试！");
                    }
                } catch (Exception e) {
                    dialog.dismiss();
                    // TODO Auto-generated catch block
                    DialogUtil.showUIToast(getActivity(), "收藏失败，请稍后重试！");
                    e.printStackTrace();
                }
            }
        };
        dialog = Background.Process(getActivity(), addMyFavorite, "正在收藏审批要素……");
    }

}
