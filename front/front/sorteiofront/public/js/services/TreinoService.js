angular.module('myHeroTraining').factory('TreinoService', function ($http) {
  var carregaTreinos = function (qnt, pg) {
    //return $http.get('https://localhost:80880/treinos', qnt, pg);
  };
  var carregaFasesTreino = function (id) {
  //  var jwt = localStorage.getItem('Bearer');

 //   $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.get('http://localhost:8080/treinos', {
      params: {
        id: id,
      },
    });
  };
  var atualizapontosUsu = function(id){
    var jwt = localStorage.getItem('Bearer');
    return $http.get('http://localhost:8080/exercicio/atualiza',{
      
      params: {
      id: id,
    },
    });
     };
  var carregaExercicios = function (id, pagina, qnt) {
    var jwt = localStorage.getItem('Bearer');

   // $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.get('http://localhost:8080/exercicio', {
      params: {
        id: id,
        pagina: pagina,
        qnt: qnt,
      },
    });
  };
  var carregaIdTreino = function (id) {
    var jwt = localStorage.getItem('Bearer');

    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.get('http://localhost:8080/fase/treino', {
      params: {
        id: id,
      },
    });
  };

  var atualizaFaseConcluida = function (id) {
    var jwt = localStorage.getItem('Bearer');

    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.put('http://localhost:8080/fase/' + id);
  };
  var atualizaIdusuarioTreino = function (model) {
    //console.log('teste' + model.id);
    var jwt = localStorage.getItem('Bearer');

    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.post(
      'http://localhost:8080/treinousuario',
      model
    );
  };

  var salvaDataFimFase = function(dadosSalvaFimFase){
    return $http.post('http://localhost:8080/dataFase', dadosSalvaFimFase);
  }

  var buscaIdUsuario = function (token) {
    return $http.get('/usuario', token);
  };
  var buscaTreinosFeitos = function (IdUsuario,data) {
    var jwt = localStorage.getItem('Bearer');

    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.get(
      'http://localhost:8080/treinousuario/recupera',
      {
        params: {
          id: IdUsuario,
          data : data

        },
      }
    );
  };
  var atualizaIdUsuario = function (faseConcluida) {
    var jwt = localStorage.getItem('Bearer');
    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;

    return $http.put('http://localhost:8080/fase', {
      params: {
        faseConcluida: faseConcluida,
      },
    });
  };
  var fotoFase = function (id) {
    var jwt = localStorage.getItem('Bearer');
    // $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;

    return $http.get('http://localhost:8080/fase/recupera', {
      params: {
        id: id,
      },
    });
  };
  var dadosCadastro = function (id) {
    var jwt = localStorage.getItem('Bearer');

    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.get('http://localhost:8080/cadastro-usuario', {
      params: {
        id: id,
      },
    });
  };
  var salvaTimeCronometroService = function (param) {
    var jwt = localStorage.getItem('Bearer');

    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.post('http://localhost:8080/tempo', param);
  };
  var getTimeCronometroService = function (id_usuario, id_fase) {
    var jwt = localStorage.getItem('Bearer');

    //$http.defaults.headers.common.Authorization = 'Bearer ' + jwt;

    return $http.get('http://localhost:8080/tempo', {
      params: {
        id_usuario: id_usuario,
        id_fase: id_fase,
      },
    });
  };
  return {
    getTimeCronometroService: getTimeCronometroService,
    salvaTimeCronometroService: salvaTimeCronometroService,
    carregarTreinos: carregaTreinos,
    carregaFasesTreino: carregaFasesTreino,
    carregaExercicios: carregaExercicios,
    carregaIdTreino: carregaIdTreino,
    atualizaFaseConcluida: atualizaFaseConcluida,
    atualizaIdusuarioTreino: atualizaIdusuarioTreino,
    buscaTreinosFeitos: buscaTreinosFeitos,
    atualizaIdUsuario: atualizaIdUsuario,
    fotoFase: fotoFase,
    dadosCadastro: dadosCadastro,
    salvaDataFimFase : salvaDataFimFase,
    atualizapontosUsu : atualizapontosUsu
  };
});
