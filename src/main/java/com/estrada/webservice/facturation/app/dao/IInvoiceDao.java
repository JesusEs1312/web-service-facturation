package com.estrada.webservice.facturation.app.dao;

import org.springframework.data.repository.CrudRepository;

import com.estrada.webservice.facturation.app.entity.Invoice;

public interface IInvoiceDao extends CrudRepository<Invoice, Long>{

}
