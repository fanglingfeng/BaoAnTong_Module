package com.tjsoft.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.entity.ApplyBean;
import com.tjsoft.webhall.imp.GloabDelegete;
import com.tjsoft.webhall.ui.dzzzk.DZZZKActivity;
import com.tjsoft.webhall.ui.work.HistoreShare_v2;
import com.tjsoft.webhall.ui.work.PermGuideContainer;
import com.tjsoft.webhall.ui.work.QYSQ;

import java.util.ArrayList;

public class ShowPopupMoreUtil {

	/**
     * 更多  首页  办事指南  在线客服  热线电话
     * @param view 设置当前页面的父布局ID=root_parent
     */
    public static void showPopupMore(View view, final Context mContext, final String PERMID, final String modelName, final String eventName, final String deptName, final ArrayList<ApplyBean> bigfileDate){
    	final Activity activity=(Activity)mContext;
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(MSFWResource.getResourseIdByName(mContext, "layout", "popup_search_schedule_more"), null);
        LinearLayout home_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_home"));
        LinearLayout bszn_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_bszn"));
        LinearLayout zxkf_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_zxkf"));
        LinearLayout rxdh_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_rxdh"));
        LinearLayout qysq_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_qysq"));
        LinearLayout yzgx_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_yzgx"));

//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels)*0.35); //屏幕宽
        int height = dm.heightPixels; //屏幕高
        final PopupWindow popupWindow = new PopupWindow(contentView,
                width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(MSFWResource.getResourseIdByName(mContext, "style", "popwin_anim_style"));
        WindowManager.LayoutParams params=activity.getWindow().getAttributes();
        params.alpha=0.6f;
        activity.getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params=activity.getWindow().getAttributes();
                params.alpha=1f;
                activity. getWindow().setAttributes(params);
            }
        });
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(MSFWResource.getResourseIdByName(mContext, "color", "tj_transparent"))));

        // 设置好参数之后再show

//        popupWindow.showAsDropDown(view,0,25);
        popupWindow.showAtLocation(activity.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "root_parent")), Gravity.RIGHT | Gravity.TOP, DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 70));

        home_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Constants.getInstance().exit();
            }
        });     
        bszn_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
				Intent intent = new Intent();
				intent.setClass(mContext, PermGuideContainer.class);
				intent.putExtra("PERMID", PERMID);
				intent.putExtra("WSBSFLAG", 3);
				mContext.startActivity(intent);
            }
        });
        zxkf_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
				if(gloabDelegete!=null){
					gloabDelegete.contactCustomer(activity, modelName, eventName, deptName);
				}            }
        });
        rxdh_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
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
        qysq_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setClass(mContext, QYSQ.class);
                mContext.startActivity(intent);


            }
        });
        yzgx_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.putExtra("SXMC",eventName);
                intent.putExtra("SXBM",PERMID);



//                intent.putParcelableArrayListExtra("dzzzklist",
//                                                    bigfileDate);
                //电子证照库
                intent.setClass(mContext, DZZZKActivity.class);
                ((Activity) mContext).startActivityForResult(intent, HistoreShare_v2.REQUESTCODE_DZZZK);
            }
        });
    }
    
	/**
     * 更多  首页    在线客服  热线电话
     * @param view 设置当前页面的父布局ID=root_parent
     */
    public static void PermListshowPopupMore(View view,final Context mContext){
    	final Activity activity=(Activity)mContext;
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(MSFWResource.getResourseIdByName(mContext, "layout", "popup_search_schedule_more"), null);
        LinearLayout home_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_home"));
        LinearLayout bszn_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_bszn"));       
        LinearLayout zxkf_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_zxkf"));
        LinearLayout rxdh_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_rxdh"));
        LinearLayout qysq_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_qysq"));
        LinearLayout yzgx_ll= (LinearLayout) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "popup_more_yzgx"));
        bszn_ll.setVisibility(View.GONE);
        yzgx_ll.setVisibility(View.GONE);
        TextView bsznLine_tv= (TextView) contentView.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "bszn_line"));
        bsznLine_tv.setVisibility(View.GONE);

//        final PopupWindow popupWindow = new PopupWindow(contentView,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = (int) ((dm.widthPixels)*0.35); //屏幕宽
        int height = dm.heightPixels; //屏幕高
        final PopupWindow popupWindow = new PopupWindow(contentView,
                width, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
//        popupWindow.setAnimationStyle(MSFWResource.getResourseIdByName(mContext, "style", "popwin_anim_style"));
        WindowManager.LayoutParams params=activity.getWindow().getAttributes();
        params.alpha=0.6f;
        activity.getWindow().setAttributes(params);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params=activity.getWindow().getAttributes();
                params.alpha=1f;
                activity. getWindow().setAttributes(params);
            }
        });
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable(mContext.getResources().getColor(MSFWResource.getResourseIdByName(mContext, "color", "tj_transparent"))));

        // 设置好参数之后再show

//        popupWindow.showAsDropDown(view,0,25);
        popupWindow.showAtLocation(activity.findViewById(MSFWResource.getResourseIdByName(mContext, "id", "root_parent")), Gravity.RIGHT | Gravity.TOP, DensityUtil.dip2px(mContext, 5), DensityUtil.dip2px(mContext, 70));

        home_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Constants.getInstance().exit();
            }
        });     
        zxkf_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
				GloabDelegete gloabDelegete = Constants.getInstance().getGloabDelegete();
				if(gloabDelegete!=null){
					gloabDelegete.contactCustomer((Activity)mContext, "事项列表", "", "");
				}
            }
        });
        rxdh_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
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

        qysq_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                private void initAccessTokenWithAkSk() {
//                    OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
//                        @Override
//                        public void onResult(AccessToken result) {
//                            String token = result.getAccessToken();
//                            hasGotToken = true;
//                        }
//
//                        @Override
//                        public void onError(OCRError error) {
//                            error.printStackTrace();
//                            alertText("AK，SK方式获取token失败", error.getMessage());
//                        }
//                    }, getApplicationContext(), "VpnslHxxBrvtiYiLqdzbX3Un", "zsEH88wLLitjWmts0tWSPWotpNXXRkik");
//                }
                popupWindow.dismiss();
                Intent intent = new Intent();
                intent.setClass(mContext, QYSQ.class);
                mContext.startActivity(intent);


            }
        });
    }
}
