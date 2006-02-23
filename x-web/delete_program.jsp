<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
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
<h2>Delete Program</h2>
<form action="DeleteProgram.do" method="post">
<p><input type="hidden" name="program_id" value="${program.id}" /></p>
<table class="data">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Update URL</th>
  <th class="rowheader">Last Update</th>
  <th class="rowheader">Update</th>
 </tr>
 <tr>
  <td class="rowdata">${program.name}</td>
  <td class="rowdata">${program.url}</td>
  <td class="rowdatacenter">${program.lastUpdate}</td>
  <td class="rowdatacenter">${program.doUpdate}</td>
 </tr>
 <tr>
  <td align="right" colspan="4">
   <input type="submit" value="Confirm" />
  </td>
 </tr>
</table>
</form>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
