# Welcome to Chimera
Chimera is an engine for executing fragment based case models, an approach that allows for modelling flexible business processes. It was developed at the [Business Process Technology (BPT) group](http://bpt.hpi.uni-potsdam.de/Public/) at the Hasso Plattner Institute (HPI). It is provided under the MIT free and open source software license (see LICENSE.md). Chimera supports:

* Execution and combination of [BPMN2](http://www.omg.org/spec/BPMN/2.0/) models based on data dependencies
* Integration to external events via the Complex Event Processing engine [Unicorn](https://bpt.hpi.uni-potsdam.de/UNICORN)
* Usage of external APIs via webservice tasks.

For further reference please visit our [website](https://bpt.hpi.uni-potsdam.de/Chimera/WebHome).

## Getting started

#### Pre-requisites
The following software is necessary to build and run Chimera:

   * `Oracle JDK`  or `OpenJDK` available at  [here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and make sure the environment variable `JAVA_HOME ` points to your JDK folder (e.g. like [this](http://www.wikihow.com/Set-Java-Home)).
   * Apache `Tomcat 7` or 8, available at [here](https://tomcat.apache.org/download-70.cgi).
   * `MySQL 5.7` Server, available [here](http://dev.mysql.com/downloads/mysql/).
   * `git`, available  [here](https://git-scm.com/downloads), to access the code repositories.
   * Apache Maven 3, available [here](http://maven.apache.org/install.html).

#### Installation & Setup

   1. Checkout out the source code from [github](http://github.com/bptlab/chimera). From the command line you can run `git clone https://github.com/bptlab/chimera.git` to download the repository to your machine.
   1. Copy the project configuration `/src/main/resources/config.properties` from the template `config_template.properties` and fill in your properties.
   1. Start your `MySQL server` and create a database, either using the `MySQL Workbench` or from the command line: `mysql -u USER_NAME -p PASSWORD -e "create schema SCHEMA_NAME` where `SCHEMA_NAME` should be something like ChimeraDB.
   1. Build the source code using Maven: `mvn install -Ddb.user=USER_NAME -Ddb.password=PASSWORD -Ddb.schema=SCHEMA_NAME` Replace *USER_NAME*, *PASSWORD*, and *SCHEMA_NAME* with the values from the previous step.
   1. Deploy the created war file `target/Chimera.war` to Tomcat by copying it to the webapps folder in your Tomcat installation.
      * Alternatively, use `mvn tomcat7:deploy-DskipTests` from the command line to automatically deploy the war file. Note however, that you need to configure your Tomcat credentials as described in [this article](http://www.mkyong.com/maven/how-to-deploy-maven-based-war-file-to-tomcat/).
   1. Start your Tomcat application server and visit http://localhost:8080/Chimera in your browser, replacing the default port 8080 with the one you configured in Tomcat. You should now be able to see the Chimera frontend.

For further information visit https://bpt.hpi.uni-potsdam.de/Chimera

## Modeling Case Models

For this you need the [Gryphon Case Modeler](https://github.com/bptlab/gryphon) which allows to create case models and deploy them into the Chimera engine.

## REST API

See the [pdf documentation](https://github.com/bptlab/chimera/raw/dev/docu/rest/JEngine_REST_Specs.pdf) inside the docu folder or refer to the [swagger files](https://github.com/bptlab/chimera/raw/dev/docu/rest/swagger.json).

{% include howto_enactment.md %}

{% include features.md %}

{% include troubleshooting.md %}
