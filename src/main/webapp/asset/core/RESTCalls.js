// JEngine Server URL
var JEngine_Server_URL = "http://172.16.64.113:8080";

// REST Interface of the JEngine
var JCore_REST_Interface_Version = "v1";
var JCore_REST_Interface = "JEngine/api/interface/" + JCore_REST_Interface_Version +"/en";

// REST Interface of the JComparsers
var JComparser_REST_Interface = "JEngine/api/jcomparser";


/**************************************************
*
* Serving the API specification of REST interface v1
* 
*/

function GetAllScenarios($scope, $http) {
    $http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/0/").
        success(function(data) {
            $scope.scenarios = data;
        });
}

function GetScenarioDetails($scope, $http) {
    $http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ $scope.scenario+"/").
        success(function(data) {
            $scope.scenarioDetails = data;
        });
}

function GetScenarioInstances($scope, $http) {
    $http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ $scope.scenario+"/instance/0/").
        success(function(data) {
            $scope.scenarioInstances = data;
        });
}

function GetScenarioInstanceDetails($scope, $http) {
    $http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ $scope.scenario+"/instance/"+ $scope.instance+ "/").
        success(function(data) {
            $scope.scenarioInstances = data;
        });
}

function GetActivitiesForScenarioInstances($scope, $http) {
    $http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ $scope.scenario+"/instance/"+ $scope.instance+ "/activityinstance/0/?status="+ $scope.state).
        success(function(data) {
            $scope.activities = data;
        });
}

function GetActivitiesLabelByID($scope, $http) {
    $http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ $scope.scenario+"/instance/"+ $scope.instance+ "/activityinstance/"+ $scope.activity+"/").
        success(function(data) {
            $scope.activityDetails = data;
        });
}

function GetAllDataobject($scope, $http) {		
    $http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ $scope.scenario+"/instance/"+ $scope.instance+ "/dataobject/0/").
        success(function(data) {
            $scope.dataobject = data;
        });
}

//TODO: implement POSTs