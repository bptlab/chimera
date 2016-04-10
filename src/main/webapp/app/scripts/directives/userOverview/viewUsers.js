angular.module('jfrontend')
    .directive('viewUsers', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userOverview/userOverview.html',
            controller: 'userMgmtController',
            controllerAs: 'userMgmtC'
        };
    });