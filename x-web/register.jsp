<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<p class="error">${message}</p>
<form action="Register.do" method="post">
<table id="login" width="0%">
 <tr>
  <td align="right">Username:</td>
  <td>
   <input type="hidden" name="action" value="${action}" />
   <input type="text" name="username" value="${username}" />
  </td>
 </tr>
 <tr>
  <td align="right">Password:</td>
  <td><input type="password" name="password1" /></td>
 </tr>
 <tr>
  <td align="right">Password Again:</td>
  <td><input type="password" name="password2" /></td>
 </tr>
 <tr>
  <td align="right" colspan="2"><input type="submit" value="Register" /></td>
 </tr>
</table>
</form>
<div><br /></div>
<%@ include file="footer.jsp" %>
</body>
</html>
