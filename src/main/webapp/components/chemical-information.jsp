<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript"> Chemical Treatment Information </a>
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
	<div class="panel-collapse" id="chemicalInformation" data-bind="slideVisible: expand">
		<div class="panel-body">
			<!-- ko if: change() -->
				<div class="alert alert-warning">
					Editing this section will reset your 14-day hold period.
					<!-- ko ifnot: waitListEdit -->
					If you want to edit this section click the edit button.
					<button href="JavaScript:" class="btn btn-sm btn-default" data-bind="click: function(data, event) {oeca.cgp.noi.waitListWarning(data, event, 'the Chemical Treatment Information section')}">Edit</button>
					<!-- /ko -->
				</div>
			<!-- /ko -->
			<fieldset data-bind="disable: change() && !waitListEdit()">
				<!-- ko if: cationicTreatmentChemicals -->
				<p>During the Permit Information section of this form you indicated that you have been authorized to use cationic
					treatment chemicals by your applicable EPA Regional Office.</p>
				<div class="row">
					<div class="col-sm-12 form-group">
						<label class="control-label" for="treatment-chemicals-used">Please indicate the treatment chemicals that
							you will use:</label>
						<select id="treatment-chemicals-used" class="form-control"
								data-bind="selectedOptions: treatmentChemicals,
											valueAllowUnset: true,
											select2: {
												ajax: {
													url: function(params) {
														return oeca.cgp.ctx + '/api/lookup/v1/chemicals/' + params.term;
													},
													processResults: function(data) {
														var options = [];
														for(var i = 0; i < data.length; ++i) {
															var chemical = data[i];
															options.push({
																id: chemical.name,
																text: chemical.name
															});
														}
														console.log(options);
														return {
															results: options
														}
													}
												},
												minimumInputLength: 3,
												closeOnSelect: false,
												tags: true
											}, foreach: preSelectedChemicals" style="width: 100%" multiple>
							<option data-bind="text: $data, attr: {value: $data}" selected></option>
						</select>
						<span class="help-block">Enter all treatment chemicals you will use.  To enter a treatment chemical type the name and then press enter or select one of the suggested names.</span>
					</div>
				</div>
				<hr>
				<!-- /ko -->
				<p>During the Permit Information section of this form, you indicated that you have been authorized to use
					cationic treatment chemicals by your applicable EPA Regional Office, attach a copy of your authorization letter and
					include documentation of the appropriate controls and implementation procedures designed to ensure that your use of
					cationic treatment chemicals will not lead to a violation of water quality standards.
					<span class="glyphicon glyphicon-asterisk" style="color: #d00; font-size: 12px"></span>
				</p>
				<div class="h4">Current Attachments</div>
				<!-- ko if: attachments().length > 0 -->
				<table class="table table-striped table-bordered table-condensed dataTable responsive no-wrap">
					<thead>
						<tr>
							<th>Name</th>
							<th>Created Date</th>
							<th>Size</th>
							<th></th>
						</tr>
					</thead>
					<tbody data-bind="foreach: attachments">
						<tr>
							<td>
								<a data-bind="attr: {href: '${pageContext.request.contextPath}/action/secured/attachment/' + id()}">
									<span class="fa fa-download"></span> <span data-bind="text: name"></span>
								</a>
							</td>
							<td data-bind="text: oeca.cgp.utils.formatDateTime(createdDate)"></td>
							<td data-bind="text: sizeDisplay"></td>
							<td><a href="JavaScript:" class="fa fa-remove" data-bind="click: $parent.removeAttachment"></a></td>
						</tr>
					</tbody>
				</table>
				<!-- /ko -->
				<!-- ko ifnot: attachments().length > 0 -->
				<span class="help-block">No attachments uploaded.</span>
				<!-- /ko -->
				<file-input params="id: 'cationic-letter', value: pendingAttachment"></file-input>
				<span class="validationMessage" data-bind="validationMessage: attachments"></span>
				<p class="help-block">Note: You are ineligible for coverage under this permit unless you notify your applicable
					EPA Regional Office in advance, and the EPA office authorizes coverage under this permit after you have included
					appropriate controls and implementation procedures designed to ensure that your use of cationic treatment chemicals
					will not lead to a violation of water quality standards.</p>
				<button class="btn btn-primary" data-bind="click: saveAndContinue"
						onclick="oeca.utils.scrollToTop();">
					<!-- ko if: showSaveButton -->
					Save Section
					<!-- /ko -->
					<!-- ko ifnot: showSaveButton -->
					Next Section
					<!-- /ko -->
				</button>
			</fieldset>
		</div>
	</div>
</div>