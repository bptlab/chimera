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

For further information visit https://bpt.hpi.uni-potsdam.de/Chimera

## Modeling Case Models

For this you need the [Gryphon Case Modeler](https://github.com/bptlab/gryphon) which allows to create case models and deploy them into the Chimera engine.

## REST API

See the [pdf documentation](https://github.com/bptlab/chimera/raw/dev/docu/rest/JEngine_REST_Specs.pdf) inside the docu folder or refer to the [swagger files](https://github.com/bptlab/chimera/raw/dev/docu/rest/swagger.json).
   


