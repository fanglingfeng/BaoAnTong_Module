package com.tjsoft.webhall.entity;

import java.io.Serializable;

public class BaoanNew implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -108329742234L;
	private String CONTENT_ID;
	private String TITLE;
	private String AUTHOR;
	private String RELEASE_DATE;
	private String DESCRIPTION;
	private String TXT;
	private String TITLE_IMG;
	
	public BaoanNew() {}

	public String getCONTENT_ID() {
		return CONTENT_ID;
	}

	public void setCONTENT_ID(String cONTENT_ID) {
		CONTENT_ID = cONTENT_ID;
	}

	public String getTITLE() {
		return TITLE;
	}

	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}

	public String getAUTHOR() {
		return AUTHOR;
	}

	public void setAUTHOR(String aUTHOR) {
		AUTHOR = aUTHOR;
	}

	public String getRELEASE_DATE() {
		return RELEASE_DATE;
	}

	public void setRELEASE_DATE(String rELEASE_DATE) {
		RELEASE_DATE = rELEASE_DATE;
	}

	public String getDESCRIPTION() {
		return DESCRIPTION;
	}

	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}

	public String getTXT() {
		return TXT;
	}

	public void setTXT(String tXT) {
		TXT = tXT;
	}

	public String getTITLE_IMG() {
		return TITLE_IMG;
	}

	public void setTITLE_IMG(String tITLE_IMG) {
		TITLE_IMG = tITLE_IMG;
	}

}
