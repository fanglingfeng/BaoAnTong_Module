package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;

public class Preaccept implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100280026356L;
	private List<CLXX> CLXXS;
	private String ADDRESS;
	private String PHONE;
	private String TRAFFIC;
	public List<CLXX> getCLXXS() {
		return CLXXS;
	}
	public void setCLXXS(List<CLXX> cLXXS) {
		CLXXS = cLXXS;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getPHONE() {
		return PHONE;
	}
	public void setPHONE(String pHONE) {
		PHONE = pHONE;
	}
	public String getTRAFFIC() {
		return TRAFFIC;
	}
	public void setTRAFFIC(String tRAFFIC) {
		TRAFFIC = tRAFFIC;
	}
	
}
