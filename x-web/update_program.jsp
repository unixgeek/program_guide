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
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2>Edit Program</h2>
<form action="UpdateProgram.do" method="post">
<p><input type="hidden" name="program_id" value="${program.id}" /></p>
<table class="data">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Update URL</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update</th>
 </tr>
 <tr>
  <td class="rowdata"><input type="text" name="name" value="${program.name}" /></td>
  <td class="rowdata"><input type="text" name="url" value="${program.url}" /></td>
  <td class="rowdatacenter">
   <c:if test='${not empty program.lastUpdate}'>
    <dt:format patternId="timestampDisplayFormat">${program.lastUpdate.time}</dt:format>
   </c:if>
  </td>
  <c:choose>
   <c:when test='${program.doUpdate == 1}'>
  <td class="rowdatacenter"><input type="checkbox" name="do_update" checked="checked" value="1" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="do_update" value="1" /></td>
   </c:otherwise>
  </c:choose>
 </tr>
 <tr>
  <td class="rowdata" align="right" colspan="4">
   <input type="submit" value="Update" />
  </td>
 </tr>
</table>
</form>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
