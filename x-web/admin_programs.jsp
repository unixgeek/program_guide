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
<h2 class="title">Admin Programs</h2>
<table class="data">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Update URL</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update</th>
  <c:if test='${canEdit == "true"}'>
  <th class="rowheader">&nbsp;</th>
  </c:if>
  <c:if test='${canDelete == "true"}'>
  <th class="rowheader">&nbsp;</th>
  </c:if>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
  <td class="rowdata">${program.name}</td>
  <td class="rowdata">${program.url}</td>
  <td class="rowdatacenter">
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
  <c:if test='${canEdit == "true"}'>
  <td class="rowdatacenter"><a class="rowdata" href="UpdateProgram.do?program_id=${program.id}">Edit</a></td>
  </c:if>
  <c:if test='${canDelete == "true"}'>
  <td class="rowdatacenter"><a class="rowdata" href="DeleteProgram.do?program_id=${program.id}">Delete</a></td>
  </c:if>
 </tr>
 </c:forEach>
 <c:if test='${canAdd == "true"}'>
 <tr>
  <td class="rowdata" colspan="6" align="right"><a class="rowdata" href="InsertProgram.do">Add new program</a></td>
 </tr>
 </c:if>
</table>
<br />
<c:if  test='${empty programsList}'><br /></c:if>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
