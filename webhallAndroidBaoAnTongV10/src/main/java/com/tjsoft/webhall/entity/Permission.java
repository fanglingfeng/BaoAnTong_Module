package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 事项实体类
 * 
 * @author Administrator
 * 
 */
public class Permission implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -100023500399L;
	private String ID;
	private String LARGEITEMID;
	private String SMALLITEMID;
	private String SXZXNAME;
	private String ITEMVERSION;
	private String ITEMLIMITTIME;
	private String ITEMLIMITUNIT;
	private String REGIONID;
	private String DEPTID;
	private String DEPTNAME;
	private String LAWADDR;
	private String REALADDR;
	private String XZXK;
	private String SFYDSB;
	private String ISRESERVE;//"是否提供网上预约服务，0否，1是"
	private String CITEMID;// 协同事项ID
	private String CITEMVERSION;// 协同事项版本号
	private String CORDERID;// 排序号
	private String STATUS;// 状态
	private String CSTATUS;// 中文状态
	private String CBUSINESSID;// 协同业务流水号
	private String BSNUM;// 业务流水号
	private String USERTYPE;//  //支持申报的用户类型 0：全部，1：个人，2：企业
	private String AUTHLEVEL;// 事项安全等级

	public String getREGIONNAME() {
		return REGIONNAME;
	}

	public void setREGIONNAME(String REGIONNAME) {
		this.REGIONNAME = REGIONNAME;
	}

	private String REGIONNAME;// 地区名称

	private String CODE1;//分类编码
	private String CODE2;//部门编码
	private String CODE3;//事项编码
	private String CODE4;//区域编码（新安  或者西乡  可能为空）
	private String RESERVEONE;//机构性质[1 国家级 ; 2 省级 ;3 地市级; 4 区县级 ;5 乡镇级; 6 社区级]
	private String WSSBDZ;//在线申办服务地址
	private String WSZXDZ;//业务咨询服务地址
	private String JGCXDZ;//结果查询地址
	private String JDCXDZ;//进度查询服务地址
	public Permission() {
		super();
	}

//	public Permission(String iD, String lARGEITEMID, String sMALLITEMID, String sXZXNAME, String iTEMVERSION, String iTEMLIMITTIME, String iTEMLIMITUNIT, String rEGIONID, String dEPTID, String dEPTNAME, String lAWADDR, String rEALADDR, String xZXK, String sFYDSB,String iSRESERVE, String cITEMID, String cITEMVERSION, String cORDERID, String sTATUS, String cSTATUS, 
//			String cBUSINESSID, String bSNUM,String CODE1,
//			String RESERVEONE,String WSSBDZ,String WSZXDZ,String JGCXDZ,String JDCXDZ) {
//		super();
//		ID = iD;
//		LARGEITEMID = lARGEITEMID;
//		SMALLITEMID = sMALLITEMID;
//		SXZXNAME = sXZXNAME;
//		ITEMVERSION = iTEMVERSION;
//		ITEMLIMITTIME = iTEMLIMITTIME;
//		ITEMLIMITUNIT = iTEMLIMITUNIT;
//		REGIONID = rEGIONID;
//		DEPTID = dEPTID;
//		DEPTNAME = dEPTNAME;
//		LAWADDR = lAWADDR;
//		REALADDR = rEALADDR;
//		XZXK = xZXK;
//		SFYDSB = sFYDSB;
//		ISRESERVE = iSRESERVE;
//		CITEMID = cITEMID;
//		CITEMVERSION = cITEMVERSION;
//		CORDERID = cORDERID;
//		STATUS = sTATUS;
//		CSTATUS = cSTATUS;
//		CBUSINESSID = cBUSINESSID;
//		BSNUM = bSNUM;
//		this.RESERVEONE=RESERVEONE;
//		this.WSSBDZ=WSSBDZ;
//		this.WSZXDZ=WSZXDZ;
//		this.JGCXDZ=JGCXDZ;
//		this.JDCXDZ=JDCXDZ;
//	}
	
	
	
	

	public Permission(String iD, String lARGEITEMID, String sMALLITEMID, String sXZXNAME, String iTEMVERSION,
			String iTEMLIMITTIME, String iTEMLIMITUNIT, String rEGIONID, String dEPTID, String dEPTNAME, String lAWADDR,
			String rEALADDR, String xZXK, String sFYDSB, String iSRESERVE, String cITEMID, String cITEMVERSION,
			String cORDERID, String sTATUS, String cSTATUS, String cBUSINESSID, String bSNUM, String cODE1,
			String cODE2, String cODE3, String cODE4, String rESERVEONE, String wSSBDZ, String wSZXDZ, String jGCXDZ,
			String jDCXDZ) {
		super();
		ID = iD;
		LARGEITEMID = lARGEITEMID;
		SMALLITEMID = sMALLITEMID;
		SXZXNAME = sXZXNAME;
		ITEMVERSION = iTEMVERSION;
		ITEMLIMITTIME = iTEMLIMITTIME;
		ITEMLIMITUNIT = iTEMLIMITUNIT;
		REGIONID = rEGIONID;
		DEPTID = dEPTID;
		DEPTNAME = dEPTNAME;
		LAWADDR = lAWADDR;
		REALADDR = rEALADDR;
		XZXK = xZXK;
		SFYDSB = sFYDSB;
		ISRESERVE = iSRESERVE;
		CITEMID = cITEMID;
		CITEMVERSION = cITEMVERSION;
		CORDERID = cORDERID;
		STATUS = sTATUS;
		CSTATUS = cSTATUS;
		CBUSINESSID = cBUSINESSID;
		BSNUM = bSNUM;
		CODE1 = cODE1;
		CODE2 = cODE2;
		CODE3 = cODE3;
		CODE4 = cODE4;
		RESERVEONE = rESERVEONE;
		WSSBDZ = wSSBDZ;
		WSZXDZ = wSZXDZ;
		JGCXDZ = jGCXDZ;
		JDCXDZ = jDCXDZ;
	}

	public String getUSERTYPE() {
		return USERTYPE;
	}

	public void setUSERTYPE(String USERTYPE) {
		this.USERTYPE = USERTYPE;
	}

	public String getAUTHLEVEL() {
		return AUTHLEVEL;
	}

	public void setAUTHLEVEL(String AUTHLEVEL) {
		this.AUTHLEVEL = AUTHLEVEL;
	}

	public String getISRESERVE() {
		return ISRESERVE;
	}

	public void setISRESERVE(String iSRESERVE) {
		ISRESERVE = iSRESERVE;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getCBUSINESSID() {
		return CBUSINESSID;
	}

	public void setCBUSINESSID(String cBUSINESSID) {
		CBUSINESSID = cBUSINESSID;
	}

	public String getBSNUM() {
		return BSNUM;
	}

	public void setBSNUM(String bSNUM) {
		BSNUM = bSNUM;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getCSTATUS() {
		return CSTATUS;
	}

	public void setCSTATUS(String cSTATUS) {
		CSTATUS = cSTATUS;
	}

	public String getCITEMID() {
		return CITEMID;
	}

	public void setCITEMID(String cITEMID) {
		CITEMID = cITEMID;
	}

	public String getCITEMVERSION() {
		return CITEMVERSION;
	}

	public void setCITEMVERSION(String cITEMVERSION) {
		CITEMVERSION = cITEMVERSION;
	}

	public String getCORDERID() {
		return CORDERID;
	}

	public void setCORDERID(String cORDERID) {
		CORDERID = cORDERID;
	}

	public String getSFYDSB() {
		return SFYDSB;
	}

	public void setSFYDSB(String sFYDSB) {
		SFYDSB = sFYDSB;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getLARGEITEMID() {
		return LARGEITEMID;
	}

	public void setLARGEITEMID(String lARGEITEMID) {
		LARGEITEMID = lARGEITEMID;
	}

	public String getSMALLITEMID() {
		return SMALLITEMID;
	}

	public void setSMALLITEMID(String sMALLITEMID) {
		SMALLITEMID = sMALLITEMID;
	}

	public String getSXZXNAME() {
		return SXZXNAME;
	}

	public void setSXZXNAME(String sXZXNAME) {
		SXZXNAME = sXZXNAME;
	}

	public String getITEMVERSION() {
		return ITEMVERSION;
	}

	public void setITEMVERSION(String iTEMVERSION) {
		ITEMVERSION = iTEMVERSION;
	}

	public String getITEMLIMITTIME() {
		return ITEMLIMITTIME;
	}

	public void setITEMLIMITTIME(String iTEMLIMITTIME) {
		ITEMLIMITTIME = iTEMLIMITTIME;
	}

	public String getITEMLIMITUNIT() {
		return ITEMLIMITUNIT;
	}

	public void setITEMLIMITUNIT(String iTEMLIMITUNIT) {
		ITEMLIMITUNIT = iTEMLIMITUNIT;
	}

	public String getREGIONID() {
		return REGIONID;
	}

	public void setREGIONID(String rEGIONID) {
		REGIONID = rEGIONID;
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

	public String getLAWADDR() {
		return LAWADDR;
	}

	public void setLAWADDR(String lAWADDR) {
		LAWADDR = lAWADDR;
	}

	public String getREALADDR() {
		return REALADDR;
	}

	public void setREALADDR(String rEALADDR) {
		REALADDR = rEALADDR;
	}

	public String getXZXK() {
		return XZXK;
	}

	public void setXZXK(String xZXK) {
		XZXK = xZXK;
	}

	public String getRESERVEONE() {
		return RESERVEONE;
	}

	public void setRESERVEONE(String rESERVEONE) {
		RESERVEONE = rESERVEONE;
	}

	public String getWSSBDZ() {
		return WSSBDZ;
	}

	public void setWSSBDZ(String wSSBDZ) {
		WSSBDZ = wSSBDZ;
	}

	public String getWSZXDZ() {
		return WSZXDZ;
	}

	public void setWSZXDZ(String wSZXDZ) {
		WSZXDZ = wSZXDZ;
	}

	public String getJGCXDZ() {
		return JGCXDZ;
	}

	public void setJGCXDZ(String jGCXDZ) {
		JGCXDZ = jGCXDZ;
	}

	public String getJDCXDZ() {
		return JDCXDZ;
	}

	public void setJDCXDZ(String jDCXDZ) {
		JDCXDZ = jDCXDZ;
	}
	public String getCODE1() {
		return CODE1;
	}

	public void setCODE1(String cODE1) {
		CODE1 = cODE1;
	}

	public String getCODE2() {
		return CODE2;
	}

	public void setCODE2(String cODE2) {
		CODE2 = cODE2;
	}

	public String getCODE3() {
		return CODE3;
	}

	public void setCODE3(String cODE3) {
		CODE3 = cODE3;
	}

	public String getCODE4() {
		return CODE4;
	}

	public void setCODE4(String cODE4) {
		CODE4 = cODE4;
	}
}
