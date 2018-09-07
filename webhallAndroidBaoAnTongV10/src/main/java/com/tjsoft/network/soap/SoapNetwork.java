package com.tjsoft.network.soap;

import android.text.TextUtils;

import com.socks.library.KLog;
import com.tjsoft.webhall.constants.AppConfig;
import com.tjsoft.webhall.ui.materialmanage.UrlConstants;

import org.json.JSONObject;
import org.ksoap2.HeaderProperty;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SoapNetwork {
    public static int result = 0;
    public static int ConnectTimeout = 30 * 1000;
    public static String url = UrlConstants.WS_URL;
    public static String shareUrl = UrlConstants.WS_SHARE_URL;

    /**
     * 原生服务调用方法
     *
     * @param method
     * @param services
     * @param param
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String excute(String method, String services, String param) throws Exception {
        KLog.d("fuchl  services method:    " + services + "          " + method);
        KLog.d("fuchl  param:    " + param);
        String cacheKey = url + services + method + param;
        String response = "";
        try {
            if (AppConfig.iscBus) {
                services = "zw_wsbs_" + services + "/1.0";
                url = UrlConstants.WS_CBUS_URL;
            }
            SoapObject rpc = new SoapObject(UrlConstants.NAMESPACE, method);
            rpc.addProperty("param", param);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(rpc);

            List<HeaderProperty> list = new ArrayList<>();
            list.add(new HeaderProperty("Authorization","Bearer 579272fbd31ca7083d7e3c042559cbce"));
            HttpTransportSE ht = new HttpTransportSE(url + services);
            ht.call(null, envelope,list);

            response = envelope.getResponse().toString();

            JSONObject jsonObject = new JSONObject(response);
            int code = jsonObject.optInt("code");
            if(code == 401) {
//                LoginUtils.startLoginActivity();
            }
            if (!TextUtils.isEmpty(response) && AppConfig.getInstance() != null) {//更新缓存
//                ACache cache = ACache.get(AppConfig.getInstance());
//                cache.put(cacheKey, response, 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null
            }
            KLog.d("fuchl  response:    " + response);
        } catch (Exception e) {
            e.printStackTrace();
//            ACache cache = ACache.get(AppConfig.getInstance());
//            response = cache.getAsString(cacheKey);

        }
        return response;
    }


    public static String excute(String method, String services, String param, boolean useCache) throws Exception {
        String response = excute(method, services, param);
        String cacheKey = url + services + method + param;
        if (TextUtils.isEmpty(response) && useCache) {
//            ACache cache = ACache.get(AppConfig.getInstance());
//            response = cache.getAsString(cacheKey);
        }
        return response;
    }


    public static String excute_GuiZhou(String method, String services, String param) throws Exception {

        System.out.println("fuchl  services method:    " + services + "          " + method);
        System.out.println("fuchl  param:    " + param);
        if (AppConfig.iscBus) {
            services = "WSBS_" + services + "_Proxy";
            url = UrlConstants.WS_CBUS_URL;
        }
        String response = "";
        try {
            SoapObject rpc = new SoapObject("http://www.gzegn.gov.cn:8888/rest/", method);
            rpc.addProperty("param", param);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE("http://www.gzegn.gov.cn:8888/services/" + services, ConnectTimeout);
            ht.call(null, envelope);
            response = envelope.getResponse().toString();
            System.out.println("fuchl  response:    " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    /**
     * 原生服务调用方法
     *
     * @param method
     * @param services
     * @param param
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String excuteBank(String method, String services, String param) throws Exception {
        KLog.d("fuchl  services method:    " + services + "          " + method);
        KLog.d("fuchl  param:    " + param);
        url = UrlConstants.WS_BANK_URL;
        String cacheKey = url + services + method + param;
        String response = "";
        try {
            SoapObject rpc = new SoapObject(UrlConstants.NAMESPACE, method);
            rpc.addProperty("param", param);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(url + services);
            ht.call(null, envelope);
            response = envelope.getResponse().toString();
//            if (!TextUtils.isEmpty(response)) {//更新缓存
//                ACache cache = ACache.get(AppConfig.getInstance());
//                cache.put(cacheKey, response, 2 * ACache.TIME_DAY);//保存两天，如果超过两天去获取这个key，将为null
//            }
            KLog.d("fuchl  response:    " + response);
        } catch (Exception e) {
            e.printStackTrace();
//            ACache cache = ACache.get(AppConfig.getInstance());
//            response = cache.getAsString(cacheKey);

        }
        return response;
    }

    /**
     * 政府公报服务调用方法
     *
     * @param method
     * @param services
     * @param param
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String excuteZFGB(String method, String services, String param) throws Exception {

        KLog.d("fuchl  services method:    " + services + "          " + method);
        KLog.d("fuchl  param:    " + param);
        KLog.d("fuchl  AppConfig.ZFGB_WS_URL:    " + UrlConstants.ZFGB_WS_URL);
        KLog.d("fuchl  AppConfig.ZFGBNAMESPACE:    " + UrlConstants.ZFGBNAMESPACE);
        String response = "";
        try {
            SoapObject rpc = new SoapObject(UrlConstants.ZFGBNAMESPACE, method);
            rpc.addProperty("json", param);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER12);
            envelope.bodyOut = rpc;
            // 设置是否调用的是dotNet开发的WebService
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE(UrlConstants.ZFGB_WS_URL + services);
            ht.call(null, envelope);
            response = envelope.getResponse().toString();
            KLog.d("fuchl  response:    " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * 共享服务调用方法
     */
    public static String excuteShare(String method, String services, String param) throws Exception {
        System.out.println("fuchl  services method===:    " + services + "          " + method);
        System.out.println("fuchl  param===:    " + param);
        if (AppConfig.iscBus) {
            services = "SPACE_" + services + "_Proxy";
            shareUrl = UrlConstants.WS_CBUS_URL;
        }
        String response = "";

        try {
            SoapObject rpc = new SoapObject(UrlConstants.NAMESPACE, method);
            rpc.addProperty("param", param);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(shareUrl + services);
            ht.call(null, envelope);
            response = envelope.getResponse().toString();
            System.out.println("fuchl  response  ====:    " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    public static String get(String address) throws Exception {
        System.out.println("fuchl  url  " + address);
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(ConnectTimeout);
                conn.setReadTimeout(ConnectTimeout);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null)
                            break;

                        output.append(line + '\n');
                    }
                    reader.close();
                } else {
                    throw new Exception(String.format("ErrorCode = %d", conn.getResponseCode()));
                }
                conn.disconnect();
            }
        } catch (SocketTimeoutException e) {
            throw new Exception("time out");
        } catch (Exception E) {
            throw new Exception("Occour error");
        }
        System.out.println("fuchl  response  " + output.toString());
        return output.toString();
    }

    public static String post(String address, String param) throws Exception {
        System.out.println("fuchl  url:" + address);
        System.out.println("fuchl  param:" + param);
        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(address);
            result = 0;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(ConnectTimeout);
                conn.setReadTimeout(ConnectTimeout);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                conn.setRequestProperty("Content-Lenth", "" + Integer.toString(param.getBytes().length));
                conn.setRequestProperty("Content-Langueage", "en-US");

                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.writeBytes(param);
                wr.flush();
                wr.close();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null)
                            break;
                        output.append(line + '\n');
                    }
                    reader.close();
                    result = 1;
                } else {
                    throw new Exception(String.format("ErrorCode = %d", conn.getResponseCode()));
                }
                conn.disconnect();
            }
        } catch (SocketTimeoutException e) {
            result = -1;
            throw new Exception("TimeOut");
        } catch (Exception E) {
            result = 0;
            throw new Exception("Occour error");
        }
        System.out.println("fuchl  response:" + output.toString());
        return output.toString();
    }


    public static String postWithJson(String address, String json) throws Exception {
        KLog.d("fcl   url  " + address);
        KLog.d("fcl   param   " + json);

        StringBuilder output = new StringBuilder();
        try {
            URL url = new URL(address);
            result = 0;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            if (conn != null) {
                conn.setConnectTimeout(ConnectTimeout);
                conn.setReadTimeout(ConnectTimeout);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);

                conn.setRequestProperty("Content-Lenth", "" + Integer.toString(json.getBytes().length));
                conn.setRequestProperty("Content-Langueage", "en-US");
//                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.write(json.getBytes());
//                wr.writeBytes(json);
                wr.flush();
                wr.close();
                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while (true) {
                        String line = reader.readLine();
                        if (line == null)
                            break;
                        output.append(line + '\n');
                    }
                    reader.close();
                    result = 1;
                } else {

                    throw new Exception(String.format("ErrorCode = %d", conn.getResponseCode()));
                }
                conn.disconnect();
            }
        } catch (SocketTimeoutException e) {
            result = -1;
            throw new Exception("TimeOut");
        } catch (Exception E) {
            result = 0;
            E.printStackTrace();
//            throw new Exception("Occour error");
        }
        System.out.println("fuchl  response:" + output.toString());
        return output.toString();
    }

    // 模拟网络 sd卡读取
//
//    public static String excuteDataFromSD(String method, String services, String param) throws Exception {
//        FileHelper helper = new FileHelper(AppConfig.getInstance());
//        String response = helper.readSDFile("test/" + (method + "_" + services + "_" + param).hashCode() + "");
//        System.out.println("fuchl   response:" + response);
//        return response;
//    }

//    // assets 目录读取
//    public static String excuteDataFromAsset(String method, String services, String param) throws Exception {
//        String response = FileUtil.getFromAssets(AppConfig.getInstance(), (method + "_" + services + "_" + param).hashCode() + "");
//        System.out.println("fuchl   response:" + response);
//        return response;
//    }

    /**
     * 原生服务调用方法
     *
     * @param method
     * @param services
     * @param param
     * @return
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static String excuteJida(String method, String services, String param)
            throws Exception {
//        http://hao521.eicp.net/jlsyzw/cxf/Users
//        String myUrl = "http://139.215.206.149/jlyzw/cxf/";
        String myUrl = "http://hao521.eicp.net/jlyzw/cxf/";
        System.out.println("fuchl  services method:    " + services
                + "          " + method);
        System.out.println("fuchl  param:    " + param);
        String response = "";
        try {
            SoapObject rpc = new SoapObject("http://service.hoo.com/", method);
            rpc.addProperty("json", param);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(rpc);
            HttpTransportSE ht = new HttpTransportSE(myUrl + services);
            ht.call(null, envelope);
            response = envelope.getResponse().toString();
            System.out.println("fuchl  response:    " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


}
