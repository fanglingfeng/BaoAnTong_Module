package com.tjsoft.webhall.entity;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.entity
 *  @文件名:   DZZZKSendbean
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 17:21
 *  @描述：    TODO
 */

import java.util.List;

public class DZZZKBean {


    /**
     * ZZDATA : [{"AUTHCODE":"20180607092113173NC0252519_44030020180008T6J2","NAME":"深圳经济特区居住证","IDCODE":"445222199903040633","LICENSECODE":"44030020180008T6J2"}]
     * TYPE : 1
     */

    private String TYPE;
    private List<ZZDATABean> ZZDATA;

    public String getTYPE() { return TYPE;}

    public void setTYPE(String TYPE) { this.TYPE = TYPE;}

    public List<ZZDATABean> getZZDATA() { return ZZDATA;}

    public void setZZDATA(List<ZZDATABean> ZZDATA) { this.ZZDATA = ZZDATA;}


}
