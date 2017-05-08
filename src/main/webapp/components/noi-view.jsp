<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="stripes"
           uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<stripes:layout-render name="view-templates.jsp"></stripes:layout-render>
<!-- ko if: form -->
<!-- ko with: form().formData -->
<div class="cor">
    <div class="row cor-header">
        <div class="col-xs-2">
            NPDES<br>
            FORM<br>
            3510-9
        </div>
        <div class="col-xs-3">
            <img class="cor-epa-logo" src="${pageContext.request.contextPath}/static/img/epa-logo-black.png"/>
        </div>
        <div class="col-xs-5">
            UNITED STATES ENVIRONMENTAL PROTECTION AGENCY<br>
            WASHINGTON, DC 20460<br>
            NOTICE OF INTENT (NOI) FOR THE 2017 NPDES CONSTRUCTION PERMIT
        </div>
        <div class="col-xs-2">
            FORM<br>
            Approved OMB No.<br>
            2040-0004
        </div>
    </div>
    <p>Submission of this Notice of Intent (NOI) constitutes notice that the operator identified in Section III of this
        form requests authorization to discharge pursuant to the NPDES Construction General Permit (CGP) permit number
        identified in Section II of this form. Submission of this NOI also constitutes notice that the operator
        identified in Section III of this form meets the eligibility requirements of Part 1.1 CGP for the project
        identified in Section IV of this form. Permit coverage is required prior to commencement of construction
        activity until you are eligible to terminate coverage as detailed in Part 8 of the CGP. To obtain authorization,
        you must submit a complete and accurate NOI form. Discharges are not authorized if your NOI is incomplete or
        inaccurate or if you were never eligible for permit coverage. Refer to the instructions at the end of this
        form.</p>
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
                                    field: $parent.form().formSet.npdesId() || oeca.cgp.noi.npdesId
                                }
                            }" id="npdes-id"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-4">
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
            <!-- ko if: projectSiteInformation.siteIndianCountry() == true -->
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
            <!-- /ko -->
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
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="stormwater-discharges-question">Have stormwater discharges
                            from your current construction site been covered previously under an NPDES permit?</label>
                        <span id="stormwater-discharges-question" data-bind="template: {
                                            name: 'yes-no-boxes',
                                            data: {
                                                field: projectSiteInformation.sitePreviousNpdesPermit
                                            }
                                        }"></span>
                    </div>
                </div>
            </div>
            <!-- ko if: projectSiteInformation.sitePreviousNpdesPermit() == true -->
            <div class="row subquestion">
                <div class="col-xs-5">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="prev-npdes-id">Your most current NPDES ID: </label>
                        <span id="prev-npdes-id" data-bind="template: {
                                                            name: 'underlined-field',
                                                            data: {
                                                                field: projectSiteInformation.sitePreviousNpdesPermitId
                                                            }
                                                        }"></span>
                    </div>
                </div>
            </div>
            <!-- /ko -->
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="use-polymers">Will you use polymers, flocculants, or other
                            treatment chemicals at your construction site?</label>
                        <span id="use-polymers" data-bind="template: {
                                                        name: 'yes-no-boxes',
                                                        data: {
                                                            field: chemicalTreatmentInformation.polymersFlocculantsOtherTreatmentChemicals
                                                        }
                                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- ko if: chemicalTreatmentInformation.polymersFlocculantsOtherTreatmentChemicals() == true -->
            <div class="row subquestion">
                <div class="col-xs-12">
                     <div class="form-group">
                         <label class="control-label" for="cationic-chemicals-use-question">Will you use cationic
                             treatment chemicals at your construction site?</label>
                         <span id="cationic-chemicals-use-question" data-bind="template: {
                                                    name: 'yes-no-boxes',
                                                    data: {
                                                        field: chemicalTreatmentInformation.cationicTreatmentChemicals
                                                    }
                                                }"></span>
                     </div>
                </div>
            </div>
            <!-- /ko -->
            <!-- ko if: chemicalTreatmentInformation.cationicTreatmentChemicals() == true -->
            <div class="row subquestion 2x">
                <div class="col-xs-12">
                     <div class="form-group">
                         <label class="control-label" for="cationic-chemicals-auth-question">Have you been authorized
                             to use cationic treatment chemicals by your applicable EPA Regional Office in advance of
                             filling your NOI?</label>
                         <span id="cationic-chemicals-auth-question" data-bind="template: {
                                    name: 'yes-no-boxes',
                                    data: {
                                        field: chemicalTreatmentInformation.cationicTreatmentChemicalsAuthorization
                                    }
                                }"></span>
                     </div>
                </div>
            </div>
            <!-- /ko -->
            <div class="row">
                <div class="col-xs-12">
                     <div class="form-group">
                         <label class="control-label" for="swppp-prepared">Has a Stormwater Pollution Prevention Plan (SWPPP) been prepared in advance of filling this NOI, as required?</label>
                         <span id="swppp-prepared" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: stormwaterPollutionPreventionPlanInformation.preparationInAdvance
                                        }
                                    }"></span>
                     </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                     <div class="form-group">
                         <label class="control-label" for="endangered-act-question">Are you able to demonstrate that you
                             meet one of the criteria listed in
                             <a href="${actionBean.cgpUrls.appendixD }" target="_blank">Appendix D</a> with respect to
                             protection of threatened or endangered species listed under the Endangered Species Act
                             (ESA) and federally designated critical habitat?</label>
                         <span id="endangered-act-question" data-bind="template: {
                                    name: 'yes-no-boxes',
                                    data: {
                                        field: endangeredSpeciesProtectionInformation.appendixDCriteriaMet
                                    }
                                }"></span>
                     </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                     <div class="form-group">
                         <label class="control-label" for="historic-screening">Have you completed the screening process
                             in <a href="${actionBean.cgpUrls.appendixE }" target="_blank">Appendix E</a> relating to
                             the protection of historic properties?</label>
                         <span id="historic-screening" data-bind="template: {
                                    name: 'yes-no-boxes',
                                    data: {
                                        field: historicPreservation.screeningCompleted
                                    }
                                }"></span>
                     </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                     <div class="form-group">
                         <label class="control-label" for="confirmation-question">Indicating "Yes" below, I confirm
                             that I understand that CGP only authorized the allowable stormwater discharges in Part
                             1.2.1 and the allowable non-stormwater discharges listed in Part 1.2.2. Any discharges not
                             expressly authorized in this permit cannot become authorized or shielded from liability
                             under CWA section 402(k) by disclosure to EPA, state or local authorities after issuance of
                             this permit via any means, Including the Notice of Intent (NOI) to be covered by the
                             permit, the Stormwater Pollution Prevention Plan (SWPPP), during an Inspection, etc. If
                             any discharges requiring NPDES permit coverage other than the allowable stormwater and
                             non-stormwater discharges listed in Parts 1.2.1 and 1.2.2 will be discharged, they must be
                             covered under another NPDES permit.</label>
                         <span id="confirmation-question" data-bind="template: {
                                    name: 'yes-no-boxes',
                                    data: {
                                        field: projectSiteInformation.siteCgpAuthorizationConfirmation
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
            <h5>Operator Information</h5>
            <div class="row">
                <div class="col-xs-7">
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
            <h5>Project/Site Address</h5>
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
            <div class="row">
                <div class="col-xs-4">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="project-start">Project Start Date:</label>
                        <span id="project-start" data-bind="template: {
                                    name: 'underlined-field',
                                    data: {
                                        field: siteProjectStart
                                    }
                                }"></span>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="project-end">Project End Date:</label>
                        <span id="project-end" data-bind="template: {
                                    name: 'underlined-field',
                                    data: {
                                        field: siteProjectEnd
                                    }
                                }"></span>
                    </div>
                </div>
                <div class="col-xs-4">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="area-disturbed">Estimated Area to be Disturbed:</label>
                        <span id="area-disturbed" data-bind="template: {
                                    name: 'underlined-field',
                                    data: {
                                        field: siteAreaDisturbed
                                    }
                                }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <label class="control-label" for="construction-site-types">Types of Construction Sites:</label>
                    <ul id="construction-site-types" data-bind="foreach: siteConstructionTypes">
                        <li class="form-group-static" data-bind="text:
                                    oeca.cgp.constants.constructionTypes[$data.toUpperCase()] ?
                                        oeca.cgp.constants.constructionTypes[$data.toUpperCase()] :
                                        $data"></li>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="structure-demolished">Will there be demolition of any
                            structure built or renovated before January 1, 1980?</label>
                        <span id="structure-demolished" data-bind="template: {
                                     name: 'yes-no-boxes',
                                     data: {
                                        field: siteStructureDemolitionBefore1980
                                     }
                                }"></span>
                    </div>
                </div>
            </div>
            <div class="row subquestion" data-bind="slideVisible: siteStructureDemolitionBefore1980()">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="structure-demolished-10k">Do any of the structures being
                            demolished have at least 10,000 square feet of floor space?</label>
                        <span id="structure-demolished-10k" data-bind="template: {
                                    name: 'yes-no-boxes',
                                    data: {
                                        field: siteStructureDemolitionBefore198010kSquareFeet
                                    }
                                }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="pre-dev-land">Was the pre-development land use used for
                            agriculture?</label>
                        <span id="pre-dev-land" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: sitePreDevelopmentAgricultural
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="earth-disturbing">Have earth-disturbing activities commenced on your
                            project/site?</label>
                        <span id="earth-disturbing" data-bind="template: {
                                    name: 'yes-no-boxes',
                                    data: {
                                        field: siteEarthDisturbingActivitiesOccurrence
                                    }
                                }"></span>
                    </div>
                </div>
            </div>
            <div class="row subquestion" data-bind="slideVisible: siteEarthDisturbingActivitiesOccurrence()">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="project-emergency-related">Is your project an
                            <dfn data-bind="popover: oeca.cgp.definitions.emergencyRelatedProject">
                                "emergency-related project"</dfn>?</label>
                        <span id="project-emergency-related" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: siteEmergencyRelated
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="indian-significance">Is your project located on a property of
                            religious or cultural significance to an Indian tribe?</label>
                        <span id="indian-significance" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: siteIndianCulturalProperty
                                        }
                                     }"></span>
                    </div>
                </div>
            </div>
            <div class="row subquestion" data-bind="slideVisible: siteIndianCulturalProperty">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="indian-significance-tribe">Indicate which tribe this land is
                            of religious or cultural significance to.</label>
                        <span id="indian-significance-tribe" data-bind="template: {
                                        name: 'underlined-field',
                                        data: {
                                            field: siteIndianCulturalPropertyTribe
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="panel panel-default" data-bind="with: dischargeInformation">
        <div class="panel-heading">Discharge Information</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="discharge-ms4">Does your project/site discharge stormwater
                            into a Municipal Separate Storm Sewer System (MS4)?</label>
                        <span id="discharge-ms4" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: dischargeMunicipalSeparateStormSewerSystem
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="water-earth-disturbance">Are there any waters of the U.S.
                            within 50 feet of your project's earth disturbances?</label>
                        <span id="water-earth-disturbance" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: dischargeUSWatersWithin50Feet
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="tier-question">Are any of the waters of the U.S. to which you
                            discharge designated by the state or tribal authority under its antidegradation policy as a
                            Tier 2 (or Tier 2.5) water (water quality exceeds levels necessary to support propagation
                            of fish, shellfish, and wildlife and recreation in and on the water) or as a Tier 3 water
                            (Outstanding National Resource Water)?
                            <a href="${actionBean.cgpUrls.appendixF}" target="_blank">See Appendix F</a></label>
                        <span id="tier-question" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: dischargeAllowable
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- ko foreach: dischargePoints-->
            <!-- ko if: $index() != 0 -->
                <hr>
            <!-- /ko -->
            <div class="row">
                <div class="col-xs-12">
                    <div class="h5">
                        <span data-bind="text: id"></span>:
                        <span data-bind="text: firstWater().receivingWaterName"></span>
                        <small data-bind="text: description"></small>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" data-bind="attr: {'for': 'tier-designation' + $index()}">Tier
                            Designation: </label>
                        <span data-bind="template: {
                                    name: 'underlined-field',
                                    data: {
                                        field: oeca.cgp.constants.tier[tier()]
                                    }
                                }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="water-impaired-question">Is this receiving water impaired (on
                            the CWA 303(d) list)?</label>
                        <span id="water-impaired-question" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field:  impaired
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="water-tmdl-question">Has a TMDL been completed for this
                            receiving waterbody?</label>
                        <span id="water-tmdl-question" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: tmdlCompleted
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- ko if: firstWater().pollutants().length > 0 -->
            <div class="list-group">
                <div class="row list-group-item list-group-item-header">
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
            <!-- /ko -->
        </div>
    </div>
    <!-- ko if: chemicalTreatmentInformation.cationicTreatmentChemicals -->
    <div class="panel panel-default" data-bind="with: chemicalTreatmentInformation">
        <div class="panel-heading">Chemical Treatment</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="treatment-chemicals-used">Treatment chemicals you will
                            use:</label>
                        <ul id="treatment-chemicals-used" data-bind="foreach: treatmentChemicals">
                            <li data-bind="text: $data"></li>
                        </ul>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="chemical-attachments">Attachments</label>
                        <table id="chemical-attachments"  class="table table-striped table-bordered table-condensed
                                    dataTable responsive no-wrap">
                            <thead>
                                <tr>
                                    <td>Name</td>
                                    <td>Created Date</td>
                                    <td>Size</td>
                                </tr>
                            </thead>
                            <tbody data-bind="foreach: $parents[1].form().attachmentsByCategory(oeca.cgp.constants.attachmentCategories.CHEMICAL_INFO)">
                            <tr>
                                <td>
                                    <a class="hidden-print" data-bind="attr: {href: '${pageContext.request.contextPath}/action/secured/attachment/' + id()}">
                                        <span class="fa fa-download"></span> <span data-bind="text: name"></span>
                                    </a>
                                    <span class="visible-print" data-bind="text: name"></span>
                                </td>
                                <td data-bind="text: oeca.cgp.utils.formatDateTime(createdDate)"></td>
                                <td data-bind="text: sizeDisplay"></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- /ko -->
    <div class="panel panel-default" data-bind="with: stormwaterPollutionPreventionPlanInformation">
        <div class="panel-heading">Stormwater Pollution Prevention Plan (SWPPP)</div>
        <div class="panel-body">
            <span data-bind="template: {
                    name: 'cor-contact',
                    data: contactInformation
                }"></span>
        </div>
    </div>
    <div class="panel panel-default" data-bind="with: endangeredSpeciesProtectionInformation">
        <div class="panel-heading">Endangered Species Protection</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="appendix-d-criteria">Using the Instructions in
                            <a href="${actionBean.cgpUrls.appendixD }" class="hidden-print" target="_blank">
                                Appendix D
                            </a>
                            <span class="visible-print-inline">Appendix D</span>
                            of the CGP, under which criterion listed in
                            <a href="${actionBean.cgpUrls.appendixD }" class="hidden-print" target="_blank">
                                Appendix D
                            </a>
                            <span class="visible-print-inline">Appendix D</span>
                            are you eligible for coverage under this permit?</label>
                        <span id="appendix-d-criteria" style="min-width: 150px;" data-bind="template: {
                                        name: 'underlined-field',
                                        data: {
                                            field: oeca.cgp.constants.appendixDCriteria[criterion()]
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="criteria-basis-summary">Provide a brief summary of the basis
                            for criterion selection listed above (the necessary content for a supportive basis statement
                            is provided under the criterion you selected.):</label>
                        <span id="criteria-basis-summary" data-bind="template: {
                                        name: 'text-block-field',
                                        data: {
                                            field: criteriaSelectionSummary
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- ko if: criterion() == 'Criterion_B' -->
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="operator-npdes-id">Provide the NPDES ID from the other
                            operator's notification of authorization under this permit:</label>
                        <span id="operator-npdes-id" data-bind="template: {
                                        name: 'underlined-field',
                                        data: {
                                            field: otherOperatorNpdesId
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- /ko -->
            <!-- ko if: criterion() == 'Criterion_C' -->
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="critical-habitat-species">What federally-listed species or
                            federally-designated critical habitat are located in your
                            <dfn data-bind="popover: oeca.cgp.definitions.actionArea">"action area"</dfn>?</label>
                        <span id="critical-habitat-species" data-bind="template: {
                                        name: 'text-block-field',
                                        data: {
                                            field: speciesAndHabitatInActionArea
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label" for="critical-habitat-distance">What is the distance between your
                            site and the listed species or critical habitat (miles)?</label>
                        <span id="critical-habitat-distance" data-bind="template: {
                                        name: 'text-block-field',
                                        data: {
                                            field: distanceFromSite
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- /ko -->
            <!-- ko if: criterion() == 'Criterion_C'
                            || criterion() == 'Criterion_D'
                            || criterion() == 'Criterion_E'
                            || criterion() == 'Criterion_F' -->
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <!-- ko if: criterion() == 'Criterion_C' -->
                        <label class="control-label" for="site-map">Copy of your Site Map:</label>
                        <!-- /ko -->
                        <!-- ko if: criterion() == 'Criterion_D'
                                 || criterion() == 'Criterion_E'
                                 || criterion() == 'Criterion_F' -->
                        <label class="control-label" for="site-map">Copies of any letters or other communications
                            between you and the U.S. Fish and Wildlife Service or National Marine Fisheries
                            Service.</label>
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
                                    <a class="hidden-print" data-bind="attr: {href: '${pageContext.request.contextPath}/action/secured/attachment/' + id()}">
                                        <span class="fa fa-download"></span> <span data-bind="text: name"></span>
                                    </a>
                                    <span class="visible-print" data-bind="text: name"></span>
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
    <div class="panel panel-default" data-bind="with: historicPreservation">
        <div class="panel-heading">Historic Preservation</div>
        <div class="panel-body">
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group">
                        <label class="control-label" for="subsurface-earth-disturbance">Are you installing any
                            stormwater controls as described in
                            <a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a> that require
                            subsurface earth disturbances?
                            (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 1)</label>
                        <span id="subsurface-earth-disturbance" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: appendexEStep1
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- ko if: appendexEStep1() == true -->
            <div class="row subquestion">
                <div class="col-sm-12">
                    <div class="form-group">
                        <label class="control-label" for="prior-surveys-evals">Have prior surveys or evaluations
                            conducted on the site already determined historic properties do not exist, or that prior
                            disturbances have precluded the existence of historic properties?
                            (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 2):</label>
                        <span id="prior-surveys-evals" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: appendexEStep2
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- /ko -->
            <!-- ko if: appendexEStep2() == false -->
            <div class="row subquestion">
                <div class="col-sm-12">
                    <div class="form-group">
                        <label class="control-label" for="determined-disturbances-no-effect-historic">Have you
                            determined that your installation of subsurface earth-disturbing stormwater controls will
                            have no effect on historic properties?
                            (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix E</a>, Step 3)</label>
                        <span id="determined-disturbances-no-effect-historic" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: appendexEStep3
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- /ko -->
            <!-- ko if: appendexEStep3() == false -->
            <div class="row subquestion">
                <div class="col-sm-12">
                    <div class="form-group">
                        <label class="control-label" for="sho-rep-respond">Did the SHPO, THPO, or other tribal
                            representative (whichever applies) respond to you within the 15 calendar days to indicate
                            whether the subsurface earth disturbances caused by the installation of stormwater controls
                            affect historic properties? (<a href="${actionBean.cgpUrls.appendixE}" target="_blank">Appendix
                                E</a>, Step 4)</label>
                        <span id="sho-rep-respond" data-bind="template: {
                                        name: 'yes-no-boxes',
                                        data: {
                                            field: appendexEStep4
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- /ko -->
            <!-- ko if: appendexEStep4() == true-->
            <div class="row subquestion">
                <div class="col-sm-12">
                    <div class="form-group cor-underline-group">
                        <label class="control-label" for="sho-rep-response-nature">Describe the nature of their
                            response:</label>
                        <span id="sho-rep-response-nature" data-bind="template: {
                                        name: 'underlined-field',
                                        data: {
                                            field: oeca.cgp.constants.appendexEStep4Responses[appendexEStep4Response().toUpperCase()] ?
                                                    oeca.cgp.constants.appendexEStep4Responses[appendexEStep4Response().toUpperCase()] :
                                                    appendexEStep4Response
                                        }
                                    }"></span>
                    </div>
                </div>
            </div>
            <!-- /ko -->
        </div>
    </div>
    <div data-bind="template: 'cor-certification'"></div>
</div>
<!-- /ko -->
<!-- /ko -->