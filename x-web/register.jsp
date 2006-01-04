<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<title>Program Guide</title>
</head>
<body>
<%@ include file="header.jsp" %>
<div class="error"><html:errors /></div>
<br />
<html:form action="RegisterSubmit.do" focus="username">
<table id="login" width="0%">
 <tr>
  <td align="right">Username:</td>
  <td>
   <html:hidden property="action" />
   <html:text property="username" /></td>
  </td>
 </tr>
 <tr>
  <td align="right">Password:</td>
  <td><html:password property="password1" /></td>
 </tr>
 <tr>
  <td align="right">Password Again:</td>
  <td><html:password property="password2" /></td>
 </tr>
 <tr>
  <td align="right" colspan="2"><html:submit /></td>
 </tr>
</table>
</html:form>
<div><br /></div>
<%@ include file="footer.jsp" %>
</body>
</html>
