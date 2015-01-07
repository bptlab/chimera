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
              There are various form elements contained in BP2014W1 UI, date picker, color picker, time picker, date range, registration forms, horizontal forms, wysihtml5 editor, fluid forms, validation states, inputs, custom select controls, custom checkbox &amp; radio, custom input controls with &amp; without appended / prepended elements, toggle (on/off) many more.
            </p>
          </div>

          <div class="row-fluid">
           <div class="span12">
              <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe0b6;"></span> Process
                    <span class="mini-title">
                      [Bosch SI 2]  Steigerung der strukturellen Ambidexterität
                    </span>
                  </div>
                </div>
                <div class="widget-body">
                  <div id="inverse">
                    <div class="navbar">
                      <div class="navbar-inner">
                        <div class="container">
                          <center>  <img src="img/human_task.png"></center>
                        </div>
                    </div>
                  </div>
                </div>
            </div>
          </div>
          </div>
         

          <div class="span6">
            <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe022;"></span> exercise of influence
                  </div>
                </div>
            <div class="widget-body">

                <!-- Expand and collapse a div using javascript -->    
                <script>
                $(".collapse_custom").click(function () {
                      $header = $(this);
                      //getting the next element
                      $content = $header.next();
                      //open up the content needed - toggle the slide- if visible, slide up, if not slidedown.
                      $content.slideToggle(500, function () {
                          //execute this after slideToggle is done
                          //change text of header based on visibility of content div
                          $header.text(function () {
                              //change text based on condition
                              return $content.is(":visible") ? "Collapse" : "Expand";
                          });
                      });

                  });
                </script>

                <div class="container_custom">
                    <div class="collapse_custom"><span>Expand</span>
                    </div>
                    <div class="content_custom">
                        <ul>
                            <li>This is just some random content.</li>
                            <li>This is just some random content.</li>
                            <li>This is just some random content.</li>
                            <li>This is just some random content.</li>
                        </ul>
                    </div>
                </div>

            </div>
          </div>
          </div>


          <div class="span5">
          <div class="widget">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe022;"></span> Human Task
                  </div>
                </div>
                <div class="widget-body">
                  <form class="form-horizontal no-margin" />
                    <div class="control-group">
                      <label class="control-label">
                        Name
                      </label>
                      <div class="controls">
                        <input type="text" placeholder="Abcdef - You are logged in as" disabled="" />
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label">
                        Email
                      </label>
                      <div class="controls">
                        <input type="email" placeholder="abcdef@gmail.com - You are logged in as" disabled="" />
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label">
                        Subject
                      </label>
                      <div class="controls">
                        <select id="subject">
                          <option />General Question
                          <option />Technical Support
                          <option />Sales
                          <option />Email
                          <option />Other
                        </select>
                      </div>
                    </div>
                    <div class="control-group">
                      <label class="control-label">
                        Message
                      </label>
                      <div class="controls">
                        <textarea class="input-block-level" placeholder="Enter text ..." style="height: 100px"></textarea>
                      </div>
                    </div>
                    <div class="control-group">
                      <div class="controls">
                        <button type="submit" class="btn btn-info">Submit</button>
                      </div>
                    </div>
                  </form>
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
        &copy; BlackForest 2013
      </p>
    </footer>
    
    <script src="js/wysiwyg/wysihtml5-0.3.0.js">
    </script>
    <script src="js/jquery.min.js">
    </script>
    <script src="js/bootstrap.js">
    </script>
    <script src="js/jquery.scrollUp.js">
    </script>
    <script src="js/wysiwyg/bootstrap-wysihtml5.js">
    </script>
    <script src="js/bootstrap-colorpicker.js">
    </script>
    <script type="text/javascript" src="js/date-picker/date.js">
    </script>
    <script type="text/javascript" src="js/date-picker/daterangepicker.js">
    </script>
    <script type="text/javascript" src="js/bootstrap-timepicker.js">
    </script>
    <script type="text/javascript" src="js/jquery.bootstrap.wizard.js"></script>
    
    
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

      //Popover
      $('.popover-pop').popover('hide');

      //Collapse
      $('#myCollapsible').collapse({
        toggle: false
      })

      //Tabs
      $('.myTabBeauty a').click(function (e) {
        e.preventDefault();
        $(this).tab('show');
      })

      //Dropdown
      $('.dropdown-toggle').dropdown();

      //wysihtml5
      $('#wysiwyg').wysihtml5();

      //Color picker
      $('.colorpicker').colorpicker();

      //Bootstrap Timepicker
      $('#timepicker1').timepicker();

      $('#timepicker2').timepicker({
        minuteStep: 1,
        secondStep: 5,
        showInputs: false,
        template: 'modal',
        modalBackdrop: true,
        showSeconds: true,
        showMeridian: false
      });

      $('#timepicker3').timepicker({
        minuteStep: 5,
        secondStep: 30,
        showInputs: false,
        showSeconds: true,
        showMeridian: false
      });

      //Date picker
      $('.date_picker').daterangepicker({
        opens: 'left'
      });
      

      //Date Picker

      $('.report_range').daterangepicker({
        ranges: {
          'Today': ['today', 'today'],
          'Yesterday': ['yesterday', 'yesterday'],
          'Last 7 Days': [Date.today().add({
            days: -6
          }), 'today'],
          'Last 30 Days': [Date.today().add({
            days: -29
          }), 'today'],
          'This Month': [Date.today().moveToFirstDayOfMonth(), Date.today().moveToLastDayOfMonth()],
          'Last Month': [Date.today().moveToFirstDayOfMonth().add({
            months: -1
          }), Date.today().moveToFirstDayOfMonth().add({
            days: -1
          })]
        },
        opens: 'left',
        format: 'MM/dd/yyyy',
        separator: ' to ',
        startDate: Date.today().add({
          days: -29
        }),
        endDate: Date.today(),
        minDate: '01/01/2012',
        maxDate: '12/31/2013',
        locale: {
          applyLabel: 'Submit',
          fromLabel: 'From',
          toLabel: 'To',
          customRangeLabel: 'Custom Range',
          daysOfWeek: ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'],
          monthNames: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
          firstDay: 1
        },
        showWeekNumbers: true,
        buttonClasses: ['btn-danger']
      },

      function (start, end) {
        $('.report_range span').html(start.toString('MMMM d, yyyy') + ' - ' + end.toString('MMMM d, yyyy'));
      });

      //Set the initial state of the picker label
      $('.report_range span').html(Date.today().add({
        days: -29
      }).toString('MMMM d, yyyy') + ' - ' + Date.today().toString('MMMM d, yyyy'));


      //Wizard Progressbar

      $('#inverse').bootstrapWizard({
        'tabClass': 'nav',
        'debug': false,
        onShow: function (tab, navigation, index) {
          console.log('onShow');
        },
        onNext: function (tab, navigation, index) {
          console.log('onNext');
          if (index == 2) {
            // Make sure we entered the name
            if (!$('#name').val()) {
              alert('You must enter your name');
              $('#name').focus();
              return false;
            }
          }

          // Set the name for the next tab
          $('#inverse-tab3').html('Hello, ' + $('#name').val());

        },
        onPrevious: function (tab, navigation, index) {
          console.log('onPrevious');
        },
        onLast: function (tab, navigation, index) {
          console.log('onLast');
        },
        onTabClick: function (tab, navigation, index) {
          console.log('onTabClick');
          alert('on tab click disabled');
          return false;
        },
        onTabShow: function (tab, navigation, index) {
          console.log('onTabShow');
          var $total = navigation.find('li').length;
          var $current = index + 1;
          var $percent = ($current / $total) * 100;
          $('#inverse').find('.bar').css({
            width: $percent + '%'
          });
        }
      });


      //Wizard Progressbar tabs left

      $('#tabsleft').bootstrapWizard({
        'tabClass': 'nav nav-tabs',
        'debug': false,
        onShow: function (tab, navigation, index) {
          console.log('onShow');
        },
        onNext: function (tab, navigation, index) {
          console.log('onNext');
        },
        onPrevious: function (tab, navigation, index) {
          console.log('onPrevious');
        },
        onLast: function (tab, navigation, index) {
          console.log('onLast');
        },
        onTabClick: function (tab, navigation, index) {
          console.log('onTabClick');

        },
        onTabShow: function (tab, navigation, index) {
          console.log('onTabShow');
          var $total = navigation.find('li').length;
          var $current = index + 1;
          var $percent = ($current / $total) * 100;
          $('#tabsleft').find('.bar').css({
            width: $percent + '%'
          });

          // If it's the last tab then hide the last button and show the finish instead
          if ($current >= $total) {
            $('#tabsleft').find('.pager .next').hide();
            $('#tabsleft').find('.pager .finish').show();
            $('#tabsleft').find('.pager .finish').removeClass('disabled');
          } else {
            $('#tabsleft').find('.pager .next').show();
            $('#tabsleft').find('.pager .finish').hide();
          }

        }
      });


      $('#tabsleft .finish').click(function () {
        alert('Finished!, Starting over!');
        $('#tabsleft').find("a[href*='tabsleft-tab1']").trigger('click');
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