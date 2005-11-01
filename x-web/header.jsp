<div id="header">Program Guide</div>
<c:choose>
<c:when test='${not empty sessionScope.user.username}'>
<div id="title">Logged in as ${sessionScope.user.username}.</div>
</c:when>
<c:otherwise>
<div id="title">Not logged in.</div>
</c:otherwise>
</c:choose>