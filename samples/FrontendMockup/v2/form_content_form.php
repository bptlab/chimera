<?php
include 'core/config.php';
include 'core/RESTCalls.php';

if(isset($_COOKIE['JEngine_ActivityID'])) {
	$PCM_ActivityID = $_COOKIE['JEngine_ActivityID'];
	$get_response = GetActivitiesLabelByID($PCM_ActivityID);
	
	$PCM_ActivityLabel

	echo "<form class='form-horizontal no-margin' />
            <div class='control-group'>
              <label class='control-label'>
                Name
              </label>
              <div class='controls'>
                <input type='text' name='activitylabel' placeholder='".$PCM_ActivityLabel."' disabled='' />
                <input type='hidden' name='activityID' value='".$PCM_ActivityID."'>
              </div>
            </div>
            <div class='control-group'>
              <label class='control-label'>
                Kommentar
              </label>
              <div class='controls'>
                <textarea class='input-block-level' name='comment' placeholder='Enter text ...' style='height: 100px'></textarea>
              </div>
            </div>
            <div class='control-group'>
              <div class='controls'>
                <button type='submit' class='btn btn-info'>Submit</button>
              </div>
            </div>
            <input type='hidden' name='form_token' value='4329074'>
          </form>";
	}
} else {
	?>
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
	<?php
}



