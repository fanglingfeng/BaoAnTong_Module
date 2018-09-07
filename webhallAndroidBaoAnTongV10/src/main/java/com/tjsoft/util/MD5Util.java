package com.tjsoft.util;



import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.tjsoft.webhall.constants.Constants;



/**
 * 
 * MD5工具类，提供字符串MD5加密（校验）、文件MD5值获取（校验）功能。
 */

public class MD5Util{

	/**
	 * 
	 * 16进制字符集
	 */
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * 
	 * 指定算法为MD5的MessageDigest
	 */
	private static MessageDigest messageDigest = null;

	/**
	 * 
	 * 初始化messageDigest的加密算法为MD5
	 */
	static{
		try{
			messageDigest = MessageDigest.getInstance("MD5");
		}catch (NoSuchAlgorithmException e){
			e.printStackTrace();
		}
	}


	/**
	 * MD5加密字符串
	 * @param str目标字符串
	 * @return MD5加密后的字符串
	 */
	public static String getMD5String(String str){
		return getMD5String(str.getBytes());
	}

	/**
	 * MD5加密以byte数组表示的字符串
	 * @param bytes目标byte数组
	 * @return MD5加密后的字符串
	 */
	public static String getMD5String(byte[] bytes){
		messageDigest.update(bytes);
		return bytesToHex(messageDigest.digest());
	}

	/**
	 * 校验密码与其MD5是否一致
	 * @param pwd密码字符串
	 * @param md5基准MD5值
	 * @return 检验结果
	 */
	public static boolean checkPassword(String pwd, String md5){
		return getMD5String(pwd).equalsIgnoreCase(md5);
	}

	/**
	 * 校验密码与其MD5是否一致
	 * @param pwd以字符数组表示的密码
	 * @param md5基准MD5值
	 * @return 检验结果
	 */
	public static boolean checkPassword(char[] pwd, String md5){
		return checkPassword(new String(pwd), md5);
	}


	/**
	 * 将字节数组转换成16进制字符串
	 * @param bytes目标字节数组
	 * @return 转换结果
	 */
	public static String bytesToHex(byte bytes[]){
		return bytesToHex(bytes, 0, bytes.length);
	}

	/**
	 * 将字节数组中指定区间的子数组转换成16进制字符串
	 * @param bytes目标字节数组
	 * @param start起始位置（包括该位置）
	 * @param end结束位置（不包括该位置）
	 * @return 转换结果
	 */
	public static String bytesToHex(byte bytes[], int start, int end){
		StringBuilder sb = new StringBuilder();
		for (int i = start; i < start + end; i++){
			sb.append(byteToHex(bytes[i]));
		}
		return sb.toString();
	}

	/**
	 * 
	 * 将单个字节码转换成16进制字符串
	 * @param bt目标字节
	 * @return 转换结果
	 */
	public static String byteToHex(byte bt){
		return HEX_DIGITS[(bt & 0xf0) >> 4] + "" + HEX_DIGITS[bt & 0xf];
	}

//	public static long byteToInt(String plainText) {
//		messageDigest.update(plainText.getBytes());
//		byte b[] = messageDigest.digest();
//		int i;
//		StringBuffer buf = new StringBuffer("");
//		for (int offset = 0; offset < b.length; offset++) {
//			i = b[offset];
//			if (i < 0)
//				i += 256;
//			if (i < 16)
//				buf.append("0");
//			buf.append(Integer.toOctalString(i));
//		}
//		System.out.println(buf.toString());
//		String result = buf.toString().substring(8, 24);
//		return StringUtil.toLong(result, 0L);
//	}

	public static void main(String[] args) throws Exception  {
		MD5Util.getResponseString("","");
	}
	
	public static String getResponseString(String url, String encoding) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("pageNo", 1);
		paramMap.put("tid", 10032);
		String queryCode = getRequertCode(paramMap);
		paramMap.put("code", queryCode);
		String queryStr = getRequertString(paramMap);
//		String response = HTTP.post(url+queryStr);
		System.out.println(queryStr+" == "+queryCode);
		
		return queryStr;
	}

	public static String getRequertString(Map<String, Object> params) {
		if(params==null || params.size()==0){
			return "";
		}
		StringBuffer queryStr = new StringBuffer("");
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			queryStr.append(entry.getKey()+"="+entry.getValue()+"&");
        }
		return queryStr.toString();
	}
	
	public static String getRequertCode(Map<String, Object> paramMap) {
		String[] keyArray = paramMap.keySet().toArray(new String[0]);
		Arrays.sort(keyArray);

		StringBuilder params = new StringBuilder();
		for (String key : keyArray) {
			params.append(key).append(paramMap.get(key));
		}
		String code = MD5Util.getMD5String(Constants.REQUEST_INFO_KEY + params.toString());
		return code;
	}
}
