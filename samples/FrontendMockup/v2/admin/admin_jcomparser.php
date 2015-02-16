<?php
include '../core/config.php';
include '../core/RESTCalls.php';


$scenarios_array = GetAvailableScenarios();
$scenarios = $scenarios_array['ids'];

foreach ($scenarios as $scenario_value => $scenario_label) {
	//echo GetScenarioImage($scenario_value);
    echo "<hr>Scenario: <b> ".$scenario_label."</b> with the ID ".$scenario_value;
    echo "<form action='update_jcomparser.php'>
             <input type='hidden' name='scenarioID' value='".$scenario_value."'>
             <input type='submit' value='Submit'>
          </form> ";
}

?>


