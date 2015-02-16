<?php
include '../core/config.php';
include '../core/RESTCalls.php';

//update Cookie Values in case of POST is set.
if(isset($_POST['update_activity_status'])) {

   $result = PostActivities($_POST['pcm_scenarioID'], $_POST['pcm_scenarioinstances'], $_POST['pcm_activity'], "terminated", "");
   if($result) {
      header("Location: admin.php?l=jcore_controller");
      die();
   } else {
      echo "fatal error in POST";
   }


} elseif(isset($_POST['pcm_scenarioID'])){
  
   unset($_COOKIE['JEngine_ScenarioID']);
   setcookie("JEngine_ScenarioID", $_POST["pcm_scenarioID"], time()+3600, '/', NULL, 0);

   header("Location: admin.php?l=jcore_controller");
   die();

} elseif(isset($_POST['pcm_scenarioinstances'])){

   unset($_COOKIE['JEngine_ScenarioInstanceID']);
   setcookie("JEngine_ScenarioInstanceID", $_POST["pcm_scenarioinstances"], time()+3600, '/', NULL, 0);

   header("Location: admin.php?l=jcore_controller");
   die();

} elseif(isset($_POST['pcm_scenarioID_new_Instance'])){
   
   $newInstanceID = StartNewInstance($_POST["pcm_scenarioID_new_Instance"]);
   setcookie("JEngine_ScenarioInstanceID", $newInstanceID, time()+3600, '/', NULL, 0);

   header("Location: admin.php?l=jcore_controller");
   die();
} else {
   echo "something went wrong..";
}