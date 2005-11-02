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
<%@ include file="menu.jsp" %>
<div class="content">
<h2 class="title">Edit User</h2>
<p class="error">${message}</p>
<table width="0%">
 <tr>
  <form action="UpdateUser.do" method="post">
  <input type="hidden" name="action" value="username" />
  <input type="hidden" name="user_id" value="${candidateUser.id}" />
  <td align="right">Username:</td>
  <td><input type="text" name="username" value="${candidateUser.username}" /></td>
  <td><input type="submit" value="Update" /></td>
  </form>
 </tr>
 <tr><td>&nbsp;</td></tr>
 <tr>
  <form action="UpdateUser.do" method="post">
  <input type="hidden" name="action" value="password" />
  <input type="hidden" name="user_id" value="${candidateUser.id}" />
  <td align="right">Password:</td>
  <td><input type="password" name="password1" /></td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td align="right">Password Again:</td>
  <td><input type="password" name="password2" /></td>
  <td><input type="submit" value="Update" /></td>
  </form>
 </tr>
 <tr><td>&nbsp;</td></tr>
 <tr>
  <form action="UpdateUser.do" method="post">
  <input type="hidden" name="action" value="level" />
  <input type="hidden" name="user_id" value="${candidateUser.id}" />
  <td align="right">Level:</td>
  <td><input type="text" name="level" value="${candidateUser.level}" /></td>
  <td><input type="submit" value="Update" /></td>
  </form>
 </tr>
</table>
</div>
<br />
<%@ include file="footer.jsp" %>
</body>
</html>
