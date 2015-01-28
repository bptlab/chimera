<?php
include '../core/config.php';
include '../core/RESTCalls.php';

$link_content = $_GET['l'];

?>
<html>
<head>
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
<body>

<a href="admin.php?l=db_lookup">dump database</a> |
<a href="admin.php?l=mail_config">mail_config</a> |
<a href="admin.php?l=session_config">session_config</a> |

<br><br>

<?php


if(!isset($_POST["ScenarioID"])){

if(isset($link_content)){
?>
    <div id="container">
        <br><br><br>
        <i>waiting for HANA..</i><br><br><img src="../img/loading-green.gif"><br><br><br><br><br><br><br>
    </div>
<?php 
  }
} ?>


