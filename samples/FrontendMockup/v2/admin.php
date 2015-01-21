<?php
include 'core/config.php';
include 'core/RESTCalls.php';

//update Cookie Values in case of POST is set.
if(isset($_POST["ScenarioID"])){
  setcookie("JEngine_ScenarioID", $_POST["ScenarioID"]);
  setcookie("JEngine_ScenarioInstanceID", $_POST["ScenarioInstanceID"]);
  setcookie("JEngine_ActivityID", $_POST["ActivityID"]);
  setcookie("JEngine_UserID", $_POST["UserID"]);
  echo "update successful";
}

//if cookie is set, do ... else you could automatically generate IDs..
if(isset($_COOKIE['JEngine_ScenarioInstanceID']) && $_COOKIE['JEngine_ScenarioInstanceID'] === "1") {

}

//print out form for editing values
echo"<html><body>
     <form action='admin.php' method='post'>
        JEngine_ScenarioID: <input type='text' name='ScenarioID' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
        JEngine_ScenarioInstanceID: <input type='text' name='ScenarioInstanceID' value='".$_COOKIE['JEngine_ScenarioInstanceID']."'><br>
        JEngine_ActivityID: <input type='text' name='ActivityID' value='".$_COOKIE['JEngine_ActivityID']."'><br>
        JEngine_UserID: <input type='text' name='UserID' value='".$_COOKIE['JEngine_UserID']."'><br>
      <input type='submit'>
    </form>
    </body></html> ";



$scenarios = ShowScenarios();
foreach ($scenarios as &$scenario_value) {
    echo "<h3>".$scenario_value."</h3>";
    $instances = ShowScenarioInstances($scenario_value);
    foreach ($instances as &$instances_value) {
            echo "<h3>".$instances_value."</h3>";

            echo "<h4>Activities as 'begin' </h4>";
            $activities_begin =  GetActivities($scenario_value, $instances_value, "begin");
            print_r($activities_begin);

            echo "<h4>Activities as 'terminate' </h4>";
            $activities_terminate =  GetActivities($scenario_value, $instances_value, "terminate");
            print_r($activities_terminate);
    }
}

