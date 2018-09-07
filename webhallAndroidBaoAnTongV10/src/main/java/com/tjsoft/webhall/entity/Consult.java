package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 咨询
 */
public class Consult implements Serializable{
	private static final long serialVersionUID = -11002323232L;
	private String TYPE ;
	private String NAME;
	private String SEX;
	private String MOVEPHONE;
	private String TELEPHONE;
	private String MAINTITLE;
	private String CONTENT;
	private String DEPARTMENTID;
	private String DEPARTMENTNAME;
	private String SXID;
	private String SXMC;
	private String CREATETIME;
	private String SFHF;
	private String HFNR;
	private String MODIFYTIME;
	
	
	
	public Consult(String tYPE, String nAME, String sEX, String mOVEPHONE, String tELEPHONE, String mAINTITLE, String cONTENT, String dEPARTMENTID, String dEPARTMENTNAME, String sXID, String sXMC, String cREATETIME, String sFHF, String hFNR, String mODIFYTIME) {
		super();
		TYPE = tYPE;
		NAME = nAME;
		SEX = sEX;
		MOVEPHONE = mOVEPHONE;
		TELEPHONE = tELEPHONE;
		MAINTITLE = mAINTITLE;
		CONTENT = cONTENT;
		DEPARTMENTID = dEPARTMENTID;
		DEPARTMENTNAME = dEPARTMENTNAME;
		SXID = sXID;
		SXMC = sXMC;
		CREATETIME = cREATETIME;
		SFHF = sFHF;
		HFNR = hFNR;
		MODIFYTIME = mODIFYTIME;
	}
	public Consult() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getSEX() {
		return SEX;
	}
	public void setSEX(String sEX) {
		SEX = sEX;
	}
	public String getMOVEPHONE() {
		return MOVEPHONE;
	}
	public void setMOVEPHONE(String mOVEPHONE) {
		MOVEPHONE = mOVEPHONE;
	}
	public String getTELEPHONE() {
		return TELEPHONE;
	}
	public void setTELEPHONE(String tELEPHONE) {
		TELEPHONE = tELEPHONE;
	}
	public String getMAINTITLE() {
		return MAINTITLE;
	}
	public void setMAINTITLE(String mAINTITLE) {
		MAINTITLE = mAINTITLE;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public String getDEPARTMENTID() {
		return DEPARTMENTID;
	}
	public void setDEPARTMENTID(String dEPARTMENTID) {
		DEPARTMENTID = dEPARTMENTID;
	}
	public String getDEPARTMENTNAME() {
		return DEPARTMENTNAME;
	}
	public void setDEPARTMENTNAME(String dEPARTMENTNAME) {
		DEPARTMENTNAME = dEPARTMENTNAME;
	}
	public String getSXID() {
		return SXID;
	}
	public void setSXID(String sXID) {
		SXID = sXID;
	}
	public String getSXMC() {
		return SXMC;
	}
	public void setSXMC(String sXMC) {
		SXMC = sXMC;
	}
	public String getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(String cREATETIME) {
		CREATETIME = cREATETIME;
	}
	public String getSFHF() {
		return SFHF;
	}
	public void setSFHF(String sFHF) {
		SFHF = sFHF;
	}
	public String getHFNR() {
		return HFNR;
	}
	public void setHFNR(String hFNR) {
		HFNR = hFNR;
	}
	public String getMODIFYTIME() {
		return MODIFYTIME;
	}
	public void setMODIFYTIME(String mODIFYTIME) {
		MODIFYTIME = mODIFYTIME;
	}


}
