package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * 用户实体类
 * @author Administrator
 *
 */
public class FaceCKParam implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -100889989898L;


	public String getCheck() {
		return check;
	}

	public void setCheck(String check) {
		this.check = check;
	}

	public ApplyParam getApply() {
		return apply;
	}

	public void setApply(ApplyParam apply) {
		this.apply = apply;
	}

	/**
	 * USER_ID : 1580908
	 * USERNAME : f71cbc7cd70645d99f44355307dd89c2
	 * REALNAME : 向含巧
	 * TOKEN : 0677F651-C326-4979-B358-6EA17FF65367
	 * EMAIL : null
	 * MOBILE : 13900000003
	 * TYPE : 1
	 * CODE : 330922197908242188
	 * AUTHLEVEL : 1
	 * USER_IMG : null
	 * COMPANY : null
	 */

	private String check;
	private ApplyParam apply;


}
