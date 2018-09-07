package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 用户实体类
 * @author Administrator
 *
 */
public class UserPermBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -100889989898L;


	/**
	 * USERTYPE : 0
	 * AUTHLEVEL : 3
	 */

	private String USERTYPE;
	private String AUTHLEVEL;

	public String getSFCXYZ() {
		return SFCXYZ;
	}

	public void setSFCXYZ(String SFCXYZ) {
		this.SFCXYZ = SFCXYZ;
	}

	private String SFCXYZ;

	public String getUSERTYPE() {
		return USERTYPE;
	}

	public void setUSERTYPE(String USERTYPE) {
		this.USERTYPE = USERTYPE;
	}

	public String getAUTHLEVEL() {
		return AUTHLEVEL;
	}

	public void setAUTHLEVEL(String AUTHLEVEL) {
		this.AUTHLEVEL = AUTHLEVEL;
	}
}
