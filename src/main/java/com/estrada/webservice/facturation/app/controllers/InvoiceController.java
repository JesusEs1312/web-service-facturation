package com.estrada.webservice.facturation.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estrada.webservice.facturation.app.entity.Invoice;
import com.estrada.webservice.facturation.app.service.IInvoiceService;

@RestController
@RequestMapping("api")
public class InvoiceController {
	
	@Autowired
	IInvoiceService invoiceService;
	
	@GetMapping("/invoices")
	public List<Invoice> findAll() {
		return invoiceService.findAll();
	}
	
	@PostMapping("/invoices")
	public ResponseEntity<?> save(@Valid @RequestBody Invoice invoice, BindingResult result) {
		Invoice newInvoice = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "The field ".concat(err.getField()).concat(": ").concat(err.getDefaultMessage()))
					.toList();
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			newInvoice = invoiceService.save(invoice);
		} catch (DataAccessException e) {
			response.put("message", "Error saving invoice");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The invoice was successfully saved");
		response.put("invoice", newInvoice);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/invoices/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Invoice invoice = null;
		Map<String, Object> response = new HashMap<>();
		try {
			invoice = invoiceService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error finding customer");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(invoice == null) {
			response.put("message", "The invoice with ID ".concat(id.toString()).concat(" not exist "));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		response.put("message", "The invoice was successfully found");
		response.put("invoice", invoice);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PutMapping("/invoices/{id}")
	public ResponseEntity<?> updateById(@Valid @RequestBody Invoice invoice, BindingResult result, @PathVariable Long id) {
		Invoice currentInvoice = invoiceService.findById(id);
		Invoice updateInvoice  = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "The field ".concat(err.getField()).concat(": ").concat(err.getDefaultMessage()))
					.toList();
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if(currentInvoice == null) {
			response.put("message", "The invoice with ID: ".concat(id.toString()).concat(" not exist in the database"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);			
		}
		try {
			currentInvoice.setNameTransmitter(invoice.getNameTransmitter());
			currentInvoice.setAddressTransmitter(invoice.getAddressTransmitter());
			currentInvoice.setDescription(invoice.getDescription());
			currentInvoice.setRemark(invoice.getRemark());
			updateInvoice = invoiceService.save(currentInvoice);
		} catch (DataAccessException e) {
			response.put("message", "Error updating invoice");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);						
		}
		response.put("message", "The invoice was successfully updated");
		response.put("invoice", updateInvoice);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);								
	}
	
	@DeleteMapping("/invoices/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		Invoice invoice = invoiceService.findById(id);
		Map<String, Object> response = new HashMap<>();
		if(invoice == null) {
			response.put("error", "The invoice with ID: ".concat(id.toString()).concat(" not exist in the database"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			invoiceService.deleteById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error removing invoice");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);			
		}
		response.put("message", "The invoice was successfully removed");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);			
	}
}
