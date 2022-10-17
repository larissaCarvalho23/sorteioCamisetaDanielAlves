package com.example.sorteio.Controller;

import com.example.sorteio.forms.PagamentoForms;
import com.example.sorteio.modelsx.DadosUusario;
import com.example.sorteio.modelsx.Pagamento;
import com.example.sorteio.repositoryy.PagamentoRepository;
import com.mercadopago.MercadoPago;
import com.mercadopago.exceptions.MPException;
import com.mercadopago.resources.Payment;
import com.mercadopago.resources.datastructures.payment.Address;
import com.mercadopago.resources.datastructures.payment.Identification;
import com.mercadopago.resources.datastructures.payment.Payer;
import configuracao.emailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.validation.Valid;
import java.util.List;
import java.util.Random;

@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/process_payment")
public class PagamentoController {

    @Autowired
    PagamentoRepository pagamentoRepository;

    @PostMapping
    public String pagamento(@Valid PagamentoForms pagamentoForms) throws Exception {


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
                DadosUusario dadosUusario = new DadosUusario();
                dadosUusario.setNumero(numeroSorteio);
                dadosUusario.setEmailUsuario(savePagamento.getEmail());
                enviaEmail(dadosUusario);
                pagamentoRepository.save(savePagamento);
                return payment.getTransactionDetails().getExternalResourceUrl();


            } catch (MPException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return "Erro ou processar boleto, tente mais tarde";
            }

        }
        if (payment.getPaymentMethodId().toString().toUpperCase().contains("MASTER") || payment.getPaymentMethodId().toString().toUpperCase().contains("VISA") || payment.getPaymentMethodId().toString().toUpperCase().contains("AMEX")) {
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


    @PostMapping("/email")
    public ResponseEntity enviaEmail(@RequestBody DadosUusario dadosUusario) throws Exception {
        JavaMailSender mailSender;
        emailConfig em = new emailConfig();
        mailSender = em.mailSender();
   
        try {

            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);
            helper.setFrom("larissacarvalho887@gmail.com");
            helper.setTo(dadosUusario.emailUsuario);
            helper.setSubject("Sorteio Jogador Daniel Alves");
            String htmlText = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
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
                    "    <title>Sorteio Jogador Daniel Alves</title>\n" +
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
                    "    <!-- pre-header end -->\n" +
                    "    <!-- header -->\n" +
                    "    <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"ffffff\">\n" +
                    "\n" +
                    "        <tr>\n" +
                    "            <td align=\"center\">\n" +
                    "                <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\n" +
                    "\n" +
                    "                    <tr>\n" +
                    "                        <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\n" +
                    "                    </tr>\n" +
                    "\n" +
                    "                    <tr>\n" +
                    "                        <td align=\"center\">\n" +
                    "\n" +
                    "                            <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\n" +
                    "\n" +
                    "                                <tr>\n" +
                    "                                    <td align=\"center\" height=\"70\" style=\"height:70px;\">\n" +
                    "									  <a href=\"\"  style=\"color: #312c32; text-decoration: none;font-size: 25px;\"><strong>Sorteio Daniel Alves</strong></a>\n" +
                    "                                     \n" +
                    "                                </tr>\n" +
                    "\n" +
                    "                                <tr>\n" +
                    "                                    <td align=\"center\">\n" +
                    "                                        <table width=\"360 \" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"border-collapse:collapse; mso-table-lspace:0pt; mso-table-rspace:0pt;\"\n" +
                    "                                            class=\"container590 hide\">\n" +
                    "                                            <tr>\n" +
                    "                                                <td width=\"120\" align=\"center\" style=\"font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;\">\n" +
                    "                                                    <a href=\"\" style=\"color: #312c32; text-decoration: none;\"></a>\n" +
                    "                                                </td>\n" +
                    "                                                <td width=\"120\" align=\"center\" style=\"font-size: 14px; font-family: 'Work Sans', Calibri, sans-serif; line-height: 24px;\">\n" +
                    "                                                    <a href=\"\" style=\"color: #312c32; text-decoration: none;\"></a>\n" +
                    "                                                </td>\n" +
                    "                                                \n" +
                    "                                            </tr>\n" +
                    "                                        </table>\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "\n" +
                    "                    <tr>\n" +
                    "                        <td height=\"25\" style=\"font-size: 25px; line-height: 25px;\">&nbsp;</td>\n" +
                    "                    </tr>\n" +
                    "\n" +
                    "                </table>\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "    </table>\n" +
                    "    <!-- end header -->\n" +
                    "\n" +
                    "    <!-- big image section -->\n" +
                    "\n" +
                    "    <table border=\"0\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" bgcolor=\"ffffff\" class=\"bg_color\">\n" +
                    "\n" +
                    "        <tr>\n" +
                    "            <td align=\"center\">\n" +
                    "                <table border=\"0\" align=\"center\" width=\"590\" cellpadding=\"0\" cellspacing=\"0\" class=\"container590\">\n" +
                    "\n" +
                    "                    <tr>\n" +
                    "                        <td align=\"center\" style=\"color: #343434; font-size: 24px; font-family: Quicksand, Calibri, sans-serif; font-weight:700;letter-spacing: 3px; line-height: 35px;\"\n" +
                    "                            class=\"main-header\">\n" +
                    "                            <!-- section text ======-->\n" +
                    "\n" +
                    "                            <div style=\"line-height: 35px\">\n" +
                    "\n" +
                    "                               Olá " + dadosUusario.emailUsuario + " seu número de sorteio é o " + dadosUusario.numero + " valendo uma camiseta autografada pelo Daniel Alves" +
                    " <span style=\"color: #5caad2;\"> Boa Sorte!! </span>\n " +
                    "\n" +
                    "                            </div>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "\n" +
                    "                                        <p style=\"line-height: 24px\">\n" +
                    "                                            Atenciosamente,</br>\n" +
                    "                                           Equipe Daniel Alves\n" +
                    "                                        </p>\n" +
                    "\n" +
                    "                                    </td>\n" +
                    "                                </tr>\n" +
                    "                            </table>\n" +
                    "                        </td>\n" +
                    "                    </tr>\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "\n" +
                    "                </table>\n" +
                    "\n" +
                    "            </td>\n" +
                    "        </tr>\n" +
                    "\n" +
                    "</body>\n" +
                    "\n" +
                    "</html>";

            helper.setText(htmlText, true);
            mailSender.send(mime);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("erro" + e.getMessage());
        }
    }
}











