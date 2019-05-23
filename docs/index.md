---
# Feel free to add content and custom Front Matter to this file.
# To modify the layout, see https://jekyllrb.com/docs/themes/#overriding-theme-defaults

layout: default
---

<img src="media\chimera_logo.jpg" alt="Chimera" width="300" height="233">

# Welcome to Chimera
Chimera is an engine for executing fragment based case models, an approach that allows for modelling flexible business processes. It was developed at the [Business Process Technology (BPT)](https://bpt.hpi.uni-potsdam.de/Public/) group at the Hasso Plattner Institute (HPI). It is provided under the MIT free and open source software license (see LICENSE.md). Chimera supports:

- Execution and combination of [BPMN2](https://www.omg.org/spec/BPMN/2.0/) models based on data dependencies
- Integration to external events via the Complex Event Processing engine [Unicorn](https://bpt.hpi.uni-potsdam.de/UNICORN).
- Usage of external APIs via webservice tasks.

For further reference please visit our [website](https://bpt.hpi.uni-potsdam.de/Chimera/WebHome).

# Getting started
## Pre-requisites.

The following software is necessary to build and run Chimera:
- [Oracle JDK or OpenJDK]( https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) and make sure the environment variable JAVA_HOME points to your JDK folder (e.g. like [this](https://www.wikihow.com/Set-Java-Home)).
- [Apache Tomcat 7 or 8](https://tomcat.apache.org/download-70.cgi).
- [MySQL 5.7 Server](https://dev.mysql.com/downloads/mysql/).
- [git](https://git-scm.com/downloads), also available to access the code repositories.
- [Apache Maven 3](http://maven.apache.org/install.html).

## Installation & Setup
1. Checkout out the source code from [github](https://github.com/bptlab/chimera). From the command line you can run `git clone https://github.com/bptlab/chimera.git` to download the repository to your machine.
2. Copy the project configuration `/src/main/resources/config.properties` from the template config_template.properties and fill in your properties.
3. Start your `MySQL server` and create a database, either using the `MySQL Workbench` or from the command line: `mysql -u USER_NAME -p PASSWORD -e "create schema SCHEMA_NAME` where `SCHEMA_NAME` should be something like ChimeraDB.
4. Build the source code using Maven: `mvn install -Ddb.user=USER_NAME -Ddb.password=PASSWORD -Ddb.schema=SCHEMA_NAME` Replace *USER_NAME, PASSWORD*, and *SCHEMA_NAME* with the values from the previous step.
5. Deploy the created war file `target/Chimera.war` to Tomcat by copying it to the webapps folder in your Tomcat installation.
- Alternatively, use `mvn tomcat7:deploy -DskipTests` from the command line to automatically deploy the war file. Note however, that you need to configure your Tomcat credentials as described in [this article](https://www.mkyong.com/maven/how-to-deploy-maven-based-war-file-to-tomcat/).
6. Start your Tomcat application server and visit [http://localhost:8080/Chimera](http://localhost:8080/Chimera) in your browser, replacing the default port 8080 with the one you configured in Tomcat. You should now be able to see the Chimera frontend.

- Incase you are facing any issues Installing Chimera, you can try this the manual way as the video below:
<video width="500" height="300" controls>
  <source src="media\installChimera.webm" type="video/webm">
</video>


For further information visit [https://bpt.hpi.uni-potsdam.de/Chimera](https://bpt.hpi.uni-potsdam.de/Chimera)

# Modelling Case models
For this you need the [Gryphon Case Modeler](https://github.com/bptlab/gryphon) which allows to create case models and deploy them into the Chimera engine.

# REST API
See the [pdf documentation](https://github.com/bptlab/chimera/raw/dev/docu/rest/JEngine_REST_Specs.pdf) inside the docu folder or refer to the [swagger files](https://github.com/bptlab/chimera/raw/dev/docu/rest/swagger.json).
## **Howtos**
# Executing a Case
Once a case has been exported to a Chimera instance, it can be found in the Chimera sidebar under “Scenarios”. There it can be selected for execution. In the execution, the predefined tasks are enabled based on control flow and data flow, and can be selected by the user in the user interface.

Cases can be started manually, or through the influence of an event modelled in the start condition of the case model. If the event occurs, a new case will be created independent of the user input. Alternatively, a new case can always be started manually (although the data conditions from the start condition may not be fulfilled).

<video width="500" height="300" controls>
  <source src="media\start_new_case.webm" type="video/webm">
</video>

Once a case has been started, all enabled tasks can be seen in the “Unassigned Tasks” panel. From there, a task can be selected in order to begin its execution. It will then be transferred to “Open Tasks”.

If there is a data input to a given activity, the user can select which of the currently active data objects should be used as input, and operated on.

<video width="500" height="300" controls>
  <source src="media\begin_activity.webm" type="video/webm">
</video>


Open tasks can be terminated by clicking the check button. Once a task is terminated, its outgoing control flow will be enabled. The engine computes the enablement of the following nodes and provides the next tasks in the “Unassigned Tasks” panel.

Upon terminating a task, the user can select the states of the outgoing data objects, if an activity has multiple output sets.

<video width="500" height="300" controls>
  <source src="media\terminate_activity.webm" type="video/webm">
</video>



The Chimera case overview page provides different information about the current state of the case:
- The “Processing Status” panel shows the active data objects of the case. It shows their data class, their current state, and their Id in order to differentiate between data objects of the same data class.
- The Log panel shows steps that have already happened in the case execution. You can see the state transitions of tasks and data objects, as well as changes in data object attributes.
- The “Case Conditions” panel shows the termination conditions in the form of data objects and their corresponding states. When all data objects specified in a condition have the specified state, the case can be terminated through a “Terminate” button.
- On the bottom of the page, all fragment models are displayed in order to provide an overview of the case steps.

<video width="500" height="300" controls>
  <source src="media\screencast.mp4" type="video/mp4">
</video>



The screencast can also be downloaded from [Chimera's Webhomepage](https://bpt.hpi.uni-potsdam.de/Chimera/WebHome) if your browser does not support mp4.

## Automatic tasks
#### Webservice Tasks

Webservice tasks are modelled using the BPMN Service Task. In the bpmn.io editor of Gryphon, you can define the properties of the webservice call in the sidebar. For each webservice task, you can provide a webservice URL and a method (GET, POST, PUT or DELETE). In the case of POST and PUT, you can also specify an HTTP body with data that will be transmitted in the call.

For example, calling the webservice with URL:
`http://api.openweathermap.org/data/2.5/weather?q=Potsdam&APPID=d0b8dcaef9210ab1d713f44a3bcf589c` and HTTP method `GET` returns the following JSON:
{% highlight ruby %}
{
  "coord":{"lon":13.07,"lat":52.4},"weather":
  [
    {"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}
  ],"base":"stations","main":
  {"temp":280.15,"pressure":1017,"humidity":65, "temp_min":280.15,"temp_max":280.15},
  "visibility":10000,
  "wind":{"speed":5.7, "deg":260},
  "clouds": {"all":40},
  "dt":1458406200,
  "sys":{
          "type":1,"id":4892,"message":0.0329,"country":"DE","sunrise":
           1469589694,"sunset":1469646353
        },
  "id":2852458,"name":"Potsdam","cod":200
}`
{% endhighlight %}

## Event Integration
Events are modelled in Gryphon with BPMN Message Receive Events or Message Receive Tasks. Similar to webservice tasks you can specify the attributes for the event registration in the sidebar. In the field “Event-Query for Unicorn”, you can add a Query in [Esper](http://www.espertech.com/esper/) Event Processing Language (EPL). This query will be registered with in Unicorn, and will spawn a notification once it triggers on an event, notifying the Chimera instance about the occurrence of said event.

More information on the integration of events can be found in the [Bachelor thesis of Jonas Beyer](https://bpt.hpi.uni-potsdam.de/foswiki/pub/FCM/CMPublications/Bachelorarbeit_Jonas.pdf). Example: Event-Query for Unicorn: SELECT * FROM TemperatureEvent WHERE Temperature > 20.0

### Saving External Attribute Values
Events and webservice tasks can return data. In order to incorporate this data into the case data objects, you can specify certain attributes for them in the [bpmn.io](http://bpmn.io/) editor.\ For each data object that is outgoing of an event or a webservice task, you can set those values. To this end, each attribute of the data object can be mapped to a [JSONPath-Expression](https://github.com/json-path/JsonPath). JSONPath is a language for evalutation data in the JSON format, similar like XPath for XML. These expressions will be evaluated on the returned data, and the resulting values will be written into the specified data attribute.

Example:\ Given the returned data from the example for webservice tasks, we can specify the following:\ For the outgoing data object `temperature`\ JSON Path for `TemperatureValue`: `$.main.temp`\ This results in the value `280.15` being saved into the `TemperatureValue` attribute.

## History and Logging
In order to provide information about the case history or analyse it later on the history rest service can be used. All requests go to the url `history/v2/scenario/{scenarioId}` The things that are logged by Chimera are:

- Change of state in activity instances
- Change of state of events
- Change of state of data objects
- Change of value of a data attribute

### Chimera log format
At `history/v2/scenario/{scenarioId}/instance/{scenarioInstanceId` all of these transitions for a specific scenario instance can be accessed. The result will be a JSON Array of entries from the following format:

{% highlight ruby %}
{
    "loggedId": {id},
    "label": "{label}",
    "oldValue": "{oldValue}",
    "newValue": "{newValue}",
    "timeStamp": {Date},
    "cause": {causeId}
}
{% endhighlight %}

Where `loggedId` is the Id of the (activity | data object | data
attribute | event). The time stamp is in the format: “YYYY-MM-DD HH:MM:SS.SSS” The
`cause` field is used to indicate whether the transition is a result of another transition. As an example if the Activity Instance “approve Costumer”
terminates and changes the state of the data object customer from reviewed to accepted there are two new transitions. Assuming that the id of the
activity instance is 123 and the id of the data object is 234. The new transitions added would be.

{% highlight ruby %}
[
    {
        "loggedId": 123,
        "label": "Change Dataobject",
        "oldValue": "RUNNING",
        "newValue": "TERMINATED",
        "timeStamp": {Date},
        "cause": NULL
    },
    {
        "loggedId": 234,
        "label": "Customer",
        "oldValue": "reviewed",
        "newValue": "accepted",
        "timeStamp": {Date},
        "cause": 123
    }
]
{% endhighlight %}

At creation of each of one on the above mentioned logged entities there is a initial transition where the old value is set to `NULL`.

### XES support
To support analysis of case models via the well known [PROM tool](http://www.promtools.org/doku.php) there is the possibility to export the history of all instances of a case model in the [XES data format](http://www.xes-standard.org/). This can be achieved via the a GET request to `history/v2/scenario/{scenarioId}/export` The format is as follows:

{% highlight ruby %}
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<log xmlns="http://www.xes-standard.org">
  <trace>
    <event>
      <timestamp key="timestamp" value="YYYY-MM-DD HH:MM:SS.SSS"/>
      <string key="entryId" value="entryId"/>
      <string key="type" value="ACTIVITY"/>
      <string key="value" value="{newValue}"/>
      <string key="label" value="{label}"/>
      <string key="id" value="{valueId}"/>
      <string key="cause" value="{causeId}"/>
    </event>
        ...
  </trace>
    ...
</log>
{% endhighlight %}

Where each trace represents one specific instance of a case model. The type is analogous to the types in the Chimera log format (activity | data object | data attribute
| event). The big difference in comparison to the Chimera Log format XES represents only the new state not a state transition. Therefore only the “value” key is
present, which is equivalent to the new value. entryId is the id of the log entry itself.

# Features
This section briefly describes the features of the Chimera case engine.

## Process Variables
Process variables are used to refer to the values of data object attributes. The basic form for process variables is `#DataClassName.attributeName`, where `DataClassName` is the name of a defined data class and `attributeName` is the name of an attribute of that class. They can be used in several locations in the process fragment:
- on outgoing sequence flow arcs of exclusive gateways (XOR split) to specify conditions, e.g. `#User.age > 17`
- in webservice tasks to make calls depending on case data, e.g.
    - use `https://www.reisewarnung.net/api?country=#Request.countryCode` as URI for the call (after the user entered the country code in a previous task)
    - use `{ "title":"#Post.title", "message": "#Post.message" }` as Post Body
- in email tasks to include user-specific text and address the email, e.g. using `#User.emailAddress` as recipient for the email
- in the JsonPath expressions used to fill attributes of data objects, e.g. to retrieve either the German or English message of travel information you could use `$.data.lang.#Request.userLang.advice` as JsonPath expression of the `message` attribute

## Webservice tasks
**This kind of task calls a REST webservice identified by an URI, expecting a JSON** response. The task type in the Gryphon case modeler needs to be changed to ‘Service Task’ (with the cogwheel symbol). Then three parameters can be provided in the property panel on the right:

- the URI of the webservice
- the http method of the REST request, i.e. GET, POST, PUT, DELETE
- a body for the call [**todo** is there a specific format to follow?]

When the webserive task is enabled during case enactment, it will begin automatically, call the webservice with the provided parameters, and terminate. Using the results of the call requires a data object connected by an outgoing data flow arc. For each attribute of this data object you can specify a JsonPath expression, e.g. `$.data.lang.en.advice` for a call to `https://www.reisewarnung.net/api?country=DE` to retrieve the advice message for traveling Germany.

For a webservice task to execute correctly, its input and output set needs to be unique. This means that it cannot have multiple data nodes referencing the same data class connected by incoming data flow arcs. [**todo** link to description of in/output sets]. If the task suceeds an exclusive gateway (XOR split) it will not be executed automatically, but can still be started manually.

## Email Tasks
## Data-based Gateways
## Event-based Gateways
## Boundary Events
## External Start Events

# Troubleshooting
### Chimera crashes while persisting after adding new annotated classes or attributes in the code or changing the code that do the persistence.

This can happen when the Database schema doesnt match the annotated classes and attributes. This can happen because there are no table for the newly added classes because new tables aren’t generated from EclipseLink on startup. This happens when in the persistencexml the property “eclipselink.ddl-generation” doesnt have the values “create-table” or “drop-and-create-table”. But even when these values are set and all classes are annotated correctly there are sometimes error. Therfore it’s often necessary to drop and create the database schema manually before redeploying Chimera.

### Errors while redeploying with “tomcat7:redeploy -DskipTests”
When you use maven with the goal mvn tomcat:redeploy -DskipTests (For example by creating such a run configuration in Eclipse or using the shell command “mvn tomcat7:redepoy -DskipTests”) after making major changes in the code, it can happen that there are stille errors despite the code works correct. To avoid this, its a good idea from time to time to deploy manually via the following staps.

1. open a shell in the chimera base directory (where the pom.xml is located)
2. run “mvn clean install -DskipTests”
3. now you find in the folder “target” in your chimera base directory a file “.war” file (Chimera.war). Copy this file
4. navigate in your tomcat directory and open the “webapp” folder (4.a If tomcat is currently running, stop it!)
5. If you previously deployed Chimera you will there find a file like “Chimera.war” and a corresponding directory like “Chimera”. Delete the file and the directory.
6. paste the “.war” file (Chimera.war)
7. start tomcat
8. Chimera should now be correcly deployed and automatically is executed with the startup of tomcat.
