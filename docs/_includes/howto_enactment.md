# Howtos

## Executing a Case

Once a case has been exported to a Chimera instance, it can be found in
the Chimera sidebar under “Scenarios”. There it can be selected for
execution. In the execution, the predefined tasks are enabled based on
control flow and data flow, and can be selected by the user in the user
interface.

Cases can be started manually, or through the influence of an event
modelled in the start condition of the case model. If the event occurs,
a new case will be created independent of the user input. Alternatively,
a new case can always be started manually (although the data conditions
from the start condition may not be fulfilled).

<video width="550" autoplay loop muted>
    <source src="{{ site.github.url }}/vid/start_new_case.webm" type="video/webm">
    Your browser does not support HTML video.
</video>

Once a case has been started, all enabled tasks can be seen in the
“Unassigned Tasks” panel. From there, a task can be selected in order to
begin its execution. It will then be transferred to “Open Tasks”.

If there is a data input to a given activity, the user can select which
of the currently active data objects should be used as input, and
operated on.

<video width="550" autoplay loop muted>
    <source src="{{ site.github.url }}/vid/begin_activity.webm" type="video/webm">
    Your browser does not support HTML video.
</video>

Open tasks can be terminated by clicking the check button. Once a task
is terminated, its outgoing control flow will be enabled. The engine
computes the enablement of the following nodes and provides the next
tasks in the “Unassigned Tasks” panel.

Upon terminating a task, the user can select the states of the outgoing
data objects, if an activity has multiple output sets.

<video width="550" autoplay loop muted>
    <source src="{{ site.github.url }}/vid/terminate_activity.webm" type="video/webm">
    Your browser does not support HTML video.
</video>

The Chimera case overview page provides different information about the
current state of the case:

-   The “Processing Status” panel shows the active data objects of the
    case. It shows their data class, their current state, and their Id
    in order to differentiate between data objects of the same data
    class.
-   The Log panel shows steps that have already happened in the case
    execution. You can see the state transitions of tasks and data
    objects, as well as changes in data object attributes.
-   The “Case Conditions” panel shows the termination conditions in the
    form of data objects and their corresponding states. When all data
    objects specified in a condition have the specified state, the case
    can be terminated through a “Terminate” button.
-   On the bottom of the page, all fragment models are displayed in
    order to provide an overview of the case steps.

#### Execution Screencast

<video width="550" controls>
    <source src="{{ site.github.url }}/vid/screencast.mp4" type="video/mp4">
    Your browser does not support HTML video.
</video>
The screencast can also be downloaded from [here](https://bpt.hpi.uni-potsdam.de/Chimera/WebHome) if your browser does not support mp4.

## Automatic Tasks

##### Webservice Tasks

Webservice tasks are modelled using the BPMN Service Task. In the
[bpmn.io](http://bpmn.io) editor of Gryphon, you can define the
properties of the webservice call in the sidebar. For each webservice
task, you can provide a webservice URL and a method (GET, POST, PUT or
DELETE). In the case of POST and PUT, you can also specify an HTTP body
with data that will be transmitted in the call.

For example, calling the webservice with URL `http://api.openweathermap.org/data/2.5/weather?q=Potsdam&APPID=d0b8dcaef9210ab1d713f44a3bcf589c` and HTTP metod `GET` returns the following JSON:
`{"coord":{"lon":13.07,"lat":52.4},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],"base":"stations","main":{"temp":280.15,"pressure":1017,"humidity":65,"temp_min":280.15,"temp_max":280.15},"visibility":10000,"wind":{"speed":5.7,"deg":260},"clouds":{"all":40},"dt":1458406200,"sys":{"type":1,"id":4892,"message":0.0329,"country":"DE","sunrise":1469589694,"sunset":1469646353},"id":2852458,"name":"Potsdam","cod":200}`

## Event Integration

Events are modelled in Gryphon with BPMN Message Receive Events or
Message Receive Tasks. Similar to webservice tasks you can specify the
attributes for the event registration in the sidebar. In the field
“Event-Query for Unicorn”, you can add a Query in
[Esper](http://www.espertech.com/esper/) Event Processing Language
(EPL). This query will be registered with in Unicorn, and will spawn a
notification once it triggers on an event, notifying the Chimera
instance about the occurrance of said event.

More information on the integration of events can be found in the
[bachelor
thesis](https://bpt.hpi.uni-potsdam.de/foswiki/pub/FCM/CMPublications/Bachelorarbeit_Jonas.pdf)
of Jonas Beyer. Example: Event-Query for Unicorn:
`SELECT * FROM TemperatureEvent WHERE Temperature > 20.0`

#### Saving External Attribute Values

Events and webservice tasks can return data. In order to incorporate
this data into the case data objects, you can specify certain attributes
for them in the [bpmn.io](http://bpmn.io) editor.\
For each data object that is outgoing of an event or a webservice task,
you can set those values. To this end, each attribute of the data object
can be mapped to a
[JSONPath](https://github.com/jayway/JsonPath)-Expression. JSONPath is a
language for evalutation data in the JSON format, similar like XPath for
XML. These expressions will be evaluated on the returned data, and the
resulting values will be written into the specified data attribute.

Example:\
Given the returned data from the example for webservice tasks, we can
specify the following:\
For the outgoing data object `Temperature`\
JSON Path for `TemperatureValue`: `$.main.temp`\
This results in the value `280.15` being saved into the
`TemperatureValue` attribute.

## History and Logging

In order to provide information about the case history or analyse it
later on the history rest service can be used. All requests go to the
url `history/v2/scenario/{scenarioId}` The things that are logged by
Chimera are:

-   Change of state in activity instances
-   Change of state of events
-   Change of state of data objects
-   Change of value of a data attribute

### Chimera log format

At `history/v2/scenario/{scenarioId}/instance/{scenarioInstanceId` all
of these transitions for a specific scenario instance can be accessed.
The result will be a JSON Array of entries from the following format:

    {
        "loggedId": {id},
        "label": "{label}",
        "oldValue": "{oldValue}",
        "newValue": "{newValue}",
        "timeStamp": {Date},
        "cause": {causeId}
    }

Where `loggedId` is the Id of the (activity | data object | data
attribute | event). The time stamp is in the format: “YYYY-MM-DD
HH:MM:SS.SSS” The `cause` field is used to indicate whether the
transition is a result of another transition. As an example if the
Activity Instance “approve Costumer” terminates and changes the state of
the data object customer from reviewed to accepted there are two new
transitions. Assuming that the id of the activity instance is 123 and
the id of the data object is 234. The new transitions added would be.

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

At creation of each of one on the above mentioned logged entities there
is a initial transition where the old value is set to `NULL`.

### XES support

To support analysis of case models via the well known [PROM
tool](http://www.promtools.org/doku.php) there is the possibility to
export the history of all instances of a case model in the [XES data
format](http://www.xes-standard.org/). This can be achieved via the a
GET request to `history/v2/scenario/{scenarioId}/export` The format is
as follows:

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

Where each trace represents one specific instance of a case model. The
type is analogous to the types in the Chimera log format (activity |
data object | data attribute | event). The big difference in comparison
to the Chimera Log format XES represents only the new state not a state
transition. Therefore only the “value” key is present, which is
equivalent to the new value. entryId is the id of the log entry itself.
