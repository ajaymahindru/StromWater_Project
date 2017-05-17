<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
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
					<!-- ko ifnot: draft || change -->
						<select style="width: 100%" class="form-control" id="state" data-bind="lookup: 'states',
							value: siteStateCode,
							optionsText: 'stateName',
							optionsValue: 'stateCode',
							optionsCaption: '',
							valueAllowUnset: true,
							select2: {placeholder: 'Select a State'}">
						</select>
					<!-- /ko -->
					<!-- ko if: draft || change -->
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
					<div class="h4">Latitude and Longitude</div><span class="help-block">Click on the map to automatically find Latitude and Longitude.</span>
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
			<hr>
			<div class="row">
				<div class="col-sm-4 form-group">
					<label class="control-label" for="project-start-date">Project Start Date</label>
					<input id="project-start-date" class="form-control" type="text" data-bind="value: siteProjectStart, datepicker" />
				</div>
				<div class="col-sm-4 form-group">
					<label class="control-label" for="project-end-date">Estimated Project End Date</label>
					<input id="project-end-date" class="form-control" type="text" data-bind="value: siteProjectEnd, datepicker" />
				</div>
				<div class="col-sm-4 form-group">
					<label class="control-label" for="estimated-area">Estimated Area to be Disturbed</label>
					<!-- ko ifnot: change -->
						<input id="estimated-area" class="form-control" type="number" data-bind="value: siteAreaDisturbed" />
					<!-- /ko -->
					<!-- ko if: change -->
						<div class="input-group">
							<input id="estimated-area" class="form-control" type="number" data-bind="value: siteAreaDisturbed, disable: change() && !waitListEdit()" />
							<div class="input-group-addon">
								<span class="glyphicon glyphicon-warning-sign" title="Changes greater than 1 acre will result in a reset of your 14-day hold period."></span>
							</div>
							<!-- ko ifnot: waitListEdit -->
							<div class="input-group-btn">
								<button class="btn btn-warning" data-bind="click: function(data, event) {oeca.cgp.noi.waitListWarning(data, event, 'the Estimated Area to be Disturbed field greater than 1 acre')}">Edit</button>
							</div>
							<!-- /ko -->
						</div>
					<!-- /ko -->
					<span class="help-block">to the nearest quarter acre</span>
				</div>
			</div>
			<div class="row">
				<div class="col-sm-12 form-group">
					<label class="control-label">Type of Construction Site</label>
					<span class="help-block">check all that apply</span>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: 'single', checked: siteConstructionTypes" />
							Single-Family Residential
						</label>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: 'multi', checked: siteConstructionTypes" />
							Multi-Family Residential
						</label>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: 'commercial', checked: siteConstructionTypes" />
							Commercial
						</label>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: 'industrial', checked: siteConstructionTypes" />
							Industrial
						</label>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: 'institutional', checked: siteConstructionTypes" />
							Institutional
						</label>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: 'road', checked: siteConstructionTypes" />
							Highway or Road
						</label>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: 'utility', checked: siteConstructionTypes" />
							Utility
						</label>
					</div>
					<div class="checkbox">
						<label>
							<input type="checkbox" name="construction-site-type" data-bind="checkedValue: otherCheckedValue, checked: siteConstructionTypes" />
							Other
						</label>
					</div>
				</div>
				<div class="col-sm-12 form-group">
					<label class="sr-only" for="other-construction-type">Other Construction Type</label>
					<input type="text" id="other-construction-type" class="form-control" maxlength="100"
						   data-bind="slideVisible: siteConstructionTypes() && siteConstructionTypes().contains(otherCheckedValue()), value: otherCheckedValue" />
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">Will there be demolition of any structure built or renovated before January 1,
						1980?</label>
					<!-- ko if: change() -->
					<div class="alert alert-warning">
						Editing this question will reset your 14-day hold period.
						<!-- ko ifnot: waitListEdit -->
						If you want to edit this section click the edit button.
						<button href="JavaScript:" class="btn btn-sm btn-default" data-bind="click: function(data, event) {oeca.cgp.noi.waitListWarning(data, event, 'the Demolition before 1980 question')}">Edit</button>
						<!-- /ko -->
					</div>
					<!-- /ko -->
					<fieldset data-bind="disable: change() && !waitListEdit()">
						<div class="radio">
							<label>
								<input type="radio" name="renovated-1980" data-bind="checkedValue: true, checked: siteStructureDemolitionBefore1980" />
								Yes
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="renovated-1980" data-bind="checkedValue: false, checked: siteStructureDemolitionBefore1980" />
								No
							</label>
						</div>
					</fieldset>
				</div>
			</div>
			<div class="row subquestion" data-bind="slideVisible: siteStructureDemolitionBefore1980">
				<span class="glyphicon glyphicon-share-alt"></span>
				<div class="col-md-12 form-group">
					<label class="control-label">If yes, do any of the structures being demolished have at least 10,000 square
						feet of floor space?</label>
					<div class="radio">
						<label>
							<input type="radio" name="demolished-sq-ft" data-bind="checkedValue: true, checked: siteStructureDemolitionBefore198010kSquareFeet" />
							Yes
						</label>
					</div>
					<div class="radio">
						<label>
							<input type="radio" name="demolished-sq-ft" data-bind="checkedValue: false, checked: siteStructureDemolitionBefore198010kSquareFeet" />
							No
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">Was the pre-development land use used for agriculture?</label>
					<span class="help-block">see <a href="${actionBean.cgpUrls.appendixA}" target="_blank">Appendix A</a> for definition of
					<dfn data-bind="popover: oeca.cgp.definitions.agriculturalLand">
					    "agricultural land"
					</dfn>
					</span>
					<div class="radio">
						<label>
							<input type="radio" name="agriculture-user" data-bind="checkedValue: true, checked: sitePreDevelopmentAgricultural" />
							Yes
						</label>
					</div>
					<div class="radio">
						<label>
							<input type="radio" name="agriculture-user" data-bind="checkedValue: false, checked: sitePreDevelopmentAgricultural" />
							No
						</label>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">Have earth-disturbing activities commenced on your project/site?</label>
					<div class="radio">
						<label>
							<input type="radio" name="earth-disturbing-activity" data-bind="checkedValue: true, checked: siteEarthDisturbingActivitiesOccurrence" />
							Yes
						</label>
					</div>
					<div class="radio">
						<label>
							<input type="radio" name="earth-disturbing-activity" data-bind="checkedValue: false, checked: siteEarthDisturbingActivitiesOccurrence" />
							No
						</label>
					</div>
				</div>
			</div>
			<div data-bind="slideVisible: siteEarthDisturbingActivitiesOccurrence, scrollTo: siteEarthDisturbingActivitiesOccurrence">
				<div class="row subquestion">
					<span class="glyphicon glyphicon-share-alt"></span>
					<div class="col-md-12 form-group">
						<label class="control-label">If yes, is your project an
						<dfn data-bind="popover: oeca.cgp.definitions.emergencyRelatedProject">
						    "emergency-related project"</dfn>?</label>
						<span class="help-block">see <a href="${actionBean.cgpUrls.appendixA}" target="_blank">Appendix A</a></span>
						<div class="radio">
							<label>
								<input type="radio" name="emergency-related-project" data-bind="checkedValue: true, checked: siteEmergencyRelated" />
								Yes
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="emergency-related-project" data-bind="checkedValue: false, checked: siteEmergencyRelated" />
								No
							</label>
						</div>
					</div>
				</div>
			</div>
			<div class="row">
				<div class="col-md-12 form-group">
					<label class="control-label">Is your project located on a property of religious or cultural significance to an Indian tribe?</label>
					<div class="radio">
						<label>
							<input type="radio" name="religious-cultural-site" data-bind="checkedValue: true, checked: siteIndianCulturalProperty">
							Yes
						</label>
					</div>
					<div class="radio">
						<label>
							<input type="radio" name="religious-cultural-site" data-bind="checkedValue: false, checked: siteIndianCulturalProperty">
							No
						</label>
					</div>
				</div>
			</div>
			<div class="row subquestion" data-bind="slideVisible: siteIndianCulturalProperty">
				<span class="glyphicon glyphicon-share-alt"></span>
				<div class="col-md-12 form-group">
					<label class="control-label">Indicate which tribe this land is of religious or cultural significance to.</label>
					<select id="biaCode" class="form-control" data-bind="lookup: {
																				options: 'biaTribes',
																				filter: {
																					value: 'stateCode',
																					by: siteStateCode
																				}
																			},
																			value: siteIndianCulturalPropertyTribe,
																			optionsValue: 'tribeName',
																			optionsText: 'tribeName',
																			optionsCaption: '',
																			valueAllowUnset: true,
																			select2: {
																				placeholder: 'Select Tribe'
																			}" style="width: 500px">
					</select>
				</div>
			</div>
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