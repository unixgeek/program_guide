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
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th colspan="6">Programs</th>
 </tr>
 <tr>
  <th>Name</th>
  <th>Update URL</th>
  <th>Last Update</th>
  <th>Update</th>
  <th>&nbsp;</th>
  <th>&nbsp;</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
  <td class="rowdata">${program.name}</td>
  <td class="rowdata">${program.url}</td>
  <td class="rowdatacenter">${program.lastUpdate}</td>
  <td class="rowdatacenter">${program.doUpdate}</td>
  <td class="rowdatacenter"><a href="UpdateProgram.do?program_id=${program.id}">Edit</a></td>
  <td class="rowdatacenter"><a href="DeleteProgram.do?program_id=${program.id}">Delete</a></td>
 </tr>
 </c:forEach>
 <tr>
  <td colspan="6" align="right"><a href="InsertProgram.do">Add new program</a></td>
 </tr>
</table>
<%@ include file="footer.jsp" %>
</body>
</html>
