package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 版本信息
 * @author Administrator
 *
 */
public class Version implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1789987323232L;
	private String LATESTVERSION;
	private String URL;
	private String DESCRIPTION;
	public String getLATESTVERSION() {
		return LATESTVERSION;
	}
	public void setLATESTVERSION(String lATESTVERSION) {
		LATESTVERSION = lATESTVERSION;
	}
	public String getURL() {
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	

}
