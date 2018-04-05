package com.hsbc.groups.es.domain;

public class BusinessObject extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String index;
	
	private String type;
	
	public BusinessObject() {}

	public BusinessObject(String index, String type) {
		super();
		this.index = index;
		this.type = type;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	
	

}
