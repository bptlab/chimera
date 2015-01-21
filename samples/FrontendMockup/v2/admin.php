<?php

if(isset($_COOKIE['JEngine_ScenarioInstanceID']) && $_COOKIE['JEngine_ScenarioInstanceID'] === "1") {

}

echo"<html><body>
     <form action='admin.php' method='post'>
        JEngine_ScenarioID: <input type='text' name='ScenarioID' value='".$_COOKIE[JEngine_ScenarioID]."'><br>
        JEngine_ScenarioInstanceID: <input type='text' name='ScenarioInstanceID' value='".$_COOKIE[JEngine_ScenarioInstanceID]."'><br>
        JEngine_ActivityID: <input type='text' name='ActivityID' value='".$_COOKIE[JEngine_ActivityID]."'><br>
        JEngine_UserID: <input type='text' name='UserID' value='".$_COOKIE[JEngine_UserID]."'><br>
      <input type='submit'>
    </form>
    </body></html> ";

if(isset($_POST["ScenarioID"])){
  setcookie("JEngine_ScenarioID", $_POST["ScenarioID"]);
  setcookie("JEngine_ScenarioInstanceID", $_POST["ScenarioInstanceID"]);
  setcookie("JEngine_ActivityID", $_POST["ActivityID"]);
  setcookie("JEngine_UserID", $_POST["UserID"]);
  echo "update successful";
}