<!-- ko if: form -->
<!-- ko with: form().formData -->
<div class="panel panel-info">
    <div class="panel-heading">Permit Information</div>
    <div class="panel-body">
        <noi-screening-questions-view params="form: $parent.form"></noi-screening-questions-view>
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
                }">
        </contact-view>
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
        <div class="row">
            <div class="col-sm-4">
                <div class="form-group">
                    <label class="control-label" for="start-date">Project Start Date:</label>
                    <span class="form-group-static" id="start-date" data-bind="text: siteProjectStart"></span>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="form-group">
                    <label class="control-label" for="project-end-date">Estimated Project End Date:</label>
                    <span class="form-group-static" id="project-end-date" data-bind="text: siteProjectEnd"></span>
                </div>
            </div>
            <div class="col-sm-4">
                <div class="form-group">
                    <label class="control-label" for="area-disturbed">Estimated Area to be Disturbed:</label>
                    <span class="form-group-static" id="area-disturbed" data-bind="text: siteAreaDisturbed"></span>
                </div>
            </div>
        </div>
        <hr>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="construction-site-types">Types of Construction Site:</label>
                    <ul data-bind="foreach: siteConstructionTypes" id="construction-site-types">
                        <li class="form-group-static" style="word-wrap: break-word"
                            data-bind="text: oeca.cgp.constants.constructionTypes[$data.toUpperCase()] ? oeca.cgp.constants.constructionTypes[$data.toUpperCase()] : $data"></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="structure-demolition">Will there be demolition of any structure
                        built or renovated before January 1,
                        1980?</label>
                    <span class="form-group-static" id="structure-demolition"
                          data-bind="text: siteStructureDemolitionBefore1980()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <div class="row subquestion" data-bind="slideVisible: siteStructureDemolitionBefore1980()">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="structure-demolished-10k">Do any of the structures being
                        demolished have at least 10,000 square
                        feet of floor space?</label>
                    <span class="form-group-static" id="structure-demolished-10k"
                          data-bind="text: siteStructureDemolitionBefore198010kSquareFeet()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="predev-agriculture">Was the pre-development land use used for
                        agriculture?</label>
                    <span class="form-group-static" id="predev-agriculture"
                          data-bind="text: sitePreDevelopmentAgricultural()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="earth-disturbing">Have earth-disturbing activities commenced on
                        your project/site?</label>
                    <span class="form-group-static" id="earth-disturbing"
                          data-bind="text: siteEarthDisturbingActivitiesOccurrence()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <div class="row subquestion" data-bind="slideVisible: siteEarthDisturbingActivitiesOccurrence()">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="project-emergency-related">Is your project an
                        <dfn data-bind="popover: oeca.cgp.definitions.emergencyRelatedProject">
                            "emergency-related project"</dfn>?</label>
                    <span class="form-group-static" id="project-emergency-related"
                          data-bind="text: siteEmergencyRelated()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12 form-group">
                <label class="control-label">Is your project located on a property of religious or cultural significance to an Indian tribe?</label>
                <span class="form-group-static" data-bind="text: siteIndianCulturalProperty()?'Yes':'No'"></span>
            </div>
        </div>
        <div class="row subquestion" data-bind="slideVisible: siteIndianCulturalProperty">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-md-12 form-group">
                <label class="control-label">Indicate which tribe this land is of religious or cultural significance to.</label>
                <span class="form-group-static" data-bind="text: siteIndianCulturalPropertyTribe"></span>
            </div>
        </div>
    </div>
</div>
<div class="panel panel-info" data-bind="with: dischargeInformation">
    <div class="panel-heading">Discharge Information</div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="discharge-ms4">Does your project/site discharge stormwater into a
                        Municipal Separate Storm
                        Sewer System (MS4)?</label>
                    <span class="form-group-static" id="discharge-ms4"
                          data-bind="text: dischargeMunicipalSeparateStormSewerSystem()?'Yes':'No'"></span>
                </div>

            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="water-earth-disturbance">Are there any waters of the U.S. within
                        50 feet of your project's earth
                        disturbances?</label>
                    <span class="form-group-static" id="water-earth-disturbance"
                          data-bind="text: dischargeUSWatersWithin50Feet()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="tier-question">Are any of the waters of the U.S. to which you
                        discharge designated by the state or tribal authority under its
                        antidegradation policy as a Tier 2 (or Tier 2.5) water (water quality exceeds levels necessary
                        to support
                        propagation of fish, shellfish, and wildlife and recreation in and on the water) or as a Tier 3
                        water (Outstanding
                        National Resource Water)? <a href="${actionBean.cgpUrls.appendixF}" target="_blank">See Appendix
                            F</a></label>
                    <span class="form-group-static" id="tier-question"
                          data-bind="text: dischargeAllowable()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <hr>
        <div class="h3">Discharges</div>
        <div class="list-group" data-bind="foreach: dischargePoints">
            <div class="list-group-item">
                <div class="h4">
                    <span data-bind="text: id"></span>:
                    <span data-bind="text: firstWater().receivingWaterName"></span>
                    <small data-bind="text: description"></small>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label" for="discharge-tier">Tier Designation:</label>
                            <span class="form-group-static" id="discharge-tier" data-bind="text: oeca.cgp.constants.tier[tier()]"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                        <div class="form-group">
                            <label class="control-label" for="water-impaired-question">Is this receiving water impaired (on the CWA 303(d) list)?</label>
                            <span calss="form-group-static" id="water-impaired-question" data-bind="yesNoBlank: impaired"></span>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-sm-12">
                    	<div class="form-group">
                    		<label class="control-label" for="water-tmdl-question">Has a TMDL been completed for this receiving waterbody?</label>
                    		<span class="form-group-static" id="water-tmdl-question" data-bind="yesNoBlank: tmdlCompleted"></span>
                    	</div>
                    </div>
                </div>
                <!-- ko if: firstWater().pollutants().length > 0 -->
                <div class="list-group" data-bind="">
                    <div class="row list-group-item list-group-item-info">
                        <div class="col-sm-4">
                            <label>Pollutant</label>
                        </div>
                        <div class="col-sm-2">
                            <label>Causing Impairment?</label>
                        </div>
                        <div class="col-sm-2">
                            <label>TMDL ID</label>
                        </div>
                        <div class="col-sm-4">
                            <label>TMDL Name</label>
                        </div>
                    </div>
                    <!-- ko  foreach: firstWater().pollutants -->
                    <div class="row list-group-item">
                        <div class="col-sm-4" data-bind="text: pollutantName"></div>
                        <div class="col-sm-2" data-bind="text: impaired() ? 'Yes' : 'No'"></div>
                        <div class="col-sm-2" data-bind="text: tmdl().id"></div>
                        <div class="col-sm-4" data-bind="text: tmdl().name"></div>
                    </div>
                    <!-- /ko -->
                </div>
                <!-- /ko -->
            </div>
        </div>
    </div>
</div>
<!-- ko if: chemicalTreatmentInformation.cationicTreatmentChemicals -->
<div class="panel panel-info" data-bind="with: chemicalTreatmentInformation">
    <div class="panel-heading">Chemical Treatment Information</div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="treatment-chemicals-used">Treatment chemicals you will use</label>
                    <ul id="treatment-chemicals-used" data-bind="foreach: treatmentChemicals">
                        <li data-bind="text: $data"></li>
                    </ul>
                </div>
            </div>
        </div>
        <table class="table table-striped table-bordered table-condensed dataTable responsive no-wrap">
            <thead>
            <tr>
                <th>Name</th>
                <th>Created Date</th>
                <th>Size</th>
            </tr>
            </thead>
            <tbody data-bind="foreach: $parents[1].form().attachmentsByCategory(oeca.cgp.constants.attachmentCategories.CHEMICAL_INFO)">
            <tr>
                <td>
                    <a data-bind="attr: {href: '${pageContext.request.contextPath}/action/secured/attachment/' + id()}">
                        <span class="fa fa-download"></span> <span data-bind="text: name"></span>
                    </a>
                </td>
                <td data-bind="text: oeca.cgp.utils.formatDateTime(createdDate)"></td>
                <td data-bind="text: sizeDisplay"></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>
<!-- /ko -->
<div class="panel panel-info" data-bind="with: stormwaterPollutionPreventionPlanInformation">
    <div class="panel-heading">Stormwater Pollution Prevention Plan</div>
    <div class="panel-body">
        <contact-view params="contact: contactInformation"></contact-view>
    </div>
</div>
<div class="panel panel-info" data-bind="with: endangeredSpeciesProtectionInformation">
    <div class="panel-heading">Endangered Species Protection</div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="criterion-eligible-coverage">Using the Instructions in <a
                            href="${actionBean.cgpUrls.appendixD }" target="_blank">Appendix D</a> of the CGP,
                        under which criterion listed in <a href="${actionBean.cgpUrls.appendixD }" target="_blank">Appendix
                            D</a> are you
                        eligible for coverage under this permit?</label>
                    <span class="form-group-static" id="criterion-eligible-coverage"
                          data-bind="text: oeca.cgp.constants.appendixDCriteria[criterion()]"></span>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="criterion-summary">
                        Provide a brief summary of the basis for criterion selection listed above [the necessary
                        content for a supportive basis statement is provided under the criterion you selected.]:</label>
                    <blockquote>
                        <span class="form-group-static" id="criterion-summary" style="word-wrap: break-word"
                              data-bind="text: criteriaSelectionSummary"></span>
                    </blockquote>
                </div>

            </div>
        </div>
        <!-- ko if: criterion() == 'Criterion_B' -->
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="operator-npdes-id">Provide the NPDES ID from the other operator's
                        notification of authorization under this permit:</label>
                    <span class="form-group-static" id="operator-npdes-id" style="word-wrap: break-word"
                          data-bind="text: otherOperatorNpdesId"></span>
                </div>
            </div>
        </div>
        <!-- /ko -->
        <!-- ko if: criterion() == 'Criterion_C' -->
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="critical-habitat-species">What federally-listed species or
                        federally-designated
                        critical habitat are located in your
                        <dfn data-bind="popover: oeca.cgp.definitions.actionArea">"action area"</dfn>?</label>
                    <blockquote>
                        <span class="form-group-static" id="critical-habitat-species" style="word-wrap: break-word"
                              data-bind="text: speciesAndHabitatInActionArea"></span>
                    </blockquote>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="critical-habitat-distance">What is the distance between your site
                        and the listed
                        species or critical habitat (miles)?</label>
                    <blockquote>
                        <span class="form-group-static" id="critical-habitat-distance" style="word-wrap: break-word"
                              data-bind="text: distanceFromSite"></span>
                    </blockquote>
                </div>
            </div>
        </div>
        <!-- /ko -->
        <!-- ko if: criterion() == 'Criterion_C'
                    || criterion() == 'Criterion_D'
                    || criterion() == 'Criterion_E'
                    || criterion() == 'Criterion_F' -->
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <!-- ko if: criterion() == 'Criterion_C' -->
                    <label class="control-label" for="site-map">Copy of your Site Map:</label>
                    <!-- /ko -->
                    <!-- ko if: criterion() == 'Criterion_D'
                             || criterion() == 'Criterion_E'
                             || criterion() == 'Criterion_F' -->
                    <label class="control-label" for="site-map">Copies of any letters or other communications between you
                        and the U.S. Fish and Wildlife Service or National Marine Fisheries Service.</label>
                    <!-- /ko -->
                    <table id="site-map"
                           class="table table-striped table-bordered table-condensed dataTable responsive no-wrap">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Created Date</th>
                            <th>Size</th>
                        </tr>
                        </thead>
                        <tbody data-bind="foreach: $parents[1].form().attachmentsByCategory(oeca.cgp.constants.attachmentCategories.ENDANGERED_SPECIES)">
                        <tr>
                            <td>
                                <a data-bind="attr: {href: '${pageContext.request.contextPath}/action/secured/attachment/' + id()}">
                                    <span class="fa fa-download"></span> <span data-bind="text: name"></span>
                                </a>
                            </td>
                            <td data-bind="text: oeca.cgp.utils.formatDateTime(createdDate)"></td>
                            <td data-bind="text: sizeDisplay"></td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        <!-- /ko -->
    </div>
</div>
<div class="panel panel-info" data-bind="with: historicPreservation">
    <div class="panel-heading">Historic Preservation</div>
    <div class="panel-body">
        <div class="row">
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="subsurface-earth-disturbance">Are you installing any stormwater
                        controls as described in <a href="${actionBean.cgpUrls.appendixE}"
                                                    target="_blank">Appendix E</a> that require subsurface earth
                        disturbances? (<a
                                href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 1)</label>
                    <span class="form-group-static" id="subsurface-earth-disturbance"
                          data-bind="text: appendexEStep1()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <!-- ko if: appendexEStep1() == true -->
        <div class="row subquestion">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="prior-surveys-evals">Have prior surveys or evaluations conducted
                        on the site already determined historic properties do not
                        exist, or that prior disturbances have precluded the existence of historic properties? (<a
                                href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 2)</label>
                    <span class="form-group-static" id="prior-surveys-evals"
                          data-bind="text: appendexEStep2()?'Yes':'No'"></span>
                </div>
            </div>
        </div>
        <!-- /ko -->
        <!-- ko if: appendexEStep2() == false -->
        <div class="row subquestion">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="determined-disturbances-no-effect-historic">Have you determined
                        that your installation of subsurface earth-disturbing stormwater controls will have no
                        effect on historic properties? (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix
                            E</a>, Step 3)</label>
                    <span class="form-group-static" id="determined-disturbances-no-effect-historic"
                          data-bind="text: appendexEStep3()?'Yes':'No'"></span>
                </div>

            </div>
        </div>
        <!-- /ko -->
        <!-- ko if: appendexEStep3() == false -->
        <div class="row subquestion">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="sho-rep-respond">Did the SHPO, THPO, or other tribal
                        representative (whichever applies) respond to you within the 15 calendar days to indicate
                        whether the subsurface earth disturbances caused by the installation of stormwater controls
                        affect historic properties? (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix
                            E</a>, Step 4)</label>
                    <span class="form-group-static" id="sho-rep-respond"
                          data-bind="text: appendexEStep4()?'Yes':'No'"></span>
                </div>

            </div>
        </div>
        <!-- /ko -->
        <!-- ko if: appendexEStep4() == true -->
        <div class="row subquestion">
            <span class="glyphicon glyphicon-share-alt"></span>
            <div class="col-sm-12">
                <div class="form-group">
                    <label class="control-label" for="sho-rep-response-nature">Describe the nature of their
                        response:</label>
                    <span class="form-group-static" id="sho-rep-response-nature" style="word-wrap: break-word"
                          data-bind="text: oeca.cgp.constants.appendexEStep4Responses[appendexEStep4Response().toUpperCase()] ?
                           oeca.cgp.constants.appendexEStep4Responses[appendexEStep4Response().toUpperCase()] : appendexEStep4Response"></span>
                    <!-- ko if: appendexEStep4Response() == 'other' -->
                    <blockquote>
                        TBD
                        <%--<span class="form-group-static" data-bind="text: appendexEStep4OtherResponse"></span>--%>
                    </blockquote>
                    <!-- /ko -->
                </div>
            </div>
        </div>
        <!-- /ko -->
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
                        <span class="form-group-static" id="certified-date" data-bind="text: oeca.cgp.utils.formatDateTime($parent.form().certifiedDate)"></span>
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