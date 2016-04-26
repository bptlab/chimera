[![Travic CI Build Status](https://travis-ci.org/BP2014W1/JEngine.svg?branch=dev)](https://travis-ci.org/BP2014W1/JEngine)
[![Coverage Status](https://coveralls.io/repos/BP2014W1/JEngine/badge.svg?branch=dev)](https://coveralls.io/r/BP2014W1/JEngine?branch=dev)


*Chimera* is an engine for executing case models developed at the Business Process Technology (BPT) group at the Hasso Plattner Institute (HPI). It is provided under the MIT free and open source software license (see LICENSE.md).

## Getting started

*Prerequisites*: To run an instance of Chimera on your machine, you need to have MySQL, Apache Tomcat7, Maven 3, git, and Java (> 7) installed.

1. clone the repository `git clone https://github.com/bptlab/chimera`
2. initialize the git submodules `git submodule init` and `git submodule update`
2. create a MySQL database `mysql -u USER_NAME -p PASSWORD -e "create schema SCHEMA_NAME"`
3. run maven `mvn install -Ddb.user=USER_NAME -Ddb.password=PASSWORD -Ddb.schema=SCHEMA_NAME`
  * alternativly run `mvn validate` and edit the file config.properties
4. deploy the created war file to your tomcat by copying it to TOMCAT_DIR/webapps
  * alternativly use `mvn tomcat7:deploy` (however, you need to have configured your tomcat credentials, see [this article](http://www.mkyong.com/maven/how-to-deploy-maven-based-war-file-to-tomcat/) ).

## Architecture

![alt Architecture](https://raw.githubusercontent.com/BP2014W1/JEngine/dev/docu/general/img/fmc-architecture-v2_4.png)

## RESTful

See the [documentation](https://github.com/BP2014W1/JEngine/raw/dev/docu/rest/JEngine_REST_Specs.pdf) inside the doc folder.


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

Now, you may access the JFrontend via

   http://localhost:8080/JEngine

We are using a [AngularJS Template](https://wrapbootstrap.com/theme/homer-responsive-admin-theme-WB055J451); please be sure to download the [AngularJS libs](https://docs.angularjs.org/misc/downloading)
   
## Features

The Chimera engine supports the execution of
* email- ,
* webservice- and
* user-tasks

within fragments of a scenario.

