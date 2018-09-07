package com.tjsoft.webhall.entity;

/*
 *  @项目名：  baoan 
 *  @包名：    com.tjsoft.webhall.entity
 *  @文件名:   DZZZKSendbean
 *  @创建者:   lingfeng
 *  @创建时间:  2018/6/6 17:21
 *  @描述：    TODO
 */

public class XKZKJSendbean {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUSERCODE() {
        return USERCODE;
    }

    public void setUSERCODE(String USERCODE) {
        this.USERCODE = USERCODE;
    }

    private String token;
    private String USERCODE;
    private String PAGENO;

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    private String NAME;

    public String getPAGENO() {
        return PAGENO;
    }

    public void setPAGENO(String PAGENO) {
        this.PAGENO = PAGENO;
    }

    public String getPAGESIZE() {
        return PAGESIZE;
    }

    public void setPAGESIZE(String PAGESIZE) {
        this.PAGESIZE = PAGESIZE;
    }

    private String PAGESIZE;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    private String ID;

    public String getCERTCODE() {
        return CERTCODE;
    }

    public void setCERTCODE(String CERTCODE) {
        this.CERTCODE = CERTCODE;
    }

    private String CERTCODE;
    private String CERTNAME;

    public String getCERTNAME() {
        return CERTNAME;
    }

    public void setCERTNAME(String CERTNAME) {
        this.CERTNAME = CERTNAME;
    }

    public String getDATAXML() {
        return DATAXML;
    }

    public void setDATAXML(String DATAXML) {
        this.DATAXML = DATAXML;
    }

    public String getCERTFILES() {
        return CERTFILES;
    }

    public void setCERTFILES(String CERTFILES) {
        this.CERTFILES = CERTFILES;
    }

    private String DATAXML;
    private String CERTFILES;


}
