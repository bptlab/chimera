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
var JComparser_REST_Interface = InstanceName + "/api/jcomparser";
var JUserManagement_REST_Interface = "JUserManagement/api/interface/v1";

(function () {
    var jfrontend = angular.module('jfrontend', [
        'ui.router',
        'adminConfiguration',
        'userInteraction']);

    // Defining Routes for the AngularJS App
    jfrontend.config(['$stateProvider', '$urlRouterProvider',
        function ($stateProvider, $urlRouterProvider) {
            $urlRouterProvider.otherwise('/otherwise');

            $stateProvider.
                // route for scenario view
                state('scenario', {
                    url: '/scenario',
                    templateUrl: 'app/views/scenario.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                // route for scenario detail view
                state('scenario', {
                    url: '/scenario/:id',
                    templateUrl: 'app/views/scenarioDetails.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                // route for scenario instance detail view
                state('scenarioInstanceId', {
                    url: '/scenario/:id/instance/:instanceId',
                    templateUrl: 'app/views/scenarioInstanceDetails.html',
                    controller: 'ScenarioInstanceController',
                    controllerAs: 'instanceCtrl'
                }).
                // route for mail task configuration
                state('adminMail', {
                    url: '/admin/mail',
                    templateUrl: 'app/views/mailConfigDetails.html',
                    controller: 'mailConfig',
                    controllerAs: 'mailC'
                }).
                // route for web service task configuration
                state('adminWebservice', {
                    url: '/admin/webservice/',
                    templateUrl: 'app/views/webserviceConfigDetails.html',
                    controller: 'webserviceConfig',
                    controllerAs: 'webserviceC'
                }).
                // route for JUserManagemetn configuration
                state('adminUserMgmt', {
                    url: '/admin/userMgmt/',
                    templateUrl: 'app/views/userMgmtConfig.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                // route for user dashboard
                state('adminUser', {
                    url: '/admin/user/',
                    templateUrl: 'app/views/user.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                // default route
                state('otherwise', {
                    url: '/otherwise',
                    templateUrl: 'app/views/scenario.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                });
        }
    ]);
})();