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
<%@ include file="menu.jsp" %>
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th colspan="8">Recent Episodes</th>
 </tr>
 <tr>
  <th>Program</th>
  <th>Season</th>
  <th>Episode</th>
  <th>Production Code</th>
  <th>Original Air Date</th>
  <th>Title</th>
 </tr>
 <c:forEach var="userEpisode" items="${userEpisodesList}">
 <tr>
  <td class="rowdata"><a href="GetUserEpisodes.do?program_id=${userEpisode.program.id}">${userEpisode.program.name}</a></td>
  <td class="rowdata">${userEpisode.episode.season}</td>
  <td class="rowdata">${userEpisode.episode.number}</td>
  <td class="rowdata">${userEpisode.episode.productionCode}</td>
  <td class="rowdatacenter">${userEpisode.episode.originalAirDate}</td>
  <td class="rowdatacenter">${userEpisode.episode.title}</td>
 </tr>
 </c:forEach>
</table>
</form>
</body>
</html>