<?php
include '../core/config.php';
include '../core/RESTCalls.php';

//Updating Status to terminate for an pcm_activity
if(isset($_POST['update_activity_status_terminate'])) {

   $result = PostActivities($_POST['pcm_scenarioID'], $_POST['pcm_scenarioinstances'], $_POST['pcm_activity'], "terminate", "");
   if($result) {
      header("Location: admin.php?l=jcore_controller");
      die();
   } else {
      echo "fatal error in POST";
   }

//Updating Status to begin for an pcm_activity
} elseif(isset($_POST['update_activity_status_begin'])) {

   $result = PostActivities($_POST['pcm_scenarioID'], $_POST['pcm_scenarioinstances'], $_POST['pcm_activity'], "begin", "");
   if($result) {
      header("Location: admin.php?l=jcore_controller");
      die();
   } else {
      echo "fatal error in POST";
   }

//update JEngine_ScenarioID Cookie
} elseif(isset($_POST['pcm_scenarioID'])){
  
   unset($_COOKIE['JEngine_ScenarioID']);
   setcookie("JEngine_ScenarioID", $_POST["pcm_scenarioID"], time()+3600, '/', NULL, 0);

   header("Location: admin.php?l=jcore_controller");
   die();

// update JEngine_ScenarioInstanceID
} elseif(isset($_POST['pcm_scenarioinstances'])){

   unset($_COOKIE['JEngine_ScenarioInstanceID']);
   setcookie("JEngine_ScenarioInstanceID", $_POST["pcm_scenarioinstances"], time()+3600, '/', NULL, 0);

   header("Location: admin.php?l=jcore_controller");
   die();

//create new Instance for a provided ScenarioID
} elseif(isset($_POST['pcm_scenarioID_new_Instance'])){
   
   $newInstanceID = StartNewInstance($_POST["pcm_scenarioID_new_Instance"]);
   setcookie("JEngine_ScenarioInstanceID", $newInstanceID, time()+3600, '/', NULL, 0);

   header("Location: admin.php?l=jcore_controller");
   die();

//reseting JEngine_ScenarioID Cookie
} elseif(isset($_POST['reset_scenarioID'])){
   
   unset($_COOKIE['JEngine_ScenarioID']);

   header("Location: admin.php?l=jcore_controller");
   die();

//reseting JEngine_ScenarioInstanceID Cookie
} elseif(isset($_POST['reset_scenarioinstanceID'])){
   
   unset($_COOKIE['JEngine_ScenarioInstanceID']);

   header("Location: admin.php?l=jcore_controller");
   die();

} else {
   echo "something went wrong..";
}