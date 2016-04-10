angular.module('jfrontend')
    .directive('modalDeleteInstance', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/modalDeleteInstance.html',
            controller: 'ScenarioController',
            controllerAs: 'scenarioCtrl'
        };
    })
    .directive('modalNewInstance', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/modalNewInstance.html',
            controller: 'ScenarioController',
            controllerAs: 'scenarioCtrl'
        };
    })
    .directive('modalTerminationCondition', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/modalTerminationCondition.html',
            controller: 'ScenarioController',
            controllerAs: 'scenarioCtrl'
        };
});