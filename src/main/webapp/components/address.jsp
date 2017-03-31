<%--
	Sample full JSON:
	{
		address1: null,
		address2: null,
		city: null,
		state: null,
		zip: null,
		zip4: null,
		county: null
	}
--%>
<!-- ko with: address -->
<div class="row">
	<div class="col-sm-12 col-lg-6 form-group">
		<label class="control-label" for="address-1">Address</label>
		<input id="address-1" class="form-control" type="text" maxlength="50" data-bind="value: address1" />
	</div>
</div>
<!-- ko if: $data.address2 != undefined -->
<div class="row">
	<div class="col-sm-12 col-lg-6 form-group">
		<label class="control-label" for="address-2">Address Line 2</label>
		<input id="address-2" class="form-control" type="text" maxlength="50" data-bind="value: address2" />
	</div>
</div>
<!-- /ko -->
<div class="row">
	<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 form-group">
		<label class="control-label" for="city">City</label>
		<input id="city" class="form-control" type="text" maxlength="30" data-bind="value: city" />
	</div>
	<div class="col-lg-3 col-md-4 col-sm-4 col-xs-12 form-group">
		<label class="control-label" for="state">State</label>
		<select style="width: 100%" class="form-control" data-bind="lookup: 'states',
						value: state,
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
		<input type="text" class="form-control" maxlength="5" data-bind="maskedZip: zip" />
	</div>
	<!-- ko if: $data.zip4 != undefined -->
	<div class="col-lg-2 col-md-2 col-sm-2 col-xs-6 form-group">
		<label class="control-label" for="zip4">Ext.</label>
		<input type="text" class="form-control" maxlength="4" data-bind="value: zip4"/>
	</div>
	<!-- /ko -->
	<div class="col-lg-4 col-md-4 col-sm-4 col-xs-12 form-group">
		<label class="control-label" for="county">County or Similar Division</label>
		<select id="county" class="form-control" style="width: 100%" data-bind="lookup: {
																options: 'counties', 
																filter: {
																	value: 'stateCode',
																	by: state
																}
															},
															value: county,
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
<!-- /ko -->