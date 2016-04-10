angular.module('jfrontend')
    .directive('modalEditMailTask', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/mailConfig/modalEditMailTask.html',
            controller: 'mailConfig',
            controllerAs: 'mailC'
        };
    })
    .directive('modalShowMailUpdate', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/mailConfig/modalShowMailUpdate.html',
            controller: 'mailConfig',
            controllerAs: 'mailC'
        };
    });