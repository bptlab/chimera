<?php
include '../core/config.php';
include '../core/RESTCalls.php';
include '../core/db_connection.php';



if(isset($_POST['receivermailaddress'])) {
    $sql="UPDATE `emailconfiguration` 
          SET receivermailaddress = '".$_POST['receivermailaddress']."', subject = '".$_POST['subject']."', message = '".$_POST['message']."'
          WHERE id='".$_POST['id']."'";


    $result = mysql_query($sql);
    if (!$result) {
        die('Ungültige Anfrage: ' . mysql_error());
    } 
    header("Location: admin.php");
	die();
} else {
	echo "there is an error..";
}