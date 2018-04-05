package com.hsbc.groups.es.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hsbc.groups.es.domain.BusinessObject;
import com.hsbc.groups.es.domain.Customer;
import com.hsbc.groups.es.service.ICustomerService;

@RestController
@RequestMapping(value="/customers")
public class CustomerController {
	
	@Autowired
	private ICustomerService customerService;
	
	//list
	@RequestMapping(method= {RequestMethod.GET,RequestMethod.POST})
	public List<?> list(@RequestBody BusinessObject businessObject, Map<String,Object> params) throws Exception{
		return customerService.find(businessObject,params);
		
	}
	
	//get one
	@PostMapping
	public Customer get(@RequestBody BusinessObject businessObject) throws Exception {
			return customerService.findOne(businessObject);
	}
	
	//add
	@PostMapping
	public boolean saveOrUpdate(@RequestBody Customer customer) throws Exception {
		return customerService.save(customer);
	}
	

	//update
//	@PutMapping(value="/{id}")
//	public boolean update(@PathVariable String id, Map<String,Object> params) {
//		return customerService.update(id,params);
//	}
	
	//delete
	@PostMapping
	public boolean delete(@RequestBody BusinessObject businessObject) throws Exception {
		return customerService.delete(businessObject);
	}
/**
 * batch operations	
 */
//	//batch add
//	@PostMapping
//	public boolean add(@RequestBody List<Customer> customerList) throws Exception {
//		return customerService.save(customerList);
//	}
//	....

}
