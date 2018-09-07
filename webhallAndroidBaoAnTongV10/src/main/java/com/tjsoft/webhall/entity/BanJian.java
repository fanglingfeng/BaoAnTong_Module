package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 办件实体
 * @author Administrator
 *
 */
public class BanJian implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -10016235322L;
	private String BSNUM;
	private String APPLICANT;
	private String COMPANY;
	private String CREATETIME;
	private String PERMID;
	private String PNAME;
	private String DEPTNAME;
	private String CSTATUS ;
	private String STATUS ;

	public String getSFFZBD() {
		return SFFZBD;
	}

	public void setSFFZBD(String SFFZBD) {
		this.SFFZBD = SFFZBD;
	}

	private String SFFZBD ;
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
	public String getPERMID() {
		return PERMID;
	}
	public void setPERMID(String pERMID) {
		PERMID = pERMID;
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
	public String getCSTATUS() {
		return CSTATUS;
	}
	public void setCSTATUS(String cSTATUS) {
		CSTATUS = cSTATUS;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	

}
