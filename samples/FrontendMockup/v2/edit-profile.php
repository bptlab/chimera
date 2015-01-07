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
            
            <div class="alert alert-block alert-info fade in">
              <button data-dismiss="alert" class="close" type="button">
                ×
              </button>
              <h4 class="alert-heading">
                Info!
              </h4>
              <p>
                Extras includes - Profile page, Invoice, Login, Sign Up, 404, 500, Help
              </p>
            </div>

            <div class="row-fluid">
              <div class="span12">
                <div class="widget no-margin">
                  <div class="widget-header">
                    <div class="title">
                      <span class="fs1" aria-hidden="true" data-icon="&#xe023;"></span> Edit Profile
                    </div>
                  </div>
                  <div class="widget-body">
                    <div class="container-fluid">
                      
                      <div class="row-fluid">
                        <div class="span3">
                          <div class="thumbnail">
                            <img alt="300x200" src="img/1.jpg" />
                            <div class="caption">
                              <p class="no-margin">
                                <a href="#" class="btn btn-info">
                                  Change Image
                                </a>
                                <a href="#" class="btn">
                                  Cancel
                                </a>
                              </p>
                            </div>
                          </div>
                        </div>
                        <div class="span9">
                          <form class="form-horizontal" />
                            <h5>
                              Login Information
                            </h5>
                            <hr />
                            <div class="control-group">
                              <label class="control-label">
                                User Name
                              </label>
                              <div class="controls">
                                <a href="#" id="userName" data-type="text" data-pk="1" data-original-title="Click here to edit your name" class="inputText editable editable-click">
                                  Srinu Baswa
                                </a>
                              </div>
                            </div>
                            <div class="control-group">
                              <label class="control-label">
                                Email ID
                              </label>
                              <div class="controls">
                                <a href="#" data-type="email" data-pk="1" data-original-title="Click here to edit your email" class="inputText editable editable-click">
                                  abcde@youmail.com
                                </a>
                              </div>
                            </div>
                            <div class="control-group">
                              <label class="control-label">
                                Password
                              </label>
                              <div class="controls">
                                <a href="#" id="password" data-type="password" data-pk="1" data-original-title="Click here to edit your password" class="inputText editable editable-click">
                                  ******
                                </a>
                              </div>
                            </div>
                            <div class="control-group">
                              <label class="control-label">
                                Confirm Password
                              </label>
                              <div class="controls">
                                <a href="#" id="confirmPassword" data-type="password" data-pk="1" data-original-title="Click here to confirm your password" class="inputText editable editable-click">
                                  ******
                                </a>
                              </div>
                            </div>
                            <br />
                            <h5>
                              Personal Information
                            </h5>
                            <hr />
                            <div class="control-group">
                              <label class="control-label">
                                First Name
                              </label>
                              <div class="controls">
                                <a href="#" id="firstName" data-type="text" data-pk="1" data-original-title="Click here to edit your first name" class="inputText editable editable-click">
                                  Srinu
                                </a>
                              </div>
                            </div>
                            <div class="control-group">
                              <label class="control-label">
                                Last Name
                              </label>
                              <div class="controls">
                                <a href="#" id="lastName" data-type="text" data-pk="1" data-original-title="Click here to edit your first name" class="inputText editable editable-click">
                                  Baswa
                                </a>
                              </div>
                            </div>
                            <div class="control-group">
                              <label class="control-label">
                                Location
                              </label>
                              <div class="controls">
                                <a href="#" id="location" data-type="text" data-pk="1" data-original-title="Click here to edit your first name" class="inputText editable editable-click">
                                  Banglore, India.
                                </a>
                              </div>
                            </div>
                            <div class="control-group">
                              <label class="control-label">
                                Website URL
                              </label>
                              <div class="controls">
                                <a href="#" id="url" data-type="url" data-pk="1" data-original-title="Click here to edit your first name" class="inputText editable editable-click">
                                  http:www.abcxyz.com
                                </a>
                              </div>
                            </div>
                            <div class="control-group">
                              <label class="control-label">
                                Tags
                              </label>
                              <div class="controls">
                                <a href="#" id="tags" data-type="select2" data-pk="1" data-original-title="Enter tags" class="editable editable-click">
                                  Html, CSS, Javascript
                                </a>
                              </div>
                            </div>
                            
                            <div class="control-group">
                              <label class="control-label">
                                About
                              </label>
                              <div class="controls">
                                <a data-original-title="Write about your self" data-placeholder="Your comments here..." data-pk="1" data-type="textarea" id="aboutMe" href="#" class="inputTextArea editable editable-click" style="margin-bottom: 10px;">
                                  About me :)
                                </a>
                              </div>
                            </div>
                            <div class="form-actions no-margin">
                              <button type="submit" class="btn btn-info">
                                Save changes
                              </button>
                              <button type="button" class="btn">
                                Cancel
                              </button>
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
    <script src="js/jquery.scrollUp.js">
    </script>
    <script src="js/bootstrap-editable.min.js">
    </script>
    <script src="js/select2.js">
    </script>
    
    
    <script type="text/javascript">
      //ScrollUp
      $(function () {
        $.scrollUp({
          scrollName: 'scrollUp', // Element ID
          topDistance: '300', // Distance from top before showing element (px)
          topSpeed: 300, // Speed back to top (ms)
          animation: 'fade', // Fade, slide, none
          animationInSpeed: 400, // Animation in speed (ms)
          animationOutSpeed: 400, // Animation out speed (ms)
          scrollText: 'Scroll to top', // Text for element
          activeOverlay: false, // Set CSS color to display scrollUp active point, e.g '#00FFFF'
        });
      });

      //Tooltip
      $('a').tooltip('hide');

      //Xeditable form fields
      $(function () {


        //enable / disable
        $('#enable').click(function () {
          $('#user .editable').editable('toggleDisabled');
        });


        //editables 
        $('.inputText').editable({
          type: 'text',
          pk: 1,
          name: 'name',
          title: 'Enter Name'
        });


        $('.inputTextArea').editable({
          showbuttons: true
        });



        $('#tags').editable({
          inputclass: 'input-large',
          select2: {
            tags: ['html5', 'javascript', 'Jquery', 'css3', 'ajax', 'Sass', 'Haml', 'Photoshop'],
            tokenSeparators: [",", " "]
          }
        });

        $('#user .editable').on('hidden', function (e, reason) {
          if (reason === 'save' || reason === 'nochange') {
            var $next = $(this).closest('tr').next().find('.editable');
            if ($('#autoopen').is(':checked')) {
              setTimeout(function () {
                $next.editable('show');
              }, 300);

            } else {
              $next.focus();
            }

          }
        });

      });
      //Xeditable form fields end

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