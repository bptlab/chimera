# Features

This section briefly describes the features of the Chimera case engine. 

## Process Variables

Process variables are used to refer to the values of data object attributes. The basic form for process variables is `#DataClassName.attributeName`, where `DataClassName` is the name of a defined data class and `attributeName` is the name of an attribute of that class.
They can be used in several locations in the process fragment:
- on outgoing sequence flow arcs of exclusive gateways (XOR split) to specify conditions, e.g. `#User.age > 17`
- in webservice tasks to make calls depending on case data, e.g. 
  * use `https://www.reisewarnung.net/api?country=#Request.countryCode` as URI for the call (after the user entered the country code in a previous task)
  * use `{ "title":"#Post.title", "message": "#Post.message" }` as Post Body
    > Hint: Don't forget the `"` around the process variables and don't use spaces in your data object names
- in email tasks to include user-specific text and address the email, e.g. using `#User.emailAddress` as recipient for the email
- in the JsonPath expressions used to fill attributes of data objects, e.g. to retrieve either the German or English message of travel information you could use `$.data.lang.#Request.userLang.advice` as JsonPath expression of the `message` attribute

## Webservice Tasks

**This kind of task calls a REST webservice identified by an URI, expecting a JSON response.** The task type in the Gryphon case modeler needs to be changed to 'Service Task' (with the cogwheel symbol). Then three parameters can be provided in the property panel on the right:
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
