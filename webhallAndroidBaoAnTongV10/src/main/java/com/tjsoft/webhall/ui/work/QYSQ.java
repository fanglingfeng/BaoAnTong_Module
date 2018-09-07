package com.tjsoft.webhall.ui.work;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tjsoft.msfw.guangdongshenzhenbaoan.R;
import com.tjsoft.util.Background;
import com.tjsoft.util.DateUtils;
import com.tjsoft.util.DialogUtil;
import com.tjsoft.util.HTTP;
import com.tjsoft.util.MSFWResource;
import com.tjsoft.webhall.AutoDialogActivity;
import com.tjsoft.webhall.constants.Constants;

import org.json.JSONObject;

/**
 * 进度查询页面
 *
 * @author Administrator
 */
public class QYSQ extends AutoDialogActivity {
    private Button findByName, findProgress, more;
    private RelativeLayout back;
    private EditText qyName;
    private Intent intent;
    private ImageButton erweima;
    private TextView userName, date;
    private String name, idNum;
    private Button submit;
    private RadioGroup type,radioGroup1, radioGroup2, radioGroup3;
    private String ISWISH = "1";
    private String ISCERTIFICATE = "1";
    private String ISREAL = "1";
    private String YWLX = "1";
    private String NAME = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(MSFWResource.getResourseIdByName(this, "layout", "tj_qysq"));
        Constants.getInstance().addActivity(this);
        InitView();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    /*
    * 提交企业申请请求
    * */
    final Runnable Submit = new Runnable() {
        @Override
        public void run() {
            try {
                NAME = qyName.getText().toString();
                if (TextUtils.isEmpty(NAME)) {
                    DialogUtil.showUIToast(QYSQ.this, "请输入企业名称");
                    return;
                }
                if (ISWISH.equals("0") || ISCERTIFICATE.equals("0") || ISREAL.equals("0")) {
                    DialogUtil.showUIToast(QYSQ.this, "问题中存在“否”的回答，无法提交！");
                    return;
                }
                JSONObject param = new JSONObject();
                param.put("NAME", NAME);
//                param.put("YWLX", YWLX);
                param.put("ISWISH", ISWISH);
                param.put("ISCERTIFICATE", ISCERTIFICATE);
                param.put("ISREAL", ISREAL);
                param.put("PERSON", name);
                param.put("TIME", DateUtils.getStringDate(DateUtils.SYSTEM_DATE_FORMAT_CN));
                param.put("STATUS", "1");
                param.put("PERSONIDCARD", idNum);
                param.put("SBLY", "2");

                String response = HTTP.excute("savaEnterpriseInfo", "RestEnterpriseService", param.toString());
                JSONObject json = new JSONObject(response);
                String code = json.getString("code");
                if (code.equals("200")) {
                    DialogUtil.showUIToast(QYSQ.this, "提交成功！");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

// 获取软键盘的显示状态
                    boolean isOpen=imm.isActive();

// 如果软键盘已经显示，则隐藏，反之则显示
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

// 隐藏软键盘
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                    finish();
                } else {
                    DialogUtil.showUIToast(QYSQ.this, json.getString("error"));
                }
            } catch (Exception e) {
                DialogUtil.showUIToast(QYSQ.this, getString(
                        MSFWResource.getResourseIdByName(QYSQ.this, "string", "tj_occurs_error_network")));
                e.printStackTrace();
            }
        }
    };

    private void InitView() {
        name = getIntent().getStringExtra("name");
        idNum = getIntent().getStringExtra("idNum");
        back = (RelativeLayout) findViewById(MSFWResource.getResourseIdByName(this, "id", "back"));
        userName = (TextView) findViewById(MSFWResource.getResourseIdByName(this, "id", "userName"));
        type = (RadioGroup) findViewById(R.id.type);
        radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
        date = (TextView) findViewById(R.id.date);
        radioGroup2 = (RadioGroup) findViewById(R.id.radioGroup2);
        radioGroup3 = (RadioGroup) findViewById(R.id.radioGroup3);
        submit = (Button) findViewById(R.id.submit);
        qyName = (EditText) findViewById(R.id.qyName);
        userName.setText(name);
        date.setText(DateUtils.getStringDate(DateUtils.SYSTEM_DATE_FORMAT_CN));

        submit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = Background.Process(QYSQ.this, Submit, "正在提交...");
            }
        });
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioBtn1_yes) {
                    ISWISH = "1";
                } else {
                    ISWISH = "0";
                }
            }
        });
        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioBtn2_yes) {
                    ISCERTIFICATE = "1";
                } else {
                    ISCERTIFICATE = "0";
                }
            }
        });
        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.radioBtn3_yes) {
                    ISREAL = "1";
                } else {
                    ISREAL = "0";
                }
            }
        });
        type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.type1) {
                    YWLX = "1";
                } else {
                    YWLX = "2";
                }
            }
        });

    }


}
