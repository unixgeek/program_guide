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
<jsp:include page="GetUserPrograms.do" />
<div class="content">
<table border="1">
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
  <td><a href="GetUserEpisodes.do?program_id=${program.id}">${program.name}</a></td>
  <td>${program.url}</td>
  <td>${program.lastUpdate}</td>
  <td>${program.doUpdate}</td>
 </tr>
 </c:forEach>
</table>
</div>
</body>
</html>
