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
<h2>Queue</h2>
<table class="data">
 <tr>
  <th class="rowheader">Program</th>
  <th class="rowheader">Season</th>
  <th class="rowheader">Episode</th>
  <th class="rowheader">Production Code</th>
  <th class="rowheader">Original Air Date</th>
  <th class="rowheader">Title</th>
  <th class="rowheader">Summary</th>
 </tr>
 <c:forEach var="userEpisode" items="${queuedEpisodesList}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}">${userEpisode.program.name}</a></td>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter">${userEpisode.episode.number}</td>
  <td class="rowdatacenter">${userEpisode.episode.productionCode}</td>
  <td class="rowdatacenter">
   <c:if test='${not empty userEpisode.episode.originalAirDate}'>
    <dt:format patternId="dateDisplayFormat">
     ${userEpisode.episode.originalAirDate.time}
    </dt:format>
   </c:if>
  </td>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}#id${userEpisode.episode.serialNumber}">${userEpisode.episode.title}</a></td>
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
 </tr>
 </c:forEach>
</table>
<br />
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>