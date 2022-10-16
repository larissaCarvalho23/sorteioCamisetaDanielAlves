package com.example.sorteio.Controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import javax.validation.Valid;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPConfException;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.exceptions.MPRestException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.datastructures.payment.Address;
import com.mercadopago.resources.datastructures.payment.Identification;
import com.mercadopago.resources.datastructures.payment.Payer;

import com.example.sorteio.forms.PagamentoForms;
import com.example.sorteio.modelsx.Pagamento;
import com.example.sorteio.repositoryy.PagamentoRepository;
@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/process_payment")
public class PagamentoController {
	@Autowired
	PagamentoRepository pagamentoRepository;

	@PostMapping
	public String pagamento(@Valid PagamentoForms pagamentoForms) throws MPConfException, MPRestException {
		
	
		MercadoPago.SDK.setAccessToken("TEST-7026030396368846-021617-96e37e8b59344581cfa84f73e1d30e4b-653173414");
		
		//Object payment_methods = MercadoPago.SDK.Get("/v1/payment_methods");
		Payment payment = new Payment();
		payment.setTransactionAmount(pagamentoForms.getTransactionAmount()).setToken(pagamentoForms.getToken())
				.setDescription(pagamentoForms.getToken()).setInstallments(pagamentoForms.getInstallments())
				.setPaymentMethodId(pagamentoForms.getPaymentMethodId());

		Identification identification = new Identification();
		identification.setType(pagamentoForms.getDocType()).setNumber(pagamentoForms.getDocNumber());

		Payer payer = new Payer();
		payer.setEmail(pagamentoForms.getEmail()).setIdentification(identification);
		payer.setFirstName(pagamentoForms.getName());
		payer.setLastName(pagamentoForms.getLastname());
		payer.setAddress(new Address()
				.setZipCode("06233200")
				.setStreetName("Av. das Nações Unidas")
				.setStreetNumber(3003)
				.setNeighborhood("Bonfim")
				.setCity("Osasco")
				.setFederalUnit("SP"));
		payment.setPayer(payer);

		if (payment.getPaymentMethodId().toString().contains("bolbradesco")) {
			try {
				payment.save();
				int numeroSorteio = RetornaNumeroSorteado();
				Pagamento savePagamento = pagamentoForms.converter();
				savePagamento.setNumeroComprado(numeroSorteio);
				savePagamento.setStatus_pagamento(payment.getStatusDetail());
				pagamentoRepository.save(savePagamento);
				return payment.getTransactionDetails().getExternalResourceUrl();


			} catch (MPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "Erro ou processar boleto, tente mais tarde";
			}
		
		}
		if (payment.getPaymentMethodId().toString().toUpperCase().contains("MASTER") || payment.getPaymentMethodId().toString().toUpperCase().contains("VISA")|| payment.getPaymentMethodId().toString().toUpperCase().contains("AMEX")) {
			try {
				payment.save();
				if (payment.getStatus().toString().toLowerCase().contains("approved")) {
					int numeroSorteio = RetornaNumeroSorteado();
					Pagamento savePagamento = pagamentoForms.converter();
					savePagamento.setNumeroComprado(numeroSorteio);
					savePagamento.setStatus_pagamento(payment.getStatus().toString().toLowerCase());
				
					pagamentoRepository.save(savePagamento);
					return payment.getStatus().toString();
				} else {
					return payment.getStatus().toString();
				}

			} catch (MPException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// return payment.getCallbackUrl();
		}
		return "Método de Pagamento não identificado";
	}

	@GetMapping
	public int RetornaNumeroSorteado() {

		List<Pagamento> numerojaSorteado = pagamentoRepository.findAll();

		Boolean salvaNumero = false;
		int valorGerado = 0;

		if (!numerojaSorteado.isEmpty()) {
			while (salvaNumero == false) {
				Random random = new Random();
				for (Pagamento dado : numerojaSorteado) {
					if (dado.getNumeroComprado() != random.nextInt()) {
						salvaNumero = true;
						valorGerado = random.nextInt();
					}
				}
			}
			return valorGerado;
		} else {
			Random random = new Random();
			return random.nextInt();
		}

	}

	@GetMapping("/resultado")
	public int ResultadoSorteio() {

		//List<Pagamento> numerojaSorteado = pagamentoRepository.findAll();
		/*List<Integer> numerojaSorteado = Lists.newArrayList(1, 2, 3, 4, 5, 6);

		Random random = new Random(numerojaSorteado.size());

		return random.nextInt();*/
		List<Pagamento> numerojaSorteado = pagamentoRepository.findAll();

		Boolean salvaNumero = false;
		int valorGerado = 0;

			while (salvaNumero == false) {
				Random random = new Random();
				for (Pagamento dado : numerojaSorteado) {
					if (dado.getNumeroComprado() == random.nextInt()) {
						salvaNumero = true;
						valorGerado = random.nextInt();
					}
				}
			}
			return valorGerado;
		}

}











