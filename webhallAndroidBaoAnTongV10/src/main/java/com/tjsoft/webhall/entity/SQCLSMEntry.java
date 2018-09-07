package com.tjsoft.webhall.entity;

import java.io.Serializable;

import android.R.string;

public class SQCLSMEntry implements Serializable{
	private boolean isShowGuide;//是否显示情形
	private String ID;//: "20170711113530322400007543331",
	private String P_GROUP_ID;//5a268ddd06ee43e680c11dae6ea845d8007543331",
	private String P_GROUP_NAME;//事业单位法人变更登记",
	private String CLID;//c90b72849f094b5ea1ec284d36a7e246",
	private String CLMC;//事业单位法人变更登记申请表",
	private String XUHAO;//"1",
	private String ORINUM;// "2",
	private String COPYNUM;// "0",
	private String FILE_URL;// "/file/upload/file/2017/06/08/181224014_6884.doc?name\u003d事业单位法人证书一证一码信息变更申请表.doc"
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getP_GROUP_ID() {
		return P_GROUP_ID;
	}
	public void setP_GROUP_ID(String p_GROUP_ID) {
		P_GROUP_ID = p_GROUP_ID;
	}
	public String getP_GROUP_NAME() {
		return P_GROUP_NAME;
	}
	
	
	
	public boolean isShowGuide() {
		return isShowGuide;
	}
	public void setShowGuide(boolean isShowGuide) {
		this.isShowGuide = isShowGuide;
	}
	public void setP_GROUP_NAME(String p_GROUP_NAME) {
		P_GROUP_NAME = p_GROUP_NAME;
	}
	public String getCLID() {
		return CLID;
	}
	public void setCLID(String cLID) {
		CLID = cLID;
	}
	public String getCLMC() {
		return CLMC;
	}
	public void setCLMC(String cLMC) {
		CLMC = cLMC;
	}
	public String getXUHAO() {
		return XUHAO;
	}
	public void setXUHAO(String xUHAO) {
		XUHAO = xUHAO;
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
	public String getFILE_URL() {
		return FILE_URL;
	}
	public void setFILE_URL(String fILE_URL) {
		FILE_URL = fILE_URL;
	}
	
	
	
}
