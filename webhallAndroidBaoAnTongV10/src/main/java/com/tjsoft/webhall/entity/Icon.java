package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 图标
 * @author Administrator
 *
 */
public class Icon implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1002350327L;
	private String PICTUREID;
	private String PICTURECODE;
	private String PICTURENAME;
	private String PARENTID;
	private String PICTUREPATH;
	private String PICTURETYPE;
	private String ACTIONTYPE;
	private String LINKURL;
	private String PERMCODE;//事项编码
	public String getPICTUREID() {
		return PICTUREID;
	}
	public void setPICTUREID(String pICTUREID) {
		PICTUREID = pICTUREID;
	}
	
	public String getPICTURECODE() {
		return PICTURECODE;
	}
	public void setPICTURECODE(String pICTURECODE) {
		PICTURECODE = pICTURECODE;
	}
	public String getPICTURENAME() {
		return PICTURENAME;
	}
	public void setPICTURENAME(String pICTURENAME) {
		PICTURENAME = pICTURENAME;
	}
	public String getPARENTID() {
		return PARENTID;
	}
	public void setPARENTID(String pARENTID) {
		PARENTID = pARENTID;
	}
	public String getPICTUREPATH() {
		return PICTUREPATH;
	}
	public void setPICTUREPATH(String pICTUREPATH) {
		PICTUREPATH = pICTUREPATH;
	}
	public String getPICTURETYPE() {
		return PICTURETYPE;
	}
	public void setPICTURETYPE(String pICTURETYPE) {
		PICTURETYPE = pICTURETYPE;
	}
	public String getACTIONTYPE() {
		return ACTIONTYPE;
	}
	public void setACTIONTYPE(String aCTIONTYPE) {
		ACTIONTYPE = aCTIONTYPE;
	}
	public String getLINKURL() {
		return LINKURL;
	}
	public void setLINKURL(String lINKURL) {
		LINKURL = lINKURL;
	}
	public String getPERMCODE() {
		return PERMCODE;
	}
	public void setPERMCODE(String pERMCODE) {
		PERMCODE = pERMCODE;
	}

}
