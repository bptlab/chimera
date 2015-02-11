<?php
include '../core/config.php';
include '../core/RESTCalls.php';

//update Cookie Values in case of POST is set.
if(isset($_POST["ScenarioID"])){
  
   unset($_COOKIE['JEngine_ScenarioID']);
   setcookie("JEngine_ScenarioID", $_POST["pcm_scenario"], time()+3600, '/', NULL, 0);
   header("Location: admin.php?l=jcore_controller");
   die();
}

if(isset($_POST["pcm_scenarioinstances"])){

   unset($_COOKIE['JEngine_ScenarioInstanceID']);
   setcookie("JEngine_ScenarioInstanceID", $_POST["pcm_scenarioinstances"], time()+3600, '/', NULL, 0);
   header("Location: admin.php?l=jcore_controller");
   die();
}

if(isset($_POST["pcm_scenarioID"])){
   
   $newInstanceID = StartNewInstance($_POST["pcm_scenarioID"]);
   setcookie("JEngine_ScenarioInstanceID", $newInstanceID, time()+3600, '/', NULL, 0);
   header("Location: admin.php?l=jcore_controller");
   die();
}


  

} else {
	echo "there is an error..";
}