package com.ibm.isp.poc.payments.service;

public class PaymentTransaction {
	public static final String STATUS_OK = "OK";
	long id = 123456789;
	String ordinante;
	String beneficiario;
	double amount;
	String status = STATUS_OK;
	String method = "Pay Pal";
	String message;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getBeneficiario() {
		return beneficiario;
	}
	public void setBeneficiario(String beneficiario) {
		this.beneficiario = beneficiario;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getOrdinante() {
		return ordinante;
	}
	public void setOrdinante(String ordinante) {
		this.ordinante = ordinante;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}