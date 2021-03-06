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
<body class="section-1">
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<form action="SetUserEpisodes.do" method="post">
<div><input type="hidden" name="program_id" value="${program.id}" /></div>
<div><input type="hidden" name="season" value="${currentSeason}" /></div>
<h2>${program.name}&nbsp;&nbsp;<input type="submit" value="Update" /></h2>
<h3>${program.network}</h3>
<h3>Season 
  <c:forEach var="season" items="${seasons}">
    <c:choose>
      <c:when test='${season != currentSeason}'>
        <a class="pagedLink" href="GetUserEpisodes.do?program_id=${program.id}&amp;season=${season}">${season}</a>
      </c:when>
      <c:otherwise>
        <span class="currentPage">${season}</span>
      </c:otherwise>
    </c:choose>
  </c:forEach>
</h3>
<table class="data">
 <tr>
  <th class="rowheader" title="Season">Season</th>
  <th class="rowheader" title="Episode">Episode</th>
  <th class="rowheader" title="Original Air Date">Air Date</th>
  <th class="rowheader" title="Title">Title</th>
  <th class="rowheader" title="Summary">Summary</th>
  <th class="rowheader" title="Status">Status</th>
 </tr>
 <c:forEach var="userEpisode" items="${userEpisodesList}">
 <tr>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter"><a id="id${userEpisode.episode.serialNumber}" />${userEpisode.episode.number}</td>
  <td class="rowdatacenter">
   <c:if test='${not empty userEpisode.episode.originalAirDate}'>
    <dt:format patternId="dateDisplayFormat">${userEpisode.episode.originalAirDate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdata">${userEpisode.episode.title}</td>
  <td class="rowdatacenter">
   <c:choose>
    <c:when test='${not empty userEpisode.episode.summaryUrl}'>
   <a class="rowdata" target="_blank" href="${userEpisode.episode.summaryUrl}">summary</a>
    </c:when>
    <c:otherwise>
    &nbsp;
    </c:otherwise>
   </c:choose>
  </td>
  <td class="rowdata">
   <c:choose>
    <c:when test='${userEpisode.status == "none"}'>
   <select name="status">
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_none" selected="selected">None</option>
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_queued">Queued</option>
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_viewed">Viewed</option>
    </c:when>
    <c:when test='${userEpisode.status == "queued"}'>
   <select name="status">
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_none">None</option>
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_queued" selected="selected">Queued</option>
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_viewed">Viewed</option>
    </c:when>
    <c:when test='${userEpisode.status == "viewed"}'>
   <select name="status">
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_none">None</option>
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_queued">Queued</option>
    <option value="${userEpisode.episode.season}_${userEpisode.episode.number}_viewed" selected="selected">Viewed</option>
    </c:when>
   </c:choose>
   </select>
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
