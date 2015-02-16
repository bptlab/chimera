<?php
include '../core/config.php';
include '../core/RESTCalls.php';

if(!isset($_COOKIE['JEngine_ScenarioID'])) {
	$scenarios = ShowScenarios();
	$ScenarioIDs = $scenarios["ids"];
	?>
		<h4> Please select your Scenario</h4>
		<form action='update_jcore_controller.php' method='post'>
		<select name="pcm_scenarioID">
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
		<h4> Please select your ScenarioInstance</h4>
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
		            <input type='hidden' name='pcm_scenarioID_new_Instance' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
		            <input type='submit' value='create new Instance'>
		        </form><br>";

} 

if((isset($_COOKIE['JEngine_ScenarioInstanceID'])) && (isset($_COOKIE['JEngine_ScenarioID']))) { ?>

    <div class="alert alert-info">
        <a href="#" class="close" data-dismiss="alert">&times;</a>
        <strong>Note!</strong> Please read the comments carefully.
    </div>
 
    <p>Current Scenario ID: <?php echo $_COOKIE['JEngine_ScenarioID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioModal">Change me</a> </p>
    <p>Current Instance ID: <?php echo $_COOKIE['JEngine_ScenarioInstanceID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioInstanceModal">Change me</a> </p>
    <p><form action='update_jcore_controller.php' method='post'>
	            <input type='hidden' name='pcm_scenarioID_new_Instance' value='".$_COOKIE['JEngine_ScenarioID']."'>
	            <input type='submit' value='create new Instance'>
	</form></p>


<?php } ?>
