package com.hsbc.groups.es.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.groups.es.domain.BusinessObject;
import com.hsbc.groups.es.domain.Customer;
import com.hsbc.groups.es.repository.ICustomerDAO;
import com.hsbc.groups.es.service.ICustomerService;

@Service
public class CustomerServiceImpl implements ICustomerService {
	
	@Autowired
	private ICustomerDAO customerDAO;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public boolean save(Customer customer) throws Exception {
		BusinessObject bo = customer.getBusinessObject();
		String id = bo.getId();
		return customerDAO.save(bo.getIndex(),bo.getType(),id,objectMapper.convertValue(customer, Map.class));
	}

	@Override
	public List<?> find(BusinessObject businessObject , Map<String, Object> params) throws Exception {
		List<?> list = customerDAO.queryByConditions(businessObject.getIndex(),businessObject.getType(),businessObject.getId(),params);
		return list;
	}

	@Override
	public Customer findOne(BusinessObject businessObject) throws Exception {
		Map<String,Object> dataMap = customerDAO.queryOne(businessObject.getIndex(),businessObject.getType(),businessObject.getId());
		return objectMapper.convertValue(dataMap, Customer.class);
	}

	@Override
	public boolean delete(BusinessObject businessObject) throws Exception {
		return customerDAO.delete(businessObject.getIndex(),businessObject.getType(),businessObject.getId());
	}

}
