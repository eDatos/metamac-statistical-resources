<%@page import="org.siemac.metamac.core.common.util.WebUtils"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page import="org.siemac.metamac.core.common.util.InternationalizationUtils" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.apache.commons.lang.LocaleUtils" %>
<%@ page import="static java.util.ResourceBundle.Control.getNoFallbackControl" %>
<%
    String internationalizationCookie = InternationalizationUtils.getInstance().getInternationalizationCookieId();
    String locale = InternationalizationUtils.getInstance().getCurrentLocale(request);
    ResourceBundle rs = ResourceBundle.getBundle("i18n.messages-swagger" , LocaleUtils.toLocale(locale), getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT));
%>
<html>
<head>
  <meta charset="UTF-8">
  <title><%=rs.getString("app.name") %></title>
 
  <link href="<%=WebUtils.getFavicon()%>" rel="shortcut icon"/>

  <c:if test="${!empty apiStyleCssUrl}">
    <link href="<c:out value='${apiStyleCssUrl}'/>" media='screen' rel='stylesheet' type='text/css' />
  </c:if>
  
</head>
<body>
    <c:set var="apiStyleHeaderUrl" value="<%=WebUtils.getApiStyleHeaderUrl()%>" />
    <c:set var="apiStyleFooterUrl" value="<%=WebUtils.getApiStyleFooterUrl()%>" />
    
    <c:set var="apiBaseURL" value="<%=WebUtils.getApiBaseURL()%>" />
    
    <c:if test="${!empty apiStyleHeaderUrl}">
       <c:import charEncoding="UTF-8" url="${apiStyleHeaderUrl}" />
    </c:if>
    
    <div class="version-list">
       <h1><%=rs.getString("api.doc.title") %></h1>
       <h2><%=rs.getString("api.doc.versions") %></h2>
       <ul>
           <li>
               <h3 class="version-title"><a href="${apiBaseURL}/latest">/latest</a></h3>
               <div class="version-description">
                   <p><strong>latest</strong> <%=rs.getString("api.doc.latest") %></p>
               </div>
           </li>
           
           <li>
               <h3 class="version-title"><a href="${apiBaseURL}/v1.0">/v1.0</a></h3>
               <div class="version-description">
                    <p><%=rs.getString("api.doc.version.1_0") %></p>
               </div>
           </li>
       </ul>
   </div>

    <c:if test="${!empty apiStyleFooterUrl}">
       <c:import charEncoding="UTF-8" url="${apiStyleFooterUrl}" />
    </c:if>
</body>
</html>
