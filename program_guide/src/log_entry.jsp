<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html 
     PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
     "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<link rel="stylesheet" href="default.css" type="text/css" />
<link rel="icon" type="image/png" href="program_guide.png" /> 
<meta name="viewport" content="width=device-width, initial-scale=1.0"/>
<title>Program Guide</title>
</head>
<body class="section-6">
<%@ include file="header.jsp" %>
<%@ include file="menu.jsp" %>
<div class="content">
<h2 class="title">Log Entry</h2>
<h3 class="title">${log.source}:&nbsp;&nbsp;
 <c:if test='${not empty log.createDate}'>
  <dt:format patternId="timestampDisplayFormat">${log.createDate.time}</dt:format>
 </c:if>
</h3>
<textarea class="log" cols="140" rows="30" readonly="readonly">
${log.content}
</textarea>
<br /><br />
</div>
<%@ include file="footer.jsp" %>
</body>
</html>
