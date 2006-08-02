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
<body class="section-2">
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2>Queue</h2>
<c:if test="${pages > 1}">
<h3>Page 
  <c:forEach var="page" items="${pageTitles}">
    <c:choose>
      <c:when test='${page != currentPage}'>
        <a class="pagedLink" href="GetUserEpisodesQueued.do?page=${page}">${page}</a>
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
  <th class="rowheader" title="Program">Program</th>
  <th class="rowheader" title="Season">S</th>
  <th class="rowheader" title="Episode">E</th>
  <th class="rowheader" title="Production Code">Code</th>
  <th class="rowheader" title="Original Air Date">Air Date</th>
  <th class="rowheader" title="Title">Title</th>
  <th class="rowheader" title="Summary">Summary</th>
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
 </tr>
 </c:forEach>
</table>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
