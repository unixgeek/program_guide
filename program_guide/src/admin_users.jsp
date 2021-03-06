<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<link rel="icon" type="image/png" href="program_guide.png" />
<meta name="viewport" content="width=device-width, initial-scale=1.0"/> 
<title>Program Guide</title>
</head>
<body class="section-6">
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2 class="title">Admin Users</h2>
<table class="data">
 <tr>
  <th class="rowheader">Username</th>
  <th class="rowheader">Password</th>
  <th class="rowheader">Last Login Date</th>
  <th class="rowheader">Registration Date</th>
  <th class="rowheader">Permissions</th>
  <c:if test='${canEdit == "true"}'>
  <th class="rowheader">&nbsp;</th>
  </c:if>
  <c:if test='${canDelete == "true"}'>
  <th class="rowheader">&nbsp;</th>
  </c:if>
 </tr>
 <c:forEach var="user" items="${usersList}">
 <tr>
  <td class="rowdata">${user.username}</td>
  <td class="rowdatacenter">${user.password}</td>
  <td class="rowdatacenter">
   <c:if test='${not empty user.lastLoginDate}'>
    <dt:format patternId="timestampDisplayFormat">${user.lastLoginDate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdatacenter">
   <c:if test='${not empty user.registrationDate}'>
    <dt:format patternId="timestampDisplayFormat">${user.registrationDate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdatacenter">${user.permissions}</td>
  <c:if test='${canEdit == "true"}'>
  <td class="rowdatacenter"><a class="rowdata" href="UpdateUser.do?user_id=${user.id}">Edit</a></td>
  </c:if>
  <c:if test='${canDelete == "true"}'>
  <td class="rowdatacenter"><a class="rowdata" href="DeleteUser.do?user_id=${user.id}">Delete</a></td>
  </c:if>
 </tr>
 </c:forEach>
 <c:if test='${canAdd == "true"}'>
 <tr>
  <td class="rowdata" colspan="7" align="right"><a class="rowdata" href="Register.do?action=nologin">Add new user</a></td>
 </tr>
 </c:if>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
