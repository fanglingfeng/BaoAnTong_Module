package com.tjsoft.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5密码加密
 * 
 * @author
 * 
 */
public class Md5PwdEncoder {

	public static  String encodePassword(String rawPass) {
		MessageDigest messageDigest = getMessageDigest();
		byte[] digest;
		try {
			digest = messageDigest.digest(rawPass.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("UTF-8 not supported!");
		}
		return new String(encodeHex(digest));
	}

	protected final static MessageDigest getMessageDigest() {
		String algorithm = "MD5";
		try {
			return MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalArgumentException("No such algorithm ["
					+ algorithm + "]");
		}
	}

	public static char[] encodeHex(byte data[]) {
		int l = data.length;
		char out[] = new char[l << 1];
		int i = 0;
		int j = 0;
		for (; i < l; i++) {
			out[j++] = DIGITS[(240 & data[i]) >>> 4];
			out[j++] = DIGITS[15 & data[i]];
		}

		return out;
	}

	private static final char DIGITS[] = { '0', '1', '2', '3', '4', '5', '6',
			'7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

}
