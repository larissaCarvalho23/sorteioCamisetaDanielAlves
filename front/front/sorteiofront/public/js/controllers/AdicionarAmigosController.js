angular
    .module('myHeroTraining')
    .controller('AdicionarAmigosController', function (
        $scope,
        $routeParams,
        amigosService,
        $location
    ) {
        var usuarioid = sessionStorage.getItem('id');

        $scope.enviar = function () {
            amigosService.enviarSolicitacao(usuarioid, $scope.email).success(function (data) {
                console.log($scope.email);
                console.log(usuarioid);
            }).error(function(data){
                console.log("erro");
                console.log(data);
            });
        }

    });
