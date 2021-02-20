angular
  .module('myHeroTraining')
  .controller(
    'desempenhoController',
    function ($scope, $http, desempenhoService, $location) {
      $scope.model = {};   
      var exibe = 0;
      var IdUsuario = sessionStorage.getItem('id');

     $scope.busca = function(){
       var data = $scope.model.dt;
       var dataf = $scope.model.df;
        let dataFormatada =  ((data.getFullYear())) + "/" + (("0" + (data.getMonth() + 1)).slice(-2)) + "/" + data.getDate();
        let dataFormatadaf =  ((dataf.getFullYear())) + "/" + (("0" + (dataf.getMonth() + 1)).slice(-2)) + "/" + dataf.getDate();
        desempenhoService.busca(IdUsuario,dataFormatada, dataFormatadaf).success(function(data)

          {
         
              $scope.dados = data;
              exibe = 1;  

          }).error(function(data){
            if(data.status === 403){
              $location.path('/login');
            }
          });
      }
    
      $scope.exibir = function(){

        if(exibe === 1)
        return true;

      }

     var carregaDados = function() {
      desempenhoService.carregaDados(IdUsuario).success(function(data)
      {
        $scope.usu = data;
      }).error(function(data){
        if(data.status === 403){
          $location.path('/login');
        }
      }); 

     };
     carregaDados();

    }  
    

  );
