<div class="menu">
<table>
 <tr><td class="username">${user.username}</td></tr>
 <tr><td><a class="menuitem" href="GetUserPrograms.do">Programs</a></td></tr>
 <tr><td><a class="menuitem" href="GetUserRecentEpisodes.do">Recent Episodes</a></td></tr>
 <c:if test="${user.level == '0'}">
 <tr><td><a class="menuitem" href="EditPrograms.do">Edit Programs</a></td></tr>
 <tr><td><a class="menuitem" href="EditUsers.do">Edit Users</a></td></tr>
 </c:if>
 <tr><td><a class="menuitem" href="AccountSettings.do">Account Settings</a></td></tr>
 <tr><td><a class="menuitem" href="Logout.do">Logout</a></td></tr>
</table>
</div>