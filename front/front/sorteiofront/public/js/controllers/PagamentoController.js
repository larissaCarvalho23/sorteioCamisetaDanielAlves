angular
  .module("Sorteio")
  .controller(
    'PagamentoController',
    
    
    function (sorteioService, $scope) {
        
       

 //REPLACE WITH YOUR PUBLIC KEY AVAILABLE IN: https://developers.mercadopago.com/panel/credentials

 var  pagamento = function(){
 window.Mercadopago.setPublishableKey("TEST-95e84925-5fd8-44b9-9e1d-d4e92d7d2f8b");

 window.Mercadopago.getIdentificationTypes();
   
 document.getElementById('cardNumber').addEventListener('change', guessPaymentMethod);
 document.getElementById('paymentMethod').addEventListener('change', AtribuirPagamento);
 function AtribuirPagamento(){
    document.getElementById('paymentMethodId').value = document.getElementById('paymentMethod').value
 if(document.getElementById('paymentMethod').value.includes('card')){
   document.querySelector('#pagamento_cartao').style.display = 'block';
 }else{
    document.querySelector('#pagamento_cartao').style.display = 'none';
  }
}
 function guessPaymentMethod(event) {
     cleanCardInfo();
 
     let cardnumber = document.getElementById("cardNumber").value;
     if (cardnumber.length >= 6) {
         let bin = cardnumber.substring(0,6);
         window.Mercadopago.getPaymentMethod({
             "bin": bin
         }, setPaymentMethod);
     }
 };
 
 function setPaymentMethod(status, response) {
     if (status == 200) {
         let paymentMethod = response[0];
    
         document.getElementById('paymentMethodId').value = paymentMethod.id;
         document.getElementById('cardNumber').style.backgroundImage = 'url(' + paymentMethod.thumbnail + ')';
         
         if(paymentMethod.additional_info_needed.includes("issuer_id")){
             getIssuers(paymentMethod.id);
 
         } else {
             document.getElementById('issuerInput').classList.add("hidden");
 
             getInstallments(
                 paymentMethod.id,
                 document.getElementById('amount').value
             );
         }
 
     } else {
         alert(`payment method info error: ${response}`);
     }
 }
 
 function getIssuers(paymentMethodId) {
     window.Mercadopago.getIssuers(
         paymentMethodId, 
         setIssuers
     );
 }
 
 function setIssuers(status, response) {
     if (status == 200) {
         let issuerSelect = document.getElementById('issuer');
 
         response.forEach( issuer => {
             let opt = document.createElement('option');
             opt.text = issuer.name;
             opt.value = issuer.id;
             issuerSelect.appendChild(opt);
         });
         
         if(issuerSelect.options.length <= 1){
             document.getElementById('issuerInput').classList.add("hidden");
         } else {
             document.getElementById('issuerInput').classList.remove("hidden");
         }
         
         getInstallments(
             document.getElementById('paymentMethodId').value,
             document.getElementById('amount').value,
             issuerSelect.value
         );
 
     } else {
         alert(`issuers method info error: ${response}`);
     }
 }
 
 function getInstallments(paymentMethodId, amount, issuerId){
     window.Mercadopago.getInstallments({
         "payment_method_id": paymentMethodId,
         "amount": parseFloat(amount),
         "issuer_id": issuerId ? parseInt(issuerId) : undefined
     }, setInstallments);
 }
 
 function setInstallments(status, response){
     if (status == 200) {
         document.getElementById('installments').options.length = 0;
         response[0].payer_costs.forEach( payerCost => {
             let opt = document.createElement('option');
             opt.text = payerCost.recommended_message;
             opt.value = payerCost.installments;
             document.getElementById('installments').appendChild(opt);
         });
     } else {
         alert(`installments method info error: ${response}`);
     }
 }  
 
 //Update offered installments when issuer changes
 document.getElementById('issuer').addEventListener('change', updateInstallmentsForIssuer);
 function updateInstallmentsForIssuer(event) {
     window.Mercadopago.getInstallments({
         "payment_method_id": document.getElementById('paymentMethodId').value,
         "amount": parseFloat(document.getElementById('amount').value),
         "issuer_id": parseInt(document.getElementById('issuer').value)
     }, setInstallments);
 }
 
 //processo de pagamento
 doSubmit = false;
 document.getElementById('paymentForm').addEventListener('submit', getCardToken);
 
 function getCardToken(event){
   
    
     event.preventDefault();
     if(!doSubmit){
         let $form = document.getElementById('paymentForm');
         if(document.getElementById('paymentMethod').value.includes('card')){
         window.Mercadopago.createToken($form, setCardTokenAndPay);
         }
    else{PaytoBoleto()}
        

         return false;
     }
  
 };
 
 function setCardTokenAndPay(status, response) {
     if (status == 200 || status == 201) {
         let form = document.getElementById('paymentForm');
         let card = document.createElement('input');
         card.setAttribute('name', 'token');
         card.setAttribute('type', 'hidden');
         card.setAttribute('value', response.id);
         form.appendChild(card);
         doSubmit=true;
        console.log(form);
         
         var http = new XMLHttpRequest();
         var url = 'http://localhost:8080/process_payment';
        
         var params = 'docType='+ document.querySelector('#docType').value+'&docNumber='+document.querySelector('#docNumber').value+'&installments=1&transactionAmount=30&paymentMethodId='+document.querySelector('#paymentMethodId').value+'&description=Licença+Premium&email='+document.querySelector('#email').value+'&token='+document.getElementsByName('token')[0].value+'&idUsuario='+sessionStorage.getItem('id');
         console.log(params)
         http.open('POST', url, true);
         
         //Send the proper header information along with the request
         http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
         
         http.onreadystatechange = function() {//Call a function when the state changes.
             if(http.readyState == 4 && http.status == 200) {
                 if(http.response.includes('Approved')){
                Swal.fire({
                    title: 'Seu pagamento foi aprovado com sucesso',
                    icon: 'success',
                   
                    text:`Clique para acessar suas novas funções`,
                    
                    showConfirmButton:true,
                    confirmButtonText: `Abrir`,
                    
                  }).then((result) => {
                    /* Read more about isConfirmed, isDenied below */
                    if (result.isConfirmed) {
                        location.replace('#/home')
                    } else if (result.isDenied) {
                      Swal.fire('Changes are not saved', '', 'info')
                    }
                  })
                }
                else{

                    Swal.fire({
                        title: 'Seu pagamento foi recusado',
                        icon: 'error',
                       
                        text:`Valide suas informações de pagamento`,
                        
                        showConfirmButton:true,
                        confirmButtonText: `Ok`,
                        
                      }).then((result) => {
                        /* Read more about isConfirmed, isDenied below */
                        if (result.isConfirmed) {
                            //location.replace('#/home')
                        } else if (result.isDenied) {
                          Swal.fire('Changes are not saved', '', 'info')
                        }
                      })
                    }   
                }
                 
             }
         
         
         http.send(params);


     } else {
         alert("Verify filled data!\n"+JSON.stringify(response, null, 4));
     }
 };
 
 /***
  * UX functions 
  */
 function PaytoBoleto(){
    var http = new XMLHttpRequest();
    var url = 'http://localhost:8080/process_payment';
   
    var params = 'docType='+ document.querySelector('#docType').value+'&docNumber='+document.querySelector('#docNumber').value+'&installments=1&transactionAmount=30&paymentMethodId='+document.querySelector('#paymentMethodId').value+'&description=Licença+Premium&email='+document.querySelector('#email').value+'&idUsuario='+sessionStorage.getItem('id');
    console.log(params)
    http.open('POST', url, true);
    
    //Send the proper header information along with the request
    http.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
    
    http.onreadystatechange = function() {//Call a function when the state changes.
        
        if(http.readyState == 4 && http.status == 200) {
          
Swal.fire({
    title: 'Boleto gerado com sucesso',
    icon: 'success',
   
    text:`Clique para abrir o boleto`,
    showConfirmButton:true,
    confirmButtonText: `Abrir`,
    
  }).then((result) => {
    /* Read more about isConfirmed, isDenied below */
    if (result.isConfirmed) {
        window.open(http.response);
    } else if (result.isDenied) {
      Swal.fire('Changes are not saved', '', 'info')
    }
  })
     

        }
    }
    
    http.send(params);
 }
 
 function cleanCardInfo() {
     document.getElementById('cardNumber').style.backgroundImage = '';
     document.getElementById('issuerInput').classList.add("hidden");
     document.getElementById('issuer').options.length = 0;
     document.getElementById('installments').options.length = 0;
 }
 
 //Handle transitions
 document.getElementById('checkout-btn').addEventListener('click', function(){ 
     $('.shopping-cart').hide("fast",function() {
        // Use arguments.callee so we don't need a named function
        $( this ).prev().hide( "fast", arguments.callee );
      });
     setTimeout(() => { $('.container_payment').show(1000); }, 500);
 });
 document.getElementById('go-back').addEventListener('click', function(){ 
     $('.container_payment').hide(500);
     setTimeout(() => { $('.shopping-cart').show(500).fadeIn(); }, 500);
 });
 
 //Handle price update
 function updatePrice(){
     let quantity = document.getElementById('quantity').value;
     let unitPrice = document.getElementById('unit-price').innerHTML;
     let amount = parseInt(unitPrice) * parseInt(quantity);
 
     document.getElementById('cart-total').innerHTML = '$ ' + amount;
     document.getElementById('summary-price').innerHTML = '$ ' + unitPrice;
     document.getElementById('summary-quantity').innerHTML = quantity;
     document.getElementById('summary-total').innerHTML = '$ ' + amount;
     document.getElementById('amount').value = amount;
 };
 document.getElementById('quantity').addEventListener('change', updatePrice);
 updatePrice();
 
 //Retrieve product description
 document.getElementById('description').value = document.getElementById('product-description').innerHTML;

 var buscaResultado = function(){
        var data = new Date()
    if(data =='2021/03/14'){

        sorteioService.resultado().success(function(data){

            $scope.resultado = data;
            mostrar = 1;
        }
        );


    }   
 }

 }

 var mostrar = 0;

 $scope.mostrar = function(){

    if(mostrar === 0)

    return true;
 }

 $scope.exibe = function(){

    if(mostrar === 1)

    return true;
 }


 
});
 






