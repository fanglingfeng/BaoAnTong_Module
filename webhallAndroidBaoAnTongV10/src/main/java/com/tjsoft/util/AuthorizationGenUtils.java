package com.tjsoft.util;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by TYKY001 on 2017/7/31.
 */

public class AuthorizationGenUtils {
    //获得随机nonce,最好是32位的uuid
    public static synchronized String getUUID(){
        UUID uuid=UUID.randomUUID();
        String str = uuid.toString();
        String nonce=str.replace("-", "");
        return nonce;
    }
    //将timestamp、nonce、API_KEY 这三个字符串进行升序排列（依据字符串首位字符的ASCII码)，并join成一个字符串
    public static String genjoinstr(String timestamp,String nonce,String API_KEY){
        ArrayList<String> beforesort = new ArrayList<String>();
        beforesort.add(API_KEY);
        beforesort.add(timestamp);
        beforesort.add(nonce);

        Collections.sort(beforesort, new SpellComparator());
        StringBuffer aftersort = new StringBuffer();
        for (int i = 0; i < beforesort.size(); i++) {
            aftersort.append(beforesort.get(i));
        }

        String join_str = aftersort.toString();
        return join_str;
    }
    public static String genEncryptString(String join_str, String API_SECRET){

        Key sk = new SecretKeySpec(API_SECRET.getBytes(), "HmacSHA256");
        Mac mac = null;
        try {
            mac = Mac.getInstance(sk.getAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        try {
            mac.init(sk);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        final byte[] hmac = mac.doFinal(join_str.getBytes());//完成hamc-sha256签名
        StringBuilder sb = new StringBuilder(hmac.length * 2);
        Formatter formatter = new Formatter(sb);
        for (byte b : hmac) {
            formatter.format("%02x", b);
        }
        String signature = sb.toString();//完成16进制编码
        return signature;
    }
    //返回签名认证字符串：
    public static String genauthorization(String API_KEY, String timestamp, String nonce, String signature){

        String authorization = "key=" + API_KEY
                +",timestamp=" + timestamp
                +",nonce=" + nonce
                +",signature=" + signature;
        return authorization;
    }
    //一步到位返回认证字符串
    public static String getAuthoriztionStr() {
        String API_KEY = "a2fb0d8579f345e0901b83f0c3f57f2c";
        String API_SECRET = "6bf262d3bd76456aa29f829f8b87c739";
        String nonce = getUUID();
        String timestamp =  System.currentTimeMillis() + "";
        String signature = genEncryptString(genjoinstr(timestamp,nonce,API_KEY),API_SECRET);
        return genauthorization(API_KEY,timestamp,nonce,signature);
    }
}
