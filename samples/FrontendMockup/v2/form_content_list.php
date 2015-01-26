<?php
include 'core/config.php';
include 'core/RESTCalls.php';

if(isset($_COOKIE['JEngine_ScenarioID'])) {

	$PCM_status = "enabled";
	$get_response = GetActivities($_COOKIE['JEngine_ScenarioID'], $_COOKIE['JEngine_ScenarioInstanceID'], $PCM_status);
	
	$response_amount_of_ids = count($get_response['ids']);
	if($response_amount_of_ids == 0){
		die (" no ids provided");
	}
	$response_label = $get_response['label'];

	foreach ($response_label as &$response_label_value) {
		echo"<li>".$response_label_value."</li>";
	}
} else {
	echo "
	<li>Teil kleben</li>
	<li>Teil transportieren</li>
	<li>Teil schweißen</li>
	<li><font color='green'>Teil schrauben</font></li>";
}



