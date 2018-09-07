package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;

public class Topic implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -17799002335L;
	private String VOTETOPIC_ID;
	private String TITLE;
	private String DESCRIPTION;
	private String MULTI_SELECT;
	private String TOTALCOUNT;
	private List<Vote> Items;
	public String getVOTETOPIC_ID() {
		return VOTETOPIC_ID;
	}
	public void setVOTETOPIC_ID(String vOTETOPIC_ID) {
		VOTETOPIC_ID = vOTETOPIC_ID;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public String getMULTI_SELECT() {
		return MULTI_SELECT;
	}
	public void setMULTI_SELECT(String mULTI_SELECT) {
		MULTI_SELECT = mULTI_SELECT;
	}
	public String getTOTALCOUNT() {
		return TOTALCOUNT;
	}
	public void setTOTALCOUNT(String tOTALCOUNT) {
		TOTALCOUNT = tOTALCOUNT;
	}
	public List<Vote> getItems() {
		return Items;
	}
	public void setItems(List<Vote> items) {
		Items = items;
	}

	
	

}
