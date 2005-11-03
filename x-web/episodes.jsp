<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
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
<input type="hidden" name="program_id" value="${program.id}" />
<h2>${program.name}&nbsp;<input type="submit" value="Update" /></h2>
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th>Season</th>
  <th>Episode</th>
  <th>Production Code</th>
  <th>Original Air Date</th>
  <th>Title</th>
  <th>Status</th>
 </tr>
 <c:forEach var="userEpisode" items="${userEpisodesList}">
 <tr>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter">${userEpisode.episode.number}</td>
  <td class="rowdatacenter">${userEpisode.episode.productionCode}</td>
  <td class="rowdatacenter">${userEpisode.episode.originalAirDate}</td>
  <td class="rowdata">${userEpisode.episode.title}</td>
  <td class="rowdatacenter">
   <select name="status">
   <c:choose>
    <c:when test='${userEpisode.status == "none"}'>
    <option value="none" selected="true">None</option>
    <option value="queued">Queued</option>
    <option value="viewed">Viewed</option>
    </c:when>
    <c:when test='${userEpisode.status == "queued"}'>
    <option value="none" selected="true">None</option>
    <option value="queued" selected="true">Queued</option>
    <option value="viewed">Viewed</option>
    </c:when>
    <c:when test='${userEpisode.status == "viewed"}'>
    <option value="none" selected="true">None</option>
    <option value="queued">Queued</option>
    <option value="viewed" selected="true">Viewed</option>
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
