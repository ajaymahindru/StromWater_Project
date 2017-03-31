<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="stripes"
	uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<stripes:layout-definition>
	<stripes:layout-render name="/layout/layout.jsp" title="${title }" tab="${tab }">
		<stripes:layout-component name="additionalHead">
			<script src="${pageContext.request.contextPath}/static/js/models.js"></script>
			<script src="${pageContext.request.contextPath}/static/js/controllers.js"></script>
			<script src="${pageContext.request.contextPath}/static/js/temp.js"></script>
			<script src="${pageContext.request.contextPath}/static/js/knockout-file-bindings.js"></script>
			<script src="${pageContext.request.contextPath}/static/js/components.js"></script>
			<script src="${pageContext.request.contextPath}/static/js/register.js"></script>
			<script src="${pageContext.request.contextPath}/static/js/cgp.js"></script>
			<link href="${pageContext.request.contextPath}/static/css/cgp.css" rel="stylesheet">
			<link href="${pageContext.request.contextPath}/static/css/knockout-file-bindings.css" rel="stylesheet">
			<script type="text/javascript" id="cromerrServerSign" src="${actionBean.cgpUrls.cdxContentFrameworks}/Cromerr/js/serverWidget.js"></script>
            <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/fancybox/jquery.easing-1.3.pack.js"></script>
            <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/fancybox/jquery.mousewheel-3.0.4.pack.js"></script>
            <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/fancybox/jquery.fancybox-1.3.4.js"></script>
            <script type="text/javascript" src="${pageContext.request.contextPath}/static/js/migrate/jquery-migrate-1.2.1.min.js"></script>
            <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/static/css/jquery.fancybox-1.3.4.css" />
			<script>
                oeca.cgp.urls = {}
                <c:forEach items="${actionBean.cgpUrls}" var="entry">
                	oeca.cgp.urls['${entry.key}'] = "${entry.value }";
                </c:forEach>

                oeca.cgp.ctx = "${pageContext.request.contextPath}";
                <c:choose>
                <c:when test="${not empty actionBean.user}">
					oeca.cgp.currentUser = {
						username: "${actionBean.user.username}",
						userRoleId: "${actionBean.user.userRoleId}"
					};
                </c:when>
                <c:otherwise>
					oeca.cgp.currentUser = {
						username: null,
						userRoleId: null
					};
                </c:otherwise>
                </c:choose>
                
                var PhantomJSPrinting = {
                    header : {
                        height : '0.0in',
                        contents : function(pageNum, numPages) {
                            return '';
                        }
                    },
                    footer : {
                        height : '0.25in',
                        contents : function(pageNum, numPages) {
                            return '<span style="font-family: Helvetica, Ariel, sans-serif; font-size: 12px; float: right">Page ' + pageNum + ' of ' + numPages + '<span>';
                        }
                    }
                };
			</script>
			<c:url var="logoutUrl" value="/action/logout"/>
			<stripes:layout-component name="additionalHead">

			</stripes:layout-component>
		</stripes:layout-component>
		<stripes:layout-component name="topNavBar">
			<div class="navbar navbar-default navbar-fixed-top" role="navigation">
				<div class="navbar-header">
	                <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
	                    <span class="sr-only">Toggle navigation</span>
	                    <span class="icon-bar"></span>
	                    <span class="icon-bar"></span>
	                    <span class="icon-bar"></span>
	                </button>
	                <div class="navbar-brand" style="margin-top: -5px">
	                	<img src="${pageContext.request.contextPath}/static/img/epa.png" style="width: 104px; height: 32px"/>
	                </div>
	                <p class="navbar-text" style="margin: 8px 15px 0px 0px; font-size: 12px; line-height: 1">
                        United States<br/>Environmental Protection<br/>Agency
	                </p>
	            </div>
	            <div class="navbar-collapse collapse">
	                <ul class="nav navbar-nav">
						<sec:authorize var="loggedIn" access="isAuthenticated()" />
						<c:choose>
							<c:when test="${loggedIn}">
								<li id="tabHome"><a href="#!/home">Home</a></li>
							</c:when>
							<c:otherwise>
								<li id="tabHome"><a href="${pageContext.request.contextPath}/action/login">Home</a></li>
							</c:otherwise>
						</c:choose>
                        <li id="tabResources" class="dropdown">
                            <a href="#" class="" data-toggle="dropdown">Resources <span class="caret"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li>
                                    <a href="${actionBean.cgpUrls.cgpDocs}" target="_blank">EPA’s 2017 CGP and Related Documents</a>
                                    <a href="${actionBean.cgpUrls.cgpHelpCenter}" target="_blank">EPA NeT CGP Help Center</a>
                                    <a href="${actionBean.cgpUrls.npdesEReporting}" target="_blank">NPDES eReporting</a>
                                    <a href="${actionBean.cgpUrls.eEnterpriseEnvironment}" target="_blank">E-Enterprise for the Environment</a>
                                </li>
                            </ul>
                        </li>
	                </ul>
	                <ul class="nav navbar-nav navbar-right nav-help">
	                	<li id="nav-bar-context"></li>
						<sec:authorize access="isAuthenticated()">
	                	<li><p class="navbar-text">Logged in as ${actionBean.user.username}</p></li>
						<li><a href="<c:url value="/action/logout" />">Logout</a></li>

							<script type="application/javascript">
								$(function () {
									$.sessionTimeout({
										keepAlive: true,
										keepAliveInterval: 300000,
										keepAliveUrl: '${pageContext.request.contextPath}/action/secured/home',
										logoutUrl: '${pageContext.request.contextPath}/action/logout',
										redirUrl: '${pageContext.request.contextPath}/action/logout',
										ajaxType: 'GET',
										warnAfter: 1500000, // 25 minutes
										redirAfter: 1800000, // 30 minutes
										countdownMessage: 'You will be logged out in {timer} seconds.',
										countdownBar: true,
										countdownSmart: true,
										useCookie: true,
										onExtendSession: function () {
											oeca.notifications.showSuccessMessage("Your session has been extended for 30 more minutes.");
										}
									});
								});
							</script>
						</sec:authorize>
                        <li class="dropdown">
                            <a href="#" class="dropdown-toggle" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false"><span class="glyphicon glyphicon-info-sign"></span></a>
                            <ul class="dropdown-menu" role="menu">
                                <li>
                                    <span class="navbar-dropdown-text">
                                        CGP Help Desk Email:  <a href="mailto:${actionBean.cgpUrls.cgpHelpDeskEmail}?subject=CGP Help Desk Request">${actionBean.cgpUrls.cgpHelpDeskEmail}</a>
                                    </span>
                                    <span class="navbar-dropdown-text">
                                        CGP Help Desk Phone: ${actionBean.cgpUrls.cgpHelpDeskPhoneHours}
                                    </span>
                                </li>
                            </ul>
                        </li>
	                </ul>
	            </div>
			</div>
		</stripes:layout-component>
		<stripes:layout-component name="container">
			<stripes:layout-component name="container"></stripes:layout-component>
			<div id="popups"></div>
			<button id="cromerr-widget-init" style="display:none"></button>
		</stripes:layout-component>
	</stripes:layout-render>
</stripes:layout-definition>