package com.tjsoft.webhall.entity;

import java.io.Serializable;
/**
 * 办理模式实体类
 * @author S_Black
 *
 */
public class BLMSBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String WSFWSD;//网上服务深度，1,2,3,4
	private String DXCCS;//到现场次数，0,1,2以上
	private String DJZZCL;//递交纸质材料，1网点递交，2速递服务
	private String LQSPJG;//领取审批结果，1网点领取，2速递服务，3自行打印
	private String YWGSD;//业务归属地：1国家级，2省级，3地市级，4区县级，5街道级，6社区级
	private String BLMS;//1.零次到现场全流程网上办理、2.线上申请、线上提交（受理环节递交纸质申请材料）、3.线上申请、线上提交（领证环节递交纸质申请材料） 、4.线上预约、线下提交
	public String getWSFWSD() {
		return WSFWSD;
	}
	public void setWSFWSD(String wSFWSD) {
		WSFWSD = wSFWSD;
	}
	public String getDXCCS() {
		return DXCCS;
	}
	public void setDXCCS(String dXCCS) {
		DXCCS = dXCCS;
	}
	public String getDJZZCL() {
		return DJZZCL;
	}
	public void setDJZZCL(String dJZZCL) {
		DJZZCL = dJZZCL;
	}
	public String getLQSPJG() {
		return LQSPJG;
	}
	public void setLQSPJG(String lQSPJG) {
		LQSPJG = lQSPJG;
	}
	public String getYWGSD() {
		return YWGSD;
	}
	public void setYWGSD(String yWGSD) {
		YWGSD = yWGSD;
	}
	public String getBLMS() {
		return BLMS;
	}
	public void setBLMS(String bLMS) {
		BLMS = bLMS;
	}
}
