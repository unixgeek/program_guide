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
  <th colspan="4">Programs</th>
 </tr>
 <tr>
  <th>Name</th>
  <th>Update URL</th>
  <th>Last Update</th>
  <th>Update</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
  <td class="rowdata"><a href="GetUserEpisodes.do?program_id=${program.id}">${program.name}</a></td>
  <td class="rowdata">${program.url}</td>
  <td class="rowdatacenter">${program.lastUpdate}</td>
  <td class="rowdatacenter">${program.doUpdate}</td>
 </tr>
 </c:forEach>
 <tr>
  <td align="right" colspan="4"><a href="SetUserPrograms.do">Edit Subscriptions</a></td>
 </tr>
</table>
</body>
</html>
