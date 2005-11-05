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
<h2 class="title">Admin Programs</h2>
<table cellspacing="0" border="1">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Update URL</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update</th>
  <th class="rowheader">&nbsp;</th>
  <th class="rowheader">&nbsp;</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
  <td class="rowdata">${program.name}</td>
  <td class="rowdata">${program.url}</td>
  <td class="rowdatacenter">${program.lastUpdate}</td>
  <td class="rowdatacenter">${program.doUpdate}</td>
  <td class="rowdatacenter"><a class="rowdata" href="UpdateProgram.do?program_id=${program.id}">Edit</a></td>
  <td class="rowdatacenter"><a class="rowdata" href="DeleteProgram.do?program_id=${program.id}">Delete</a></td>
 </tr>
 </c:forEach>
 <tr>
  <td colspan="6" align="right"><a class="rowdata" href="InsertProgram.do">Add new program</a></td>
 </tr>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
