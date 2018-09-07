package com.tjsoft.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

import android.util.Base64;
import android.util.Log;

/**
 * 上传文件到服务器
 * 
 * @author Administrator
 * 
 */
public class SocketHttpRequester {
	/**
	 * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能: <FORM METHOD=POST
	 * ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet"
	 * enctype="multipart/form-data"> <INPUT TYPE="text" NAME="name"> <INPUT
	 * TYPE="text" NAME="id"> <input type="file" name="imagefile"/> <input
	 * type="file" name="zip"/> </FORM>
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */

	public static long s;
	private static String TAG = "fuchl";

	public static String post(String path, Map<String, String> params, FormFile[] files) throws Exception {
		final String BOUNDARY = "---------------------------7da2137580612"; // 数据分隔线
		final String endline = "--" + BOUNDARY + "--\r\n";// 数据结束标志

		int fileDataLength = 0;
		for (FormFile uploadFile : files) {// 得到文件类型数据的总长度
			StringBuilder fileExplain = new StringBuilder();
			fileExplain.append("--");
			fileExplain.append(BOUNDARY);
			fileExplain.append("\r\n");
			fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + Base64.encodeToString(uploadFile.getFilname().getBytes(), 1) + "\"\r\n");
			fileExplain.append("Content-Type: " + "application/octet-stream" + "\r\n\r\n");
			fileExplain.append("\r\n");
			fileDataLength += fileExplain.length();
			if (uploadFile.getInStream() != null) {
				fileDataLength += uploadFile.getFile().length();
			} else {
				fileDataLength += uploadFile.getData().length;
			}
		}
		StringBuilder textEntity = new StringBuilder();
		for (Map.Entry<String, String> entry : params.entrySet()) {// 构造文本类型参数的实体数据
			textEntity.append("--");
			textEntity.append(BOUNDARY);
			textEntity.append("\r\n");
			textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
			textEntity.append(entry.getValue());
			textEntity.append("\r\n");
		}
		// 计算传输给服务器的实体数据总长度
		int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

		URL url = new URL(path);
		int port = url.getPort() == -1 ? 80 : url.getPort();
		Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
		OutputStream outStream = socket.getOutputStream();
		// 下面完成HTTP请求头的发送
		String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
		outStream.write(requestmethod.getBytes());
		String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
		outStream.write(accept.getBytes());
		String language = "Accept-Language: zh-CN\r\n";
		outStream.write(language.getBytes());
		String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
		outStream.write(contenttype.getBytes());
		String contentlength = "Content-Length: " + dataLength + "\r\n";
		outStream.write(contentlength.getBytes());
		String alive = "Connection: Keep-Alive\r\n";
		outStream.write(alive.getBytes());
		String host = "Host: " + url.getHost() + ":" + port + "\r\n";
		outStream.write(host.getBytes());
		// 写完HTTP请求头后根据HTTP协议再写一个回车换行
		outStream.write("\r\n".getBytes());
		// 把所有文本类型的实体数据发送出来
		outStream.write(textEntity.toString().getBytes());
		// 把所有文件类型的实体数据发送出来
		for (FormFile uploadFile : files) {
			StringBuilder fileEntity = new StringBuilder();
			fileEntity.append("--");
			fileEntity.append(BOUNDARY);
			fileEntity.append("\r\n");
			fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
			fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
			outStream.write(fileEntity.toString().getBytes());

			if (uploadFile.getInStream() != null) {
				byte[] buffer = new byte[1024];
				int len = 0;
				while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
					outStream.write(buffer, 0, len);
				}
				uploadFile.getInStream().close();
			} else {
				outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
			}
			outStream.write("\r\n".getBytes());
		}
		// 下面发送数据结束标志，表示数据已经结束
		outStream.write(endline.getBytes());
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

		String temp = "";
		StringBuilder str = new StringBuilder();
		while ((temp = reader.readLine()) != null) {
			str.append(temp);
			if (temp.contains("}")) {
				break;
			}
		}
		System.out.println(" fuchl   upload response     " + str);
		int start = str.indexOf("{");
		int end = str.indexOf("}");
		String json = str.substring(start, end + 2);
		System.out.println("fuchl   json:" + json);
		if (!str.toString().contains("200")) {// 读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
			return "";
		}
		outStream.flush();
		outStream.close();
		reader.close();
		socket.close();

		return json;
	}

	public String inputStream2String(InputStream in) throws IOException {
		StringBuffer out = new StringBuffer();
		byte[] b = new byte[4096];
		for (int n; (n = in.read(b)) != -1;) {
			out.append(new String(b, 0, n));
		}
		return out.toString();
	}

	/**
	 * 提交数据到服务器
	 * 
	 * @param path
	 *            上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://
	 *            www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
	 * @param params
	 *            请求参数 key为参数名,value为参数值
	 * @param file
	 *            上传文件
	 */
	public static String post(String path, Map<String, String> params, FormFile file) throws Exception {
		return post(path, params, new FormFile[] { file });
	}

	/**
	 * 上传文件 form post
	 * 
	 * @category 各种类型，图片，文档等，统一视为file
	 * @param request
	 * @param params
	 * @param filePath
	 *            必须 必须 1：头像；2：身份证；3：在职证明；4：资产证明；5：名片；
	 */
	public static String uploadFile(String uploadUrl, String request, Map<String, String> params, String filePath) {

		// form标准提交格式拼接
		String end = "\r\n";
		String twoHyphens = "--"; // 两个连字符
		String boundary = "---------------pedataisfantasticforvcpe"; // 分界符的字符串随便
		try {
			URL url = new URL(uploadUrl);
			HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
			httpURLConnection.setDoInput(true);
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestMethod("POST");
			// 设置Http请求头
			httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Charset", "UTF-8");

			// mCurrentSession = CookieUtil.getSessionToken();
			// if (mCurrentSession != null) {
			// httpURLConnection.setRequestProperty("Cookie", mCurrentSession);
			// }

			// 必须在Content-Type 请求头中指定分界符中的任意字符串
			httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

			// check file if exist
			File file = new File(filePath);
			if (!file.exists() || !file.isFile()) {// 没有文件
				Log.e(TAG, "savePath,file = " + filePath + "不存在！");
				// if (mListener != null) {
				// mListener.onRequestHttpResult(actionId, "文件不存在",
				// RequestResultListener.RESULT_CODE_NET_OTHER_ERROR,
				// otherFlag);
				// return;
				// }
				return null;
			}

			// 定义数据写入流，准备上传文件
			DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());

			// form 拼接格式 start
			// ===================1
			dos.writeBytes(twoHyphens + boundary + end);
			// 设置与上传文件相关的信息
			// String filename = "/sdcard/c.mp4";
			// String filename = "/sdcard/b.jpg";
			// String filename = "/sdcard/a.txt";

			// 固定格式 name is key
			// 固定格式 filename is value
			dos.writeBytes("Content-Disposition:form-data;name=\"FileName\";filename=\"" + filePath.substring(filePath.lastIndexOf("/") + 1) + "\"" + end);
			// value上要空一行我去
			// 文件格式需要指定标准
			dos.writeBytes("Content-Type:application/octet-stream" + end + end);// 二进制通吃
			// dos.writeBytes("Content-Type:image/jpeg" + end + end);//单一jpg
			FileInputStream fis = new FileInputStream(filePath);

			byte[] buffer = new byte[8192]; // 8k
			int count = 0;
			// 读取文件夹内容，并写入OutputStream对象
			while ((count = fis.read(buffer)) != -1) {
				dos.write(buffer, 0, count);
			}
			fis.close();
			dos.flush();
			dos.writeBytes(end);

			// ===================2 文本
			dos.writeBytes(twoHyphens + boundary + end);
			dos.writeBytes("Content-Disposition:form-data;name=\"FileType\"" + end + end);// 和Value隔开一行
			// /r/n
			dos.writeBytes("2" + end);

			// ===================3

			// end --结尾作为结束标识
			dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
			dos.flush();

			// 处理响应结果
			int responseCode = httpURLConnection.getResponseCode();
			Log.d(TAG, "responseCode=" + responseCode);
			if (responseCode == 301 || responseCode == 302) {
				String location = httpURLConnection.getHeaderField("location");
				Log.d(TAG, "location=" + location);
			}
			if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
				throw new RuntimeException("服务器地址错误！");
			} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
				throw new RuntimeException("服务器连接错误！");
			} else if (responseCode == HttpURLConnection.HTTP_OK) {
				Log.d(TAG, "服务器连接成功");
			}

			java.io.InputStream pageStream = httpURLConnection.getInputStream();
			// byte数组的最大长度是61858764，近60M。
			byte[] result = readStream(pageStream);
			pageStream.close();
			dos.close();
			httpURLConnection.disconnect();

			// handle result
			if (result != null) {
				try {
					// String 最长的长度 为 2^32，4G。
					String strResult = new String(result, "UTF-8");

					Log.d(TAG, "strResult=" + strResult);
					return strResult;
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			} else {
				Log.e(TAG, "byte[] result is null");
			}

		} catch (IOException e) {
			e.printStackTrace();
			Log.e(TAG, "http helper error=" + e.toString());

		}

		return null;
	}

	/**
	 * 文件流转换
	 * 
	 * @param pageStream
	 * @return
	 */
	private static byte[] readStream(InputStream pageStream) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		try {
			while (((len = pageStream.read(buffer)) != -1)) {
				bos.write(buffer, 0, len);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return bos.toByteArray();
	}
}