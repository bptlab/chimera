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
			
			$http.get(JEngine_Server_URL + "/" + JComparser_REST_Interface + "/scenarios").
				success(function(data){
				    controller.scenarioDetails = data['ids'];
                }).
                error(function() {
                    console.log('request failed');
                });

			this.getImageForScenario = function(id){
				return	JEngine_Server_URL + "/" + JComparser_REST_Interface + 
					"/scenarios/" + id + "/image/";
			};
					
			// Creates a new instance of the scenario with the given Id
			this.loadInstance = function(id){
				$http.post(JEngine_Server_URL + "/" + JComparser_REST_Interface + "/launch/" + id).
					success(function(data) {
						if (data) {
							return data;
						}
                    }).
                    error(function() {
                        console.log('request failed');
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

			$http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").
				success(function(data){
				    controller.scenarioIDs = data['ids'];

                }).
                error(function() {
                    console.log('request failed');
                });

			//post update for email tasks
			this.submitMyForm=function(){
				var data=$scope.form;  
				$http.post(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/emailtask/"+ controller.workingID + "/?", data);
		   	 }

			//get all infos for popup
			this.getDetails = function(id){
				controller.getDetailsForMailtaskID(id);
				controller.workingID = id;
			};

			// Got all emailtasks with the given Id
			this.getAllMailtaskForScenarioID = function(id){
				$http.get(JEngine_Server_URL+"/" + JConfig_REST_Interface + "/scenario/" + id + "/emailtask/").
					success(function(data) {
						controller.emailtaskIDs = data['ids'];
                    }).
                    error(function() {
                        console.log('request failed');
                    });
			};
			// Got to the instance with the given Id
			this.getDetailsForMailtaskID = function(id){
				$http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface +
					"/scenario/1/emailtask/" + id + "/?").
					success(function(data) {
						controller.detailsForID = data;
						$scope.form = { 
								receiver: data['receiver'],
								subject: data['subject'],
								content: data['message']
								};
					});
			};
			
			this.loadData = function(){
				controller.getDetailsForMailtaskID(controller.workingID);
			};
		}]
	);

    // Create a Controller for mail config
    adminCon.controller('jcoreConfig', ['$routeParams', '$location', '$http', '$scope',
            function($routeParams, $location, $http, $scope){
                var jcoreC = this;

                // Got to the instance with the given Id
                this.deleteScenario = function(id){
                    $http.delete(JEngine_Server_URL + "/" + JConfig_REST_Interface +
                    "/scenario/" + id + "/?").
                        success(function(data) {
                            console.log("deleting scenario was successful..");
                        }).
                        error(function() {
                            console.log('request failed');
                        });
                };


            }]
    );

    // Create a Controller for user management config
    adminCon.controller('userMgmtController', ['$routeParams', '$location', '$http', '$scope',
            function($routeParams, $location, $http, $scope){
                var userMgmtC = this;

                this.workingID = "";
                this.type = "";
                this.Details = {};

                $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/user").
                        success(function(data) {
                        userMgmtC.Details['user'] = data;
                    }).
                    error(function() {
                        console.log('request failed');
                    });

                $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/role").
                    success(function(data) {
                        userMgmtC.Details['role'] = data;
                    }).
                    error(function() {
                        console.log('request failed');
                    });

                this.setWorkingID = function(id){
                    userMgmtC.workingID = id;
                };

                this.setTypeRole = function(){
                    userMgmtC.type = "role";
                };

                this.setTypeUser = function(){
                    userMgmtC.type = "user";
                };

                this.getDetailsForRoleId = function(id){
                    $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/role/" + id + "/?").
                        success(function(data) {
                            var value = {};
                            value = data['myArrayList'][0]['map'];
                            $scope.form = {
                                name: value['rolename'],
                                description: value['description'],
                                admin_id: value['admin_id'],
                                id: value['id']
                            };
                        });
                    userMgmtC.setWorkingID(id);
                };

                this.getDetailsForUserId = function(id){
                    $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/user/" + id + "/?").
                        success(function(data) {
                            var value = {};
                            value = data['myArrayList'][0]['map'];
                            $scope.form = {
                                name: value['username'],
                                description: value['description'],
                                admin_id: value['admin_id'],
                                id: value['id']
                            };
                        });
                    userMgmtC.setWorkingID(id);
                };

                //post update for user or role data
                this.submitMyForm=function(){
                    var data=$scope.form;
                    $http.put(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/" + userMgmtC.type + "/" + userMgmtC.workingID, data);
                    $location.path("/admin/userMgmt/");
                }

                // Got to the instance with the given Id
                this.deleteUser = function(id){
                    $http.delete(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/user/" + id + "/?").
                        success(function(data) {
                            console.log("deleting user was successful..");
                        }).
                        error(function() {
                            console.log('request failed');
                        });
                    $location.path("/admin/userMgmt/");
                };

                // Got to the instance with the given Id
                this.deleteRole = function(id){
                    $http.delete(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/role/" + id + "/?").
                        success(function(data) {
                            console.log("deleting role was successful..");
                        }).
                        error(function() {
                            console.log('request failed');
                        });
                    $location.path("/admin/userMgmt/");
                };

            }]
    );
})();
