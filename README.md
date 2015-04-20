[![Travic CI Build Status](https://travis-ci.org/BP2014W1/JUserManagement.svg?branch=dev)](https://travis-ci.org/BP2014W1/JUserManagement)

# JUserManagement
The JUserManagement offers a user management support to the JEngine to enable a role feature within PCM

## REST
creating new resources (like users or roles)
```
PUT http://ip:port/JUserManagement/api/interface/v1/{user|role}/
```
retrieving informations about all resources (like users or roles)
```
GET http://ip:port/JUserManagement/api/interface/v1/{user|role}/
```
retrieving informations about a specific resource (like users or roles)
```
GET http://ip:port/JUserManagement/api/interface/v1/{user|role}/{id}
```
updating already existing resource (like a user or a role)
```
PUT http://ip:port/JUserManagement/api/interface/v1/{user|role}/{id}
```
deleting a resource (like a user or a role)
```
DELETE http://ip:port/JUserManagement/api/interface/v1/{user|role}/{id}
```
An example JSON structure for a user is
```json
{
  "name":"MaxMustermann",
  "description":"Max is a Manager",
  "role_id":1
}
```
and for a role the structure is analog
```json
{
  "name":"ServiceManager",
  "description":"Answering Calls and Questions",
  "admin_id":1
}
```

## Features
We currently support the
* creation
* modification
* deletion

of 

* users
* roles

:)
