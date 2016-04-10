angular.module('jfrontend')
    .directive('userManagement', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/userManagement.html',
            controller: 'userMgmtController',
            controllerAs: 'userMgmtC'
        };
});