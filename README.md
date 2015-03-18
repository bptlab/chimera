[![Travic CI Build Status](https://travis-ci.org/BP2014W1/JEngine.svg?branch=dev)](https://travis-ci.org/BP2014W1/JEngine)
[![Coverage Status](https://coveralls.io/repos/BP2014W1/JEngine/badge.svg?branch=dev)](https://coveralls.io/r/BP2014W1/JEngine?branch=dev)
<a href="https://scan.coverity.com/projects/4326">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/4326/badge.svg"/>
</a>

# JEngine

The JEngine is a ProcessEngine to execute Production Case Management (PCM).

## Architecture

![alt Architecture](https://github.com/BP2014W1/JEngine/blob/dev/docu/img/fmc-architecture-v2_2.png)

## RESTful

See [the documentation](https://github.com/BP2014W1/JEngine/tree/dev/docu/REST) inside the doc folder.

## Setup

Update APT 

    sudo apt-get update

Installing Tomcat, MySQL Server, Maven (>3,2), Java (>1,7), git

    sudo apt-get install  tomcat7 mysql-server maven git default-jdk 

Cloning of this repo

    git clone https://github.com/BP2014W1/JEngine

Further an MySQL Database should be created named as "JEngineV2" & import SQL file from 

    mysql -u username -p -h localhost JEngineV2 < JEngine\src\main\resources\JEngineV2.sql

Please be aware of the database settings inside the web.xml . For the tests you may want to adapt also the database_connection in

    JEngine/blob/dev/src/main/resources/database_connection


## Deployment

In order to deploy the JEngine install [Maven](http://maven.apache.org/ Maven).
Then execute the following commands in your command line:

    mvn install -Dmaven.test.skip=true

A war file will be created which can be executed using tomcat. Stop tomcat

    service tomcat7 stop

copy the JEngine.war in the tomcat webapp dir

    cp target/JEngine.war /var/lib/tomcat7/webapps/

remove old JEngine folder in case of an update

    rm -r /var/lib/tomcat7/webapps/JEngine

and start tomcat again

    service tomcat7 start

Now, you may access the jFrontend via

   http://localhost:8080/JEngine


## Features

This JEngine does execute
* sequential
* usertasks
* data objects
* AND gateways
* XOR gateways
* email services tasks
* termination condition for scenarios

Additionally, we use an AngularJS frontend to control the JComparser and the JEngine..
