<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<c:set var="rootContext" scope="page" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}"/>
<c:set var="regV1Api" scope="page" value="${rootContext}/oeca-svc-registration/api/registration/v1"/>
<c:set var="authV1Api" scope="page" value="${rootContext}/oeca-svc-auth/api/authentication/v1/public"/>

<stripes:layout-render name="/layout/cgp-layout.jsp" title="NPDES eReporting Tool" tab="Home">
    <stripes:layout-component name="container">
    	<script>
    		$(function() {
    			var ViewModel = function(data) {
    				var self = this;
    				self.redirect = function() {
    					$.ajax({
    						url: '${pageContext.request.contextPath}/rest/v1/user',
    						type: 'post',
    						data: ko.mapping.toJSON(self),
    						contentType: 'application/json',
    						success: function(result, status, xhr) {
    							window.location.href = xhr.getResponseHeader('Location');
    						}
    					});
    					window.location = '${pageContext.request.contextPath}/action/secured/home'
    				}
    			}
    			ko.applyBindings(new ViewModel());
    		});
    	</script>
    	<style>
    		.panel-primary > .panel-heading a {
    			color: #FFCC66;
    		}
    	</style>
    	<sign-in params="type: 'cdx-redirect', success: redirect, registerUrl: '${pageContext.request.contextPath}/action/registration', imageUrl: '${pageContext.request.contextPath}/static/img/NeT.png'"></sign-in>
    </stripes:layout-component>
</stripes:layout-render>