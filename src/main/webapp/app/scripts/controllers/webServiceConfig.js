'use strict';

angular.module('jfrontend')
    .controller('webserviceConfig', ['$routeParams', '$location', '$http', '$scope',
        function ($routeParams, $location, $http, $scope) {
            var webserviceC = this;

            // initialize an empty list of scenario Ids
            this.Details = [];
            this.webserviceIDs = [];
            this.scenarioIDs = [];
            this.detailsForID = [];
            this.DataAttributeArray = [];
            //this.NgRepeatAttributeArray = [];

            //requesting initially all available scenarios
            $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").success(function (data) {
                webserviceC.scenarioIDs = data['labels'];

            }).error(function () {
                console.log('request failed');
            });

            //post update for webservice tasks
            this.submitMyForm = function () {
                //using the data set in the form as request content
                var data = $scope.form;
                $http.put(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/webservice/" + webserviceC.workingID + "/?", data);
            }

            this.addAttribute = function (dataattribute_id) {
                //initialising new Attribute object
                webserviceC.newAttribute = {};
                //cloning last entry of the attribute array
                angular.copy($scope.form.attributes[$scope.form.attributes.length - 1], webserviceC.newAttribute);
                //setting controlnode id from working Webservice task ID
                webserviceC.newAttribute['controlnode_id'] = webserviceC.workingID;
                //increasing order id if null set 0
                if (webserviceC.newAttribute['order'] == null) {
                    webserviceC.newAttribute['order'] = 0
                } else {
                    webserviceC.newAttribute['order'] = webserviceC.newAttribute['order'] + 1;
                }
                //setting key to blank
                webserviceC.newAttribute['key'] = "";
                //setting dataattribute ID
                webserviceC.newAttribute['dataattribute_id'] = dataattribute_id;
                //pushing new Attribute into old attribute array
                $scope.form.attributes.push(webserviceC.newAttribute);

                var array_key = $scope.NgRepeatAttributeArray[$scope.NgRepeatAttributeArray.length - 1];
                if (webserviceC.newAttribute['array_key'] == null) {
                    webserviceC.newAttribute['array_key'] = 0;
                } else {
                    webserviceC.newAttribute['array_key'] = array_key['array_key'] + 1;
                }
                //webserviceC.NgRepeatAttributeArray.push(webserviceC.newAttribute);

                webserviceC.getDifferentDataattributes();
            }

            //get all infos for popup
            this.getDetails = function (webserviceID, scenarioID) {
                webserviceC.getDetailsForWebserviceID(webserviceID, scenarioID);
                webserviceC.workingID = webserviceID;
            };

            // Got all webservices for the given  webservice Id
            this.getAllWebservicetaskForScenarioID = function (id) {
                $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/scenario/" + id + "/webservice/").success(function (data) {
                    webserviceC.webserviceIDs = data['ids'];
                    //if the emailtaskIDs array is not empty, prefetch the first item details
                    if (webserviceC.webserviceIDs.length > 0) {
                        webserviceC.getDetails(webserviceC.webserviceIDs[0], id);
                    }
                }).error(function () {
                    console.log('request failed');
                });
            };

            // Got to the instance with the given Id
            this.getDetailsForWebserviceID = function (webserviceID, scenarioID) {
                $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface +
                    "/scenario/" + scenarioID + "/webservice/" + webserviceID + "/?").success(function (data) {
                    webserviceC.detailsForID = data;
                    //$scope.form.attributes.$destroy();
                    //reset attributes otherwise we have duplicates
                    if ($scope.form != null) {
                        $scope.form.attributes = null;
                    }
                    $scope.form = {
                        method: data['method'],
                        link: data['link'],
                        body: data['body'],
                        attributes: data['attributes']
                    };
                    $scope.NgRepeatAttributeArray = data['attributes'];
                    if ($scope.NgRepeatAttributeArray != []) {
                        angular.forEach($scope.NgRepeatAttributeArray, function (value, key) {
                            value.array_key = key;
                            $scope.NgRepeatAttributeArray[key] = value;
                        });
                    }
                    webserviceC.getDifferentDataattributes();
                });
            };

            this.getDifferentDataattributes = function () {
                angular.forEach($scope.form.attributes, function (value, key) {
                    //if the DataAttributeArray doesnt contain the item already, we add it
                    // so we are able to remove duplicates within the array
                    if (webserviceC.DataAttributeArray.indexOf(value['dataattribute_id']) == -1) {
                        //if they are unique then we are adding them as item to the array
                        webserviceC.DataAttributeArray.push(value['dataattribute_id']);
                    }
                });
            };

        }]
    );