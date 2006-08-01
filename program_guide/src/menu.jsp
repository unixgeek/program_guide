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
  <li id="nav-6"><a href="AdminPrograms.do">Admin</a>
    <ul id="subnav-6">
      <li><a href="AdminPrograms.do">Programs</a></li>
      <li><a href="GetLog.do">Log</a></li>
      <li><a href="AdminUsers.do">Users</a></li>
    </ul>
  </li>
  <li id="nav-7"><a href="Logout.do">Logout (${sessionScope.user.username})</a></li>
</ul>
