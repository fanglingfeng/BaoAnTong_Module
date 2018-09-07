package com.tjsoft.webhall.entity;

import java.io.Serializable;
/**
 * 评价实体类
 * @author Administrator
 *
 */
public class Advice implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100231325323L;
	private String BSNUM;
	private String APPLICANT;
	private String COMPANY;
	private String CREATETIME;
	private String PNAME;
	private String DEPTNAME;
	private String CSTATUS ;
	private String STATUS ;
	private String RESULT;
	private String REMARK;
	
	
	
	
	
	

	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
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
	public String getRESULT() {
		return RESULT;
	}
	public void setRESULT(String rESULT) {
		RESULT = rESULT;
	}

	

}
