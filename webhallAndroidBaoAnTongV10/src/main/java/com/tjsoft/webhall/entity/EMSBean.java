package com.tjsoft.webhall.entity;

import java.io.Serializable;
import java.util.List;

public class EMSBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<PostInfo> Items;
	private String LZFS;
	public List<PostInfo> getItems() {
		return Items;
	}
	public void setItems(List<PostInfo> items) {
		Items = items;
	}
	public String getLZFS() {
		return LZFS;
	}
	public void setLZFS(String lZFS) {
		LZFS = lZFS;
	}

}
