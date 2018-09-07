package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 材料信息实体
 * @author Administrator
 *
 */
public class CLXX implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100101010132L;
	private String NO;
	private String CLMC;
	private String TJFS;
	private String ORINUM;
	private String COPYNUM;
	public String getNO() {
		return NO;
	}
	public void setNO(String nO) {
		NO = nO;
	}
	public String getCLMC() {
		return CLMC;
	}
	public void setCLMC(String cLMC) {
		CLMC = cLMC;
	}
	public String getTJFS() {
		return TJFS;
	}
	public void setTJFS(String tJFS) {
		TJFS = tJFS;
	}
	public String getORINUM() {
		return ORINUM;
	}
	public void setORINUM(String oRINUM) {
		ORINUM = oRINUM;
	}
	public String getCOPYNUM() {
		return COPYNUM;
	}
	public void setCOPYNUM(String cOPYNUM) {
		COPYNUM = cOPYNUM;
	}
	
}
