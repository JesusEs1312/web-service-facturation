package com.estrada.webservice.facturation.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estrada.webservice.facturation.app.dao.ICustomerDao;
import com.estrada.webservice.facturation.app.entity.Customer;

@Service
public class CustomerServiceImpl implements ICustomerService{
	
	@Autowired
	private ICustomerDao customerDao;

	@Override
	public Customer save(Customer customer) {
		return this.customerDao.save(customer);
	}

	@Override
	public Customer findById(Long id) {
		return this.customerDao.findById(id).orElse(null);
	}

	@Override
	public List<Customer> findAll() {
		return (List<Customer>) this.customerDao.findAll();
	}

	@Override
	public void deleteById(Long id) {
		this.customerDao.deleteById(id);
	}
	
}
