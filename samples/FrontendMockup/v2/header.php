<!DOCTYPE html>
<!--[if lt IE 7]>

<html class="lt-ie9 lt-ie8 lt-ie7" lang="en">

<![endif]-->
<!--[if IE 7]>

<html class="lt-ie9 lt-ie8" lang="en">

<![endif]-->
<!--[if IE 8]>

<html class="lt-ie9" lang="en">

<![endif]-->
<!--[if gt IE 8]>
  <!-->

  <html lang="en">
  
  <!--
  <![endif]-->
  <head>
    <meta charset="utf-8" />
    <title>
      BP2014W1
    </title>
    <meta name="author" content="" />
    <meta content="width=device-width, initial-scale=1.0, user-scalable=no" name="viewport" />
    <meta name="description" content="" />
    <meta name="author" content="" />
    <!-- bootstrap css -->
    <script type="text/javascript" src="http://html5shiv.googlecode.com/svn/trunk/html5.js">
    </script>
    <link href="icomoon/style.css" rel="stylesheet" />
    <!--[if lte IE 7]>
    <script src="css/icomoon-font/lte-ie7.js">
    </script>
    <![endif]-->
    <link href="css/main.css" rel="stylesheet" />
    <link href="css/wysiwyg/bootstrap-wysihtml5.css" rel="stylesheet" />
    <link href="css/wysiwyg/wysiwyg-color.css" rel="stylesheet" />
    <link href="css/charts-graphs.css" rel="stylesheet" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

        <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
        <script type="text/javascript">
        $(document).ready(function(){    
                var a = $(this).attr('id');
                $.post("form_content_list.php", {
                }, function(response){
                    $('#form_content_list').fadeOut();
                    $('#form_content_list').html(unescape(response));
                    $('#form_content_list').fadeIn();
                    setTimeout("finishAjax('form_content_list', '"+escape(response)+"')", 400);
                });  
        });    
        function finishAjax(id, response){
          $('#'+id).html(unescape(response));
          $('#'+id).fadeIn();
        } 
        </script>	
        <script type="text/javascript">
        $(document).ready(function(){    
                var a = $(this).attr('id');
                $.post("form_content_form.php", {
                }, function(response){
                    $('#form_content_form').fadeOut();
                    $('#form_content_form').html(unescape(response));
                    $('#form_content_form').fadeIn();
                    setTimeout("finishAjax('form_content_form', '"+escape(response)+"')", 400);
                });  
        });    
        function finishAjax(id, response){
          $('#'+id).html(unescape(response));
          $('#'+id).fadeIn();
        } 
        </script> 
  </head>
  <body>
    <header>
      <a href="index.php" class="logo">
        <img src="img/logo.png" alt="Logo" />
      </a>
      <div class="user-profile">
        <a data-toggle="dropdown" class="dropdown-toggle">
          <img src="img/profile1.png" alt="profile" />
        </a>
        <span class="caret"></span>
        <ul class="dropdown-menu pull-right">
          <li>
            <a href="edit-profile.php#">
              Edit Profile
            </a>
          </li>
          <li>
            <a href="edit-profile.php">
              Account Settings
            </a>
          </li>
          <li>
            <a href="login.php">
              Logout
            </a>
          </li>
        </ul>
      </div>
      <ul class="mini-nav">
        <li>
          <a href="#">
            <div class="fs1" aria-hidden="true" data-icon="&#xe04c;"></div>
            <span class="info-label-green">
              5
            </span>
          </a>
        </li>
      </ul>
    </header>

    <div class="container-fluid">
      <div id="mainnav" class="hidden-phone hidden-tablet">
        <ul style="display: block;">
<?php if(basename($_SERVER['REQUEST_URI']) == 'index.php'){ echo "<li class='active'>"; } else {  echo "<li>"; }  ?>
            <a href="index.php">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe0a0;"></span>
              </div>
              Dashboard
            </a>
          </li>
<?php if(basename($_SERVER['REQUEST_URI']) == 'forms.php'){ echo "<li class='active'>"; } else {  echo "<li>"; }  ?>
            <a href="forms.php">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe0b8;"></span>
              </div>
              Human Tasks
            </a>
          </li>
<?php if(basename($_SERVER['REQUEST_URI']) == 'history.php'){ echo "<li class='active'>"; } else {  echo "<li>"; }  ?>
            <a href="history.php">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe14a;"></span>
              </div>
              Activity Log
            </a>
          </li>
          <li class="submenu">
            <a href="edit-profile.php" class="selected">
              <div class="icon">
                <span class="fs1" aria-hidden="true" data-icon="&#xe0aa;"></span>
              </div>
              Extras
            </a>
            <ul>
              <li>
                <a href="edit-profile.php">Edit Profile</a>
              </li>
              <li>
                <a href="calendar.php">Calendar</a>
              </li>
              <li>
                <a href="login.php">Login</a>
              </li>
              <li>
                <a href="help.php">Help</a>
              </li>
            </ul>
          </li>
        </ul>
      </div>