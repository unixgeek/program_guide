<div>
  <ul id="menu">
    <li><a href="GetUserPrograms.do">Programs (${programCount})</a></li>
    <li><a href="GetUserEpisodesQueued.do">Queue (${queueCount})</a></li>
    <li><a href="GetScheduleByMonth.do">Schedule (${todayCount})</a></li>
    <li><a href="SearchEpisodes.do">Search Episodes</a></li>
    <li><a href="SetUserSettings.do">Account Settings</a></li>
    <c:if test='${showAdminLog == "true"}'>
      <li><a href="GetLog.do">Admin Log</a></li>
    </c:if>
    <c:if test='${showAdminPrograms == "true"}'>
      <li><a href="AdminPrograms.do">Admin Programs</a></li>
    </c:if>
    <c:if test='${showAdminUsers == "true"}'>
      <li><a href="AdminUsers.do">Admin Users</a></li>
    </c:if>
    <li><a href="Logout.do">Logout (${sessionScope.user.username})</a></li>
  </ul>
</div>
