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
<jsp:include page="EditPrograms" />
<form action="edit_programs.jsp" method="post">
<input type="hidden" name="program_id" value="${program.id}" />
<table border="1">
 <tr>
  <th colspan="4">Programs</th>
 </tr>
 <tr>
  <th>Name</th>
  <th>Update URL</th>
  <th>Last Update</th>
  <th>Update</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
  <td><input type="text" name="name${program.id}" value="${program.name}" /></td>
  <td><input type="text" name="url${program.id}" value="${program.url}" /></td>
  <td>${program.lastUpdate}</td>
  <c:choose>
   <c:when test='${program.doUpdate == 1}'>
  <td><input type="checkbox" name="update${program.id}" checked value="0" /></td>
   </c:when>
   <c:otherwise>
  <td><input type="checkbox" name="update${program.id}" value="1" /></td>
   </c:otherwise>
  </c:choose>
 </tr>
 </c:forEach>
</table>
<input type="submit" value="Update" />
</form>
</body>
</html>
