'use strict';

angular.module('jfrontend')
    .controller('mailConfig', ['$routeParams', '$location', '$http', '$scope',
        function ($routeParams, $location, $http, $scope) {
            var controller = this;

            // initialize an empty list of scenario Ids, details for each Id and email tasks id array
            this.Details = [];
            this.emailtaskIDs = [];
            this.scenarioIDs = [];
            this.detailsForID = [];
            

            //requesting initially all available scenarios
            $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").success(function (data) {
                controller.scenarioIDs = data['labels'];

            }).error(function () {
                console.log('request failed');
            });

            //post update for email tasks
            this.submitMyForm = function () {
                //using the data set in the form as request content
                var data = $scope.form;
                $http.put(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/emailtask/" + controller.workingID + "/?", data).success(function (data) {
                    controller.loadData();
                }).error(function () {
                    console.log('request failed');
                });

            }

            //get all infos for popup
            this.getDetails = function (id) {
                //retrieving all details for a specific email task ID
                controller.getDetailsForMailtaskID(id);
                // we are setting our workingID so we can access it more easy and dont have to grind it over the URL
                controller.workingID = id;
            };

            // Got all emailtasks with the given Id
            this.getAllMailtaskForScenarioID = function (id) {
				//if no scenario selected do nothing
				if(id==null){
					return;
				}
            	//save the given scenario ID to later identify the scenario 
            	this.currentScenarioID=id;
                $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/scenario/" + id + "/emailtask/").success(function (data) {
                    controller.emailtaskIDs = data['ids'];
                    //if the emailtaskIDs array is not empty, prefetch the first item details
                    if (controller.emailtaskIDs.length > 0) {
                        controller.getDetails(controller.emailtaskIDs[0], id);
                    }
                }).error(function () {
                    console.log('request failed');
                });
            };
            // Got to the instance with the given Id
            this.getDetailsForMailtaskID = function (id) {
            	//use the previously stored currentScenarioID and the emailtask ID to get the Mailtask Details
                $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface +
                    "/scenario/" + this.currentScenarioID +"/emailtask/" + id + "/?").success(function (data) {
                    // we are storing the data duplicated for faster access, once in the detailsForID array
                    controller.detailsForID = data;
                    // again in the form so the user can edit them directly
                    $scope.form = {
                        receiver: data['receiver'],
                        subject: data['subject'],
                        message: data['message']
                    };
                });
            };

            //reloading data for better UIX
            this.loadData = function () {
                controller.getDetailsForMailtaskID(controller.workingID);
            };
        }]
    );