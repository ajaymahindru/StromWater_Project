<div class="panel panel-info" data-bind="disableSection: screeningQuestionsLocked, template: {afterRender: componentLoaded() }">
    <div class="panel-heading">
        <div data-bind="click: function(){expand(!expand())}">
            <span data-bind="visible: screeningQuestionsLocked" class="glyphicon glyphicon-lock"></span>
            <a role="button" href="JavaScript:;">Permit Information</a>
            <!-- ko if: expand -->
            <span class="glyphicon glyphicon-chevron-down pull-right"></span>
            <!-- /ko -->
            <!-- ko ifnot: expand -->
            <span class="glyphicon glyphicon-chevron-up pull-right"></span>
            <!-- /ko -->
        </div>
    </div>
    <div class="panel-body" id="sectionA" data-bind="slideVisible: expand">
        <!-- ko ifnot: screeningQuestionsLocked -->
        <div class="row">
            <div class="col-sm-12 form-group">
                <label class="control-label" for="state">Select the state where your construction site is
                    located</label>
                <br/>
                <select id="state" class="form-control" data-bind="lookup: 'states',
																	value: state,
																	optionsValue: 'stateCode',
																	optionsText: 'stateName',
																	optionsCaption: '',
																	valueAllowUnset: true,
																	select2: {
																		placeholder: 'Select State'
																	}" style="width: 300px">
                </select>
            </div>
        </div>
        <div class="row" data-bind="slideVisible: showBia, scrollTo: showBia">
            <div class="col-sm-12 form-group">
                <label class="control-label">Is your construction site located on Indian Country lands?</label>
                <div class="radio">
                    <label><input type="radio" name="bia" data-bind="checkedValue: true, checked: bia"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="bia" data-bind="checkedValue: false, checked: bia"/>No</label>
                </div>
            </div>
        </div>
        <div class="row subquestion" data-bind="slideVisible: showTribe, scrollTo: showTribe">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12 form-group">
                <div>
                    <label class="control-label" for="biaCode">Select the Indian Country lands</label>
                    <br/>
                    <select id="biaCode" class="form-control" data-bind="options: tribes,
																			value: biaCode,
																			optionsValue: 'tribalName',
																			optionsText: 'tribalName',
																			optionsCaption: '',
																			valueAllowUnset: true,
																			select2: {
																				placeholder: 'Select Tribe'
																			}" style="width: 500px">
                    </select>
                </div>
            </div>
        </div>
        <hr data-bind="slideVisible: showCoverage">
        <div class="row" data-bind="slideVisible: showCoverage, scrollTo: showCoverage">
            <div class="col-sm-12 form-group">
                <label class="control-label" for="federalOperator">
                    Are you requesting coverage under this NOI as a <dfn
                        data-bind="popover: oeca.cgp.definitions.federalOperator">"Federal Operator"</dfn> as defined in
                    <a href="${actionBean.cgpUrls.appendixA }" target="_blank">Appendix A</a>?
                </label>
                <div class="radio">
                    <label><input type="radio" name="federalOperator"
                                  data-bind="checkedValue: true, checked: federalOperator"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="federalOperator"
                                  data-bind="checkedValue: false, checked: federalOperator"/>No</label>
                </div>
            </div>
        </div>
        <!-- ko if: eligibilityChecker.checkingEligibility -->
        <span data-bind="fadeVisible: eligibilityChecker.checkingEligibility"
              class="fa fa-spinner fa-2x fa-spin"></span>
        <!-- /ko -->
        <div class="alert alert-danger" data-bind="slideVisible: eligibilityChecker.isEligible() == false">
            EPA is not the NPDES permitting authority in the area for which you are requesting permit coverage. Please
            refer to
            <a href="${actionBean.cgpUrls.appendixB}" target="_blank">Appendix B</a> of the CGP for the list of areas that the CGP covers. Contact your
            <a href="${actionBean.cgpUrls.npdesPermitOffice}" target="_blank">state NPDES permitting office</a> for information about getting coverage under
            your state's permit.
        </div>
        <!-- ko if: eligibilityChecker.isEligible -->
        <div class="alert alert-info" data-bind="html: federalOperatorText, slideVisible: bia() == false && eligibilityChecker.isEligible() == true && federalOperatorText() != null">
        </div>
        <hr data-bind="slideVisible: federalOperator() != null">
        <div class="row" data-bind="slideVisible: federalOperator() != null, scrollTo: federalOperator() != null">
            <div class="col-sm-12 form-group">
                <label class="control-label">Is construction activity at the project site less than five (5) acres in area?</label>
                <div class="radio">
                    <label><input type="radio" name="site-acres-question"
                                  data-bind="checkedValue: true, checked: acresQuestion"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="site-acres-question"
                                  data-bind="checkedValue: false, checked: acresQuestion"/>No</label>
                </div>
            </div>
        </div>
        <div class="alert alert-danger" data-bind="slideVisible: acresQuestion() == false">
            Your construction site is greater than 5 acres. You do not qualify for a LEW and must complete the NOI form.
            <button class="btn btn-default" data-bind="click: function(){pager.navigate('#!/noi');}">Create a new NOI
            </button>
        </div>
        <div class="row subquestion" data-bind="slideVisible: acresQuestion(), scrollTo: acresQuestion()">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12 form-group">
                <label class="control-label">Is your rainfall erosivity factor (<a
                        href="${actionBean.cgpUrls.rfactorCalculator }" target="_blank">R-Factor</a>) less than five
                    (5)?</label>
                <div class="radio">
                    <label><input type="radio" name="rfactor-question"
                                  data-bind="checkedValue: true, checked: rfactorQuestion"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="rfactor-question"
                                  data-bind="checkedValue: false, checked: rfactorQuestion"/>No</label>
                </div>
            </div>
        </div>
        <div class="alert alert-danger" data-bind="slideVisible: rfactorQuestion() == false">
            Your construction site has an R-Factor greater than 5, you do not qualify for a LEW and must complete the
            NOI form.
            <button class="btn btn-default" data-bind="click: function(){pager.navigate('#!/noi');}">Create a new NOI
            </button>
        </div>
        <button class="btn btn-primary" data-bind="slideVisible: screeningFinished, click: next"
                onclick="oeca.utils.scrollToTop();">Next</button>
        <!-- /ko -->
        <!-- /ko -->
        <!-- ko if: screeningQuestionsLocked -->
        <lew-screening-questions-view params="form: $parent.form"></lew-screening-questions-view>
        <!-- /ko -->
    </div>
</div>