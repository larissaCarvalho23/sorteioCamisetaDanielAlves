angular
  .module('myHeroTraining')
  .controller(
    'treinoPersonalizadoController',
    function ($scope, 
      TreinoPersonalizadoService,
      $location
     ) {

      var valor=0;
      var faseTerminadas = [];
      var tamanhofor;
      var IdUsuario = sessionStorage.getItem('id');
        $scope.model={
          id: IdUsuario
        };

        $scope.desabilita = function() {
          var tamanhoteste = document.getElementById('teste').getElementsByTagName('tr').length;
          var contador = 0;


          for (var j = 0; j <tamanhoteste-1; j++) {
             
            //console.log(document.getElementById('teste').getElementsByTagName('tr')[j].getElementsByTagName('td')[0].getElementsByTagName('div')[0].getElementsByTagName('input')[0].checked == true)
                  
               if(document.getElementById('teste').getElementsByTagName('tr')[j].getElementsByTagName('td')[0].getElementsByTagName('div')[0].getElementsByTagName('input')[0].checked == false){
                  contador++;

              
               }        
                     
          }
          if(contador === tamanhoteste-1){
            document.getElementById('btn').disabled=true;
           }
           else{
            document.getElementById('btn').disabled=false;
           }   
         
        }
     
       $scope.salva = function(){            
               var tamanhoteste = document.getElementById('teste').getElementsByTagName('tr').length;
            
              for (var j = 0; j <tamanhoteste-1; j++) {
             
                 
               if(document.getElementById('teste').getElementsByTagName('tr')[j].getElementsByTagName('td')[0].getElementsByTagName('div')[0].getElementsByTagName('input')[0].checked == true){
               var id = document.getElementById('teste').getElementsByTagName('tr')[j].getElementsByTagName('td')[0].getElementsByTagName('div')[0].getElementsByTagName('input')[0].id;
                faseTerminadas.push(id);
              
               }        
              }

              for(var i = 0; i <faseTerminadas.length; i++) {              
                var Idescolha = faseTerminadas[i]; 
                 dados = {
                  id: IdUsuario,
                  exercicio : Idescolha            
                }
                TreinoPersonalizadoService.salvar(dados).success(function(data){   
               
                }).error(function(data){
                  if(data.status === 403){
                    $location.path('/login');
                  }
                });
              };         
              swal('Treino salvo com sucesso!');
              $location.path('/home');            
                    
            }
                 
       var carregaTreino = function(){       
        TreinoPersonalizadoService.carregaTreino(IdUsuario).success(function(data)
        {
            $scope.personalizado = data;
            tamanhofor = data.lenght;

        });
       }
        $scope.apagaTreino = function(){
        //pegar Id aqui 
        TreinoPersonalizadoService.apaga(IdUsuario).success(function(data)
        {
          swal('Treino Excluido com sucesso!');
          $location.path('/home');
          
          
        });      

       }
       
       $scope.teste = function(valor){
          if(valor === true)

          return true;
       }
       carregaTreino();

     var carregaTreinoUsuario = function(){
        TreinoPersonalizadoService.carregaTreinoUsuario(IdUsuario).success(function(data){
          $scope.usu = data;
        });

     }

     $scope.excluir = function(){

      alert("Oi");
     }


      carregaTreinoUsuario();
    });
