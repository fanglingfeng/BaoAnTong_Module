package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author 
 *预约时间
 */
public class DateBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -10002233523L;
	private String RESERVEDATE;
	private List<ReserveTime> RESERVETIMES;
	public String getRESERVEDATE() {
		return RESERVEDATE;
	}
	public void setRESERVEDATE(String rESERVEDATE) {
		RESERVEDATE = rESERVEDATE;
	}
	public List<ReserveTime> getRESERVETIMES() {
		return RESERVETIMES;
	}
	public void setRESERVETIMES(List<ReserveTime> rESERVETIMES) {
		RESERVETIMES = rESERVETIMES;
	}
	
}
