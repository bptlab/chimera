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
			this.scenarioIds = [];
			this.scenarios = {};
			
			$http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/0/").
				success(function(data){
					controller.scenarioIds = data['ids'];
					controller.getDetailedInformation();
				});
				
			this.getDetailedInformation = function(){
				this.scenarioIds.forEach(function(id){
					$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/" + id + "/").
						success(function(data) {
							//controller.scenarios["" + id]['id'] = id;
							controller.scenarios["" + id] = data;
							controller.getImageForScenario(id);
							controller.getInstancesOfScenario(id);
						});
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
					var ret = this.scenarios["" + $routeParams.id];
					if (ret) {
						ret.id = $routeParams.id;
						return ret;
					}
				}
			};
			
			this.getInstancesOfScenario = function(id) {
				$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/"+ id + "/instance/0/").
					success(function(data) {
						controller.scenarios["" + id]['instanceIds'] = data['ids'];
				});
			};
			
			// Creates a new instance of the scenario with the given Id
			this.createInstance = function(id){
				$http.post(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/"+ id+"/").
					success(function(data) {
						$location.path("/scenario/" + id + "/instance/" + data);
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
						instanceCtrl.instances[$routeParams.instanceId].activities[state] = data;
					});
				});
			}

			this.initializeDataobjectInstances = function(){
				instanceCtrl.instances[$routeParams.instanceId].dataobjects = {};
					$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface +
						  "/scenario/" + $routeParams.id + "/instance/" + $routeParams.instanceId +
						"/dataobject/0/").
					success(function(data){
						instanceCtrl.instances[$routeParams.instanceId].dataobjects = data;
					});
			}

			this.initialize = function(){
				if ($routeParams.instanceId) {
					// initialize if necessary the specified instance
					// The scenario and instance is specified by the routeParams
					this.initializeInstance($routeParams.instanceId);
				} else {
					// First get the Scenario InstanceIds
					$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/"+ $routeParams.id + "/instance/0/").
					success(function(data) {
						instanceCtrl.scenario['instanceIds'] = data['ids'];
						// fetch the detail information of every scenario Instance
						// The scenario is specified by the routeParams
						instanceCtrl.scenario['instanceIds'].forEach(function(instanceId){
							$http.get(
								JEngine_Server_URL + "/" + JCore_REST_Interface +
								"/scenario/" + $routeParams.id + "/instance/" + instanceId + "/"
							).success(function(data){
								instanceCtrl.instances['' + instanceId] = data;
								instanceCtrl.initializeInstance(instanceId);
							});
						});
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
					instanceCtrl.scenario['instanceIds'] = instanceCtrl.scenario['instanceIds'] || [];
					instanceCtrl.scenario['instanceIds'].push(id);
					instanceCtrl.instances['' + id] = data;
					if ($routeParams.instanceId) {
						instanceCtrl.initializeActivityInstances();
						instanceCtrl.initializeDataobjectInstances();
					}
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
					});
			};			
		}
	]);
})();
