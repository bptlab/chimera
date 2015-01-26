<?php
include 'core/config.php';
include 'core/RESTCalls.php';

$scenarios = ShowScenarios();
$ScenarioIDs = $scenarios["ids"];
$ScenarioIDs = [2];
foreach ($ScenarioIDs as &$scenario_values) {
    echo "<hr>Scenario: <b> ".$scenario_values."</b>";
    $instances_array = ShowScenarioInstances($scenario_values);
    $instances = $instances_array["ids"];
    echo "<table><tr>";
    foreach ($instances as &$instances_value) {
            echo "<th> Instance <p>".$instances_value."</p>";
            echo "<i>Activities as 'enabled' </i><br>";
            $activities_begin =  GetActivities($scenario_values, $instances_value, "enabled");
	    if($activities_begin === "{empty}") {
                echo "empty";
            } else {
                $activities_begin_array = $activities_begin["ids"];
	//	echo "activities_begin_array:"; var_dump($activities_begin_array);
                foreach ($activities_begin_array as &$activities_begin_array_value) {
                    echo $activities_begin["label"][$activities_begin_array_value]."<br>";
                }
            }

            echo "<br><i>Activities as 'terminated' </i><br>";
            $activities_terminate =  GetActivities($scenario_values, $instances_value, "terminated");
            if($activities_terminate === "{empty}") {
                echo "empty";
            } else {
                $activities_terminate_array = $activities_terminate["ids"];
	//	echo "activities_terminate_array:"; var_dump($activities_terminate_array);
                foreach ($activities_terminate_array as &$activities_terminate_array_value) {
                    echo $activities_terminate["label"][$activities_terminate_array_value] ."<br>";
                }
            }
            //var_dump($activities_terminate);
            echo "</th>";
    }
    echo "</tr></table>";
}
