package com.tjsoft.webhall.entity;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.entity
 *  @文件名:   DZZZKSendbean
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 17:21
 *  @描述：    TODO
 */

public class DZZZKUrlbean {


    /**
     * ONEOFFURL : https://10.248.77.194:443/license-biz/license/view_license?license_access_token=Z78ff8bf7-7da6-4214-918a-a33ee80081d4
     * TYPE : 1 成功 0 失败
     */

    private String ONEOFFURL;
    private String TYPE;

    public String getONEOFFURL() { return ONEOFFURL;}

    public void setONEOFFURL(String ONEOFFURL) { this.ONEOFFURL = ONEOFFURL;}

    public String getTYPE() { return TYPE;}

    public void setTYPE(String TYPE) { this.TYPE = TYPE;}
}
