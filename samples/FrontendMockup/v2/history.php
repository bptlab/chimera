<?php include 'header.php'; ?>
      
      <div class="dashboard-wrapper">
        <div class="main-container">
          <div class="navbar hidden-desktop">
            <div class="navbar-inner">
              <div class="container">
                <a data-target=".navbar-responsive-collapse" data-toggle="collapse" class="btn btn-navbar">
                  <span class="icon-bar">
                  </span>
                  <span class="icon-bar">
                  </span>
                  <span class="icon-bar">
                  </span>
                </a>
                <div class="nav-collapse collapse navbar-responsive-collapse">
                  <ul class="nav">
                    <li>
                      <a href="index.html">Dashboard</a>
                    </li>
                    <li>
                      <a href="forms.html">Forms</a>
                    </li>
                    <li>
                      <a href="graphs.html">Charts</a>
                    </li>
                    <li>
                      <a href="ui-elements.html">UI Elements</a>
                    </li>
                    <li>
                      <a href="icons.html">Icons</a>
                    </li>
                    <li>
                      <a href="tables.html">Tables</a>
                    </li>
                    <li>
                      <a href="media.html">Gallery</a>
                    </li>
                    <li>
                      <a href="typography.html">Typography</a>
                    </li>
                    <li>
                      <a href="edit-profile.html">Edit Profile</a>
                    </li>
                    <li>
                      <a href="calendar.html">Calendar</a>
                    </li>
                    <li>
                      <a href="invoice.html">Invoice</a>
                    </li>
                    <li>
                      <a href="login.html">Login</a>
                    </li>
                    <li>
                      <a href="error.html">Error</a>
                    </li>
                    
                    <li>
                      <a href="help.html">Help</a>
                    </li>
                  </ul>
                </div>
                <!-- /.nav-collapse -->
              </div>
            </div>
            <!-- /navbar-inner -->
          </div>

          <div class="row-fluid">
            <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe14a;"></span> Gateways
                  </div>
                </div>
                <div class="widget-body">
                  <table class="table table-condensed table-striped table-bordered table-hover no-margin">
                    <thead>
                      <tr>
                        <th style="width:40%">
                          Gateway
                        </th>
                        <th style="width:20%" class="hidden-phone">
                          Decision
                        </th>
                        <th style="width:10%" class="hidden-phone">
                          Option
                        </th>
                        <th style="width:15%" class="hidden-phone">
                          Status
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>
                          <span class="name">
                            Invoice recieved?
                          </span>
                        </td>
                        <td class="hidden-phone">
                          yes/no
                        </td>
                        <td class="hidden-phone">
                          1
                        </td>
                        <td class="hidden-phone">
                          <span class="badge badge-info">
                            Processing
                          </span>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <span class="name">
                            Message send?
                          </span>
                        </td>
                        <td class="hidden-phone">
                           yes/no
                        </td>
                        <td class="hidden-phone">
                          2
                        </td>
                        <td class="hidden-phone">
                          <span class="badge badge-success">
                            Sent
                          </span>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <span class="name">
                            Record fixed?
                          </span>
                        </td>
                        <td class="hidden-phone">
                           yes/no/later
                        </td>
                        <td class="hidden-phone">
                          1
                        </td>
                        <td class="hidden-phone">
                          <span class="badge badge-important">
                            Cancelled
                          </span>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <span class="name">
                            Designer needed?
                          </span>
                        </td>
                        <td class="hidden-phone">
                         yes/no/maybe/just in case
                        </td>
                        <td class="hidden-phone">
                          4
                        </td>
                        <td class="hidden-phone">
                          <span class="badge badge-info">
                            Processing
                          </span>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <span class="name">
                            Plan Meeting
                          </span>
                        </td>
                        <td class="hidden-phone">
                          Yes/no
                        </td>
                        <td class="hidden-phone">
                          1
                        </td>
                        <td class="hidden-phone">
                          <span class="badge badge-success">
                            Sent
                          </span>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>

   

          <div class="row-fluid">
            <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe040;"></span> Human Tasks ongoing
                  </div>
                  <div class="tools pull-right">
                    <div class="btn-group">
                      <a class="btn btn-small">
                        <i class="icon-share-alt" data-original-title="Share"></i>
                      </a>
                      <a class="btn btn-small">
                        <i class="icon-exclamation-sign" data-original-title="Report">
                        </i>
                      </a>
                      <a class="btn btn-small">
                        <i class="icon-trash" data-original-title="Delete"></i>
                      </a>
                    </div>
                    <div class="btn-group">
                      <a class="btn btn-small">
                        <i class="icon-folder-close" data-original-title="Move to"></i>
                      </a>
                    </div>
                    <div class="btn-group">
                      <a class="btn btn-small">
                        <i class="icon-chevron-left" data-original-title="Prev"></i>
                      </a>
                      <a class="btn btn-small btn-info">
                        <i class="icon-chevron-right icon-white" data-original-title="Next"></i>
                      </a>
                    </div>
                  </div>
                </div>
                <div class="widget-body">
                  <div class="mail">
                    <table class="table table-condensed table-striped table-bordered table-hover no-margin">
                      <thead>
                        <tr>
                          <th style="width:7%">
                            <input type="checkbox" class="no-margin" />
                          </th>
                          <th style="width:17%">
                            Agent
                          </th>
                          <th style="width:50%" class="hidden-phone">
                            Task Name
                          </th>
                          <th style="width:12%" class="hidden-phone">
                            Labels
                          </th>
                          <th style="width:12%" class="hidden-phone">
                           Due Date
                          </th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-empty"></i></a>
                          </td>
                          <td>
                            Srinu Baswa
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              Compass, Sass
                            </strong>
                            <small class="info-fade">
                              Eyeball Methodologies
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge-important">
                              IMP
                            </span>
                          </td>
                          <td class="hidden-phone">
                            12:30AM
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-full"></i></a>
                          </td>
                          <td>
                            Mahendra Dhoni
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              Compass, Sass
                            </strong>
                            <small class="info-fade">
                              Methodologies eyeball
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge-info">
                              Read
                            </span>
                          </td>
                          <td class="hidden-phone">
                            16-02-2013
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-empty"></i></a>
                          </td>
                          <td>
                            Michel Clar
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              Senior Developer
                            </strong>
                            <small class="info-fade">
                              Platforms web-enabled cultivat
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge-success">
                              New
                            </span>
                          </td>
                          <td class="hidden-phone">
                            Valentine
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-full"></i></a>
                          </td>
                          <td>
                            Rahul Dravid
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              Bitbucket
                            </strong>
                            
                            <small class="info-fade">
                              Technologies content deploy ROI
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge-important">
                              Imp
                            </span>
                          </td>
                          <td class="hidden-phone">
                            Yesterday
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-full"></i></a>
                          </td>
                          <td>
                            Anthony Michell
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              Verify your email
                            </strong>                            
                            <small class="info-fade">
                              Less schemas seamless band
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge-info">
                              Read
                            </span>
                          </td>
                          <td class="hidden-phone">
                            18-02-2013
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-empty"></i></a>
                          </td>
                          <td>
                            Srinu Baswa
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              Statement for December 2012
                            </strong>
                            <small class="info-fade">
                              Models seize 
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge badge-success">
                              New
                            </span>
                          </td>
                          <td class="hidden-phone">
                            09-02-2013
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-full"></i></a>
                          </td>
                          <td>
                            Crazy Singh
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              You're In!
                            </strong>
                            <small class="info-fade">
                              Tagclouds endwidth; morph; distr
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge-warning">
                              Read
                            </span>
                          </td>
                          <td class="hidden-phone">
                            21-01-2013
                          </td>
                        </tr>
                        <tr>
                          <td>
                            <input type="checkbox" class="no-margin" />
                            <a href="#"><i class="icon-star-empty"></i></a>
                          </td>
                          <td>
                            Sri Ram Raju
                          </td>
                          <td class="hidden-phone">
                            <strong>
                              Support
                            </strong>
                            <small class="info-fade">
                              Distributed incentivize enabl
                            </small>
                          </td>
                          <td class="hidden-phone">
                            <span class="badge badge-success">
                              New
                            </span>
                          </td>
                          <td class="hidden-phone">
                            19-01-2013
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="row-fluid">
            <div class="span5">
              <div class="widget no-margin">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe14a;"></span> Event Listeners
                  </div>
                </div>
                <div class="widget-body">
                  <table class="table table-striped table-bordered no-margin">
                    <thead>
                      <tr>
                        <th style="width:20%">
                          Sl. No.
                        </th>
                        <th style="width:40%">
                         Event  Name
                        </th>
                        <th style="width:20%" class="hidden-phone">
                          Status
                        </th>
                        <th style="width:20%" class="hidden-phone">
                          Actions
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>
                          1
                        </td>
                        <td>
                          JIRA
                        </td>
                        <td class="hidden-phone">
                          Active
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          2
                        </td>
                        <td>
                          Bosch Robot
                        </td>
                        <td class="hidden-phone">
                          Active
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          3
                        </td>
                        <td>
                          emergency button 
                        </td>
                        <td class="hidden-phone">
                          Inactive
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          4
                        </td>
                        <td>
                           	fire department
                        </td>
                        <td class="hidden-phone">
                          Active
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          5
                        </td>
                        <td>
                          Gajju (CEO)
                        </td>
                        <td class="hidden-phone">
                          Inactive
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
            
            
            <div class="span7">
              <div class="widget no-margin">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe14a;"></span> Data Objects
                  </div>
                </div>
                <div class="widget-body">
                  <table class="table table-striped table-bordered no-margin">
                    <thead>
                      <tr>
                        <th style="width:20%">
                          Sl. No.
                        </th>
                        <th style="width:40%">
                          Data Object Name
                        </th>
                        <th style="width:20%" class="hidden-phone">
                          Value
                        </th>
                        <th style="width:20%" class="hidden-phone">
                          Actions
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr>
                        <td>
                          1
                        </td>
                        <td>
                          Invoice
                        </td>
                        <td class="hidden-phone">
                          Status=Active
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          2
                        </td>
                        <td>
                          Messagebus
                        </td>
                        <td class="hidden-phone">
                          Status=Active
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          3
                        </td>
                        <td>
                          Internet Connection
                        </td>
                        <td class="hidden-phone">
                          status=Inactive
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          4
                        </td>
                        <td>
                          Employee Satisfaction
                        </td>
                        <td class="hidden-phone">
                          6
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td>
                          5
                        </td>
                        <td>
                          Gajju
                        </td>
                        <td class="hidden-phone">
                          EmployeeStatus=Inactive
                        </td>
                        <td class="hidden-phone">
                          <a class="delete-row" data-original-title="Delete" href="#">
                            <i class="icon-trash">
                            </i>
                          </a>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
        
      </div>
      <!--/.fluid-container-->
    </div>

    <script src="js/jquery.min.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/jquery-ui-1.8.23.custom.min.js"></script>


    <script src="js/jquery.dataTables.js"></script>
    
    <script type="text/javascript">

      //Tooltip
      $('a').tooltip('hide');

      //Data Tables
      $(document).ready(function () {
        $('#data-table').dataTable({
          "sPaginationType": "full_numbers"
        });
      });

      jQuery('.delete-row').click(function () {
        var conf = confirm('Continue delete?');
        if (conf) jQuery(this).parents('tr').fadeOut(function () {
          jQuery(this).remove();
        });
          return false;
        });

      //Main menu navigation
      
      $('.submenu > a').click(function(e){
        e.preventDefault();
        var submenu = $(this).siblings('ul');
        var li = $(this).parents('li');
        var submenus = $('#mainnav li.submenu ul');
        var submenus_parents = $('#mainnav li.submenu');
        if(li.hasClass('open'))
        {
          if(($(window).width() > 768) || ($(window).width() < 479)) {
            submenu.slideUp();
          } else {
            submenu.fadeOut(250);
          }
          li.removeClass('open');
        } else 
        {
          if(($(window).width() > 768) || ($(window).width() < 479)) {
            submenus.slideUp();     
            submenu.slideDown();
          } else {
            submenus.fadeOut(250);      
            submenu.fadeIn(250);
          }
          submenus_parents.removeClass('open');   
          li.addClass('open');  
        }
      });
      
      var ul = $('#mainnav > ul');
      
      $('#mainnav > a').click(function(e)
      {
        e.preventDefault();
        var mainnav = $('#mainnav');
        if(mainnav.hasClass('open'))
        {
          mainnav.removeClass('open');
          ul.slideUp(250);
        } else 
        {
          mainnav.addClass('open');
          ul.slideDown(250);
        }
      });
      </script>
      
  </body>
</html>