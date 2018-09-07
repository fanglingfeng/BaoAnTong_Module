package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 *
 * @author Administrator
 *
 */
public class InsIncExt
        implements Serializable  {


    /**
     * TYPE : 1:个人办事，2:企业办事
     * INC_EXT_ID : 主键ID
     * INC_NAME : 深圳太极云软技术股份有限公司
     * INC_TYSHXYDM : 123456789
     * INC_DEPUTY : 张三
     * INC_PID : 612401123345661231
     * DEPUTY_ISREALNAME : 0
     * DEPUTY_AUTHLEVEL : 2
     * ISREALNAME : 1
     * AUTHLEVEL : 2
     * USERID : 123456789
     * ISDEFAULT : 1
     * INC_ZZJGDM : 13123456
     * INC_ADDR : 深圳市宝安区
     */

    private String TYPE;
    private String INC_EXT_ID;
    private String INC_NAME;
    private String INC_TYSHXYDM;
    private String INC_DEPUTY;
    private String INC_PID;
    private String DEPUTY_ISREALNAME;
    private String DEPUTY_AUTHLEVEL;
    private String ISREALNAME;
    private String AUTHLEVEL;
    private String USERID;
    private String ISDEFAULT;
    private String INC_ZZJGDM;
    private String INC_ADDR;

    public String getTYPE() { return TYPE;}

    public void setTYPE(String TYPE) { this.TYPE = TYPE;}

    public String getINC_EXT_ID() { return INC_EXT_ID;}

    public void setINC_EXT_ID(String INC_EXT_ID) { this.INC_EXT_ID = INC_EXT_ID;}

    public String getINC_NAME() { return INC_NAME;}

    public void setINC_NAME(String INC_NAME) { this.INC_NAME = INC_NAME;}

    public String getINC_TYSHXYDM() { return INC_TYSHXYDM;}

    public void setINC_TYSHXYDM(String INC_TYSHXYDM) { this.INC_TYSHXYDM = INC_TYSHXYDM;}

    public String getINC_DEPUTY() { return INC_DEPUTY;}

    public void setINC_DEPUTY(String INC_DEPUTY) { this.INC_DEPUTY = INC_DEPUTY;}

    public String getINC_PID() { return INC_PID;}

    public void setINC_PID(String INC_PID) { this.INC_PID = INC_PID;}

    public String getDEPUTY_ISREALNAME() { return DEPUTY_ISREALNAME;}

    public void setDEPUTY_ISREALNAME(String DEPUTY_ISREALNAME) { this.DEPUTY_ISREALNAME = DEPUTY_ISREALNAME;}

    public String getDEPUTY_AUTHLEVEL() { return DEPUTY_AUTHLEVEL;}

    public void setDEPUTY_AUTHLEVEL(String DEPUTY_AUTHLEVEL) { this.DEPUTY_AUTHLEVEL = DEPUTY_AUTHLEVEL;}

    public String getISREALNAME() { return ISREALNAME;}

    public void setISREALNAME(String ISREALNAME) { this.ISREALNAME = ISREALNAME;}

    public String getAUTHLEVEL() { return AUTHLEVEL;}

    public void setAUTHLEVEL(String AUTHLEVEL) { this.AUTHLEVEL = AUTHLEVEL;}

    public String getUSERID() { return USERID;}

    public void setUSERID(String USERID) { this.USERID = USERID;}

    public String getISDEFAULT() { return ISDEFAULT;}

    public void setISDEFAULT(String ISDEFAULT) { this.ISDEFAULT = ISDEFAULT;}

    public String getINC_ZZJGDM() { return INC_ZZJGDM;}

    public void setINC_ZZJGDM(String INC_ZZJGDM) { this.INC_ZZJGDM = INC_ZZJGDM;}

    public String getINC_ADDR() { return INC_ADDR;}

    public void setINC_ADDR(String INC_ADDR) { this.INC_ADDR = INC_ADDR;}
}
