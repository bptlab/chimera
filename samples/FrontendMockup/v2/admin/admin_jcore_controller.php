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

        <strong>Note!</strong> Please read the comments carefully. <a href="#" data-toggle="modal" data-target="#generalModal">Change me</a>

    </div>


<!-- Button trigger modal -->
<button type="button" class="btn btn-primary btn-lg" data-toggle="modal" data-target="#generalModal">
  Launch demo modal
</button>
<?php } ?>


<!-- Modal: general informations -->
<div class="modal fade" id="generalModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Modal title</h4>
      </div>
      <div class="modal-body">
        Current Scenario ID: <?php echo $_COOKIE['JEngine_ScenarioID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioModal">Change me</a> <br>
        Current Instance ID: <?php echo $_COOKIE['JEngine_ScenarioInstanceID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioInstanceModal">Change me</a> <br>
        <form action='update_jcore_controller.php' method='post'>
		            <input type='hidden' name='pcm_scenarioID_new_Instance' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
		            <input type='submit' value='create new Instance'>
		</form><br>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal: Scenario informations -->
<div class="modal fade" id="ScenarioModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Modal title</h4>
      </div>
      <div class="modal-body">
        Current Scenario ID: <?php echo $_COOKIE['JEngine_ScenarioID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioModal">Change me</a> <br>
        Current Instance ID: <?php echo $_COOKIE['JEngine_ScenarioInstanceID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioInstanceModal">Change me</a> <br>
        <form action='update_jcore_controller.php' method='post'>
		            <input type='hidden' name='pcm_scenarioID_new_Instance' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
		            <input type='submit' value='create new Instance'>
		</form><br>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>

<!-- Modal: ScenarioInstance informations -->
<div class="modal fade" id="ScenarioInstanceModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
        <h4 class="modal-title" id="myModalLabel">Modal title</h4>
      </div>
      <div class="modal-body">
        Current Scenario ID: <?php echo $_COOKIE['JEngine_ScenarioID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioModal">Change me</a> <br>
        Current Instance ID: <?php echo $_COOKIE['JEngine_ScenarioInstanceID']; ?> <a href="#" data-toggle="modal" data-target="#ScenarioInstanceModal">Change me</a> <br>
        <form action='update_jcore_controller.php' method='post'>
		            <input type='hidden' name='pcm_scenarioID_new_Instance' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
		            <input type='submit' value='create new Instance'>
		</form><br>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
        <button type="button" class="btn btn-primary">Save changes</button>
      </div>
    </div>
  </div>
</div>