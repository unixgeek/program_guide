<div id="menu">
 <a class="menuitem" href="GetUserPrograms.do">Programs (${programCount})</a>
 <a class="menuitem" href="GetUserEpisodesSchedule.do">Schedule (${todayCount})</a>
 <a class="menuitem" href="GetUserEpisodesQueued.do">Queue (${queueCount})</a>
 <c:if test='${showAdminPrograms == "true"}'>
 <a class="menuitem" href="AdminPrograms.do">Admin Programs</a>
 </c:if>
 <c:if test='${showAdminUsers == "true"}'>
 <a class="menuitem" href="AdminUsers.do">Admin Users</a>
 </c:if>
 <a class="menuitem" href="SetUserSettings.do">Account Settings</a>
 <a class="menuitem" href="Logout.do">Logout</a>
</div>
