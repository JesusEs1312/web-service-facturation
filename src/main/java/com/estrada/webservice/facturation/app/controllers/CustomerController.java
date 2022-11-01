package com.estrada.webservice.facturation.app.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import com.estrada.webservice.facturation.app.entity.Customer;
import com.estrada.webservice.facturation.app.service.ICustomerService;

@RestController
@RequestMapping("api")
public class CustomerController {
	
	@Autowired
	ICustomerService customerService;
	
	//--- FindAll Customers
	@GetMapping("/customers")
	public List<Customer> findAll(){
		return customerService.findAll();
	}
	
	//--- Save Customer
	@PostMapping("/customers")
	public ResponseEntity<?> createCustomer(@Valid @RequestBody Customer customer, BindingResult result) {
		Customer newCustomer = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo ".concat(err.getField()).concat(": ").concat(err.getDefaultMessage()))
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);	
		}
		try {
			newCustomer = customerService.save(customer);
		} catch (DataAccessException e) {
			response.put("message", "Can't add user");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("customer", newCustomer);
		response.put("message", "Customer save success");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	//--- FindById Customer
	@GetMapping("/customers/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Customer customer = null;
		Map<String, Object> response = new HashMap<>();
		try {
			customer = customerService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "The user not find in the database");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(customer == null) {
			response.put("message", "The customer with ID: ".concat(id.toString()).concat(" not exist"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Customer>(customer, HttpStatus.OK);
	}
	
	//--- Edit Customer
	@PutMapping("/customers/{id}")
	public ResponseEntity<?> updateById(@Valid @RequestBody Customer customer, BindingResult result, @PathVariable Long id) {
		Customer currentCustomer = customerService.findById(id);
		Customer updateCustomer  = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo ".concat(err.getField()).concat(": ").concat(err.getDefaultMessage()))
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if(currentCustomer == null) {
			response.put("message", "The customer with ID: ".concat(id.toString()).concat(" not exist in database"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			currentCustomer.setName(customer.getName());
			currentCustomer.setAddress(customer.getAddress());
			currentCustomer.setEmail(customer.getEmail());
			currentCustomer.setFec_nac(customer.getFec_nac());
			currentCustomer.setImage(customer.getImage());
			currentCustomer.setPhone(customer.getPhone());
			currentCustomer.setRfc(customer.getRfc());
			updateCustomer = customerService.save(currentCustomer);
		} catch (DataAccessException e) {
			response.put("message", "Error updating customer");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("customer", updateCustomer);
		response.put("message", "Update customers Successful: ".concat(updateCustomer.getName()));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	//--- Delete Customer
	@DeleteMapping("/customers/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		Customer deleteCustomer = customerService.findById(id);
		Map<String, Object> response = new HashMap<>();
		if(deleteCustomer == null) {
			response.put("message", "The customer with ID: ".concat(id.toString()).concat(" not exist in database"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			customerService.deleteById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error deleting customer");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The customer was successfully removed");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
}
