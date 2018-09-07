package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;


/**
 * 获取办事材料 材料信息实体
 * @author Administrator
 *
 */
public class ApplyBean implements Serializable{

	private static final long serialVersionUID = 1L;
	private String CASEID;
	private String ID;
	private String CLBH;
	private String CLMC;
	private String DZHYQ;
	private String ORINUM;
	private String COPYNUM;
	private String SFBY;
	private String STATUS;
	private String FILEID;
	private String FILENAME;
	private String FILEPATH;
	private String FILEDEL;
	private String SH;//审核
	private String SHYJ;//审核意见
	private List<ATT> ATTS;
	private String CERTIFICATEID;
	private String CERTIFICATESTARTDATE;
	private String CERTIFICATEENDDATE;
	private String FORMID;
	private String FORMVER;
	private String YQ;

	private String CERTCODE;// 证照编号
	private String CERTSTARTTIME;
	private String CERTENDTIME;

	public boolean isIsselected() {
		return isselected;
	}

	public void setIsselected(boolean isselected) {
		this.isselected = isselected;
	}

	private boolean isselected = false;


	private String FILE_URL;//空表地址

	public String getFILE_URL() {
		return FILE_URL;
	}

	public void setFILE_URL(String FILE_URL) {
		this.FILE_URL = FILE_URL;
	}

	public String getSAMPLE_FILE() {
		return SAMPLE_FILE;
	}

	public void setSAMPLE_FILE(String SAMPLE_FILE) {
		this.SAMPLE_FILE = SAMPLE_FILE;
	}

	private String SAMPLE_FILE;//样表地址


	public ApplyBean() {}

	public ApplyBean(String cASEID, String iD, String cLBH, String cLMC, String dZHYQ, String oRINUM, String cOPYNUM, String sFBY, String sTATUS, String fILEID, String fILENAME, String fILEPATH, String fILEDEL, String sH, String sHYJ, String cERTIFICATEID, String cERTIFICATESTARTDATE, String cERTIFICATEENDDATE, String fORMID, String fORMVER) {
		super();
		CASEID = cASEID;
		ID = iD;
		CLBH = cLBH;
		CLMC = cLMC;
		DZHYQ = dZHYQ;
		ORINUM = oRINUM;
		COPYNUM = cOPYNUM;
		SFBY = sFBY;
		STATUS = sTATUS;
		FILEID = fILEID;
		FILENAME = fILENAME;
		FILEPATH = fILEPATH;
		FILEDEL = fILEDEL;
		SH = sH;
		SHYJ = sHYJ;
		CERTIFICATEID = cERTIFICATEID;
		CERTIFICATESTARTDATE = cERTIFICATESTARTDATE;
		CERTIFICATEENDDATE = cERTIFICATEENDDATE;
		FORMID = fORMID;
		FORMVER = fORMVER;
	}






	public String getCERTCODE() {
		return CERTCODE;
	}

	public void setCERTCODE(String cERTCODE) {
		CERTCODE = cERTCODE;
	}

	public String getCERTSTARTTIME() {
		return CERTSTARTTIME;
	}

	public void setCERTSTARTTIME(String cERTSTARTTIME) {
		CERTSTARTTIME = cERTSTARTTIME;
	}

	public String getCERTENDTIME() {
		return CERTENDTIME;
	}

	public void setCERTENDTIME(String cERTENDTIME) {
		CERTENDTIME = cERTENDTIME;
	}

	public String getFORMID() {
		return FORMID;
	}

	public void setFORMID(String fORMID) {
		FORMID = fORMID;
	}

	public String getFORMVER() {
		return FORMVER;
	}

	public void setFORMVER(String fORMVER) {
		FORMVER = fORMVER;
	}

	public String getSH() {
		return SH;
	}
	public void setSH(String sH) {
		SH = sH;
	}
	public String getSHYJ() {
		return SHYJ;
	}
	public void setSHYJ(String sHYJ) {
		SHYJ = sHYJ;
	}
	public List<ATT> getATTS() {
		return ATTS;
	}
	public void setATTS(List<ATT> aTTS) {
		ATTS = aTTS;
	}
	public String getCASEID() {
		return CASEID;
	}
	public void setCASEID(String cASEID) {
		CASEID = cASEID;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCLBH() {
		return CLBH;
	}
	public void setCLBH(String cLBH) {
		CLBH = cLBH;
	}
	public String getCLMC() {
		return CLMC;
	}
	public void setCLMC(String cLMC) {
		CLMC = cLMC;
	}
	public String getDZHYQ() {
		return DZHYQ;
	}
	public void setDZHYQ(String dZHYQ) {
		DZHYQ = dZHYQ;
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
	public String getSFBY() {
		return SFBY;
	}
	public void setSFBY(String sFBY) {
		SFBY = sFBY;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getFILEID() {
		return FILEID;
	}
	public void setFILEID(String fILEID) {
		FILEID = fILEID;
	}
	public String getFILENAME() {
		return FILENAME;
	}
	public void setFILENAME(String fILENAME) {
		FILENAME = fILENAME;
	}
	public String getFILEPATH() {
		return FILEPATH;
	}
	public void setFILEPATH(String fILEPATH) {
		FILEPATH = fILEPATH;
	}
	public String getFILEDEL() {
		return FILEDEL;
	}
	public void setFILEDEL(String fILEDEL) {
		FILEDEL = fILEDEL;
	}
	public String getCERTIFICATEID() {
		return CERTIFICATEID;
	}

	public void setCERTIFICATEID(String cERTIFICATEID) {
		CERTIFICATEID = cERTIFICATEID;
	}

	public String getCERTIFICATESTARTDATE() {
		return CERTIFICATESTARTDATE;
	}

	public void setCERTIFICATESTARTDATE(String cERTIFICATESTARTDATE) {
		CERTIFICATESTARTDATE = cERTIFICATESTARTDATE;
	}

	public String getCERTIFICATEENDDATE() {
		return CERTIFICATEENDDATE;
	}

	public void setCERTIFICATEENDDATE(String cERTIFICATEENDDATE) {
		CERTIFICATEENDDATE = cERTIFICATEENDDATE;
	}


	public String getYQ() {
		return YQ;
	}

	public void setYQ(String yQ) {
		YQ = yQ;
	}

	@Override
	public String toString() {
		return "ApplyBean [CASEID=" + CASEID + ", ID=" + ID + ", CLBH=" + CLBH + ", CLMC=" + CLMC + ", DZHYQ=" + DZHYQ + ", ORINUM=" + ORINUM + ", COPYNUM=" + COPYNUM + ", SFBY="
				+ SFBY + ", STATUS=" + STATUS + ", FILEID=" + FILEID + ", FILENAME=" + FILENAME + ", FILEPATH=" + FILEPATH + ", FILEDEL=" + FILEDEL + ", SH=" + SH + ", SHYJ="
				+ SHYJ + ", ATTS=" + ATTS + ", CERTIFICATEID=" + CERTIFICATEID + ", CERTIFICATESTARTDATE=" + CERTIFICATESTARTDATE + ", CERTIFICATEENDDATE=" + CERTIFICATEENDDATE
				+ "]";
	}


}
