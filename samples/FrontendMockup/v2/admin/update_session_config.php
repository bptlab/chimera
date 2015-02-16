<?php


//update Cookie Values in case of POST is set.
if(isset($_POST["ScenarioID"])){
  
   unset($_COOKIE['JEngine_ScenarioID']);
   setcookie("JEngine_ScenarioID", $_POST["ScenarioID"], time()+3600, '/', NULL, 0);
   unset($_COOKIE['JEngine_ScenarioInstanceID']);
   setcookie("JEngine_ScenarioInstanceID", $_POST["ScenarioInstanceID"], time()+3600, '/', NULL, 0);
   unset($_COOKIE['JEngine_ActivityID']);
   setcookie("JEngine_ActivityID", $_POST["ActivityID"], time()+3600, '/', NULL, 0);
   unset($_COOKIE['JEngine_UserID']);
   setcookie("JEngine_UserID", $_POST["UserID"], time()+3600, '/', NULL, 0);
   unset($_COOKIE['JEngine_Role']);
   setcookie("JEngine_Role", $_POST["Role"], time()+3600, '/', NULL, 0);

   header("Location: admin.php?l=session_config");
	die();

} else {
	echo "there is an error..";
}