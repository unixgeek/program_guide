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
<body>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2>Schedule for ${month}</h2>
<table class="data">
 <tr class="test">
  <th class="rowheader">Sunday</th>
  <th class="rowheader">Monday</th>
  <th class="rowheader">Tuesday</th>
  <th class="rowheader">Wednesday</th>
  <th class="rowheader">Thursday</th>
  <th class="rowheader">Friday</th>
  <th class="rowheader">Saturday</th>
 </tr>
 <c:forEach var="week" items="${schedule}">
 <tr>
  <c:forEach var="day" items="${week}">
   <td class="calendarentry">
    <table width="100%">
     <tr><td class="calendardate" colspan="2">${day.dayOfMonth}</td></tr>
     <c:forEach var="userEpisodes" items="${day.userEpisodes}">
     <tr><td class="calendarepisode">${userEpisodes.episode.title}</td></tr>
     </c:forEach>
    </table>
   </td>
  </c:forEach>
 </tr>
 </c:forEach>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
