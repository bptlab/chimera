<?php
include '../core/config.php';
include '../core/RESTCalls.php';
include '../core/db_connection.php';



$result = mysql_query("SELECT * FROM `emailconfiguration`");

while($row = mysql_fetch_array($result)) {
    $label_row = mysql_query("SELECT label FROM `controlnode` WHERE id= '".$row['controlnode_id']."'");
    $label = mysql_fetch_row($label_row);
    echo "<h4>".$label[0]."</h4>";

    echo "
        <form action='update_mail_config.php' method='post'>
            <input type='hidden' name='id' value='".$row['id']."'><br>
            
            receiver: <input type='text' name='receivermailaddress' value='".$row['receivermailaddress']."'><br>
            subject: <input type='text' name='subject' value='".$row['subject']."'><br>
            message: <textarea type='text' name='message'rows='4' cols='40'>".$row['message']."</textarea><br>
            <input type='submit'>
        </form><br>
    ";


    //print_r($row);
}


