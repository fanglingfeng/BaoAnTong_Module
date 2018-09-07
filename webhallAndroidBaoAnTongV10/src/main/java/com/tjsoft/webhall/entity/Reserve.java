package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 预约实体类
 * @author Administrator
 *
 */
public class Reserve implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -10020020099873L;
	private String BSNUM;
	private String APPLICANT;
	private String COMPANY;
	private String CREATETIME;
	private String PNAME;
	private String DEPTNAME;
	private String STATUS ;
	private String RESERVEDATE;
	private String RESERVETIME;
	public String getBSNUM() {
		return BSNUM;
	}
	public void setBSNUM(String bSNUM) {
		BSNUM = bSNUM;
	}
	public String getAPPLICANT() {
		return APPLICANT;
	}
	public void setAPPLICANT(String aPPLICANT) {
		APPLICANT = aPPLICANT;
	}
	public String getCOMPANY() {
		return COMPANY;
	}
	public void setCOMPANY(String cOMPANY) {
		COMPANY = cOMPANY;
	}
	public String getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(String cREATETIME) {
		CREATETIME = cREATETIME;
	}
	public String getPNAME() {
		return PNAME;
	}
	public void setPNAME(String pNAME) {
		PNAME = pNAME;
	}
	public String getDEPTNAME() {
		return DEPTNAME;
	}
	public void setDEPTNAME(String dEPTNAME) {
		DEPTNAME = dEPTNAME;
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
