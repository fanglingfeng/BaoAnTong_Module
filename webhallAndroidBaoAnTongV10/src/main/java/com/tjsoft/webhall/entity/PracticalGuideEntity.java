package com.tjsoft.webhall.entity;

import java.io.Serializable;

public class PracticalGuideEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -15699235231L;
	private String ID;
	private String PERMID;
	private String PERMVER;
	private String PRACTICAL;
	private String CHANGETYPE;
	private String ISCHANGE;
	private String ORDERID;
	
	public PracticalGuideEntity() {}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPERMID() {
		return PERMID;
	}

	public void setPERMID(String pERMID) {
		PERMID = pERMID;
	}

	public String getPERMVER() {
		return PERMVER;
	}

	public void setPERMVER(String pERMVER) {
		PERMVER = pERMVER;
	}

	public String getPRACTICAL() {
		return PRACTICAL;
	}

	public void setPRACTICAL(String pRACTICAL) {
		PRACTICAL = pRACTICAL;
	}

	public String getCHANGETYPE() {
		return CHANGETYPE;
	}

	public void setCHANGETYPE(String cHANGETYPE) {
		CHANGETYPE = cHANGETYPE;
	}

	public String getISCHANGE() {
		return ISCHANGE;
	}

	public void setISCHANGE(String iSCHANGE) {
		ISCHANGE = iSCHANGE;
	}

	public String getORDERID() {
		return ORDERID;
	}

	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}

}
