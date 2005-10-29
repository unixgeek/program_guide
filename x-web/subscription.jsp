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
<form action="SetUserPrograms.do" method="post">
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th colspan="5">Programs</th>
 </tr>
 <tr>
  <th>Subscribed</th>
  <th>Name</th>
  <th>Update URL</th>
  <th>Last Update</th>
  <th>Update</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
   <c:choose>
   <c:when test='${program.subscribed == 1}'>
  <td class="rowdatacenter"><input type="checkbox" name="subscribed" value="${program.program.id}_1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="subscribed" value="${program.program.id}_1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">${program.program.name}</td>
  <td class="rowdata">${program.program.url}</td>
  <td class="rowdatacenter">${program.program.lastUpdate}</td>
  <td class="rowdatacenter">${program.program.doUpdate}</td>
 </tr>
 </c:forEach>
  <tr>
  <td align="right" colspan="5">
   <input type="submit" value="Update" />
  </td>
 </tr>
</table>
<%@ include file="footer.jsp" %>
</body>
</html>
