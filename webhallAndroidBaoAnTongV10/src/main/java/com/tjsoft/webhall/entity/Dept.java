package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * @author Administrator
 *	部门实体类
 */
public class Dept implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 100232534527L;
	private String DEPTID;
	private String NAME;
	private String SHORTNAME;
	private String AREAID;
	private String ORDERID;
	private String CNUM;
	private String RESERVEONE;
	private String PERMNUM;
	
	
	
	
	
	
	

	public String getPERMNUM() {
		return PERMNUM;
	}
	public void setPERMNUM(String pERMNUM) {
		PERMNUM = pERMNUM;
	}
	public String getRESERVEONE() {
		return RESERVEONE;
	}
	public void setRESERVEONE(String rESERVEONE) {
		RESERVEONE = rESERVEONE;
	}
	public String getCNUM() {
		return CNUM;
	}
	public void setCNUM(String cNUM) {
		CNUM = cNUM;
	}
	public String getDEPTID() {
		return DEPTID;
	}
	public void setDEPTID(String dEPTID) {
		DEPTID = dEPTID;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getSHORTNAME() {
		return SHORTNAME;
	}
	public void setSHORTNAME(String sHORTNAME) {
		SHORTNAME = sHORTNAME;
	}
	public String getAREAID() {
		return AREAID;
	}
	public void setAREAID(String aREAID) {
		AREAID = aREAID;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}
	
	
}
