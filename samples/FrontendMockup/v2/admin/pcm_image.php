<?php
include '../core/config.php';
include '../core/RESTCalls.php';

$scenario_value = $_GET['id'];
    $image= GetScenarioImage($scenario_value);
    header('Content-type: image/png');
    header('Content-Length: ' . filesize($image));
    echo $image;
