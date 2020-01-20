
# Welcome to Chimera
Chimera is an engine for executing fragment-based case models, an approach that allows for modelling flexible business processes.
It is developed and maintained by the [Business Process Technology (BPT) group](http://bpt.hpi.uni-potsdam.de/) at the Hasso Plattner Institute (HPI).

Chimera is **not a modeling tool** for case models! Creation and deployment of case models is handled by the [Gryphon case modeler](https://github.com/bptlab/gryphon) also available at GitHub.

#### Features of Chimera
* Execution of [BPMN2](http://www.omg.org/spec/BPMN/2.0/) models based on data dependencies
* Integration to external events via the Complex Event Processing engine Unicorn
* Usage of external APIs via webservice tasks

## Getting started
The easiest way to try out Chimera is using the publicly available instance at ~https://bpt-lab.org/chimera-demo~, which contains several exemplary case models demonstrating the features of Chimera. Please refer to the [user guide](https://bptlab.github.io/chimera) on how to start and enact cases.

The following instructions will get the Chimera case engine up and running on your local machine.

### Pre-requisites
The following software is necessary to build and run Chimera:

   * `Java 8 JDK` available from [Oracle](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html). Make sure that the environment variable `JAVA_HOME` points to your JDK folder (e.g. like [this](http://www.wikihow.com/Set-Java-Home)).
   * `Apache Tomcat 7` or newer, available [here](https://tomcat.apache.org/download-70.cgi).
   * `MySQL 5.7 Server`, available [here](https://dev.mysql.com/downloads/mysql/5.7.html#downloads). For your convenience, you can download the [installer](https://dev.mysql.com/get/Downloads/MySQLInstaller/mysql-installer-web-community-5.7.23.0.msi) (direct link) and install MySQL Server, MySQL Notifier, MySQL Workbench, and Connector/J. 
   * `git`, available [here](https://git-scm.com/downloads), to access the code repositories.
   * `Apache Maven 3`, available [here](http://maven.apache.org/install.html).

### Installation

   1. Checkout out the source code from [github](http://github.com/bptlab/chimera). From the command line you can run `git clone https://github.com/bptlab/chimera.git` to download the repository to your machine.
   1. Start your MySQL server and create a database, either using the MySQL Workbench or from the command line: `mysql -u USER_NAME -p PASSWORD -e "create schema SCHEMA_NAME` where `SCHEMA_NAME` should be something like 'chimeradb'.
   1. Similarily, create a second database to be used for running test cases and name it something like 'chimeradb_test'.
   1. Build the source code using Maven: `mvn install -Ddb.user=USER_NAME -Ddb.password=PASSWORD -Ddb.schema=SCHEMA_NAME -Ddb.test.schema=TEST_SCHEMA_NAME -DskipTests` Replace *USER_NAME*, *PASSWORD*, *SCHEMA_NAME*, and *TEST_SCHEMA_NAME* with the values from the previous steps.
   1. Check whether the configuration file `config.properties` was copied to the main directory and whether the variables (`mysql.username`, `mysql.password` etc.) have been replaced correctly.
   1. Deploy the created war file `target/Chimera.war` to Tomcat by copying it to the webapps folder in your Tomcat installation.
      * Alternatively, use `mvn tomcat7:deploy -DskipTests` from the command line to automatically deploy the war file. Note however, that you need to configure your Tomcat credentials as described in [this article](http://www.mkyong.com/maven/how-to-deploy-maven-based-war-file-to-tomcat/).
   1. Start your Tomcat application server and visit http://localhost:8080/Chimera in your browser, replacing the default port 8080 with the one you configured in Tomcat. You should now be able to see the Chimera frontend.

## Creating & Deploying Case Models
Now that you have the Chimera case engine running, you probably want to execute some case models. 
To create case models and deploy them into Chimera you will need the [Gryphon Case Modeler](https://github.com/bptlab/gryphon).

## Executing Cases
To manually start a new case select the case model from the left menu and click on the green `Start Instance` button.
This will open the case view. Enabled activities are displayed on the right; you can begin an activity by clicking on its name.
If the activity has a data pre-condition you will need to select a data object on which it operates.

Running activities are displayed in the middle. You can terminate them by clicking on the checkmark symbol. 
This will open a modal dialog, which allows to terminate the activity and set state and values of a potential output data object.

**For further information check the [Chimera user guide](https://bptlab.github.io/chimera).**

## Deployment
Chimera offers two main deployment options: 1) using a Java Servlet container like Apache Tomcat, 2) using a [Docker](https://www.docker.com/) image.

### Deploying to a Servlet container
This was already described in the installation section. 
If you deploy the war file to a remote server, you need to make sure, that a MySQL server is running with the configured database schema.
> :warning: Deployment was only tested with Apache Tomcat

### Deploying Docker image
Use the `Dockerfile` in the main directory of the project repository to create a docker image.
To start the image in a container follow the [instructions in the wiki](https://github.com/bptlab/chimera/wiki/ChimeraConfig#deployment-with-docker).

Up to date docker images of the Chimera case engine are also hosted on our [dockerhub](https://hub.docker.com/r/bptlab/chimera/).
You can pull these images with `docker pull bptlab/chimera`.

**TODO**: connect with MySQL container

### Developing & Deploying with Docker/Compose
To simplify the local development workflow, you can make use of the `Makefile`. 
**Todo**: mention requirements for this approach.

* Maven build: `make build`
* Local tomcat release: `make release_local` (requires Tomcat and Unix)
* Docker build + deploy: `make docker` (update docker tag in `docker-compose.yml` beforehand!)

## Contributing
You want to help us? Great :+1:

Please check out [Contributing.md](CONTRIBUTING.md) for information how you can contribute to the Chimera project, including templates for bug reports, feature suggestions, and pull requests. For in-depth information about the architecture and the individual components of Chimera, the [developer documentation in the wiki](https://github.com/bptlab/chimera/wiki/DevDoc) is the best starting point.

## License 
The Chimera case engine is provided under the MIT free and open source software license - see [LICENSE.md](LICENSE.md) for details.
