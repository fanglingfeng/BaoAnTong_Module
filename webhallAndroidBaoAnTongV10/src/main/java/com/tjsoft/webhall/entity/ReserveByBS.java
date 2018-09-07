package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 根据流水号获取预约信息
 * @author Administrator
 *
 */
public class ReserveByBS implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -100008732352L;
	private String BSNUM;
	private String PNAME;
	private String DEPTID;
	private String DEPTNAME;
	private String APPLICANT;
	private String APPCOMPANY;
	private String APPLYTIME;
	private String STATUS;
	private String RESERVEDATE;
	private String RESERVETIME;
	public String getBSNUM() {
		return BSNUM;
	}
	public void setBSNUM(String bSNUM) {
		BSNUM = bSNUM;
	}
	public String getPNAME() {
		return PNAME;
	}
	public void setPNAME(String pNAME) {
		PNAME = pNAME;
	}
	public String getDEPTID() {
		return DEPTID;
	}
	public void setDEPTID(String dEPTID) {
		DEPTID = dEPTID;
	}
	public String getDEPTNAME() {
		return DEPTNAME;
	}
	public void setDEPTNAME(String dEPTNAME) {
		DEPTNAME = dEPTNAME;
	}
	public String getAPPLICANT() {
		return APPLICANT;
	}
	public void setAPPLICANT(String aPPLICANT) {
		APPLICANT = aPPLICANT;
	}
	public String getAPPCOMPANY() {
		return APPCOMPANY;
	}
	public void setAPPCOMPANY(String aPPCOMPANY) {
		APPCOMPANY = aPPCOMPANY;
	}
	public String getAPPLYTIME() {
		return APPLYTIME;
	}
	public void setAPPLYTIME(String aPPLYTIME) {
		APPLYTIME = aPPLYTIME;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getRESERVEDATE() {
		return RESERVEDATE;
	}
	public void setRESERVEDATE(String rESERVEDATE) {
		RESERVEDATE = rESERVEDATE;
	}
	public String getRESERVETIME() {
		return RESERVETIME;
	}
	public void setRESERVETIME(String rESERVETIME) {
		RESERVETIME = rESERVETIME;
	}
	

}
