package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 办事窗口
 * @author Administrator
 *
 */
public class Window implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100887232533L;
	private String ID;
	private String NAME;
	private String ADDRESS;
	private String PHONE;
	private String OFFICEHOURS;
	private String TRAFFIC;
	private String LONGITUDE;
	private String LATITUDE;
	
	
	
	
	public Window() {
		super();
	}
	
	public Window(String iD, String nAME, String aDDRESS, String pHONE, String oFFICEHOURS, String tRAFFIC, String lONGITUDE, String lATITUDE) {
		super();
		ID = iD;
		NAME = nAME;
		ADDRESS = aDDRESS;
		PHONE = pHONE;
		OFFICEHOURS = oFFICEHOURS;
		TRAFFIC = tRAFFIC;
		LONGITUDE = lONGITUDE;
		LATITUDE = lATITUDE;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
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
	public String getOFFICEHOURS() {
		return OFFICEHOURS;
	}
	public void setOFFICEHOURS(String oFFICEHOURS) {
		OFFICEHOURS = oFFICEHOURS;
	}
	public String getTRAFFIC() {
		return TRAFFIC;
	}
	public void setTRAFFIC(String tRAFFIC) {
		TRAFFIC = tRAFFIC;
	}
	public String getLONGITUDE() {
		return LONGITUDE;
	}
	public void setLONGITUDE(String lONGITUDE) {
		LONGITUDE = lONGITUDE;
	}
	public String getLATITUDE() {
		return LATITUDE;
	}
	public void setLATITUDE(String lATITUDE) {
		LATITUDE = lATITUDE;
	}
	

}
