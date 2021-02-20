angular
    .module('myHeroTraining')
    .controller('SolicitacoesController', function (
        $scope,
        $routeParams,
        amigosService,
        $location
    ) {
        var IdUsuario = sessionStorage.getItem('id');

        $scope.solicitacoes = function () {
            amigosService.carregarSolicitacoes(IdUsuario).success(function (data) {
                $scope.usuarios = data;
                console.log($scope.usuarios);
            }).error(function(data){
                console.log("erro");
                console.log(data);
            });
        }

        $scope.aceitarSolicitacao = function (amizadeid) {

            amigosService.aceitarSolicitacao(amizadeid, IdUsuario).success(function (data){
                $scope.solicitacoes();
            }).error(function (data){
                console.log("erro");
                console.log(data);
            });
        }

        $scope.recusarSolicitacao = function (amizadeid) {

            amigosService.recusarSolicitacao(amizadeid, IdUsuario).success(function (data){
                $scope.solicitacoes();
            }).error(function (data){
                console.log("erro");
                console.log(data);
            });
        }

        $scope.solicitacoes();
    });

