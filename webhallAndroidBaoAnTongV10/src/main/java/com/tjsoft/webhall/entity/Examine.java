package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 审查交互bean
 *
 */
public class Examine implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1002352359L;
	private String MSG_RE_ID;
	private String MSG_TITLE;
	private String SEND_TIME;
	private String MSG_SEND_USER_NAME;
	private String  MSG_CONTENT;

	public String getMSG_RE_ID() {
		return MSG_RE_ID;
	}
	public void setMSG_RE_ID(String mSG_RE_ID) {
		MSG_RE_ID = mSG_RE_ID;
	}
	public String getMSG_TITLE() {
		return MSG_TITLE;
	}
	public void setMSG_TITLE(String mSG_TITLE) {
		MSG_TITLE = mSG_TITLE;
	}
	public String getSEND_TIME() {
		return SEND_TIME;
	}
	public void setSEND_TIME(String sEND_TIME) {
		SEND_TIME = sEND_TIME;
	}
	public String getMSG_SEND_USER_NAME() {
		return MSG_SEND_USER_NAME;
	}
	public void setMSG_SEND_USER_NAME(String mSG_SEND_USER_NAME) {
		MSG_SEND_USER_NAME = mSG_SEND_USER_NAME;
	}
	public String getMSG_CONTENT() {
		return MSG_CONTENT;
	}
	public void setMSG_CONTENT(String mSG_CONTENT) {
		MSG_CONTENT = mSG_CONTENT;
	}

}
