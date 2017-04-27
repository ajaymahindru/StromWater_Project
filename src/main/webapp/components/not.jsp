<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="stripes"
           uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>
<stripes:layout-render name="view-templates.jsp"></stripes:layout-render>
<!-- ko if: form -->
<!-- ko with: form().formData -->
<div class="cor">
    <div class="row cor-header">
        <div class="col-xs-2">
            NPDES<br>
            FORM<br>
            3510-13
        </div>
        <div class="col-xs-3">
            <img width="150px" src="${pageContext.request.contextPath}/static/img/epa-logo-black.png"/>
        </div>
        <div class="col-xs-5">
            UNITED STATES ENVIRONMENTAL PROTECTION AGENCY<br>
            WASHINGTON, DC 20460<br>
            NOTICE OF TERMINATION (NOT) FOR THE 2017 NPDES CONSTRUCTION PERMIT
        </div>
        <div class="col-xs-2">
            FORM<br>
            Approved OMB No.<br>
            2040-0004
        </div>
    </div>
    <p>Submission of this Notice of Termination constitutes notice that the operator identified in Section III
        of this form is no longer authorized discharge pursuant to the NPDES Construction General Permit (CGP)
        from the site identified in Section IV of this form. All necessary information must be included on this
        form. Refer to the instructions at the end of this form.</p>
    <div class="panel panel-default">
        <div class="panel-heading">Permit Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-12">
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
                <div class="col-sm-12">
                    <div class="form-group">
                        <label class="control-label">Reason for Termination:</label>

                        <%--<div class="cor-boxes">
                            <div data-bind="template: {name: 'view-checkbox', data: {isChecked: false, text: 'Something'}}"></div>
                            <label>
                                <input type="radio" class="sr-only" disabled/>
                                <span class="fa fa-check-square-o"></span>
                                You have completed earth-disturbing activities at your site, and you have met all other requirements in Part 8.2.1.
                            </label>
                            <label>
                                <input type="radio" class="sr-only" disabled/>
                                <span class="fa fa-square-o"></span>
                                Another operator has assumed cntrol over all areas of the site and that operator has submitted an NOI and obtained coverage under the CGP.
                            </label>
                            <label>
                                <input type="radio" class="sr-only" disabled/>
                                <span class="fa fa-square-o"></span>
                                You have obtained coverage under an individual permit or anohter general NPDES permit addressing stormwater discharges from the constructionsite.
                            </label>
                        </div>--%>
                        <span class="form-control-static" data-bind="text: projectSiteInformation.siteTerminationReason"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-default" data-bind="with: operatorInformation">
        <div class="panel-heading">Operator Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="operator-name">Operator Name:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: operatorName
                                }
                            }" id="operator-name"></span>
                    </div>
                </div>
            </div>
            <div data-bind="template: {
                    name: 'cor-address',
                    data: {
                        address1: operatorAddress,
                        city: operatorCity,
                        state: operatorStateCode,
                        zip: operatorZipCode,
                        county: operatorCounty
                    }
                }"></div>
            <div data-bind="template: {
                        name: 'cor-contact',
                        data: {
                            phone: operatorPointOfContact.phone,
                            phoneExtension: operatorPointOfContact.phoneExtension,
                            email: operatorPointOfContact.email
                        }
                    }"></div>
            <div class="h4">Operator Point of Contact</div>
            <div data-bind="template: {
                    name: 'cor-contact',
                    data: {
                        firstName: operatorPointOfContact.firstName,
                        middleInitial: operatorPointOfContact.middleInitial,
                        lastName: operatorPointOfContact.lastName
                    }
                }"></div>
        </div>
    </div>
    <div class="panel panel-default" data-bind="with: projectSiteInformation">
        <div class="panel-heading">Project/Site Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="site-name">Project/Site Name:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: siteName
                                }
                            }" id="site-name"></span>
                    </div>
                </div>
            </div>
            <hr>
            <div class="h4">Project/Site Address</div>
            <div data-bind="template: {
                    name: 'cor-address',
                    data: {
                        address1: siteAddress,
                        city: siteCity,
                        state: siteStateCode,
                        zip: siteZipCode,
                        county: siteCounty
                    }
                }"></div>
            <%--<address-view params="address: {
                        address1: siteAddress,
                        city: siteCity,
                        state: siteStateCode,
                        zip: siteZipCode,
                        county: siteCounty
                    }"></address-view>--%>
        </div>
    </div>
    <div data-bind="template: 'cor-certification'"></div>
</div>
<!-- /ko -->
<!-- /ko -->
