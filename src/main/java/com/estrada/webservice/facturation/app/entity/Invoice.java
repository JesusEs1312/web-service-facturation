package com.estrada.webservice.facturation.app.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "invoices")
public class Invoice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@NotNull
	@Column(name = "name_transmitter")
	private String nameTransmitter;
	@NotNull
	@Column(name = "address_transmitter")
	private String addressTransmitter;
	@NotNull
	private String description;
	private String remark;
	@Temporal(TemporalType.TIME)
	@Column(name = "create_at")
	private Date createAt;
	@ManyToOne(fetch = FetchType.LAZY)
	private Customer customer;
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "invoice_id")
	private List<ItemInvoice> itemsInvoice;
	
	public Invoice() {
		this.itemsInvoice = new ArrayList<>();
	}
	@PrePersist
	public void prePersist() {
		this.createAt = new Date();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNameTransmitter() {
		return nameTransmitter;
	}
	public void setNameTransmitter(String name_transmitter) {
		this.nameTransmitter = name_transmitter;
	}
	public String getAddressTransmitter() {
		return addressTransmitter;
	}
	public void setAddressTransmitter(String address_transmitter) {
		this.addressTransmitter = address_transmitter;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Customer getCustomer() {
		return customer;
	}
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	public List<ItemInvoice> getItemsInvoice() {
		return itemsInvoice;
	}
	public void setItemsInvoice(List<ItemInvoice> itemsInvoice) {
		this.itemsInvoice = itemsInvoice;
	}
	public void addItemInvoice(ItemInvoice item) {
		itemsInvoice.add(item);
	}
	public Double calulateTotal() {
		Double total = 0.0;
		for(ItemInvoice item: itemsInvoice) {
			total += item.calculateAmount();
		}
		return total;
	}
	
	/**
	 * 
	 */
}
