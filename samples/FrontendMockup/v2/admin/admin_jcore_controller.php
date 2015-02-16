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
		    foreach($ScenarioIDs as $key => $value) {
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
		    foreach($scenarioinstanceIds as $key => $value) {
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
        <strong>Current Working Set</strong> Scenario ID: <?php echo $_COOKIE['JEngine_ScenarioID']; ?> | Instance ID: <?php echo $_COOKIE['JEngine_ScenarioInstanceID']; ?> |     
        <form action='update_jcore_controller.php' method='post'>
	            <input type='hidden' name='pcm_scenarioID_new_Instance' value='".$_COOKIE['JEngine_ScenarioID']."'>
	            <input type='submit' class="btn btn-link" value='create new Instance'>
		</form> 
    </div>
<?php 
	$enabled_activities = GetActivities($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID'], "enabled");
    
    echo "<h4>enabled Activities</h4>
    		<table>
    			<tr>
    				<th>Activity ID</th>
    				<th>Activity Label</th>
    				<th>Action</th>
    			</tr>";
    foreach($enabled_activities["label"] as $key => $value) {
			  echo "<tr>";
			  echo "<th>".$key."</th>";
			  echo "<th>".$value."</th>";
			  echo "<th><form action='update_jcore_controller.php' method='post'>
	            			<input type='hidden' name='update_activity_status' value='true'>
	            			<input type='hidden' name='pcm_scenarioID' value='".$_COOKIE['JEngine_ScenarioID']."'>
	            			<input type='hidden' name='pcm_scenarioinstances' value='".$_COOKIE['JEngine_ScenarioInstanceID']."'>
	            			<input type='hidden' name='pcm_activity' value='".key."'>
	          				<input type='submit' class='btn btn-link' value='terminate'>
						</form> </th>";
			  echo "</tr>";
	}
	echo "</table>
		  <br><hr>";

    $terminated_activities = GetActivities($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID'], "terminated");
    
    echo "<h4>terminated Activities</h4>
    		<table>
    			<tr>
    				<th>Activity ID</th>
    				<th>Activity Label</th>
    			</tr>
    			<tr>";
    foreach($terminated_activities["label"] as $key => $value) {
    	      echo "<tr>";
			  echo "<th>".$key."</th>";
			  echo "<th>".$value."</th>";
		      echo "</tr>";
	}
	echo "</table>";
} ?>
