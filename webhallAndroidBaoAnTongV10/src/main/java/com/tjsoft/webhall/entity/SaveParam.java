package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 用户实体类
 * @author Administrator
 *
 */
public class SaveParam implements Serializable{
	/**
	 * 
	 */


	private String token;

	public String getPID() {
		return PID;
	}

	public void setPID(String PID) {
		this.PID = PID;
	}

	private String PID;
	private String AUTH_TYPE;
	private String USERTYPE;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	public String getAUTH_TYPE() {
		return AUTH_TYPE;
	}

	public void setAUTH_TYPE(String AUTH_TYPE) {
		this.AUTH_TYPE = AUTH_TYPE;
	}

	public String getUSERTYPE() {
		return USERTYPE;
	}

	public void setUSERTYPE(String USERTYPE) {
		this.USERTYPE = USERTYPE;
	}

	public AUTH_MSGparam getAUTH_MSG() {
		return AUTH_MSG;
	}

	public void setAUTH_MSG(AUTH_MSGparam AUTH_MSG) {
		this.AUTH_MSG = AUTH_MSG;
	}

	private AUTH_MSGparam AUTH_MSG;


}
