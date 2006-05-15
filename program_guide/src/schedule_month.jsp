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
<h2>${month} ${year}</h2>
<table class="data">
 <tr>
  <td class="calendarlinkleft">
   <a class="rowdata" href="GetScheduleByMonth.do?date=${previousDate}">&lt;&lt;Previous</a>
  </td>
  <td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td>
  <td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td>
  <td class="rowdata">&nbsp;</td>
  <td class="calendarlinkright">
   <a class="rowdata" href="GetScheduleByMonth.do?date=${nextDate}">Next&gt;&gt;</a>
  </td>
 </tr>
 <tr>
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
   <c:choose>
    <c:when test='${not empty day}'>
     <c:choose>
      <c:when test='${day.element.today == "true"}'>
   <td class="today">
      </c:when>
      <c:otherwise>
   <td class="calendarentry">
      </c:otherwise>
     </c:choose>
    </c:when>
    <c:otherwise>
   <td class="nullcalendarentry">
    </c:otherwise>
   </c:choose>
    <table width="100%">
     <tr>
       <td class="calendardate" colspan="2">
         <a class="calendarentry" href="GetScheduleByDay.do?date=<dt:format pattern='yyyyMMdd'>${day.date.time}</dt:format>">
           ${day.dayOfMonth}
         </a>
       </td>
     </tr>
     <c:forEach var="userEpisode" items="${day.element.userEpisodes}">
     <tr><td class="calendarepisode">
      <a class="calendarentry" title="${userEpisode.episode.title}" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}&amp;season=${userEpisode.episode.season}#id${userEpisode.episode.serialNumber}">${userEpisode.program.name}</a>
     </td></tr>
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
