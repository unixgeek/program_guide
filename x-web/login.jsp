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
<html:form action="LoginSubmit.do" focus="username">
<table id="login" width="0%">
 <tr>
  <td>Username:</td>
  <td><html:text property="username" /></td>
 </tr>
 <tr>
  <td>Password:</td>
  <td><html:password property="password" /></td>
 </tr>
 <tr>
  <td align="right" colspan="2"><html:submit /></td>
 </tr>
 <tr>
  <td align="left" colspan="2"><a class="generic" href="Register.do">Register</a></td>
 </tr>
</table>
</html:form>
<%@ include file="footer.jsp" %>
</body>
</html>
