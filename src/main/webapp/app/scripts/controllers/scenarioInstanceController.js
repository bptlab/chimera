'use strict';

angular.module('jfrontend')
    .controller('ScenarioInstanceController', ['$routeParams', '$location', '$http', '$scope',
        function ($routeParams, $location, $http, $scope) {
            var instanceCtrl = this;

            $scope.$on('$viewContentLoaded', function () {
                console.log($routeParams.id, $routeParams.instanceId);
                instanceCtrl.initialize();
            });

            // For accessing data from inside the $http context


            // initialize an empty object for the instances
            this.instances = {};
            this.instanceDetails = {};
            this.scenario = {};
            this.activityOutputStates = {};
            this.changeAttributeObject = {};
            this.availableInput = {};
            this.selectedStates = {};
            this.activityInputAttributes = {};
            this.workingItems = {};
            this.selectedDataObjectIds = {};
            
            // activity instance termination
            this.availableOutput = {};
            this.newDataObjectCreation = {};
            this.dataObjectTransition = {};
            this.attributeValues = {};
            
            this.alerts = [];

            this.addAlert = function (alert, type) {
                this.alerts.push({msg: alert, type: type});
            };

            this.closeAlert = function (index) {
                this.alerts.splice(index, 1);
            };

            this.fragmentXmlStrings = [];

            //post update for webservice tasks
            this.submitAttributeForm = function () {
                //using the put
                var data = $scope.form;
                $http.put(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/webservice/" + webserviceC.workingID + "/?", data);
            };

            /* ____ BEGIN_INITIALIZATION ____ */
            this.initializeFragmentXmlStrings = function () {
                $http.get(JEngine_Server_URL + '/' + JCore_REST_Interface + '/scenario/'
                    + $routeParams.id + '/xml')
                    .success(function (data) {
                        instanceCtrl.fragmentXmlStrings = data;
                        
                        var index = 0;
                        instanceCtrl.fragmentXmlStrings.forEach(function (xml) {
                            var divId = 'renderXml' + index;
                            $('#xmlContainer').append('<div id="' + divId + '"></div>');
                            var divIdHash = '#' + divId;
                            var viewer = new BPMNViewer({container: divIdHash});
                            viewer.importXML(xml);
                        });

                    });
            };

            this.initializeActivityInstances = function () {
                instanceCtrl.instanceDetails.activityInstances = {};
                ["ready", "terminated", "running"].forEach(function (state) {
                    $http.get(
                        JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" +
                        $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/?state=" + state).success(function (data) {
                        instanceCtrl.instanceDetails.activityInstances[state] = data;
                    }).error(function () {
                        console.log('request failed');
                    });
                });
            };

            this.initializeDataobjectInstances = function () {
                instanceCtrl.instanceDetails.dataobjects = {};
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface +
                    "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                    "/dataobject/").success(function (data) {
                    instanceCtrl.instanceDetails.dataobjects = data;
                }).error(function () {
                    console.log('request failed');
                });
            };
            // activitylogs
            this.initializeActivitylogInstances = function () {
                instanceCtrl.instanceDetails.activitylogs = {};
                $http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
                    "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                    "/activities/").success(function (data) {
                    instanceCtrl.instanceDetails.activitylogs = data;
                }).error(function () {
                    console.log('request failed');
                });
            };
            // dataobjectlogs
            this.initializeDataobjectlogInstances = function () {
                instanceCtrl.instanceDetails.dataobjectlogs = {};
                $http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
                    "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                    "/dataobjects/").success(function (data) {
                    instanceCtrl.instanceDetails.dataobjectlogs = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

            // dataobjectattributeslogs
            this.initializeDataobjectAttributelogInstances = function () {
                instanceCtrl.instanceDetails.dataobjectAttributelogs = {};
                $http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
                    "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                    "/attributes/").success(function (data) {
                    instanceCtrl.instanceDetails.dataobjectAttributelogs = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

            // if necessary initialize the specified Scenario
            this.initialize = function () {

                if ($routeParams.instanceId) {
                    // initialize if necessary the specified instance
                    // The scenario and instance is specified by the routeParams
                    this.initializeInstance($routeParams.instanceId);
                } else {
                    // First get the Scenario InstanceIds
                    $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/").success(function (data) {
                        instanceCtrl.scenario['instances'] = data;
                        instanceCtrl.scenario['id'] = $routeParams.id;
                        angular.forEach(instanceCtrl.scenario['instances']['ids'], function (scenarioInstance, key) {
                            $http.post(JEngine_Server_URL + "/" + JAnalytics_REST_Interface
                                + "/services/de.uni_potsdam.hpi.bpt.bp2014.janalytics.ScenarioInstanceRuntime",
                                {"args": [scenarioInstance]}).success(function (data) {
                                if (!instanceCtrl.scenario['instances']['durations']) {
                                    instanceCtrl.scenario['instances']['durations'] = {};
                                }
                                instanceCtrl.scenario['instances']['durations'][data['scenarioId']] = data['ScenarioInstanceRuntime'];
                                console.log(data);
                            })
                        })
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
                        instanceCtrl.refreshPage();
                        instanceCtrl.getTerminationConditionOfScenario($routeParams.id);
                        instanceCtrl.initializeFragmentXmlStrings();
                    }
                }).error(function () {
                    console.log('request failed');
                });
            };

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
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + instanceID).success(function (data) {
                    //return data['name'];
                    instanceCtrl.instanceDetails['instance_name'] = data['name'];
                }).error(function () {
                    console.log('request failed');
                });
            };

            this.setAttribute = function (attrid, value, activityInstanceId) {
                if (!instanceCtrl.changeAttributeObject.hasOwnProperty(activityInstanceId)) {
                    instanceCtrl.changeAttributeObject[activityInstanceId] = {'idToValue': {}};
                }
                instanceCtrl.changeAttributeObject[activityInstanceId]['idToValue'][attrid] = value;
            };

            // begins an activity
            this.beginActivity = function (activityInstanceId) {
                var dataObject = {'dataobjects': []};
                for (var dclassname in instanceCtrl.selectedDataObjectIds) {
                    if (instanceCtrl.selectedDataObjectIds.hasOwnProperty(dclassname)) {
                        dataObject['dataobjects'].push(instanceCtrl.selectedDataObjectIds[dclassname])
                    }
                }
                $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface +
                    "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                    "/activityinstance/" + activityInstanceId + "/begin", dataObject).success(function () {
                    instanceCtrl.instanceDetails.activityInstances = {};
                    instanceCtrl.availableInput = {};
                    instanceCtrl.selectedDataObjectIds = {};
                    instanceCtrl.activityInputAttributes = {};
                    //reloading content so the dashboard is uptodate
                    instanceCtrl.refreshPage();
                }).error(function () {
                    console.log('request failed');
                });
            };

            this.terminateActivity = function (activityInstanceId) {
            	var values = {};
                values['ids'] = [];
                values['new_creation'] = [];
                values['transition'] = [];
                values['datanode_attribute_values'] = {};
                values['dataobject_attribute_values'] = {};
                values['transition_to_datanode'] = {};
                instanceCtrl.availableOutput[activityInstanceId].forEach(function (datanode) {
                	values['ids'].push(datanode.id);
                    if (instanceCtrl.newDataObjectCreation[datanode.id]) {
                        values['new_creation'].push(datanode.id);
                       	values['datanode_attribute_values'][datanode.id] = instanceCtrl.attributeValues[datanode.id];
                    } else {
                    	values['transition'].push(instanceCtrl.dataObjectTransition[datanode.id]);
                    	values['transition_to_datanode'][instanceCtrl.dataObjectTransition[datanode.id]] = datanode.id;
                    	values['dataobject_attribute_values'][instanceCtrl.dataObjectTransition[datanode.id]] = instanceCtrl.attributeValues[instanceCtrl.dataObjectTransition[datanode.id]];
                    }
                });
                console.log(JSON.stringify(values));
                /*
            	this.availableOutput[activityInstanceId].forEach(function (datanode) {
                    if (instanceCtrl.newDataObjectCreation[datanode.id]) {
                    	var valueMap = {};
                        valueMap['attribute_values'] = instanceCtrl.attributeValues[datanode.id];
                    	console.log('create ' + JSON.stringify(valueMap));
                    	$http.put(JEngine_Server_URL + "/" + JCore_REST_Interface +
                                "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                                "/activityinstance/" + activityInstanceId + "/datanode/" + datanode.id + "/new/", JSON.stringify(valueMap)).success(function (data) {
                            }).error(function () {
                                console.log('DataNode creation failed.');
                            });
                    } else {
                    	var valueMap = {}; 
                    	var fromId = instanceCtrl.dataObjectTransition[datanode.id];
                        valueMap['attribute_values'] = instanceCtrl.attributeValues[fromId];
                        valueMap['from_id'] = fromId;
                        valueMap['to_id'] = datanode.id;
                    	console.log('transition: ' + JSON.stringify(valueMap));
                    }
                });
                */
                // save attribute values
                /*
                if (instanceCtrl.changeAttributeObject.hasOwnProperty(activityInstanceId)) {
                    var valueMap = instanceCtrl.changeAttributeObject[activityInstanceId].idToValue;

                    $http.put(JEngine_Server_URL + "/" + JCore_REST_Interface +
                        "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                        "/activityinstance/" + activityInstanceId, JSON.stringify(valueMap)).success(function (data) {
                        console.log('Attributes changed.')
                    }).error(function () {
                        console.log('Saving attribute values failed.');
                    });

                    instanceCtrl.initializeDataobjectAttributelogInstances();
                }
				*/
                // terminate activity with selected states
                $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface +
                    "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                    "/activityinstance/" + activityInstanceId + "/terminate", JSON.stringify(values)).success(function (data) {
                    instanceCtrl.instanceDetails.activityInstances = {};
                    instanceCtrl.selectedStates = {};
                    //reloading content so the dashboard is uptodate
                    instanceCtrl.refreshPage();
                }).error(function () {
                    console.log('request failed');
                });
            };

            this.getTerminationConditionOfScenario = function (id) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/terminationcondition/").success(function (data) {
                    instanceCtrl.scenario['terminationcondition'] = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

            // check whether the scenario instance can terminate
            this.canTerminate = function () {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/canTerminate").success(function (data) {
                    instanceCtrl.instanceDetails.canTerminate = data['canTerminate'];
                }).error(function () {
                    instanceCtrl.instanceDetails.canTerminate = false;
                    console.log('request failed');
                });
            };

            // terminate the scenario instance
            this.terminateInstance = function () {
                $http.post(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/terminate").success(function () {
                    console.log('[DBG] request successful');
                }).error(function () {
                    console.log('request failed');
                });
            };

            this.getActivityReferences = function (activityID) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/activity/" + activityID + "/references").success(function (data) {
                    instanceCtrl.scenario['activity'][activityID]['references'] = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

            this.getActivityInput = function (activityInstanceId) {
                $http.get(JEngine_Server_URL + '/' + JCore_REST_Interface + '/scenario/' + $routeParams.id
                    + '/instance/' + $routeParams.instanceId + '/activityinstance/' + activityInstanceId + '/availableInput')
                    .success(function (data) {
                        instanceCtrl.availableInput[activityInstanceId] = {};
                        data.forEach(function (dataobject) {
                            if (!instanceCtrl.availableInput[activityInstanceId].hasOwnProperty(dataobject['label'])) {
                                instanceCtrl.availableInput[activityInstanceId][dataobject['label']] = [];
                            }
                            instanceCtrl.availableInput[activityInstanceId][dataobject['label']].push(dataobject);
                        });
                    })
                    .error(function () {
                        console.log('Loading activity inputs failed.')
                    });
            };

            this.getWorkingItems = function (activityInstanceId) {
            	instanceCtrl.attributeValues = {};
                $http.get(JEngine_Server_URL + '/' + JCore_REST_Interface + '/scenario/' + $routeParams.id
                	+ '/instance/' + $routeParams.instanceId + '/activityinstance/' + activityInstanceId + '/workingItems')
                    .success(function (data) {
                        instanceCtrl.workingItems[activityInstanceId] = data;
                    }).error(function () {
                    console.log('Loading activity working items failed.');
                })
            };
            
            this.setAttributeValues = function (activityInstanceId) {
            	$http.put(JEngine_Server_URL + "/" + JCore_REST_Interface +
            			"/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
                        "/activityinstance/" + activityInstanceId, JSON.stringify(instanceCtrl.attributeValues)).success(function (data) {
                    console.log('Attributes changed.')
                }).error(function () {
                	console.log('Saving attribute values failed.');
                });
            }
            
            this.getActivityOutput = function (activityInstanceId) {
            	this.availableOutput = {};
            	this.newDataObjectCreation = {};
                $http.get(JEngine_Server_URL + '/' + JCore_REST_Interface + '/scenario/' + $routeParams.id
                    + '/instance/' + $routeParams.instanceId + '/activityinstance/' + activityInstanceId + '/availableOutput')
                    .success(function (data) {
                        instanceCtrl.availableOutput[activityInstanceId] = data;
                    }).error(function () {
                    console.log('Loading activity output failed.');
                })
            };

            // from -> to
            this.setDataObjectTransition = function(incommingDataObjectId, dataNodeId) {
        		// to -> from
            	this.dataObjectTransition[dataNodeId] = incommingDataObjectId;
            };
            
            this.setAttributeTransition = function(dataObjectId, attributeId, value) {
            	if(!this.attributeValues.hasOwnProperty(dataObjectId)) {
            		this.attributeValues[dataObjectId] = {};
                }
            	this.attributeValues[dataObjectId][attributeId] = value;
            };
            
            this.setNewDataObjectCreation = function(id, value) {
                 this.newDataObjectCreation[id] = value;
            };
            
            this.getActivityOutputStates = function (activityInstanceId) {
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId + "/activityinstance/" + activityInstanceId + "/output").success(function (data) {
                    instanceCtrl.activityOutputStates[activityInstanceId] = data;
                }).error(function () {
                    console.log('Loading activity output states failed.');
                });
            };

            // TODO support referenced activities again
            /* this.handleReferencedActivities = function (activityID) {
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
             success(function(data){
             var sorteddata = {};
             data.forEach(function(data){
             if (!(data.label in sorteddata)) {
             sorteddata[data.label] = {
             'label': data.label,
             'id': data.id,
             'state': data.state,
             'instances': []
             }
             }
             sorteddata[data.label]['instances'].push(data)
             });
             instanceCtrl.activityOutputStates[activityID] = sorteddata;
             }).
             error(function () {
             console.log('request failed');
             });
             }; */

            this.refreshPage = function () {
                instanceCtrl.initializeActivityInstances();
                instanceCtrl.initializeDataobjectInstances();
                instanceCtrl.initializeActivitylogInstances();
                instanceCtrl.initializeDataobjectlogInstances();
                instanceCtrl.initializeDataobjectAttributelogInstances();
                instanceCtrl.canTerminate();
            };

            // Event Listener for receiving events from the backend and refreshing the page
            source.addEventListener('refresh', function (event) {
                instanceCtrl.refreshPage();
            });

            source.addEventListener('warning', function (event) {
                instanceCtrl.addAlert(event.msg, 'warning');
            });

            source.addEventListener('error', function (event) {
                instanceCtrl.addAlert(event.msg, 'danger');
            })

            this.selectDataObject = function (dclassname, dobjectid, attrconfiguration) {
                instanceCtrl.activityInputAttributes[dclassname] = attrconfiguration;
                instanceCtrl.selectedDataObjectIds[dclassname] = dobjectid;
            };

            this.selectState = function (dclass, dstate) {
                instanceCtrl.selectedStates[dclass] = dstate;
            }
        }
    ]);
