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
<h2>Schedule</h2>
<h3>Next 6 Days</h3>
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th>Program</th>
  <th>Season</th>
  <th>Episode</th>
  <th>Production Code</th>
  <th>Original Air Date</th>
  <th>Title</th>
 </tr>
 <c:forEach var="userEpisode" items="${nextEpisodesList}">
 <tr>
  <td class="rowdata"><a href="GetUserEpisodes.do?program_id=${userEpisode.program.id}">${userEpisode.program.name}</a></td>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter">${userEpisode.episode.number}</td>
  <td class="rowdatacenter">${userEpisode.episode.productionCode}</td>
  <td class="rowdatacenter">${userEpisode.episode.originalAirDate}</td>
  <td class="rowdata">${userEpisode.episode.title}</td>
 </tr>
 </c:forEach>
</table>
<h3>Previous 6 Days</h3>
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th>Program</th>
  <th>Season</th>
  <th>Episode</th>
  <th>Production Code</th>
  <th>Original Air Date</th>
  <th>Title</th>
 </tr>
 <c:forEach var="userEpisode" items="${previousEpisodesList}">
 <tr>
  <td class="rowdata"><a href="GetUserEpisodes.do?program_id=${userEpisode.program.id}">${userEpisode.program.name}</a></td>
  <td class="rowdatacenter">${userEpisode.episode.season}</td>
  <td class="rowdatacenter">${userEpisode.episode.number}</td>
  <td class="rowdatacenter">${userEpisode.episode.productionCode}</td>
  <td class="rowdatacenter">${userEpisode.episode.originalAirDate}</td>
  <td class="rowdata">${userEpisode.episode.title}</td>
 </tr>
 </c:forEach>
</table>
</form>
</div>
<br />
<%@ include file="footer.jsp" %>
</body>
</html>
