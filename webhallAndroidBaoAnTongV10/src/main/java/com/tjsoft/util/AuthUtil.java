package com.tjsoft.util;

import android.app.Activity;
import android.content.Intent;

import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.widget.ConfirmCertDialog;

/**
 * Created by long on 2018/4/11.
 */

public class AuthUtil {
    public static int HUAXUN_AUTH = 100;
    public static int HUAXUN_AUTH_FAREN = 200;
    public static int HUAXUN_AUTH_SUCCESS = 200;

    /*
    * 调用华讯身份认证
    * */
    public static void startHuaXunFaceAuth(final Activity activity, final boolean isShow) {
        //跳转到认证界面
        ConfirmCertDialog confirmCertDialog = new ConfirmCertDialog(activity, Constants.user.getREALNAME(), Constants.user.getCODE(), new ConfirmCertDialog.Confirm() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent("com.mst.action.FACE_CERT");
                intent.putExtra("name", Constants.user.getREALNAME());
                intent.putExtra("idCardNum", Constants.user.getCODE());
                intent.putExtra("isShow", isShow);
                intent.putExtra("from", 2);
                activity.startActivityForResult(intent, HUAXUN_AUTH);

            }
        });

    }

    /*
  * 调用华讯身份认证
  * */
    public static void startHuaXunFaceAuth(final Activity activity, final String name, final String idCardNum,boolean isShow) {
        //跳转到认证界面
        Intent intent = new Intent("com.mst.action.FACE_CERT");
        intent.putExtra("name", name);
        intent.putExtra("idCardNum", idCardNum);
        intent.putExtra("isShow", isShow);
        if (isShow) {
            intent.putExtra("from", 2);

        }
        activity.startActivityForResult(intent, HUAXUN_AUTH);


    }

    /*
  * 调用华讯身份认证
  * */
    public static void startHuaXunFaceAuthFaren(final Activity activity, final String name, final String idCardNum) {
        //跳转到认证界面
        Intent intent = new Intent("com.mst.action.FACE_CERT");
        intent.putExtra("name", name);
        intent.putExtra("idCardNum", idCardNum);
        intent.putExtra("isShow", false);
        intent.putExtra("from", 1);

        activity.startActivityForResult(intent, HUAXUN_AUTH_FAREN);


    } /*
  * 调用华讯身份认证
  * */

    public static void startHuaXunFaceAuthFaren(final Activity activity) {
        //跳转到认证界面
        //跳转到认证界面
        ConfirmCertDialog confirmCertDialog = new ConfirmCertDialog(activity, Constants.user.getREALNAME(), Constants.user.getCODE(), new ConfirmCertDialog.Confirm() {
            @Override
            public void onConfirm() {
                Intent intent = new Intent("com.mst.action.FACE_CERT");
                intent.putExtra("name", Constants.user.getREALNAME());
                intent.putExtra("idCardNum", Constants.user.getCODE());
                intent.putExtra("isShow", false);
                intent.putExtra("from", 1);

                activity.startActivityForResult(intent, HUAXUN_AUTH);

            }
        });


    }

}
