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
<h2><dt:format pattern="EEEE MMMM dd yyyy">${schedule.date.time}</dt:format></h2>
<table class="data">
 <tr>
  <td class="calendarlinkleft">
    <a class="rowdata" href="GetScheduleByDay.do?date=${previousDate}">&lt;&lt;Previous</a>
  </td>
  <td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td>
  <td class="calendarlinkright">
    <a class="rowdata" href="GetScheduleByDay.do?date=${nextDate}">Next&gt;&gt;</a>
  </td>
 </tr>
 <tr>
  <th class="rowheader">Program</th>
  <th class="rowheader">Season</th>
  <th class="rowheader">Episode</th>
  <th class="rowheader">Title</th>
  <th class="rowheader">Summary</th>
  <th class="rowheader">Status</th>
  <th class="rowheader">Torrent</th>
 </tr>
 <c:forEach var="userEpisode" items="${schedule.element.userEpisodes}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}">${userEpisode.program.name}</a></td>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter">${userEpisode.episode.number}</td>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}&amp;season=${userEpisode.episode.season}#id${userEpisode.episode.serialNumber}">${userEpisode.episode.title}</a></td>
  <td class="rowdata">
   <c:choose>
    <c:when test='${not empty userEpisode.episode.summaryUrl}'>
   <a class="rowdata" href="${userEpisode.episode.summaryUrl}">Summary</a>
    </c:when>
    <c:otherwise>
    &nbsp;
    </c:otherwise>
   </c:choose>
  </td>
  <td class="rowdatacenter">${userEpisode.status}</td>
  <td class="rowdatacenter"><a class="rowdatacenter" href="${site.searchString}<str:encodeUrl>${userEpisode.program.name} ${userEpisode.episode.number}</str:encodeUrl>">${site.name}</a></td>
 </tr>
 </c:forEach>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
