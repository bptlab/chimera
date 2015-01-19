<?php

$PCM_Scenario = "";
$PCM_Fragment = "";

$get_json = file_get_contents($JEngine_Server_URL."/".$JCore_REST_Interface."/".$PCM_Scenario."/".$PCM_Fragment);
$get_response = json_decode($get_json,true);

//echo  $get_response['John'][status];
?>

                        <li>Teil kleben</li>
                        <li>Teil transportieren</li>
                        <li>Teil schweißen</li>
                        <li><font color="green">Teil schrauben</font></li>

