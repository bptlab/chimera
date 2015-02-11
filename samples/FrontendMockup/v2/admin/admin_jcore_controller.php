<?php
include '../core/config.php';
include '../core/RESTCalls.php';

if(!isset($_COOKIE['JEngine_ScenarioID'])) {
	$scenarios = ShowScenarios();
	$ScenarioIDs = $scenarios["ids"];
	?>
		<h4> Please select your Scenario</h4>
		<form action='update_jcore_controller.php' method='post'>
		<select name="pcm_scenario">
		<option value=""></option>
		<?php 
		    foreach($ScenarioIDs as $key => $value) 
		    {
		       $value = htmlspecialchars($value); 
		       echo '<option value="'. $value .'">'. $value .'</option>';
		    }
		?>
		</select>
		<input type='submit' value='Submit'>
		</form>

<?php } elseif(!isset($_COOKIE['JEngine_ScenarioInstanceID'])) {
	$scenarioinstances = ShowScenarioInstances($_COOKIE['JEngine_ScenarioID']);
	$scenarioinstanceIds = $scenarioinstances["ids"];
	?>
		<h4> Please select your Scenario</h4>
		<form action='update_jcore_controller.php' method='post'>
		<select name="pcm_scenarioinstances">
		<option value=""></option>
		<?php 
		    foreach($scenarioinstanceIds as $key => $value) 
		    {
		       $category = htmlspecialchars($category); 
		       echo '<option value="'. $value .'">'. $value .'</option>';
		    }
		?>
		</select>
		<input type='submit' value='Submit'>
		</form><br>
		<?php
        echo "<form action='update_jcore_controller.php' method='post'>
		            <input type='hidden' name='pcm_scenarioID' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
		            <input type='submit' value='create new Instance'>
		        </form><br>";

} 

if((isset($_COOKIE['JEngine_ScenarioInstanceID'])) && (isset($_COOKIE['JEngine_ScenarioID']))) {

}