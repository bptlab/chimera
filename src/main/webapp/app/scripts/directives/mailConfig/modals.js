angular.module('jfrontend')
    .directive('modalEditMailTask', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/mailConfig/modalEditMailTask.html'
        };
    })
    .directive('modalShowMailUpdate', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/mailConfig/modalShowMailUpdate.html'
        };
    });