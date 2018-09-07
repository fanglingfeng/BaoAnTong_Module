package com.tjsoft.webhall.entity;

import java.io.Serializable;

	/**
	 * 区域实体类
	 * @author Administrator
	 *
	 */
public class Region implements Serializable{
	
	
	private static final long serialVersionUID = -100023520356L;
	private String AREAID;
	private String AREANAME;
	private String PARENTID;
	private String LAYER;
 
	
	public Region() {
		super();
	}
	public Region(String aREAID, String aREANAME, String pARENTID, String lAYER) {
		super();
		AREAID = aREAID;
		AREANAME = aREANAME;
		PARENTID = pARENTID;
		LAYER = lAYER;
	}
	public String getAREAID() {
		return AREAID;
	}
	public void setAREAID(String aREAID) {
		AREAID = aREAID;
	}
	public String getAREANAME() {
		return AREANAME;
	}
	public void setAREANAME(String aREANAME) {
		AREANAME = aREANAME;
	}
	public String getPARENTID() {
		return PARENTID;
	}
	public void setPARENTID(String pARENTID) {
		PARENTID = pARENTID;
	}
	public String getLAYER() {
		return LAYER;
	}
	public void setLAYER(String lAYER) {
		LAYER = lAYER;
	}
	
}
