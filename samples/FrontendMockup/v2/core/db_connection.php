<?php

$servername = "localhost";
$username = "root";
$password = "foi6cixoo0Quah2e";
$db_name = "JEngineV2";

// Create connection
$con=mysql_connect($servername, $username, $password, $db_name);

// Check connection
if (mysql_error()) {
  echo "Failed to connect to MySQL: " . mysql_error();
}

mysql_select_db($db_name);
