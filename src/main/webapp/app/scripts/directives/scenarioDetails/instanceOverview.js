angular.module('jfrontend')
    .directive('instanceOverview', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/instanceOverview.html',
            controller: 'ScenarioController',
            controllerAs: 'scenarioCtrl'
        };
});