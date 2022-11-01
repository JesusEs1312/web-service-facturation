package com.estrada.webservice.facturation.app.service;

import java.util.List;

import com.estrada.webservice.facturation.app.entity.Customer;

public interface ICustomerService {
	
	Customer save(Customer customer);
	Customer findById(Long id);
	List<Customer> findAll();
	void deleteById(Long id);
}
