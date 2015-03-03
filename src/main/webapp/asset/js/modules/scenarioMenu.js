(function(){
	var scenario = angular.module('Scenario', []);
	
	// Create a directive for Scenarios Menu Entry
	scenario.directive('scenarioMenuEntry', function(){
		return {
			restrict: 'A',
			templateUrl: 'asset/templates/scenarioMenuEntry.html',
		};
	});
	
	// Create a Controller for the Scenario Information
	scenario.controller('ScenarioController', ['$routeParams', '$location', '$http',
		function($routeParams, $location, $http){
			var controller = this;
			
			// initialize an empty list of scenario Ids
			this.scenarioIds = [];
			
			// initialize an empty list of scenarios
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
							controller.scenarios["" + id] = data;
							controller.getImageForScenario(id);
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
			}
		}]
	);
})();