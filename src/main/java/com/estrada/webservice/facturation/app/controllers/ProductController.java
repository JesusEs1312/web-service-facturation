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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.estrada.webservice.facturation.app.entity.Product;
import com.estrada.webservice.facturation.app.service.IProductService;

@RestController
@RequestMapping("api")
public class ProductController {

	@Autowired
	IProductService productService;
	
	@GetMapping("/products")
	public List<Product> findAll() {
		return productService.findAll();
	}
	
	@PostMapping("/products")
	public ResponseEntity<?> save(@Valid @RequestBody Product product, BindingResult result) {
		Product newProduct = null;
		Map<String, Object> response = new HashMap<>();
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> err.getField().concat(": ").concat(err.getDefaultMessage()))
					.toList();
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			newProduct = productService.save(product);
		} catch (DataAccessException e) {
			response.put("message", "Error saving product");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The product was successfully saved");
		response.put("product", newProduct);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@GetMapping("/products/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Product product = null;
		try {
			product = productService.findById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error finding product");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		if(product == null) {
			response.put("message", "The product with: ".concat(String.valueOf(id)).concat(" no exists"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		response.put("message", "The product was successfully found");
		response.put("product", product);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@PostMapping("/products/{id}")
	public ResponseEntity<?> updateById(@Valid @RequestBody Product product, @PathVariable Long id, BindingResult result) {
		Map<String, Object> response = new HashMap<>();
		Product currentProduct = productService.findById(id);
		Product updateProduct  = null;
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> err.getField().concat(": ").concat(err.getDefaultMessage()))
					.toList();
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		if(currentProduct == null) {
			response.put("message", "The product with: ".concat(String.valueOf(id)).concat(" no exists"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			currentProduct.setProductName(product.getProductName());
			currentProduct.setPrice(product.getPrice());
			currentProduct.setDescription(product.getDescription());
			updateProduct = productService.save(currentProduct);
		} catch (DataAccessException e) {
			response.put("message", "Error editing product");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The product with ID:".concat(String.valueOf(id)).concat("was sucessfully edited"));
		response.put("product", updateProduct);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@DeleteMapping("/products/{id}")
	public ResponseEntity<?> deleteById(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Product productDelete = this.productService.findById(id);
		if(productDelete == null) {
			response.put("message", "The product with ID: ".concat(String.valueOf(id)).concat(" not exist"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			this.productService.deleteById(id);
		} catch (DataAccessException e) {
			response.put("message", "Error deleting product");
			response.put("error", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("message", "The product with ID: ".concat(String.valueOf(id)).concat("was successfully deleted"));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	
}
