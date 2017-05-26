<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">Historic Preservation</a>
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
	<div class="panel-collapse collapse" id="historicPreservation" data-bind="slideVisible: expand">
		<div class="panel-body">
			<!-- ko if: change() -->
			<div class="alert alert-warning">
				Editing this section will reset your 14-day hold period.
				<!-- ko ifnot: waitListEdit -->
				If you want to edit this section click the edit button.
				<button href="JavaScript:" class="btn btn-sm btn-default" data-bind="click: function(data, event) {oeca.cgp.noi.waitListWarning(data, event, 'the Historic Preservation section')}">Edit</button>
				<!-- /ko -->
			</div>
			<!-- /ko -->
			<fieldset data-bind="disable: change() && !waitListEdit()">
				<div class="row">
					<div class="col-md-12 form-group">
						<label class="control-label">
							Are you installing any stormwater controls as described in <a href="${actionBean.cgpUrls.appendixE}"
								target="_blank">Appendix E</a> that require subsurface earth disturbances? (<a
								href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 1)
						</label>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s1" data-bind="checkedValue: true, checked: appendexEStep1" />
								Yes
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s1" data-bind="checkedValue: false, checked: appendexEStep1" />
								No
							</label>
						</div>
					</div>
				</div>
				<div class="row subquestion" data-bind="slideVisible: appendexEStep1() == true">
					<span class="glyphicon glyphicon-share-alt"></span>
					<div class="col-md-12 form-group">
						<label class="control-label">
							Have prior surveys or evaluations conducted on the site already determined historic properties do not
							exist, or that prior disturbances have precluded the existence of historic properties? (<a
								href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 2)
						</label>
						<div class="radio">
							<label>
								<input type="radio" name="append-e-s2" data-bind="checkedValue: true, checked: appendexEStep2" />
								Yes
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="append-e-s2" data-bind="checkedValue: false, checked: appendexEStep2" />
								No
							</label>
						</div>
					</div>
				</div>
				<div class="row subquestion 2x" data-bind="slideVisible: appendexEStep2() == false">
					<span class="glyphicon glyphicon-share-alt"></span>
					<div class="col-md-12 form-group">
						<label class="control-label">
							Have you determined that your installation of subsurface earth-disturbing stormwater controls will have no
							effect on historic properties? (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 3)
						</label>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s3" data-bind="checkedValue: true, checked: appendexEStep3" />
								Yes
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s3" data-bind="checkedValue: false, checked: appendexEStep3" />
								No
							</label>
						</div>
					</div>
				</div>
				<div class="row subquestion 3x" data-bind="slideVisible: appendexEStep3() == false">
					<span class="glyphicon glyphicon-share-alt"></span>
					<div class="col-md-12 form-group">
						<label class="control-label">
							Did the SHPO, THPO, or other tribal representative (whichever applies) respond to you within the 15 calendar days to indicate whether the subsurface earth disturbances caused by the installation of stormwater controls affect historic properties? (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 4)
						</label>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s4" data-bind="checkedValue: true, checked: appendexEStep4" />
								Yes
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s4" data-bind="checkedValue: false, checked: appendexEStep4" />
								No
							</label>
						</div>
					</div>
				</div>
				<div class="row subquestion 4x" data-bind="slideVisible: appendexEStep4() == true">
					<span class="glyphicon glyphicon-share-alt"></span>
					<div class="col-md-12 form-group">
						<label class="control-label">Describe the nature of their response:</label>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s4-response" data-bind="checkedValue: 'written_no', checked: appendexEStep4Response" />
								Written Indication that no historic properties will be affected by the installation of storm water controls
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s4-response" data-bind="checkedValue: 'written_yes', checked: appendexEStep4Response" />
								Written indication that adverse effects to historic properties from the installation of stormwater controls can
								be mitigated by agreed upon actions
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s4-response" data-bind="checkedValue: 'no_agreement', checked: appendexEStep4Response" />
								No agreement has been reached regarding measures to mitigate affects to historic properties from the installation
								of stormwater controls
							</label>
						</div>
						<div class="radio">
							<label>
								<input type="radio" name="appendix-e-s4-response" data-bind="checkedValue: otherCheckedValue, checked: appendexEStep4Response" />
								Other:
							</label>
						</div>
						<textarea class="form-control" data-bind="slideVisible: appendexEStep4Response() == otherCheckedValue(), value: otherCheckedValue"></textarea>
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
			</fieldset>
		</div>
	</div>
</div>