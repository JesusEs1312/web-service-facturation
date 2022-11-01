package com.estrada.webservice.facturation.app.service;

import java.util.List;

import com.estrada.webservice.facturation.app.entity.Product;

public interface IProductService {
	Product save(Product product);
	List<Product> findAll();
	Product findById(Long id);
	void deleteById(Long id);
}
