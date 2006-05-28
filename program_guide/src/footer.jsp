<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<div>
 <ul id="footerlist">
  <c:if test='${not empty elapsedTime}'>
  <li>${elapsedTime}s</li>
  </c:if>
  <li><a href="http://validator.w3.org/check?uri=referer">valid xhtml</a></li>
  <li>${packageVersion}</li>
 </ul>
</div> 
