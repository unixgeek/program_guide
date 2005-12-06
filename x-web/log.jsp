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
<h2 class="title">Log</h2>
<table cellspacing="0" border="1">
 <tr>
  <th class="rowheader">Source</th>
  <th class="rowheader">Date</th>
 </tr>
 <c:forEach var="log" items="${logEntries}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetLogEntry.do?log_id=${log.id}">${log.source}</a></td>
  <td class="rowdata">${log.createDate}</td>
 </tr>
 </c:forEach>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
