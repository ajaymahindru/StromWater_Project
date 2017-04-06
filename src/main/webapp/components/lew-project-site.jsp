<div class="panel panel-info"  data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">Project/Site Information</a>
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
	<div class="panel-collapse" id="projectSite" data-bind="slideVisible: expand">
		<div class="panel-body">
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label" for="site-name">Project/Site Name</label>
					<input id="site-name" class="form-control" type="text" maxlength="80" data-bind="value: siteName" />
				</div>
			</div>
			<hr>
			<div class="h4">Project/Site Address</div>
			<div class="row">
				<div class="col-sm-12 col-lg-6 form-group">
					<label class="control-label" for="address-1">Address</label>
					<input id="address-1" class="form-control" type="text" maxlength="50" data-bind="value: siteAddress" />
				</div>
			</div>
			<div class="row">
				<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 form-group">
					<label class="control-label" for="city">City</label>
					<input id="city" class="form-control" type="text" maxlength="60" data-bind="value: siteCity" />
				</div>
				<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 form-group">
					<label class="control-label" for="state">State</label>
					<!-- ko ifnot: draft || submitted -->
					<select style="width: 100%" class="form-control" id="state" data-bind="lookup: 'states',
							value: siteStateCode,
							optionsText: 'stateName',
							optionsValue: 'stateCode',
							optionsCaption: '',
							valueAllowUnset: true,
							select2: {placeholder: 'Select a State'}">
					</select>
					<!-- /ko -->
					<!-- ko if: draft || submitted -->
					<div class="input-group">
						<select style="width: 100%" class="form-control" data-bind="lookup: 'states',
								value: siteStateCode,
								optionsText: 'stateName',
								optionsValue: 'stateCode',
								optionsCaption: '',
								valueAllowUnset: true,
								disable: true,
								select2: {placeholder: 'Select a State'}">
						</select>
						<div class="input-group-addon"><span class="glyphicon glyphicon-lock"></span></div>
					</div>
					<a href="JavaScript:oeca.cgp.notifications.projectStateLocked();">Why can't I edit this field?</a>
					<!-- /ko -->
				</div>
			</div>
			<div class="row">
				<div class="col-lg-2 col-md-2 col-sm-2 col-xs-6 form-group">
					<label class="control-label" for="zip">Zip</label>
					<input type="text" class="form-control" maxlength="5" data-bind="maskedZip: siteZipCode" />
				</div>
				<!-- ko if: counties().length > 0 -->
				<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-group">
					<label class="control-label" for="county">County or Similar Division</label>
					<select id="county" class="form-control" style="width: 100%" data-bind="options: counties,
															value: siteCounty,
															optionsText: 'countyName',
															optionsValue: 'countyName',
															valueAllowUnset: true,
															optionsCaption: '',
															select2: {
																placeholder: 'Select County'
															}">
					</select>
				</div>
				<!-- /ko -->
			</div>
			<hr>
			<div class="row">
				<div class="col-sm-6">
					<div class="h4">Latitude and Longitude</div>
					<span class="help-block">Click on the map to automatically find Latitude and Longitude.</span>
					<location params="location: siteLocation"></location>
				</div>
				<div class="col-sm-6" data-bind="if: expand">
					<div style="height: 250px;" data-bind="leaflet: {
																latLongMarker: {
																	latLong: siteLocation
																},
																center: {
																	city: siteCity(),
																	state: siteStateCode(),
																	lat: siteLocation.latitude(),
																	long: siteLocation.longitude()
																}
															}"></div>
				</div>
			</div>
			<button class="btn btn-primary" data-bind="click: saveAndContinue">
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