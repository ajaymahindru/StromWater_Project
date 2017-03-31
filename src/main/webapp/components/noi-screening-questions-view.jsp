<!-- ko with: form().formSet -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="npdes">NPDES ID:</label>
            <span class="form-group-static" id="npdes"
            data-bind="text: npdesId() || oeca.cgp.noi.npdesId"></span>
        </div>

    </div>
</div>
<!-- /ko -->
<!-- ko with: form().formData -->
<div class="row" data-bind="validationOptions: {insertMessages: false}">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="site-state">State where your construction site is located:</label>
            <span class="form-group-static" id="site-state"
                  data-bind="text: projectSiteInformation.siteStateCode"></span>
        </div>

    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="tribe-question">Is your construction site located on Indian
                Country
                Lands?</label>
            <span class="form-group-static" id="tribe-question"
                  data-bind="text: projectSiteInformation.siteIndianCountry() ? 'Yes' : 'No'"></span>
        </div>
    </div>
</div>
<!-- ko if: projectSiteInformation.siteIndianCountry() == true -->
<div class="row subquestion">
    <span class="glyphicon glyphicon-share-alt"></span>
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="tribe-selection">Indian Country Land:</label>
            <span class="form-group-static" id="tribe-selection"
                  data-bind="text: projectSiteInformation.siteIndianCountryLands"></span>
        </div>
    </div>
</div>
<!-- /ko -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="federal-qustion"> Are you requesting coverage under this NOI as a
                <dfn
                        data-bind="popover: oeca.cgp.definitions.federalOperator">"Federal Operator"</dfn> as
                defined in
                <a href="${actionBean.cgpUrls.appendixA }" target="_blank">Appendix A</a>?</label>
            <span class="form-group-static" id="federal-qustion"
                  data-bind="text: operatorInformation.operatorFederal() ? 'Yes' : 'No'"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="storm-npdes-question">Have stormwater discharges from your current
                construction site been covered
                previously under an NPDES permit?</label>
            <span class="form-group-static" id="storm-npdes-question"
                  data-bind="text: projectSiteInformation.sitePreviousNpdesPermit() ? 'Yes':'No'"></span>
        </div>
    </div>
</div>
<!-- ko if: projectSiteInformation.sitePreviousNpdesPermit() == true -->
<div class="row subquestion">
    <span class="glyphicon glyphicon-share-alt"></span>
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="storm-npdes-number">Your most current NPDES ID:</label>
            <span class="form-group-static" id="storm-npdes-number"
                  data-bind="text: projectSiteInformation.sitePreviousNpdesPermitId"></span>
        </div>

    </div>
</div>
<!-- /ko -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="polymers-usage-question">Will you use polymers, flocculants, or other
                treatment chemicals at your
                construction site?</label>
            <span class="form-group-static" id="polymers-usage-question"
                  data-bind="text: chemicalTreatmentInformation.polymersFlocculantsOtherTreatmentChemicals() ? 'Yes':'No'"></span>
        </div>
    </div>
</div>
<!-- ko if: chemicalTreatmentInformation.polymersFlocculantsOtherTreatmentChemicals() == true -->
<div class="row subquestion">
    <span class="glyphicon glyphicon-share-alt"></span>
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="cationic-chemicals-usage">Will you use cationic treatment chemicals at
                your construction
                site?</label>
            <span class="form-group-static" id="cationic-chemicals-usage"
                  data-bind="text: chemicalTreatmentInformation.cationicTreatmentChemicals() ? 'Yes':'No'"></span>
        </div>
    </div>
</div>
<!-- ko if: chemicalTreatmentInformation.cationicTreatmentChemicals() == true -->
<div class="row subquestion 2x">
    <span class="glyphicon glyphicon-share-alt"></span>
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="cationic-chemicals-authorized">Have you been authorized to use cationic
                treatment chemicals by your
                applicable EPA Regional Office in advance of filing your NOI?</label>
            <span class="form-group-static" id="cationic-chemicals-authorized"
                  data-bind="text: chemicalTreatmentInformation.cationicTreatmentChemicalsAuthorization() ? 'Yes':'No'"></span>
        </div>
    </div>
</div>
<!-- /ko -->
<!-- /ko -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="swppp">Has a Stormwater Pollution Prevention Plan (SWPPP) been prepared in
                advance
                of filling this NOI, as required?</label>
            <span class="form-group-static" id="swppp"
                  data-bind="text: stormwaterPollutionPreventionPlanInformation.preparationInAdvance() ? 'Yes':'No'"></span>
        </div>

    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="endangered-species-question">Are you able to demonstrate that you meet one
                of the criteria listed in
                <a href="${actionBean.cgpUrls.appendixD }" target="_blank">Appendix D</a> with respect to protection
                of threatened or endangered species listed under the Endangered Species Act (ESA) and federally
                designated critical habitat?</label>
            <span class="form-group-static" id="endangered-species-question"
                  data-bind="text: endangeredSpeciesProtectionInformation.appendixDCriteriaMet()?'Yes':'No'"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="historic-properties-question">Have you completed the screening process in
                <a
                        href="${actionBean.cgpUrls.appendixE }" target="_blank">Appendix E</a> relating to the
                protection of historic properties?</label>
            <span class="form-group-static" id="historic-properties-question"
                  data-bind="text: historicPreservation.screeningCompleted()?'Yes':'No'"></span>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="confirmatin-question">By Indicating "Yes" below, I confirm that I
                understand that CGP only
                authorized the allowable stormwater discharges in Part 1.2.1 and the allowable non-stormwater
                discharges listed in Part 1.2.2. Any discharges not expressly authorized in this permit cannot
                become authorized or shielded from liability under CWA section 402(k) by disclosure to EPA, state or
                local authorities after issuance of this permit via any means, Including the Notice of Intent (NOI)
                to be covered by the permit, the Stormwater Pollution Prevention Plan (SWPPP), during an Inspection,
                etc. If any discharges requiring NPDES permit coverage other than the allowable stormwater and
                non-stormwater discharges listed in Parts 1.2.1 and 1.2.2 will be discharged, they must be covered
                under another NPDES permit.</label>
            <span class="form-group-static" id="confirmatin-question"
                  data-bind="text: projectSiteInformation.siteCgpAuthorizationConfirmation()?'Yes':'No'"></span>
        </div>
    </div>
</div>
<!-- /ko -->