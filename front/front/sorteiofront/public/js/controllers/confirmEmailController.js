angular.module('myHeroTraining').controller('confirmEmail',function ($scope, $location, $routeParams, cadastroService ) {
    $scope.init = function () {
            let params;

        function successCallback() {
            $location.path('/account-verified');
        }

        function errorCallback() {
            $location.path('/account-verified-error');
        }

        if ($routeParams != null) {
                params = $routeParams.token;
                cadastroService.confirmar(params).then(successCallback, errorCallback)
            } else {
        }
        }
        $scope.init();
});
