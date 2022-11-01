package com.estrada.webservice.facturation.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estrada.webservice.facturation.app.dao.IInvoiceDao;
import com.estrada.webservice.facturation.app.entity.Invoice;

@Service
public class InvoiceServiceImpl implements IInvoiceService{

	@Autowired
	IInvoiceDao invoiceDao;
	
	@Override
	public Invoice save(Invoice invoice) {
		return invoiceDao.save(invoice);
	}

	@Override
	public List<Invoice> findAll() {
		return (List<Invoice>) invoiceDao.findAll();
	}

	@Override
	public Invoice findById(Long id) {
		return invoiceDao.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		invoiceDao.deleteById(id);
	}

}
