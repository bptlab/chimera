'use strict';

angular.module('jfrontend')
    .controller('userMgmtController', ['$routeParams', '$location', '$http', '$scope',
        function ($routeParams, $location, $http, $scope) {
            var userMgmtC = this;

            this.workingID = "";
            this.type = "";
            this.Details = {};

            //requesting initially all users from the JUserManagement
            $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/user").success(function (data) {
                userMgmtC.Details['user'] = data;
            }).error(function () {
                console.log('request failed');
            });

            //requesting initially all roles from the JUserManagement
            $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/role").success(function (data) {
                userMgmtC.Details['role'] = data;
            }).error(function () {
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
                    "/role/" + id + "/?").success(function (data) {
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
                    "/user/" + id + "/?").success(function (data) {
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
                $http.put(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/" + userMgmtC.type + "/" + userMgmtC.workingID, data).success(function (data) {
                    //if REST call was successfull, update the content
                    userMgmtC.refreshContent();
                });
            }

            // Got to the instance with the given Id
            this.deleteUser = function (id) {
                $http.delete(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/user/" + id + "/?").success(function (data) {
                    //if REST call was successfull, update the content
                    userMgmtC.refreshContent();
                }).error(function () {
                    console.log('request failed');
                });
                // we want to redirect the user to the overview after deleting a user
                $location.path("/admin/userMgmt/");
            };

            // Got to the instance with the given Id
            this.deleteRole = function (id) {
                $http.delete(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface +
                    "/role/" + id + "/?").success(function (data) {
                    //if REST call was successfull, update the content
                    userMgmtC.refreshContent();
                }).error(function () {
                    console.log('request failed');
                });
            };

            this.goToOverview = function () {
                $location.path("/admin/userMgmt/");
            };

            //if we executed a REST call successful, we want to update the content accordingly
            this.refreshContent = function () {
                $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/user").success(function (data) {
                    userMgmtC.Details['user'] = data;
                }).error(function () {
                    console.log('request failed');
                });

                $http.get(JUserManagement_Server_URL + "/" + JUserManagement_REST_Interface + "/role").success(function (data) {
                    userMgmtC.Details['role'] = data;
                }).error(function () {
                    console.log('request failed');
                });
            };

        }]
    );