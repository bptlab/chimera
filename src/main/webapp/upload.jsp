<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>JComparser - upload your PCM Model</title>
</head>
<body>
<h1>JComparser - upload your PCM Model</h1>
	<form name="form1" id="form1" action="done" method="post" enctype="multipart/form-data">
	<!--<input type="hidden" name="hiddenfield1" value="ok">-->
	Please select the files you want to upload into the JEngine
	<br/><br/><br/>
	<input type="file" size="50" name="file1">
	<br/><br/><br/>
	<!--<input type="file" size="50" name="file2">
	<br/>
	<input type="file" size="50" name="file3">
	<br/>-->
	<input type="submit" value="Upload">
	</form>
</body>
</html>