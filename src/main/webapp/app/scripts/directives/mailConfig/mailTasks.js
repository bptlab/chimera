angular.module('jfrontend')
    .directive('mailTasks', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/mailConfig/mailTasks.html',
            controller: 'mailConfig',
            controllerAs: 'mailC'
        };
});