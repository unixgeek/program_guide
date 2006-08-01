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
<body class="section-5">
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2 class="title">Account Settings</h2>
<p class="error">${message}</p>
<form action="SetUserSettings.do" method="post">
<table width="0%">
 <tr>
  <td>Username:</td>
  <td>
   <input type="hidden" name="action" value="username" />
   <input type="text" name="username" value="${sessionScope.user.username}" />
  </td>
  <td><input type="submit" value="Update" /></td>
 </tr>
</table>
</form>
<br />
<form action="SetUserSettings.do" method="post">
<table width="0%">
 <tr>
  <td>Password:</td>
  <td>
   <input type="hidden" name="action" value="password" />
   <input type="password" name="password1" />
  </td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Password Again:</td>
  <td><input type="password" name="password2" /></td>
  <td><input type="submit" value="Update" /></td>
 </tr>
</table>
</form>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
