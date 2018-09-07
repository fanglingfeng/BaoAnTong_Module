package com.tjsoft.webhall.entity;

public class Table {
	private String id;
	private String type;
	private String name;
	private String source;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Table(String id, String type, String name) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
	}
	public Table(String id, String type, String name,String source) {
		super();
		this.id = id;
		this.type = type;
		this.name = name;
		this.source = source;
	}
	

}
