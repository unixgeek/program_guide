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
<form action="UpdateProgram.do" method="post">
<input type="hidden" name="program_id" value="${program.id}" />
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th colspan="4">Edit Program</th>
 </tr>
 <tr>
  <th>Name</th>
  <th>Update URL</th>
  <th>Last Update</th>
  <th>Update</th>
 </tr>
 <tr>
  <td class="rowdata"><input type="text" name="name" value="${program.name}" /></td>
  <td class="rowdata"><input type="text" name="url" value="${program.url}" /></td>
  <td class="rowdatacenter">${program.lastUpdate}</td>
  <c:choose>
   <c:when test='${program.doUpdate == 1}'>
  <td class="rowdatacenter"><input type="checkbox" name="do_update" checked value="0" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="do_update" value="1" /></td>
   </c:otherwise>
  </c:choose>
 </tr>
 <tr>
  <td align="right" colspan="4">
   <input type="submit" value="Update" />
  </td>
 </tr>
</table>
</form>
</body>
</html>