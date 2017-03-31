<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes.tld" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<stripes:layout-definition>
	<c:choose>
		<c:when test="${type == 'danger'}">
			<c:set var="modalClass" value="bootstrap-dialog type-danger oeca-error-alert"/>
		</c:when>
		<c:when test="${type == 'info'}">
			<c:set var="modalClass" value="bootstrap-dialog type-info oeca-info-alert"/>
		</c:when>
		<c:when test="${type == 'success'}">
			<c:set var="modalClass" value="bootstrap-dialog type-success oeca-success-alert"/>
		</c:when>
	</c:choose>
	<div class="modal fade ${modalClass}" tabindex="-1" role="dialog" data-bind="BSModal: open" id="${id}" aria-label="${title}">
		<div class="modal-dialog ${addClass}">
			<div class="modal-content">
				<div class="modal-header ${headerClass }">
					<stripes:layout-component name="header">
						<button type="button" class="close" data-bind="click: function() {closeModal(false)}" aria-label="Close"><span aria-hidden="true">&times;</span></button>
						<div class="modal-title bootstrap-dialog-title h4">${title}</div>
					</stripes:layout-component>
					<div data-bind="with: popupModel">
					<stripes:layout-component name="modalProgressBar" >
						
					</stripes:layout-component>
					</div>
				</div>
				<div class="modal-body" data-bind="with: popupModel">
					<stripes:layout-component name="body">
					
					</stripes:layout-component>
				</div>					
				<div class="modal-footer">
					<stripes:layout-component name="footer">
						
					</stripes:layout-component>
				</div>
			</div>
		</div>
	</div>
</stripes:layout-definition>