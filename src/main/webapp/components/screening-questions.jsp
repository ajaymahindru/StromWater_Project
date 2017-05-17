<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
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
        <p>To ensure your eligibility under the CGP, please respond to the following interview questions:</p>
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
                <label class="control-label">
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
            <a href="${actionBean.cgpUrls.appendixB}" target="_blank">Appendix B</a> of the CGP for the list of areas
            that the CGP covers. Contact your
            <a href="${actionBean.cgpUrls.npdesPermitOffice}" target="_blank">state NPDES permitting office</a> for
            information about getting coverage under your
            state's permit.
        </div>
        <!-- ko if: eligibilityChecker.isEligible -->
        <div class="alert alert-info" data-bind="html: federalOperatorText, slideVisible: bia() == false && eligibilityChecker.isEligible() == true && federalOperatorText() != null">
        </div>
        <hr data-bind="slideVisible: showStormwater">
        <div class="row" data-bind="slideVisible: showStormwater, scrollTo: showStormwater">
            <div class="col-sm-12 form-group">
                <label class="control-label">Have stormwater discharges from your current construction site been covered
                    previously under an NPDES permit?</label>
                <div class="radio">
                    <label><input type="radio" name="npdesPermitQuestion"
                                  data-bind="checkedValue: true, checked: npdesPermitQuestion"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="npdesPermitQuestion"
                                  data-bind="checkedValue: false, checked: npdesPermitQuestion"/>No</label>
                </div>
            </div>
        </div>
        <div class="row subquestion" data-bind="slideVisible: showPermitNumber, scrollTo: showPermitNumber">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12 form-group">
                <label class="control-label" for="npdesId">
                    Provide your most current NPDES ID if you had coverage under EPA's 2012 CGP or an individual NPDES permit.
                </label>
                <br/>
                <input type="text" id="npdesId" class="form-control" data-bind="textInput: npdesPermit"
                       style="width: 300px" maxlength="9"/>
            </div>
        </div>
        <!-- ko ifnot: permitNumberNext -->
        <div class="row subquestion">
            <div class="col-sm-12">
                <button class="btn btn-primary"
                        data-bind="slideVisible: showPermitNumber, enable: npdesPermit, click: function() {permitNumberNext(true)}">
                    Next
                </button>
            </div>
        </div>
        <!-- /ko -->
        <hr data-bind="slideVisible: showChemicals">
        <div class="row" data-bind="slideVisible: showChemicals, scrollTo: showChemicals">
            <div class="col-sm-12 form-group">
                <label class="control-label">Will you use polymers, flocculants, or other treatment chemicals at your
                    construction site?</label>
                <div class="radio">
                    <label><input type="radio" name="treatment"
                                  data-bind="checkedValue: true, checked: treatmentChemicals"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="treatment"
                                  data-bind="checkedValue: false, checked: treatmentChemicals"/>No</label>
                </div>
            </div>
        </div>
        <div class="row subquestion" data-bind="slideVisible: treatmentChemicals, scrollTo: treatmentChemicals">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12 form-group">
                <label class="control-label">Will you use cationic treatment chemicals at your construction
                    site?</label>
                <div class="radio">
                    <label><input type="radio" name="cationic"
                                  data-bind="checkedValue: true, checked: cationicTreatmentChemicals"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="cationic"
                                  data-bind="checkedValue: false, checked: cationicTreatmentChemicals"/>No</label>
                </div>
            </div>
        </div>
        <div class="row subquestion 2x"
             data-bind="slideVisible: cationicTreatmentChemicals, scrollTo: cationicTreatmentChemicals">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12 form-group">
                <label class="control-label">Have you been authorized to use cationic treatment chemicals by your
                    applicable EPA Regional Office in advance of filing your NOI?</label>
                <div class="radio">
                    <label><input type="radio" name="cationic-authorized"
                                  data-bind="checkedValue: true, checked: cationicAuthorized"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="cationic-authorized"
                                  data-bind="checkedValue: false, checked: cationicAuthorized"/>No</label>
                </div>
            </div>
        </div>
        <!-- ko if: cationicAuthorized() == false -->
        <div class="alert alert-danger">
            You are ineligible for coverage under this permit unless you notify your applicable EPA Regional Office in
            advance and the EPA office authorizes coverage under this permit after you have included appropriate
            controls and implementation procedures designed to ensure that your use of cationic treatment chemicals will
            not lead to a violation of water quality standards.
        </div>
        <!-- /ko -->
        <!-- ko if: cationicAuthorized() == true -->
        <div class="alert alert-info">
            At the end of the application process you will be required to provide a list of the treatment chemicals you
            will use. Additionally you will need to attach a copy of your authorization letter and include documentation
            of the appropriate controls and implementation procedures designed to ensure that your use of cationic
            treatment chemicals will not lead to a violation of water quality standards.
        </div>
        <!-- /ko -->
        <hr data-bind="slideVisible: showSwppp, scrollTo: showSwppp">
        <div class="row" data-bind="slideVisible: showSwppp">
            <div class="col-sm-12 form-group">
                <label class="control-label">Has a Stormwater Pollution Prevention Plan (SWPPP) been prepared in advance
                    of filling this NOI, as required?</label>
                <div class="radio">
                    <label><input type="radio" name="swppp" data-bind="checkedValue: true, checked: swppp"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="swppp" data-bind="checkedValue: false, checked: swppp"/>No</label>
                </div>
            </div>
        </div>
        <!-- ko if: swppp() == false -->
        <div class="alert alert-danger">
            You may not submit your NOI for coverage under the CGP if you have not developed your Stormwater Pollution
            Prevention Plan (SWPPP). For information about what information is required in your SWPPP,
            <a href="${actionBean.cgpUrls.cgpDocs }" target="_blank">see Part 7 of the permit</a>.
        </div>
        <!-- /ko -->
        <div class="row" data-bind="slideVisible: swppp, scrollTo: swppp">
            <div class="col-sm-12 form-group">
                <label class="control-label">Are you able to demonstrate that you meet one of the criteria listed in
                    <a href="${actionBean.cgpUrls.appendixD }" target="_blank">Appendix D</a> with respect to protection
                    of threatened or endangered species listed under the Endangered Species Act (ESA) and federally
                    designated critical habitat?</label>
                <div class="radio">
                    <label><input type="radio" name="endangered-species"
                                  data-bind="checkedValue: true, checked: endangeredSpecies"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="endangered-species"
                                  data-bind="checkedValue: false, checked: endangeredSpecies"/>No</label>
                </div>
            </div>
        </div>
        <!-- ko if: endangeredSpecies() == false -->
        <div class="alert alert-danger">
            You must meet one of the criteria listed in <a href="${actionBean.cgpUrls.appendixD}" target="_blank">Appendix
            D</a> to be eligible to be covered by this permit.
        </div>
        <!-- /ko -->
        <!-- ko if: endangeredSpecies() == true -->
        <div class="alert alert-info">
            You will be required to provide more information on endangered species protection later in the form.
        </div>
        <!-- /ko -->
        <div class="row" data-bind="slideVisible: endangeredSpecies, scrollTo: endangeredSpecies">
            <div class="col-sm-12 form-group">
                <label class="control-label">Have you completed the screening process in <a
                        href="${actionBean.cgpUrls.appendixE }" target="_blank">Appendix E</a> relating to the
                    protection of historic properties?</label>
                <div class="radio">
                    <label><input type="radio" name="historic"
                                  data-bind="checkedValue: true, checked: historicProperties"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="historic"
                                  data-bind="checkedValue: false, checked: historicProperties"/>No</label>
                </div>
            </div>
        </div>
        <!-- ko if: historicProperties() == true -->
        <div class="alert alert-info">
            You will be required to provide more information on historic properties screening later in the form.
        </div>
        <!-- /ko -->
        <!-- ko if: historicProperties() == false -->
        <div class="alert alert-danger">
            You must complete the screening process as required in <a href="${actionBean.cgpUrls.appendixE}"
                                                                      target="_blank">Appendix E</a> to be eligible to
            be covered by this permit.
        </div>
        <!-- /ko -->
        <hr data-bind="slideVisible: showAgreement">
        <div class="row" data-bind="slideVisible: showAgreement, scrollTo: showAgreement">
            <div class="col-sm-12 form-group">
                <label class="control-label">By Indicating "Yes" below, I confirm that I understand that CGP only
                    authorized the allowable stormwater discharges in Part 1.2.1 and the allowable non-stormwater
                    discharges listed in Part 1.2.2. Any discharges not expressly authorized in this permit cannot
                    become authorized or shielded from liability under CWA section 402(k) by disclosure to EPA, state or
                    local authorities after issuance of this permit via any means, Including the Notice of Intent (NOI)
                    to be covered by the permit, the Stormwater Pollution Prevention Plan (SWPPP), during an Inspection,
                    etc. If any discharges requiring NPDES permit coverage other than the allowable stormwater and
                    non-stormwater discharges listed in Parts 1.2.1 and 1.2.2 will be discharged, they must be covered
                    under another NPDES permit.</label>
                <div class="radio">
                    <label><input type="radio" name="confirmation"
                                  data-bind="checkedValue: true, checked: npdesConfirm"/>Yes</label>
                </div>
                <div class="radio">
                    <label><input type="radio" name="confirmation"
                                  data-bind="checkedValue: false, checked: npdesConfirm"/>No</label>
                </div>
            </div>
        </div>
        <!-- ko if: npdesConfirm() == false -->
        <div class="alert alert-danger">
            To continue, you must affirm yes to this statement.
        </div>
        <!-- /ko -->
        <button class="btn btn-primary" data-bind="slideVisible: npdesConfirm, click: next" onclick="oeca.utils.scrollToTop();">Next</button>
        <!-- /ko -->
        <!-- /ko -->
        <!-- ko if: screeningQuestionsLocked -->
        <noi-screening-questions-view params="form: form"></noi-screening-questions-view>
        <!-- /ko -->
    </div>
</div>