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
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<form action="SetUserEpisodes.do" method="post">
<p><input type="hidden" name="program_id" value="${program.id}" /></p>
<h2>${program.name}&nbsp;<input type="submit" value="Update" /></h2>
<table class="data">
 <tr>
  <th class="rowheader">Season</th>
  <th class="rowheader">Episode</th>
  <th class="rowheader">Production Code</th>
  <th class="rowheader">Original Air Date</th>
  <th class="rowheader">Title</th>
  <th class="rowheader">Torrent</th>
  <th class="rowheader">Status</th>
 </tr>
 <c:forEach var="userEpisode" items="${userEpisodesList}">
 <tr>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter"><a name="${userEpisode.episode.serialNumber}" />${userEpisode.episode.number}</td>
  <td class="rowdatacenter">${userEpisode.episode.productionCode}</td>
  <td class="rowdatacenter">
   <c:if test='${not empty userEpisode.episode.originalAirDate}'>
    <dt:format patternId="dateDisplayFormat">${userEpisode.episode.originalAirDate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdata">${userEpisode.episode.title}</td>
    <td class="rowdatacenter"><a class="rowdatacenter" href="${site.searchString}<str:encodeUrl>${userEpisode.program.name} ${userEpisode.episode.number}</str:encodeUrl>">${site.name}</a></td>
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
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
