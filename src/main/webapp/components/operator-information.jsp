<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">Operator Information</a>
			<!-- ko if: expand -->
				<span class="glyphicon glyphicon-chevron-down pull-right"></span>
			<!-- /ko -->
			<!-- ko ifnot: expand -->
				<span class="glyphicon glyphicon-chevron-up pull-right"></span>
			<!-- /ko -->
			<!-- ko if: $data.errors && errors.isAnyMessageShown() -->
			<span class="label label-danger" title="This section has validation errors.">
				<span class="glyphicon glyphicon-alert"></span>
				<span data-bind="text: errors().length"></span>
			</span>
			<!-- /ko -->
			<!-- ko if: errors().length == 0-->
			<span class="label label-primary" title="This section has validation errors.">
				<span class="glyphicon glyphicon-ok" title="No errors"></span>
			</span>
			<!-- /ko -->
		</div>
	</div>
	<div class="panel-collapse" id="operatorInfo" data-bind="slideVisible: expand">
		<div class="panel-body">
			<div class="h4">Operator Name</div>
			<div class="row">
				<div class="col-sm-6 form-group">
					<label class="control-label" for="operator-name">
						Operator Name
					</label>
					<!-- ko ifnot: isChange -->
					<input id="operator-name" class="form-control" type="text" maxlength="80" data-bind="value: operatorName"/>
					<!-- /ko -->
					<!-- ko if: isChange -->
						<div class="input-group">
							<input id="operator-name" class="form-control" type="text" data-bind="value: operatorName" disabled/>
							<div class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></div>
						</div>
						<a href="JavaScript:oeca.cgp.notifications.operatorNameLocked();">Why can't I edit this field?</a>
					<!-- /ko -->
				</div>
			</div>
			<hr>
			<div class="h4">Operator Mailing Address</div>
			<div class="row">
				<div class="col-sm-12 col-lg-6 form-group">
					<label class="control-label" for="address-1">Address</label>
					<input id="address-1" class="form-control" type="text" maxlength="50" data-bind="value: operatorAddress" />
				</div>
			</div>
			<div class="row">
				<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 form-group">
					<label class="control-label" for="city">City</label>
					<input id="city" class="form-control" type="text" maxlength="30" data-bind="value: operatorCity" />
				</div>
				<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 form-group">
					<label class="control-label" for="state">State</label>
					<select style="width: 100%" class="form-control" id="state" data-bind="lookup: 'states',
							value: operatorStateCode,
							optionsText: 'stateName',
							optionsValue: 'stateCode',
							optionsCaption: '',
							valueAllowUnset: true,
							select2: {placeholder: 'Select a State'}">
					</select>
				</div>
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-2 col-xs-6 form-group">
					<label class="control-label" for="zip">Zip</label>
					<input type="text" class="form-control" maxlength="5" data-bind="maskedZip: operatorZipCode" />
				</div>
				<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-group" data-bind="slideVisible: counties().length > 0">
					<label class="control-label" for="county">County or Similar Division</label>
					<select id="county" class="form-control" style="width: 100%" data-bind="options: counties,
															value: operatorCounty,
															optionsText: 'countyName',
															optionsValue: 'countyName',
															valueAllowUnset: true,
															optionsCaption: '',
															select2: {
																placeholder: 'Select County'
															}">
					</select>
				</div>
			</div>
			<hr>
			<div class="h4">
				Operator Point of Contact Information
			</div>
			<contact-info params="contact: {
								firstName: operatorPointOfContact.firstName,
								middleInitial: operatorPointOfContact.middleInitial,
								lastName: operatorPointOfContact.lastName,
								title: operatorPointOfContact.title,
								phone: operatorPointOfContact.phone,
								phoneExtension: operatorPointOfContact.phoneExtension,
								email: operatorPointOfContact.email
							}"></contact-info>
			<div>
				<button class="btn btn-primary" data-bind="click: saveAndContinue" onclick="oeca.utils.scrollToTop();">
					<!-- ko if: showSaveButton -->
						Save Section
					<!-- /ko -->
					<!-- ko ifnot: showSaveButton -->
						Next Section
					<!-- /ko -->
				</button>
			</div>
		</div>
	</div>
</div>