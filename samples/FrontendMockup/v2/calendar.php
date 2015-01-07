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
              <div class="widget no-margin">
                <div class="widget-header">
                  <div class="title">
                    <span class="fs1" aria-hidden="true" data-icon="&#xe052;"></span> Calendar
                  </div>
                </div>
                <div class="widget-body">
                  <div id='calendar'>
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
    <script src="js/jquery-ui-1.8.23.custom.min.js">
    </script>
    <script src="js/bootstrap.js">
    </script>
    <script src="js/fullcalendar.js">
    </script>
    <script src="js/jquery.scrollUp.js">
    </script>
    
    
    <script type="text/javascript">
    $(document).ready(function () {

      var date = new Date();
      var d = date.getDate();
      var m = date.getMonth();
      var y = date.getFullYear();

      calendar = $('#calendar').fullCalendar({
        theme: true,
        selectable: true,
        selectHelper: true,
        select: function (start, end, allDay) {
          var title = prompt('Event Title:');
          if (title) {
            calendar.fullCalendar('renderEvent', {
              title: title,
              start: start,
              end: end,
              allDay: allDay
            },
              true // make the event "stick"
              );
          }
          calendar.fullCalendar('unselect');
        },
        header: {
          left: 'prev,next today',
          center: 'title',
          right: 'month,agendaWeek,agendaDay'
        },
        editable: true,
        events: [{
          title: 'All Day Event',
          start: new Date(y, m, 1)
        }, {
          title: 'Long Event',
          start: new Date(y, m, d - 5),
          end: new Date(y, m, d - 2)
        }, {
          id: 999,
          title: 'Repeating Event',
          start: new Date(y, m, d - 3, 16, 0),
          allDay: false
        }, {
          id: 999,
          title: 'Repeating Event',
          start: new Date(y, m, d + 4, 16, 0),
          allDay: false
        }, {
          title: 'Meeting',
          start: new Date(y, m, d, 10, 30),
          allDay: false
        }, {
          title: 'Lunch',
          start: new Date(y, m, d, 12, 0),
          end: new Date(y, m, d, 14, 0),
          allDay: false
        }, {
          title: 'Birthday Party',
          start: new Date(y, m, d + 1, 19, 0),
          end: new Date(y, m, d + 1, 22, 30),
          allDay: false
        }]
      });

});

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