angular.module('jfrontend')
    .directive('unassignedTasks', function () {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/unassignedTasks.html'
        };
    });