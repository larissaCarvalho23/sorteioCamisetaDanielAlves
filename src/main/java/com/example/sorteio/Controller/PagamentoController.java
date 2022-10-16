package com.example.sorteio.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import javax.validation.Valid;

import com.example.sorteio.modelsx.DadosUusario;
import com.google.common.collect.Lists;
import configuracao.emailConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

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

@RestController
@RequestMapping("/process_payment")
public class PagamentoController {
    @Autowired
    PagamentoRepository pagamentoRepository;


    @PostMapping
    public String pagamento(@Valid PagamentoForms pagamentoForms) throws Exception {

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        MercadoPago.SDK.setAccessToken("TEST-7026030396368846-021617-96e37e8b59344581cfa84f73e1d30e4b-653173414");
        //System.out.println(MercadoPago.SDK.Get("/v1/payment_methods").getStringResponse());
        Payment payment = new Payment();
        payment.setTransactionAmount(pagamentoForms.getTransactionAmount()).setToken(pagamentoForms.getToken())
                .setDescription(pagamentoForms.getToken()).setInstallments(pagamentoForms.getInstallments())
                .setPaymentMethodId(pagamentoForms.getPaymentMethodId());

        Identification identification = new Identification();
        identification.setType(pagamentoForms.getDocType()).setNumber(pagamentoForms.getDocNumber());

        Payer payer = new Payer();
        payer.setEmail(pagamentoForms.getEmail()).setIdentification(identification);
        payer.setFirstName("Raphael");
        payer.setLastName("Ptta");
        payer.setAddress(new Address()
                .setZipCode("06233200")
                .setStreetName("Av. das Nações Unidas")
                .setStreetNumber(3003)
                .setNeighborhood("Bonfim")
                .setCity("Osasco")
                .setFederalUnit("SP"));
        payment.setPayer(payer);

        if (payment.getPaymentMethodId().contains("bolbradesco")) {
            try {
                payment.save();
                int numeroSorteio = RetornaNumeroSorteado();
                Pagamento savePagamento = pagamentoForms.converter();
                savePagamento.setNumeroComprado(numeroSorteio);
                savePagamento.setStatus_pagamento(payment.getStatusDetail());
                // savePagamento.setDataPagamento(Date.now());
                pagamentoRepository.save(savePagamento);
                return payment.getTransactionDetails().getExternalResourceUrl();


            } catch (MPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "Erro ou processar boleto, tente mais tarde";
            }
            // return payment.getCallbackUrl();
        }
        if (payment.getPaymentMethodId().contains("card")) {
            try {
                payment.save();
                if (payment.getStatus().toString().toLowerCase() == "approved") {
                    int numeroSorteio = RetornaNumeroSorteado();
                    Pagamento savePagamento = pagamentoForms.converter();
                    savePagamento.setNumeroComprado(numeroSorteio);
                    savePagamento.setStatus_pagamento(payment.getStatus().toString().toLowerCase());
                    // savePagamento.setDataPagamento(Date.now());
                    DadosUusario dadosUusario = new DadosUusario();
                    dadosUusario.setEmailUsuario(savePagamento.getEmail());
                    dadosUusario.setNumero(numeroSorteio);
                    pagamentoRepository.save(savePagamento);
                    enviaEmail(dadosUusario);
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

        if (numerojaSorteado != null) {
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

    @PostMapping("/email")
    public ResponseEntity enviaEmail(@RequestBody DadosUusario dadosUusario) throws Exception {
        JavaMailSender mailSender;
        emailConfig em = new emailConfig();
        mailSender = em.mailSender();
        // emailRepository.save(emailUsuario);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
                String text = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                    "<html xmlns:v=\"urn:schemas-microsoft-com:vml\">\n" +
                    "\n" +
                    "<head>\n" +
                    "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\" />\n" +
                    "    <meta name=\"viewport\" content=\"width=device-width; initial-scale=1.0; maximum-scale=1.0;\" />\n" +
                    "    <!--[if !mso]--><!-- -->\n" +
                    "    <link href='https://fonts.googleapis.com/css?family=Work+Sans:300,400,500,600,700' rel=\"stylesheet\">\n" +
                    "    <link href='https://fonts.googleapis.com/css?family=Quicksand:300,400,700' rel=\"stylesheet\">\n" +
                    "    <!--<![endif]-->\n" +
                    "\n" +
                    "    <title>Myhero Training</title>\n" +
                    "\n" +
                    "    <style type=\"text/css\">\n" +
                    "        body {\n" +
                    "            width: 100%;\n" +
                    "            background-color: #ffffff;\n" +
                    "            margin: 0;\n" +
                    "            padding: 0;\n" +
                    "            -webkit-font-smoothing: antialiased;\n" +
                    "            mso-margin-top-alt: 0px;\n" +
                    "            mso-margin-bottom-alt: 0px;\n" +
                    "            mso-padding-alt: 0px 0px 0px 0px;\n" +
                    "        }\n" +
                    "\n" +
                    "        p,\n" +
                    "        h1,\n" +
                    "        h2,\n" +
                    "        h3,\n" +
                    "        h4 {\n" +
                    "            margin-top: 0;\n" +
                    "            margin-bottom: 0;\n" +
                    "            padding-top: 0;\n" +
                    "            padding-bottom: 0;\n" +
                    "        }\n" +
                    "\n" +
                    "        span.preheader {\n" +
                    "            display: none;\n" +
                    "            font-size: 1px;\n" +
                    "        }\n" +
                    "\n" +
                    "        html {\n" +
                    "            width: 100%;\n" +
                    "        }\n" +
                    "\n" +
                    "        table {\n" +
                    "            font-size: 14px;\n" +
                    "            border: 0;\n" +
                    "        }\n" +
                    "        /* ----------- responsivity ----------- */\n" +
                    "\n" +
                    "        @media only screen and (max-width: 640px) {\n" +
                    "            /*------ top header ------ */\n" +
                    "            .main-header {\n" +
                    "                font-size: 20px !important;\n" +
                    "            }\n" +
                    "            .main-section-header {\n" +
                    "                font-size: 28px !important;\n" +
                    "            }\n" +
                    "            .show {\n" +
                    "                display: block !important;\n" +
                    "            }\n" +
                    "            .hide {\n" +
                    "                display: none !important;\n" +
                    "            }\n" +
                    "            .align-center {\n" +
                    "                text-align: center !important;\n" +
                    "            }\n" +
                    "            .no-bg {\n" +
                    "                background: none !important;\n" +
                    "            }\n" +
                    "            /*----- main image -------*/\n" +
                    "            .main-image img {\n" +
                    "                width: 440px !important;\n" +
                    "                height: auto !important;\n" +
                    "            }\n" +
                    "            /* ====== divider ====== */\n" +
                    "            .divider img {\n" +
                    "                width: 440px !important;\n" +
                    "            }\n" +
                    "            /*-------- container --------*/\n" +
                    "            .container590 {\n" +
                    "                width: 440px !important;\n" +
                    "            }\n" +
                    "            .container580 {\n" +
                    "                width: 400px !important;\n" +
                    "            }\n" +
                    "            .main-button {\n" +
                    "                width: 220px !important;\n" +
                    "            }\n" +
                    "            /*-------- secions ----------*/\n" +
                    "            .section-img img {\n" +
                    "                width: 320px !important;\n" +
                    "                height: auto !important;\n" +
                    "            }\n" +
                    "            .team-img img {\n" +
                    "                width: 100% !important;\n" +
                    "                height: auto !important;\n" +
                    "            }\n" +
                    "        }\n" +
                    "\n" +
                    "        @media only screen and (max-width: 479px) {\n" +
                    "            /*------ top header ------ */\n" +
                    "            .main-header {\n" +
                    "                font-size: 18px !important;\n" +
                    "            }\n" +
                    "            .main-section-header {\n" +
                    "                font-size: 26px !important;\n" +
                    "            }\n" +
                    "            /* ====== divider ====== */\n" +
                    "            .divider img {\n" +
                    "                width: 280px !important;\n" +
                    "            }\n" +
                    "            /*-------- container --------*/\n" +
                    "            .container590 {\n" +
                    "                width: 280px !important;\n" +
                    "            }\n" +
                    "            .container590 {\n" +
                    "                width: 280px !important;\n" +
                    "            }\n" +
                    "            .container580 {\n" +
                    "                width: 260px !important;\n" +
                    "            }\n" +
                    "            /*-------- secions ----------*/\n" +
                    "            .section-img img {\n" +
                    "                width: 280px !important;\n" +
                    "                height: auto !important;\n" +
                    "            }\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "    <!--[if gte mso 9]><style type=”text/css”>\n" +
                    "        body {\n" +
                    "        font-family: arial, sans-serif!important;\n" +
                    "        }\n" +
                    "        </style>\n" +
                    "    <![endif]-->\n" +
                    "</head>\n" +
                    "\n" +
                    "\n" +
                    "<body class=\"respond\" leftmargin=\"0\" topmargin=\"0\" marginwidth=\"0\" marginheight=\"0\">\n" +
                    "    <!-- pre-header -->\n" +
                    "    <table style=\"display:none!important;\">\n" +
                    "        <tr>\n" +
                    "            <td>\n" +
                    "                <div style=\"overflow:hidden;display:none;font-size:1px;color:#ffffff;line-height:1px;font-family:Arial;maxheight:0px;max-width:0px;opacity:0;\">\n"+
                    "                Olá " + dadosUusario.emailUsuario + " o seu número de sorteio é o "  + dadosUusario.numero + " valendo uma camiseta autografada pelo jogador Daniel Alves\n" +
                                     "Desejamos boa sorte!!!"+
                    "                </div>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "    <!-- pre-header end -->\n" +
                    "    <!-- header -->\n" +

                    "    <!-- end footer ====== -->\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";

            message.setText(text);
            message.setTo(dadosUusario.emailUsuario);
            message.setFrom("laiscarvalhoo00@gmail.com");
            message.setSubject("Sorteio Camiseta Daniel Alves");
            mailSender.send(message);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("erro" + e.getMessage());
        }
    }
}











