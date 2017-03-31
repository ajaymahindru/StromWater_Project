<!-- ko if: form -->
<!-- ko with: form().formData -->
<div class="panel panel-info">
    <div class="panel-heading">Permit Information</div>
    <div class="panel-body">
        <lew-screening-questions-view params="form: $parent.form"></lew-screening-questions-view>
    </div>
</div>
<div class="panel panel-info" data-bind="with: lowErosivityWaiver">
    <div class="panel-heading">Low Erosivity Waiver Information</div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="control-label" for="project-start">Estimated Project Start Date:</label>
                    <span class="form-group-static" id="project-start" data-bind="text: lewProjectStart"></span>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="control-label" for="project-end">Estimated Project End Date:</label>
                    <span class="form-group-static" id="project-end" data-bind="text: lewProjectEnd"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="area-disturbed">Estimated Area to be Disturbed (in Acres):</label>
                    <span class="form-group-static" id="area-disturbed" data-bind="text: lewAreaDisturbed"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="control-label" for="site-rfactor">Construction site's R-Factor:</label>
                    <span class="form-group-static" id="site-rfactor" data-bind="text: lewRFactor"></span>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="form-group">
                    <label class="control-label" for="rfactor-method">Rainfall Erosivity factor was calculated
                        using:</label>
                    <span class="form-group-static" id="rfactor-method"
                          data-bind="text: lewRFactorCalculationMethodDisplay"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="site-stabilization-question">Are interim non vegetative site
                        stabilization measures used to establish the
                        project completion date for purposes of obtaining this waiver?</label>
                    <span class="form-group-static" id="site-stabilization-question"
                          data-bind="yesNoBlank: interimSiteStabilizationMeasures()"></span>
                </div>
            </div>
        </div>
    </div>
</div>
<div class="panel panel-info" data-bind="with: operatorInformation">
    <div class="panel-heading">Operator Information</div>
    <div class="panel-body">
        <div class="h4">Operator Name</div>
        <div class="row">
        	<div class="col-sm-12">
        		<div class="form-group">
        			<label class="control-label" for="operator-name">Operator Name:</label>
        			<span class="form-group-static" id="operator-name" data-bind="text: operatorName"></span>
        		</div>
        	</div>
        </div>
        <hr>
        <div class="h4">Operator Mailing Address</div>
        <address-view params="address: {
                    address1: operatorAddress,
                    city: operatorCity,
                    state: operatorStateCode,
					zip: operatorZipCode,
					county: operatorCounty
                }"></address-view>
        <hr>
        <div class="h4">Operator Point of Contact</div>
        <contact-view params="contact: {
                    firstName: operatorPointOfContact.firstName,
                    middleInitial: operatorPointOfContact.middleInitial,
                    lastName: operatorPointOfContact.lastName,
                    title: operatorPointOfContact.title,
                    phone: operatorPointOfContact.phone,
                    phoneExtension: operatorPointOfContact.phoneExtension,
                    email: operatorPointOfContact.email,
                }"></contact-view>
    </div>
</div>
<div class="panel panel-info" data-bind="with: projectSiteInformation">
    <div class="panel-heading">Project/Site Information</div>
    <div class="panel-body">
        <div class="row">
        	<div class="col-sm-12">
        		<div class="form-group">
        			<label class="control-label" for="site-name">Project/Site Name:</label>
        			<span class="form-group-static" id="site-name" data-bind="text: siteName"></span>
        		</div>
        	</div>
        </div>
        <hr>
        <div class="h4">Project/Site Address</div>
        <address-view params="address: {
                    address1: siteAddress,
                    city: siteCity,
                    state: siteStateCode,
                    zip: siteZipCode,
                    county: siteCounty
                }"></address-view>
        <hr>
        <div class="h4">Latitude and Longitude</div>
        <location-view params="location: siteLocation"></location-view>
    </div>
</div>
<div class="panel panel-info">
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
<div class="row" style="margin-bottom: 10px">
    <div class="col-sm-12">
        <button class="btn btn-primary" data-bind="click: $parent.returnToHome">Return to Home</button>
    </div>
</div>
<!-- /ko -->
<!-- /ko -->