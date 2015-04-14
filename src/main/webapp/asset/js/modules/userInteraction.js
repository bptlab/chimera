(function(){
	var userIn = angular.module('userInteraction', []);
	
	// TODO: At a future state we maybe should use a service to share data between the controllers
	// Create an Object which holds the scenario Data globally
	/*scenario.factory('globalStorage', function(){
		var globalStorage = {
			scenarios: {}
		};
		return globalStorage;
	});*/
	
	// Create a directive for Scenarios Menu Entry
	userIn.directive('scenarioMenuEntry', function(){
		return {
			restrict: 'A',
			templateUrl: 'asset/templates/scenarioMenuEntry.html',
		};
	});

	// Create a Controller for the Scenario Information
	userIn.controller('ScenarioController', ['$routeParams', '$location', '$http', '$scope',
		function($routeParams, $location, $http){
			var controller = this;
			
			// initialize an empty list of scenario Ids
			this.scenarioDetails = {};
            this.currentScenario = {};
			this.scenarios = {};
			
			$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").
				success(function(data){
					controller.scenarios = data;
					//controller.getDetailedInformation();
				});
				
			this.getDetailedInformation = function(id){
					$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/").
						success(function(data) {
							controller.currentScenario['details'] = data;
							//controller.getImageForScenario(id);
							controller.getInstancesOfScenario(id);
						}).
                        error(function(data) {
                            console.log('request failed');
                        });
			};
			
			this.getImageForScenario = function(id){
				this.scenarios["" + id]['imageUrl'] =
					JEngine_Server_URL + "/" + JComparser_REST_Interface + 
					"/scenarios/" + id + "/image/";
			};
			
			this.goToDetailsFrom = function(id){
				$location.path('scenario/' + id);
			};
			
			this.getCurrentScenario = function(){
				if ($routeParams.id != null) {
                    controller.getDetailedInformation($routeParams.id);
                    controller.getInstancesOfScenario($routeParams.id);

                    controller.currentScenario['id'] = $routeParams.id;
				}
			};
			
			this.getInstancesOfScenario = function(id) {
				$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/"+ id + "/instance/").
					success(function(data) {
						controller.currentScenario['instances'] = data;
				}).
                    error(function(data) {
                         console.log('request failed');
                });
			};
			
			// Creates a new instance of the scenario with the given Id
			this.createInstance = function(id){
				$http.post(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ id+"/").
					success(function(data) {
						$location.path("/scenario/" + id + "/instance/" + data);
					}).
                    error(function(data) {
                        console.log('request failed');
                    });
			};
		}]
	);
	
	userIn.controller('ScenarioInstanceController', ['$routeParams', '$location', '$http', '$scope',
		function($routeParams, $location, $http){
			// For accessing data from inside the $http context
			var instanceCtrl = this;
			
			// initialize an empty object for the instances
			this.instances = {};
            this.instanceDetails = {};
			this.scenario = {};
			
			/* ____ BEGIN_INITIALIZATION ____ */
			this.initializeActivityInstances = function(){
				instanceCtrl.instances[$routeParams.instanceId].activities = {};
				["enabled", "terminated", "running"].forEach(function(state){
					var state2 = state;
					$http.get(
						JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" +
						$routeParams.id + "/instance/" + $routeParams.instanceId + "/activityinstance/0?status=" + state).
					success(function(data){
						instanceCtrl.instanceDetails.activities[state] = data;
					}).
                        error(function(data) {
                            console.log('request failed');
                        });
				});
			}

			this.initializeDataobjectInstances = function(){
				instanceCtrl.instances[$routeParams.instanceId].dataobjects = {};
					$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface +
						  "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
						"/dataobject/0/").
					success(function(data){
						instanceCtrl.instanceDetails.dataobjects = data;
					}).
                        error(function(data) {
                            console.log('request failed');
                        });
			}
			// activitylogs
			this.initializeActivitylogInstances = function(){
				instanceCtrl.instances[$routeParams.instanceId].dataobjects = {};
					$http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
						"/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
						"/activities/").
					success(function(data){
						instanceCtrl.instanceDetails.activitylogs = data;
					}).
                        error(function(data) {
                            console.log('request failed');
                        });
			}
			// dataobjectlogs
			this.initializeDataobjectlogInstances = function(){
				instanceCtrl.instances[$routeParams.instanceId].dataobjects = {};
					$http.get(JEngine_Server_URL + "/" + JHistory_REST_Interface +
						"/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
						"/dataobjects/").
					success(function(data){
						instanceCtrl.instanceDetails.dataobjectlogs = data;
					}).
                        error(function(data) {
                            console.log('request failed');
                        });
			}

			this.initialize = function(){
				if ($routeParams.instanceId) {
					// initialize if necessary the specified instance
					// The scenario and instance is specified by the routeParams
					this.initializeInstance($routeParams.instanceId);
				} else {
					// First get the Scenario InstanceIds
					$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/"+ $routeParams.id + "/instance/").
					success(function(data) {
						instanceCtrl.scenario['instances'] = data;
                        instanceCtrl.scenario['id'] = $routeParams.id;
					}).error(function(data) {
                            console.log('request failed');
                        });
				}
			};
			
			// Initializes an Instance object for the given ID by fetching the information
			this.initializeInstance = function(id){
				$http.get(
					JEngine_Server_URL + "/" + JCore_REST_Interface +
					"/scenario/" + $routeParams.id + "/instance/" + 
					id + "/"
				).success(function(data){
					//instanceCtrl.scenario['instanceIds'] = instanceCtrl.scenario['instanceIds'] || [];
					//instanceCtrl.scenario['instanceIds'].push(id);
					//instanceCtrl.instances['' + id] = data;
                        instanceCtrl.instanceDetails['id'] = id;
					if ($routeParams.instanceId) {
						instanceCtrl.initializeActivityInstances();
						instanceCtrl.initializeDataobjectInstances();
						instanceCtrl.initializeActivitylogInstances();
						instanceCtrl.initializeDataobjectlogInstances();
					}
				}).
                    error(function(data) {
                        console.log('request failed');
                    });
			}
			
			// if necessary initialize the specified Scenario
			this.initialize();
			/* ____ END_INITIALIZATION ____ */
			
			// returns the label of the current scenario
			this.getScenarioName = function(){
				// The scenarioId is specified by the routeParams
				var instanceArray = instanceCtrl.scenario['instanceIds'];
				// There is a lot of concurrency, check if all values are set
				if (instanceArray && instanceArray[0] && this.instances[instanceArray[0]]) {
					return this.instances[instanceArray[0]]['label'];
				}
			};
			
			// Got to the instance with the given Id
			this.goToDetailsFrom = function(id){
				$location.path("/scenario/" + $routeParams.id + "/instance/" + id);
			};
			
			// returns the current instance object
			this.getCurrentInstance = function(){
				var ret = this.instances[$routeParams.instanceId];
				if (ret) {
					return ret;
				}
			};
			
			// begins an activity
			this.beginActivity = function(activityId) {
				$http.post(JEngine_Server_URL + "/" + JCore_REST_Interface +
					"/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
					"/activityinstance/"+ activityId + "?status=begin").
					success(function(data) {
						instanceCtrl.instances.activities = {};
						instanceCtrl.initializeActivityInstances();
						instanceCtrl.initializeDataobjectInstances();
						instanceCtrl.initializeActivitylogInstances();
						instanceCtrl.initializeDataobjectlogInstances();
					}).
                    error(function(data) {
                        console.log('request failed');
                    });
			};
			
			// terminates an activity
			this.terminateActivity = function(activityId) {
				$http.post(JEngine_Server_URL + "/" + JCore_REST_Interface +
					"/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
					"/activityinstance/"+ activityId + "?status=terminate").
					success(function(data) {
						instanceCtrl.instances.activities = {};
						instanceCtrl.initializeActivityInstances();
						instanceCtrl.initializeDataobjectInstances();
						instanceCtrl.initializeActivitylogInstances();
						instanceCtrl.initializeDataobjectlogInstances();
					}).
                    error(function(data) {
                        console.log('request failed');
                    });
			};				
		}
	]);
})();
