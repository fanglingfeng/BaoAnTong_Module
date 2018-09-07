package com.tjsoft.webhall.entity;

public class Log {
	private String LID;
	private String NODENAME;
	private String IDEA;
	private String HANDLETIME;
	
	
	public Log(String lID, String nODENAME, String iDEA, String hANDLETIME) {
		super();
		LID = lID;
		NODENAME = nODENAME;
		IDEA = iDEA;
		HANDLETIME = hANDLETIME;
	}
	public String getLID() {
		return LID;
	}
	public void setLID(String lID) {
		LID = lID;
	}
	public String getNODENAME() {
		return NODENAME;
	}
	public void setNODENAME(String nODENAME) {
		NODENAME = nODENAME;
	}
	public String getIDEA() {
		return IDEA;
	}
	public void setIDEA(String iDEA) {
		IDEA = iDEA;
	}
	public String getHANDLETIME() {
		return HANDLETIME;
	}
	public void setHANDLETIME(String hANDLETIME) {
		HANDLETIME = hANDLETIME;
	}


}
