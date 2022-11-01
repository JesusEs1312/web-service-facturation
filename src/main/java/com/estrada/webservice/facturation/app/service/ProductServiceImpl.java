package com.estrada.webservice.facturation.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estrada.webservice.facturation.app.dao.IProductDao;
import com.estrada.webservice.facturation.app.entity.Product;

@Service
public class ProductServiceImpl implements IProductService{

	@Autowired
	IProductDao productDao;
	
	@Override
	public Product save(Product product) {
		return this.productDao.save(product);
	}

	@Override
	public List<Product> findAll() {
		return (List<Product>) this.productDao.findAll();
	}

	@Override
	public Product findById(Long id) {
		return this.productDao.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		this.productDao.deleteById(id);
	}

}
