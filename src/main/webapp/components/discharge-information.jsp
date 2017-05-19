<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">Discharge Information</a>
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
	<script type="text/html" id="discharge-point-edit">
		<div class="well" data-bind="with: data">
			<div class="row">
				<div class="col-sm-3 form-group">
					<label class="control-label" for="discharge-id">Point of Discharge ID</label>
					<!-- ko if: $data.isNew -->
					<input type="text" id="discharge-id" class="form-control" maxlength="3" style="width: 70px"
						   data-bind="value: id"/>
					<!-- /ko -->
					<!-- ko ifnot: $data.isNew -->
					<span id="discharge-id" class="static-control" data-bind="text: id"></span>
					<!-- /ko -->
				</div>
				<div class="col-sm-9 form-group">
					<label class="control-label" for="receiving-water">Receiving Water</label>
					<dfn data-bind="popover: oeca.cgp.definitions.receivingWater"><span class="glyphicon glyphicon-info-sign"></span></dfn>
					<input type="text" class="form-control" id="receiving-water" data-bind="value: firstWater().receivingWaterName" maxlength="50">
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label" for="discharge-description">Description (optional)</label>
					<textarea class="form-control" id="discharge-description" data-bind="value: description" maxlength="100"></textarea>
				</div>
			</div>
			<!-- ko if: $parent.waterWillReset($data) -->
				<div class="alert alert-warning">
					This receiving water is a new receiving and will cause your 14 day hold period to reset.
				</div>
			<!-- /ko -->
			<hr>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label">Is this receiving water impaired (on the CWA 303(d) list)?</label>
					<div>
						<span class="validationMessage" data-bind="validationMessage: $parent.waterImpairedQuestion"></span>
					</div>
					<div class="radio" data-bind="validationOptions: {insertMessages: false}">
						<label>
							<input type="radio" name="receiving-water-impaired"
								   data-bind="checked: $parent.waterImpairedQuestion, checkedValue: true,
												attr: {name: 'receiving-water-impaired' + $parent.uniqueId}"/> Yes
						</label>
					</div>
					<div class="radio" data-bind="validationOptions: {insertMessages: false}">
						<label>
							<input type="radio" name="receiving-water-impaired"
								   data-bind="checked: $parent.waterImpairedQuestion, checkedValue: false,
												attr: {name: 'receiving-water-impaired' + $parent.uniqueId}"/> No
						</label>
					</div>
				</div>
			</div>
			<!-- ko if: $parent.waterImpairedQuestion() -->
			<%--
				Validations here are weird.  Since we have two fields that map into one field instead of using
				the normal messaging system we look for a code in the validation messages and use that to
				display the actual message.
			 --%>
			<!-- ko if: errors().contains('impaired required') && errors.isAnyMessageShown() -->
			<div>
				<span class="validationMessage">At least one impaired pollutant is required</span>
			</div>
			<!-- /ko -->
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label" for="pollutants">List the pollutant(s) that are causing the impairment</label>
					<select id="pollutants" class="form-control" multiple style="width: 100%;"
							data-bind="select2: {
			                				placeholder: 'Select Pollutant(s)',
											ajax: {
												url: '${pageContext.request.contextPath}/api/lookup/v1/pollutants',
												dataType: 'json',
												delay: 250,
												data: function(params) {
													return {
														criteria: params.term
													}
												},
												processResults: function(data, params) {
													return {
														results: $.map(data, function(pollutant) {
															pollutant.text = pollutant.pollutantName;
															pollutant.id = pollutant.pollutantName;
															return pollutant;
														})
													}
												}
											},
											minimumInputLength: 3
										}, select2AjaxValue: {
                                            value: $parent.impairedPollutants,
                                            model: PollutantController,
                                            compare: function(toRemove, item) {
                                                return toRemove.id == item.pollutantName();
                                            }
                                        }, foreach: $parent.preSelectedImpairedPollutants">
						<!--
                            For each through the currently selected options so that select2 will show them as already
                            selected.  There doesn't appear to be any api method in select2 to do this.
                        -->
						<option data-bind="text: pollutantName, attr: {value: pollutantName}" selected></option>
					</select>
				</div>
			</div>
			<!-- /ko -->
			<hr>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label">Has a TMDL been completed for this receiving waterbody?</label>
					<div>
						<span class="validationMessage" data-bind="validationMessage: $parent.tmdlQuestion"></span>
					</div>
					<div class="radio" data-bind="validationOptions: {insertMessages: false}">
						<label>
							<input type="radio" name="receiving-water-tmdl"
								   data-bind="checked: $parent.tmdlQuestion, checkedValue: true,
												attr: {name: 'receiving-water-tmdl' + $parent.uniqueId}"/> Yes
						</label>
					</div>
					<div class="radio" data-bind="validationOptions: {insertMessages: false}">
						<label>
							<input type="radio" name="receiving-water-tmdl"
								   data-bind="checked: $parent.tmdlQuestion, checkedValue: false,
												attr: {name: 'receiving-water-tmdl' + $parent.uniqueId}"/> No
						</label>
					</div>
				</div>
			</div>
			<!-- ko if: $parent.tmdlQuestion() -->
				<h4>TMDLS</h4>
				<%--
							Validations here are weird.  Since we have two fields that map into one field instead of using
							the normal messaging system we look for a code in the validation messages and use that to
							display the actual message.
						 --%>
				<!-- ko if: errors().contains('tmdl required') && errors.isAnyMessageShown() -->
				<div>
					<span class="validationMessage">At least one TMDL is required</span>
				</div>
				<!-- /ko -->
				<!-- ko foreach: $parent.tmdls -->
				<div class="row">
					<div class="col-sm-3 form-group">
						<label class="control-label" for="tmdl-id">TMDL ID</label>
						<input type="text" class="form-control" id="tmdl-id" data-bind="value: id" maxlength="6"/>
					</div>
					<div class="col-sm-3 form-group">
						<label class="control-label" for="tmdl-name">TMDL Name</label>
						<input type="text" class="form-control" id="tmdl-name" data-bind="value: name" maxlength="100"/>
					</div>
					<div class="col-sm-5 form-group">
						<label class="control-label">Pollutants</label>
						<select id="pollutants" class="form-control" multiple style="width: 100%;"
								data-bind="select2: {
			                				placeholder: 'Select Pollutant(s)',
											ajax: {
												url: '${pageContext.request.contextPath}/api/lookup/v1/pollutants',
												dataType: 'json',
												delay: 250,
												data: function(params) {
													return {
														criteria: params.term
													}
												},
												processResults: processPollutants
											},
											minimumInputLength: 3
										}, select2AjaxValue: {
                                            value: pollutants,
                                            model: PollutantController,
                                            compare: function(toRemove, item) {
                                                return toRemove.id == item.pollutantName();
                                            }
                                        }, foreach: originalPollutants">
							<!--
                                For each through the currently selected options so that select2 will show them as already
                                selected.  There doesn't appear to be any api method in select2 to do this.
                            -->
							<option data-bind="text: pollutantName, attr: {value: pollutantName}" selected></option>
						</select>
					</div>
                    <div class="col-sm-1 form-group">
                        <label class="control-label">Remove</label>
                        <span class="glyphicon glyphicon-remove text-danger form-control-static" style="cursor: pointer"
                              title="Remove TMDL" data-bind="click: $parents[1].removeTmdl"></span>
                    </div>
				</div>
				<!-- /ko -->
				<!-- ko if: $parent.tmdls().length == 0 -->
					<p>No TMDLs have been added for this waterbody</p>
				<!-- /ko -->
			<hr>
			<!-- ko if: $parent.showNewTmdl -->
			<div class="row" data-bind="if: $parent.newTmdl">
					<div class="col-sm-3 form-group">
						<label class="control-label" for="tmdl-id-new">TMDL ID</label>
						<input type="text" class="form-control" id="tmdl-id-new" data-bind="value: $parent.newTmdl().id" maxlength="6"/>
					</div>
					<div class="col-sm-3 form-group">
						<label class="control-label" for="tmdl-name-new">TMDL Name</label>
						<input type="text" class="form-control" id="tmdl-name-new" data-bind="value: $parent.newTmdl().name" maxlength="100"/>
					</div>
					<div class="col-sm-6 form-group">
						<label class="control-label">Pollutants</label>
						<select id="pollutants" class="form-control" multiple style="width: 100%;"
								data-bind="select2: {
			                				placeholder: 'Select Pollutant(s)',
											ajax: {
												url: '${pageContext.request.contextPath}/api/lookup/v1/pollutants',
												dataType: 'json',
												delay: 250,
												data: function(params) {
													return {
														criteria: params.term
													}
												},
												processResults: $parent.newTmdl().processPollutants
											},
											minimumInputLength: 3
										}, select2AjaxValue: {
                                            value: $parent.newTmdl().pollutants,
                                            model: PollutantController,
                                            compare: function(toRemove, item) {
                                                return toRemove.id == item.pollutantName();
                                            }
                                        }">
						</select>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12">
						<button class="btn btn-primary" data-bind="click: $parent.addTmdl">Add this TMDL</button>
						<button class="btn btn-default pull-right" data-bind="click: $parent.discardTmdl">Discard this TMDL</button>
					</div>
				</div>
				<!-- /ko -->
				<!-- ko ifnot: $parent.showNewTmdl -->
				<div class="row">
					<div class="col-sm-12">
						<button class="btn btn-primary" data-bind="click: function(){$parent.showNewTmdl(true)}">Add another TMDL</button>
					</div>
				</div>
				<!-- /ko -->
			<!-- /ko -->
			<hr>
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">Tier Designation</label>
                    <!-- ko if: $parent.enableTier -->
						<div>
							<span class="validationMessage" data-bind="validationMessage: tier"></span>
						</div>
                        <div class="radio" data-bind="validationOptions: {insertMessages: false}">
                            <label>
								<input type="radio" data-bind="checkedValue: 'Tier_2', checked: tier,
																attr: {name: 'water-tier-designation' + $parent.uniqueId}"/>
								Tier 2
							</label>
                        </div>
                        <div class="radio" data-bind="validationOptions: {insertMessages: false}">
                            <label>
								<input type="radio" data-bind="checkedValue: 'Tier_2_5', checked: tier,
																attr: {name: 'water-tier-designation' + $parent.uniqueId}"/>
								Tier 2.5
							</label>
                        </div>
                        <div class="radio" data-bind="validationOptions: {insertMessages: false}">
                            <label>
								<input type="radio" data-bind="checkedValue: 'Tier_3', checked: tier,
																attr: {name: 'water-tier-designation' + $parent.uniqueId}"/>
								Tier 3
							</label>
                        </div>
                        <div class="radio" data-bind="validationOptions: {insertMessages: false}">
                            <label>
								<input type="radio" data-bind="checkedValue: 'NA', checked: tier,
																attr: {name: 'water-tier-designation' + $parent.uniqueId}"/>
								N/A
							</label>
                        </div>
                    <!-- /ko -->
                    <!-- ko ifnot: $parent.enableTier -->
                        <span class="static-info">N/A
							<dfn data-bind="popover: oeca.cgp.definitions.tierNA"><span class="glyphicon glyphicon-question-sign"></span></dfn>
						</span>
                    <!-- /ko -->
				</div>
			</div>
			<!-- ko ifnot: $data.isNew -->
			<div class="row">
				<div class="col-sm-12">
					<button class="btn btn-primary" data-bind="click: $parent.done">Done</button>
				</div>
			</div>
			<!-- /ko -->
		</div>
	</script>
	<div class="panel-collapse" id="dischargeInformation" data-bind="slideVisible: expand">
		<div class="panel-body">
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">Does your project/site discharge stormwater into a Municipal Separate Storm
						Sewer System (MS4)?</label>
					<div class="radio">
						<label>
							<input type="radio" name="discharge-ms4-system"
								data-bind="checkedValue: true, checked: dischargeMunicipalSeparateStormSewerSystem" />
							Yes
						</label>
					</div>
					<div class="radio">
						<label>
							<input type="radio" name="discharge-ms4-system"
								data-bind="checkedValue: false, checked: dischargeMunicipalSeparateStormSewerSystem" />
							No
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">Are there any waters of the U.S. within 50 feet of your project's earth
						disturbances?</label>
					<div class="radio">
						<label>
							<input type="radio" name="disturbances-within-50feet"
								data-bind="checkedValue: true, checked: dischargeUSWatersWithin50Feet" />
							Yes
						</label>
					</div>
					<div class="radio">
						<label>
							<input type="radio" name="disturbances-within-50feet"
								data-bind="checkedValue: false, checked: dischargeUSWatersWithin50Feet" />
							No
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">
						Are any of the waters of the U.S. to which you discharge designated by the state or tribal authority under its
						antidegradation policy as a Tier 2 (or Tier 2.5) water (water quality exceeds levels necessary to support
						propagation of fish, shellfish, and wildlife and recreation in and on the water) or as a Tier 3 water (Outstanding
						National Resource Water)? <a href="${actionBean.cgpUrls.appendixF}" target="_blank">See Appendix F</a>
					</label>
					<div class="radio">
						<label>
							<input type="radio" name="tier-2-or-tier-3" data-bind="checkedValue: true, checked: dischargeAllowable" />
							Yes
						</label>
					</div>
					<div class="radio">
						<label>
							<input type="radio" name="tier-2-or-tier-3" data-bind="checkedValue: false, checked: dischargeAllowable" />
							No
						</label>
					</div>
				</div>
			</div>
			<hr>
			<div class="form-group required">
				<div class="h4 control-label">Receiving Waters Information:</div>
				<span class="validationMessage" data-bind="validationMessage: dischargePoints"></span>
			</div>
			<p><strong>List all of the stormwater points of discharge from your facility.</strong> Each point of
				discharge must be identified by a unique 3-digit ID (e.g., 001, 002). Note that this information does
				not need to be updated in the NOI if the points of discarge change during the project.</p>
			<!-- ko if: change() -->
			<div class="alert alert-warning">
				Adding or editing any discharge in this section that causes a new receiving water to be added to the form will reset your 14-day hold period.
				<!-- ko ifnot: waitListEdit -->
				If you want to edit this section click the edit button.
				<button href="JavaScript:" class="btn btn-sm btn-default" data-bind="click: function(data, event) {oeca.cgp.noi.waitListWarning(data, event, 'the discharges resulting in a new receiving water being sent to the EPA')}">Edit</button>
				<!-- /ko -->
			</div>
			<!-- /ko -->
			<fieldset data-bind="disable: change() && !waitListEdit()">
				<!-- ko if: dischargePoints().length > 0 -->
				<div>
					<%--  It seems knockout cannot remove a table initialized as a datatable with an if unless you wrap
							that object in a div --%>
					<table id="discharges" class="table table-bordered table-condensed dataTable responsive no-wrap" style="width: 100%" data-bind="datatable: {
									responsive: {
										details: false
									},
									columns: [
										{
											className: 'details-control',
											orderable: false,
											data: null,
											render: $.fn.dataTable.render.ko.templateInline(
														'<span class=&quot;glyphicon glyphicon-plus-sign&quot;></span> ' +
														'<!-- ko if: data.errors().length > 0 -->' +
														'<span class=&quot;label label-danger&quot; data-bind=&quot;text: data.errors().length&quot;></span>' +
														'<!-- /ko --><!-- ko if: data.errors().length == 0 -->' +
														'<span class=&quot;label label-success&quot;>' +
															'<span class=&quot;glyphicon glyphicon-ok&quot;></span>' +
														'</span>' +
														'<!-- /ko -->'
											),
											responsivePriority: 1,
											width: '25px'
										},
										{
											name: 'id',
											orderable: true,
											data: 'id',
											responsivePriority: 1,
											render: $.fn.dataTable.render.ko.observable()
										},
										{
											name: 'description',
											orderable: true,
											data: 'description',
											responsivePriority: 1,
											render: $.fn.dataTable.render.ko.observable()
										},
										{
											name: 'recievingWater',
											orderable: true,
											data: null,
											responsivePriority: 10,
											className: 'desktop',
											render: $.fn.dataTable.render.ko.computed(function(data) {
												return data.firstWater() !== null ? data.firstWater().receivingWaterName()
																					: '';
											})
										},
										{
											name: 'tier',
											orderable: true,
											data: 'tierDisplay',
											responsivePriority: 20,
											className: 'desktop',
											render: $.fn.dataTable.render.ko.observable()
										},
										{
											name: 'actions',
											orderable: false,
											data: null,
											render: $.fn.dataTable.render.ko.actions([
												{
													name: 'Edit',
													action: editDischarge,
													cssClass: 'btn-primary-outline btn-xs'
												},
												{
													name: 'Delete',
													action: deleteDischarge,
													cssClass: 'btn-danger-outline btn-xs'
												}
											], '#discharges')
										}
									],
									order: [[1, 'asc']],
									dom: '<\'pull-right\'f><t><\'col-sm-8\'i><\'col-sm-2\'l><\'pull-right\'p>'
								}">
						<thead>
							<tr>
								<th></th>
								<th>ID</th>
								<th>Description</th>
								<th>Receiving Water</th>
								<th>Tier Designation</th>
								<th>Actions</th>
							</tr>
						</thead>
						<tbody data-bind="datasource: dischargePoints, childRow: {
																name: 'discharge-point-edit',
																data: {
																	enableTier: dischargeAllowable,
																	waterWillReset: waterWillReset,
																	isAdded: true
																},
																vm: DischargePointChildRow,
																allowMultiple: false
															}">
						</tbody>
					</table>
				</div>
				<!-- /ko -->
				<!-- ko ifnot: dischargePoints().length > 0 -->
				<p>No discharge points have been added yet.  Click <a href="JavaScript:" data-bind="click: createDischarge">New Discharge Point</a> to add one.</p>
				<!-- /ko -->
				<!-- ko ifnot: newDischargeVM -->
					<button class="btn btn-primary" data-bind="click: createDischarge">New Discharge Point</button>
				<!-- /ko -->
				<!-- ko if: newDischargeVM -->
                    <div data-bind="template: {name: 'discharge-point-edit', data : newDischargeVM}">
                    </div>
					<button class="btn btn-primary" data-bind="click: function(){addDischarge()}">Save and Add This Discharge Point</button>
					<button class="btn btn-primary-outline" data-bind="click: addDischargeAndDuplicate">Save and Duplicate This Discharge Point</button>
					<button class="btn btn-default pull-right" data-bind="click: discardDischarge">Discard Discharge</button>
				<!-- /ko -->
			</fieldset>
			<hr>
			<button class="btn btn-primary" data-bind="click: saveAndContinue"
					onclick="oeca.utils.scrollToTop();">
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