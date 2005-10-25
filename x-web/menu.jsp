<div id="menu">
<ul>
 <li><a href="GetUserPrograms.do">Programs</a></li>
 <c:if test="${user.level == '0'}">
 <li><a href="programsstuff">Edit Programs</a></li>
 </c:if>
 <li><a href="">Account Settings</a></li>
 <li><a href="Logout.do">Logout</a></li>
</ul>
</div>