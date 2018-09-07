package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 *
 * @author Administrator
 *
 */
public class AddCompanyResponseBean implements Serializable {




    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public String getDEPUTY_ISREALNAME() {
        return DEPUTY_ISREALNAME;
    }

    public void setDEPUTY_ISREALNAME(String DEPUTY_ISREALNAME) {
        this.DEPUTY_ISREALNAME = DEPUTY_ISREALNAME;
    }

    private String INC_EXT_ID;
    private String ISREALNAME;
    private String DEPUTY_ISREALNAME;

}
