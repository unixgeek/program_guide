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
<form action="InsertProgram.do" method="post">
<table class="tabledata" cellspacing="0" border="1">
 <tr>
  <th colspan="4">New Program</th>
 </tr>
 <tr>
  <th>Name</th>
  <th>Update URL</th>
  <th>Update</th>
 </tr>
 <tr>
  <td class="rowdata"><input type="text" name="name" /></td>
  <td class="rowdata"><input type="text" name="url" /></td>
  <td class="rowdatacenter"><input type="checkbox" name="do_update" value="1" /></td>
 </tr>
 <tr>
  <td align="right" colspan="4">
   <input type="submit" value="Submit" />
  </td>
 </tr>
</table>
</form>
</body>
</html>