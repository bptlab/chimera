<?php
include 'core/config.php';
include 'core/RESTCalls.php';

$scenarios = ShowScenarios();
$ScenarioIDs = $scenarios["ids"];
//var_dump($ScenarioIDs);
foreach ($ScenarioIDs as &$scenario_values) {
    echo "<hr>Scenario: <b> ".$scenario_values."</b>";
    $instances_array = ShowScenarioInstances($scenario_values);
    $instances = $instances_array["ids"];
    //var_dump($instances);
    echo "<table><tr>";
    foreach ($instances as &$instances_value) {
            echo "<th>
                      Instance <p>".$instances_value."</p>";
            echo "<i>Activities as 'enabled' </i><br>";
            $activities_begin =  GetActivities($scenario_values, $instances_value, "enabled");
            var_dump($activities_begin);
            echo "<br><i>Activities as 'terminated' </i><br>";
            $activities_terminate =  GetActivities($scenario_values, $instances_value, "terminated");
            var_dump($activities_terminate);
             echo "</th>";
    }
    echo "</tr></table>";
}