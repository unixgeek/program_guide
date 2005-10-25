<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
<link rel="stylesheet" href="default.css" type="text/css"
<title>Program Guide</title>
</head>
<body>
<p>${message}</p>
<form name="login" action="Login.do" method="post">
<table class="login" width="0%">
 <tr>
  <td>Username:</td>
  <td><input type="text" name="username" value="${username}"/></td>
 </tr>
 <tr>
  <td>Password:</td>
  <td><input type="password" name="password" /></td>
 </tr>
 <tr>
  <td>&nbsp;</td>
  <td class="tablebutton"><input type="submit" value="Login" /></td>
 </tr>
</table>
</form>
</body>
</html>