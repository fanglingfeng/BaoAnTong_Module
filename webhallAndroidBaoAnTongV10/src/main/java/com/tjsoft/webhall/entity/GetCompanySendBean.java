package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 材料信息实体
 * @author Administrator
 *
 */
public class GetCompanySendBean implements Serializable {
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getUSERID() {
		return USERID;
	}

	public void setUSERID(String USERID) {
		this.USERID = USERID;
	}

	/**
	 * 
	 */
	private String token;
	private String USERID;
}
