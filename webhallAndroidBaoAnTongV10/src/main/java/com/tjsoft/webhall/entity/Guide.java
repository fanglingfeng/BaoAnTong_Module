package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 办事指南实体类
 * @author Administrator
 *
 */
public class Guide implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1002356482L;
	private  String ID;
	private  String SXZXNAME;
	private  String DEPTID;
	private  String DEPTNAME;
	private  String XZXK;
	private  String SPTJ;//审批条件
	private  String SQCL;//申请材料
	private  String SPCX;//审批程序
	private  String WLBLLC;//网上办理流程
	private  String LIMITDAYS;//办理时限
	private  String CHARGE;//收费标准
	private  String CJWTJD;//常见问题解答
	private  String LAWPRODUCE;//办理依据
	private  String FORMS;//审批表单
	private  List<Window> WINDOWS;//窗口
	private List<SQCLSMEntry> SQCLSM;
	private Permission PERMISSION;
	private BLMS BLMS;
	
	

	public Guide(String iD, String sXZXNAME, String dEPTID, String dEPTNAME, String xZXK, String sPTJ, String sQCL, String sPCX, String wLBLLC, String lIMITDAYS, String cHARGE, String cJWTJD, String lAWPRODUCE, String fORMS, List<Window> wINDOWS) {
		super();
		ID = iD;
		SXZXNAME = sXZXNAME;
		DEPTID = dEPTID;
		DEPTNAME = dEPTNAME;
		XZXK = xZXK;
		SPTJ = sPTJ;
		SQCL = sQCL;
		SPCX = sPCX;
		WLBLLC = wLBLLC;
		LIMITDAYS = lIMITDAYS;
		CHARGE = cHARGE;
		CJWTJD = cJWTJD;
		LAWPRODUCE = lAWPRODUCE;
		FORMS = fORMS;
		WINDOWS = wINDOWS;
	}

	public Permission getPERMISSION() {
		return PERMISSION;
	}

	public void setPERMISSION(Permission pERMISSION) {
		PERMISSION = pERMISSION;
	}

	public String getFORMS() {
		return FORMS;
	}
	public void setFORMS(String fORMS) {
		FORMS = fORMS;
	}
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getSXZXNAME() {
		return SXZXNAME;
	}
	public void setSXZXNAME(String sXZXNAME) {
		SXZXNAME = sXZXNAME;
	}
	public String getDEPTID() {
		return DEPTID;
	}
	public void setDEPTID(String dEPTID) {
		DEPTID = dEPTID;
	}
	public String getDEPTNAME() {
		return DEPTNAME;
	}
	public void setDEPTNAME(String dEPTNAME) {
		DEPTNAME = dEPTNAME;
	}
	public String getXZXK() {
		return XZXK;
	}
	public void setXZXK(String xZXK) {
		XZXK = xZXK;
	}
	public String getSPTJ() {
		return SPTJ;
	}
	public void setSPTJ(String sPTJ) {
		SPTJ = sPTJ;
	}
	public String getSQCL() {
		return SQCL;
	}
	public void setSQCL(String sQCL) {
		SQCL = sQCL;
	}
	public String getSPCX() {
		return SPCX;
	}
	public void setSPCX(String sPCX) {
		SPCX = sPCX;
	}
	public String getWLBLLC() {
		return WLBLLC;
	}
	public void setWLBLLC(String wLBLLC) {
		WLBLLC = wLBLLC;
	}
	public String getLIMITDAYS() {
		return LIMITDAYS;
	}
	public void setLIMITDAYS(String lIMITDAYS) {
		LIMITDAYS = lIMITDAYS;
	}
	public String getCHARGE() {
		return CHARGE;
	}
	public void setCHARGE(String cHARGE) {
		CHARGE = cHARGE;
	}
	public String getCJWTJD() {
		return CJWTJD;
	}
	public void setCJWTJD(String cJWTJD) {
		CJWTJD = cJWTJD;
	}
	public String getLAWPRODUCE() {
		return LAWPRODUCE;
	}
	public void setLAWPRODUCE(String lAWPRODUCE) {
		LAWPRODUCE = lAWPRODUCE;
	}
	public List<Window> getWINDOWS() {
		return WINDOWS;
	}
	public void setWINDOWS(List<Window> wINDOWS) {
		WINDOWS = wINDOWS;
	}

	public List<SQCLSMEntry> getSQCLSM() {
		return SQCLSM;
	}

	public void setSQCLSM(List<SQCLSMEntry> sQCLSM) {
		SQCLSM = sQCLSM;
	}

	public BLMS getBLMS() {
		return BLMS;
	}

	public void setBLMS(BLMS bLMS) {
		BLMS = bLMS;
	}

}
