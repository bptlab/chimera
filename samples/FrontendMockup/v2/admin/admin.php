<?php
include '../core/config.php';
include '../core/RESTCalls.php';

$link_content = $_GET['l'];

?>
<html>
<head>
<title> BP2014W1 - Admin Dashboard</title>
    <meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport' />
    <!--[if lt IE 9]>
    <script src='assets/javascripts/html5shiv.js' type='text/javascript'></script>
    <![endif]-->
    <link href='assets/stylesheets/bootstrap/bootstrap.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/bootstrap/bootstrap-responsive.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/jquery_ui/jquery-ui-1.10.0.custom.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/jquery_ui/jquery.ui.1.10.0.ie.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/bootstrap_switch/bootstrap-switch.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/xeditable/bootstrap-editable.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/common/bootstrap-wysihtml5.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/common/bootstrap-wysihtml5.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/select2/select2.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/mention/mention.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/tabdrop/tabdrop.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/jgrowl/jquery.jgrowl.min.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/datatables/bootstrap-datatable.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/dynatree/ui.dynatree.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/bootstrap_colorpicker/bootstrap-colorpicker.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/bootstrap_datetimepicker/bootstrap-datetimepicker.min.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/bootstrap_daterangepicker/bootstrap-daterangepicker.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/flags/flags.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/slider_nav/slidernav.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/fuelux/wizard.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/light-theme.css' id='color-settings-body-color' media='all' rel='stylesheet' type='text/css' />

    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <!-- remove
    <link href='assets/stylesheets/plugins/jquery_fileupload/jquery.fileupload-ui.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/plugins/fullcalendar/fullcalendar.css' media='all' rel='stylesheet' type='text/css' />
    <link href='assets/stylesheets/demo.css' media='all' rel='stylesheet' type='text/css' />
    -->

    <script src="http://code.jquery.com/jquery-1.11.0.min.js"></script>
    <?php
    if(isset($link_content)){
            ?>  
                <script type="text/javascript">
                    $(document).ready(function(){    
                        var a = $(this).attr('id');
                        $.post('admin_<?php echo $link_content;?>.php', {
                        }, function(response){
                            $('#container').fadeOut();
                            $('#container').html(unescape(response));
                            $('#container').fadeIn();
                            setTimeout("finishAjax('container', '"+escape(response)+"')", 400);
                        });  
                    });    
                    function finishAjax(id, response){
                      $('#'+id).html(unescape(response));
                      $('#'+id).fadeIn();
                    } 
                    </script>
     <?php
    }
    ?>
</head>
<body class='contrast-green'>
<!-- header -->
<header>
    <div class='navbar'>
        <div class='navbar-inner'>
            <div class='container-fluid'>
                <a class='brand' href='index.html'>
                    <a href="admin.php"><img src="img/hpi.png" alt="Logo" align="left"  height="40px" width="40px"></a>
                </a>
                <a class='toggle-nav btn pull-left' href='#'>
                    <i class='icon-reorder'></i>
                </a>
            </div>
        </div>
    </div>
</header>
<!-- Menu -->
<div id='wrapper'>
<div id='main-nav-bg'></div>
<nav class='' id='main-nav'>
<div class='navigation'>

<ul class='nav nav-stacked'>
<li class='active'>
    <a href="admin.php?l=db_lookup">
        <i class='icon-globe'></i>
        <span>database_dump</span>
    </a>
</li>
<li class='active'>
    <a href="admin.php?l=mail_config">
        <i class='icon-globe'></i>
        <span>mail_config</span>
    </a>
</li>
<li class='active'>
    <a href="admin.php?l=session_config">
        <i class='icon-globe'></i>
        <span>session_config</span>
    </a>
</li>
<li class='active'>
    <a href="admin.php?l=jcomparser">
        <i class='icon-globe'></i>
        <span>jcomparser</span>
    </a>
</li>
</ul>
</div>
</nav>
<!-- Content: top -->
<section id='content'>
<div class='container-fluid'>
<div class='row-fluid' id='content-wrapper'>
<div class='span12'>
<div class='page-header'>
    <h1 class='pull-left'>
        <i class='icon-dashboard'></i>
        <span>Admin dashboard</span>
    </h1>
</div>



<?php

if(isset($link_content)){
?>
    <div id="container">
        <br><br><br>
        <i>waiting for HANA..</i><br><br><img src="../img/loading-green.gif"><br><br><br><br><br><br><br>
    </div>
<?php 
} else {
  echo "welcome to the admin backend! :) please choose from menu.. ";
}
?>


