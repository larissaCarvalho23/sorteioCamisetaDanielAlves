angular
  .module('myHeroTraining')
  .controller('classificacaoController', function (
    $scope,
    classificacaoservice,
    
  ) {

    var carregaClassificacao = function(){
        classificacaoservice.carregaClassificacao().success(function(data){
            $scope.classificacao = data;
           
        }).error(function(data){
        if(data.status === 403){
          $location.path('/login');
        }
      });
  
    }
    carregaClassificacao();
    
  });
