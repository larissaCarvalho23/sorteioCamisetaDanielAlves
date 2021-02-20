angular.module('myHeroTraining').factory('TreinoPersonalizadoService', function ($http) {
       var salvar = function (dados) {
  
   //  var jwt = localStorage.getItem('Bearer');
  
     // $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
      return $http.post('http://localhost:8080/treinoPersonalizado', dados)
     
    };

    var carregaTreino = function(id){
     // var jwt = localStorage.getItem('Bearer');
  
    //  $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
  
        return $http.get('http://localhost:8080/treinoPersonalizado', {
            params: {
              id: id              
            },
          });
        }
        
     
    var carregaTreinoUsuario = function(id){
   //   var jwt = localStorage.getItem('Bearer');
  
  //    $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
  
      return $http.get('http://localhost:8080/treinoPersonalizado/dadosUsuario', {
          params: {
            id: id              
          },
        });
      }
      var apaga = function(id){
    //    var jwt = localStorage.getItem('Bearer');
  
    //  $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
  
        return $http.delete('http://localhost:8080/treinoPersonalizado/apaga', {
            params: {
              id: id              
            },
          });
        }   
    return {
    salvar: salvar,
    carregaTreino: carregaTreino,
    carregaTreinoUsuario : carregaTreinoUsuario,
    apaga : apaga
          };

          
  });
  