package com.tjsoft.webhall.entity;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.entity
 *  @文件名:   DZZZKSendbean
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 17:21
 *  @描述：    TODO
 */

public class DZZZKUrlSendbean {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAUTHCODE() {
        return AUTHCODE;
    }

    public void setAUTHCODE(String AUTHCODE) {
        this.AUTHCODE = AUTHCODE;
    }

    private String token;
    private String AUTHCODE;


}
