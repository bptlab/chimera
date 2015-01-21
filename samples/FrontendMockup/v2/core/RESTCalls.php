<?php


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

function PostActivities($PCM_Scenario, $PCM_Fragment, $PCM_Activity, $PCM_status, $PCM_comment) {
	$URL = $JEngine_Server_URL."/".$JCore_REST_Interface."/".$PCM_Scenario."/".$PCM_Fragment."/".$PCM_Activity."/".$PCM_status."/".$PCM_comment;
	//$data = array('key1' => 'value1', 'key2' => 'value2');
	$data = array()
	$result = PostWrapper($URL, $data);
	if($result){
		return true;
	} else {
		return false;
	}

}



###################################################
#
#	Functions for JEngine Calls

function PostWrapper($URL, $data) {
	//$data = array('key1' => 'value1', 'key2' => 'value2');
	$options = array(
	    'http' => array(
	        'header'  => "Content-type: application/x-www-form-urlencoded\r\n",
	        'method'  => 'POST',
	        'content' => http_build_query($data),
	    ),
	);
	$context  = stream_context_create($options);
	$result = file_get_contents($URL, false, $context);
	return $result;
}