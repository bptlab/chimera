angular.module('jfrontend')
    .directive('modalDeleteInstance', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/modalDeleteInstance.html'
        };
    })
    .directive('modalNewInstance', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/modalNewInstance.html'
        };
    })
    .directive('modalTerminationCondition', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/modalTerminationCondition.html'
        };
});