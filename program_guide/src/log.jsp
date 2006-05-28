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
<form action="DeleteLog.do" method="post">
<h2 class="title">Log&nbsp;<input type="submit" value="Clear Log" /></h2>
<c:if test="${pages > 1}">
<h3>Page 
  <c:forEach var="page" items="${pageTitles}">
    <c:choose>
      <c:when test='${page != currentPage}'>
        <a class="pagedLink" href="GetLog.do?page=${page}">${page}</a>
      </c:when>
      <c:otherwise>
        <span class="currentPage">${page}</span>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</h3>
</c:if>
<table class="data">
 <tr>
  <th class="rowheader">Source</th>
  <th class="rowheader">Date</th>
 </tr>
 <c:forEach var="log" items="${logEntries}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetLogEntry.do?log_id=${log.id}">${log.source}</a></td>
  <td class="rowdata">
   <c:if test='${not empty log.createDate}'>
    <dt:format patternId="timestampDisplayFormat">${log.createDate.time}</dt:format>
   </c:if>
  </td>
 </tr>
 </c:forEach>
</table>
</form>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
