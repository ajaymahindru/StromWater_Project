<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">Certification Information</a>
				<!-- ko if: expand -->
				<span class="glyphicon glyphicon-chevron-down pull-right"></span>
				<!-- /ko -->
				<!-- ko ifnot: expand -->
				<span class="glyphicon glyphicon-chevron-up pull-right"></span>
				<!-- /ko -->
			</a>
		</div>
	</div>
	<div class="panel-collapse" id="certificationInformation" data-bind="slideVisible: expand">
		<div class="panel-body">
			<div class="row">
				<div class="col-sm-12">
					<div data-bind="if: showOptions">
						<div class="h4">What would you like to do now?</div>
						<security:authorize access="hasRole('ROLE_CGP_PREPARER')">
							<p>You can route your form to another preparer for additional edits or route your form to a certifier for submission to EPA.</p>
						</security:authorize>
						<security:authorize access="hasRole('ROLE_CGP_CERTIFIER')">
							<p>You can sign and submit your form to EPA by clicking Certify form, or route your form to another user by clicking route to certifier or route to preparer.</p>
						</security:authorize>
						<div class="radio">
							<label>
								<input type="radio" name="certify-action" data-bind="checkedValue: 'route-certifier', checked: action"/>Route form to certifier
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="certify-action" data-bind="checkedValue: 'route-preparer', checked: action"/>Route form to preparer
							</label>
						</div>
						<security:authorize access="hasRole('ROLE_CGP_CERTIFIER')">
							<div class="radio">
								<label>
									<input type="radio" name="certify-action" data-bind="checkedValue: 'certify', checked: action"/>Certify Form
								</label>
							</div>
							<!-- ko if: formData.operatorInformation.preparer().userId() != oeca.cgp.currentUser.username -->
							<div class="radio">
								<label>
									<input type="radio" name="reject-action" data-bind="checkedValue: 'reject', checked: action"/>Reject form and send back to preparer
								</label>
							</div>
							<!-- /ko -->
						</security:authorize>
						<button class="btn btn-primary" data-bind="enable: action, click: saveAndPickAction">Next</button>
						<div style="height: 150px"></div>
					</div>
					<div data-bind="if: showRouteCertifier">
						<user-search params="callback: confirmCertifier,
											type: 'Certifier',
											helpText: {
												results: 'user-search-certifier-results-help',
												confirm: 'user-search-certifier-confirm-help'
											},
											id: 'certification-route'"></user-search>
						<p><a href="JavaScript:" data-bind="click: showOptions">Return to the options page</a></p>
					</div>
					<div data-bind="if: showRoutePreparer">
						<user-search params="callback: confirmPreparer,
											type: 'Preparer',
											helpText: {
												results: 'user-search-preparer-results-help',
												confirm: 'user-search-preparer-confirm-help'
											},
											id: 'preparer-route'"></user-search>
						<p><a href="JavaScript:" data-bind="click: showOptions">Return to the options page</a></p>
					</div>
					<security:authorize access="hasRole('ROLE_CGP_CERTIFIER')">
						<div data-bind="if: showCertify">
							<button class="btn btn-primary" data-bind="cromerr: {callback: certify, validate: validateCertification}">Certify</button>
							<p><a href="JavaScript:" data-bind="click: showOptions">Return to the options page</a></p>
						</div>
						<div data-bind="if: showReject">
							<div class="form-group">
								<label class="control-label" for="rejection-reason">Reason for Rejection:</label>
								<textarea class="form-control" id="rejection-reason" data-bind="value: rejectionReason"></textarea>
							</div>
							<button class="btn btn-danger" data-bind="click: reject">Reject Form and send back to preparer</button>
							<p><a href="JavaScript:" data-bind="click: showOptions">Return to the options page</a></p>
						</div>
					</security:authorize>
				</div>
			</div>
		</div>
	</div>
</div>