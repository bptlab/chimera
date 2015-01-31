<?php


//update Cookie Values in case of POST is set.
if(isset($_POST["ScenarioID"])){
  
   unset($_COOKIE['JEngine_ScenarioID']);
   setcookie("JEngine_ScenarioID", $_POST["ScenarioID"]);
   unset($_COOKIE['JEngine_ScenarioInstanceID']);
   setcookie("JEngine_ScenarioInstanceID", $_POST["ScenarioInstanceID"]);
   unset($_COOKIE['JEngine_ActivityID']);
   setcookie("JEngine_ActivityID", $_POST["ActivityID"], NULL, 0);
   unset($_COOKIE['JEngine_UserID']);
   setcookie("JEngine_UserID", $_POST["UserID"]);
   unset($_COOKIE['JEngine_Role']);
   setcookie("JEngine_Role", $_POST["Role"]);

   header("Location: admin.php");
	die();
   
} else {
	echo "there is an error..";
}