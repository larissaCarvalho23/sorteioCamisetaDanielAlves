angular
  .module('myHeroTraining')
  .controller(
    'recoverPassword',
    function ($scope, $location, $routeParams, cadastroService) {
      $scope.recuperarSenha = function () {
        $scope.erro = false;
        $scope.carregando = true;
        function successCallback() {
          console.log('sucesso');
          $location.path('/success-send-reset');
        }

        function errorCallback() {
          $scope.carregando = false;
          $scope.erro = true;
        }

        cadastroService
          .recuperar($scope.model.email)
          .then(successCallback, errorCallback);
      };

      $scope.trocarSenha = function () {
        let params, pass, send;
        function successCallback() {
          $location.path('/confirm-reset-success');
        }

        function errorCallback() {
          $location.path('/confirm-reset-fail');
        }

        if ($routeParams != null) {
          params = $routeParams.token;
          pass = $scope.model.senha;
          send = {
            pass: pass,
            params: params,
          };
          cadastroService.trocar(send).then(successCallback, errorCallback);
        } else {
        }
      };
    }
  );
