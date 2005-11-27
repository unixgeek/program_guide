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
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2 class="title">Edit User</h2>
<p class="error">${message}</p>
<form action="UpdateUser.do" method="post">
<table width="0%">
 <tr>
  <td>Username:</td>
  <td>
   <input type="hidden" name="action" value="username" />
   <input type="hidden" name="user_id" value="${candidateUser.id}" />
   <input type="text" name="username" value="${candidateUser.username}" />
  </td>
  <td><input type="submit" value="Update" /></td>
 </tr>
</table>
</form>
<br />
<form action="UpdateUser.do" method="post">
<table width="0%">
 <tr>
  <td>Password:</td>
  <td>
   <input type="hidden" name="action" value="password" />
   <input type="hidden" name="user_id" value="${candidateUser.id}" />
   <input type="password" name="password1" />
  </td>
  <td>&nbsp;</td>
 </tr>
 <tr>
  <td>Password Again:</td>
  <td><input type="password" name="password2" /></td>
  <td><input type="submit" value="Update" /></td>
 </tr>
</table>
</form>
<br />
<form action="UpdateUser.do" method="post">
<p>
 <input type="hidden" name="action" value="permissions" />
 <input type="hidden" name="user_id" value="${candidateUser.id}" />
</p>
<table cellspacing="0" border="1">
 <tr>
  <th class="rowheader">Granted</th>
  <th class="rowheader">Name</th>
 </tr>
 <tr>
   <c:choose>
   <c:when test='${canUse == "true"}'>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="USAGE|1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="USAGE|1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">USAGE</td>
 </tr>
 <tr>
   <c:choose>
   <c:when test='${canAddProgram == "true"}'>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="ADD_PROGRAM|1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="ADD_PROGRAM|1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">ADD_PROGRAM</td>
 </tr>
 <tr>
   <c:choose>
   <c:when test='${canDeleteProgram == "true"}'>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="DELETE_PROGRAM|1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="DELETE_PROGRAM|1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">DELETE_PROGRAM</td>
 </tr>
 <tr>
   <c:choose>
   <c:when test='${canEditProgram == "true"}'>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="EDIT_PROGRAM|1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="EDIT_PROGRAM|1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">EDIT_PROGRAM</td>
 </tr>
 <tr>
   <c:choose>
   <c:when test='${canAddUser == "true"}'>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="ADD_USER|1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="ADD_USER|1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">ADD_USER</td>
 </tr>
 <tr>
   <c:choose>
   <c:when test='${canDeleteUser == "true"}'>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="DELETE_USER|1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="DELETE_USER|1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">DELETE_USER</td>
 </tr>
 <tr>
   <c:choose>
   <c:when test='${canEditUser == "true"}'>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="EDIT_USER|1" checked="checked" /></td>
   </c:when>
   <c:otherwise>
  <td class="rowdatacenter"><input type="checkbox" name="granted" value="EDIT_USER|1" /></td>
   </c:otherwise>
  </c:choose>
  <td class="rowdata">EDIT_USER</td>
 </tr>
 <tr>
  <td colspan="2" align="right"><input type="submit" value="Update" /></td>
 </tr>
</table>
</form>
<br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
