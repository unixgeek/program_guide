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
<h2>Search Episodes</h2>
<p class="error">${message}</p>
<form action="SearchEpisodes.do" method="post">
<table class="data" width="0%">
 <tr>
  <td class="rowdatacenter"><input type="text" name="query" value="${query}" /></td>
  <td class="rowdatacenter">
   <select name="type">
    <option value="natural" <c:if test='${type == "natural"}'>selected="selected"</c:if>>Natural-Language</option>
    <option value="boolean" <c:if test='${type == "boolean"}'>selected="selected"</c:if>>Boolean</option>
    <option value="expansion" <c:if test='${type == "expansion"}'>selected="selected"</c:if>>Query Expansion</option>
   </select>
  <td class="rowdatacenter"><input type="submit" value="Search" /></td>
 </tr>
</table>
</form>
<c:if  test='${not empty userEpisodesList}'>
<h3>${count} Result(s) (${elapsedTime} seconds)</h3>
<table class="data">
 <tr>
  <th class="rowheader">Program</th>
  <th class="rowheader">Season</th>
  <th class="rowheader">Episode</th>
  <th class="rowheader">Production Code</th>
  <th class="rowheader">Original Air Date</th>
  <th class="rowheader">Title</th>
  <th class="rowheader">Summary</th>
  <th class="rowheader">Torrent</th>
  <th class="rowheader">Status</th>
 </tr>
 <c:forEach var="userEpisode" items="${userEpisodesList}">
 <tr>
  <td class="rowdata"><a class="rowdata" href="GetUserEpisodes.do?program_id=${userEpisode.program.id}&amp;season=${userEpisode.episode.season}#id${userEpisode.episode.serialNumber}">${userEpisode.program.name}</a></td>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter"><a name="${userEpisode.episode.serialNumber}" />${userEpisode.episode.number}</td>
  <td class="rowdatacenter">${userEpisode.episode.productionCode}</td>
  <td class="rowdatacenter">
   <c:if test='${not empty userEpisode.episode.originalAirDate}'>
    <dt:format patternId="dateDisplayFormat">${userEpisode.episode.originalAirDate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdata">${userEpisode.episode.title}</td>
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
  <td class="rowdatacenter"><a class="rowdatacenter" href="${site.searchString}<str:encodeUrl>${userEpisode.program.name} ${userEpisode.episode.number}</str:encodeUrl>">${site.name}</a></td>
  <td class="rowdatacenter">${userEpisode.status}</td>
 </tr>
 </c:forEach>
</table>
</c:if>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
