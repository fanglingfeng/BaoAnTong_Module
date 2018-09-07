/**
 * PostInfo.java[V 1.0.0]
 * class:com.tjsoft.webhall.entity.PostInfo
 * author Create at 2016-6-1 下午3:24:43
 */
package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 *com.tjsoft.webhall.entity.PostInfo
 * @author 傅成龙 <br/>
 * create at 2016-6-1 下午3:24:43	
 * 邮政速递信息
 */

public class PostInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String POSTID;
	private String RECEIVE;
	private String MOBILE;
	private String PHONE;
	private String ADDRESS;
	private String POSTCODE;
	private String PROVINCE;
	private String CITY;
	private String COUNTRY;

	
	
	public String getPOSTID() {
		return POSTID;
	}
	public void setPOSTID(String pOSTID) {
		POSTID = pOSTID;
	}
	public String getRECEIVE() {
		return RECEIVE;
	}
	public void setRECEIVE(String rECEIVE) {
		RECEIVE = rECEIVE;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getPOSTCODE() {
		return POSTCODE;
	}
	public void setPOSTCODE(String pOSTCODE) {
		POSTCODE = pOSTCODE;
	}

	public String getPROVINCE() {
		return PROVINCE;
	}

	public void setPROVINCE(String PROVINCE) {
		this.PROVINCE = PROVINCE;
	}

	public String getCITY() {
		return CITY;
	}

	public void setCITY(String CITY) {
		this.CITY = CITY;
	}

	public String getCOUNTRY() {
		return COUNTRY;
	}

	public void setCOUNTRY(String COUNTRY) {
		this.COUNTRY = COUNTRY;
	}
}
