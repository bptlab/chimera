angular.module('jfrontend')
    .directive('logs', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/logs.html',
            controller: 'ScenarioInstanceController',
            controllerAs: 'instanceCtrl'
        };
});