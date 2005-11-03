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
<form action="SetUserPrograms.do" method="post">
<h2>Edit Subscriptions&nbsp;<input type="submit" value="Update" /></h2>
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th>Subscribed</th>
  <th>Name</th>
  <th>Last Update</th>
  <th>Update URL</th>
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
  <td class="rowdatacenter">${program.program.lastUpdate}</td>
  <td class="rowdata"><a href="${program.program.url}">${program.program.url}</a></td>
 </tr>
 </c:forEach>
</table>
</form>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
