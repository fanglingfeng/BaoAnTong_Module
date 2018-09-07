package com.tjsoft.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;

import org.apache.http.NoHttpResponseException;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.tjsoft.webhall.AppConfig;
import com.tjsoft.webhall.constants.Constants;
import com.tjsoft.webhall.ui.wsbs.WSBS1;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class HTTP {
	public static int result = 0;
	public static int ConnectTimeout = 15 * 1000;
	public static String url = Constants.WS_URL;
	public static String shareUrl = Constants.WS_SHARE_URL;
	public static int TimeOut=30*1000;
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
	public static String excute(String method, String services, String param)
			throws IOException, XmlPullParserException {

		System.out.println("fuchl  services method:    " + services
				+ "          " + method);
		System.out.println("fuchl  param:    " + param);
		String response = "";

		if (Constants.iscBus) {
			services = "WSBS_" + services + "_Proxy";
			url = Constants.WS_CBUS_URL;
		}

		SoapObject rpc = new SoapObject(Constants.NAMESPACE, method);
		rpc.addProperty("param", param);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(url + services);
		ht.call(null, envelope);
		response = envelope.getResponse().toString();

		System.out.println("fuchl  response:    " + response);

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
	public static String excuteAndCache(String method, String services, String param,Context context)
			throws IOException, XmlPullParserException {

		System.out.println("fuchl  services method:    " + services
				+ "          " + method);
		System.out.println("fuchl  param:    " + param);
		String cacheKey = url + services + method + param;
		String response = "";
		try {

			if (Constants.iscBus) {
				services = "WSBS_" + services + "_Proxy";
				url = Constants.WS_CBUS_URL;
			}

			SoapObject rpc = new SoapObject(Constants.NAMESPACE, method);
			rpc.addProperty("param", param);
			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope.setOutputSoapObject(rpc);
			 // 设置与.NET提供的Web service保持有良好的兼容性  
//			envelope.dotNet = true;  
			HttpTransportSE ht = new HttpTransportSE(url + services,TimeOut);
			ht.call(null, envelope);			
			response = envelope.getResponse().toString();			
			if (!TextUtils.isEmpty(response)) {// 更新缓存
				ACache cache = ACache.get(context);
				cache.put(cacheKey, response, 2 * ACache.TIME_DAY);// 保存两天，如果超过两天去获取这个key，将为null
			}
			System.out.println("fuchl  response:    " + response);
		} catch (Exception e) {	
			e.printStackTrace();
			String msg="";
			if(e instanceof SocketTimeoutException){
				msg="网络连接超时，请稍后重试";
			}
//			else if(e instanceof UnknownHostException){
//				msg="未知服务器，请检查配置";
//			}
			else{
				msg="网络异常，请稍后重试";
			}
			Log.e("TAG", "sps=="+msg);
			DialogUtil.showUIToast(context, msg);

//			return response;
//			ACache cache = ACache.get(context);
//            response = cache.getAsString(cacheKey);
		}
		return response;
	}

	/** 共享服务调用方法 */
	public static String excuteShare(String method, String services,
			String param) throws IOException, XmlPullParserException {
		System.out.println("fuchl  services method===:    " + services
				+ "          " + method);
		System.out.println("fuchl  param===:    " + param);
		if (Constants.iscBus) {
			services = "SPACE_" + services + "_Proxy";
			shareUrl = Constants.WS_CBUS_URL;
		}
		String response = "";
		SoapObject rpc = new SoapObject(Constants.NAMESPACE, method);
		rpc.addProperty("param", param);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER11);
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(shareUrl + services);
		ht.call(null, envelope);
		response = envelope.getResponse().toString();
		System.out.println("fuchl  response  ====:    " + response);
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
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					while (true) {
						String line = reader.readLine();
						if (line == null)
							break;

						output.append(line + '\n');
					}
					reader.close();
				} else {
					throw new Exception(String.format("ErrorCode = %d",
							conn.getResponseCode()));
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

				conn.setRequestProperty("Content-Lenth",
						"" + Integer.toString(param.getBytes().length));
				conn.setRequestProperty("Content-Langueage", "en-US");
				DataOutputStream wr = new DataOutputStream(
						conn.getOutputStream());
				wr.writeBytes(param);
				wr.flush();
				wr.close();

				if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));
					while (true) {
						String line = reader.readLine();
						if (line == null)
							break;

						output.append(line + '\n');
					}
					reader.close();
					result = 1;
				} else {
					throw new Exception(String.format("ErrorCode = %d",
							conn.getResponseCode()));
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



}
