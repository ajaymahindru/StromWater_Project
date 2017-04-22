<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="stripes"
           uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<stripes:layout-render name="view-templates.jsp"></stripes:layout-render>
<!-- ko if: form -->
<!-- ko with: form().formData -->
<div class="cor">
    <div class="cor-header">
        <div>
            NPDES<br>
            FORM
        </div>
        <div>
            <img width="150px" src="${pageContext.request.contextPath}/static/img/epa-logo-black.png"/>
        </div>
        <div>
            UNITED STATES ENVIRONMENTAL PROTECTION AGENCY<br>
            WASHINGTON, DC 20460<br>
            NOTICE OF TERMINATION (NOT) FOR THE 2017 NPDES CONSTRUCTION PERMIT
        </div>
        <div>
            FORM<br>
            Approved OMB No.<br>
            2-4-0004
        </div>
    </div>
    <p>This form provides notice to EPA that you, the project operator identified in Section II of this form,, are
        certifying that construction activity at the project site identified in Section III, will take place during a
        period when the rainfall erosivity factor is less than five <a href="JavaScript:">40 CFR 122.26(b)(15)(i)(A)
            TODO</a>. By submitting a complete and accurate form, the otherwise applicable NPDES permitting requirements
        for stormwater discharges associated with construction activity, are waived. Based on your certification, a
        waiver is granted for the period beginning on the date this Low Erosivity Waiver Form is mailed to EPA (i.e.,
        postmark date), or the project start date specified in Part IV of this form, whichever shall occur last, and
        ending on the project completion date specified in Part IV. Refer to the instructions at the end of this form
        for more details.</p>
    <div class="panel panel-default">
        <div class=" panel-heading">Permit Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-4">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="npdes-id">NPDES ID:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: $parent.form().formSet.npdesId
                                }
                            }" id="npdes-id"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-8">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="project-state">
                            State where your construction site is located:</label>
                        <span data-bind="template: {
                            name: 'underlined-field',
                            data: {
                                field: projectSiteInformation.siteStateCode
                            }
                        }" id="project-state"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-yes-no">
                        <label class="control-label" for="on-indian-country">Is your construction site located on Indian
                            Country Lands?</label>
                        <span data-bind="template: {
                            name: 'yes-no-boxes',
                            data: {
                                field: projectSiteInformation.siteIndianCountry
                            }
                        }" id="on-indian-country"></span>
                    </div>
                </div>
            </div>
            <div class="row subquestion">
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="indian-country">Indian Country Lands:</label>
                        <span data-bind="template: {
                            name: 'underlined-field',
                            data: {
                                field: projectSiteInformation.siteIndianCountryLands
                            }
                        }" id="indian-country"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-yes-no">
                        <label class="control-label" for="operator-federal">Are you requesting coverage under this NOI
                            as a
                            <dfn data-bind="popover: oeca.cgp.definitions.federalOperator">"Federal Operator"</dfn>
                            as defined in
                            <a href="${actionBean.cgpUrls.appendixA }" target="_blank">Appendix A</a>?</label>
                        <span data-bind="template: {
                            name: 'yes-no-boxes',
                            data: {
                                field: operatorInformation.operatorFederal
                            }
                        }" id="operator-federal"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-yes-no">
                        <label class="control-label" for="less-than-5-acres">Is construction activity at the project
                            site less than five (5) acres in area?</label>
                        <span data-bind="template: {
                            name: 'yes-no-boxes',
                            data: {
                                field: lowErosivityWaiver.lewLessThan5Acres
                            }
                        }" id="less-than-5-acres"></span>
                    </div>
                </div>
            </div>
            <div class="row subquestion">
                <div class="col-xs-12">
                    <div class="form-group cor-yes-no">
                        <label class="control-label" for="r-factor-less-than-5">Is your rainfall erosivity factor (<a
                                href="${actionBean.cgpUrls.rfactorCalculator }" target="_blank">R-Factor</a>) less than
                            five (5)?</label>
                        <span data-bind="template: {
                            name: 'yes-no-boxes',
                            data: {
                                field: lowErosivityWaiver.lewRFactorLessThan5
                            }
                        }" id="r-factor-less-than-5"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-default" data-bind="with: lowErosivityWaiver">
        <div class="panel-heading">Low Erosivity Waiver Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="estimated-start-date">Estimated Project Start Date:</label>
                        <span id="estimated-start-date" data-bind="template: {
                                            name: 'underlined-field',
                                            data: {
                                                field: lewProjectStart
                                            }
                                        }"></span>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="estimated-end-date">Estimated Project End Date:</label>
                        <span id="estimated-end-date" data-bind="template: {
                                            name: 'underlined-field',
                                            data: {
                                                field: lewProjectEnd
                                            }
                                        }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="lew-area-disturbed">Estimated Area to be Disturbed (in Acres):</label>
                        <span id="lew-area-disturbed" data-bind="template: {
                                            name: 'underlined-field',
                                            data: {
                                                field: lewAreaDisturbed
                                            }
                                        }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="lew-r-factor">Construction site's R-Factor</label>
                        <span id="lew-r-factor" data-bind="template: {
                                            name: 'underlined-field',
                                            data: {
                                                field: lewRFactor
                                            }
                                        }"></span>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="lew-r-factor-method">Rainfall Erosivity factor was calculated using:</label>
                        <span id="lew-r-factor-method" data-bind="template: {
                                            name: 'underlined-field',
                                            data: {
                                                field: lewRFactorCalculationMethodDisplay
                                            }
                                        }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="site-stabilization-measures">Are interim non vegetative site stabilization measures used to establish the project completion date for purposes of obtaining this waiver?</label>
                        <span id="site-stabilization-measures" data-bind="template: {
                                            name: 'yes-no-boxes',
                                            data: {
                                                field: interimSiteStabilizationMeasures
                                            }
                                        }"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-default" data-bind="with: operatorInformation">
        <div class="panel-heading">Operator Information</div>
        <div class="panel-body">
            <h4>Operator Information</h4>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="operator-name">Operator Name:</label>
                        <span id="operator-name" data-bind="template: {
                                                name: 'underlined-field',
                                                data: {
                                                    field: operatorName
                                                }
                                            }"></span>
                    </div>
                </div>
            </div>
            <h5>Mailing Address:</h5>
            <span data-bind="template: {
                                name: 'cor-address',
                                data: {
                                    address1: operatorAddress,
                                    city: operatorCity,
                                    state: operatorStateCode,
                                    zip: operatorZipCode,
                                    county: operatorCounty
                                }
                            }"></span>
            <h5>Operator Point of Contact Information</h5>
            <span data-bind="template: {
                                            name: 'cor-contact',
                                            data: {
                                                firstName: operatorPointOfContact.firstName,
                                                middleInitial: operatorPointOfContact.middleInitial,
                                                lastName: operatorPointOfContact.lastName,
                                                title: operatorPointOfContact.title,
                                                phone: operatorPointOfContact.phone,
                                                phoneExtension: operatorPointOfContact.phoneExtension,
                                                email: operatorPointOfContact.email
                                            }
                                        }"></span>
        </div>
    </div>
    <div class="panel panel-default" data-bind="with: projectSiteInformation">
        <div class="panel-heading">Project/Site Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="project-site-name">Project/Site Name:</label>
                        <span id="project-site-name" data-bind="template: {
                                            name: 'underlined-field',
                                            data: {
                                                field:  siteName
                                            }
                                        }"></span>
                    </div>
                </div>
            </div>
            <h4>Project/Site Address</h4>
            <div class="row">
                <div class="col-xs-12">
                    <span data-bind="template: {
                                name: 'cor-address',
                                data: {
                                    address1: siteAddress,
                                    city: siteCity,
                                    state: siteStateCode,
                                    zip: siteZipCode,
                                    county: siteCounty
                                }
                            }"></span>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <span data-bind="template: {
                                name: 'cor-location',
                                data: siteLocation
                            }"></span>
                </div>
            </div>
        </div>
    </div>
    <div data-bind="template: 'cor-certification'"></div>
</div>

<!-- /ko -->
<!-- /ko -->