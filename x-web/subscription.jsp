<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<link rel="icon" type="image/png" href="program_guide.png" /> 
<script type="text/javascript">
    function toggle(checked) {
        form = document.getElementById("subscription");
        for (var i = 0; i != form.subscribed.length; i++) {
            form.subscribed[i].checked = checked;
        }
    }
</script>
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<form action="SetUserPrograms.do" id="subscription" method="post">
<h2>Edit Subscriptions&nbsp;<input type="submit" value="Update" /></h2>
<div><a class="rowdata" href="javascript:toggle(true)">Check All</a><a class="rowdata" href="javascript:toggle(false)">Uncheck All</a></div>
<table class="data">
 <tr>
  <th class="rowheader">Subscribed</th>
  <th class="rowheader">Name</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update URL</th>
 </tr>
 <c:forEach var="program" items="${programsList}">
 <tr>
   <c:choose>
   <c:when test='${program.subscribed == 1}'>
  <td class="rowdatacenter"><input type="checkbox" name="subscribed" value="${program.program.id}_1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="subscribed" value="${program.program.id}_1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">${program.program.name}</td>
  <td class="rowdatacenter">
   <c:if test='${not empty program.program.lastUpdate}'>
    <dt:format patternId="timestampDisplayFormat">${program.program.lastUpdate.time}</dt:format>
   </c:if>
  </td>
  <td class="rowdata"><a class="rowdata" href="${program.program.url}">${program.program.url}</a></td>
 </tr>
 </c:forEach>
</table>
</form>
<br />
<c:if  test='${empty programsList}'><br /></c:if>
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
