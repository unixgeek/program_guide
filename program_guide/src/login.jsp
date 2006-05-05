<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<link rel="icon" type="image/png" href="program_guide.png" /> 
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<p class="error">${message}</p>
<!-- Because IE is total crap. -->
<div class="center">
<form action="Login.do" method="post">
<table class="centertable">
 <tr>
  <td>Username:</td>
  <td><input type="text" name="username" value="${username}" /></td>
 </tr>
 <tr>
  <td>Password:</td>
  <td><input type="password" name="password" /></td>
 </tr>
 <tr>
  <td align="right" colspan="2"><input type="submit" value="Login" /></td>
 </tr>
 <tr>
  <td align="left" colspan="2"><a class="generic" href="Register.do">Register</a></td>
 </tr>
</table>
</form>
</div>
<div><br /></div>
<%@ include file="footer.jsp" %>
</body>
</html>
