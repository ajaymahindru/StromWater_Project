<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"%>

<c:set var="rootContext" scope="page"
	value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}" />
<c:set var="regV1Api" scope="page" value="${rootContext}/oeca-svc-registration/api/registration/v1" />
<c:set var="authV1Api" scope="page" value="${rootContext}/oeca-svc-auth/api/authentication/v1/public" />

<stripes:layout-render name="/layout/cgp-layout.jsp" title="EPA CGP" tab="Home">
	<stripes:layout-component name="additionalHead">
		<script src="${pageContext.request.contextPath}/static/js/models.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/controllers.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/knockout-file-bindings.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/components.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/register.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/cgp.js"></script>
		<link href="${pageContext.request.contextPath}/static/css/cgp.css" rel="stylesheet">
		<script>
		var ViewModel = function() {
			var self = this;
			self.component = function(name, params) {
				return function(page, callback) {
					var componentDiv = $('<div></div>');
					if(params) {
						componentDiv.attr('data-bind', "component: {name: '" + name + "', params: " + params + "}");
					}
					else {
						componentDiv.attr('data-bind', "component: '" + name + "'");
					}
					$(page.element).html(componentDiv);
					callback();
				}
			}
			self.showHome = function() {
				oeca.cgp.nav.setContext('');
			}
			self.showNoi = function() {
				oeca.cgp.nav.setContext('NOI Form')
			}
			self.showType = ko.observable(true);
			
		}
			ko.validation.registerExtenders();
			pager.Href.hash = '#!/';
			viewModel = new ViewModel();
			pager.extendWithPage(viewModel);
			ko.applyBindings(viewModel);
			pager.start();
		</script>
	</stripes:layout-component>
	<stripes:layout-component name="container">
		<div id="styles-div">
			<h1>Buttons</h1>
			<h2>Default</h2>
			<div>
				<button class="btn btn-default">Default</button>
				<button class="btn btn-success">Success</button>
				<button class="btn btn-info">Info</button>
				<button class="btn btn-primary">Primary</button>
				<button class="btn btn-warning">Warning</button>
				<button class="btn btn-danger">Danger</button>
				<a class="btn">Link</a>
			</div>
			<h2>Outline</h2>
			<div>
				<button class="btn btn-primary-outline">Primary</button>
				<button class="btn btn-success-outline">Success</button>
				<button class="btn btn-info-outline">Info</button>
				<button class="btn btn-warning-outline">Warning</button>
				<button class="btn btn-danger-outline">Danger</button>
			</div>
			<hr>
			<h1>Modals</h1>
			<div>
				<a class="btn btn-success-outline" data-bind="click: successAlert">Success</a>
				<a class="btn btn-primary-outline" href="JavaScript:oeca.cgp.notifications.infoAlert('Info')">Info</a>
				<a class="btn btn-danger-outline" href="JavaScript:oeca.cgp.notifications.errorAlert('Error')">Error</a>
			</div>
		</div>
	</stripes:layout-component>
</stripes:layout-render>