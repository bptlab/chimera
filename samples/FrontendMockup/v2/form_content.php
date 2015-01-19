<?php

$PCM_Scenario = "1";
$PCM_Fragment = "1";
$PCM_options = "closed";

$get_json = file_get_contents($JEngine_Server_URL."/".$JCore_REST_Interface."/".$PCM_Scenario."/".$PCM_Fragment."/".$PCM_options);

//$get_json = '{"ids":[2,4],"label":{"2":"Essen kochen","4":"Zutaten kaufen"}}';
$get_response = json_decode($get_json,true);

echo "get_json: ".$get_json;
echo $get_response['ids'];
print_r($get_response['label']);
echo "get_response: "; var_dump($get_response);

print_r(json_last_error());


?>

<li>Teil kleben</li>
<li>Teil transportieren</li>
<li>Teil schweißen</li>
<li><font color="green">Teil schrauben</font></li>

