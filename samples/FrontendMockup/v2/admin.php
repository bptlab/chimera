<?php
include 'config.php';

//update Cookie Values in case of POST is set.
if(isset($_POST["ScenarioID"])){
  setcookie("JEngine_ScenarioID", $_POST["ScenarioID"]);
  setcookie("JEngine_ScenarioInstanceID", $_POST["ScenarioInstanceID"]);
  setcookie("JEngine_ActivityID", $_POST["ActivityID"]);
  setcookie("JEngine_UserID", $_POST["UserID"]);
  echo "update successful";
}

//if cookie is set, do ... else you could automatically generate IDs..
if(isset($_COOKIE['JEngine_ScenarioInstanceID']) && $_COOKIE['JEngine_ScenarioInstanceID'] === "1") {

}

//print out form for editing values
echo"<html><body>
     <form action='admin.php' method='post'>
        JEngine_ScenarioID: <input type='text' name='ScenarioID' value='".$_COOKIE[JEngine_ScenarioID]."'><br>
        JEngine_ScenarioInstanceID: <input type='text' name='ScenarioInstanceID' value='".$_COOKIE[JEngine_ScenarioInstanceID]."'><br>
        JEngine_ActivityID: <input type='text' name='ActivityID' value='".$_COOKIE[JEngine_ActivityID]."'><br>
        JEngine_UserID: <input type='text' name='UserID' value='".$_COOKIE[JEngine_UserID]."'><br>
      <input type='submit'>
    </form>
    </body></html> ";





$get_json = file_get_contents($JEngine_Server_URL."/".$JCore_REST_Interface."/Show");

//Exmaple JSON String: '{"ids":[2,4],"label":{"2":"Essen kochen","4":"Zutaten kaufen"}}';
$get_response = json_decode($get_json,true);

$response_amount_of_ids = count($get_response['ids']);
$response_label = $get_response['label'];

if($response_amount_of_ids == 0){
  die (" no ids provided");
}