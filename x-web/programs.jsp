<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs-string.tld" prefix="str" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2>Programs</h2>
<table cellspacing="0" border="1">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update URL</th>
  <th class="rowheader">Torrent</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${program.id}">${program.name}</a></td>
  <td class="rowdatacenter">${program.lastUpdate}</td>
  <td class="rowdata"><a class="rowdata" href="${program.url}">${program.url}</a></td>
  <td class="rowdatacenter"><a class="rowdata" href="${site.searchString}<str:encodeUrl>${program.name}</str:encodeUrl>">${site.name}</a></td>
 </tr>
 </c:forEach>
 <tr>
  <td align="right" colspan="4"><a class="rowdata" href="SetUserPrograms.do">Edit Subscriptions</a></td>
 </tr>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
