package com.hsbc.groups.es.domain;


public class Customer {
	
	private BusinessObject businessObject;
	
	private String name;
	private String gender;
	
	public Customer() {}
	
	
	public Customer(BusinessObject businessObject, String name, String gender) {
		this.businessObject = businessObject;
		this.name = name;
		this.gender = gender;
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}

	public BusinessObject getBusinessObject() {
		return businessObject;
	}

	public void setBusinessObject(BusinessObject businessObject) {
		this.businessObject = businessObject;
	}
	
}
