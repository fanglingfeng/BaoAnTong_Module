package com.tjsoft.webhall.ui.wsbs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.LoginUtil;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.Permission;
import com.tjsoft.webhall.entity.TransportEntity;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.user.Login;
import com.tjsoft.webhall.ui.work.PermGuideContainer;
import com.tjsoft.webhall.util.SharedPreferenceUtil;

import java.util.List;

public class PermAdapter extends BaseAdapter {
    private int selectorPosition = -1;
    private List<Permission> listItems;
    private Context mContext;
    private Intent intent;
    private SharedPreferences sp;//记录版本号的

    public PermAdapter(Context mContext, List<Permission> listItems) {
        this.listItems = listItems;
        this.mContext = mContext;
        sp = mContext.getSharedPreferences("Version", Context.MODE_PRIVATE);
        // TODO Auto-generated constructor stub
    }

    public void addData(List<Permission> listItems) {
        if (listItems != null) {
            this.listItems = listItems;
            this.notifyDataSetChanged();
        }
    }

    public void clearAll() {
        this.listItems.clear();
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public Object getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setSelsetorItem(int i) {
        this.selectorPosition = i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        PermItem item;
        if (null == convertView) {
            item = new PermItem();
            convertView = LayoutInflater.from(mContext)
                    .inflate(MSFWResource.getResourseIdByName(mContext, "layout", "tj_permlist_item"), parent, false);
            item.name = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "name"));
            item.permCode = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "permCode"));
            item.deptName = (TextView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "deptName"));
            item.item_bg = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "item_bg"));
            item.isApply = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "isApply"));
            item.isOrder = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "isOrder"));
            item.parent_ll = (LinearLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "more_parent_ll"));
            item.bszn = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "bszn"));
            item.wysb = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "wysb"));
            item.zxkf = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "zxkf"));
            item.rxdh = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "rxdh"));
            item.wyyy = (RelativeLayout) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "wyyy"));
            item.icon_arrow = (ImageView) convertView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "icon_arrow"));
            convertView.setTag(item);
        } else {
            item = (PermItem) convertView.getTag();
        }


        final Permission permission = listItems.get(position);
        item.name.setText(permission.getSXZXNAME());
        item.deptName.setText(permission.getDEPTNAME());
//		item.permCode.setText("事项编码："+permission.getLARGEITEMID()+"-"+permission.getSMALLITEMID());
        if (!TextUtils.isEmpty(permission.getCODE4()) && !permission.getCODE4().equals("null")) {
            item.permCode.setText("事项编码：" + permission.getCODE3() + "(" + permission.getCODE4() + ")");
        } else {
            if (!TextUtils.isEmpty(permission.getCODE3()) && !permission.getCODE3().equals("null")) {
                item.permCode.setText("事项编码：" + permission.getCODE3());
            } else {
                item.permCode.setText("");
            }
        }
        if (null != permission && null != permission.getSFYDSB() && permission.getSFYDSB().equals("1")) {// 支持手机申报
            item.isApply.setVisibility(View.VISIBLE);
            item.wysb.setVisibility(View.VISIBLE);
        } else {
            item.isApply.setVisibility(View.GONE);
            item.wysb.setVisibility(View.GONE);
        }
        if (null != permission && null != permission.getISRESERVE() && permission.getISRESERVE().equals("1")) {// 支持预约
            item.isOrder.setVisibility(View.VISIBLE);
            item.wyyy.setVisibility(View.VISIBLE);
        } else {
            item.isOrder.setVisibility(View.GONE);
            item.wyyy.setVisibility(View.GONE);
        }

        if (position == selectorPosition) {
            if (item.parent_ll.isSelected()) {
                item.parent_ll.setSelected(false);
                item.parent_ll.setVisibility(View.GONE);
                item.icon_arrow.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_arrows"));
            } else {
                item.parent_ll.setSelected(true);
                item.parent_ll.setVisibility(View.VISIBLE);
                item.icon_arrow.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_arrows_open"));

            }
        } else {
            item.parent_ll.setSelected(false);
            item.icon_arrow.setBackgroundResource(MSFWResource.getResourseIdByName(mContext, "drawable", "tj_arrows"));
            item.parent_ll.setVisibility(View.GONE);
        }
        item.bszn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                intent = new Intent();
                intent.setClass(mContext, PermGuideContainer.class);
                intent.putExtra("PERMID", permission.getID());
                intent.putExtra("permission", permission);
                intent.putExtra("WSBSFLAG", 3);
                mContext.startActivity(intent);
            }
        });
        item.wysb.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//
                if (null != permission && null != permission.getSFYDSB() && permission.getSFYDSB().equals("1")) {// 支持手机申报

//
                    if (TextUtils.isEmpty(permission.getWSSBDZ())) {
                        //保存Permission
                        SharedPreferenceUtil.save(mContext,"file_key","value_key",permission );

                        System.out.println("fuchl  保存permission"+permission.toString());


                        nextActivity(1, permission);


                    } else {
                        new AlertDialog.Builder(mContext)
                                .setTitle("温馨提示")
                                .setMessage("该事项由市级平台提供，申报地址：" + permission.getWSSBDZ())
                                .setPositiveButton("前往申报", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        intent = new Intent();
                                        intent.setClass(mContext, WSBSWeb.class);
                                        intent.putExtra("url", permission.getWSSBDZ());
                                        mContext.startActivity(intent);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).show();

//
                    }
                } else {
                    //不可以申报
                    DialogUtil.showUIToast(mContext, "该事项不可申报");
                }

            }
        });
        item.zxkf.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
                if (gloabDelegete != null) {
                    gloabDelegete.contactCustomer((Activity) mContext, "事项列表", permission.getSXZXNAME(), permission.getDEPTNAME());
                }
            }
        });
        item.rxdh.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new AlertDialog.Builder(mContext).setMessage("是否要拨打联系电话：0755-85908590").setTitle(mContext.getString(MSFWResource.getResourseIdByName(mContext, "string", "tj_notify"))).setCancelable(false).setPositiveButton("拨打", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:0755-85908590"));
                        mContext.startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                }).show();
            }
        });
        item.wyyy.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                nextActivity(2, permission);
            }
        });
        return convertView;
    }

    public final class PermItem {
        public TextView name;
        public TextView permCode;
        public TextView deptName;
        public RelativeLayout item_bg;
        public ImageView isApply;
        public ImageView isOrder;
        public ImageView icon_arrow;
        public LinearLayout parent_ll;
        public RelativeLayout bszn;
        public RelativeLayout wysb;
        public RelativeLayout zxkf;
        public RelativeLayout rxdh;
        public RelativeLayout wyyy;

    }

    /**
     * 判断实名信息是否完全
     *
     * @param t
     * @return
     */
    private boolean checkRealNameInfo(TransportEntity t) {
        System.out.println("fuchl  token   "+t.getToken());

        if (TextUtils.isEmpty(t.getToken())) {
            return false;
        }
        return true;
    }

    public void nextActivity(int flag, final Permission permission) {
        // TODO Auto-generated method stub
        if (!Constants.DEBUG_TOGGLE) {
            /**
             * 判断是否登录MST
             */
            GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
            System.out.println("fuchl  gloabDelegete   "+gloabDelegete.toString());

            Log.e("Login", "gloabDelegete===" + gloabDelegete);
            if (gloabDelegete != null) {
                TransportEntity transportEntity = gloabDelegete.getUserInfo();

                if (checkRealNameInfo(transportEntity)) {// 信息完整
                    // 登录自己的接口
                    LoginUtil loginUtil = new LoginUtil(mContext, sp, permission);
                    loginUtil.Login(flag, transportEntity);
                    // Constants.user.setUSER_ID(transportEntity.getUserId());
                } else {
                    new AlertDialog.Builder(mContext).setMessage("你还没有登录，是否现在登录")
                            .setTitle(mContext.getString(MSFWResource
                                    .getResourseIdByName(mContext, "string", "tj_notify")))
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    if (gloabDelegete != null) {
                                        gloabDelegete.doActivity(mContext, 1, null);
                                    }
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            dialog.dismiss();
                        }

                    }).show();
                    return;
                }
            } else {
                DialogUtil.showUIToast(mContext, "gloabDelegete is null");
                return;
            }
        } else {
            if (null == Constants.user) {
                Intent intent = new Intent();
                intent.setClass(mContext, Login.class);
                mContext.startActivity(intent);
                return;
            } else {


                Intent intent = new Intent();
                intent.setClass(mContext, AddConsult.class);
                intent.putExtra("permission", permission);
                mContext.startActivity(intent);

            }

        }

    }

}
