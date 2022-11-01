package com.estrada.webservice.facturation.app.service;

import java.util.List;

import com.estrada.webservice.facturation.app.entity.Invoice;

public interface IInvoiceService {
	Invoice save(Invoice invoice);
	List<Invoice> findAll();
	Invoice findById(Long id);
	void deleteById(Long id);
}
