<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">Low Erosivity Waiver Information</a>
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
	<div class="panel-body" id="sectionA" data-bind="slideVisible: expand">
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label" for="estimated-project-start-date">Estimated Project Start Date</label>
				<input id="estimated-project-start-date" class="form-control" type="text" data-bind="value: lewProjectStart, datepicker" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label" for="estimated-project-end-date">Estimated Project End Date</label>
				<input id="estimated-project-end-date" class="form-control" type="text" data-bind="value: lewProjectEnd, datepicker" />
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label" for="area-disturbed">Estimated Area to be Disturbed (in Acres)</label>
				<input id="area-disturbed" class="form-control" type="number" data-bind="value: lewAreaDisturbed"/>
				<span class="help-block">To the nearest quarter acre</span>
			</div>
		</div>
		<!-- ko if: lewRFactor() && recalcRFactor.isDirty() -->
		<div class="alert alert-warning">
			You will need to recalculate your R-Factor.
		</div>
		<!-- /ko -->
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label" for="site-r-factor">What is your construction site's R-Factor?</label>
				<input id="site-r-factor" class="form-control" type="text" data-bind="value: lewRFactor" />
				<span class="help-block"><a href="${actionBean.cgpUrls.rfactorCalculator }" target="_blank">R-Factor Calculator</a></span>
			</div>
		</div>
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label">Rainfall Erosivity factor was calculated by using:</label>
				<div class="radio">
					<label>
						<input type="radio" name="r-factor-calc-method"
							data-bind="checkedValue: 'online-calculator', checked: lewRFactorCalculationMethod" />
						Online Calculator
					</label>
				</div>
				<div class="radio">
					<label>
						<input type="radio" name="r-factor-calc-method" data-bind="checkedValue: 'fact-sheet', checked: lewRFactorCalculationMethod" />
						EPA Fact Sheet 3.1
					</label>
				</div>
				<div class="radio">
					<label>
						<input type="radio" name="r-factor-calc-method"
							data-bind="checkedValue: 'usda-handbook', checked: lewRFactorCalculationMethod" />
						USDA Handbook 703
					</label>
				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-md-12 form-group">
				<label class="control-label">Are interim non-vegetative site stabilization measures used to establish the
					project completion date for purposes of obtaining this waiver?</label>
				<div class="radio">
					<label>
						<input type="radio" name="stabilization-measures" data-bind="checkedValue: true, checked: interimSiteStabilizationMeasures" />
						Yes
					</label>
				</div>
				<div class="radio">
					<label>
						<input type="radio" name="stabilization-measures" data-bind="checkedValue: false, checked: interimSiteStabilizationMeasures" />
						No
					</label>
				</div>
			</div>
		</div>
		<button class="btn btn-primary" data-bind="click: next"
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