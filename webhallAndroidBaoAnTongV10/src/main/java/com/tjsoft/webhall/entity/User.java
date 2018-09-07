package com.tjsoft.webhall.entity;

import java.io.Serializable;
/**
 * 用户实体类
 * @author Administrator
 *
 */
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -100889989898L;


	/**
	 * USER_ID : 1580908
	 * USERNAME : f71cbc7cd70645d99f44355307dd89c2
	 * REALNAME : 向含巧
	 * TOKEN : 0677F651-C326-4979-B358-6EA17FF65367
	 * EMAIL : null
	 * MOBILE : 13900000003
	 * TYPE : 1
	 * CODE : 330922197908242188
	 * AUTHLEVEL : 1
	 * USER_IMG : null
	 * COMPANY : null
	 */

	private String USER_ID;
	private String USERNAME;
	private String REALNAME;
	private String TOKEN;
	private Object EMAIL;
	private String MOBILE;
	private String TYPE;
	private String CODE;
	private String AUTHLEVEL;
	private Object USER_IMG;
	private Object COMPANY;

	public String getUSER_ID() {
		return USER_ID;
	}

	public void setUSER_ID(String USER_ID) {
		this.USER_ID = USER_ID;
	}

	public String getUSERNAME() {
		return USERNAME;
	}

	public void setUSERNAME(String USERNAME) {
		this.USERNAME = USERNAME;
	}

	public String getREALNAME() {
		return REALNAME;
	}

	public void setREALNAME(String REALNAME) {
		this.REALNAME = REALNAME;
	}

	public String getTOKEN() {
		return TOKEN;
	}

	public void setTOKEN(String TOKEN) {
		this.TOKEN = TOKEN;
	}

	public Object getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(Object EMAIL) {
		this.EMAIL = EMAIL;
	}

	public String getMOBILE() {
		return MOBILE;
	}

	public void setMOBILE(String MOBILE) {
		this.MOBILE = MOBILE;
	}

	public String getTYPE() {
		return TYPE;
	}

	public void setTYPE(String TYPE) {
		this.TYPE = TYPE;
	}

	public String getCODE() {
		return CODE;
	}

	public void setCODE(String CODE) {
		this.CODE = CODE;
	}

	public String getAUTHLEVEL() {
		return AUTHLEVEL;
	}

	public void setAUTHLEVEL(String AUTHLEVEL) {
		this.AUTHLEVEL = AUTHLEVEL;
	}

	public Object getUSER_IMG() {
		return USER_IMG;
	}

	public void setUSER_IMG(Object USER_IMG) {
		this.USER_IMG = USER_IMG;
	}

	public Object getCOMPANY() {
		return COMPANY;
	}

	public void setCOMPANY(Object COMPANY) {
		this.COMPANY = COMPANY;
	}

	@Override
	public String toString() {
		return "User{" +
				"USER_ID='" + USER_ID + '\'' +
				", USERNAME='" + USERNAME + '\'' +
				", REALNAME='" + REALNAME + '\'' +
				", TOKEN='" + TOKEN + '\'' +
				", EMAIL=" + EMAIL +
				", MOBILE='" + MOBILE + '\'' +
				", TYPE='" + TYPE + '\'' +
				", CODE='" + CODE + '\'' +
				", AUTHLEVEL='" + AUTHLEVEL + '\'' +
				", USER_IMG=" + USER_IMG +
				", COMPANY=" + COMPANY +
				'}';
	}
}
