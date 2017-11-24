angular.module('jfrontend')
    .directive('terminationCondition', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/terminationCondition.html'
        };
    });