package com.tjsoft.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tjsoft.webhall.AppConfig;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.db.PermListByDB;
import com.tjsoft.webhall.entity.AUTH_MSGparam;
import com.tjsoft.webhall.entity.ApplyParam;
import com.tjsoft.webhall.entity.FaceCKParam;
import com.tjsoft.webhall.entity.PermGroup;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.SaveParam;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.entity.User;
import com.tjsoft.webhall.entity.UserPermBean;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.bsdt.ReserveList;
import com.tjsoft.webhall.ui.bsdt.ReserveSubmit;
import com.tjsoft.webhall.ui.bsdt.WDBJ;
import com.tjsoft.webhall.ui.work.HistoreShareNoPre;
import com.tjsoft.webhall.ui.wsbs.AddConsult;

import org.json.JSONObject;

import java.util.Date;
import java.util.List;

public class LoginUtil {
    private Context mContext;
    private SharedPreferences sp;//记录版本号的
    private int version;
    private Permission permission;
    private Intent intent;
    private String P_GROUP_ID;// 分组id
    private String P_GROUP_NAME;// 分组名称
    private List<PermGroup> groups;
    private String BSNUM = "";
    private String usertype;
    private String authlevel;
    ProgressDialog progressdialog2;
    AlertDialog dialog;

    public LoginUtil(Context context, SharedPreferences sp, Permission permission) {
        this.mContext = context;
        this.sp = sp;
        this.permission = permission;
        BSNUM = permission.getBSNUM();
        version = sp.getInt("version", 0);
    }

    public LoginUtil(Context context, SharedPreferences sp) {
        this.mContext = context;
        this.sp = sp;
        version = sp.getInt("version", 0);
    }

    /**
     * 万能密码登录
     *
     * @param flag 1   申报页   2  我要预约界面  3 在线咨询 4 我的办件  5 我的收藏
     */
    public void Login(final int flag, final TransportEntity transportEntity) {

        ProgressDialog progressdialog = DialogUtil.creativeProgressBar(mContext, "加载中");
        progressdialog.setCancelable(true);
        progressdialog.setCanceledOnTouchOutside(false);

        progressdialog2 = progressdialog;
        progressdialog2.show();

        final Runnable LoginNotPassW = new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject param = new JSONObject();
                    if (!TextUtils.isEmpty(transportEntity.getName())) {
                        param.put("USERNAME", transportEntity.getName());
                    } else {
                        showDialog();
                        return;
                    }
//                    param.put("PASSWORD", "afdd0b4ad2ec172c586e2150770fbf9e");
                    param.put("PASSWORD", "C4BD1B3942F3C2ACD7657CBD0B5D952F");
                    String response = HTTP.excute("login", "RestUserService", param.toString());
                    final JSONObject json = new JSONObject(response);
                    String code = json.getString("code");

                    if (code.equals("200")) {
                        Constants.user = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), User.class);
//                        DialogUtil.showUIToast(mContext, /*"00000000" +*/ "登陆网厅成功");
                        int ver = transportEntity.getVersion();
//                        if (version < ver) {
                        updateUserInfo(transportEntity);
//                        }
                        switch (flag) {
                            case 1://网上申报
                                //判断用户类型是否正确
                                new Thread(getPermExtInfo).start();

//							Background.Process(mContext, getPermExtInfo, "加载中");// 获取分组ID
                                break;

                            case 2://我要预约
                                if (progressdialog2 != null) {
                                    progressdialog2.dismiss();

                                }
                                intent = new Intent();
                                intent.setClass(mContext, ReserveSubmit.class);
                                intent.putExtra("permission", permission);
                                mContext.startActivity(intent);
                                break;
                            case 3://在线咨询
                                if (progressdialog2 != null) {
                                    progressdialog2.dismiss();

                                }
                                Intent intent = new Intent();
                                intent.setClass(mContext, AddConsult.class);
                                intent.putExtra("permission", permission);
                                mContext.startActivity(intent);
                                break;
                            case 4://我的办件
                                if (progressdialog2 != null) {
                                    progressdialog2.dismiss();

                                }
                                mContext.startActivity(new Intent().setClass(mContext, WDBJ.class));
                                break;
                            case 5://我的收藏
                                if (progressdialog2 != null) {
                                    progressdialog2.dismiss();

                                }
                                mContext.startActivity(new Intent().setClass(mContext, PermListByDB.class));
                                ;
                                break;
                            case 6://我的预约
                                if (progressdialog2 != null) {
                                    progressdialog2.dismiss();

                                }
                                mContext.startActivity(new Intent().setClass(mContext, ReserveList.class));
                                break;
                            default:
                                break;
                        }
                    } else if (code.equals("600")) {
                        if (progressdialog2 != null) {
                            progressdialog2.dismiss();

                        }
                        if (transportEntity.getEnterpriseStatus().equals("3")) {
                            QY_Register(flag, transportEntity);
                        } else {
                            GR_Register(flag, transportEntity);
                        }
                    } else {
                        if (progressdialog2 != null) {
                            progressdialog2.dismiss();

                        }
                        String error = json.getString("error");
                        DialogUtil.showUIToast(mContext, error);
                    }

                } catch (Exception e) {
                    if (progressdialog2 != null) {
                        progressdialog2.dismiss();

                    }
//                    DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
//                    DialogUtil.showUIToast(mContext, /*"00000000" +*/ "登陆网厅失败");

                    e.printStackTrace();

                }
            }
        };

        new Thread(LoginNotPassW).start();
//		dialog=Background.Process(PermGuideContainer.this, LoginNotPassW,PermGuideContainer.this.getString(MSFWResource.getResourseIdByName(PermGuideContainer.this, "string", "tj_loding")));
    }

    final Runnable GetInfoByUserid = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("token", Constants.user.getTOKEN());
                param.put("ID", Constants.user.getUSER_ID());
                String response = HTTP.excuteAndCache("getInfoByUserid", "RestUserService", param.toString(),
                        mContext);
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    final String userDetail = json.getString("ReturnValue");
                    JSONObject userDetailJson = new JSONObject(userDetail);
                    final String TYPE = userDetailJson.getString("TYPE");//个人的type
                    final String AUTHLEVEL = userDetailJson.getString("AUTHLEVEL");//个人的lever

                    if (TextUtils.equals(TYPE, "1")||TextUtils.equals(usertype, "0")) {
                        new Thread(GetGroup).start();
                        GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                        final TransportEntity userInfo = gloabDelegete.getUserInfo();
                        if (userInfo.isRealUserAuth()) {
                            new Thread(saveUserAuthenticationMsg).start();//同步
                        }

//
                    } else if (TextUtils.equals(TYPE, "2") && TextUtils.equals(usertype, "1")) {//个人用户
                        Activity activity = (Activity) mContext;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(mContext)
                                        .setTitle("温馨提示")
                                        .setMessage("该事项只能由企业D用户进行申报!")
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        });
                    }

                } else {
                    DialogUtil.showUIToast(mContext, "加载申报信息失败！");
                }

            } catch (Exception e) {
                DialogUtil.showUIToast(mContext, e.toString());
                e.printStackTrace();
            }
        }
    };
    final Runnable checkSxry = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                final TransportEntity userInfo = gloabDelegete.getUserInfo();
//                if (TextUtils.equals(Constants.user.getTYPE(), "1")) {

                if (userInfo.getRealName() != null) {
                    param.put("USERNAME", userInfo.getRealName());

                }
                if (userInfo.getIdcardNum() != null) {

                    param.put("PID", userInfo.getIdcardNum());
                }
//                } else {
                if (userInfo.getINC_NAME() != null) {

                    param.put("QYMC", userInfo.getINC_NAME());
                }
                if (userInfo.getTYSHXYDM() != null) {

                    param.put("TYXYDM", userInfo.getTYSHXYDM());
                }
//                }
                param.put("USERNAME", userInfo.getRealName());
                param.put("PID", userInfo.getIdcardNum());
                param.put("SXID", permission.getID());
                param.put("SXNAME", permission.getSXZXNAME());
                param.put("DEPTID", permission.getDEPTID());
                param.put("DEPETNAME", permission.getDEPTNAME());
                param.put("STATE", "5");
                param.put("AREANAME", Constants.AREANAME);

                String response = HTTP.excuteAndCache("checkSxry", "RestSxyzService", param.toString(),
                        mContext);
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (progressdialog2 != null) {
                    progressdialog2.dismiss();

                }
                if (code.equals("200")) {
                    final String userDetail = json.getString("ReturnValue");

//                    DialogUtil.showUIToast(mContext, "加载信用信息失败！");

                    if (TextUtils.equals(userDetail, "0")) {

                        new Thread(GetInfoByUserid).start();
//
                    } else if (TextUtils.equals(userDetail, "1")) {//失信人员
                        Activity activity = (Activity) mContext;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (dialog == null) {
                                    dialog = new AlertDialog.Builder(mContext)
                                            .setTitle("温馨提示")
                                            .setMessage("经核查，您已被纳入失信被执行人名单，根据《宝安区关于对失信被执行人实施联合惩戒的合作备忘录》，您不能申报【" + permission.getSXZXNAME() + "】事项， 如有疑问，请联系宝安区人民法院执行局，地址：宝安区宝城29区宝民一路一巷，联系电话：0755-29997791。\n")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).create();


                                    dialog.show();
                                }

                            }
                        });
                    }
//
                } else {
                    if (progressdialog2 != null) {
                        progressdialog2.dismiss();

                    }
                    DialogUtil.showUIToast(mContext, "加载信用信息失败！");
                }

            } catch (Exception e) {
                if (progressdialog2 != null) {
                    progressdialog2.dismiss();

                }
                DialogUtil.showUIToast(mContext, "加载信用信息失败！" + e.toString());
                e.printStackTrace();
            }
        }
    };

    private void GR_Register(final int flag, final TransportEntity transportEntity) {
        final Runnable Submit1 = new Runnable() {
            @Override
            public void run() {
                try {
                    Log.d("PermGuide", "身份类型===" + transportEntity.getIdcardType());
                    String TypeId = "";
                    JSONObject param = new JSONObject();
                    param.put("USERNAME", transportEntity.getName());
                    param.put("PASSWORD", transportEntity.getPassword());
                    if ((!TextUtils.isEmpty(transportEntity.getIdcardType()) && !transportEntity.getIdcardType().equals("null")) &&
                            (!TextUtils.isEmpty(transportEntity.getRealName()) && !transportEntity.getRealName().equals("null") &&
                                    (!TextUtils.isEmpty(transportEntity.getIdcardNum()) && !transportEntity.getIdcardNum().equals("null")) &&
                                    (!TextUtils.isEmpty(transportEntity.getLoginPhone()) && !transportEntity.getLoginPhone().equals("null"))
                            )) {
                        if (transportEntity.getIdcardType().equals("1")) {
                            TypeId = "10";
                        } else if (transportEntity.getIdcardType().equals("2")) {
                            TypeId = "20";
                        } else if (transportEntity.getIdcardType().equals("3")) {
                            TypeId = "14";
                        } else if (transportEntity.getIdcardType().equals("4")) {
                            TypeId = "15";
                        }
                        param.put("USER_PID", transportEntity.getIdcardNum());
                        param.put("USER_MOBILE", transportEntity.getLoginPhone());
                        param.put("REALNAME", transportEntity.getRealName());
                        param.put("CERTIFICATETYPE", TypeId);
                        Log.d("PermGuide", "转换后身份类型===" + TypeId);
                    } else {
                        showDialog();
                        return;
                    }
                    param.put("EMAIL", transportEntity.getEMAIL());
                    param.put("REGISTER_TIME", new Date());
                    param.put("USER_GENDER", transportEntity.getSex());
                    param.put("USER_SOURCE", "2");

                    String response = HTTP.excute("registerUser", "RestUserService", param.toString());
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    if (code.equals("200")) {
                        Login(flag, transportEntity);
                        // DialogUtil.showUIToast(Register.this, "注册成功！");
//						FileUtil.Write(WSBSMainActivity.this, "username", transportEntity.getName());
//						FileUtil.Write(WSBSMainActivity.this, "password", transportEntity.getPassword());
//						new Thread(autoLogin).start();// 自动登陆						
                    } else {
                        String error = json.getString("error");
                        Log.d("WSBSMAIN", error);
//						DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource
//								.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        DialogUtil.showUIToast(mContext, error);
                    }

                } catch (Exception e) {
                    DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource
                            .getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
                    e.printStackTrace();

                }
            }
        };
        new Thread(Submit1).start();
    }


    private void QY_Register(final int flag, final TransportEntity transportEntity) {
        final Runnable Submit2 = new Runnable() {
            @Override
            public void run() {
                try {

                    JSONObject param = new JSONObject();
                    if (!TextUtils.isEmpty(transportEntity.getName()) && !transportEntity.getName().equals("null")) {
                        param.put("USERNAME", transportEntity.getName());
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;

                    }
                    if (!TextUtils.isEmpty(transportEntity.getPassword()) && !transportEntity.getPassword().equals("null")) {
                        param.put("PASSWORD", transportEntity.getPassword());
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;

                    }
                    param.put("EMAIL", transportEntity.getEMAIL());
                    if (!TextUtils.isEmpty(transportEntity.getINC_NAME()) && !transportEntity.getINC_NAME().equals("null")) {
                        param.put("INC_NAME", transportEntity.getINC_NAME());
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;

                    }
                    if (!TextUtils.isEmpty(transportEntity.getINC_TYPE()) && !transportEntity.getINC_TYPE().equals("null")) {
                        param.put("INC_TYPE", transportEntity.getINC_TYPE());
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;

                    }
                    if (!TextUtils.isEmpty(transportEntity.getINC_DEPUTY()) && !transportEntity.getINC_DEPUTY().equals("null")) {
                        param.put("INC_DEPUTY", transportEntity.getINC_DEPUTY());
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;

                    }
                    if ((!TextUtils.isEmpty(transportEntity.getINC_ZZJGDM()) && !transportEntity.getINC_ZZJGDM().equals("null"))
                            || (!TextUtils.isEmpty(transportEntity.getTYSHXYDM()) && !transportEntity.getTYSHXYDM().equals("null"))
                            || (!TextUtils.isEmpty(transportEntity.getINC_PERMIT()) && !transportEntity.getINC_PERMIT().equals("null"))) {
                        if (!TextUtils.isEmpty(transportEntity.getINC_ZZJGDM()) && !transportEntity.getINC_ZZJGDM().equals("null")) {
                            param.put("INC_ZZJGDM", transportEntity.getINC_ZZJGDM());
                        } else {
                            param.put("INC_ZZJGDM", "");
                        }
                        if (!TextUtils.isEmpty(transportEntity.getTYSHXYDM()) && !transportEntity.getTYSHXYDM().equals("null")) {
                            param.put("TYSHXYDM", transportEntity.getTYSHXYDM());
                        } else {
                            param.put("TYSHXYDM", "");
                        }
                        if (!TextUtils.isEmpty(transportEntity.getINC_PERMIT()) && !transportEntity.getINC_PERMIT().equals("null")) {
                            param.put("INC_PERMIT", transportEntity.getINC_PERMIT());
                        } else {
                            param.put("INC_PERMIT", "");
                        }
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;
                    }

                    if (!TextUtils.isEmpty(transportEntity.getINC_PID()) && !transportEntity.getINC_PID().equals("null")) {
                        param.put("INC_PID", transportEntity.getINC_PID());

                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;
                    }

                    param.put("INC_ADDR", transportEntity.getINC_ADDR());
                    param.put("INC_INDICIA", transportEntity.getINC_INDICIA());
//					param.put("INC_PHONE", incPHONE);
//					param.put("INC_FAX", incFAX);
//					param.put("INC_NETWORK", incNETWORK);
//					param.put("INC_EMAIL", incEMAIL);
                    if (!TextUtils.isEmpty(transportEntity.getRealName()) && !transportEntity.getRealName().equals("null")) {
                        param.put("AGE_NAME", transportEntity.getRealName());

                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;
                    }
                    if (!TextUtils.isEmpty(transportEntity.getIdcardNum()) && !transportEntity.getIdcardNum().equals("null")) {
                        param.put("AGE_PID", transportEntity.getIdcardNum());
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;
                    }
                    if (!TextUtils.isEmpty(transportEntity.getLoginPhone()) && !transportEntity.getLoginPhone().equals("null")) {
                        param.put("AGE_MOBILE", transportEntity.getLoginPhone());
                    } else {
                        DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        return;
                    }
                    param.put("USER_SOURCE", "2");

                    String response = HTTP.excute("registerInc", "RestUserService", param.toString());
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    if (code.equals("200")) {
                        Login(flag, transportEntity);

                    } else {
                        String error = json.getString("error");
                        Log.d("WSBSMAIN", error);
//						DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_update_user_error")));
                        DialogUtil.showUIToast(mContext, error);
                    }

                } catch (Exception e) {
                    DialogUtil.showUIToast(mContext, mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_occurs_error_network")));
                    e.printStackTrace();

                }
            }
        };
        new Thread(Submit2).start();
    }

    private void updateUserInfo(final TransportEntity transportEntity) {
        final Runnable updateInfo = new Runnable() {
            @Override
            public void run() {
                try {


                    JSONObject param = new JSONObject();

                    param.put("token", Constants.user.getTOKEN());
                    param.put("ID", Constants.user.getUSER_ID());
                    param.put("PSW", transportEntity.getPassword());
//                    param.put("AUTHLEVEL", transportEntity.getAUTHLEVEL());

                    if (transportEntity.getEnterpriseStatus().equals("3")) {
                        param.put("TYPE", "2");
                        param.put("INC_NAME", transportEntity.getINC_NAME());
                        param.put("INC_TYPE", transportEntity.getINC_TYPE());
                        if ((!TextUtils.isEmpty(transportEntity.getINC_ZZJGDM()) && !transportEntity.getINC_ZZJGDM().equals("null"))
                                || (!TextUtils.isEmpty(transportEntity.getTYSHXYDM()) && !transportEntity.getTYSHXYDM().equals("null"))
                                || (!TextUtils.isEmpty(transportEntity.getINC_PERMIT()) && !transportEntity.getINC_PERMIT().equals("null"))) {
                            param.put("INC_ZZJGDM", transportEntity.getINC_ZZJGDM());
                            param.put("TYSHXYDM", transportEntity.getTYSHXYDM());
                            param.put("INC_PERMIT", transportEntity.getINC_PERMIT());
                        } else {
                            Log.e("ddddddd", "不能同时为空");
                            return;
                        }
                        param.put("INC_DEPUTY", transportEntity.getINC_DEPUTY());
                        param.put("INC_PID", transportEntity.getINC_PID());
                        param.put("AGE_NAME", transportEntity.getRealName());
                        param.put("AGE_PID", transportEntity.getIdcardNum());
//						param.put("AGE_EMAIL", ageEMAIL);						
//						param.put("AGE_PHONE", agePHONE);
//						param.put("AGE_INDICIA", ageINDICIA);
//						param.put("AGE_ADDR", ageADDR);
                        param.put("AGE_MOBILE", transportEntity.getLoginPhone());
                    } else {
                        String TypeId = "";
                        if (transportEntity.getIdcardType().equals("1")) {
                            TypeId = "10";
                        } else if (transportEntity.getIdcardType().equals("2")) {
                            TypeId = "20";
                        } else if (transportEntity.getIdcardType().equals("3")) {
                            TypeId = "14";
                        } else if (transportEntity.getIdcardType().equals("4")) {
                            TypeId = "15";
                        }
                        param.put("TYPE", "1");
//						param.put("USER_GENDER", transportEntity.getSex());
                        param.put("USER_NAME", transportEntity.getRealName());
//						param.put("USER_EMAIL", USER_EMAIL);
                        param.put("CERTIFICATETYPE", TypeId);
                        param.put("USER_PID", transportEntity.getIdcardNum());
                        param.put("USER_MOBILE", transportEntity.getLoginPhone());
//						param.put("USER_ADDRESS", transportEntity.get);
                    }

                    String response = HTTP.excute("modifyinfo", "RestUserService", param.toString());
                    JSONObject json = new JSONObject(response);
                    String code = json.getString("code");
                    if (code.equals("200")) {
//                        DialogUtil.showUIToast(mContext, /*"00000000" +*/ "修改成功");
                        Log.e("error", "修改成功");
                        Editor editor = sp.edit();
                        editor.putInt("version", transportEntity.getVersion());
                        editor.commit();
                    } else {
                        String error = json.getString("error");
                        Log.e("error", error);
//                        DialogUtil.showUIToast(mContext, /*"00000000" +*/ "修改失败");

                    }

                } catch (Exception e) {
                    e.printStackTrace();
//                    DialogUtil.showUIToast(mContext, /*"00000000" +*/ "修改失败");

                }
            }
        };
        new Thread(updateInfo).start();
    }

    private void showDialog() {
        Activity activity = (Activity) mContext;
        activity.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                new AlertDialog.Builder(mContext).setMessage("您的用户信息不完善，如需进入下一步操作，请先完善用户信息。").setTitle(mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_notify"))).setCancelable(false).setPositiveButton("去完善", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                        if (gloabDelegete != null) {
                            gloabDelegete.doActivity(mContext, 3, null);
                        } else {
                            DialogUtil.showUIToast(mContext, "gloabDelegete is null");
                        }
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
            }
        });

    }


    /*
     *
     * */
    final Runnable getPermExtInfo = new Runnable() {
        @Override
        public void run() {
//            Looper.prepare();
            try {
                GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                final TransportEntity userInfo = gloabDelegete.getUserInfo();
                JSONObject param = new JSONObject();
                param.put("PERMID", permission.getID());
                param.put("USERID", userInfo.getUserId());
                String response = HTTP.excute("getPermExtInfo", "RestPermissionitemService", param.toString());
                JSONObject json = new JSONObject(response);
                int code = json.getInt("code");
                if (code == 200) {
//"USERTYPE": "2", //支持申报的用户类型 0：全部，1：个人，2：企业
// "AUTHLEVEL": “1” //所需用户的最低等级
                    UserPermBean permBean = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), UserPermBean.class);
                    usertype = permBean.getUSERTYPE();
                    authlevel = permBean.getAUTHLEVEL();
                    permission.setAUTHLEVEL(authlevel);
                    permission.setUSERTYPE(usertype);
//                    checkApplyInfo();
//                    DialogUtil.showUIToast(mContext, /*"00000000" +*/ "获取拓展信息成功");
                    if (TextUtils.equals(permBean.getSFCXYZ(), "1")) {
                        //要验证诚信

                        new Thread(checkSxry).start();


                    } else {
                        if (progressdialog2 != null) {
                            progressdialog2.dismiss();

                        }
                        new Thread(GetInfoByUserid).start();

                    }


//                    // TODO: 2018/1/10 可以申请
//

                } else {
                    if (progressdialog2 != null) {
                        progressdialog2.dismiss();

                    }
                    DialogUtil.showUIToast(mContext,
                            "获取事项扩展信息失败");
                }

            } catch (Exception e) {
                if (progressdialog2 != null) {
                    progressdialog2.dismiss();

                }
                DialogUtil.showUIToast(mContext,
                        "获取事项扩展信息失败" + e.toString());
                e.printStackTrace();

            }
        }
    };

    /*
     * 检测申报条件
     * */
    private void checkApplyInfo() {


    }

    /*
     * 跳转申报
     * */
    final Runnable GetGroup = new Runnable() {
        @Override
        public void run() {
            try {
                JSONObject param = new JSONObject();
                param.put("PERMID", permission.getID());
                String response = HTTP.excuteAndCache("getClxxGroupByPermid", "RestPermissionitemService", param.toString(), mContext);
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    groups = JSONUtil.getGson().fromJson(json.getString("ReturnValue"), new TypeToken<List<PermGroup>>() {
                    }.getType());

                    if (null != groups && groups.size() > 0) {
                        Activity activity = (Activity) mContext;
                        activity.runOnUiThread(new Runnable() {
                            public void run() {
                                String[] groupNames = new String[groups.size()];
                                for (int i = 0; i < groupNames.length; i++) {
                                    groupNames[i] = groups.get(i).getP_GROUP_NAME();
                                }
                                if (groupNames.length <= 1) {
                                    P_GROUP_ID = groups.get(0).getP_GROUP_ID();
                                    P_GROUP_NAME = groups.get(0).getP_GROUP_NAME();
                                    intent = new Intent();
                                    intent.putExtra("permission", permission);
                                    intent.putExtra("STATUS", -1);// 新申报
                                    intent.putExtra("BSNUM", BSNUM);
                                    intent.putExtra("P_GROUP_ID", P_GROUP_ID);
                                    intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
                                    intent.putExtra("mark", "6");
                                    intent.setClass(mContext, HistoreShareNoPre.class);
                                    mContext.startActivity(intent);
                                } else {
                                    new AlertDialog.Builder(mContext).setTitle("请选择材料分组").setIcon(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_ic_dialog_info")).setSingleChoiceItems(groupNames, 0, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int index) {
//
//

                                            P_GROUP_ID = groups.get(index).getP_GROUP_ID();
                                            P_GROUP_NAME = groups.get(index).getP_GROUP_NAME();
                                            intent = new Intent();
                                            intent.putExtra("permission", permission);
                                            intent.putExtra("STATUS", -1);// 新申报
                                            intent.putExtra("BSNUM", BSNUM);
                                            intent.putExtra("P_GROUP_ID", P_GROUP_ID);
                                            intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
                                            intent.putExtra("mark", "6");
                                            intent.setClass(mContext, HistoreShareNoPre.class);
                                            mContext.startActivity(intent);
                                            dialog.dismiss();
//

                                        }
                                    }).show();
                                }
                            }
                        });
                    } else {

                        intent = new Intent();
                        intent.putExtra("permission", permission);
                        intent.putExtra("STATUS", -1);// 新申报
                        intent.putExtra("BSNUM", BSNUM);
                        intent.putExtra("P_GROUP_ID", P_GROUP_ID);
                        intent.putExtra("P_GROUP_NAME", P_GROUP_NAME);
                        intent.putExtra("mark", "6");
                        intent.setClass(mContext, HistoreShareNoPre.class);
                        mContext.startActivity(intent);
                    }

                } else {
                    DialogUtil.showUIToast(mContext, json.getString("error"));
                }

            } catch (Exception e) {
                e.printStackTrace();

            }
        }
    };
    final Runnable saveUserAuthenticationMsg = new Runnable() {
        @Override
        public void run() {
            try {
                Gson mGson = new Gson();
                GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                final TransportEntity userInfo = gloabDelegete.getUserInfo();
                ApplyParam applyparam = new ApplyParam();
                applyparam.setUsername(userInfo.getRealName());
                applyparam.setPid(Constants.user.getCODE());
                applyparam.setSource("2");
                applyparam.setDatetime(net.liang.appbaselibrary.utils.TimeUtils.getCurTimeString());

                FaceCKParam faceCKparam = new FaceCKParam();
                faceCKparam.setCheck("1");
                faceCKparam.setApply(applyparam);


                AUTH_MSGparam aUTH_MSGparam = new AUTH_MSGparam();
                aUTH_MSGparam.setPid(Constants.user.getCODE());
                aUTH_MSGparam.setFaceCK(faceCKparam);


                SaveParam param = new SaveParam();
                param.setToken(Constants.user.getTOKEN());
                param.setPID(Constants.user.getCODE());
                param.setAUTH_TYPE("14");
                param.setUSERTYPE(Constants.user.getTYPE());
                param.setAUTH_MSG(aUTH_MSGparam);


                String response = HTTP.excuteAndCache("saveUserAuthenticationMsg", "RestUserService", mGson.toJson(param), mContext);
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");


                if (code.equals("200")) {
                    //保存认证信息成功，进行跳转
//                    new Thread(GetGroup).start();
//                    DialogUtil.showUIToast(mContext, "保存认证成功");

                } else {
//                    DialogUtil.showUIToast(mContext, json.getString("error"));
                }

            } catch (Exception e) {
                e.printStackTrace();
                DialogUtil.showUIToast(mContext, /*"44444444444444" +*/ e.toString());

            }
        }
    };
}
