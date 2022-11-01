package com.estrada.webservice.facturation.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.estrada.webservice.facturation.app.entity.Product;

public interface IProductDao extends CrudRepository<Product, Long>{

}
