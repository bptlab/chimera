angular.module('jfrontend')
    .directive('dataObjectStatus', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/dataObjectStatus.html',
            controller: 'ScenarioInstanceController',
            controllerAs: 'instanceCtrl'
        };
});