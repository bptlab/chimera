angular.module('jfrontend')
    .directive('scenarioMetaData', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/scenarioDetails/scenarioMetaData.html'
        };
    });