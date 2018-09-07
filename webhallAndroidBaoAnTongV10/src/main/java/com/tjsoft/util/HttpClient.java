package com.tjsoft.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.cert.CertificateException;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.CoreProtocolPNames;

public class HttpClient {
	private static final String CHARSET = "UTF-8";

	public static void main(String[] args) throws Exception {
		// testApi();

	}

	/**
	 * 调用http请求,返回string结果数据
	 * 
	 * @param req
	 *            json格式请求参数
	 * @param fi
	 * @return
	 */
	public static String requestForStringUseGet(String url) throws Exception {
		String returnStr = "";
		HttpResponse httpResponse = null;
		DefaultHttpClient httpClient = null;

		// Log.v("begin请求入参", Global.INTERFACE_URL + "?if="+fi+"&req="+req);

		// httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,
		// HTTP.UTF_8));

		httpClient = new DefaultHttpClient();// http客户端
		enableSSL(httpClient);

		httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
				false);
		// 连接超时
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);

		// 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				30000);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, false);

		// httpResponse = httpClient.execute(httppost);
		// Log.v("请求返回状态",
		// StringUtil.toString(httpResponse.getStatusLine().getStatusCode()));
		// 请求地址
		HttpGet httpget = new HttpGet(url);

		Date sDate = new Date();
		System.out
				.println("#######################调用开始########################");
		httpResponse = httpClient.execute(httpget);

		// 请求结果处理
		HttpEntity entity = httpResponse.getEntity();

		// if(httpResponse.getStatusLine().getStatusCode() == 200){
		if (entity != null) {
			InputStream content = entity.getContent();
			returnStr = convertStreamToString(content);
		}

		// 释放连接
		System.out
				.println("#######################调用结束########################本次调用耗时："
						+ DateUtils.getTimeInMillis(sDate, new Date()));

		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}

		System.out.println("end 请求出参===>" + returnStr);
		return returnStr;

	}

	/**
	 * 调用http请求,返回string结果数据
	 * 
	 * @param req
	 *            json格式请求参数
	 * @param fi
	 * @return
	 */
	public static String requestForStringUsePost(String url, String json)
			throws Exception {
		System.out.println("fuchl post url:"+url);
		System.out.println("fuchl post json:"+json);
		String returnStr = "";
		HttpResponse httpResponse = null;
		DefaultHttpClient httpClient = null;
		//System.out.println("###调用开始###");

		// 请求参数

		httpClient = new DefaultHttpClient();// http客户端
		enableSSL(httpClient);
		httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS,
				false);
		// 连接超时
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);

		// 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				30000);
		httpClient.getParams().setParameter(
				CoreProtocolPNames.USE_EXPECT_CONTINUE, false);
		// 请求地址
		HttpPost httppost = new HttpPost(url);
		if (StringUtil.isNotBlank(json)) {
			StringEntity stringEntity = new StringEntity(json, "utf-8");
			httppost.setEntity(stringEntity);
		} else {
			StringEntity stringEntity = new StringEntity("{}", "utf-8");
			httppost.setEntity(stringEntity);
		}
		// HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);

		httppost.setHeader("Content-type", "application/json");

		httpResponse = httpClient.execute(httppost);

		// Log.v("请求返回状态",
		// StringUtil.toString(httpResponse.getStatusLine().getStatusCode()));

		// 请求结果处理
		HttpEntity entity = httpResponse.getEntity();

		// if(httpResponse.getStatusLine().getStatusCode() == 200){
		if (entity != null) {
			InputStream content = entity.getContent();
			returnStr = convertStreamToString(content);
		}

		// 释放连接
		//System.out.println("###调用结束###本次调用耗时："+ DateUtil.getTimeInMillis(sDate, new Date()));
		if (httpClient != null) {
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}

		//System.out.println("end 请求出参===>" + returnStr);
		return returnStr;

	}

	/**
	 * 将流转换为字符串
	 * 
	 * @param is
	 * @return
	 */
	public static String convertStreamToString(InputStream is) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is,
				CHARSET));
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static void enableSSL(DefaultHttpClient httpclient) {
		// 调用ssl
		try {
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, new TrustManager[] { truseAllManager }, null);
			SSLSocketFactory sf = new SSLSocketFactory(sslcontext);
			sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			Scheme https = new Scheme("https", sf, 443);
			httpclient.getConnectionManager().getSchemeRegistry().register(
					https);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写验证方法，取消检测ssl
	 */
	private static TrustManager truseAllManager = new X509TrustManager() {

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] arg0, String arg1)
				throws CertificateException {
			// TODO Auto-generated method stub

		}

		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			// TODO Auto-generated method stub
			return null;
		}

	};
}
