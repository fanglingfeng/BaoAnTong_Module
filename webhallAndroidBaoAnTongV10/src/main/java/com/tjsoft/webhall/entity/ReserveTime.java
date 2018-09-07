package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 预约时间
 * @author Administrator
 *
 */
public class ReserveTime implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100099991235L;
	private String RESERVETIME;
	private String RESERVENUM;
	private String NOWNUM;
	public String getRESERVETIME() {
		return RESERVETIME;
	}
	public void setRESERVETIME(String rESERVETIME) {
		RESERVETIME = rESERVETIME;
	}
	public String getRESERVENUM() {
		return RESERVENUM;
	}
	public void setRESERVENUM(String rESERVENUM) {
		RESERVENUM = rESERVENUM;
	}
	public String getNOWNUM() {
		return NOWNUM;
	}
	public void setNOWNUM(String nOWNUM) {
		NOWNUM = nOWNUM;
	}
	
}
