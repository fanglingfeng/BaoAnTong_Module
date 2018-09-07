package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 预约数量实体类
 * @author Administrator
 *
 */
public class ReserveCount implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100089000023L;
	private String RESERVETIME;
	private String RESERVENUM;
	private String NOWNUM;
	public ReserveCount(String rESERVETIME, String rESERVENUM, String nOWNUM) {
		super();
		RESERVETIME = rESERVETIME;
		RESERVENUM = rESERVENUM;
		NOWNUM = nOWNUM;
	}
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
