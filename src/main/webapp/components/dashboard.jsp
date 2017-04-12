<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<sec:authorize access="hasRole('${actionBean.regAuthRole}')">
	<script type="text/html" id="table-actions">
		<!-- ko with: data -->
		<!-- ko if: phase() == 'New'  || phase() == 'Change' || phase() == 'Terminate' -->
		<!-- ko if: status() == 'Submitted' || status() == 'OnHold' -->
		<div class="help-block">
			<div class="btn-group btn-group-xs">
				<button class="btn btn-primary-outline" data-bind="click: $parent.viewAction"><span class="fa fa-search"></span> View</button>
				<!-- ko if: status() == 'Submitted' -->
				<button class="btn btn-warning-outline" data-bind="click: $parent.holdAction"><span class="fa fa-hand-paper-o"></span> Hold</button>
				<!-- /ko -->
				<!-- ko if: status() == 'OnHold' -->
				<button class="btn btn-success-outline" data-bind="click: $parent.releaseAction"><span class="fa fa-check"></span> Release</button>
				<!-- /ko -->
				<button class="btn btn-danger-outline" data-bind="click: $parent.denyAction"><span class="fa fa-times"></span> Reject</button>
			</div>
            <div class="progress">
                <div class="progress-bar"
                     data-bind="attr: {
																'aria-valuenow': moment().format('X'),
																'aria-valuemin': moment(certifiedDate()).format('X'),
																'aria-valuemax': moment(reviewExpiration()).format('X'),
															},
															style: {
																width: holdPeriodPercent() + '%'
															}">
                    <!-- ko if: holdPeriodPercent() >= 50 -->
                    <span data-bind="text: progressDisplay"></span>
                    <!-- /ko -->
                </div>
                <!-- ko if: holdPeriodPercent() < 50 -->
                <span data-bind="text: progressDisplay"></span>
                <!-- /ko -->
            </div>
		</div>
		<!-- /ko -->
		<!-- ko if: status() != 'Submitted' && status() != 'OnHold' -->
		<div class="help-block">
			<div class="btn-group btn-group-xs">
				<button class="btn btn-primary-outline" data-bind="click: $parent.viewAction"><span class="fa fa-search"></span> View</button>
			</div>
		</div>
		<!-- /ko -->
		<!-- ko if: status() == 'Active' -->
		<!-- ko if: type() == 'Low_Erosivity_Waiver' -->
		    <div class="btn-group btn-group-xs">
                <button class="btn btn-danger-outline" data-bind="modal: {
                                                                        name: 'terminate-modal',
                                                                        params: {
                                                                            data: $data,
                                                                            copy: true
                                                                        }
                                                                      }">Discontinue</button>
             </div>
		<!-- /ko -->
		<!-- ko if: type() == 'Notice_Of_Intent' -->
		    <div class="btn-group btn-group-xs">
                <button class="btn btn-danger-outline" data-bind="modal: {
                                                                        name: 'terminate-modal',
                                                                        params: {
                                                                            data: $data,
                                                                            copy: true
                                                                        }
                                                                      }">Terminate</button>
            </div>
		<!-- /ko -->
		<!-- /ko -->
		<!-- /ko -->
		<!-- /ko -->
	</script>
</sec:authorize>
<sec:authorize access="hasRole('${actionBean.helpdeskRole}')">
	<script type="text/html" id="table-actions">
		<!-- ko with: data -->
		<!-- ko if: oeca.cgp.currentUser.username == formSet.owner() -->
		<div class="btn-group btn-group-xs">
		<!-- ko if: (phase() == 'New' || phase() == 'Change') && status() == 'Draft' -->
			<button class="btn btn-primary-outline" data-bind="click: $parent.editAction"><span class="fa fa-pencil"></span> Edit</button>
			<button class="btn btn-danger-outline" data-bind="click: $parent.deleteAction">
				<!-- ko if: phase() == 'New' -->
				<span class="fa fa-times"></span> Delete
				<!-- /ko -->
				<!-- ko if: phase() == 'Change' -->
				<span class="fa fa-undo"></span> Revert
				<!-- /ko -->
			</button>
		<!-- /ko -->
		<!-- ko if: (phase() == 'New' || phase() == 'Change') && status() == 'Active' -->
			<button class="btn btn-primary-outline" data-bind="click: $parent.changeAction"><span class="fa fa-pencil"></span> Change</button>
			<button class="btn btn-danger-outline" data-bind="modal: {
																	name: 'terminate-modal',
																	params: {
																		data: $data,
																		copy: true
																	}
																  }">
				<span class="fa fa-times"></span>
				<!-- ko if: type() == 'Low_Erosivity_Waiver' -->
				Discontinue
				<!-- /ko -->
				<!-- ko if: type() == 'Notice_Of_Intent' -->
				Terminate
				<!-- /ko -->
			</button>
		<!-- /ko -->
		<!-- ko if: phase() == 'Terminate' && status() == 'Draft' -->
			<button class="btn btn-danger-outline" style="width: 178px" data-bind="click: $parent.signTerminateAction">
				<span class="fa fa-file-text-o"></span>
				<!-- ko if: type() == 'Low_Erosivity_Waiver' -->
				Sign Discontinue Request
				<!-- /ko -->
				<!-- ko if: type() == 'Notice_Of_Intent' -->
				Sign Terminate Request
				<!-- /ko -->
			</button>
			<button class="btn btn-danger-outline" data-bind="click: $parent.deleteAction">
				<span class="fa fa-undo"></span> Revert
			</button>
		<!-- /ko -->
		<button class="btn btn-primary-outline" data-bind="click: $parent.viewAction"><span class="fa fa-search"></span> View</button>
        <!-- ko if: status() != 'Terminated' && status() != 'Discontinued' -->
        <button class="btn btn-primary-outline" data-bind="click: $parent.assignAction"><span class="fa fa-pencil-square-o"></span> Reassign</button>
        <!-- /ko -->
		</div>
		<!-- ko if: status() == 'Submitted' -->
		<div class="progress">
			<div class="progress-bar"
				 data-bind="attr: {
																'aria-valuenow': moment().format('X'),
																'aria-valuemin': moment(certifiedDate()).format('X'),
																'aria-valuemax': moment(reviewExpiration()).format('X'),
															},
															style: {
																width: holdPeriodPercent() + '%'
															}">
				<!-- ko if: holdPeriodPercent() >= 50 -->
				<span data-bind="text: progressDisplay"></span>
				<!-- /ko -->
			</div>
			<!-- ko if: holdPeriodPercent() < 50 -->
			<span data-bind="text: progressDisplay"></span>
			<!-- /ko -->
		</div>
		<!-- /ko -->
		<!-- /ko -->
		<!-- ko if: oeca.cgp.currentUser.username != formSet.owner() -->
		<div class="btn-group btn-group-xs">
			<button class="btn btn-primary-outline" data-bind="click: $parent.viewAction"><span class="fa fa-search"></span> View</button>
			<!-- ko if: status() != 'Terminated' && status() != 'Discontinued' -->
			<button class="btn btn-primary-outline" data-bind="click: $parent.assignAction"><span class="fa fa-pencil-square-o"></span> Reassign</button>
			<!-- /ko -->
		</div>
		<!-- ko if: status() == 'Submitted' -->
		<div class="progress">
			<div class="progress-bar"
				 data-bind="attr: {
																'aria-valuenow': moment().format('X'),
																'aria-valuemin': moment(certifiedDate()).format('X'),
																'aria-valuemax': moment(reviewExpiration()).format('X'),
															},
															style: {
																width: holdPeriodPercent() + '%'
															}">
				<!-- ko if: holdPeriodPercent() >= 50 -->
				<span data-bind="text: progressDisplay"></span>
				<!-- /ko -->
			</div>
			<!-- ko if: holdPeriodPercent() < 50 -->
			<span data-bind="text: progressDisplay"></span>
			<!-- /ko -->
		</div>
		<!-- /ko -->
		<!-- /ko -->
		<!-- /ko -->
	</script>
</sec:authorize>
<sec:authorize access="hasAnyRole('${actionBean.certifierRole}', '${actionBean.preparerRole}')">
	<script type="text/html" id="table-actions">
    <!-- ko with: data -->
	<div class="btn-group btn-group-xs">
		<!-- ko if: oeca.cgp.currentUser.username == formSet.owner() -->
		<!-- ko if: (phase() == 'New' || phase() == 'Change') && status() == 'Draft' -->
			<button class="btn btn-primary-outline" data-bind="click: $parent.editAction"><span class="fa fa-pencil"></span> Edit</button>
			<button class="btn btn-danger-outline" data-bind="click: $parent.deleteAction">
				<!-- ko if: phase() == 'New' -->
				<span class="fa fa-times"></span> Delete
				<!-- /ko -->
				<!-- ko if: phase() == 'Change' -->
				<span class="fa fa-undo"></span> Revert
				<!-- /ko -->
			</button>
		<!-- /ko -->
		<!-- ko if: (phase() == 'New' || phase() == 'Change') && status() == 'Active' -->
			<button class="btn btn-primary-outline" data-bind="click: $parent.changeAction"><span class="fa fa-pencil"></span> Change</button>
			<button class="btn btn-danger-outline" data-bind="modal: {
																	name: 'terminate-modal',
																	params: {
																		data: $data,
																		copy: true
																	}
																  }">
				<span class="fa fa-times"></span>
				<!-- ko if: type() == 'Low_Erosivity_Waiver' -->
				Discontinue
				<!-- /ko -->
				<!-- ko if: type() == 'Notice_Of_Intent' -->
				Terminate
				<!-- /ko -->
			</button>
		<!-- /ko -->
		<!-- ko if: phase() == 'Terminate' && status() == 'Draft' -->
			<button class="btn btn-danger-outline" style="width: 178px" data-bind="click: $parent.signTerminateAction">
				<span class="fa fa-file-text-o"></span>
				<!-- ko if: type() == 'Low_Erosivity_Waiver' -->
				Sign Discontinue Request
				<!-- /ko -->
				<!-- ko if: type() == 'Notice_Of_Intent' -->
				Sign Terminate Request
				<!-- /ko -->
			</button>
			<button class="btn btn-danger-outline" data-bind="click: $parent.deleteAction">
				<span class="fa fa-undo"></span> Revert
			</button>
		<!-- /ko -->
		<!-- /ko -->
		<button class="btn btn-primary-outline" data-bind="click: $parent.viewAction"><span class="fa fa-search"></span> View</button>
	</div>
	<!-- ko if: (phase() == 'New' || phase() == 'Change') && status() == 'Submitted' -->
	<div class="help-block">
	    <!-- ko if: type() == 'Notice_Of_Intent' && showOnHold() == true-->
		<div>
			<span class="fa fa-lock" style="font-size: 18px"></span> On hold
			<span class="glyphicon glyphicon-question-sign pull-right" style="font-size: 18px" data-bind="popover: { options: { title: 'On hold', placement: 'left', content: 'In order to make a change to this form you must contact your RA.' } }"></span>
		</div>
		<!-- /ko -->
		<div class="progress">
			<div class="progress-bar"
				 data-bind="attr: {
																'aria-valuenow': moment().format('X'),
																'aria-valuemin': moment(certifiedDate()).format('X'),
																'aria-valuemax': moment(reviewExpiration()).format('X'),
															},
															style: {
																width: holdPeriodPercent() + '%'
															}">
				<!-- ko if: holdPeriodPercent() >= 50 -->
				<span data-bind="text: progressDisplay"></span>
				<!-- /ko -->
			</div>
			<!-- ko if: holdPeriodPercent() < 50 -->
			<span data-bind="text: progressDisplay"></span>
			<!-- /ko -->
		</div>
	</div>
	<!-- /ko -->
	<!-- /ko -->
</script>
</sec:authorize>
<script type="text/html" id="table-details-control">
    <span class="glyphicon glyphicon-plus-sign"></span>
</script>
<script type="text/html" id="forms-row-details">
	<div class="well">
		<div class="row" style="margin-top: 15px; margin-bottom: 15px" data-bind="with: data">
			<div class="col-sm-1" style="width: 4%">
				<span class="glyphicon glyphicon-share-alt" style="transform: scaleY(-1)"></span>
			</div>
			<div class="col-sm-11" style="width: 96%">
				<div class="col-sm-12">
					<span style="font-size: 150%">Details</span>
				</div>
				<div class="col-sm-4">
					Application Type: <span data-bind="text: typeDisplay"></span><br/>
					NPDES ID: <span data-bind="text: formSet.npdesId"></span><br/>
					Master Permit Number: <span data-bind="text: formSet.masterPermitNumber"></span><br/>
					Tracking Number: <span data-bind="text: trackingNumber"></span><br/>
					Operator Name: <span data-bind="text: formData.operatorInformation.operatorName"></span><br/>
					Project/Site Name: <span data-bind="text: formData.projectSiteInformation.siteName"></span><br/>
				</div>
				<div class="col-sm-4">
					Project City: <span data-bind="text: formData.projectSiteInformation.siteCity"></span><br/>
					Project State: <span data-bind="text: formData.projectSiteInformation.siteStateCode"></span><br/>
					Project Zip Code: <span data-bind="text: formData.projectSiteInformation.siteZipCode"></span><br/>
					Project County: <span data-bind="text: formData.projectSiteInformation.siteCounty"></span><br/>
					Project Area: <span data-bind="text: type() === oeca.cgp.constants.NOI_TYPE ? (formData.projectSiteInformation.siteAreaDisturbed() !== null ? formData.projectSiteInformation.siteAreaDisturbed() + ' acre(s)' : '') :
							(formData.lowErosivityWaiver.lewAreaDisturbed() !== null ? formData.lowErosivityWaiver.lewAreaDisturbed() + ' acre(s)' : '')"></span><br/>
					Project Start Date: <span data-bind="text: type() === oeca.cgp.constants.NOI_TYPE ? formData.projectSiteInformation.siteProjectStart :
							formData.lowErosivityWaiver.lewProjectStart"></span><br/>
					Project End Date: <span data-bind="text: type() === oeca.cgp.constants.NOI_TYPE ? formData.projectSiteInformation.siteProjectEnd :
							formData.lowErosivityWaiver.lewProjectEnd"></span>
				</div>
				<div class="col-sm-4">
					Indian Lands: <span data-bind="text: formData.projectSiteInformation.siteIndianCountryLands"></span><br/>
					Location: <span data-bind="text: formData.projectSiteInformation.siteLocation.display"></span><br/>
					Owner: <span data-bind="text: formSet.owner"></span><br/>
					Status: <span data-bind="text: statusDisplay"></span><br/>
					Date Certified: <span data-bind="text: certifiedDate() !== null ? moment(certifiedDate()).format('MM/DD/YYYY h:mm A') : ''"></span><br/>
					Date Active: <span data-bind="text: reviewExpiration() !== null ? moment(reviewExpiration()).format('MM/DD/YYYY h:mm A') : ''"></span>
				</div>
			</div>
		</div>
		<!-- ko if: history().length > 0 -->
		<hr>
		<div class="h3">Historical Forms</div>
		<table class="table table-striped table-bordered dataTable responsive no-wrap no-footer dtr-inline">
			<thead>
			<tr>
				<th>Tracking Number</th>
				<th>Status</th>
				<th>Last Updated</th>
				<th>Actions</th>
			</tr>
			</thead>
			<tbody data-bind="foreach: history">
			<tr>
				<td data-bind="text: trackingNumber"></td>
				<td data-bind="text: status"></td>
				<td data-bind="text: lastUpdatedDate() !== null ? moment(lastUpdatedDate()).format('MM/DD/YYYY h:mm A') : ''"></td>
				<td><div class="btn-group btn-group-xs"><button type="button" class="btn-primary-outline btn" data-bind="click: $parent.view"><span class="fa fa-search"></span> View</button></div></td>
			</tr>
			</tbody>
		</table>
		<!-- /ko -->
	</div>
</script>
<div class="row" style="margin-bottom: 10px">
    <div class="col-sm-2" style="width: 180px">
        <img src="${pageContext.request.contextPath}/static/img/NeT.png"/>
    </div>
    <div class="col-sm-10">
        <h1 style="vertical-align: middle; display: table-cell">EPA's Construction General Permit (CGP) Electronic Forms</h1>
    </div>
</div>
<sec:authorize access="hasAnyRole('${actionBean.certifierRole}', '${actionBean.preparerRole}', '${actionBean.helpdeskRole}')">
	<div class="panel panel-default">
		<button style="display: none" id="sign-terminate"></button>
		<div class="panel-heading">Create New</div>
		<div class="panel-body">
			<div class="cgp-btn-full-width">
				<div class="btn-group">
					<a class="btn btn-lg btn-primary" data-bind="page-href: '/noi'">Notice of Intent (NOI)</a>
				</div>
				<div class="btn-group">
					<a class="btn btn-lg btn-primary" data-bind="page-href: '/lew'">Low Erosivity Waiver (LEW)</a>
				</div>
				<div class="btn-group">
					<a href="JavaScript:;" class="btn btn-lg btn-default"  data-bind="click: function(){oeca.cgp.notifications.lewQualification()}">Do I qualify for a LEW?</a>
				</div>
			</div>
		</div>
	</div>
</sec:authorize>
<div class="panel panel-default">
	<div class="panel-heading">NOIs and LEWs</div>
	<div class="panel-body">
		<div class="panel-group" id="accordion">
			<div class="panel panel-default">
				<div class="panel-heading" data-bind="click: function(){showFilter(!showFilter())}">
					<a role="button" href="JavaScript:;">Filter NOIs and LEWs</a>
					<!-- ko if: showFilter -->
					<span class="glyphicon glyphicon-chevron-down pull-right"></span>
					<!-- /ko -->
					<!-- ko ifnot: showFilter -->
					<span class="glyphicon glyphicon-chevron-up pull-right"></span>
					<!-- /ko -->
				</div>
				<div class="panel-body" id="filter" data-bind="slideVisible: showFilter, with: criteria">
					<div class="row">
						<div class="col-sm-4 form-group">
							<label class="control-label" for="npdes-id">NPDES ID</label>
							<input id="npdes-id" class="form-control" type="text" data-bind="value: npdesId" />
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="tracking-number">Tracking Number</label>
							<input id="tracking-number" class="form-control" type="text" data-bind="value: trackingNumber" />
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4 form-group">
							<label class="control-label" for="application-type">Application Type</label>
							<select id="application-type" class="form-control" data-bind="lookup: 'formTypes',
																					value: type,
																					optionsValue: 'code',
																					optionsText: 'description',
																					valueAllowUnset: true,
																					optionsCaption: 'Select Form Type'"></select>
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="project-site">Project/Site</label>
							<input id="project-site" class="form-control" type="text" data-bind="value: siteName" />
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="operator-name">Operator Name</label>
							<input id="operator-name" class="form-control" type="text" data-bind="value: operatorName" />
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4 form-group">
							<label class="control-label" for="project-state">Project State</label>
							<select id="project-state" class="form-control" data-bind="lookup: 'states',
																				value: siteStateCode,
																				optionsText: 'stateName',
																				optionsValue: 'stateCode',
																				valueAllowUnset: true,
																				optionsCaption: '',
																				select2: {placeholder: 'Select State'}"
									style="width: 100%"/>
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="project-county">Project County</label>
							<select id="project-county" class="form-control" style="width: 100%" data-bind="enable: siteStateCode,
																										lookup: {
																											options: 'counties',
																											filter: {
																												value: 'stateCode',
																												by: siteStateCode
																											}
																										},
																										optionsText: 'countyName',
																										optionsValue: 'countyName',
																										valueAllowUnset: true,
																										optionsCaption: '',
																										select2: {
																											placeholder: 'Select County'
																										}
																										" />
						    <span class="help-block" data-bind="visible: !siteStateCode()">Project State must be selected first.</span>
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="project-city">Project City</label>
							<input id="project-city" class="form-control" type="text" data-bind="value: siteCity" />
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4 form-group">
							<label class="control-label" for="project-zip">Project Zip</label>
							<input id="project-zip" class="form-control" type="text" data-bind="maskedZip: siteZipCode" />
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="project-status">Project Status</label>
							<select id="project-status" class="form-control" data-bind="lookup: 'formStatuses',
																					value: status,
																					optionsValue: 'code',
																					optionsText: 'description',
																					valueAllowUnset: true,
																					optionsCaption: 'Select Status'"></select>
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="tribe-name">Tribe Name</label>
							<select id="tribe-name" class="form-control" style="width: 100%" data-bind="lookup: {
				    														options: 'tribes',
				    														filter: {
				    															value: function(item, value) {
				    																if(!ko.utils.unwrapObservable(value)) {
				    																	return true;
				    																}
				    																return item.states.filterByProp(value, 'stateCode').length > 0;
				    															},
				    															by: siteStateCode
				    														}
				    													 },
				    													 value: siteIndianCountryLands,
                                                                         optionsValue: 'tribalName',
                                                                         optionsText: 'tribalName',
                                                                         optionsCaption: '',
                                                                         valueAllowUnset: true,
                                                                         select2: {
                                                                             placeholder: 'Select Tribe'
                                                                         }"></select>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4 form-group">
							<label class="control-label" for="submitted-date-from">Submitted Date From</label>
							<input id="submitted-date-from" class="form-control" type="text" data-bind="value: submittedFrom, datepicker" />
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="submited-date-to">Submitted Date To</label>
							<input id="submited-date-to" class="form-control" type="text" data-bind="value: submittedTo, datepicker" />
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label">Federal Facility</label>
							<div>
								<div class="radio radio-inline" style="padding-left: 0px">
									<label>
										<input type="radio" name="federal-facility" id="federal-facility-yes" data-bind="checkedValue: true, checked: operatorFederal"/>Yes
									</label>
									&nbsp;
									<label>
										<input type="radio" name="federal-facility" id="federal-facility-no" data-bind="checkedValue: false, checked: operatorFederal"/>No
									</label>
								</div>
							</div>
						</div>
					</div>
					<div class="row">
						<div class="col-sm-4 form-group">
							<label class="control-label" for="updated-date-from">Updated Date From</label>
							<input id="updated-date-from" class="form-control" type="text" data-bind="value: updatedFrom, datepicker" />
						</div>
						<div class="col-sm-4 form-group">
							<label class="control-label" for="updated-date-to">Updated Date To</label>
							<input id="updated-date-to" class="form-control" type="text" data-bind="value: updatedTo, datepicker" />
						</div>
						<div class="col-sm-4" style="padding-top: 13px">
							<div class="btn-group btn-group-justified">
								<div class="btn-group">
									<button class="btn btn-lg btn-default" data-bind="click: $parent.clearFilter">Clear</button>
								</div>
								<div class="btn-group">
									<button class="btn btn-lg btn-primary" data-bind="click: $parent.applyFilter">Apply</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
		<table id="forms" class="table table-striped table-condensed dataTable responsive no-wrap" style="width: 100%" >
			<thead>
				<tr>
				    <th> </th>
					<th>Type</th>
					<th>NPDES ID</th>
					<th>Master Permit Number</th>
					<th>Tracking Number</th>
					<th>Site State</th>
					<th>Operator</th>
					<th>Site Name</th>
					<th>Owner</th>
					<th>Status</th>
					<th>Last Modified</th>
					<th>Actions</th>
				</tr>
			</thead>
			<tbody data-bind="childRow: {name: 'forms-row-details', vm: DashboardDetailsController}" >
			</tbody>
		</table>
	</div>
</div>