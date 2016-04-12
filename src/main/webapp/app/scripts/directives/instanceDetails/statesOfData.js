angular.module('jfrontend')
    .directive('statesOfData', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/statesOfData.html',
            controller: 'ScenarioInstanceController',
            controllerAs: 'instanceCtrl'
        };
});