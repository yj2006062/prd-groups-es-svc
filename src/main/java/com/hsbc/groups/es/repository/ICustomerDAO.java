package com.hsbc.groups.es.repository;

import java.util.List;
import java.util.Map;

import com.hsbc.groups.es.domain.Customer;

public interface ICustomerDAO {

	List<?> queryByConditions(String index, String type, String id, Map<String, Object> params) throws Exception;

	boolean save(String index, String type, String id, Map dataSource) throws Exception;

	Map<String,Object> queryOne(String index, String type, String id) throws Exception;

	boolean delete(String index, String type, String id) throws Exception;

}
