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
