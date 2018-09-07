package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 共享材料
 *
 * @author Administrator
 */
public class ShareMaterial implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -199900032233L;
    private String ATTACHCODE;
    private String ATTACHNAME;
    private String ID;

    private String ATTACHURL;
    private String COMPRESULT;

    private String CERTCODE;// 证照编号
    private String CERTSTARTTIME;
    private String CERTENDTIME;

    private List<ATT> ATTS;

    public List<ATTBean> getATTBeans() {
        return ATTBeans;
    }

    public void setATTBeans(List<ATTBean> ATTBeans) {
        this.ATTBeans = ATTBeans;
    }

    private List<ATTBean> ATTBeans;

    public List<ATT> getATTS() {
        return ATTS;
    }

    public void setATTS(List<ATT> aTTS) {
        ATTS = aTTS;
    }

    public String getATTACHCODE() {
        return ATTACHCODE;
    }

    public void setATTACHCODE(String aTTACHCODE) {
        ATTACHCODE = aTTACHCODE;
    }


    public String getCERTSTARTTIME() {
        return CERTSTARTTIME;
    }

    public void setCERTSTARTTIME(String cERTSTARTTIME) {
        CERTSTARTTIME = cERTSTARTTIME;
    }

    public String getCERTENDTIME() {
        return CERTENDTIME;
    }

    public void setCERTENDTIME(String cERTENDTIME) {
        CERTENDTIME = cERTENDTIME;
    }

    public String getCERTCODE() {
        return CERTCODE;
    }

    public void setCERTCODE(String cERTCODE) {
        CERTCODE = cERTCODE;
    }

    public String getATTACHNAME() {
        return ATTACHNAME;
    }

    public void setATTACHNAME(String aTTACHNAME) {
        ATTACHNAME = aTTACHNAME;
    }

    public String getID() {
        return ID;
    }

    public void setID(String iD) {
        ID = iD;
    }

    public String getATTACHURL() {
        return ATTACHURL;
    }

    public void setATTACHURL(String aTTACHURL) {
        ATTACHURL = aTTACHURL;
    }

    public String getCOMPRESULT() {
        return COMPRESULT;
    }

    public void setCOMPRESULT(String cOMPRESULT) {
        COMPRESULT = cOMPRESULT;
    }

}
