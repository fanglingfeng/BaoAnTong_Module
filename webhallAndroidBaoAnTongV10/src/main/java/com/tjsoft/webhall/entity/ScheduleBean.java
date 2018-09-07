package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 流水号搜索结果
 * @author Administrator
 *
 */
public class ScheduleBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -19998823235L;
	private String BSNUM;
	private String SXZXNAME;
	private String DEPTID;
	private String DEPTNAME;
	private String APPNAME;
	private String APPCOMPANY;
	private String APPLYTIME;
	private String STATUS;
	private String CSTATUS;
	private String LIMITDAYS;
	private String PERMID;
	private List<Log> LOGS;
	
	
	
	
	
	public List<Log> getLOGS() {
		return LOGS;
	}
	public void setLOGS(List<Log> lOGS) {
		LOGS = lOGS;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public ScheduleBean(String bSNUM, String sXZXNAME, String dEPTID, String dEPTNAME, String aPPNAME, String aPPCOMPANY, String aPPLYTIME, String sTATUS, String cSTATUS, List<Log> lOGS) {
		super();
		LOGS = lOGS;
		BSNUM = bSNUM;
		SXZXNAME = sXZXNAME;
		DEPTID = dEPTID;
		DEPTNAME = dEPTNAME;
		APPNAME = aPPNAME;
		APPCOMPANY = aPPCOMPANY;
		APPLYTIME = aPPLYTIME;
		STATUS = sTATUS;
		CSTATUS = cSTATUS;
	}
	public ScheduleBean() {
		super();
	}
	public String getBSNUM() {
		return BSNUM;
	}
	public void setBSNUM(String bSNUM) {
		BSNUM = bSNUM;
	}
	public String getSXZXNAME() {
		return SXZXNAME;
	}
	public void setSXZXNAME(String sXZXNAME) {
		SXZXNAME = sXZXNAME;
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
	public String getAPPNAME() {
		return APPNAME;
	}
	public void setAPPNAME(String aPPNAME) {
		APPNAME = aPPNAME;
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
	public String getCSTATUS() {
		return CSTATUS;
	}
	public void setCSTATUS(String cSTATUS) {
		CSTATUS = cSTATUS;
	}
	public String getLIMITDAYS() {
		return LIMITDAYS;
	}
	public void setLIMITDAYS(String lIMITDAYS) {
		LIMITDAYS = lIMITDAYS;
	}
	public String getPERMID() {
		return PERMID;
	}
	public void setPERMID(String pERMID) {
		PERMID = pERMID;
	}
	

}
