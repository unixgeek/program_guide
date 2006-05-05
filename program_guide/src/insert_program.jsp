<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
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
<h2>Add Program</h2>
<form action="InsertProgram.do" method="post">
<table class="data">
 <tr>
  <th class="rowheader">Name</th>
  <th class="rowheader">Update URL</th>
  <th class="rowheader">Update</th>
 </tr>
 <tr>
  <td class="rowdata"><input type="text" name="name" /></td>
  <td class="rowdata"><input type="text" name="url" /></td>
  <td class="rowdatacenter"><input type="checkbox" name="do_update" value="1" /></td>
 </tr>
 <tr>
  <td class="rowdata" align="right" colspan="4">
   <input type="submit" value="Submit" />
  </td>
 </tr>
</table>
</form>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>