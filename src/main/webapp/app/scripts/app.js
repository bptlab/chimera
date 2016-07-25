'use strict';
// global constants
var JEngine_Server_URL = window.location.origin;
var InstanceName = window.location.pathname.split('/')[1];
var JUserManagement_Server_URL = window.location.origin;
// vars defining the URIs of the REST-APIs
var JCore_REST_Interface = InstanceName + "/api/interface/v2";
var JConfig_REST_Interface = InstanceName + "/api/config/v2";
var JHistory_REST_Interface = InstanceName + "/api/history/v2";
var JAnalytics_REST_Interface = InstanceName + "/api/analytics/v2";
var JUserManagement_REST_Interface = InstanceName + "/api/interface/v1";

// SSE Event Receiver for reloading the page when an event occurs
var source = new EventSource(JEngine_Server_URL + '/' + InstanceName + '/sse');

(function () {
    var jfrontend = angular.module('jfrontend', [
        'ngRoute',
        'adminConfiguration',
        'ui.bootstrap'
        ]);

    // Defining Routes for the AngularJS App
    jfrontend.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                // route for scenario view
                when('/scenario/', {
                    templateUrl: 'app/views/scenario.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                // route for scenario detail view
                when('/scenario/:id/', {
                    templateUrl: 'app/views/scenarioDetails.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                // route for scenario instance detail view
                when('/scenario/:id/instance/:instanceId/', {
                    templateUrl: 'app/views/scenarioInstanceDetails.html',
                    controller: 'ScenarioInstanceController',
                    controllerAs: 'instanceCtrl'
                }).
                // route for mail task configuration
                when('/admin/mail/', {
                    templateUrl: 'app/views/mailConfigDetails.html',
                    controller: 'mailConfig',
                    controllerAs: 'mailC'
                }).
                // route for JUserManagemetn configuration
                when('/admin/userMgmt/', {
                    templateUrl: 'app/views/userMgmtConfig.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                // route for user dashboard
                when('/admin/user/', {
                    templateUrl: 'app/views/user.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                // default route
                otherwise({redirectTo: '/scenario/'});
        }
    ]);
})();
