<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<%@ page import="org.siemac.metamac.core.common.util.InternationalizationUtils" %>
<%@ page import="java.util.ResourceBundle" %>
<%@ page import="org.apache.commons.lang.LocaleUtils" %>
<%@ page import="static java.util.ResourceBundle.Control.getNoFallbackControl" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%
    String internationalizationCookie = InternationalizationUtils.getInstance().getInternationalizationCookieId();
    String locale = InternationalizationUtils.getInstance().getCurrentLocale(request);
    String appName = ResourceBundle.getBundle("i18n.messages-swagger" , LocaleUtils.toLocale(locale), getNoFallbackControl(ResourceBundle.Control.FORMAT_DEFAULT)).getString("apps.api_catalog.name");
%>
<fmt:setLocale value="<%= locale %>"/>
<fmt:setBundle basename="i18n.messages-swagger" var="i18n"/>
<fmt:bundle basename="application"/>
<html>
<head>
  <meta charset="UTF-8">
  <title><fmt:message key="api.doc.title" bundle="${i18n}"/></title>
  <link href="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/css/typography.css" media='screen' rel='stylesheet' type='text/css'/>
  <link href="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/css/reset.css" media='screen' rel='stylesheet' type='text/css'/>
  <link href="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/css/screen.css" media='screen' rel='stylesheet' type='text/css'/>
  <link href="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/css/reset.css" media='print' rel='stylesheet' type='text/css'/>
  <link href="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/css/print.css" media='print' rel='stylesheet' type='text/css'/>
  <link href="<%=org.siemac.metamac.core.common.util.WebUtils.getFavicon()%>" rel="shortcut icon"/>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/jquery-1.8.0.min.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/jquery.slideto.min.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/jquery.wiggle.min.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/jquery.ba-bbq.min.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/handlebars-2.0.0.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/underscore-min.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/backbone-min.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/swagger-ui.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/highlight.7.3.pack.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/jsoneditor.min.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/marked.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lib/swagger-oauth.js" type='text/javascript'></script>

  <!-- Some basic translations -->
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lang/translator.js" type='text/javascript'></script>
  <script src="<%=org.siemac.metamac.core.common.util.swagger.SwaggerUtils.getResourceBaseURL(request)%>/swagger-ui/lang/<%=locale %>.js" type='text/javascript'></script>  

  <c:if test="${!empty apiStyleCssUrl}">
    <link href="<c:out value='${apiStyleCssUrl}'/>" media='screen' rel='stylesheet' type='text/css' />
  </c:if>
  
  <script type="text/javascript">
    $(function () {
      var url = window.location.search.match(/url=([^&]+)/);
      if (url && url.length > 1) {
        url = decodeURIComponent(url[1]);
      } else {
    	  var baseUrl = document.location.href;
          //this removes the anchor at the end, if there is one
          baseUrl = baseUrl.substring(0, (baseUrl.indexOf("#") == -1) ? baseUrl.length : baseUrl.indexOf("#"));
          //this removes the query after the file name, if there is one
          baseUrl = baseUrl.substring(0, (baseUrl.indexOf("?") == -1) ? baseUrl.length : baseUrl.indexOf("?"));
          //this removes everything after the last slash in the path
          baseUrl = baseUrl.substring(0, (baseUrl.lastIndexOf("/") == -1) ? baseUrl.length : baseUrl.lastIndexOf("/"));

          url = baseUrl + "/swagger.jsp";
      }

      // Pre load translate...
      if(window.SwaggerTranslator) {
        window.SwaggerTranslator.translate();
      }
      window.swaggerUi = new SwaggerUi({
        url: url,
        dom_id: "swagger-ui-container",
        supportedSubmitMethods: ['get', 'post', 'put', 'delete', 'patch'],
        onComplete: function(swaggerApi, swaggerUi){
          if(typeof initOAuth == "function") {
            initOAuth({
              clientId: "your-client-id",
              clientSecret: "your-client-secret-if-required",
              realm: "your-realms",
              appName: "your-app-name", 
              scopeSeparator: ",",
              additionalQueryStringParams: {}
            });
          }

          if(window.SwaggerTranslator) {
            window.SwaggerTranslator.translate();
          }

          $('pre code').each(function(i, e) {
            hljs.highlightBlock(e)
          });

        },
        onFailure: function(data) {
          log("Unable to Load SwaggerUI");
        },
        docExpansion: "none",
        jsonEditor: false,
        apisSorter: "alpha",
        defaultModelRendering: 'model',
        showRequestHeaders: false
      });

      window.swaggerUi.load();

      function log() {
        if ('console' in window) {
          console.log.apply(console, arguments);
        }
      }
  });
  </script>
</head>

<body>
	<c:set var="apiStyleHeaderUrl" value="<%=org.siemac.metamac.core.common.util.WebUtils.getApiStyleHeaderUrl()%>" />
	<c:set var="apiStyleFooterUrl" value="<%=org.siemac.metamac.core.common.util.WebUtils.getApiStyleFooterUrl()%>" />
	
	<c:if test="${!empty apiStyleHeaderUrl}">
	   <c:import charEncoding="UTF-8" url="${apiStyleHeaderUrl}">
	      <c:param name="appName" value="<%= appName %>" />
	      <c:param name="<%= internationalizationCookie %>" value="<%= locale %>" />
	      <c:param name="appId" value="statistical-resources-external"/>
	   </c:import>
	</c:if>
	
	<div class="swagger-section">
		<div id="message-bar" class="swagger-ui-wrap" data-sw-translate>&nbsp;</div>
		<div id="swagger-ui-container" class="swagger-ui-wrap"></div>
	</div>
	
	<c:if test="${!empty apiStyleFooterUrl}">
	   <c:import charEncoding="UTF-8" url="${apiStyleFooterUrl}">
	      <c:param name="appName" value="<%= appName %>" />
	      <c:param name="<%= internationalizationCookie %>" value="<%= locale %>" />
	   </c:import>
	</c:if>
</body>
</html>
