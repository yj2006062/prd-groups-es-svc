package com.hsbc.groups.es.service;

import java.util.List;
import java.util.Map;

import com.hsbc.groups.es.domain.BusinessObject;
import com.hsbc.groups.es.domain.Customer;

public interface ICustomerService {

	Customer findOne(BusinessObject businessObject) throws Exception;

	boolean save(Customer customer) throws Exception;

	boolean delete(BusinessObject businessObject) throws Exception;

	List<?> find(BusinessObject businessObject,Map<String, Object> params) throws Exception;

}
