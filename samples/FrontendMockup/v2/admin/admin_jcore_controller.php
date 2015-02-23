<?php
include '../core/config.php';
include '../core/RESTCalls.php';

$reset = $_GET['reset'];

if(!isset($_COOKIE['JEngine_ScenarioID']) || ($reset == "scenarioID")) {
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

<?php } elseif(!isset($_COOKIE['JEngine_ScenarioInstanceID']) || ($reset == "scenarioinstanceID")) {
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
	            <input type='hidden' name='reset_scenarioID' value='true'>
	            <input type='submit' class="btn btn-link" value='change scenarioID'>
		</form> 
		| <form action='update_jcore_controller.php' method='post'>
	            <input type='hidden' name='reset_scenarioinstanceID' value='true'>
	            <input type='submit' class="btn btn-link" value='change scenarioinstanceID'>
		</form> 
    </div>
<?php 

	$enabled_activities = GetActivities($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID'], "enabled");
    
    echo "<h4>enabled Activities</h4>
    		<table>
    			<tr>
    				<th> ID</th>
    				<th> Label</th>
    				<th>Action</th>
    			</tr>";

    $amount_of_enabled_activities = count($enabled_activities["ids"]);
    for ($i = 0; $i < $amount_of_enabled_activities; $i++) {
    	 $key = $enabled_activities["ids"][$i];
    	 $label = $enabled_activities["label"][$key];
    	 if($key == "{" || $value == "{") {
    	 	continue;
    	 }
    //foreach($enabled_activities["label"] as $key => $value) {
			  echo "<tr>";
			  echo "<th>".$key."</th>";
			  echo "<th>".$label."</th>";
			  echo "<th><form action='update_jcore_controller.php' method='post'>
	            			<input type='hidden' name='update_activity_status_begin' value='true'>
	            			<input type='hidden' name='pcm_scenarioID' value='".$_COOKIE['JEngine_ScenarioID']."'>
	            			<input type='hidden' name='pcm_scenarioinstances' value='".$_COOKIE['JEngine_ScenarioInstanceID']."'>
	            			<input type='hidden' name='pcm_activity' value='".$key."'>
	          				<input type='submit' class='btn btn-link' value='begin'>
						</form> </th>";
			  echo "</tr>";
	}
	echo "</table>
		  <br><hr>";
	$running_activities = GetActivities($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID'], "running");
    			
    echo "<h4>running Activities</h4>
    		<table>
    			<tr>
    				<th> ID</th>
    				<th> Label</th>
    			</tr>
    			<tr>";

    $amount_of_running_activities = count($running_activities["ids"]);
    for ($i = 0; $i < $amount_of_running_activities; $i++) {
    	 $key = $running_activities["ids"][$i];
    	 $value = $running_activities["label"][$key];
    	 if($key == "{" || $value == "{") {
    	 	continue;
    	 }
	//}
    //foreach($running_activities["label"] as $key => $value) {
    	      echo "<tr>";
			  echo "<th>".$key."</th>";
			  echo "<th>".$value."</th>";
			  echo "<th><form action='update_jcore_controller.php' method='post'>
	            			<input type='hidden' name='update_activity_status_terminate' value='true'>
	            			<input type='hidden' name='pcm_scenarioID' value='".$_COOKIE['JEngine_ScenarioID']."'>
	            			<input type='hidden' name='pcm_scenarioinstances' value='".$_COOKIE['JEngine_ScenarioInstanceID']."'>
	            			<input type='hidden' name='pcm_activity' value='".$key."'>
	          				<input type='submit' class='btn btn-link' value='terminate'>
						</form></th>";
		      echo "</tr>";
	}
	echo "</table>
		  <br><hr>";

    $terminated_activities = GetActivities($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID'], "terminated");
    
    echo "<h4>terminated Activities</h4>
    		<table>
    			<tr>
    				<th> ID</th>
    				<th> Label</th>
    			</tr>
    			<tr>";

    $amount_of_terminated_activities = count($terminated_activities["ids"]);
    for ($i = 0; $i < $amount_of_terminated_activities; $i++) {
    	 $key = $terminated_activities["ids"][$i];
    	 $value = $terminated_activities["label"][$key];
    	 if($key == "{" || $value == "{") {
    	 	continue;
    	 }
    //foreach($terminated_activities["label"] as $key => $value) {
    	      echo "<tr>";
			  echo "<th>".$key."</th>";
			  echo "<th>".$value."</th>";
		      echo "</tr>";
	}
	echo "</table><br><br>";

	$dataobjects = GetAllDataobject($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID']);

    echo "<h4>Dataobject</h4>
    		<table>
    			<tr>
    				<th> ID</th>
    				<th> States</th>
    				<th> Label</th>
    			</tr>
    			<tr>";

    $amount_of_dataobjects = count($dataobjects["ids"]);
    for ($i = 0; $i < $amount_of_dataobjects; $i++) {
    	 $key = $dataobjects["ids"][$i];
    	 $label = $dataobjects["label"][$key];
    	 $states = $dataobjects["states"][$key];
    	 if($key == "{" || $value == "{") {
    	 	continue;
    	 }
    //foreach($terminated_activities["label"] as $key => $value) {
    	      echo "<tr>";
			  echo "<th>".$key."</th>";
			  echo "<th>".$label."</th>";
			  echo "<th>".$states."</th>";
		      echo "</tr>";
	}
	echo "</table>";
} ?>
