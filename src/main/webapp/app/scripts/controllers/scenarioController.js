'use strict';

angular.module('jfrontend')
    .controller('ScenarioController', ['$routeParams', '$location', '$http', '$scope',
        function ($routeParams, $location, $http, $scope) {
            $scope.$on('$viewContentLoaded', function () {
            	// TODO: think wether this should be called everytime just CaseModel gets refreshed
                console.log($routeParams.id);
                
                //if we are within the scenario layer
                if ($routeParams.id != null) {
                    // setting current id of scenario based on the URI
                    controller.currentScenario['id'] = $routeParams.id;
                    // fetching details for this scenario
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + controller.currentScenario['id'] + "/").success(function (data) {
                        controller.currentScenario['details'] = data;
                    }).error(function () {
                        console.log('request failed');
                    });
                    // requesting additional informations for this scenario
                    controller.getInstancesOfScenario(controller.currentScenario['id']);
                    controller.getTerminationConditionOfScenario(controller.currentScenario['id']);
                }
            });
            // For accessing data from inside the $http context
            var controller = this;

            // initialize empty objects within the scope of the controller
            this.currentScenario = {};
            this.scenarios = {};

            // pre fetch all scenarios within the JEngine
            $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").success(function (data) {
                controller.scenarios = data;
            });

            // retrieve all instances of a specified scenario
            this.getInstancesOfScenario = function (id) {
                // defining the algorithm for log analysis details
                var algorithm = "de.uni_potsdam.hpi.bpt.bp2014.janalytics.ExampleAlgorithm";
                //fetching all scenarios
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/instance/").success(function (data) {
                    // "persisting" data within stable environment
                    controller.currentScenario['instances'] = data;
                    //TODO: implement "Average Duration"
                    // initializing execution of specified algorithm
                    /*
                    $http.post(JEngine_Server_URL + "/" + JAnalytics_REST_Interface + "/services/" + algorithm, {"args": [id]})
                        .success(function (data) {
                            // retrieving results of algorithm as soon as POST is done successfully
                            controller.currentScenario['duration'] = data['meanScenarioInstanceRuntime'];
                            *//*$http.get(JEngine_Server_URL + JAnalytics_REST_Interface + "/services/" + algorithm)
                             .success(function (data) {
                             controller.currentScenario['duration'] = data['meanScenarioInstanceRuntime'];
                             })*//*
                        })
                     */
                }).error(function () {
                    console.log('request failed');
                });
            };

            // retrieving the termination condition for this scenario
            this.getTerminationConditionOfScenario = function (id) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/terminationcondition/").success(function (data) {
                    controller.currentScenario['terminationcondition'] = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

            // requesting details for a specified scenario
            this.getScenarioDetails = function (id) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/").success(function (data) {
                    controller.currentScenario['details'] = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

            // navigating to the specified scenario
            this.goToDetailsFrom = function (id) {
                $location.path('scenario/' + $routeParams.id + '/instance/' + id);
            };

            // helper for accessing scenario details for a scenario
            this.getCurrentScenario = function () {
                if ($routeParams.id != null) {
                    controller.getScenarioDetails($routeParams.id);
                }
            };

            // give the user the possibility to delete a scenario where no instances are running
            this.deleteScenario = function (id) {
                // send HTTP Delete package to JEngine
                $http.delete(JEngine_Server_URL + "/" + JCore_REST_Interface +
                    "/scenario/" + id + "/?").success(function (data) {
                    console.log("deleting scenario was successful..");
                });
                //load new scenario list
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").success(function (data) {
                    controller.scenarios = data;
                });
                //navigating to upper scenario level
                $location.path("/scenario/");
            };

            // retrieving the termination condition for this scenario
            this.getTerminationConditionOfScenario = function (id) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/terminationcondition/").success(function (data) {
                    controller.currentScenario['terminationcondition'] = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

            // Creates a new instance of the scenario with the given Id
            this.createInstance = function (id) {
                //if name was set we are using the PUT call
                if ($scope.instanceName) {
                	console.log($scope.instanceName);
                    // building the json content for naming the instance
                    var data = "{\"name\":\"" + $scope.instanceName + "\"}";

                    $http.put(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/instance/", data).success(function (response) {
                        $location.path("/scenario/" + id + "/instance/" + response['id']);
                    }).error(function () {
                        console.log('request failed');
                    });
                    // otherwise use the post with default name
                } else {
                    $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/instance/").success(function (data) {
                        $location.path("/scenario/" + id + "/instance/" + data['id']);
                    }).error(function () {
                        console.log('request failed');
                    });
                }
            };

            source.addEventListener('refresh', function (event) {
                controller.getInstancesOfScenario(controller.currentScenario['id']);
                controller.getTerminationConditionOfScenario(controller.currentScenario['id']);
            });

        }]
    );
