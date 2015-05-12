// Constants
var JEngine_Server_URL = window.location.origin;
var JUserManagement_Server_URL = window.location.origin;
var JCore_REST_Interface = "JEngine/api/interface/v2";
var JConfig_REST_Interface = "JEngine/api/config/v2";
var JHistory_REST_Interface = "JEngine/api/history/v2";
var JComparser_REST_Interface = "JEngine/api/jcomparser";
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
                // Routes for jCore
                when('/scenario', {
                    templateUrl: 'asset/templates/scenario.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                when('/scenario/:id', {
                    templateUrl: 'asset/templates/scenarioDetails.html',
                    controller: 'ScenarioController',
                    controllerAs: 'scenarioCtrl'
                }).
                when('/scenario/:id/instance', {
                    templateUrl: 'asset/templates/scenarioInstance.html',
                    controller: 'ScenarioInstanceController',
                    controllerAs: 'instanceCtrl'
                }).
                when('/scenario/:id/instance/:instanceId', {
                    templateUrl: 'asset/templates/scenarioInstanceDetails.html',
                    controller: 'ScenarioInstanceController',
                    controllerAs: 'instanceCtrl'
                }).
                // Routes for Admin Dashbaord
                when('/admin/jcomparser/', {
                    templateUrl: 'asset/templates/jcomparser.html',
                    controller: 'jcomparserMainView',
                    controllerAs: 'jcomparserMV'
                }).
                when('/admin/mail/', {
                    templateUrl: 'asset/templates/mailConfigDetails.html',
                    controller: 'mailConfig',
                    controllerAs: 'mailC'
                }).
                when('/admin/webservice/', {
                    templateUrl: 'asset/templates/webserviceConfigDetails.html',
                    controller: 'webserviceConfig',
                    controllerAs: 'webserviceC'
                }).
                when('/admin/core/', {
                    templateUrl: 'asset/templates/jcoreConfig.html',
                    controller: 'jcoreConfig',
                    controllerAs: 'jcoreC'
                }).
                when('/admin/userMgmt/', {
                    templateUrl: 'asset/templates/userMgmtConfig.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                when('/admin/user/', {
                    templateUrl: 'asset/templates/user.html',
                    controller: 'userMgmtController',
                    controllerAs: 'userMgmtC'
                }).
                // default Route
                otherwise({
                    redirectTo: '/scenario'
                });
        }
    ]);
})();
