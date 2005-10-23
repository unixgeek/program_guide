<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
<title>Program Guide</title>
</head>
<body>
<h2>Episodes</h2>
<table border="0">
 <tr>
  <th>Name</th>
  <th>Season</th>
  <th>Episode</th>
  <th>Production Code</th>
  <th>Original Air Date</th>
  <th>Title</th>
  <th>Queued</th>
  <th>Viewed</th>
 </tr>
 <jsp:include page="GetUserEpisodes" />
 <c:forEach var="userEpisode" items="${userEpisodesList}">
 <tr>
  <td>${userEpisode.program.name}</td>
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