package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 用户实体类
 * @author Administrator
 *
 */
public class AUTH_MSGparam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -100889989898L;


	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public FaceCKParam getFaceCK() {
		return faceCK;
	}

	public void setFaceCK(FaceCKParam faceCK) {
		this.faceCK = faceCK;
	}

	private String pid;
	private FaceCKParam faceCK;


}
