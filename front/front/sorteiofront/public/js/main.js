angular
  .module("myHeroTraining", ["ngRoute", "ngSanitize", "pascalprecht.translate"])
  .config(function ($routeProvider, $translateProvider) {
    //  $locationProvider.html5Mode(true);
   
    $routeProvider.when("/sorteio", {
      templateUrl: "./view/home.html",
      controller: "HomeController",
    });
    
  });
