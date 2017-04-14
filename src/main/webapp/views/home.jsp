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
		<!-- TODO host files locally? -->
		<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/leaflet/leaflet.css"/>
		<script src="${pageContext.request.contextPath}/static/js/leaflet/leaflet.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/leaflet/esri-leaflet.js"></script>
		<script src="${pageContext.request.contextPath}/static/js/leaflet/esri-leaflet-geocoder.js"></script>
		<script>
			var viewModel;
			oeca.cgp.noi.npdesId = "${actionBean.npdesId}";
			$(function() {
				$.cromerrWidget(oeca.cgp.defaults.cromerrSettings());
				$.when(
					loadLookup('counties')
				).done(function() {
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
						self.disposeComponent = function(page, callback) {
							if(callback) {
								$(page.element).hide();
								callback();
								$(page.element).children().each(function(index, child) {
									ko.utils.domNodeDisposal.removeNode(child);
								});
							}
						}
						self.showHome = function() {							
							oeca.cgp.nav.setContext('');
							$('#container').removeClass('container').addClass('container-fluid');
							postal.channel('nav').publish('home.show');
						}
						self.hideHome = function(page, callback) {
							$('#container').removeClass('container-fluid').addClass('container');
						    //self.disposeComponent(page, callback)
						}
						self.showNoi = function() {
							oeca.cgp.nav.setContext('NOI Form');
						}
						self.showNoiCor = function() {
						    self.showNoi();
						    self.viewCor = 'true';
						}
						self.showLew = function() {
							oeca.cgp.nav.setContext('LEW Form');
						}
						self.showLewCor = function() {
						    self.showLew();
						    self.viewCor = 'true';
						}
						self.showNot = function() {
						    oeca.cgp.nav.setContext('NOT Form')
						}
						self.showNotCor = function() {
						    self.showNot();
						    self.viewCor = 'true';
						}
						self.showType = ko.observable(true);
					}
					ko.validation.registerExtenders();
					pager.Href.hash = '#!/';
					viewModel = new ViewModel();
					pager.extendWithPage(viewModel);
					ko.applyBindings(viewModel);
					pager.start();
				})
			});
		</script>
		<script type="text/html" id="user-search-preparer-confirm-help">
			Wrong Preparer?  <a href="JavaScript:" data-bind="click: showResults">Return to Preparer Information</a>.
		</script>
		<script type="text/html" id="user-search-preparer-results-help">
			Can't find your Preparer?  <a href="JavaScript:" data-bind="click: showSearch">Return to search page</a> or <a href="JavaScript:" data-bind="click: showInvite">invite your preparer to join CDX</a>.
		</script>
		<script type="text/html" id="user-search-certifier-confirm-help">
			Wrong Certifier?  <a href="JavaScript:" data-bind="click: showResults">Return to Certifier Information</a>.
		</script>
		<script type="text/html" id="user-search-certifier-results-help">
			Can't find your Certifier?  <a href="JavaScript:" data-bind="click: showSearch">Return to search page</a> or <a href="JavaScript:" data-bind="click: showInvite">invite your certifier to join CDX</a>.
		</script>
		<script type="text/html" id="user-search-help-desk-results-help">
			<a href="JavaScript:" data-bind="click: showSearch">Return to search page</a>
		</script>
		<script type="text/html" id="user-search-help-desk-confirm-help">
			<a href="JavaScript:" data-bind="click: showSearch">Return to search results</a>
		</script>
	</stripes:layout-component>
	<stripes:layout-component name="container">
		<div class="dashboard" data-bind="page: {id: 'home', role: 'start', afterShow: showHome, afterHide: hideHome}">
			<dashboard></dashboard>
		</div>
		<div id="noi-form-page" data-bind="page: {id: 'noi', params: ['formId']}">
			<div data-bind="page: {id: 'edit', role: 'start', sourceOnShow: component('noi', '{id: formId}'), afterShow: showNoi, hideElement: disposeComponent}">
				
			</div>
			<div data-bind="page: {id: 'view', sourceOnShow: component('noi-view', '{id: formId}'), afterShow: showNoi, hideElement: disposeComponent}">

			</div>
			<div data-bind="page: {id: 'cor', sourceOnShow: component('noi-view', '{id: formId}'), afterShow: showNoiCor, hideElement: disposeComponent}">

			</div>
		</div>
		<div data-bind="page: {id: 'lew', params: ['formId']}">
			<div data-bind="page: {id: 'edit', role: 'start', sourceOnShow: component('lew', '{id: formId}'), afterShow: showLew, hideElement: disposeComponent}">
			
			</div>
			<div data-bind="page: {id: 'view', sourceOnShow: component('lew-view', '{id: formId}'), afterShow: showLew, hideElement: disposeComponent}">

			</div>
			<div data-bind="page: {id: 'cor', sourceOnShow: component('lew-view', '{id: formId}'), afterShow: showLewCor, hideElement: disposeComponent}">

			</div>
		</div>
		<div data-bind="page: {id: 'not', params: ['formId']}">
			<div data-bind="page: {id: 'view', role: 'start', sourceOnShow: component('not', '{id: formId}'), afterShow: showNot, hideElement: disposeComponent}">

			</div>
			<div data-bind="page: {id: 'cor', sourceOnShow: component('not', '{id: formId}'), afterShow: showNotCor, hideElement: disposeComponent}">

			</div>
		</div>
	</stripes:layout-component>
</stripes:layout-render>