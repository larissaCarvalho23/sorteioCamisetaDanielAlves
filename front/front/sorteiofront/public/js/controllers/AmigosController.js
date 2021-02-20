angular
    .module('myHeroTraining')
    .controller('AmigosController', function (
        $scope,
        $routeParams,
        amigosService,
        $location
    ) {
        var IdUsuario = sessionStorage.getItem('id');

        $scope.InitAmigos = function () {
            amigosService.carregarAmigos(IdUsuario).success(function (data) {
                $scope.amigos = data;
                console.log($scope.amigos);
            }).error(function (data) {
                console.log("erro");
                console.log(data);
            });
        }

        $scope.removerAmigo = function (amizadeid) {

            amigosService.recusarSolicitacao(amizadeid, IdUsuario).success(function (data){
                $scope.InitAmigos();
            }).error(function (data){
                console.log("erro");
                console.log(data);
            });
        }

        $scope.InitAmigos();
    });