angular.module('jfrontend')
    .directive('modalBeginActivity', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/modalBeginActivity.html'
        };
    })
    .directive('modalTerminateActivity', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/modalTerminateActivity.html'
        };
});