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
  echo "generating user profile successful.<br>";
}

//update Cookie Values in case of POST is set.
if(isset($_POST["ScenarioID"])){
  
   unset($_COOKIE['JEngine_ScenarioID']);
   setcookie("JEngine_ScenarioID", $_POST["ScenarioID"]);
   unset($_COOKIE['JEngine_ScenarioInstanceID']);
   setcookie("JEngine_ScenarioInstanceID", $_POST["ScenarioInstanceID"]);
   unset($_COOKIE['JEngine_ActivityID']);
   setcookie("JEngine_ActivityID", $_POST["ActivityID"]);
   unset($_COOKIE['JEngine_UserID']);
   setcookie("JEngine_UserID", $_POST["UserID"]);

  echo "<br>update successful.<br>";
}


//if cookie is set, do ... else you could automatically generate IDs..
if(isset($_COOKIE['ScenarioID']) && $_COOKIE['JEngine_ScenarioInstanceID'] === "1") {

}

//print out form for editing values
echo"<form action='admin.php' method='post'>
        JEngine_ScenarioID: <input type='text' name='ScenarioID' value='".$_COOKIE['JEngine_ScenarioID']."'><br>
        JEngine_ScenarioInstanceID: <input type='text' name='ScenarioInstanceID' value='".$_COOKIE['JEngine_ScenarioInstanceID']."'><br>
        JEngine_ActivityID: <input type='text' name='ActivityID' value='".$_COOKIE['JEngine_ActivityID']."'><br>
        JEngine_UserID: <input type='text' name='UserID' value='".$_COOKIE['JEngine_UserID']."'><br>
      <input type='submit'>
    </form> ";

