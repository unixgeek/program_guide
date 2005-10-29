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
<form action="UpdateUser.do" method="post">
<input type="hidden" name="user_id" value="${candidateUser.id}" />
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th colspan="5">Update User</th>
 </tr>
 <tr>
  <th>Username</th>
  <th>Password</th>
  <th>Last Login Date</th>
  <th>Registration Date</th>
  <th>Level</td>
 </tr>
 <tr>
  <td class="rowdata"><input type="text" name="username" value="${candidateUser.username}" /></td>
  <td class="rowdata"><input type="password" name="password" /></td>
  <td class="rowdatacenter">${candidateUser.lastLoginDate}</td>
  <td class="rowdatacenter">${candidateUser.registrationDate}</td>
  <td class="rowdata"><input type="text" name="level" value="${candidateUser.level}" /></td>
 </tr>
 <tr>
  <td align="right" colspan="5">
   <input type="submit" value="Update" />
  </td>
 </tr>
</table>
</form>
<%@ include file="footer.jsp" %>
</body>
</html>
