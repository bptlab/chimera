angular.module('jfrontend')
    .directive('modalCreateRole', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/modalCreateRole.html'
        };
    })
    .directive('modalDeleteRole', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/modalDeleteRole.html'
        };
    })
    .directive('modalUpdateRole', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/modalUpdateRole.html'
        };
    })
    .directive('modalCreateUser', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/modalCreateUser.html'
        };
    })
    .directive('modalDeleteUser', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/modalDeleteUser.html'
        };
    })
    .directive('modalUpdateUser', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/userMgmt/modalUpdateUser.html'
        };
    });