(function () {
    var adminCon = angular.module('adminConfiguration', []);

    //introducing an custom filter for checking unique entries within an array
    //
    adminCon.filter('unique', function () {
        return function (collection, keyname) {
            var output = [],
                keys = [];

            angular.forEach(collection, function (item) {
                var key = item[keyname];
                if (keys.indexOf(key) === -1) {
                    keys.push(key);
                    output.push(item);
                }
            });
            return output;
        };
    });


    // Create a Controller for jcomparser admin dashboard
    adminCon.controller('jcomparserMainView', ['$routeParams', '$location', '$http', '$scope',
            function ($routeParams, $location, $http) {
                var controller = this;

                // initialize an empty list of scenario Ids
                this.scenarioIds = [];
                this.scenarios = {};
                this.scenarioDetails = {};

                //requesting initially all available scenarios
                $http.get(JEngine_Server_URL + "/" + JComparser_REST_Interface + "/scenarios").
                    success(function (data) {
                        controller.scenarioDetails = data['ids'];
                    }).
                    error(function () {
                        console.log('request failed');
                    });

                //fetching the images from the jcomparser for better UIX within scenario import
                this.getImageForScenario = function (id) {
                    return JEngine_Server_URL + "/" + JComparser_REST_Interface +
                        "/scenarios/" + id + "/image/";
                };

                // Creates a new instance of the scenario with the given Id
                this.loadInstance = function (id) {
                    $http.post(JEngine_Server_URL + "/" + JComparser_REST_Interface + "/launch/" + id).
                        success(function (data) {
                            if (data) {
                                return data;
                            }
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

		this.isEmpty = function (obj) {

		    // null and undefined are "empty"
		    if (obj == null) return true;

		    // Assume if it has a length property with a non-zero value
		    // that that property is correct.
		    if (obj.length > 0)    return false;
		    if (obj.length === 0)  return true;

		    // Otherwise, does it have any properties of its own?
		    // Note that this doesn't handle
		    // toString and valueOf enumeration bugs in IE < 9
		    for (var key in obj) {
			if (hasOwnProperty.call(obj, key)) return false;
		    }

		    return true;
		}
            }]
    );

    // Create a Controller for mail config
    adminCon.controller('mailConfig', ['$routeParams', '$location', '$http', '$scope',
            function ($routeParams, $location, $http, $scope) {
                var controller = this;

                // initialize an empty list of scenario Ids, details for each Id and email tasks id array
                this.Details = [];
                this.emailtaskIDs = [];
                this.scenarioIDs = [];
                this.detailsForID = [];

                //requesting initially all available scenarios
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").
                    success(function (data) {
                        controller.scenarioIDs = data['labels'];

                    }).
                    error(function () {
                        console.log('request failed');
                    });

                //post update for email tasks
                this.submitMyForm = function () {
                    //using the data set in the form as request content
                    var data = $scope.form;
                    $http.put(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/emailtask/" + controller.workingID + "/?", data).
                        success(function (data) {
                                controller.loadData();
                            }).
                            error(function () {
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
                    $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/scenario/" + id + "/emailtask/").
                        success(function (data) {
                            controller.emailtaskIDs = data['ids'];
                            //if the emailtaskIDs array is not empty, prefetch the first item details
                            if(controller.emailtaskIDs.length > 0){
                               controller.getDetails(controller.emailtaskIDs[0], id);
                            }
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };
                // Got to the instance with the given Id
                this.getDetailsForMailtaskID = function (id) {
                    $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface +
                    "/scenario/1/emailtask/" + id + "/?").
                        success(function (data) {
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

    // Create a Controller for mail config
    adminCon.controller('webserviceConfig', ['$routeParams', '$location', '$http', '$scope',
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
                $http.get(JEngine_Server_URL + "/" + JCore_REST_Interface + "/scenario/").
                    success(function (data) {
                        webserviceC.scenarioIDs = data['labels'];

                    }).
                    error(function () {
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
                    $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface + "/scenario/" + id + "/webservice/").
                        success(function (data) {
                            webserviceC.webserviceIDs = data['ids'];
                            //if the emailtaskIDs array is not empty, prefetch the first item details
                            if(webserviceC.webserviceIDs.length > 0){
                               webserviceC.getDetails(webserviceC.webserviceIDs[0], id);
                            }
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

                // Got to the instance with the given Id
                this.getDetailsForWebserviceID = function (webserviceID, scenarioID) {
                    $http.get(JEngine_Server_URL + "/" + JConfig_REST_Interface +
                    "/scenario/" + scenarioID + "/webservice/" + webserviceID + "/?").
                        success(function (data) {
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

    // Create a Controller for mail config
    adminCon.controller('jcoreConfig', ['$routeParams', '$location', '$http', '$scope',
            function ($routeParams, $location, $http, $scope) {
                var jcoreC = this;

                // Got to the instance with the given Id
                this.deleteScenario = function (id) {
                    $http.delete(JEngine_Server_URL + "/" + JConfig_REST_Interface +
                    "/scenario/" + id + "/?").
                        success(function (data) {
                            console.info("deleting scenario was successful..");
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };


            }]
    );

    // Create a Controller for user management config
    adminCon.controller('userMgmtController', ['$routeParams', '$location', '$http', '$scope',
            function ($routeParams, $location, $http, $scope) {
                var userMgmtC = this;

                this.workingID = "";
                this.type = "";
                this.Details = {};

                //requesting initially all users from the JUserManagement
                $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/user").
                    success(function (data) {
                        userMgmtC.Details['user'] = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });

                //requesting initially all roles from the JUserManagement
                $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/role").
                    success(function (data) {
                        userMgmtC.Details['role'] = data;
                    }).
                    error(function () {
                        console.log('request failed');
                    });

                //setting the working ID for faster acecss
                this.setWorkingID = function (id) {
                    userMgmtC.workingID = id;
                };

                //if the user unfocus a content we also have to unset the working it
                this.unsetWorkingID = function () {
                    userMgmtC.workingID = "";
                };

                //for REST calls, we are setting the type we want to work with; in this case role
                this.setTypeRole = function () {
                    userMgmtC.type = "role";
                };

                //for REST calls, we are setting the type we want to work with; in this case user
                this.setTypeUser = function () {
                    userMgmtC.type = "user";
                };

                //retrieving all details for a role giving by its id and providing it to the user via form values
                this.getDetailsForRoleId = function (id) {
                    $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/role/" + id + "/?").
                        success(function (data) {
                            var value = {};
                            value = data[0];
                            //transmit specific value content to related form fields, so the user can edit them directly
                            $scope.form = {
                                name: value['rolename'],
                                description: value['description'],
                                admin_id: value['admin_id'],
                                id: value['id']
                            };
                        });
                    //always keep in mind to set the working ID
                    userMgmtC.setWorkingID(id);
                };

                //retrieving all details for a role giving by its id and providing it to the role via form values
                this.getDetailsForUserId = function (id) {
                    $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/user/" + id + "/?").
                        success(function (data) {
                            var value = {};
                            value = data[0];
                            //transmit specific value content to related form fields, so the user can edit them directly
                            $scope.form = {
                                name: value['username'],
                                description: value['description'],
                                admin_id: value['admin_id'],
                                role_id: value['role_id'],
                                id: value['id']
                            };
                        });
                    userMgmtC.setWorkingID(id);
                };

                //post update for user or role data
                this.submitMyForm = function () {
                    //using the data set in the form as request content
                    var data = $scope.form;
                    $http.put(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/" + userMgmtC.type + "/" + userMgmtC.workingID, data).
                        success(function (data) {
                            //if REST call was successfull, update the content
                            userMgmtC.refreshContent();
                        });
                }

                // Got to the instance with the given Id
                this.deleteUser = function (id) {
                    $http.delete(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/user/" + id + "/?").
                        success(function (data) {
                            //if REST call was successfull, update the content
                            userMgmtC.refreshContent();
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                    // we want to redirect the user to the overview after deleting a user
                    $location.path("/admin/userMgmt/");
                };

                // Got to the instance with the given Id
                this.deleteRole = function (id) {
                    $http.delete(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/role/" + id + "/?").
                        success(function (data) {
                            //if REST call was successfull, update the content
                            userMgmtC.refreshContent();
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

                this.goToOverview = function () {
                    $location.path("/admin/userMgmt/");
                };

                //if we executed a REST call successful, we want to update the content accordingly
                this.refreshContent = function () {
                    $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/user").
                        success(function (data) {
                            userMgmtC.Details['user'] = data;
                        }).
                        error(function () {
                            console.log('request failed');
                        });

                    $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/role").
                        success(function (data) {
                            userMgmtC.Details['role'] = data;
                        }).
                        error(function () {
                            console.log('request failed');
                        });
                };

            }]
    );
})();
