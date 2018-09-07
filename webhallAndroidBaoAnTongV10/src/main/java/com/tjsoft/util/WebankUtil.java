package com.tjsoft.util;

import android.content.Context;
import android.util.Log;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.FormBody;

/**
 * Created by long on 2018/4/3.
 */

public class WebankUtil {
    public static String getToken() {
        try {
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url("https://idasc.webank.com/api/oauth2/access_token?app_id=TIDA2311&secret=1UK1rvASjReoRUa72BWtuN1afCnnr8uLBYd2DVxmdm5KloOOcJlW4KU8PNgBPWiw&grant_type=client_credential&version=1.0.0")//请求接口。如果需要传参拼接到接口后面。
                    .build();//创建Request 对象
            Response res = client.newCall(request).execute();//得到Response 对象
            String response = res.body().string();
            String access_token = (String) new JSONObject(response).get("access_token");
            System.out.println("---- token:" + access_token);
            return access_token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTicket() {
        try {
            OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象
            Request request = new Request.Builder()
                    .url("https://idasc.webank.com/api/oauth2/api_ticket?app_id=TIDA2311&access_token=" + getToken() + "&type=NONCE&version=1.0.0&user_id=254988392")//请求接口。如果需要传参拼接到接口后面。
                    .build();//创建Request 对象
            Response res = client.newCall(request).execute();//得到Response 对象
            String ticket = new JSONObject(res.body().string()).getJSONArray("tickets").getJSONObject(0).getString("value");
            System.out.println("---- ticket:" + ticket);
            return ticket;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getUserSign(Context context, String idcard, String nonceStr) {

        try {
            String param = "idcard=" + idcard + "&nonceStr=" + nonceStr;
            String response = HttpUtils.doPost("http://test.hxfzsoft.com:10047/sso/face/getSign.do", param.toString());
            String userSign = new JSONObject(response).getString("data");
            System.out.println("--------response:" + response);
            System.out.println("--------userSign:" + userSign);
            return userSign;
        } catch (JSONException e) {
            DialogUtil.showUIToast(context, "获取签名失败！");
            e.printStackTrace();
        }
        return "";
    }
}
