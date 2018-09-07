package com.tjsoft.webhall.imp;

public interface TransportCallBack {

	public void onStart();
	public void onFinish();
	/**
	 * @param status [0: 成功 ， 1：失败]
	 * **/
	public void onResult(int status);
	
}
