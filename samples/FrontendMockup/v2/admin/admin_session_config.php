<?php
include '../core/config.php';
include '../core/RESTCalls.php';




if(!isset($_COOKIE['JEngine_ScenarioID'])) {

  $JEngine_ScenarioInstanceID ="1";
  $JEngine_ActivityID = "1";
  $JEngine_UserID = rand(5, 100);
  $JEngine_ScenarioID = ShowScenarios();
  $JEngine_ScenarioID = $JEngine_ScenarioID["ids"][0];

  setcookie("JEngine_ScenarioID", $JEngine_ScenarioID);
  setcookie("JEngine_ScenarioInstanceID", $JEngine_ScenarioInstanceID);
  setcookie("JEngine_ActivityID", $JEngine_ActivityID);
  setcookie("JEngine_UserID", $JEngine_UserID);
  setcookie("JEngine_Role", $JEngine_Role);
  echo "generating user profile successful.<br>";
}


//if cookie is set, do ... else you could automatically generate IDs..
if(isset($_COOKIE['ScenarioID']) && $_COOKIE['JEngine_ScenarioInstanceID'] === "1") {

}

//print out form for editing values
echo"<form action='update_session_config.php' method='post'>
        JEngine_ScenarioID: <input type='text' name='ScenarioID' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
        JEngine_ScenarioInstanceID: <input type='text' name='ScenarioInstanceID' value='".$_COOKIE['JEngine_ScenarioInstanceID']."'><br>
        JEngine_ActivityID: <input type='text' name='ActivityID' value='".$_COOKIE['JEngine_ActivityID']."'><br>
        JEngine_UserID: <input type='text' name='UserID' value='".$_COOKIE['JEngine_UserID']."'><br>
        JEngine_Role: <input type='text' name='Role' value='".$_COOKIE['JEngine_Role']."'><br>
      <input type='submit'>
    </form> ";

if(!isset($_POST["ScenarioID"])){
}