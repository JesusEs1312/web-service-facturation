package com.estrada.webservice.facturation.app.dao;


import org.springframework.data.repository.CrudRepository;

import com.estrada.webservice.facturation.app.entity.Customer;

public interface ICustomerDao extends CrudRepository<Customer, Long>{

}
