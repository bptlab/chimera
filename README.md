[![Travic CI Build Status](https://travis-ci.org/BP2014W1/JEngine.svg?branch=dev)](https://travis-ci.org/BP2014W1/JEngine)
[![Coverage Status](https://coveralls.io/repos/BP2014W1/JEngine/badge.svg?branch=dev)](https://coveralls.io/r/BP2014W1/JEngine?branch=dev)


# JEngine

The JEngine is a ProcessEngine to execute Production Case Management (PCM).

## License

The source code of the JEngine is provided under [MIT License](license.md).
Please notice, that the used [Antlr framework](https://github.com/antlr/antlr4) is published under the [BSD License](https://github.com/antlr/antlr4/blob/master/LICENSE.txt).

## Architecture

![alt Architecture](https://raw.githubusercontent.com/BP2014W1/JEngine/dev/docu/general/img/fmc-architecture-v2_4.png)

## RESTful

See the [documentation](https://github.com/BP2014W1/JEngine/raw/dev/docu/rest/JEngine_REST_Specs.pdf) inside the doc folder.

## Setup

Update APT 

    sudo apt-get update

Installing Tomcat, MySQL Server, Maven (>3,2), Java (>1,7), git

    sudo apt-get install  tomcat7 mysql-server maven git default-jdk 

Cloning of this repo

    git clone https://github.com/BP2014W1/JEngine

Further an MySQL Database should be created named as "JEngineV2" & import SQL file from 

    mysql -u username -p -h localhost JEngineV2 < JEngine\src\main\resources\JEngineV2_schema.sql

Please be aware of the database settings inside the web.xml in

    JEngine/src/main/resources/webapp/WEB-INF/web.xml

For the tests you may want to adapt also the database_connection in

    JEngine/src/main/resources/database_connection

After changing server ips you may want to update the Config.java in the config package.

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

We are using a [AngularJS Template](https://wrapbootstrap.com/theme/homer-responsive-admin-theme-WB055J451); please be sure to download the [AngularJS libs](https://docs.angularjs.org/misc/downloading)
   
## UML

Class View

![alt Class view](https://raw.githubusercontent.com/BP2014W1/JEngine/dev/docu/general/img/class_view_organic.png)

Package View

![alt Package view](https://raw.githubusercontent.com/BP2014W1/JEngine/dev/docu/general/img/package_view_organic.png)

## Features

This JEngine does execute
* AND, XOR gateways
* user tasks
* email- , webservices tasks
* data objects
* termination condition for scenarios

## Addendum

For further details we kindly refer to the [project site](https://bpt.hpi.uni-potsdam.de/Internal/BP2014Docu)
