package com.tjsoft.webhall.entity;

import java.io.Serializable;

/**
 * http返回json对象
 * @author Administrator
 *
 */
public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1888003232L;
	private String code;
	private String error;
	private Object ReturnValue;
	
	
	public Response() {
		super();
	}
	public Response(String code, String error, Object returnValue) {
		super();
		this.code = code;
		this.error = error;
		ReturnValue = returnValue;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public Object getReturnValue() {
		return ReturnValue;
	}
	public void setReturnValue(Object returnValue) {
		ReturnValue = returnValue;
	}

	
	
	

}
