<?php
include 'core/config.php';
include 'core/RESTCalls.php';

if(isset($_POST["form_token"])){

	$PCM_comment = $_POST["comment"];
	$PCM_status = "terminate";
	$PCM_ActivityLabel = $_POST["activitylabel"];
	$PCM_ActivityID = $_POST["activityID"];


	$request = PostActivities($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID'], $PCM_ActivityID, $PCM_status, $PCM_comment);
	if(!$request){
		echo "error with the Post request";
	}
} else {
	echo "error with form input";
}

header("Location: ../form.php");
die();