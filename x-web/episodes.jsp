<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>Program Guide</title>
</head>
<body>
<jsp:include page="GetUserEpisodes" />
<table border="1">
 <tr>
  <th colspan="8">${program.name}</th>
 </tr>
 <tr>
  <th>Season</th>
  <th>Episode</th>
  <th>Production Code</th>
  <th>Original Air Date</th>
  <th>Title</th>
  <th>Queued</th>
  <th>Viewed</th>
 </tr>
 <c:forEach var="userEpisode" items="${userEpisodesList}">
 <tr>
  <td>${userEpisode.episode.season}</td>
  <td>${userEpisode.episode.number}</td>
  <td>${userEpisode.episode.productionCode}</td>
  <td>${userEpisode.episode.originalAirDate}</td>
  <td>${userEpisode.episode.title}</td>
  <td>${userEpisode.queued}</td>
  <td>${userEpisode.viewed}</td>
 </tr>
 </c:forEach>
</table>
</body>
</html>