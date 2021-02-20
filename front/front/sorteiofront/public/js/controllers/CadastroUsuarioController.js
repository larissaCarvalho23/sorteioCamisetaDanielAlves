angular
  .module('myHeroTraining')
  .controller('cadastroController', function ($scope, $route, cadastroService) {
    $scope.model = {};
    $scope.getkeys = function () {
      const pass = $scope.model.senha;
      if (pass != null) {
        if (pass.length >= 8) {
          $scope.m8charactersC = {
            color: 'green',
          };
        } else {
          $scope.m8charactersC = {
            color: 'black',
          };
        }
      }

      if (pass.match(/([0-9])/)) {
        $scope.numberC = {
          color: 'green',
        };
      } else {
        $scope.numberC = {
          color: 'black',
        };
      }

      if (pass.match(/[-@#!$%^&*()_+|~=`{}\[\]:";'<>?,.\/]/)) {
        $scope.symbolsC = {
          color: 'green',
        };
      } else {
        $scope.symbolsC = {
          color: 'black',
        };
      }
      if (pass.match(/[a-z]/)) {
        $scope.charactersMinC = {
          color: 'green',
        };
      } else {
        $scope.charactersMinC = {
          color: 'black',
        };
      }

      if (pass.match(/[A-Z]/)) {
        $scope.charactersMaxC = {
          color: 'green',
        };
      } else {
        $scope.charactersMaxC = {
          color: 'black',
        };
      }
    };
    $scope.salvar = function () {
      if ($scope.formulario.$valid) {
        cadastroService
          .incluir($scope.model)
          .success(function (data) {
            //alert('Cadastro realizado com sucesso!');
            swal(
              'Cadastro realizado com sucesso',
              'Enviamos um e-mail de confirmação para seu endereço cadastrado. Ao acessar o e-mail, clique em "Minha Conta" para validar o cadastro'
            );
            $route.reload();
            enviarEmail();
            usuarioCadastro();
          })
          .error(function (data, status) {
            alert('Email já cadastrado!');
            $route.reload();
          });
        //fazer tratamento de erro caso não retorno com sucess
      } else if ($scope.formulario.$invalid) {
        alert('Dados inválidos');
      }
    };
    var enviarEmail = function () {
      cadastroService.email($scope.model.email).success(function (data) {
        
      });
    };
    var usuarioCadastro = function () {
      cadastroService.usuario($scope.model);
    };
  });
