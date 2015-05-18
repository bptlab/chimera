(function () {
    // defining module within MVC pattern. here, we primary realize the controller action for the user interaction with all compontens of the Jengine
    var userIn = angular.module('userInteraction', []);

    // Create a directive for scenarios menu entry
    userIn.directive('scenarioMenuEntry', function () {
        return {
            restrict: 'A',
            templateUrl: 'asset/templates/scenarioMenuEntry.html',
        };
    });

    // Create a controller for the scenario information
    userIn.controller('ScenarioController', ['$routeParams', '$location', '$http', '$scope',
            function ($routeParams, $location, $http, $scope) {
                // For accessing data from inside the $http context
                var controller = this;

                // initialize an empty list of scenario Ids
                this.currentScenario = {};
                this.scenarios = {};

                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").
                    success(function (data) {
                        controller.scenarios = data;
                    });

                this.getInstancesOfScenario = function (id) {
                    var algorithm = "de.uni_potsdam.hpi.bpt.bp2014.janalytics.ExampleAlgorithm";
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/instance/").
                        success(function (data) {
                            controller.currentScenario['instances'] = data;
							// ************** TODO: Make this pretty! **************//
							$http.post(JEngine_Server_URL + "/" + JAnalytics_REST_Interface + "/services/" + algorithm, {"args":[id]})
								.success(function (data) {
									$http.get(JEngine_Server_URL + JAnalytics_REST_Interface + "/services/"+ algorithm)
										.success(function (data) {
											controller.currentScenario['duration'] = data['meanScenarioInstanceRuntime'];
									})
								})
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

                this.getTerminationConditionOfScenario = function (id) {
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/terminationcondition/").
                        success(function (data) {
                            controller.currentScenario['terminationcondition'] = data;
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

                //if we are within the layer scenario
                if ($routeParams.id != null) {
                    //setting current id of scenario based on the URI
                    controller.currentScenario['id'] = $routeParams.id;
                    //calling details for this scenario
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + controller.currentScenario['id'] + "/").
                        success(function (data) {
                            controller.currentScenario['details'] = data;
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                    controller.getInstancesOfScenario(controller.currentScenario['id']);
                    controller.getTerminationConditionOfScenario(controller.currentScenario['id']);
                }

                this.getScenarioDetails = function (id) {
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/").
                        success(function (data) {
                            controller.currentScenario['details'] = data;
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

                this.getImageForScenario = function (id) {
                    this.scenarios["" + id]['imageUrl'] =
                        JEngine_Server_URL + "/" + JComparser_REST_Interface + "/scenarios/" + id + "/image/";
                };

                this.goToDetailsFrom = function (id) {
                    $location.path('scenario/' + id);
                };

                this.getCurrentScenario = function () {
                    if ($routeParams.id != null) {
                        controller.getScenarioDetails($routeParams.id);
                    }
                };

                this.deleteScenario = function (id) {
                    $http.delete(JEngine_Server_URL + "/" + JConfig_REST_Interface +
                    "/scenario/" + id + "/?").
                        success(function (data) {
                            console.log("deleting scenario was successful..");
                        });
                    //load new scenario list
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").
                        success(function (data) {
                            controller.scenarios = data;
                        });
                    $location.path("/scenario/");
                };

                this.getTerminationConditionOfScenario = function (id) {
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/terminationcondition/").
                        success(function (data) {
                            controller.currentScenario['terminationcondition'] = data;
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

                // Creates a new instance of the scenario with the given Id
                this.createInstance = function (id) {
	 	    //if name was set we are using the PUT call
		    if($scope.instanceName){
		            var data = "{\"name\":\""+$scope.instanceName+"\"}";
				
		            $http.put(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/instance/", data).
		                success(function (response) {
		                    $location.path("/scenario/" + id + "/instance/" + response['id']);
		                }).
		                error(function () {
		                    console.log('request failed');
		                });
		    // otherwise use the post with default name
		    } else {
		            $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/instance/").
		                success(function (data) {
		                    $location.path("/scenario/" + id + "/instance/" + data['id']);
		                }).
		                error(function () {
		                    console.log('request failed');
		                });
	 	     	     }
                };

                /* ____ BEGIN_INITIALIZATION ____ */
                this.initialize = function () {
                    console.info('initializing...'); // not really needed but for some debug purposes always good.
                }

                this.initialize();
                /* ____ END_INITIALIZATION ____ */
            }]
    );

    userIn.controller('ScenarioInstanceController', ['$routeParams', '$location', '$http', '$scope',
        function ($routeParams, $location, $http, $scope) {
            // For accessing data from inside the $http context
            var instanceCtrl = this;

            // initialize an empty object for the instances
            this.instances = {};
            this.instanceDetails = {};
            this.scenario = {};
            this.activityOutput = {};
            this.changeAttributObject = {};

            //post update for webservice tasks
            this.submitAttributeForm = function () {
                //using the put
                var data = $scope.form;
                $http.put(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/webservice/" + webserviceC.workingID + "/?", data);
            }

            /* ____ BEGIN_INITIALIZATION ____ */
            this.initializeActivityInstances = function () {
                instanceCtrl.instanceDetails.activities = {};
                ["ready", "terminated", "running"].forEach(function (state) {
                    var state2 = state;
                    $http.get(
                        JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" +
                        $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/?state=" + state).
                        success(function (data) {
                            instanceCtrl.instanceDetails.activities[state] = data;
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                });
            }

            this.initializeDataobjectInstances = function () {
                instanceCtrl.instanceDetails.dataobjects = {};
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/dataobject/").
                    success(function (data) {
                        instanceCtrl.instanceDetails.dataobjects = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            }
            // activitylogs
            this.initializeActivitylogInstances = function () {
                instanceCtrl.instanceDetails.dataobjects = {};
                $http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/activities/").
                    success(function (data) {
                        instanceCtrl.instanceDetails.activitylogs = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            }
            // dataobjectlogs
            this.initializeDataobjectlogInstances = function () {
                instanceCtrl.instanceDetails.dataobjects = {};
                $http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/dataobjects/").
                    success(function (data) {
                        instanceCtrl.instanceDetails.dataobjectlogs = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            }

            // dataobjectattributeslogs
            this.initializeDataobjectAttributelogInstances = function () {
                instanceCtrl.instanceDetails.dataobjects = {};
                $http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/attributes/").
                    success(function (data) {
                        instanceCtrl.instanceDetails.dataobjectAttributelogs = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            }

            // if necessary initialize the specified Scenario
            this.initialize = function () {
                if ($routeParams.instanceId) {
                    // initialize if necessary the specified instance
                    // The scenario and instance is specified by the routeParams
                    this.initializeInstance($routeParams.instanceId);
                } else {
                    // First get the Scenario InstanceIds
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/").
                        success(function (data) {
                            instanceCtrl.scenario['instances'] = data;
                            instanceCtrl.scenario['id'] = $routeParams.id;
                        }).error(function () {
                            console.log('request failed');
                        });
                }
            };

            // Initializes an Instance object for the given ID by fetching the information
            this.initializeInstance = function (id) {
                $http.get(
                    JEngine_Server_URL + "/" + JCore_REST_Interface +
                    "/scenario/" + $routeParams.id + "/instance/" +
                    id + "/"
                ).success(function (data) {
                        instanceCtrl.instanceDetails['instance_name'] = data['name'];
                        instanceCtrl.instanceDetails['scenario_id'] = $routeParams.id;
                        instanceCtrl.instanceDetails['id'] = id;
                        if ($routeParams.instanceId) {
                            instanceCtrl.initializeActivityInstances();
                            instanceCtrl.initializeDataobjectInstances();
                            instanceCtrl.initializeActivitylogInstances();
                            instanceCtrl.initializeDataobjectlogInstances();
                            instanceCtrl.initializeDataobjectAttributelogInstances();
                            instanceCtrl.getTerminationConditionOfScenario($routeParams.id);
                        }
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            }

            this.initialize();
            /* ____ END_INITIALIZATION ____ */

            // Got to the instance with the given Id
            this.goToDetailsFrom = function (id) {
                $location.path("/scenario/" + $routeParams.id + "/instance/" + id);
            };

            // returns the current instance object
            this.getCurrentInstance = function () {
                instanceCtrl.instanceDetails['id'] = $routeParams.instanceId;
            };
            
            this.getInstanceName = function (instanceID) {
               $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + instanceID).
                        success(function (data) {
                            //return data['name'];
                            instanceCtrl.instanceDetails['instance_name'] = data['name'];
                        }).error(function () {
                            console.log('request failed');
                        });
            }

            this.setAttribute = function (id, value, activityId) {
                var data = {};
                data.id = id;
                data.value = value;

                $http.put(JEngine_Server_URL + "/" + JCore_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/activity/" + activityId, data).
                    success(function (data) {
                        instanceCtrl.initializeDataobjectAttributelogInstances();
                        instanceCtrl.changeAttributObject['' + id] = value;
                        instanceCtrl.getOutputAndOutputsets(activityId);
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            this.setCurrentAttributeObject = function (id, value) {
                instanceCtrl.changeAttributObject['' + id] = value;
            };

            //not needed any more
            this.checkArrayLength = function (id) {
                if (!instanceCtrl.scenario['activity']) {
                    instanceCtrl.scenario['activity'] = {};
                }
                if (instanceCtrl.scenario['activity'][id]['references']['ids'].length > 1) {
                    return true;
                } else {
                    return false;
                }
            };

            // begins an activity
            this.beginActivity = function (activityId) {
                var dataObject = "";
                $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/activity/" + activityId + "?state=begin", dataObject).
                    success(function (data) {
                        instanceCtrl.instanceDetails.activities = {};
                        //reloading content so the dashboard is uptodate
                        instanceCtrl.initializeActivityInstances();
                        instanceCtrl.initializeDataobjectInstances();
                        instanceCtrl.initializeActivitylogInstances();
                        instanceCtrl.initializeDataobjectlogInstances();
                        instanceCtrl.initializeDataobjectAttributelogInstances();
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            // terminates an activity
            this.terminateActivity = function (activityId) {
                var dataObject = "";
                $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/activity/" + activityId + "?state=terminate", dataObject).
                    success(function (data) {
                        instanceCtrl.instanceDetails.activities = {};
                        //reloading content so the dashboard is uptodate
                        instanceCtrl.initializeActivityInstances();
                        instanceCtrl.initializeDataobjectInstances();
                        instanceCtrl.initializeActivitylogInstances();
                        instanceCtrl.initializeDataobjectlogInstances();
                        instanceCtrl.initializeDataobjectAttributelogInstances();
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            this.terminateActivityWithOutputset = function (activityId, outputset) {
                var dataObject = "";
                $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface +
                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                "/activity/" + activityId + "?state=terminate&outputset=" + outputset, dataObject).
                    success(function (data) {
                        instanceCtrl.instanceDetails.activities = {};
                        //reloading content so the dashboard is uptodate
                        instanceCtrl.initializeActivityInstances();
                        instanceCtrl.initializeDataobjectInstances();
                        instanceCtrl.initializeActivitylogInstances();
                        instanceCtrl.initializeDataobjectlogInstances();
                        instanceCtrl.initializeDataobjectAttributelogInstances();
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };


            this.getTerminationConditionOfScenario = function (id) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/terminationcondition/").
                    success(function (data) {
                        instanceCtrl.scenario['terminationcondition'] = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            this.getActivityReferences = function (activityID) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/" + activityID + "/references").
                    success(function (data) {
                        instanceCtrl.scenario['activity'][activityID]['references'] = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            this.getActivityOutput = function (activityID) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/" + activityID + "/output").
                    success(function (data) {
                        instanceCtrl.scenario['activity'][activityID]['output'] = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            this.getOutputsets = function (outputsetID, activityID) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/outputset/" + outputsetID + "").
                    success(function (data) {
                        instanceCtrl.scenario['activity'][activityID]['outputsets'][outputsetID] = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            this.getOutputAndOutputsets = function (activityID) {
                //if outputsets is already defined, we dont touch them
                if (!instanceCtrl.scenario['outputsets']) {
                    instanceCtrl.scenario['outputsets'] = {};
                }
                // initializing outputsetsLength and outputsetsNameAndStateArray
                instanceCtrl.scenario['outputsetsLength'] = [];
                instanceCtrl.scenario['outputsetsNameAndStateArray'] = [];

                //retrieving the output for each retrieved referenced Activity
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/" + activityID + "/output").
                    success(function (data2) {
                        instanceCtrl.activityOutput[activityID] = {};
                        instanceCtrl.activityOutput[activityID] = data2;
                        //now, we also want to get the details of the outputset to access the label e.g. for each entry
                        angular.forEach(instanceCtrl.activityOutput[activityID], function (outputset, key2) {
                            $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/outputset/" + outputset['id'] + "").
                                success(function (data3) {
                                    instanceCtrl.scenario['outputsets'][outputset['id']] = {};
                                    instanceCtrl.scenario['outputsets'][outputset['id']] = data3;
                                    //we are storing some information duplicated in order to access them quicker and more easy afterwards
                                    instanceCtrl.scenario['outputsetsLength'].push(outputset['id']);
                                }).
                                error(function () {
                                    console.log('request failed');
                                });
                        });
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

            this.handleReferencedActivities = function (activityID) {
                //if outputsets is already defined, we dont touch them
                if (!instanceCtrl.scenario['outputsets']) {
                    instanceCtrl.scenario['outputsets'] = {};
                }
                //if "refLength" as length of the references array is already defined, we dont touch them
                if (!instanceCtrl.scenario['refLength']) {
                    instanceCtrl.scenario['refLength'] = 0;
                }
                //if activity is already defined, we dont touch them
                if (!instanceCtrl.scenario['activity']) {
                    instanceCtrl.scenario['activity'] = {};
                }
                instanceCtrl.scenario['activity'][activityID] = {};
                //retrieve referenced Activities for this activityID
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/" + activityID + "/references").
                    success(function (data) {
                        instanceCtrl.scenario['activity'][activityID]['references'] = data;
                        var activityArray = data['ids'];
                        instanceCtrl.scenario['refLength'] = data['ids'].length;
                        activityArray.push(activityID);
                        //check if there are any referenced Activities
                        if (instanceCtrl.scenario['activity'][activityID]['references']['ids'].length > 0) {
                            //if so, lets get the output for each of them
                            angular.forEach(activityArray, function (refActivityID, key) {
                                //retrieving the output for each retrieved referenced Activity
                                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/" + refActivityID + "/output").
                                    success(function (data2) {
                                        instanceCtrl.activityOutput[refActivityID] = {};
                                        instanceCtrl.activityOutput[refActivityID] = data2;
                                        //now, we also want to get the details of the outputset to access the label e.g. for each entry
                                        angular.forEach(instanceCtrl.activityOutput[refActivityID], function (outputset, key2) {
                                            $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/outputset/" + outputset['id'] + "").
                                                success(function (data3) {
                                                    instanceCtrl.scenario['outputsets'][outputset['id']] = {};
                                                    instanceCtrl.scenario['outputsets'][outputset['id']] = data3;
                                                }).
                                                error(function () {
                                                    console.log('request failed');
                                                });
                                        });
                                    }).
                                    error(function () {
                                        console.log('request failed');
                                    });
                            });
                        }
                    }).
                    error(function () {
                        console.log('request failed');
                    });
            };

        }
    ]);
})();
