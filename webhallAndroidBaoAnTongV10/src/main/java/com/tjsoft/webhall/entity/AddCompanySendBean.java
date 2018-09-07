package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 *
 * @author Administrator
 *
 */
public class AddCompanySendBean implements Serializable {


    /**
     * INC_EXT_ID : 主键ID
     * INC_NAME : 深圳太极云软技术股份有限公司
     * INC_TYSHXYDM : 123456789
     * INC_DEPUTY : 张三
     * INC_PID : 612401123345661231
     * ISREALNAME : 1
     * AUTHLEVEL : 2
     * USERID : 123456789
     */

    private String INC_EXT_ID;
    private String INC_NAME;
    private String INC_TYSHXYDM;
    private String INC_DEPUTY;
    private String INC_PID;
    private String ISREALNAME;
    private String AUTHLEVEL;
    private String USERID;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String token;

    public String getDEPUTY_ISREALNAME() {
        return DEPUTY_ISREALNAME;
    }

    public void setDEPUTY_ISREALNAME(String DEPUTY_ISREALNAME) {
        this.DEPUTY_ISREALNAME = DEPUTY_ISREALNAME;
    }

    private String DEPUTY_ISREALNAME;

    public String getISDEFAULT() {
        return ISDEFAULT;
    }

    public void setISDEFAULT(String ISDEFAULT) {
        this.ISDEFAULT = ISDEFAULT;
    }

    private String ISDEFAULT;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;

    public String getINC_EXT_ID() {
        return INC_EXT_ID;
    }

    public void setINC_EXT_ID(String INC_EXT_ID) {
        this.INC_EXT_ID = INC_EXT_ID;
    }

    public String getINC_NAME() {
        return INC_NAME;
    }

    public void setINC_NAME(String INC_NAME) {
        this.INC_NAME = INC_NAME;
    }

    public String getINC_TYSHXYDM() {
        return INC_TYSHXYDM;
    }

    public void setINC_TYSHXYDM(String INC_TYSHXYDM) {
        this.INC_TYSHXYDM = INC_TYSHXYDM;
    }

    public String getINC_DEPUTY() {
        return INC_DEPUTY;
    }

    public void setINC_DEPUTY(String INC_DEPUTY) {
        this.INC_DEPUTY = INC_DEPUTY;
    }

    public String getINC_PID() {
        return INC_PID;
    }

    public void setINC_PID(String INC_PID) {
        this.INC_PID = INC_PID;
    }

    public String getISREALNAME() {
        return ISREALNAME;
    }

    public void setISREALNAME(String ISREALNAME) {
        this.ISREALNAME = ISREALNAME;
    }

    public String getAUTHLEVEL() {
        return AUTHLEVEL;
    }

    public void setAUTHLEVEL(String AUTHLEVEL) {
        this.AUTHLEVEL = AUTHLEVEL;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }
}
