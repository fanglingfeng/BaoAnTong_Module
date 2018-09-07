package com.tjsoft.webhall.entity;

import java.util.List;

public class PermInfo {
	private String DESCRIPT;
	private List<PermInfoBean> Items;
	public String getDESCRIPT() {
		return DESCRIPT;
	}
	public void setDESCRIPT(String dESCRIPT) {
		DESCRIPT = dESCRIPT;
	}
	public List<PermInfoBean> getItems() {
		return Items;
	}
	public void setItems(List<PermInfoBean> items) {
		Items = items;
	}
	
	

}
