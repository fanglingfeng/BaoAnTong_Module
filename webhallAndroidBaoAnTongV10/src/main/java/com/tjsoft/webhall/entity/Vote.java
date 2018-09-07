package com.tjsoft.webhall.entity;

import java.io.Serializable;

public class Vote implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1980762353L;
	private String VOTEITEM_ID;
	private String VOTEITEM_TITLE;
	private String PRIORITY;
	private String VOTECOUNT;
	
	
	
	
	
	public Vote(String vOTEITEM_ID, String vOTEITEM_TITLE, String pRIORITY, String vOTECOUNT) {
		super();
		VOTEITEM_ID = vOTEITEM_ID;
		VOTEITEM_TITLE = vOTEITEM_TITLE;
		PRIORITY = pRIORITY;
		VOTECOUNT = vOTECOUNT;
	}
	public Vote() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getVOTEITEM_ID() {
		return VOTEITEM_ID;
	}
	public void setVOTEITEM_ID(String vOTEITEM_ID) {
		VOTEITEM_ID = vOTEITEM_ID;
	}
	public String getVOTEITEM_TITLE() {
		return VOTEITEM_TITLE;
	}
	public void setVOTEITEM_TITLE(String vOTEITEM_TITLE) {
		VOTEITEM_TITLE = vOTEITEM_TITLE;
	}
	public String getPRIORITY() {
		return PRIORITY;
	}
	public void setPRIORITY(String pRIORITY) {
		PRIORITY = pRIORITY;
	}
	public String getVOTECOUNT() {
		return VOTECOUNT;
	}
	public void setVOTECOUNT(String vOTECOUNT) {
		VOTECOUNT = vOTECOUNT;
	}

}
