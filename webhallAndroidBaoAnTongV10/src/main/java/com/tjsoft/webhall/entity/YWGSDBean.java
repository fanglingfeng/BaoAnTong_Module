package com.tjsoft.webhall.entity;

import java.util.List;

public class YWGSDBean {
	private List<Region> PARENTS;
	private List<Region> CHILDREN;
	
	public List<Region> getPARENTS() {
		return PARENTS;
	}
	public void setPARENTS(List<Region> pARENTS) {
		PARENTS = pARENTS;
	}
	public List<Region> getCHILDREN() {
		return CHILDREN;
	}
	public void setCHILDREN(List<Region> cHILDREN) {
		CHILDREN = cHILDREN;
	}
}
