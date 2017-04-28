<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">Endangered Species Protection</a>
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
	<div class="panel-collapse collapse" id="endangeredSpeciesProtection" data-bind="slideVisible: expand">
		<div class="panel-body">
			<!-- ko if: change() -->
			<div class="alert alert-warning">
				Editing this section will reset your 14-day hold period.
				<!-- ko ifnot: waitListEdit -->
				If you want to edit this section click the edit button.
				<button href="JavaScript:" class="btn btn-sm btn-default" data-bind="click: function(data, event) {oeca.cgp.noi.waitListWarning(data, event, 'the Endangered Species Protection section')}">Edit</button>
				<!-- /ko -->
			</div>
			<!-- /ko -->
			<fieldset data-bind="disable: change() && !waitListEdit()">
				<div class="row">
					<div class="col-sm-12 form-group">
						<label class="control-label">
							Using the instructions in <a href="${actionBean.cgpUrls.appendixD }" target="_blank">Appendix D</a> of the CGP,
                            under which criterion listed below are you eligible for coverage under this permit? Check only 1 box,
                            include the required information and provide a sound basis for supporting the criterion selected.
                            You must consider Endangered Species Act listed threatened or endangered species (ESA-listed) and/or
                            designated critical habitat(s) under the jurisdiction of both the U.S. Fish and Wildlife Service (USFWS)
                            and National Marine Fisheries Service (NMFS) and select the most conservative criterion that applies.
						</label>
						<div class="radio">
							<label>
								<input type="radio" name="endangered-species-criteria"
									data-bind="checkedValue: 'Criterion_A', checked: criterion" />
								Criterion A <a class="glyphicon glyphicon-info-sign"
									data-bind="click: function(){alerts.endangeredAppendixA()}"></a>
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="endangered-species-criteria"
									   data-bind="checkedValue: 'Criterion_B', checked: criterion" />
								Criterion B <a class="glyphicon glyphicon-info-sign"
									data-bind="click: function(){alerts.endangeredAppendixB()}"></a>
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="endangered-species-criteria"
									   data-bind="checkedValue: 'Criterion_C', checked: criterion" />
								Criterion C <a class="glyphicon glyphicon-info-sign"
									data-bind="click: function(){alerts.endangeredAppendixC()}"></a>
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="endangered-species-criteria"
									   data-bind="checkedValue: 'Criterion_D', checked: criterion" />
								Criterion D <a class="glyphicon glyphicon-info-sign"
									data-bind="click: function(){alerts.endangeredAppendixD()}"></a>
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="endangered-species-criteria"
									   data-bind="checkedValue: 'Criterion_E', checked: criterion" />
								Criterion E <a class="glyphicon glyphicon-info-sign"
									data-bind="click: function(){alerts.endangeredAppendixE()}"></a>
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="endangered-species-criteria"
									   data-bind="checkedValue: 'Criterion_F', checked: criterion" />
								Criterion F <a href="JavaScript:;" class="glyphicon glyphicon-info-sign"
									data-bind="click: function(){alerts.endangeredAppendixF()}"></a>
							</label>
						</div>
					</div>
				</div>
				<div class="row" data-bind="slideVisible: criterion() == 'Criterion_B'">
					<div class="col-lg-12 form-group">
						<label class="control-label" for="other-operator-npdes-id">Provide the NPDES ID from the other operator's
							notification of authorization under this permit:</label>
						<input type="text" id="other-operator-npdes-id" class="form-control" maxlength="9" data-bind="value: otherOperatorNpdesId" />
					</div>
				</div>
				<div class="row">
					<div class="col-lg-12 form-group">
						<label class="control-label" for="endangered-species-criterion-summary">
							Provide a brief summary of the basis for criterion selection listed above [the necessary
							content for a supportive basis statement is provided under the criterion you selected].
						</label>
						<textarea id="endangered-species-criterion-summary" class="form-control" data-bind="value: criteriaSelectionSummary"></textarea>
						<span class="static-info" data-bind="visible: criterion() == 'Criterion_A'">
						    <strong>A basis statement supporting the selection of this criterion should identify the USFWS and NMFS information
                            sources used. Attaching aerial image(s) of the site to this NOI is helpful to EPA, USFWS, and
                            NMFS in confirming eligibility under this criterion. Please Note: NMFS' jurisdiction includes
                            ESA-listed marine and estuarine species that spawn in inland rivers.</strong>
					    </span>
						<span class="static-info" data-bind="visible: criterion() == 'Criterion_B'">
						    <strong>A basis statement supporting the selection of this criterion should identify the eligibility criterion of
						    the other CGP NOI, the authorization date, and confirmation that the authorization is effective.</strong>
						</span>
						<span class="static-info" data-bind="visible: criterion() == 'Criterion_C'">
						    <strong>A basis statement supporting the selection of this criterion should identify the information resources and expertise
						    (e.g., state or federal biologists) used to arrive at this conclusion. Any supporting documentation should explicitly state that
						    both ESA-listed species and designated critical habitat under the jurisdiction of the USFWS and/or NMFS were considered
						    in the evaluation. Attaching aerial image(s) of the site to this NOI is helpful to EPA, USFWS, and NMFS in confirming eligibility
						    under this criterion.</strong>
						</span>
						<span class="static-info" data-bind="visible: criterion() == 'Criterion_D'">
						    <strong>A basis statement supporting the selection of this criterion should identify whether USFWS or NMFS or both agencies participated
						    in coordination, the field office/regional office(s) providing that coordination, and the date that coordination concluded.</strong>
						</span>
						<span class="static-info" data-bind="visible: criterion() == 'Criterion_E'">
						    <strong>A basis statement supporting the selection of this criterion should identify the federal action agencie(s) involved,
						    the field office/regional office(s) providing that consultation, any tracking numbers of identifiers associated with that consultation
						    (e.g., IPaC number, PCTS number), and the date the consultation was completed.</strong>
						</span>
						<span class="static-info" data-bind="visible: criterion() == 'Criterion_F'">
						    <strong>A basis statement supporting the selection of this criterion should identify whether USFWS or NMFS or both agencies provided a
						    section 10 permit, the field office/regional office(s) providing permit(s), any tracking numbers of identifiers associated with that
						    consultation (e.g., IPaC number, PCTS number), and the date the permit was granted.</strong>
						</span>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 form-group" data-bind="slideVisible: criterion() == 'Criterion_C'">
						<label class="control-label" for="habitateds-located">What federally-listed species or federally-designated
							critical habitat are located in your
							<dfn data-bind="modal: {name: 'action-area-modal'}">"action area"</dfn>?</label>
						<br/>
						<textarea id="habitateds-located" class="form-control" data-bind="value: speciesAndHabitatInActionArea"></textarea>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 form-group" data-bind="slideVisible: criterion() == 'Criterion_C'">
						<label class="control-label" for="habitat-distance">What is the distance between your site and the listed
							species or critical habitat (miles)?</label>
						<textarea id="habitat-distance" class="form-control" data-bind="value: distanceFromSite"></textarea>
					</div>
				</div>
				<div class="row">
					<div class="col-sm-12 form-group required" data-bind="slideVisible: criterion() == 'Criterion_C'">
						<label class="control-label">You must attach a copy of your site map</label>
						<%--<file-input params="id: 'endangered-species-site-map', value: tempAttachment"></file-input>--%>
						<span class="help-block">see part 7.2.6 of the permit</span>
					</div>
					<div class="col-sm-12 form-group required" data-bind="slideVisible: criterion() == 'Criterion_D' || criterion() == 'Criterion_E' || criterion() == 'Criterion_F'">
						<label class="control-label">Attach copies of any letters or other communications between you and the U.S.
							Fish and Wildlife Service or National Marine Fisheries Service.</label>
						<%--<file-input params="id: 'endangered-species-usfs-nmfs-communications', value: tempAttachment"></file-input>--%>
						<span class="help-block">e.g., communication with the U.S. Fish and Wildlife Service or National Marine
							Fisheries Service, specific study</span>
					</div>
				</div>
				<div class="row" data-bind="slideVisible: criterion() == 'Criterion_C' || criterion() == 'Criterion_D' || criterion() == 'Criterion_E' || criterion() == 'Criterion_F'">
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
					<div class="col-sm-12">
						<file-input params="id: 'endangered-species', value: pendingAttachment"></file-input>
						<span class="validationMessage" data-bind="validationMessage: attachments"></span>
					</div>
				</div>
				<button class="btn btn-primary" data-bind="click: saveAndContinue"
						onclick="scrollToTop();">
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