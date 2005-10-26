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
<%@ include file="menu.jsp" %>
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th colspan="7">Users</th>
 </tr>
 <tr>
  <th>Username</th>
  <th>Password</th>
  <th>Last Login Date</th>
  <th>Registration Date</th>
  <th>Level</th>
  <th>&nbsp;</th>
  <th>&nbsp;</th>
 </tr>
 <c:forEach var="user" items="${usersList}">
 <tr>
  <td class="rowdata">${user.username}</td>
  <td class="rowdatacenter">${user.password}</td>
  <td class="rowdatacenter">${user.lastLoginDate}</td>
  <td class="rowdatacenter">${user.registrationDate}</td>
  <td class="rowdatacenter">${user.level}</td>
  <td class="rowdatacenter"><a href="UpdateUser.do?user_id=${user.id}">Edit</a></td>
  <td class="rowdatacenter"><a href="DeleteUser.do?user_id=${user.id}">Delete</a></td>
 </tr>
 </c:forEach>
 <tr>
  <td colspan="7" align="right"><a href="Register.do">Add new user.</a></td>
 </tr>
</table>
</body>
</html>
