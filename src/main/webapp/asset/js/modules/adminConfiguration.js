(function(){
	var adminCon = angular.module('adminConfiguration', []);
	
	// Create a Controller for jcomparser admin dashboard
	adminCon.controller('jcomparserMainView', ['$routeParams', '$location', '$http', '$scope',
		function($routeParams, $location, $http){
			var controller = this;
			
			// initialize an empty list of scenario Ids
			this.scenarioIds = [];
			this.scenarios = {};
			this.scenarioDetails = {};
			
			$http.get(JEngine_Server_URL+"/"+JComparser_REST_Interface+"/scenarios").
				success(function(data){
				    controller.scenarioDetails = data['ids'];
				    
        		});

			this.getImageForScenario = function(id){
				return	JEngine_Server_URL + "/" + JComparser_REST_Interface + 
					"/scenarios/" + id + "/image/";
			};
					
			// Creates a new instance of the scenario with the given Id
			this.loadInstance = function(id){
				$http.post(JEngine_Server_URL+"/"+JComparser_REST_Interface+"/launch/"+ id).
					success(function(data) {
						if (data) {
							return data;
						}
					});
			};
		}]
	);

	// Create a Controller for mail config
	adminCon.controller('mailConfig', ['$routeParams', '$location', '$http', '$scope',
		function($routeParams, $location, $http, $scope){
			var controller = this;
			
			// initialize an empty list of scenario Ids
			this.Details = [];
			this.emailtaskIDs = [];
			this.scenarioIDs = [];
			this.detailsForID = [];

			$http.get(JEngine_Server_URL+"/"+JCore_REST_Interface+"/scenario/0/").
				success(function(data){
				    controller.scenarioIDs = data['ids'];
				    
        		});

			//post update for email tasks
			this.submitMyForm=function(){
				var data=$scope.form;  
				$http.post(JEngine_Server_URL + "/" + JCore_REST_Interface + "/config/emailtask/"+ controller.workingID + "/?", data);        
		   	 }

			//get all infos for popup
			this.getDetails = function(id){
				controller.getDetailsForMailtaskID(id);
				controller.workingID = id;
			};

			// Got all emailtasks with the given Id
			this.getAllMailtaskForScenarioID = function(id){
				$http.get(JEngine_Server_URL+"/" + JCore_REST_Interface + "/scenario/" + id + "/emailtask/0/").
					success(function(data) {
						controller.emailtaskIDs = data['ids'];
					});
			};
			// Got to the instance with the given Id
			this.getDetailsForMailtaskID = function(id){
				$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface +
					"/scenario/0/emailtask/" + id + "/?").
					success(function(data) {
						controller.detailsForID = data;
					});
			};
			
			this.loadData = function(){
				controller.getDetailsForMailtaskID(controller.workingID);
			};
		}]
	);
})();
