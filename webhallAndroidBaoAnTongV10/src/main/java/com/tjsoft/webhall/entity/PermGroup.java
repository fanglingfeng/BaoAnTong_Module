package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 事项分组
 * @author Administrator
 *
 */
public class PermGroup implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100232502359L;
	private String P_GROUP_ID;
	private String P_GROUP_NAME;
	private String P_ID;
	public String getP_GROUP_ID() {
		return P_GROUP_ID;
	}
	public void setP_GROUP_ID(String p_GROUP_ID) {
		P_GROUP_ID = p_GROUP_ID;
	}
	public String getP_GROUP_NAME() {
		return P_GROUP_NAME;
	}
	public void setP_GROUP_NAME(String p_GROUP_NAME) {
		P_GROUP_NAME = p_GROUP_NAME;
	}
	public String getP_ID() {
		return P_ID;
	}
	public void setP_ID(String p_ID) {
		P_ID = p_ID;
	}
	

}
