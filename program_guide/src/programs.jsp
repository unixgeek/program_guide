<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<link rel="icon" type="image/png" href="program_guide.png" /> 
<title>Program Guide</title>
</head>
<body class="section-1">
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2>Programs</h2>
<h3> 
  <c:forEach var="prefix" items="${prefixes}">
    <c:choose>
      <c:when test='${prefix != currentPrefix}'>
        <a class="pagedLink" href="GetUserPrograms.do?prefix=${prefix}">${prefix}</a>
      </c:when>
      <c:otherwise>
        <span class="currentPage">${prefix}</span>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</h3>
<table class="data">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update URL</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${program.id}">${program.name}</a></td>
  <td class="rowdatacenter">
   <c:if test='${not empty program.lastUpdate}'>
    <dt:format patternId="timestampDisplayFormat">${program.lastUpdate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdata"><a class="rowdata" target="_blank" href="${program.url}">${program.url}</a></td>
 </tr>
 </c:forEach>
 <tr>
  <td class="rowdata" align="right" colspan="4"><a class="rowdata" href="SetUserPrograms.do">Edit Subscriptions</a></td>
 </tr>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
