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
<meta name="viewport" content="width=device-width, initial-scale=1.0"/> 
<title>Program Guide</title>
</head>
<body class="section-3">
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2><dt:format pattern="EEEE MMMM dd yyyy">${schedule.date.time}</dt:format></h2>
<table class="data">
 <tr>
  <td class="calendarlinkleft">
    <a class="rowdata" href="GetScheduleByDay.do?date=${previousDate}">&lt;&lt;Previous</a>
  </td>
  <td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td><td class="rowdata">&nbsp;</td>
  <td class="calendarlinkright">
    <a class="rowdata" href="GetScheduleByDay.do?date=${nextDate}">Next&gt;&gt;</a>
  </td>
 </tr>
 <tr>
  <th class="rowheader" title="Program">Program</th>
  <th class="rowheader" title="Season">Season</th>
  <th class="rowheader" title="Episode">Episode</th>
  <th class="rowheader" title="Title">Title</th>
  <th class="rowheader" title="Summary">Summary</th>
  <th class="rowheader" title="Status">Status</th>
 </tr>
 <c:forEach var="userEpisode" items="${schedule.userObject.userEpisodes}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}">${userEpisode.program.name}</a></td>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter">${userEpisode.episode.number}</td>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}&amp;season=${userEpisode.episode.season}#id${userEpisode.episode.serialNumber}">${userEpisode.episode.title}</a></td>
  <td class="rowdatacenter">
   <c:choose>
    <c:when test='${not empty userEpisode.episode.summaryUrl}'>
   <a class="rowdata" href="${userEpisode.episode.summaryUrl}">summary</a>
    </c:when>
    <c:otherwise>
    &nbsp;
    </c:otherwise>
   </c:choose>
  </td>
  <td class="rowdatacenter">${userEpisode.status}</td>
 </tr>
 </c:forEach>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
