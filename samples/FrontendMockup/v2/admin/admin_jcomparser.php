<?php

if(isset($_POST['scenarioID'])) {
   if(is_int($_POST['scenarioID'])){
      $check = PostScenarios($_POST['scenarioID']);
      if($check){
         header( 'Location: admin.php?l=jcomparser' ) ;
         die;
      } else {
         echo "ERROR: something went wrong with the POST";
         die;  
      }
   } else {
      echo "ERROR: scenarioID wasnt an ID... ".$_POST['scenarioID'];
      die;
   }
}


include '../core/config.php';
include '../core/RESTCalls.php';

var_dump(GetAvailableScenarios());

?>

<form action="admin_jcomparser.php">
Scenario ID:<br>
<input type="text" name="scenarioID" value="scenarioID">
<br><br>
<input type="submit" value="Submit">
</form> 

