angular
  .module('myHeroTraining')
  .controller('login-controller', function ($scope, loginService, $location) {
    $scope.model = {};
    $scope.logar = function () {
      this.erro = '';
      loginService.autenticar($scope.model).then(success, error);
      function success(response) {
        carregaid($scope.model.email);
        localStorage.setItem(response.data.tipo, response.data.token);

        $location.path('/home');
        swal('MyHeroTraining', 'Bem Vindo!', 'success');
      }
      function error(response) {
        console.log(response.status);
        if (response.status == 403) {
          swal(
            'Confirme sua conta primeiro!',
            'Enviamos um e-mail de confirmação para seu endereço cadastrado.',
            'error'
          );
          $location.path('/login');
        } else if (response.status == 401) {
          swal(
            'Não autorizado!',
            'Seu usuario e/ou senha estão errados.',
            'error'
          );
          $location.path('/login');
        } else {
          swal(
            'Erro!',
            'Por algum motivo não podemos realizar seu login!',
            'error'
          );
        }
      }
    };
    var id = sessionStorage.getItem('id');
    var carregaid = function (email) {
      loginService.dadosLogin(email).success(function (data) {
        sessionStorage.setItem('id', data[0].id);
      });
    };
  });