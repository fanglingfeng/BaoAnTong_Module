package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 返回表单实体
 * @author Administrator
 *
 */
public class Form implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -10002356784L;
	private String ID;
	private String FORMVER;
	private String NAME;
	private String FORMTYPE;
	private String XML;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getFORMVER() {
		return FORMVER;
	}
	public void setFORMVER(String fORMVER) {
		FORMVER = fORMVER;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getFORMTYPE() {
		return FORMTYPE;
	}
	public void setFORMTYPE(String fORMTYPE) {
		FORMTYPE = fORMTYPE;
	}
	public String getXML() {
		return XML;
	}
	public void setXML(String xML) {
		XML = xML;
	}
	
	

}
