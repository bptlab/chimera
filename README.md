[![Travic CI Build Status](https://travis-ci.org/BP2014W1/JUserManagement.svg?branch=dev)](https://travis-ci.org/BP2014W1/JUserManagement)

# JUserManagement
The JUserManagement offers a user management support to the JEngine to enable a role feature within PCM

## Setup

Update APT 

    sudo apt-get update

Installing Tomcat, MySQL Server, Maven (>3,2), Java (>1,7), git

    sudo apt-get install  tomcat7 mysql-server maven git default-jdk 

Cloning of this repo

    git clone https://github.com/BP2014W1/JUserManagement

Further an MySQL Database should be created named as "JUserManagement" & import SQL file from 

    mysql -u username -p -h localhost JUserManagement < JEngine\src\main\resources\JUserManagement.sql

Please be aware of the database settings inside the web.xml . For the tests you may want to adapt also the database_connection in

    JEngine/blob/dev/src/main/resources/database_connection


## Deployment

In order to deploy the JUserManagement install [Maven](http://maven.apache.org/ Maven).
Then execute the following commands in your command line:

    mvn install -Dmaven.test.skip=true

A war file will be created which can be executed using tomcat. Stop tomcat

    service tomcat7 stop

copy the JEngine.war in the tomcat webapp dir

    cp target/JUserManagement.war /var/lib/tomcat7/webapps/

remove old JEngine folder in case of an update

    rm -r /var/lib/tomcat7/webapps/JUserManagement

and start tomcat again

    service tomcat7 start

Now, you may access the jFrontend via

   http://localhost:8080/JUserManagement


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
