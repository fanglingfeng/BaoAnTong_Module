package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 协同办事实体
 * @author fuchl
 *
 */
public class CooperateVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -10019923223L;
	private String CFLOWID;//协同流程ID
	private String CFLOWNAME;//协同流程名称
	private String REMARK;//备注
	public String getCFLOWID() {
		return CFLOWID;
	}
	public void setCFLOWID(String cFLOWID) {
		CFLOWID = cFLOWID;
	}
	public String getCFLOWNAME() {
		return CFLOWNAME;
	}
	public void setCFLOWNAME(String cFLOWNAME) {
		CFLOWNAME = cFLOWNAME;
	}
	public String getREMARK() {
		return REMARK;
	}
	public void setREMARK(String rEMARK) {
		REMARK = rEMARK;
	}
	
	

}
