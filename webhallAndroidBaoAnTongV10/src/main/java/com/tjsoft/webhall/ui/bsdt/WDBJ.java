package com.tjsoft.webhall.ui.bsdt;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.google.zxing.CaptureActivity;
import com.tjsoft.util.Background;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.JSONUtil;
import com.tjsoft.util.LoginBaoAnTongUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.util.Md5PwdEncoder;
import com.tjsoft.util.StatisticsTools;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.BanJian;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.entity.UserPermBean;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.imp.TransportCallBack;
import com.tjsoft.webhall.lib.XListView;
import com.tjsoft.webhall.lib.XListView.IXListViewListener;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的办件主页面
 *
 * @author Administrator
 */
public class WDBJ extends AutoDialogActivity implements IXListViewListener {

    public static String[] regionNames;
    private Intent intent;
    private LayoutInflater inflater;
    private List<BanJian> banjians = new ArrayList<BanJian>();
    private int PAGENO = 1;
    private String BSNUM = "";
    private String USERTYPE = "";
    private String PERMID = "";
    private int STATUS;//办件状态
    private RelativeLayout back;
    private Context mContext;
    private String username, password;
    private final static int SHOW_DIALOG = 1; // 登录时显示进度对话框
    private final static int CLOSE_DIALOG = 2; // 关闭进度对话框
    private final static int SHOW_TOAST = 3; // 显示提示框
    private ProgressDialog progressDialog;
    private String REALNAME = "";
    private String CERTIFICATETYPE = "";
    private String USER_PID = "";
    private String USER_MOBILE = "";
    private String BtnTxt = "";
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String[] types = {"全部", "在办件", "办结件", "暂存件", "退回件", "补交件"};
    private XListView xListView;
    private BanJianAdapter banjianAdapter;
    private String[] metheds = {"wodebanjian", "zaibanjian", "banjiejian", "zancunjian", "tuihuijian", "bujiaojian"};
    private String methed = "wodebanjian";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatisticsTools.start();
        mContext = this;
        inflater = getLayoutInflater();
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_wdbj"));
        Constants.getInstance().addActivity(this);
        if (null == Constants.user) {
            DialogUtil.showUIToast(this, "您还没有登录");
            finish();
        } else {
            initView();
        }

    }

    private void initView() {

        back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                WDBJ.this.finish();
            }
        });
        xListView = (XListView) findViewById(MSFWResource.getResourseIdByName(this, "id", "xListView"));
        ImageView empty = (ImageView) findViewById(MSFWResource.getResourseIdByName(this, "id", "empty"));
        xListView.setEmptyView(empty);
        xListView.setOnItemClickListener(new MyItemListener());
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        banjianAdapter = new BanJianAdapter(banjians);
        xListView.setAdapter(banjianAdapter);

        spinner = (Spinner) findViewById(MSFWResource.getResourseIdByName(this, "id", "spinner"));
        adapter = new ArrayAdapter<String>(WDBJ.this, MSFWResource.getResourseIdByName(WDBJ.this, "layout", "my_spinner_item_consult2"), types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
                banjians.clear();
                methed = metheds[position];
                dialog = Background.Process(WDBJ.this, GetWDBJ, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_loding")));

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        StatisticsTools.end("我的办件", null, null);
        super.onDestroy();
    }

    final Runnable Register = new Runnable() {
        @Override
        public void run() {
            try {

                JSONObject param = new JSONObject();

                param.put("USERNAME", username);
                param.put("PASSWORD", Md5PwdEncoder.encodePassword(password));
                param.put("REALNAME", REALNAME);
                param.put("CERTIFICATETYPE", CERTIFICATETYPE);
                param.put("USER_PID", USER_PID);
                param.put("USER_MOBILE", USER_MOBILE);

                String response = HTTP.excute("registerUser", "RestUserService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    DialogUtil.showUIToast(WDBJ.this, "注册成功！");
                    GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                    if (gloabDelegete != null) {
                        gloabDelegete.doBoundMSTAccount(WDBJ.this, username, password, new TransportCallBack() {

                            @Override
                            public void onStart() {
                            }

                            @Override
                            public void onResult(int status) {// 0成功
                                if (status == 0) {
                                    new Thread(Login).start();
                                } else {
                                    DialogUtil.showUIToast(WDBJ.this, "绑定宝安通失败！");
                                }
                            }

                            @Override
                            public void onFinish() {
                            }
                        });
                    }
                    finish();

                } else {
                    Intent intent = new Intent();
                    intent.setClass(WDBJ.this, com.tjsoft.webhall.ui.user.Register.class);
                    startActivity(intent);
                    String error = json.getString("error");
                    DialogUtil.showUIToast(WDBJ.this, error + ",请手动注册网上办事大厅账号!");
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(WDBJ.this, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };

    /**
     * 判断实名信息是否完全
     *
     * @param t
     * @return
     */
    private boolean checkRealNameInfo(TransportEntity t) {
        if (TextUtils.isEmpty(t.getToken())) {
            return false;
        }
//		if (TextUtils.isEmpty(t.getBatUserName())) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getRealName())) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getIdCardType() + "")) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getIdCardNo())) {
//			return false;
//		}
//		if (TextUtils.isEmpty(t.getMobile())) {
//			return false;
//		}
        return true;
    }

    void checkLogin() {
        LoginBaoAnTongUtil.checkLogin(WDBJ.this);
//		if (!Constants.DEBUG_TOGGLE) {
//			/**
//			 * 判断是否登录MST
//			 */
//
//			GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//			if (gloabDelegete != null) {
//				TransportEntity transportEntity = gloabDelegete.get();
//				BtnTxt = Constants.ADD_INFO;
//				if (checkRealNameInfo(transportEntity)) {// 信息完整
//					BtnTxt = Constants.AUTO_LOGIN;
//				}
//				if (!transportEntity.isLoginStatus()) {// 是否登录民生通，没有的话去登录
//					gloabDelegete.loginMST(WDBJ.this);
//					return;
//				}
//
//				else if (TextUtils.isEmpty(transportEntity.getAccount())) {// 没有绑定网上办事大厅账号提示提示去绑定
//					isLoginAlert();
//					return;
//				} else if (TextUtils.isEmpty(transportEntity.getPassword())) {// 没有网上办事大厅密码提示去登录
//					intent = new Intent();
//					intent.setClass(WDBJ.this, Login.class);
//					intent.putExtra("account", transportEntity.getAccount());
//					startActivity(intent);
//					return;
//				} else {
//					if (null == Constants.user || !Constants.user.getUSERNAME().equals(transportEntity.getAccount())) {
//						username = transportEntity.getAccount();
//						password = transportEntity.getPassword();
//						Background.Process(WDBJ.this, Login, "正在登录...");
//						return;
//					}
//				}
//
//			}
//			if (null == Constants.user) {
//				isLoginAlert();
//				return;
//			}
//		} else {
//			if (null == Constants.user) {
//				intent = new Intent();
//				intent.setClass(WDBJ.this, Login.class);
//				startActivity(intent);
//				return;
//			}
//		}
    }

    /**
     * 是否自动注册
     */
    private void isAutoRegisterAlert() {
        new AlertDialog.Builder(this).setMessage("是否自动注册网上办事账号，账号为宝安通账号。密码默认为身份证后6位？").setTitle(getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_notify"))).setCancelable(true).setPositiveButton("是", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Background.Process(WDBJ.this, Register, "正在登录...");
            }
        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        }).show();
    }

//	private void isLoginAlert() {
//		new AlertDialog.Builder(this).setMessage("你还没有绑定网上办事账号，是否现在绑定？").setTitle(getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_notify"))).setCancelable(true).setPositiveButton("手动绑定", new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//				if (gloabDelegete != null) {
//					gloabDelegete.doBoundMSTAccount(WDBJ.this, username, password, new TransportCallBack() {
//
//						@Override
//						public void onStart() {
//							// myHandler.sendEmptyMessage(SHOW_DIALOG);
//						}
//
//						@Override
//						public void onResult(int status) {
//							// myHandler.sendEmptyMessage(CLOSE_DIALOG);
//							if (status == 0) {
//								Message msg = Message.obtain();
//								msg.obj = "绑定成功！";
//								msg.what = SHOW_TOAST;
//								DialogUtil.showUIToast(WDBJ.this, "绑定成功");
//								try {
//									Background.Process(WDBJ.this, Login, "正在登录...");
//								} catch (Exception e) {
//									e.printStackTrace();
//								}
//								finish();
//							} else {
//								Message msg = Message.obtain();
//								msg.obj = "绑定失败！";
//								msg.what = SHOW_TOAST;
//								DialogUtil.showUIToast(WDBJ.this, "绑定失败");
//								// myHandler.sendMessage(msg);
//							}
//						}
//
//						@Override
//						public void onFinish() {
//							// myHandler.sendEmptyMessage(CLOSE_DIALOG);
//						}
//					});
//				}
//			}
//		}).setNegativeButton(BtnTxt, new DialogInterface.OnClickListener() {
//			public void onClick(DialogInterface dialog, int id) {
//
//				if (BtnTxt.equals(Constants.AUTO_LOGIN)) {
//					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//					if (gloabDelegete != null) {
//
//						TransportEntity transportEntity = gloabDelegete.get();
//						username = transportEntity.getBatUserName();
//						REALNAME = transportEntity.getRealName();
//						CERTIFICATETYPE = transportEntity.getIdCardType() + "";
//						USER_PID = transportEntity.getIdCardNo();
//						USER_MOBILE = transportEntity.getMobile();
//						password = USER_PID.substring(USER_PID.length() - 6, USER_PID.length());
//						isAutoRegisterAlert();
//					}
//
//				} else if (BtnTxt.equals(Constants.ADD_INFO)) {
//
//					GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
//					gloabDelegete.addInfo(WDBJ.this, new AddInfoCallBack() {
//
//						@Override
//						public void success(TransportEntity t) {
//							username = t.getBatUserName();
//							REALNAME = t.getRealName();
//							CERTIFICATETYPE = t.getIdCardType() + "";
//							USER_PID = t.getIdCardNo();
//							USER_MOBILE = t.getMobile();
//							password = USER_PID.substring(USER_PID.length() - 6, USER_PID.length());
//							isAutoRegisterAlert();
//
//						}
//					});
//					return;
//				}
//			}
//		}).show();
//	}

    static class MyHandler extends Handler {
        private WeakReference<WDBJ> logins;

        public MyHandler(WDBJ login) {
            logins = new WeakReference<WDBJ>(login);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            WDBJ login = logins.get();
            if (login == null) {
                return;
            }
            switch (msg.what) {
                case SHOW_DIALOG:
                    if (login.progressDialog != null) {
                        login.progressDialog.show();
                    }
                    break;
                case CLOSE_DIALOG:
                    if (login.progressDialog != null) {
                        login.progressDialog.dismiss();
                    }
                    break;
                case SHOW_TOAST:
                    String showMsg = (String) msg.obj;
                    DialogUtil.showMyToast(login, showMsg);
                    break;

                default:
                    break;
            }
        }
    }

    final Runnable Login = new Runnable() {
        @Override
        public void run() {
            try {

                JSONObject param = new JSONObject();
                param.put("USERNAME", username);
                param.put("PASSWORD", Md5PwdEncoder.encodePassword(password));
                String response = HTTP.excute("login", "RestUserService", param.toString());
                final JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
                    dialog = Background.Process(WDBJ.this, GetWDBJ, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_loding")));
                    intent = new Intent();

                } else {
                    String error = json.getString("error");
                    DialogUtil.showUIToast(WDBJ.this, error);
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(WDBJ.this, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };
    final Runnable GetWDBJ = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("APPLICANTID", Constants.user.getUSER_ID());
                param.put("PAGENO", PAGENO + "");
                param.put("PAGESIZE", "5");

                String response = HTTP.excuteAndCache(methed, "RestOnlineDeclareService", param.toString(), WDBJ.this);
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    final List<BanJian> temp = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<BanJian>>() {
                    }.getType());
                    runOnUiThread(new Runnable() {
                        public void run() {
                            if (temp.size() < 5) {
                                xListView.setPullLoadEnable(false);
                            }
                            banjians.addAll(temp);
                            banjianAdapter.notifyDataSetChanged();
                        }
                    });

                } else {
                    DialogUtil.showUIToast(WDBJ.this, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_occurs_error_network")));
                    finish();
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(WDBJ.this, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();

            }
        }
    };

    @Override
    public void onBackPressed() {
        this.onDestroy();
        System.gc();
        super.onBackPressed();
    }

    public class MyViewPagerAdapter extends PagerAdapter {
        private List<View> mListViews;

        public MyViewPagerAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mListViews.get(position));

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mListViews.get(position), 0);
            return mListViews.get(position);
        }

        @Override
        public int getCount() {
            return mListViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }

    final Runnable GetPermision = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("BSNUM", BSNUM);
                param.put("PERMID", PERMID);
                String response = HTTP.excuteAndCache("getPermissionByPermid", "RestPermissionitemService", param.toString(), WDBJ.this);
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (("200".equals(code))) {
                    Permission permission = JSONUtil.getGson().fromJson(json.getJSONObject("ReturnValue").getString("PERMISSION"), Permission.class);

//					Permission permission = new Permission(business.getPERMID(), business.getLARGEITEMID(), business.getSMALLITEMID(), business.getSMALLITEMNAME(), business.getITEMVERSION(), business.getITEMLIMITTIME(), business.getITEMLIMITUNIT(), business.getREGIONID(), business.getDEPTCODE(), business.getDEPTNAME(), business.getLAWADDR(), business.getREALADDR(), null, null,
//							"", "", "", "", "", "", "", "","","","","","","","","","");
                    /*
                     * intent = new Intent(); intent.setClass(WDBJ.this,
					 * WorkSpace.class); intent.putExtra("permission",
					 * permission); intent.putExtra("business", business);
					 * intent.putExtra("BSNUM", BSNUM);
					 * intent.putExtra("STATUS",
					 * Integer.parseInt(business.getSTATUS()));
					 * startActivity(intent);
					 */
                    permission.setUSERTYPE(USERTYPE);
                    if (null != permission && TextUtils.equals("1", permission.getSFYDSB())) {
                        intent = new Intent();
                        intent.putExtra("permission", permission);
                        intent.putExtra("mark", "4");
                        intent.putExtra("BSNUM", BSNUM);
                        intent.putExtra("STATUS", STATUS);
                        intent.setClass(WDBJ.this, HistoreShare_v2.class);
                        startActivityForResult(intent, 50);
                    } else {
                        DialogUtil.showUIToast(WDBJ.this, "该事项已失效！");
                    }

                } else {
                    DialogUtil.showUIToast(WDBJ.this, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_occurs_error_network")));
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(WDBJ.this, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };


    class MyItemListener implements OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

            BSNUM = banjians.get(position - 1).getBSNUM();
            PERMID = banjians.get(position - 1).getPERMID();
            STATUS = Integer.parseInt(banjians.get(position - 1).getSTATUS());
            dialog = Background.Process(WDBJ.this, getPermExtInfo, "数据加载中...");
        }

    }
    final Runnable getPermExtInfo = new Runnable() {
        @Override
        public void run() {

            try {
                GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                final TransportEntity userInfo = gloabDelegete.getUserInfo();
                JSONObject param = new JSONObject();
                param.put("PERMID", PERMID);
                param.put("USERID", userInfo.getUserId());
                String response = HTTP.excute("getPermExtInfo", "RestPermissionitemService", param.toString());
                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");
                if (code == 200) {
                    //"USERTYPE": "2", //支持申报的用户类型 0：全部，1：个人，2：企业
                    // "AUTHLEVEL": “1” //所需用户的最低等级


                    //                    // TODO: 2018/1/10 可以申请
                    //
                    UserPermBean permBean = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), UserPermBean.class);
                    USERTYPE = permBean.getUSERTYPE();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dialog = Background.Process(WDBJ.this, GetPermision, "数据加载中...");

                        }
                    });


                } else {
                    DialogUtil.showUIToast(mContext,
                                           "获取事项扩展信息失败");
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(mContext,
                                       e.toString() /*+ "11111111111111111111"*/);
                e.printStackTrace();

            }
        }
    };

    class BanJianAdapter extends BaseAdapter {
        private List<BanJian> banJians;

        public BanJianAdapter(List<BanJian> banJians) {
            super();
            this.banJians = banJians;
        }

        @Override
        public int getCount() {
            return banJians.size();
        }

        @Override
        public Object getItem(int position) {
            return banJians.get(position);
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
                convertView = inflater.inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_banjian_list_item"), parent, false);
                item.BSNUM = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "BSNUM"));
                item.TIME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "TIME"));
                item.APPLICANT = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "APPLICANT"));
                item.COMPANY = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "COMPANY"));
                item.PNAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "PNAME"));
                item.DEPTNAME = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "DEPTNAME"));
                item.RESULT = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "RESULT"));
                item.opinion = (Button) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "opinion"));// 获取意见
                item.tv_isfzbd = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "tv_isfzbd"));// 获取意见
                item.iv_toscan = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "iv_toscan"));// 获取意见
                item.opinion.setFocusable(false);
                item.opinion.setFocusableInTouchMode(false);
                convertView.setTag(item);
            } else {
                item = (Item) convertView.getTag();
            }

            final BanJian banJian = banJians.get(position);
            item.BSNUM.setText(banJian.getBSNUM());
            item.TIME.setText(banJian.getCREATETIME());
            item.APPLICANT.setText(null == banJian.getAPPLICANT() ? "无" : banJian.getAPPLICANT());
            item.COMPANY.setText(null == banJian.getCOMPANY() ? "无" : banJian.getCOMPANY());
            item.PNAME.setText(banJian.getPNAME());
            item.DEPTNAME.setText(banJian.getDEPTNAME());
            item.RESULT.setText(banJian.getCSTATUS());
            if (TextUtils.equals(banJian.getSFFZBD(), "0")) {
                item.tv_isfzbd.setText("否");
            } else if (TextUtils.equals(banJian.getSFFZBD(), "1")) {
                item.tv_isfzbd.setText("是");
            }
            if (TextUtils.equals("暂存", banJian.getCSTATUS()) && TextUtils.equals(banJian.getSFFZBD(), "1")) {
                item.iv_toscan.setVisibility(View.VISIBLE);
                item.iv_toscan.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(WDBJ.this).setMessage("该事项包含复杂表单!请先前往电脑端打开（http://bsdt.baoan.gov.cn/appscan），然后扫描二维码继续填报相关信息").setTitle("提示")
                                .setCancelable(false).setPositiveButton("去扫描", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                intent = new Intent();
                                intent.putExtra("flag", 3);
                                intent.putExtra("SXID", banJian.getPERMID());
                                intent.putExtra("BSNUM", banJian.getBSNUM());
                                intent.setClass(WDBJ.this, CaptureActivity.class);
                                startActivityForResult(intent, 100);// 二维码扫描;
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        }).show();
                    }
                });


            } else {
                item.iv_toscan.setVisibility(View.GONE);
            }


            return convertView;
        }

        public final class Item {
            public TextView BSNUM;
            public TextView TIME;
            public TextView APPLICANT;
            public TextView COMPANY;
            public TextView PNAME;
            public TextView DEPTNAME;
            public TextView RESULT;
            public Button opinion;
            public TextView tv_isfzbd;
            public ImageView iv_toscan;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 50 && resultCode == 100) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        PAGENO = 1;
        banjians.clear();
        dialog = Background.Process(this, GetWDBJ, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_loding")));
        xListView.stopRefresh();
        xListView.setPullLoadEnable(true);

    }

    @Override
    public void onLoadMore() {
        PAGENO++;
        dialog = Background.Process(this, GetWDBJ, getString(MSFWResource.getResourseIdByName(WDBJ.this, "string", "tj_loding")));
        xListView.stopLoadMore();

    }

}
