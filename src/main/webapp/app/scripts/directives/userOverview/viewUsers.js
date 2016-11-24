angular.module('jfrontend')
    .directive('viewUsers', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userOverview/viewUsers.html'
        };
    });