[![Travic CI Build Status](https://travis-ci.org/BP2014W1/JEngine.svg?branch=dev)](https://travis-ci.org/BP2014W1/JEngine)
[![Coverage Status](https://coveralls.io/repos/BP2014W1/JEngine/badge.svg?branch=dev)](https://coveralls.io/r/BP2014W1/JEngine?branch=dev)
<a href="https://scan.coverity.com/projects/4326">
  <img alt="Coverity Scan Build Status"
       src="https://scan.coverity.com/projects/4326/badge.svg"/>
</a>

# JEngine

The JEngine is a ProcessEngine to execute Production Case Management (PCM).

## RESTful

See [the documentation](https://github.com/BP2014W1/JEngine/tree/dev/docu/REST) inside the doc folder.

## Deployment

In order to deploy the JEngine install [Maven](http://maven.apache.org/ Maven).
Then execute the following commands in your command line:

    mvn install -Dmaven.test.skip=true


A war file will be created which can be executed using tomcat.

## Database Setup

You can use the src/main/resources/JEngineV2.sql file for your Database setup.
Please set your database settings inside the web.xml.

## Features

This JEngine does execute
* sequential
* usertasks
* data objects
* AND gateways
* termination condition for scenarios

Additionally, we provide an AngularJS frontend to control the JComparser and the JEngine..
