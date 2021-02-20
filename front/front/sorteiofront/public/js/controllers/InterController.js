angular.module('myHeroTraining').controller('PageCtrl',function ($scope,$translate ) {
  
$scope.alterarIdioma = function(chave){
	
	$translate.use(chave);
};
});
