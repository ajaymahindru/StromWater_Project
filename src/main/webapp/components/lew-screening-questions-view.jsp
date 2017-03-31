<!-- ko with: form().formSet -->
<div class="row">
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="npdes">NPDES ID:</label>
            <span class="form-group-static" id="npdes"
                  data-bind="text: npdesId"></span>
        </div>

    </div>
</div>
<!-- /ko -->
<!-- ko with: form().formData -->
<div class="row">
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
<!-- ko if: projectSiteInformation.siteIndianCountry() -->
<div class="row subquestion">
    <span class="glyphicon glyphicon-share-alt"></span>
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="tribe-selection">Indian Country Land:</label>
            <span class="form-group-static" id="tribe-selection"
                  data-bind="text: projectSiteInformation.siteIndianCountryLands()"></span>
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
            <label class="control-label" for="acres-question">Is construction activity at the project site less
                than
                five (5) acres in area?</label>
            <span class="form-group-static" id="acres-question"
                  data-bind="text: lowErosivityWaiver.lewLessThan5Acres() ? 'Yes' : 'No'"></span>
        </div>
    </div>
</div>
<div class="row subquestion">
    <span class="glyphicon glyphicon-share-alt"></span>
    <div class="col-sm-12">
        <div class="form-group">
            <label class="control-label" for="rfactor-question">Is your rainfall erosivity factor (<a
                    href="${actionBean.cgpUrls.rfactorCalculator }" target="_blank">R-Factor</a>) less than five
                (5)?</label>
            <span class="form-group-static" id="rfactor-question"
                  data-bind="text: lowErosivityWaiver.lewRFactorLessThan5() ? 'Yes' : 'No'"></span>
        </div>
    </div>
</div>
<!-- /ko -->