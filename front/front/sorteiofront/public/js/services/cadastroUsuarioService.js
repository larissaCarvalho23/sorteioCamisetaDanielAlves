angular.module('myHeroTraining').factory('cadastroService', function ($http) {
  var incluir = function (model) {
    var jwt = localStorage.getItem('Bearer');

    $http.defaults.headers.common.Authorization = 'Bearer ' + jwt;
    return $http.post(
      'http://localhost:8080/cadastro-usuario',
      model
    );
  };
  var email = function (email) {
    return $http.post('http://localhost:8080/email', email);
  };
  var usuario = function (model) {
    return $http.post('http://localhost:8080/usuario', model);
  };
  var confirmar = function (params) {
    return $http.post(
      'http://localhost:8080/confirm-account',
      params
    );
  };
  var recuperar = function (email) {
    return $http.post(
      'http://localhost:8080/forgot-password',
      email
    );
  };
  var trocar = function (model) {
    return $http.post(
      'http://localhost:8080/confirm-reset',
      model
    );
  };

  return {
    incluir: incluir,
    email: email,
    usuario: usuario,
    confirmar: confirmar,
    recuperar: recuperar,
    trocar: trocar,
  };
});
