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
<c:if  test='${not empty searchResults}'>
<h3>${count} Results (${elapsedTime} seconds)</h3>
<table class="data">
 <tr>
  <th class="rowheader">Program</th>
  <th class="rowheader">Title</th>
  <th class="rowheader">Score</th>
 </tr>
 <c:forEach var="result" items="${searchResults}">
 <tr>
  <td class="rowdata">${result.programName}</td>
  <td class="rowdata">${result.episodeTitle}</td>
  <td class="rowdatacenter">${result.formattedScore}</td>
  </td>
 </tr>
 </c:forEach>
</table>
</c:if>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
