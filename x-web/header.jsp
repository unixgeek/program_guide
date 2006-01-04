<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<div id="header">Program Guide</div>
<c:choose>
<c:when test='${not empty sessionScope.user.username}'>
<div id="subheader">Logged in as ${sessionScope.user.username}.</div>
</c:when>
<c:otherwise>
<div id="subheader">Not logged in.</div>
</c:otherwise>
</c:choose>