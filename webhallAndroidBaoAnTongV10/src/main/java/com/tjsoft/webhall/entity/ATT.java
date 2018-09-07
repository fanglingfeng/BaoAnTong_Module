package com.tjsoft.webhall.entity;

import java.io.Serializable;

public class ATT implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -10013235235L;
	private String ID;
	private String ATTACHNAME;
	private String ATTACHURL;
	private String ATTACHUID;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	private String status;//1.上传中，2.上传成功，3.上传失败

	public ATT() {
		super();
	}
	
	public ATT(String iD, String aTTACHNAME, String aTTACHURL,String aTTACHUID) {
		super();
		ID = iD;
		ATTACHNAME = aTTACHNAME;
		ATTACHURL = aTTACHURL;
		ATTACHUID = aTTACHUID;
	}
	public ATT(String iD, String aTTACHNAME, String aTTACHURL,String aTTACHUID,String status) {
		ID = iD;
		ATTACHNAME = aTTACHNAME;
		ATTACHURL = aTTACHURL;
		ATTACHUID = aTTACHUID;
		this.status = status;
	}
	
	
	
	public String getATTACHUID() {
		return ATTACHUID;
	}

	public void setATTACHUID(String aTTACHUID) {
		ATTACHUID = aTTACHUID;
	}

	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getATTACHNAME() {
		return ATTACHNAME;
	}
	public void setATTACHNAME(String aTTACHNAME) {
		ATTACHNAME = aTTACHNAME;
	}
	public String getATTACHURL() {
		return ATTACHURL;
	}
	public void setATTACHURL(String aTTACHURL) {
		ATTACHURL = aTTACHURL;
	}

}
