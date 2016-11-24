angular.module('jfrontend')
    .directive('roleManagement', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/roleManagement.html'
        };
    });