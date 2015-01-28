<?php
$debug = false;

###################################################
#
#	Functions for JEngine Calls
function GetActivities($PCM_Scenario, $PCM_Fragment, $PCM_status) {
	global $JEngine_Server_URL, $JCore_REST_Interface, $debug;
	$URL = $JEngine_Server_URL."/".$JCore_REST_Interface."/".$PCM_Scenario."/".$PCM_Fragment."/".$PCM_status;
	$get_json = file_get_contents($URL);
	if($get_json === "{empty}") {
		return $get_json;
	} else {
		$get_response_as_array = json_decode($get_json,true);
		if(empty($get_response_as_array)){
	                die("ERROR: decoding within GetActivities failed");
	    } elseif(strpos($get_response_as_array, 'Error')){
	    			echo "There is an REST Error..";
	    }

		if($debug){
			error_log("HTTP GET on ".$URL);
			error_log("Returned ".$get_json);
			error_log("Decoded json as ".print_r($get_response_as_array));
		}
		return $get_response_as_array;
	}
}

function GetActivitiesLabelByID($PCM_ActivityID) {
	global $JEngine_Server_URL, $JCore_REST_Interface, $debug;
	$URL = $JEngine_Server_URL."/".$JCore_REST_Interface."/ActivityID/".$PCM_ActivityID;
	$get_json = file_get_contents($URL);
	if($get_json === "{empty}") {
		return $get_json;
	} else {
		$get_response_as_array = json_decode($get_json,true);
		if(empty($get_response_as_array)){
	                die("ERROR: decoding within GetActivitiesLabelByID failed");
	    } elseif(strpos($get_response_as_array, 'Error')){
	    			echo "There is an REST Error..";
	    }

		if($debug){
			error_log("HTTP GET on ".$URL);
			error_log("Returned ".$get_json);
			error_log("Decoded json as ".print_r($get_response_as_array));
		}
		return $get_response_as_array;
	}
}

function ShowScenarios() {
	global $JEngine_Server_URL, $JCore_REST_Interface, $debug;
	$URL = $JEngine_Server_URL."/".$JCore_REST_Interface."/Show";
	$get_json = file_get_contents($URL);
	$get_response_as_array = json_decode($get_json,true);
	if(!$get_response_as_array){
                die("ERROR: decoding within ShowScenarios failed");
    } elseif(strpos($get_response_as_array, 'Error')){
    			echo "There is an REST Error..";
    }
	if($debug){
		error_log("HTTP GET on ".$URL);
		error_log("Returned ".$get_json);
		error_log("Decoded json as ".var_dump($get_response_as_array));
	}
	return $get_response_as_array;
}

function ShowScenarioInstances($PCM_Scenario) {
	global $JEngine_Server_URL, $JCore_REST_Interface, $debug;
	$URL = $JEngine_Server_URL."/".$JCore_REST_Interface."/Instances/".$PCM_Scenario;
	$get_json = file_get_contents($URL);
	$get_response_as_array = json_decode($get_json,true);
	if(!$get_response_as_array){
                die("ERROR: decoding within ShowScenarioInstances failed");
    } elseif(strpos($get_response_as_array, 'Error')){
    			echo "There is an REST Error..";
    }
    if($debug){
		error_log("HTTP GET on ".$URL);
		error_log("Returned ".$get_json);
		error_log("Decoded json as ".print_r($get_response_as_array));
	}
	return $get_response_as_array;
}

function PostActivities($PCM_Scenario, $PCM_Fragment, $PCM_Activity, $PCM_status, $PCM_comment) {
	global $JEngine_Server_URL, $JCore_REST_Interface, $debug;
	$URL = $JEngine_Server_URL."/".$JCore_REST_Interface."/".$PCM_Scenario."/".$PCM_Fragment."/".$PCM_Activity."/".$PCM_status."/".$PCM_comment;
	//$data = array('key1' => 'value1', 'key2' => 'value2');
	$data = array();
	$result = PostWrapper($URL, $data);
	if($debug){
		error_log("HTTP GET on ".$URL);
		error_log("Returned ".$result);
	}
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
