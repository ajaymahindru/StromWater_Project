<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="stripes"
           uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>
<stripes:layout-render name="view-templates.jsp"></stripes:layout-render>
<!-- ko if: form -->
<!-- ko with: form().formData -->
<div class="cor">
    <div class="cor-header">
        <div>
            NPDES<br>
            FORM<br>
            3510-9
        </div>
        <div>
            EPA LOGO
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
    <div class="row">
        <div class="col-xs-12">
            <p>Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah Blah </p>
        </div>
    </div>
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
                        lastName: operatorPointOfContact.lastName,
                        title: operatorPointOfContact.title
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
    <div class="panel panel-default">
        <div class="panel-heading">Certification Information</div>
        <div class="panel-body">
            <!-- Needed for CoR since certification information is not saved until after CoR generation -->
            <div data-bind="visible: $root.viewCor === 'true'">
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label" for="certifier-name">Certified By:</label>
                            <span class="form-group-static" id="certifier-name">
                                ${actionBean.user.name}
                                (${actionBean.user.username})
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label" for="certified-date">Certified On:</label>
                            <span class="form-group-static" id="certified-date" data-bind="text: oeca.cgp.utils.formatDateTime(new Date())"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <p>
                            I certify under penalty of law that this document and all attachments were prepared under my direction or supervision in accordance with a system designed to assure that qualified personnel properly gathered and evaluated the information submitted.
                            Based on my inquiry of the person or persons who manage the system, or those persons directly responsible for gathering the information, the information submitted is, to the best of my knowledge and belief, true, accurate, and complete.
                            I have no personal knowledge that the information submitted is other than true, accurate, and complete. I am aware that there are significant penalties for submitting false information, including the possibility of fine and imprisonment for knowing violations.
                            Signing an electronic document on behalf of another person is subject to criminal, civil, administrative, or other lawful action.
                        </p>
                    </div>
                </div>
            </div>

            <div data-bind="visible: $root.viewCor !== 'true'">
                <!-- ko if: operatorInformation.certifier() -->
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label" for="certifier-name">Certified By:</label>
                            <span class="form-group-static" id="certifier-name">
                                <span data-bind="text: operatorInformation.certifier().name"></span>
                                <!-- ko if: operatorInformation.certifier().userId -->
                                (<span data-bind="text: operatorInformation.certifier().userId"></span>)
                                <!-- /ko -->
                            </span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label" for="certified-date">Certified On:</label>
                            <span class="form-group-static" id="certified-date" data-bind="text: oeca.cgp.utils.formatDateTime($parent.form().certifiedDate())"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <p>
                            I certify under penalty of law that this document and all attachments were prepared under my direction or supervision in accordance with a system designed to assure that qualified personnel properly gathered and evaluated the information submitted.
                            Based on my inquiry of the person or persons who manage the system, or those persons directly responsible for gathering the information, the information submitted is, to the best of my knowledge and belief, true, accurate, and complete.
                            I have no personal knowledge that the information submitted is other than true, accurate, and complete. I am aware that there are significant penalties for submitting false information, including the possibility of fine and imprisonment for knowing violations.
                            Signing an electronic document on behalf of another person is subject to criminal, civil, administrative, or other lawful action.
                        </p>
                    </div>
                </div>
                <!-- /ko -->
                <!-- ko ifnot: operatorInformation.certifier -->
                Form has not been certified yet.
                <!-- /ko -->
            </div>
        </div>
    </div>
</div>
<!-- /ko -->
<!-- /ko -->
