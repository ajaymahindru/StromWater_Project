<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<stripes:layout-render name="/layout/modal-layout.jsp" type="danger" title="<span class='glyphicon glyphicon-remove-sign'></span>" addClass="modal-lg">
    <stripes:layout-component name="body">
        <button style="display:none" id="sign-terminate"></button>
        <!-- ko if: panel() == 'warning' -->
            <div class="h4">Warning</div>
            <!-- ko if: type() == 'Low_Erosivity_Waiver' -->
            <p>You have selected Discontinue.  Are you sure you want to change this forms status to Discontinued?</p>
            <!-- /ko -->
            <!-- ko if: type() == 'Notice_Of_Intent' -->
            <p>You have selected Terminate.  Are you sure you want to change this forms status to Terminated?</p>
            <!-- /ko -->
        <!-- /ko -->
        <!-- ko if: panel() == 'confirmation' -->
            <!-- ko if: type() == 'Low_Erosivity_Waiver' -->
            <div class="h4">Notice of Discontinuation</div>
            <!-- /ko -->
            <!-- ko if: type() == 'Notice_Of_Intent' -->
            <div class="h4">Notice of Termination</div>
            <!-- /ko -->
            <div class="text-left">
                <div class="form-group">
                    <label class="control-label">Project/Site Name</label>
                    <span class="form-control-static" data-bind="text: formData.projectSiteInformation.siteName"></span>
                </div>
                <div class="form-group">
                    <label class="control-label">Operator Name</label>
                    <span class="form-control-static" data-bind="text: formData.operatorInformation.operatorName"></span>
                </div>
                <div class="form-group">
                    <label class="control-label">NPDES ID</label>
                    <span class="form-control-static" data-bind="text: formSet.npdesId"></span>
                </div>
                <div class="form-group">
                    <label class="control-label">
                        <!-- ko if: type() == 'Low_Erosivity_Waiver' -->
                        Current Status
                        <!-- /ko -->
                        <!-- ko if: type() == 'Notice_Of_Intent' -->
                        Current Permit Status
                        <!-- /ko -->
                    </label>
                    <span class="form-control-static" data-bind="text: status"></span>
                </div>
                <!-- ko if: type() == 'Low_Erosivity_Waiver' -->
                <div class="form-group">
                    <label class="control-label" for="termination-reason">Reason for Discontinuation:</label>
                    <textarea id="termination-reason" class="form-control" data-bind="value: formData.projectSiteInformation.siteTerminationReason"></textarea>
                    <span class="help-block">Providing a Reason is Optional.</span>
                </div>
                <!-- /ko -->
                <!-- ko if: type() == 'Notice_Of_Intent' -->
                <div class="form-group required">
                    <label class="control-label">Reason for Termination:</label>
                    <div class="radio">
                        <label><input type="radio" name="termination-reason" data-bind="checked: formData.projectSiteInformation.siteTerminationReason,
                        checkedValue: 'Change of Control over the site.'">
                            Another operator has assumed control over all areas for the site and that operator has submitted an NOI and obtained coverage under the CGP.</label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="termination-reason" data-bind="checked: formData.projectSiteInformation.siteTerminationReason,
                        checkedValue: 'Earth Disturbing Activities Complete, Part 8.2.1 requirements met.'">
                            You have completed earth-disturbing activities at your site, and you have met all other requirements in Part 8.2.1.</label>
                    </div>
                    <div class="radio">
                        <label><input type="radio" name="termination-reason" data-bind="checked: formData.projectSiteInformation.siteTerminationReason,
                        checkedValue: 'Other Permit coverage obtained.'">
                            You have obtained coverage under an individual permit or another general NPDES permit addressing stormwater discharges from the construction site.</label>
                    </div>
                </div>
                <!-- /ko -->
                <sec:authorize access="hasAnyRole('${actionBean.helpdeskRole}')">
                    <div class="h4">Certifier</div>
                    <div class="row">
                        <div class="col-sm-5 form-group required">
                            <label class="control-label" for="first-name">First Name</label>
                            <input id="first-name" class="form-control" type="text" maxlength="30" data-bind="value: formData.operatorInformation.certifier.firstName" />
                        </div>
                        <div class="col-sm-2 form-group">
                            <label class="control-label" for="middle-initial">Middle Initial</label>
                            <input id="middle-initial" class="form-control" type="text" maxlength="1" data-bind="value: formData.operatorInformation.certifier.middleInitial" />
                        </div>
                        <div class="col-sm-5 form-group required">
                            <label class="control-label" for="last-name">Last Name</label>
                            <input id="last-name" class="form-control" type="text" maxlength="30" data-bind="value: formData.operatorInformation.certifier.lastName" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-5 form-group required">
                            <label class="control-label" for="poc-title">Title</label>
                            <input id="poc-title" class="form-control" type="text" maxlength="40" data-bind="value: formData.operatorInformation.certifier.title" />
                        </div>
                        <div class="col-sm-7 form-group required">
                            <label class="control-label" for="email">Email</label>
                            <input id="email" class="form-control" type="text" maxlength="100" data-bind="value: formData.operatorInformation.certifier.email" />
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-sm-5">
                            <div class="form-group required">
                                <label class="control-label" for="certified-date">Date Terminated:</label>
                                <input type="text" class="form-control" id="certified-date" data-bind="date: terminationDate, datepicker, dateFormats: {from: 'YYYY-MM-DDTHH:mm:ss.SSSZZ', to: 'MM/DD/YYYY'}">
                            </div>
                        </div>
                    </div>
                </sec:authorize>
            </div>
        <!-- /ko -->
        <!-- ko if: showUserSearch -->
            <user-search params="id: 'terminate-to-certifier', callback: confirmCertifier, type: 'Certifier'"></user-search>
        <!-- /ko -->
    </stripes:layout-component>
    <stripes:layout-component name="footer">
        <button class="btn btn-danger-outline" data-bind="close: 'cancel'">Return</button>
        <!-- ko if: popupModel().panel() == 'warning' -->
        <button class="btn btn-danger" data-bind="click: popupModel().continue">
            <!-- ko if: popupModel().type() == 'Low_Erosivity_Waiver' -->
            Discontinue Form
            <!-- /ko -->
            <!-- ko if: popupModel().type() == 'Notice_Of_Intent' -->
            Terminate Form
            <!-- /ko -->
        </button>
        <!-- /ko -->
        <sec:authorize access="hasAnyRole('${actionBean.certifierRole}', '${actionBean.regAuthRole}')">
            <!-- ko if: popupModel().panel() == 'confirmation' -->
            <button id="confirm-terminate" class="btn btn-danger" data-bind="click: popupModel().confirmSignTerminate, disable: popupModel().terminating()">Confirm</button>
            <!-- /ko -->
        </sec:authorize>
        <sec:authorize access="hasAnyRole('${actionBean.helpdeskRole}')">
            <!-- ko if: popupModel().panel() == 'confirmation' -->
            <button id="confirm-terminate" class="btn btn-danger" data-bind="click: popupModel().confirmTerminate, disable: popupModel().terminating()">Confirm</button>
            <!-- /ko -->
        </sec:authorize>
        <sec:authorize access="hasRole('${actionBean.preparerRole}')">
            <!-- ko if: popupModel().panel() == 'confirmation' -->
            <button class="btn btn-danger" data-bind="click: popupModel().submitToCertifier">Submit to Certifier</button>
            <!-- /ko -->
        </sec:authorize>
    </stripes:layout-component>
</stripes:layout-render>