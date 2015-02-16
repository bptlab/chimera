<?php
include '../core/config.php';
include '../core/RESTCalls.php';

if(isset($_GET[scenarioID])) {
   if(is_numeric($_GET[scenarioID])){
      $check = PostScenarios($_GET[scenarioID]);
      if($check){
         header( 'Location: admin.php?l=jcomparser' ) ;
         die;
      } else {
         echo "ERROR: something went wrong with the POST";
         die;  
      }
   } else {
      echo "ERROR: scenarioID wasnt numberic... ".$_GET[scenarioID];
      die;
   }
} else {
   echo "ERROR: scenarioID not set.. ";
}


?>


