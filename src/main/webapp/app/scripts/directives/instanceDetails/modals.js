angular.module('jfrontend')
    .directive('modalBeginActivity', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/modalBeginActivity.html'
        };
    })
    .directive('modalInspectDataobject', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/modalInspectDataobject.html'
        }
    })
    .directive('modalTerminateActivity', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/modalTerminateActivity.html'
        };
    })
    .directive('modalInfoActivity', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/modalInfoActivity.html'
        };
    });