/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.tjsoft.camera;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.AuthUtil;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.ui.work.QYSQ;
//import com.webank.wbcloudfaceverify2.tools.WbCloudFaceVerifySdk;
//import com.webank.wbcloudfaceverify2.ui.FaceVerifyStatus;

//import com.webank.wbcloudfaceverify2.tools.WbCloudFaceVerifySdk;
//import com.webank.wbcloudfaceverify2.ui.FaceVerifyStatus;

//import com.webank.wbcloudfaceverify2.tools.WbCloudFaceVerifySdk;

public class FaceAuthActivity extends AutoDialogActivity {
    EditText name, idcardNum;
    Button start;
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_auth);
        initView();
    }

    private void initView() {

        name = (EditText) findViewById(R.id.name);
        idcardNum = (EditText) findViewById(R.id.idcardNum);
        Button start = (Button) findViewById(R.id.start);
        RelativeLayout back = (RelativeLayout) findViewById(R.id.back);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                dialog = Background.Process(FaceAuthActivity.this, TencentAuth, "数据加载中");
                tencentAuth2();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /**
     * 获取部门
     */
    final Runnable TencentAuth = new Runnable() {
        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)

                {
                    requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 321);
                } else {
                    tencentAuth();
                }
            } else {
                tencentAuth();
            }


        }
    };

    private void tencentAuth2() {
        final String nameStr = name.getText().toString();
        final String idcardNumStr = idcardNum.getText().toString();
        if (TextUtils.isEmpty(nameStr)) {
            DialogUtil.showUIToast(this, "姓名不能为空！");
            return;
        }
        if (TextUtils.isEmpty(idcardNumStr)) {
            DialogUtil.showUIToast(this, "证件号码不能为空！");
            return;
        }

        if (!idcardNumStr.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[a-z]))$")) {
            char pidChar = idcardNumStr.charAt(idcardNumStr.length() - 1);
            if (Character.isLowerCase(pidChar)) {
                DialogUtil.showUIToast(this, "证件号结尾不能为小写字母！");
                return;
            }
        }

        if (!idcardNumStr.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z]))$")) {
            DialogUtil.showUIToast(this, "证件号不合法！");
            return;
        }
        AuthUtil.startHuaXunFaceAuth(this, nameStr, idcardNumStr,false);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == AuthUtil.HUAXUN_AUTH) {
            String result = data.getStringExtra("result");

            if (TextUtils.equals(result, "success")) {
                DialogUtil.showUIToast(FaceAuthActivity.this, "认证成功");
                Log.d("------------", "刷脸成功！ ");
                Intent intent = new Intent();
                intent.putExtra("name", name.getText().toString());
                intent.putExtra("idNum", idcardNum.getText().toString());
                intent.setClass(getApplicationContext(), QYSQ.class);
                FaceAuthActivity.this.startActivity(intent);
                finish();
            } else {
                DialogUtil.showUIToast(this, "认证失败");
            }
        }
    }

    private void tencentAuth() {
//        final String nameStr = name.getText().toString();
//        final String idcardNumStr = idcardNum.getText().toString();
//        String nonceStr = "4378fad62cad4c7592e2c0bcf263427e";
//        if (TextUtils.isEmpty(nameStr)) {
//            DialogUtil.showUIToast(this, "姓名不能为空！");
//            return;
//        }
//        if (TextUtils.isEmpty(idcardNumStr)) {
//            DialogUtil.showUIToast(this, "证件号码不能为空！");
//            return;
//        }
//
//        if (!idcardNumStr.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[a-z]))$")) {
//            char pidChar = idcardNumStr.charAt(idcardNumStr.length() - 1);
//            if (Character.isLowerCase(pidChar)) {
//                DialogUtil.showUIToast(this, "证件号结尾不能为小写字母！");
//                return;
//            }
//        }
//
//        if (!idcardNumStr.matches("^([1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3})|([1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[A-Z]))$")) {
//            DialogUtil.showUIToast(this, "证件号不合法！");
//            return;
//        }
//        String userSign = WebankUtil.getUserSign(FaceAuthActivity.this, idcardNumStr, nonceStr);
//        Bundle data = new Bundle();
//        WbCloudFaceVerifySdk.InputData inputData = new
//                WbCloudFaceVerifySdk.InputData(
//                nameStr,
//                "01",
//                idcardNumStr,
//                System.currentTimeMillis() + "",
//                "ip",
//                "gps",//GPS
//                "IDA01748",//华讯提供IDA01748
//                "1.0.0",
//                nonceStr,//32 位随机字符串//4378fad62cad4c7592e2c0bcf263427e
//                idcardNumStr,
//                userSign,
//                true,
//                FaceVerifyStatus.Mode.ADVANCED,
//                "cMei9OgTlv7TQlO8cZyOKtGb7twy1fqex2UqBp3Dcj5vC9LH9+LLWFeI+YCZvJ4l10KJ81mqp7ZjVem9RGFxFes2Kq0jWzTcV1PzE+nl0n1tkS55J1HhQgX2ThCbaFmqAqwBoPc/+xIuUQsDd/4GjJLoCPsV73VEoweYUv3U/vdDMyeC6YDzvlVGkAXPHbMWqv1wQbMbNGEPwfXszX4Xd4PqS6uYQY353axSFG7WaBvKbhb3TLaCrPWJUKO3bhYV7babpfYzsisHYlHn2vVMJNGC/qxFm466Ucfrd8m2cxVlKo6OjpFbQZhrZ+/PLySP8lzu3teF4G5P5Plgn++Qbg==");//华讯提供
//
//        System.out.println("--------data:" + inputData.toString());
//
//        data.putSerializable(WbCloudFaceVerifySdk.INPUT_DATA, inputData);
////个性化参数设置,可以不设置，不设置则为默认选项。
////此处均设置为与默认相反
////是否显示成功结果页，默认显示，此处设置为不显示
//        data.putBoolean(WbCloudFaceVerifySdk.SHOW_SUCCESS_PAGE, false);
////sdk 样式设置，默认暗黑模式， 此处设置为明亮模式
//        data.putString(WbCloudFaceVerifySdk.COLOR_MODE,
//                WbCloudFaceVerifySdk.WHITE);
////是否对录制视频进行检查,默认不检查，此处设置为检查
//        data.putBoolean(WbCloudFaceVerifySdk.VIDEO_CHECK, true);
////初始化 sdk，得到是否登录 sdk 成功的结果
//        WbCloudFaceVerifySdk.getInstance().init(
//                this,
//                data,
//                //由 FaceVerifyLoginListener 返回登录结果
//                new WbCloudFaceVerifySdk.FaceVerifyLoginListener() {
//                    @Override
//                    public void onLoginSuccess() {
//                        //登录成功，拉起 sdk 页面
//                        WbCloudFaceVerifySdk.getInstance().startActivityForSecurity(new WbCloudFaceVerifySdk.FaceVerifyResultForSecureListener() {
//                            //由 FaceVerifyResultListener 返回刷脸结果
//                            @Override
//                            public void onFinish(int resultCode, boolean nextShowGuide, String
//                                    faceCode, String faceMsg, String sign, Bundle extendData) {
//                                // resultCode 为 0，则刷脸成功；否则刷脸失败
//                                if (resultCode == 0) {
//                                    DialogUtil.showUIToast(FaceAuthActivity.this, "认证成功");
//                                    Log.d("------------", "刷脸成功！ ");
//                                    Intent intent = new Intent();
//                                    intent.putExtra("name", nameStr);
//                                    intent.putExtra("idNum", idcardNumStr);
//                                    intent.setClass(getApplicationContext(), QYSQ.class);
//                                    FaceAuthActivity.this.startActivity(intent);
//                                    finish();
//                                } else {
//                                    DialogUtil.showUIToast(FaceAuthActivity.this, "认证失败");
//                                    Log.d("------------", "刷脸失败！");
//                                }
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onLoginFailed(String s, String s1) {
//                        DialogUtil.showUIToast(FaceAuthActivity.this, "code=" + s + ",授权登录失败！");
//                        Log.d("------------", s);
//                        Log.d("------------", s1);
//                    }
//
//                });
    }
}
