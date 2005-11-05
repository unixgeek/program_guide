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
<h2 class="title">Admin Users</h2>
<table cellspacing="0" border="1">
 <tr>
  <th class="rowheader">Username</th>
  <th class="rowheader">Password</th>
  <th class="rowheader">Last Login Date</th>
  <th class="rowheader">Registration Date</th>
  <th class="rowheader">Level</th>
  <th class="rowheader">&nbsp;</th>
  <th class="rowheader">&nbsp;</th>
 </tr>
 <c:forEach var="user" items="${usersList}">
 <tr>
  <td class="rowdata">${user.username}</td>
  <td class="rowdatacenter">${user.password}</td>
  <td class="rowdatacenter">${user.lastLoginDate}</td>
  <td class="rowdatacenter">${user.registrationDate}</td>
  <td class="rowdatacenter">${user.level}</td>
  <td class="rowdatacenter"><a class="rowdata" href="UpdateUser.do?user_id=${user.id}">Edit</a></td>
  <td class="rowdatacenter"><a class="rowdata" href="DeleteUser.do?user_id=${user.id}">Delete</a></td>
 </tr>
 </c:forEach>
 <tr>
  <td colspan="7" align="right"><a class="rowdata" href="Register.do?action=nologin">Add new user</a></td>
 </tr>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
