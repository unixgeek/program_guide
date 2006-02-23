<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2 class="title">Delete User</h2>
<form action="DeleteUser.do" method="post">
<p><input type="hidden" name="user_id" value="${candidateUser.id}" /></p>
<table class="data">
 <tr>
  <th class="rowheader">Username</th>
  <th class="rowheader">Password</th>
  <th class="rowheader">Last Login Date</th>
  <th class="rowheader">Registration Date</th>
  <th class="rowheader">Permissions</th>
 </tr>
 <tr>
  <td class="rowdata">${candidateUser.username}</td>
  <td class="rowdatacenter">${candidateUser.password}</td>
  <td class="rowdatacenter">${candidateUser.lastLoginDate}</td>
  <td class="rowdatacenter">${candidateUser.registrationDate}</td>
  <td class="rowdatacenter">${candidateUser.permissions}</td>
 </tr>
 <tr>
  <td align="right" colspan="5">
   <input type="submit" value="Confirm" />
  </td>
 </tr>
</table>
</form>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
