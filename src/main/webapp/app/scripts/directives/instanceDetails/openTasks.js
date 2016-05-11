angular.module('jfrontend')
    .directive('openTasks', function() {
        return {
            restrict: 'AE',
            templateUrl: 'app/views/directives/instanceDetails/openTasks.html'
        };
});