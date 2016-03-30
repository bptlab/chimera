// global constants
var JEngine_Server_URL = window.location.origin;
var InstanceName = window.location.pathname.split('/')[1];
var JUserManagement_Server_URL = window.location.origin;
var JCore_REST_Interface = InstanceName + "/api/interface/v2";
var JConfig_REST_Interface = InstanceName + "/api/config/v2";
var JHistory_REST_Interface = InstanceName + "/api/history/v2";
var JAnalytics_REST_Interface = InstanceName + "/api/analytics/v2";
var JComparser_REST_Interface = InstanceName + "/api/jcomparser";
var JUserManagement_REST_Interface = "JUserManagement/api/interface/v1";

(function () {
    // vars defining the URIs of the REST-APIs
    var jfrontend = angular.module('jfrontend', [
        'ngRoute',
        'adminConfiguration',
        'userInteraction']);

    // Defining Routes for the AngularJS App
    jfrontend.config(['$routeProvider',
        function ($routeProvider) {
            $routeProvider.
                // route for scenario view
                when('/scenario', {
                    templateUrl: 'templates/scenario.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                // route for scenario detail view
                when('/scenario/:id', {
                    templateUrl: 'templates/scenarioDetails.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                // route for scenario instance view
                when('/scenario/:id/instance', {
                    templateUrl: 'templates/scenarioInstance.html',
                    controller: 'ScenarioInstanceController',
                    controllerAs: 'instanceCtrl'
                }).
                // route for scenario instance detail view
                when('/scenario/:id/instance/:instanceId', {
                    templateUrl: 'templates/scenarioInstanceDetails.html',
                    controller: 'ScenarioInstanceController',
                    controllerAs: 'instanceCtrl'
                }).
                // route for jcomparser control panel view
                when('/admin/jcomparser/', {
                    templateUrl: 'templates/jcomparser.html',
                    controller: 'jcomparserMainView',
                    controllerAs: 'jcomparserMV'
                }).
                // route for mail task configuration
                when('/admin/mail/', {
                    templateUrl: 'templates/mailConfigDetails.html',
                    controller: 'mailConfig',
                    controllerAs: 'mailC'
                }).
                // route for web service task configuration
                when('/admin/webservice/', {
                    templateUrl: 'templates/webserviceConfigDetails.html',
                    controller: 'webserviceConfig',
                    controllerAs: 'webserviceC'
                }).
                // route for jcore configuration
                when('/admin/core/', {
                    templateUrl: 'templates/jcoreConfig.html',
                    controller: 'jcoreConfig',
                    controllerAs: 'jcoreC'
                }).
                // route for JUserManagemetn configuration
                when('/admin/userMgmt/', {
                    templateUrl: 'templates/userMgmtConfig.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                // route for user dashboard
                when('/admin/user/', {
                    templateUrl: 'templates/user.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                // default route
                otherwise({
                    redirectTo: '/scenario'
                });
        }
    ]);
})();