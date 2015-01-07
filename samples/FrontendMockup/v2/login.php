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
                    <span class="fs1" aria-hidden="true" data-icon="&#xe088;"></span> Login
                  </div>
                </div>
                <div class="widget-body">
                  <div class="row-fluid">
                    <div class="span4 offset4">
                      <div class="signup">
                        <form action="#" class="signup-wrapper" method="post" />
                          <div class="header">
                            <h2>Login</h2>
                            <p>Fill out the form below to login to your control panel.</p>
                          </div>
                          <div class="content">
                            <input class="input input-block-level" placeholder="Email" required type="email" value="" />
                            <input class="input input-block-level" placeholder="Password" required type="password" />
                          </div>
                          <div class="actions">
                            <input class="btn btn-info btn-large pull-right" type="submit" value="Login" />
                            <span class="checkbox-wrapper">
                              <a href="#" class="pull-left">Forgot Password</a>
                            </span>
                            <div class="clearfix"></div>
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row-fluid">
            <div class="span12">
              <div class="widget no-margin">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe088;"></span> Sign Up
                  </div>
                </div>
                <div class="widget-body">
                  <div class="row-fluid">
                    <div class="span4 offset4">
                      <div class="signup">
                        <form action="#" class="signup-wrapper" method="post" />
                          <div class="header">
                            <h2>Sign Up</h2>
                            <p>Fill out the form below to login to your control panel.</p>
                          </div>
                          <div class="content">
                            <input class="input input-block-level" placeholder="Name" required type="text" value="" />
                            <input class="input input-block-level" placeholder="Email" required type="email" value="" />
                            <input class="input input-block-level" placeholder="Password" required type="password" />
                            <input class="input input-block-level" placeholder="Company Name" required type="text" />
                          </div>
                          <div class="actions">
                            <input class="btn btn-info btn-large pull-right" name="" type="submit" value="Sign Up" />
                            <span class="checkbox-wrapper">
                              <input class="checkbox" id="form-terms" type="checkbox" checked="" />
                              <label class="checkbox-label" for="form-terms"></label>
                              <span class="label-text">Remember</span>
                            </span>
                            <div class="clearfix"></div>
                          </div>
                        </form>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
        </div>
        
      </div>
      
      <!--/.fluid-container-->
    </div>
    <footer>
      <p>
        &copy; BP2014W1 2013
      </p>
    </footer>
    
    <script src="js/jquery.min.js">
    </script>
    <script src="js/bootstrap.js">
    </script>
    
    
    <script type="text/javascript">
      //Tooltip
      $('a').tooltip('hide');

      //Popover
      $('.popover-pop').popover('hide');

      //Collapse
      $('#myCollapsible').collapse({
        toggle: false
      })

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