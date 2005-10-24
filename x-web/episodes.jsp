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
<a href="programs.jsp">Back to programs</a>
<jsp:include page="GetUserEpisodes" />
<form action="episodes.jsp" method="post">
<input type="hidden" name="program_id" value="${program.id}" />
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
  <c:choose>
   <c:when test='${userEpisode.queued == 1}'>
  <td><input type="checkbox" name="queued" value="${userEpisode.episode.season}_${userEpisode.episode.number}" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td><input type="checkbox" name="queued" value="${userEpisode.episode.season}_${userEpisode.episode.number}" /></td>
   </c:otherwise>
  </c:choose>
  <c:choose>
   <c:when test='${userEpisode.viewed == 1}'>
  <td><input type="checkbox" name="viewed" value="${userEpisode.episode.season}_${userEpisode.episode.number}" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td><input type="checkbox" name="viewed" value="${userEpisode.episode.season}_${userEpisode.episode.number}" /></td>
   </c:otherwise>
  </c:choose>
 </tr>
 </c:forEach>
</table>
<input type="submit" value="Update" />
</form>
</body>
</html>