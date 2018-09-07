package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class EditCompanyBean implements Serializable {


    /**
     * INC_EXT_ID : 201805021542583314
     * ISREALNAME : 0
     * msg : 公司名称输入错误，或该公司非深圳市注册，实名失败
     */

    private String INC_EXT_ID;
    private String ISREALNAME;
    private String msg;

    public String getINC_EXT_ID() {
        return INC_EXT_ID;
    }

    public void setINC_EXT_ID(String INC_EXT_ID) {
        this.INC_EXT_ID = INC_EXT_ID;
    }

    public String getISREALNAME() {
        return ISREALNAME;
    }

    public void setISREALNAME(String ISREALNAME) {
        this.ISREALNAME = ISREALNAME;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
