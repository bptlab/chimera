<?php
include 'core/config.php';
include 'core/RESTCalls.php';

$automatic = true;

$PCM_Scenario = "1";
$PCM_Fragment = "60";
$PCM_status = "enabled";
$PCM_comment = "comment";

$get_response = GetActivities($PCM_Scenario, $PCM_Fragment, $PCM_status);

$response_amount_of_ids = count($get_response['ids']);
$response_label = $get_response['label'];

if($response_amount_of_ids == 0){
	die (" no ids provided");
}


if($automatic){
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

