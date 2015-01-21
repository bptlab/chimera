<?php

###################################################
#
#	JEngine Server URL
$JEngine_Server_URL = "http://172.16.64.113:8080" ;

###################################################
#
#	REST Interface of the JEngine
$JCore_REST_Interface = "JEngine/Scenario" ;


###################################################
#
#	Functions for JEngine Calls
function GetActivities($PCM_Scenario, $PCM_Fragment, $PCM_status) {
	$get_json = file_get_contents($JEngine_Server_URL."/".$JCore_REST_Interface."/".$PCM_Scenario."/".$PCM_Fragment."/".$PCM_status);
	$get_response_as_array = json_decode($get_json,true);
	return $get_respone_as_array;
}

function ShowScenarios() {
	$get_json = file_get_contents($JEngine_Server_URL."/".$JCore_REST_Interface."/Show");
	$get_response_as_array = json_decode($get_json,true);
	return $get_respone_as_array['ids'];
}

function ShowScenarioInstances($PCM_Scenario) {
	$get_json = file_get_contents($JEngine_Server_URL."/".$JCore_REST_Interface."/Instances/".$PCM_Scenario);
	$get_response_as_array = json_decode($get_json,true);
	return $get_respone_as_array['ids'];
}

$url = 'http://server.com/path';
$data = array('key1' => 'value1', 'key2' => 'value2');

// use key 'http' even if you send the request to https://...
$options = array(
    'http' => array(
        'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
        'method'  => 'POST',
        'content' => http_build_query($data),
    ),
);
$context  = stream_context_create($options);
$result = file_get_contents($url, false, $context);


function PostActivities($PCM_Scenario, $PCM_Fragment, $PCM_Activity, $PCM_status, $PCM_comment) {
	$get_json = file_get_contents($JEngine_Server_URL."/".$JCore_REST_Interface."/".$PCM_Scenario."/".$PCM_Fragment."/".$PCM_Activity."/".$PCM_status."/".$PCM_comment);
	$get_response_as_array = json_decode($get_json,true);
	return $get_respone_as_array;
}
