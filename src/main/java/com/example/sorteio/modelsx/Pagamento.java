package com.example.sorteio.modelsx;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class Pagamento {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	 private int id;
	 private int usuarioId;
	 private Date dataPagamento;
	 private float transactionAmount;
	 private String token;
	 private String description;
	 private int installments;
	 private String paymentMethodId;
	 private String docType;
	 private String docNumber;
	private String email;
	private String status_pagamento;

	public int getNumeroComprado() {
		return numeroComprado;
	}

	public void setNumeroComprado(int numeroComprado) {
		this.numeroComprado = numeroComprado;
	}

	private int numeroComprado;
	
	
	
	public Pagamento() {
		
	}

	public Pagamento(float transactionAmount, String token,
			String description, int installments, String paymentMethodId, String docType, String docNumber,
			String email, String status_pagamento, int usuarioId) {
		
		this.id = id;
		this.usuarioId = usuarioId;
		this.dataPagamento = dataPagamento;
		this.transactionAmount = transactionAmount;
		this.token = token;
		this.description = description;
		this.installments = installments;
		this.paymentMethodId = paymentMethodId;
		this.docType = docType;
		this.docNumber = docNumber;
		this.email = email;
		this.status_pagamento =status_pagamento;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUsuarioId() {
		return usuarioId;
	}

	public void setUsuarioId(int usuarioId) {
		this.usuarioId = usuarioId;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}

	public float getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(float transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getInstallments() {
		return installments;
	}

	public void setInstallments(int installments) {
		this.installments = installments;
	}

	public String getPaymentMethodId() {
		return paymentMethodId;
	}

	public void setPaymentMethodId(String paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocNumber() {
		return docNumber;
	}

	public void setDocNumber(String docNumber) {
		this.docNumber = docNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStatus_pagamento() {
		return status_pagamento;
	}

	public void setStatus_pagamento(String status_pagamento) {
		this.status_pagamento = status_pagamento;
	}



	 
}
