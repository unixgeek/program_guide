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
<h2>Delete Program</h2>
<form action="DeleteProgram.do" method="post">
<p><input type="hidden" name="program_id" value="${program.id}" /></p>
<table class="data">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Update URL</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update</th>
 </tr>
 <tr>
  <td class="rowdata">${program.name}</td>
  <td class="rowdata">${program.url}</td>
  <td class="rowdata">
   <c:if test='${not empty program.lastUpdate}'>
    <dt:format patternId="timestampDisplayFormat">${program.lastUpdate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdatacenter">
   <c:choose>
    <c:when test='${program.doUpdate == "1"}'>yes</c:when>
    <c:otherwise>no</c:otherwise>
   </c:choose>
  </td>
 </tr>
 <tr>
  <td class="rowdata" align="right" colspan="4">
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
