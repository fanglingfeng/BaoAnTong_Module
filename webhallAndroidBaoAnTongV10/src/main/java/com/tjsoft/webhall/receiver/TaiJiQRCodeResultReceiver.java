package com.tjsoft.webhall.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.tjsoft.webhall.ui.search.SearchSchedule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 第三方二唯码扫描结果接收者
 * EG. 太极处理
 */
public class TaiJiQRCodeResultReceiver extends BroadcastReceiver {

    public static final String DATA = "data";

    /**
     * 二唯码扫描事件
     */
    public static final String ACTION_QRCODE_SCAN = "com.mst.code.ACTION_QRCODE_SCAN";

    /**
     * 太极的匹配规则（正则）
     */
    private static Pattern pattern;
    private static Pattern pattern1;

    static {
        String re1="[1-3]{1}[0-9]{1}[01-12]{1}[01-31]{1}";	// YYYYMMDD 1
        String re3="[0-9]{14}";	// Integer Number 1

        pattern= Pattern.compile(re1+re3,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
        String re4="(S)";	// Any Single Word Character (Not Whitespace) 1
        String re5="[0-9]{15}";	// Integer Number 1
        String re6="[0-9a-zA-Z]";	// Any Single Word Character (Not Whitespace) 2
        String re7="[0-9a-zA-Z]";
      pattern1 = Pattern.compile(re4+re5+re6+re7,Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (ACTION_QRCODE_SCAN.equals(action)) {
            String result = intent.getStringExtra(DATA);
            if (!TextUtils.isEmpty(result)) {
                result = result.trim();
                if (result.length() != 0) {
                    processResult(context, result);
                    return;
                }
            }
            Toast.makeText(context, "二唯码内容为空！", Toast.LENGTH_SHORT).show();
            abortBroadcast();
        }
    }

    /**
     * 处理二唯码结果
     *
     * @param context
     * @param result 二唯码内容
     */
    private void processResult(Context context, String result) {
        Matcher matcher = pattern.matcher(result);
        Matcher matcher1 = pattern1.matcher(result);
        if (matcher.matches()||matcher1.matches()) {
            //====== start 这里写上太级自己的处理代码==============
            //=====================end========================
//            DialogUtil.showUIToast(context, result);
            Intent intent = new Intent();
            intent.setClass(context, SearchSchedule.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("BSNUM", result);
            intent.putExtra("APPNAME", "erweima");
            context.startActivity(intent);



            //下面这一句代码非常重要，如果可以处理，必须调用abortBroadcast()方法，以终止广播，否则不能调用
            abortBroadcast();
        }
    }
}
