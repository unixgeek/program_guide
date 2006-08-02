<ul id="menu">
  <li id="nav-1"><a href="GetUserPrograms.do">Programs (${programCount})</a></li>
  <li id="nav-2"><a href="GetUserEpisodesQueued.do">Queue (${queueCount})</a></li>
  <li id="nav-3"><a href="GetScheduleByMonth.do">Schedule (${todayCount})</a>
    <ul id="subnav-3">
      <li><a href="GetScheduleByMonth.do">Monthly</a></li>
      <li><a href="GetScheduleByDay.do">Daily</a></li>
    </ul>
  </li>
  <li id="nav-4"><a href="SearchEpisodes.do">Search Episodes</a></li>
  <li id="nav-5"><a href="SetUserSettings.do">Account Settings</a></li>
  <c:if test='${(showAdminPrograms == "true") || (showAdminLog == "true") || (showAdminUsers == "true")}'>
  <li id="nav-6"><a href="ShowAdmin.do">Admin</a>
    <ul id="subnav-6">
      <c:if test='${showAdminPrograms == "true"}'>
        <li><a href="AdminPrograms.do">Programs</a></li>
      </c:if>
      <c:if test='${showAdminLog == "true"}'>
        <li><a href="GetLog.do">Log</a></li>
      </c:if>
      <c:if test='${showAdminUsers == "true"}'>
        <li><a href="AdminUsers.do">Users</a></li>
      </c:if>
    </ul>
  </li>
  </c:if>
  <li id="nav-7"><a href="Logout.do">Logout (${sessionScope.user.username})</a></li>
</ul>
