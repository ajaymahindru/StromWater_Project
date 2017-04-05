<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<stripes:layout-render name="/layout/cgp-layout.jsp" title="CGP Streamlined Registration" tab="Home">
    <stripes:layout-component name="container">

        <script type="text/javascript">

            $(function () {
                var enablePasswordAnswerMask = function () {
                    $("input.password-mask").hideShowPassword({
                        show: false,
                        innerToggle: false
                    }); ///hidden by default
                    $('#showPassword').on('change', function () {
                        if ($("#showPassword").is(':checked')) {
                            $('input.password-mask').hideShowPassword({
                                show: true
                            });
                        } else {
                            $('input.password-mask').hideShowPassword({
                                show: false
                            });
                        }
                    });
                    $("input.answer-mask").hideShowPassword({
                        show: false,
                        innerToggle: false
                    }); ///hidden by default

                    $('#showAnswer').on('change', function() {
                        if ($("#showAnswer").is(':checked')) {
                            $('input.answer-mask').hideShowPassword({
                                show: true
                            });
                        } else {
                            $('input.answer-mask').hideShowPassword({
                                show: false
                            });
                        }
                    });
                };

                var enableCromerrMask = function () {
                    $("input.cromerr-mask").hideShowPassword({
                        show: false,
                        innerToggle: false
                    }); ///hidden by default

                    $('#showCromerrAnswer').on('change', function() {
                        if ($("#showCromerrAnswer").is(':checked')) {
                            $('input.cromerr-mask').hideShowPassword({
                                show: true
                            });
                        } else {
                            $('input.cromerr-mask').hideShowPassword({
                                show: false
                            });
                        }
                    });
                };
                var formatOrgsTable = function () {
                    $('#orgs').DataTable({
                        paging: true,
                        ordering: true,
                        searching: false,
                        nowrap: true,
                        fixedHeader: {
                            header: true
                        },
                        order: [[ 1, "asc" ]],
                        columnDefs: [ {
                            targets: 0,
                            orderable: false
                        },
                            {
                                className: 'control',
                                orderable: false,
                                targets:   -1
                            }],
                        responsive: {
                            details: {
                                type: 'column',
                                target: -1
                            }
                        }
                    });
                };
                //enable modal display on right-click for a "pager" page
                var afterPageDisplay = function() {
                    $("[data-toggle='modal']").on("contextmenu", function (e) {
                        e.preventDefault();
                        var targetModal = $(this).data('target');
                        $(targetModal).modal("show");
                    });
                };


                var viewModel = function (data) {
                    var self = this;
                    self.titles = ko.observableArray();
                    self.suffixes = ko.observableArray();
                    self.allSecretQuestions = ko.observableArray();
                    self.selectedQuestion1 = ko.observableArray();
                    self.selectedQuestion2 = ko.observableArray();
                    self.selectedQuestion3 = ko.observableArray();
                    self.secretQuestions1 = ko.computed(function() {
                        return self.allSecretQuestions().filter(function (option) {
                            return (self.selectedQuestion2().concat(self.selectedQuestion3())).indexOf(option) === -1;
                        }.bind(this));
                    });
                    self.secretQuestions2 = ko.computed(function() {
                        return self.allSecretQuestions().filter(function(option) {
                            return (self.selectedQuestion1().concat(self.selectedQuestion3())).indexOf(option) === -1;
                        }.bind(this));
                    });
                    self.secretQuestions3 = ko.computed(function() {
                        return self.allSecretQuestions().filter(function(option) {
                            return (self.selectedQuestion1().concat(self.selectedQuestion2())).indexOf(option) === -1;
                        }.bind(this));
                    });

                    self.allCromerrQuestions = ko.observableArray();
                    self.selectedCromerr1 = ko.observableArray();
                    self.selectedCromerr2 = ko.observableArray();
                    self.selectedCromerr3 = ko.observableArray();
                    self.selectedCromerr4 = ko.observableArray();
                    self.selectedCromerr5 = ko.observableArray();
                    self.cromerrQuestions1 = ko.computed(function() {
                        return self.allCromerrQuestions().filter(function(option) {
                            return (self.selectedCromerr2().concat(self.selectedCromerr3()).concat(self.selectedCromerr4()
                                            .concat(self.selectedCromerr5())).concat(self.selected)).indexOf(option) === -1;
                        }.bind(this));
                    });
                    self.cromerrQuestions2 = ko.computed(function() {
                        return self.allCromerrQuestions().filter(function(option) {
                            return (self.selectedCromerr1().concat(self.selectedCromerr3()).concat(self.selectedCromerr4()
                                            .concat(self.selectedCromerr5())).concat(self.selected)).indexOf(option) === -1;
                        }.bind(this));
                    });
                    self.cromerrQuestions3 = ko.computed(function() {
                        return self.allCromerrQuestions().filter(function(option) {
                            return (self.selectedCromerr1().concat(self.selectedCromerr2()).concat(self.selectedCromerr4()
                                            .concat(self.selectedCromerr5())).concat(self.selected)).indexOf(option) === -1;
                        }.bind(this));
                    });
                    self.cromerrQuestions4 = ko.computed(function() {
                        return self.allCromerrQuestions().filter(function(option) {
                            return (self.selectedCromerr1().concat(self.selectedCromerr2()).concat(self.selectedCromerr3()
                                            .concat(self.selectedCromerr5())).concat(self.selected)).indexOf(option) === -1;
                        }.bind(this));
                    });
                    self.cromerrQuestions5 = ko.computed(function() {
                        return self.allCromerrQuestions().filter(function(option) {
                            return (self.selectedCromerr1().concat(self.selectedCromerr2()).concat(self.selectedCromerr3()
                                            .concat(self.selectedCromerr4())).concat(self.selected)).indexOf(option) === -1;
                        }.bind(this));
                    });

                    self.orgSearchResults = ko.observableArray();
                    self.esaHtml = ko.observable();

                    self.roleNext = function () {
                        var roleErrors = self.roleViewModel().errors;

                        if (roleErrors().length > 0) {
                            roleErrors.showAllMessages();
                            return;
                        }
                        $.getJSON(config.registration.ctx + '/api/registration/v1/reference/title',
                                function (data) {
                                    self.titles(data);
                                }
                        );

                        $.getJSON(config.registration.ctx + '/api/registration/v1/reference/suffix',
                                function (data) {
                                    self.suffixes(data);
                                    self.showPersonalInfo(true);
                                    self.showRoleNextButton(false);
                                    self.personalInfoViewModel(new persInfoViewModel(self.roleViewModel().role().signatureQuestionsRequired));
                                    expandPanel("personalInfoHead");
                                });

                        //start watching role in case user changes role
                        self.roleViewModel().role().subject = 'N/A';
                        if (self.roleViewModel().role().signatureQuestionsRequired) {
                            self.createEsignModule();
                        }
                        self.roleViewModel().role.subscribe(function(){
                            self.roleChanged(true);
                            self.roleViewModel().role().subject = 'N/A';
                            if (self.roleViewModel().role().signatureQuestionsRequired) {
                                self.createEsignModule();
                            } else {
                                self.showEsign(false);
                                self.esignViewModel = ko.observable();
                            }
                        });
                    };

                    self.createEsignModule = function() {
                        self.esignViewModel = ko.observable(new esignViewModel());
                        $.getJSON(config.registration.ctx + '/api/registration/v1/reference/cromerrquestion',
                                function (data) {
                                    self.allCromerrQuestions(data);
                                    self.showEsign(true);
                                    enableCromerrMask();
                                });
                    };

                    self.personalInfoNext = function () {
                        var personalInfoErrors = self.personalInfoViewModel().errors;

                        if (personalInfoErrors().length > 0) {
                            personalInfoErrors.showAllMessages();
                            return;
                        }

                        $.getJSON(config.registration.ctx + '/api/registration/v1/reference/secretquestion', function (data) {
                            self.allSecretQuestions(data);
                            self.showUserPass(true);
                            self.showPersInfoNextButton(false);
                            enablePasswordAnswerMask();
                            expandPanel("userPassHead");

                        });
                    };

                    self.userPassNext = function () {
                        var userPassErrors = self.userPassViewModel().errors;

                        if (userPassErrors().length > 0) {
                            userPassErrors.showAllMessages();
                            return;
                        }
                        if (self.roleViewModel().role().signatureQuestionsRequired) {
                            expandPanel("esignHead");
                        } else {
                            self.showOrgInfo(true);
                            self.showOrgSearchFields(true);
                            self.showOrgSearchResults(false);
                            self.showUserPassNextButton(false);
                            expandPanel("orgInfoHead");
                        }
                    };

                    self.esignNext = function () {
                        var esignErrors = self.esignViewModel().errors;

                        if (esignErrors().length > 0) {
                            esignErrors.showAllMessages();
                            return;
                        }

                        self.showOrgInfo(true);
                        self.showOrgSearchFields(true);
                        self.showOrgSearchResults(false);
                        self.showEsignNextButton(false);
                        expandPanel("orgInfoHead");
                    };

                    self.findOrg = function() {
                        var orgSearchErrors = self.orgSearchModel().errors;

                        if (orgSearchErrors().length > 0) {
                            orgSearchErrors.showAllMessages();
                            return;
                        }
                        self.showOrgSearchFields(false);
                        var stateCodeString = function(stateCode) {
                            if(stateCode === undefined || stateCode === null) {
                                return '';
                            } else {
                                return stateCode;
                            }
                        };
                        var criteriaString = 'organizationName='+self.orgSearchModel().organizationName()+'&address='+self.orgSearchModel().mailingAddress1()
                                +'&address2='+self.orgSearchModel().mailingAddress2()+'&city='+self.orgSearchModel().city()+'&state='+stateCodeString(self.orgSearchModel().stateCode())
                                +'&zip='+self.orgSearchModel().zip();
                        $.getJSON(config.registration.ctx + '/api/registration/v1/organization?'+criteriaString,
                                function (data) {
                                    self.orgSearchResults(ko.mapping.fromJS(data));
                                    self.showOrgSearchFields(false);
                                    self.showOrgSearchResults(true);
                                    formatOrgsTable();
                                }).error(function(res) {
                                    oeca.notifications.errorAlert(res.responseJSON.message);
                                    self.showOrgSearchFields(true);
                                });
                    };

                    self.backToSearch = function () {
                        self.showOrgSearchFields(true);
                        self.showOrgSearchResults(false);
                    };

                    self.addNewOrg = function () {
                        self.orgInfoViewModel(new newOrgViewModel());
                        self.newOrg(true);
                        self.showOrgContact(true);
                        expandPanel("orgContactHead");
                    };

                    self.selectOrg = function(selectedOrg) {
                        self.orgInfoViewModel(new cdxOrgViewModel());
                        ko.mapping.fromJS(selectedOrg, {}, self.orgInfoViewModel);
                        self.displaySelectedOrg(true);
                        self.showOrgSearchResults(false);
                        self.newOrg(false);
                    };

                    self.backToResults = function() {
                        self.displaySelectedOrg(false);
                        self.showOrgSearchResults(true);
                        formatOrgsTable();
                    };

                    self.orgInfoNext = function() {
                        self.showOrgInfoNextButton(false);
                        self.showOrgContact(true);
                        self.newOrg(false);
                        setTimeout(function () {
                            self.orgInfoViewModel().errors.showAllMessages(false);
                        }, 200);
                        expandPanel("orgContactHead");

                    };

                    self.sendVerificationCode = function () {
                        //check if all orgInfo fields are entered, except for confirmationCode
                        if (self.orgInfoViewModel().errors().length > 1) {
                            self.orgInfoViewModel().errors.showAllMessages();
                            return;
                        } else {
                            self.orgInfoViewModel().errors.showAllMessages(false);
                        }
                        if (!self.orgInfoViewModel().codeValidationAdded()) {
                            self.orgInfoViewModel().confirmationCode.extend({
                                validation: {
                                    async: true,
                                    validator: function (val, params, callback) {
                                        self.orgInfoViewModel().confCodeErrorMessage(null);
                                        var userString = ko.toJSON({user: self.userPassViewModel().userId(), email: self.orgInfoViewModel().email(), confirmationCode: val});
                                        $.ajax({
                                            url: config.ctx + '/api/registration/v1/confirmation_code_validation',
                                            data: userString,
                                            dataType: "json",
                                            contentType: oeca.xhrSettings.mimeTypes.JSON,
                                            type: 'POST',
                                            success: function(result, status, xhr) {
                                                self.orgInfoViewModel().codeIsValid(true);
                                                callback(true);
                                            },
                                            statusCode: {
                                                500: function(res) {
                                                    var response = res.responseJSON;
                                                    if(response) {
                                                        self.orgInfoViewModel().confCodeErrorMessage(response.message);
                                                        toastr.remove();
                                                    }
                                                    else {
                                                        self.orgInfoViewModel().confCodeErrorMessage("We have encountered an error");
                                                    }
                                                    self.orgInfoViewModel().codeIsValid(false);
                                                    callback(false);
                                                },
                                                204: function () {
                                                    self.orgInfoViewModel().codeIsValid(true);
                                                    callback(true);
                                                }
                                            }
                                        })
                                    },
                                    message: function () {
                                        if (self.orgInfoViewModel().confCodeErrorMessage() !== null || self.orgInfoViewModel().confCodeErrorMessage() !== undefined) {
                                            return ko.toJS(self.orgInfoViewModel().confCodeErrorMessage());
                                        }
                                    }
                                }
                            });
                        }
                        self.orgInfoViewModel().codeValidationAdded(true);

                        $.ajax({
                            url: config.ctx + '/api/registration/v1/email_available',
                            data: self.orgInfoViewModel().email(),
                            dataType: "json",
                            contentType: oeca.xhrSettings.mimeTypes.JSON,
                            type: 'POST',
                            success: function (data) {
                                if (data == true) {
                                    var confCodeString = ko.toJSON({user: self.userPassViewModel().userId(), email: self.orgInfoViewModel().email()});
                                    oeca.logger.debug(confCodeString);
                                    $.ajax({
                                        url: config.ctx + '/api/registration/v1/new_account_confirmation',
                                        data: confCodeString,
                                        dataType: "json",
                                        contentType: oeca.xhrSettings.mimeTypes.JSON,
                                        type: 'POST',
                                        success: function() {
                                            oeca.notifications.showSuccessMessage("A verification code has been sent to "+self.orgInfoViewModel().email() +". " +
                                                    "Enter the code below and select Continue to finalize your CDX account setup.");
                                        }
                                    });
                                }
                                else {
                                    oeca.notifications.showWarnMessage("Email address is already being used by another NeT or NetDMR role. Please choose a different email address.");
                                }
                            }
                        });
                    };

                    self.submitRegistration = function (data) {
                        self.personalInfoViewModel().userId(self.userPassViewModel().userId);
                        self.personalInfoViewModel().password(self.userPassViewModel().password);
                        self.roleViewModel().role().dataflow = self.roleViewModel().dataflowAcronym();

                        if (!self.roleViewModel().role().signatureQuestionsRequired) {
                            var registrationInfo = {
                                user: self.personalInfoViewModel,
                                organization: self.orgInfoViewModel,
                                role: self.roleViewModel().role,
                                secretAnswers: [
                                    self.userPassViewModel().secretAnswer1,
                                    self.userPassViewModel().secretAnswer2,
                                    self.userPassViewModel().secretAnswer3
                                ]
                            };
                        } else {
                            var registrationInfo = {
                                user: self.personalInfoViewModel,
                                organization: self.orgInfoViewModel,
                                role: self.roleViewModel().role,
                                secretAnswers: [
                                    self.userPassViewModel().secretAnswer1,
                                    self.userPassViewModel().secretAnswer2,
                                    self.userPassViewModel().secretAnswer3
                                ],
                                electronicSignatureAnswers: [
                                    self.esignViewModel().electronicSignatureAnswer1,
                                    self.esignViewModel().electronicSignatureAnswer2,
                                    self.esignViewModel().electronicSignatureAnswer3,
                                    self.esignViewModel().electronicSignatureAnswer4,
                                    self.esignViewModel().electronicSignatureAnswer5
                                ]
                            };
                        }
                        var serializedRegistrationInfo = ko.toJSON(registrationInfo, function (key, value) {
                            //Prevent errors and other irrelevant properties from being sent to the server
                            return $.inArray(key,["verifyEmail","confCodeErrorMessage","codeValidationAdded","errors","codeIsValid", "confirmationCode"])
                            > -1  ? undefined : value;
                        });
                        oeca.logger.debug(serializedRegistrationInfo);
                        $.ajax({
                            url: config.registration.ctx + '/api/registration/v1/user',
                            data: serializedRegistrationInfo,
                            dataType: "json",
                            contentType: oeca.xhrSettings.mimeTypes.JSON,
                            type: 'POST',
                            success: function (result) {
                                self.userProfile(result);
                                self.registrationSubmitted(true);
                                oeca.logger.debug(ko.toJSON(self.userProfile));
                                oeca.notifications.successAlert({
                                    message: 'You have completed core CDX account registration. Click Continue to proceed.' +
                                    ' Depending on your CGP role, you may be asked to complete Identity Proofing before your role is activated.',
                                    buttons: [{
                                        label: 'Continue',
                                        action: function(dialogRef) {
                                            dialogRef.close();
                                            self.continueRegistration();
                                        }
                                    }]
                                });
                            }
                        })
                    };

                    self.redirectToCdx = function () {
                        var cdxSigninData = {
                            userId: self.userPassViewModel().userId,
                            password: self.userPassViewModel().password
                        };
                        self.cdxRedirect = ko.observable(new CdxRedirectWorkflow(cdxSigninData));
                        self.cdxRedirect().authenticate();
                    };

                    self.enableSsnMask = function () {
                        $("input.ssn-mask").hideShowPassword({
                            show: false,
                            innerToggle: false
                        }); ///hidden by default
                        $('#showSsn').on('change', function() {
                            if ($("#showSsn").is(':checked')) {
                                $('input.ssn-mask').hideShowPassword({
                                    show: true
                                });
                            } else {
                                $('input.ssn-mask').hideShowPassword({
                                    show: false
                                });
                            }
                        });
                    };

                    self.continueRegistration = function () {
                        if (self.roleViewModel().role().signatureQuestionsRequired) {
                            self.electronicProofing(true);
                            self.idProofing(new idProofingModel());
                            self.idProofing().userId(self.userPassViewModel().userId);
                            self.idProofing().firstName(self.personalInfoViewModel().firstName);
                            self.idProofing().lastName(self.personalInfoViewModel().lastName);
                            self.idProofing().middleInitial(self.personalInfoViewModel().middleInitial);
                            self.idProofing().userRoleId(self.userProfile().role.userRoleId);
                            self.generateEsa();
                            pager.navigate("#!/id-proofing");
                        } else {
                            self.redirectToCdx();
                        }
                    };

                    self.generateEsa = function() {
                        var userProfileJson = ko.toJSON(self.userProfile);
                        $.ajax({
                            url: config.registration.ctx + '/api/registration/v1/esa/generate',
                            data: userProfileJson,
                            dataType: "html",
                            contentType: oeca.xhrSettings.mimeTypes.JSON,
                            type: 'POST',
                            success: function (res) {
                                self.esaHtml(res);
                                oeca.notifications.showSuccessMessage("Your ESA has been generated");
                            }
                        })
                    };

                    self.verifySign = function () {
                        if (self.idProofing().errors().length > 0) {
                            self.idProofing().errors.showAllMessages();
                            return;
                        }
                        var identityData = self.idProofing;

                        var serializedIdentityData = ko.toJSON(identityData, function (key, value) {
                            //Prevent errors from being sent to the server
                            return $.inArray(key,["errors", "agreeToEsa"])
                            > -1  ? undefined : value;
                        });
                        oeca.logger.debug(serializedIdentityData);
                        $.ajax({
                            url: config.registration.ctx + '/api/registration/v1/identity',
                            data: serializedIdentityData,
                            dataType: "json",
                            contentType: oeca.xhrSettings.mimeTypes.JSON,
                            type: 'POST',
                            success: function (result) {
                                oeca.notifications.showSuccessMessage("You have successfully passed Identity Proofing.");
                                self.signEsa();
                            },
                            error: function(response) {
                                self.electronicProofing(false);
                                self.eProofingFailure(true);
                            }
                        })

                    };

                    self.printEsa = function() {
                        window.frames['esaIframe'].document.execCommand('print', false, null);
                        self.esaPrinted(true);
                    };

                    self.signEsa = function() {
                        //use sign paper ESA service here
                        var userProfileJson = ko.toJSON(self.userProfile);
                        oeca.logger.debug(userProfileJson);
                        $.ajax({
                            url: config.registration.ctx + '/api/registration/v1/esa',
                            data: userProfileJson,
                            dataType: "json",
                            contentType: oeca.xhrSettings.mimeTypes.JSON,
                            type: 'POST',
                            success: function (result) {
                                oeca.notifications.showSuccessMessage("You have successfully signed Electronic Signature Agreement");
                                self.esaSigned(true);
                                self.redirectToCdx();
                            }
                        })
                    };
                    setTimeout(function () {
                        self.roleViewModel().errors.showAllMessages(false);
                    }, 200);
                    self.electronicProofing = ko.observable(true);
                    self.esaPrinted = ko.observable(false);
                    self.eProofingFailure = ko.observable(false);
                    self.esaSigned = ko.observable(false);
                    self.registrationSubmitted = ko.observable(false);
                    self.roleChanged = ko.observable(false);

                    self.roleViewModel = ko.observable(new dataflowRoleViewModel("NETEPACGP"));
                    self.personalInfoViewModel = ko.observable(new persInfoViewModel());
                    self.userPassViewModel = ko.observable(new userPassViewModel());
                    self.orgSearchModel = ko.observable(new orgSearchViewModel());
                    self.orgInfoViewModel = ko.observable();
                    self.esignViewModel = ko.observable();
                    self.userProfile = ko.observable();
                    self.idProofing = ko.observable();//add idProofing properties later for validation reasons

                    self.showPersonalInfo = ko.observable(false);
                    self.showUserPass = ko.observable(false);
                    self.showEsign = ko.observable(false);

                    self.showOrgSearchFields = ko.observable(true);
                    self.showOrgSearchResults = ko.observable(false);
                    self.showOrgInfo = ko.observable(false);
                    self.displaySelectedOrg = ko.observable(false);
                    self.showOrgContact = ko.observable(false);

                    self.showRoleNextButton = ko.observable(true);
                    self.showPersInfoNextButton = ko.observable(true);
                    self.showUserPassNextButton = ko.observable(true);
                    self.showEsignNextButton = ko.observable(true);
                    self.showOrgInfoNextButton = ko.observable(true);
                    self.newOrg = ko.observable(false);

                    self.errors = ko.validation.group(self, {deep: true, observable: true, live: true});
                    self.isValid = ko.computed(function () {
                        if (self.roleViewModel() !== undefined && self.orgInfoViewModel() !== undefined) {
                            if (self.roleViewModel().role().signatureQuestionsRequired) {
                                return (self.errors().length + self.esignViewModel().errors().length + self.orgInfoViewModel().errors().length) <= 0 ;
                            } else {
                                return (self.errors().length + self.orgInfoViewModel().errors().length) <= 0;
                            }
                        } else {
                            return self.errors().length <= 0;
                        }
                    });
                    self.canShowIdProofing = function (page, route, callback) {
                        if (self.isValid()) {
                            callback();
                        } else {
                            pager.navigate("");
                        }
                    }
                };
                //lookups
                registerLookup('registerStates', config.registration.ctx + '/api/registration/v1/reference/state', BaseLookupModel);
                registerLookup('registerCountries', config.registration.ctx + '/api/registration/v1/reference/country', BaseLookupModel);

                var pageViewModel = new viewModel();

                // extend your view-model with pager.js specific data
                pager.extendWithPage(pageViewModel);

                ko.applyBindings(pageViewModel);

                // start pager.js
                pager.start();

            });

            function expandPanel(panelHead) {
                $("#"+panelHead).click();
            }

            function toggleChevron(e) {
                $(e.target)
                        .prev('.panel-heading')
                        .find("i.indicator")
                        .toggleClass('glyphicon-chevron-down glyphicon-chevron-up');
            }




            $(document).ready(function() {
                $('#accordion').on('hidden.bs.collapse', toggleChevron);
                $('#accordion').on('shown.bs.collapse', function(e) {
                    toggleChevron(e);
                    $('[data-toggle="tooltip"]').tooltip();
                });
                $("[data-toggle='modal']").on("contextmenu", function (e) {
                    e.preventDefault();
                    var targetModal = $(this).data('target');
                    $(targetModal).modal("show");
                });

            });
        </script>

        <div data-bind="page: {id: 'start', modal: true, afterShow: afterPageDisplay}">
            <div class="row" style="margin-bottom: 10px">
                <div class="col-sm-3" style="width: 180px">
                    <img src="${pageContext.request.contextPath}/static/img/NeT.png"/>
                </div>
                <div class="col-sm-9">
                    <div class="row bs-wizard" style="border-bottom:0;">
                        <div class="col-xs-4 bs-wizard-step complete">
                            <div class="text-center bs-wizard-stepnum">New Account</div>
                            <div class="progress"><div class="progress-bar"></div></div>
                            <a href="#" class="bs-wizard-dot"></a>
                        </div>
                        <div class="col-xs-4 bs-wizard-step disabled">
                            <div class="text-center bs-wizard-stepnum">ID Proofing</div>
                            <div class="progress"><div class="progress-bar"></div></div>
                            <a href="#" class="bs-wizard-dot"></a>
                        </div>
                        <div class="col-xs-4 bs-wizard-step disabled">
                            <div class="text-center bs-wizard-stepnum">Submission</div>
                            <div class="progress"><div class="progress-bar"></div></div>
                            <a href="#" class="bs-wizard-dot"></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-bottom: 10px">
                <div class="col-sm-8">
                    <h1>Create a New Account</h1>
                </div>
                <div class="col-sm-4" style="margin-top: 35px">
                    <div class="form-group required pull-right">
                        <label class="control-label"></label> = required
                    </div>
                </div>
            </div>
            <div>
                <div class="panel-group" id="accordion">
                    <div class="panel panel-default panel-info" id="rolePanel">
                        <div class="panel-heading">
                            <a data-toggle="collapse" data-parent="#accordion" href="#roleModule">
                                <div>
                                    Select Role
                                    <i class="indicator glyphicon glyphicon-chevron-down pull-right"></i>
                                </div>
                            </a>
                        </div>
                        <div id="roleModule" class="panel-collapse collapse in">
                            <form id="roleForm" data-bind="submit: $root.roleNext, enableAll: !$root.registrationSubmitted()">
                                <div class="panel-body" data-bind="with: roleViewModel">
                                    <div class="row">
                                        <div class="col-sm-4">
                                            <div class="form-group">
                                                <label for="role" class="control-label">Role</label>
                                                <select id="role" class="form-control" data-bind='options: roles, optionsText: "description",
                                                optionsCaption: "Select a role...", value: role'>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <button type="submit" class="btn btn-primary"
                                            data-bind="visible: $root.showRoleNextButton, click: $root.roleNext, enable: errors().length == 0">Next
                                    </button>
                                </div>
                            </form>
                        </div>
                    </div>
                    <div class="panel panel-default panel-info" id="personalInfoPanel" data-bind="if: showPersonalInfo(), visible: showPersonalInfo()">
                        <div class="panel-heading">
                            <a data-toggle="collapse" id="personalInfoHead" data-parent="#accordion" href="#personalInfo">
                                <div>Personal Information
                                    <i class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
                                </div>
                                <div data-bind="fadeVisible: personalInfoViewModel().errors().length >0 && roleChanged()" style="color: darkred">
                                    Please review this section.
                                </div>
                            </a>
                        </div>
                        <div id="personalInfo" class="panel-collapse collapse">
                            <div class="panel-body" data-bind="with: personalInfoViewModel">
                                <form data-bind="submit: $root.personalInfoNext, enableAll: !$root.registrationSubmitted()">
                                    <div class="row">
                                        <div class="col-sm-2">
                                            <div class="form-group">
                                                <label for="title" class="control-label">Title</label><br/>
                                                <select id="title" class="form-control"
                                                        data-bind='options: $root.titles, optionsText: "description", optionsCaption: "Select...",
                                                optionsValue: "description", value: title, hasFocus: $root.showPersonalInfo()'>
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-4">
                                            <div class="form-group">
                                                <label for="firstName" class="control-label">First Name</label>
                                                <input type="text" class="form-control" id="firstName"
                                                       data-bind="textInput: firstName" maxlength="38"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-2">
                                            <div class="form-group">
                                                <label for="middleInitial" class="control-label">Middle Initial</label>
                                                <input type="text" class="form-control" id="middleInitial"
                                                       data-bind="textInput: middleInitial" maxlength="1"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-4">
                                            <div class="form-group">
                                                <label for="lastName" class="control-label">Last Name</label>
                                                <input type="text" class="form-control" id="lastName" maxlength="38"
                                                       data-bind="textInput: lastName"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-2">
                                            <div class="form-group">
                                                <label for="suffix" class="control-label">Suffix</label><br/>
                                                <select id="suffix" class="form-control"
                                                        data-bind="options: $root.suffixes, optionsText: 'description', optionsCaption: 'Select...',
                                            optionsValue: 'description', value: suffix">
                                                </select>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row" data-bind="if: $root.roleViewModel().role().signatureQuestionsRequired">
                                        <div class="col-sm-4">
                                            <div class="form-group">
                                                <label for="jobTitle" class="control-label">Job Title</label>
                            <span class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="right"
                                  title="What is your position in the company? example: Manager"></span>
                                                <input type="text" class="form-control" id="jobTitle" maxlength="38"
                                                       data-bind="textInput: jobTitle.extend({required: {
                                                onlyIf: function() {
                                                    return $root.roleViewModel().role().signatureQuestionsRequired;
                                                }}})" >
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <button type="submit" class="btn btn-primary"
                                                data-bind="visible: $root.showPersInfoNextButton, click: $root.personalInfoNext, enable: errors().length == 0">Next
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default panel-info" id="userPassPanel" data-bind="if: showUserPass, visible: showUserPass">
                        <div class="panel-heading">
                            <a data-toggle="collapse" id="userPassHead" data-parent="#accordion" href="#userPass" aria-expanded="false" aria-controls="userPass">
                                <div>Create a User ID and Password
                                    <i class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
                                </div>
                            </a>
                        </div>
                        <div id="userPass" class="panel-collapse collapse">
                            <div class="panel-body" data-bind="with: userPassViewModel">
                                <form id="userPassForm" data-bind="submit: $root.userPassNext, enableAll: !$root.registrationSubmitted()">
                                    <div class="row">
                                        <div class="form-group col-sm-4">
                                            <label for="userId" class="control-label">User ID</label>
                                            <input type="text" class="form-control" id="userId" maxlength="80"
                                                   data-bind="value: userId, valueUpdate: 'blur', hasFocus: $root.showUserPass()"/>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="password" class="control-label">Password</label>
                                            <input type="password" class="form-control password-mask" id="password" maxlength="15"
                                                   data-bind="enable: enablePasswordInput, value: password, valueUpdate: 'blur', hasFocus: enablePasswordInput()"/>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="verifyPassword" class="control-label">Verify Password</label>
                                            <input type="password" class="form-control password-mask" id="verifyPassword" maxlength="15"
                                                   data-bind="enable: enablePasswordInput, value: verifyPassword, valueUpdate: 'blur'"/>
                                            <br/>
                                            <label>
                                                <input id="showPassword" type="checkbox"/>
                                                Show password
                                            </label>
                                        </div>
                                    </div>
                                    <h4>These questions will be used to reset your password:</h4>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="question1" class="control-label">Question 1</label><br/>
                                            <select id="question1" class="form-control"
                                                    data-bind="options: $root.secretQuestions1, value: secretAnswer1().question, optionsText: 'text',
                                            optionsCaption: 'Select a question...', selectedOptions: $root.selectedQuestion1">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="answer1" class="control-label">Answer 1</label>
                        <span class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="right"
                              title="Answers are case-sensitive."></span>
                                            <input type="password" class="form-control answer-mask" id="answer1" maxlength="80"
                                                   data-bind="textInput: secretAnswer1().answer"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="question2" class="control-label">Question 2</label><br/>
                                            <select id="question2" class="form-control"
                                                    data-bind="options: $root.secretQuestions2, optionsText:'text', value: secretAnswer2().question,
                                           optionsCaption: 'Select a question...', selectedOptions: $root.selectedQuestion2">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="answer2" class="control-label">Answer 2</label>
                                            <input type="password" class="form-control answer-mask" id="answer2" maxlength="80"
                                                   data-bind="textInput: secretAnswer2().answer"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="question3" class="control-label">Question 3</label><br/>
                                            <select id="question3" class="form-control"
                                                    data-bind="options: $root.secretQuestions3, optionsText: 'text', value: secretAnswer3().question,
                                           optionsCaption: 'Select a question...', selectedOptions: $root.selectedQuestion3">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="answer3" class="control-label">Answer 3</label>
                                            <input type="password" class="form-control answer-mask" id="answer3" maxlength="80"
                                                   data-bind="textInput: secretAnswer3().answer"/>
                                            <br/>
                                            <label>
                                                <input id="showAnswer" type="checkbox"/>
                                                Show answers
                                            </label>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-sm-12">
                                            <div class="checkbox">
                                                <label>
                                                    <input type="checkbox" id="termsCheck" value="1" data-bind="checked: termsCheck">
                                                    Agree to the
                                                    <a role="button" data-toggle="modal" data-target="#termsModal">Terms and Conditions</a>
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <button type="submit" class="btn btn-primary"
                                                data-bind="visible: $root.showUserPassNextButton, click: $root.userPassNext, enable: errors().length == 0">Next
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default panel-info" id="esignPanel" data-bind="if: showEsign, visible: showEsign">
                        <div class="panel-heading">
                            <a data-toggle="collapse" id="esignHead" data-parent="#accordion" href="#esign">
                                <div>Electronic Signature Setup
                                    <i class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
                                </div>
                                <div data-bind="fadeVisible: esignViewModel().errors().length >0 && roleChanged()" style="color: darkred">
                                    Please review this section.
                                </div>
                            </a>
                        </div>
                        <div id="esign" class="panel-collapse collapse">
                            <div class="panel-body" data-bind="with: esignViewModel">
                                <form data-bind="submit: $root.esignNext, enableAll: !$root.registrationSubmitted()">
                                    <h4>These questions will be used for signing your document electronically:</h4>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="equestion1" class="control-label">Question 1</label><br/>
                                            <select id="equestion1" class="form-control"
                                                    data-bind="options: $root.cromerrQuestions1, optionsText: 'text', value: electronicSignatureAnswer1().question,
                                           optionsCaption: 'Select a question...', selectedOptions: $root.selectedCromerr1">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="esignAnswer1" class="control-label">Answer 1</label>
                                                    <span class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="right"
                                                          title="Answers are case-sensitive."></span>
                                            <input type="password" class="form-control cromerr-mask" id="esignAnswer1" maxlength="80"
                                                   data-bind="textInput: electronicSignatureAnswer1().answer"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="equestion2" class="control-label">Question 2</label><br/>
                                            <select id="equestion2" class="form-control"
                                                    data-bind="options: $root.cromerrQuestions2, optionsText: 'text', value: electronicSignatureAnswer2().question,
                                           optionsCaption: 'Select a question...', selectedOptions: $root.selectedCromerr2">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="esignAnswer2" class="control-label">Answer 2</label>
                                            <input type="password" class="form-control cromerr-mask" id="esignAnswer2" maxlength="80"
                                                   data-bind="textInput: electronicSignatureAnswer2().answer"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="equestion3" class="control-label">Question 3</label><br/>
                                            <select id="equestion3" class="form-control"
                                                    data-bind="options: $root.cromerrQuestions3, optionsText: 'text', value: electronicSignatureAnswer3().question,
                                           optionsCaption: 'Select a question...', selectedOptions: $root.selectedCromerr3">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="esignAnswer3" class="control-label">Answer 3</label>
                                            <input type="password" class="form-control cromerr-mask" id="esignAnswer3" maxlength="80"
                                                   data-bind="textInput: electronicSignatureAnswer3().answer"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="equestion4" class="control-label">Question 4</label><br/>
                                            <select id="equestion4" class="form-control"
                                                    data-bind="options: $root.cromerrQuestions4, optionsText: 'text', value: electronicSignatureAnswer4().question,
                                           optionsCaption: 'Select a question...', selectedOptions: $root.selectedCromerr4">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="esignAnswer4" class="control-label">Answer 4</label>
                                            <input type="password" class="form-control cromerr-mask" id="esignAnswer4" maxlength="80"
                                                   data-bind="textInput: electronicSignatureAnswer4().answer"/>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="form-group col-sm-8">
                                            <label for="equestion5" class="control-label">Question 5</label><br/>
                                            <select id="equestion5" class="form-control"
                                                    data-bind="options: $root.cromerrQuestions5, optionsText: 'text', value: electronicSignatureAnswer5().question,
                                           optionsCaption: 'Select a question...', selectedOptions: $root.selectedCromerr5">
                                            </select>
                                        </div>
                                        <div class="form-group col-sm-4">
                                            <label for="esignAnswer5" class="control-label">Answer 5</label>
                                            <input type="password" class="form-control cromerr-mask" id="esignAnswer5" maxlength="80"
                                                   data-bind="textInput: electronicSignatureAnswer5().answer"/>
                                            <br/>
                                            <label>
                                                <input id="showCromerrAnswer" type="checkbox"/>
                                                Show answers
                                            </label>
                                        </div>
                                    </div>
                                    <div>
                                        <button type="submit" class="btn btn-primary"
                                                data-bind="visible: $root.showEsignNextButton, click: $root.esignNext, enable: errors().length == 0">Next
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default panel-info" id="orgInfoPanel" data-bind="if: showOrgInfo, visible: showOrgInfo">
                        <div class="panel-heading">
                            <a data-toggle="collapse" id="orgInfoHead" data-parent="#accordion" href="#orgInfo">
                                <div>Organization Information
                                    <i class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
                                </div>
                            </a>
                        </div>
                        <div id="orgInfo" class="panel-collapse collapse">
                            <div class="panel-body">
                                <form id="orgInfoForm" data-bind="with: orgSearchModel, fadeVisible: $root.showOrgSearchFields, enableAll: !$root.registrationSubmitted()">

                                    <h4>Search your organization by one or more of the following criteria:</h4>

                                    <div class="row">
                                        <div class="col-sm-4">
                                            <div class="form-group">
                                                <label for="orgName" class="control-label">Your Organization</label>
                          <span class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="right"
                                title="'Your organization' means the organization of the person registering for the CDX account,
                                 not necessarily the organization seeking permit coverage, e.g. in the case of a contractor preparing an NOI for an operator,
                                 the organization would be the contractor's company, not the operator's company."></span>
                                                <input type="text" class="form-control" id="orgName"
                                                       data-bind="textInput: organizationName"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-4">
                                            <div class="form-group">
                                                <label for="orgAddress1" class="control-label">Mailing Address (line 1)</label>
                                                <input type="text" class="form-control" id="orgAddress1"
                                                       data-bind="textInput: mailingAddress1"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-4">
                                            <div class="form-group">
                                                <label for="orgAddress2" class="control-label">Mailing Address (line 2)</label>
                                                <input type="text" class="form-control" id="orgAddress2"
                                                       data-bind="textInput: mailingAddress2"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div class="row">
                                        <div class="col-sm-3">
                                            <div class="form-group">
                                                <label for="orgCity" class="control-label">City</label>
                                                <input type="text" class="form-control" id="orgCity"
                                                       data-bind="textInput: city"/>
                                            </div>
                                        </div>
                                        <div class="col-sm-3">
                                            <div class="form-group">
                                                <label for="orgState" class="control-label">State</label>
                                                <select  class="form-control" id="orgState"
                                                         data-bind='lookup: "registerStates",
                                                                    value: stateCode,
                                                                    optionsText: "name",
                                                                    optionsValue: "code",
                                                                    optionsCaption: "",
                                                                    valueAllowUnset: true,
                                                                    select2: {placeholder: "Select a State"}'>
                                                </select>
                                            </div>
                                        </div>
                                        <div class="col-sm-2">
                                            <div class="form-group">
                                                <label for="orgZip" class="control-label">Zip/Postal Code</label>
                                                <input type="text" class="form-control" id="orgZip" maxlength="5"
                                                       data-bind="textInput: zip"/>
                                            </div>
                                        </div>
                                    </div>
                                    <div>
                                        <button type="submit" class="btn btn-primary"
                                                data-bind="click: $root.findOrg, enable: $root.showOrgSearchFields()">Find</button>
                                    </div>
                                </form>
                                <!-- ko if: !showOrgSearchFields() -->
                                <span data-bind="fadeVisible: !showOrgSearchResults() && !displaySelectedOrg()" class="fa fa-spinner fa-2x fa-spin"></span>
                                <!-- /ko -->
                                <div id="tableContainer" data-bind="if: showOrgSearchResults">
                                    <form data-bind="fadeVisible: $root.showOrgSearchResults">
                                        <label class="control-label">Select your organization:</label>
                                        <table style="width: 100%" id="orgs" class="table table-striped table-bordered table-hover dataTable responsive">
                                            <thead>
                                            <tr style="height: 40px">
                                                <th>Action</th>
                                                <th>Organization ID</th>
                                                <th>Organization Name</th>
                                                <th>Address 1</th>
                                                <th>Address 2</th>
                                                <th>City</th>
                                                <th>State</th>
                                                <th>Zip Code</th>
                                                <th></th>
                                            </tr>
                                            </thead>
                                            <tbody data-bind="foreach: $root.orgSearchResults()">
                                            <td><button class="btn btn-primary btn-sm" data-bind='click: $root.selectOrg'>Select</button></td>
                                            <td data-bind="text: organizationId"></td>
                                            <td data-bind="text: organizationName"></td>
                                            <td data-bind="text: mailingAddress1"></td>
                                            <td data-bind="text: mailingAddress2"></td>
                                            <td data-bind="text: city"></td>
                                            <td data-bind="text: stateCode"></td>
                                            <td data-bind="text: zip"></td>
                                            <td></td>
                                            </tbody>
                                        </table>
                                        <br/>
                                        <br/>
                                    </form>
                                    <div>
                                        <p>Can't find your organization? <a role="button" id="backToSearch" data-bind="click: backToSearch">Back to search page</a> or
                                            <a role="button" data-bind="click: $root.addNewOrg">request that we add your organization</a>.</p>
                                    </div>
                                </div>

                                <div id="addressContainer" data-bind="if: $root.displaySelectedOrg">
                                    <div><!--ko text: orgInfoViewModel().organizationName--><!--/ko--></div>
                                    <div><!--ko text: orgInfoViewModel().mailingAddress1--><!--/ko--></div>
                                    <div><!--ko text: orgInfoViewModel().mailingAddress2--><!--/ko--></div>
                                    <div><!--ko text: orgInfoViewModel().city--><!--/ko-->, <!--ko text: orgInfoViewModel().stateCode--><!--/ko-->
                                        <!--ko text: orgInfoViewModel().zip--><!--/ko--></div>
                                    <div><!--ko text: orgInfoViewModel().countryCode--><!--/ko--></div>
                                    <br/>
                                    <div>
                                        <small>Wrong organization information? <a role="button" id="backToResults" data-bind="click: backToResults">Back to search results</a> or
                                            <a role="button" data-bind="click: addNewOrg">request that we add your organization</a>.</small>
                                        <br/><br/>
                                        <button class="btn btn-primary" type="submit"
                                                data-bind="visible: showOrgInfoNextButton, click: orgInfoNext">Next
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="panel panel-default panel-info" id="orgContactPanel" data-bind="if: showOrgContact, fadeVisible: showOrgContact">
                        <div class="panel-heading">
                            <a data-toggle="collapse" id="orgContactHead" data-parent="#accordion" href="#orgContact">
                                <div>Contact Information
                                    <i class="indicator glyphicon glyphicon-chevron-up pull-right"></i>
                                </div>
                            </a>
                        </div>
                        <div id="orgContact" class="panel-collapse collapse">
                            <div class="panel-body">
                                <form id="orgContactForm" data-bind="submit: $root.sendVerificationCode, enableAll: !$root.registrationSubmitted()">
                                    <div id="newOrg" data-bind="if: $root.newOrg()">
                                        <!-- ko with: orgInfoViewModel -->
                                        <div class="row">
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label for="newOrgName" class="control-label">Your Organization</label>
                          <span class="glyphicon glyphicon-info-sign" data-toggle="tooltip" data-placement="right"
                                title="'Your organization' means the organization of the person registering for the CDX account,
                                 not necessarily the organization seeking permit coverage, e.g. in the case of a contractor preparing an NOI for an operator,
                                 the organization would be the contractor's company, not the operator's company."></span>
                                                    <input type="text" class="form-control" id="newOrgName"
                                                           data-bind="textInput: organizationName"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label for="newOrgAddress1" class="control-label">Mailing Address (line 1)</label>
                                                    <input type="text" class="form-control" id="newOrgAddress1"
                                                           data-bind="textInput: mailingAddress1"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-4">
                                                <div class="form-group">
                                                    <label for="newOrgAddress2" class="control-label">Mailing Address (line 2)</label>
                                                    <input type="text" class="form-control" id="newOrgAddress2"
                                                           data-bind="textInput: mailingAddress2"/>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-3">
                                                <div class="form-group">
                                                    <label for="newOrgCity" class="control-label">City</label>
                                                    <input type="text" class="form-control" id="newOrgCity"
                                                           data-bind="textInput: city"/>
                                                </div>
                                            </div>
                                            <div class="col-sm-3">
                                                <div class="form-group">
                                                    <label for="newOrgState" class="control-label">State</label>
                                                    <select  class="form-control" id="newOrgState"
                                                             data-bind='lookup: "registerStates",
                                                                    value: stateCode,
                                                                    optionsText: "name",
                                                                    optionsValue: "code",
                                                                    optionsCaption: "",
                                                                    valueAllowUnset: true,
                                                                    select2: {placeholder: "Select a State"}'>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="col-sm-2">
                                                <div class="form-group">
                                                    <label for="newOrgZip" class="control-label">Zip/Postal Code</label>
                                                    <input type="text" class="form-control" id="newOrgZip" maxlength="5"
                                                           data-bind="textInput: zip"/>
                                                </div>
                                            </div>
                                            <div class="col-sm-3">
                                                <div class="form-group">
                                                    <label for="newOrgCountry" class="control-label">Country</label>
                                                    <select  class="form-control" id="newOrgCountry"
                                                             data-bind='lookup: "registerCountries",
                                                                    value: countryCode,
                                                                    optionsText: "name",
                                                                    optionsValue: "code",
                                                                    optionsCaption: "",
                                                                    valueAllowUnset: true,
                                                                    select2: {placeholder: "Select a Country"}'>
                                                    </select>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-sm-3">
                                                <label for="newOrgPhone" class="control-label">Phone Number</label>
                                                <input type="text" class="form-control" id="newOrgPhone"
                                                       data-bind="maskedPhone: phone, valueUpdate: 'blur'"/>
                                            </div>
                                            <div class="form-group col-sm-2">
                                                <label for="newOrgExt" class="control-label">Extension</label>
                                                <input type="text" class="form-control" id="newOrgExt"
                                                       data-bind="value: phoneExtension, valueUpdate: 'blur'"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-sm-3">
                                                <label for="newOrgEmail" class="control-label">Email</label>
                                                <input type="text" class="form-control" id="newOrgEmail"
                                                       data-bind="value: email, valueUpdate: 'blur', disable: codeIsValid()"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-sm-3">
                                                <label for="newOrgVerifyEmail" class="control-label">Re-enter Email</label>
                                                <input type="text" class="form-control" id="newOrgVerifyEmail"
                                                       data-bind="value: verifyEmail, valueUpdate: 'blur', disable: codeIsValid()"/>
                                            </div>
                                            <div class="form-group col-sm-1"><label></label>
                                                <input type="button" id="newOrgSendCode" class="btn btn-primary" value="Send Verification Code"
                                                       data-bind="click: $root.sendVerificationCode, enable: verifyEmail.isValid(),hasFocus: verifyEmail.isValid()">
                                            </div>
                                        </div>
                                        <div class="row" id="newOrgCodeContainer" data-bind="if: codeValidationAdded">
                                            <div class="form-group required has-feedback">
                                                <label for="newOrgCode" class="control-label col-sm-12">Verification Code</label>
                                                <div class="col-sm-5 form-space">
                                                    <input type="text" class="form-control" id="newOrgCode" data-bind="value: confirmationCode, valueUpdate: 'input', disable: codeIsValid()"/>
                                                    <i class="glyphicon glyphicon-ok form-control-feedback" style="margin-right: 10px; color: green" data-bind="fadeVisible: codeIsValid()"></i>
                                                    <i class="glyphicon glyphicon-remove form-control-feedback" style="margin-right: 10px; color: red" data-bind="fadeVisible: !codeIsValid()"></i>
                                                </div>
                                            </div>
                                            <small class="col-sm-12" data-bind="fadeVisible: !codeIsValid()">Haven't received your verification code yet?
                                                <a role="button" data-bind="click: $root.sendVerificationCode">Click to resend</a>.</small>
                                        </div>
                                        <!-- /ko -->
                                    </div>
                                    <div data-bind="if: $root.newOrg() == false">
                                        <!-- ko with: orgInfoViewModel -->
                                        <div class="row">
                                            <div class="form-group col-sm-3">
                                                <label for="phone" class="control-label">Phone Number</label>
                                                <input type="text" class="form-control" id="phone"
                                                       data-bind="maskedPhone: phone, valueUpdate: 'blur'"/>
                                            </div>
                                            <div class="form-group col-sm-2">
                                                <label for="ext" class="control-label">Extension</label>
                                                <input type="text" class="form-control" id="ext"
                                                       data-bind="value: phoneExtension, valueUpdate: 'blur'"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-sm-3">
                                                <label for="email" class="control-label">Email</label>
                                                <input type="text" class="form-control" id="email"
                                                       data-bind="value: email, valueUpdate: 'blur', disable: codeIsValid()"/>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="form-group col-sm-3">
                                                <label for="verifyEmail" class="control-label">Re-enter Email</label>
                                                <input type="text" class="form-control" id="verifyEmail"
                                                       data-bind="value: verifyEmail, valueUpdate: 'blur', disable: codeIsValid()"/>
                                            </div>
                                            <div class="form-group col-sm-1"><label></label>
                                                <input type="button" id="sendCode" class="btn btn-primary" value="Send Verification Code"
                                                       data-bind="click: $root.sendVerificationCode, enable: verifyEmail.isValid(),hasFocus: verifyEmail.isValid()">
                                            </div>
                                        </div>
                                        <div class="row" id="codeContainer" data-bind="if: codeValidationAdded">
                                            <div class="form-group required has-feedback">
                                                <label for="code" class="control-label col-sm-12">Verification Code</label>
                                                <div class="col-sm-5 form-space">
                                                    <input type="text" class="form-control" id="code" data-bind="value: confirmationCode, valueUpdate: 'input', disable: codeIsValid()"/>
                                                    <i class="glyphicon glyphicon-ok form-control-feedback" style="margin-right: 10px; color: green" data-bind="fadeVisible: codeIsValid()"></i>
                                                    <i class="glyphicon glyphicon-remove form-control-feedback" style="margin-right: 10px; color: red" data-bind="fadeVisible: !codeIsValid()"></i>
                                                </div>
                                            </div>
                                            <small class="col-sm-12" data-bind="fadeVisible: !codeIsValid()">Haven't received your verification code yet?
                                                <a role="button" data-bind="click: $root.sendVerificationCode">Click to resend</a>.</small>
                                        </div>
                                        <!-- /ko -->
                                    </div>
                                    <br/>
                                </form>
                            </div>
                            <div class="panel-footer">
                                <input type="button" class="btn btn-primary" value="Register" data-bind="click: submitRegistration, enable: isValid() && !registrationSubmitted()"/>
                                <input type="button" class="btn btn-primary" value="Continue" data-bind="click: continueRegistration, enable: registrationSubmitted()"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div id="termsModal" class="modal fade" role="dialog" aria-hidden="true" style="width: auto" data-keyboard="false" data-backdrop="static" tabindex="-1">
                    <stripes:layout-render name="/static/components/CDXTermsConditionsModal.jsp" />
                </div>

            </div>
        </div>
        <div data-bind="page: {id: 'id-proofing', guard: canShowIdProofing, title: 'CGP Identity Proofing', modal: true, afterShow: enableSsnMask()}">
            <div class="row" style="margin-bottom: 10px">
                <div class="col-sm-3" style="width: 180px">
                    <img src="${pageContext.request.contextPath}/static/img/NeT.png"/>
                </div>
                <div class="col-sm-9">
                    <div class="row bs-wizard" style="border-bottom:0;">
                        <div class="col-xs-4 bs-wizard-step complete">
                            <div class="text-center bs-wizard-stepnum">New Account</div>
                            <div class="progress"><div class="progress-bar"></div></div>
                            <a href="#" class="bs-wizard-dot"></a>
                        </div>
                        <div class="col-xs-4 bs-wizard-step complete">
                            <div class="text-center bs-wizard-stepnum">ID Proofing</div>
                            <div class="progress"><div class="progress-bar"></div></div>
                            <a href="#" class="bs-wizard-dot"></a>
                        </div>
                        <div class="col-xs-4 bs-wizard-step disabled">
                            <div class="text-center bs-wizard-stepnum">Submission</div>
                            <div class="progress"><div class="progress-bar"></div></div>
                            <a href="#" class="bs-wizard-dot"></a>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" style="margin-bottom: 10px">
                <div class="col-sm-8">
                    <h1>Identity Proofing</h1>
                </div>
                <div class="col-sm-4" style="margin-top: 35px">
                    <div class="form-group required pull-right">
                        <label class="control-label"></label> = required
                    </div>
                </div>
            </div>
            <div class="panel panel-info" id="idProofingPanel">
                <div class="panel-heading">
                    <h4 class="panel-title">Identity Verification</h4>
                </div>
                <div class="panel-body">
                    <form role="form" class="form" id="proofingOptionDiv" data-bind="if: !eProofingFailure()">
                        <div class="form-group required">
                            <label for="proofingOption">Would you like to perform electronic Identity Proofing?</label><br/>
                            <input type="checkbox" class="form-control" id="proofingOption" data-on-text="YES" data-off-text="NO"
                                   data-bind="bootstrapSwitchOn: electronicProofing">
                        </div>
                        <hr class="title" />
                    </form>

                    <div data-bind="fadeVisible: electronicProofing()">
                        <div data-bind="with: $root.idProofing">
                            <form role="form" class="form" data-bind="submit: $root.verifySign"
                                  autocomplete="off">
                                <h4>Electronic Identity Proofing</h4>
                                <h5>The following information will be used for identity proofing, it will not be stored.</h5>
                                <div class="row">
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label for="address1" class="control-label">Home Mailing Address (line 1)</label>
                                            <input type="text" class="form-control" id="address1"
                                                   data-bind="textInput: mailingAddress1"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-4">
                                        <div class="form-group">
                                            <label for="address2" class="control-label">Home Mailing Address (line 2)</label>
                                            <input type="text" class="form-control" id="address2"
                                                   data-bind="textInput: mailingAddress2"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label for="city" class="control-label">City</label>
                                            <input type="text" class="form-control" id="city"
                                                   data-bind="textInput: city"/>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label for="state" class="control-label">State</label>
                                            <select  class="form-control" id="state"
                                                     data-bind='lookup: "registerStates",
                                                                    value: state,
                                                                    optionsText: "name",
                                                                    optionsValue: "code",
                                                                    optionsCaption: "",
                                                                    valueAllowUnset: true,
                                                                    select2: {placeholder: "Select a State"}'>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="col-sm-3">
                                        <div class="form-group">
                                            <label for="zip" class="control-label">Zip/Postal Code</label>
                                            <input type="text" class="form-control" id="zip" maxlength="5"
                                                   data-bind="textInput: zip" autocomplete="new-password"/>
                                        </div>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-sm-12">
                                        <label for="dob" class="control-label">Date of Birth</label><br/>
                                        <input type="text" data-custom-class="form-control col-sm-3" id="dob" data-format="YYYY-MM-DD"
                                               data-template="MMM D YYYY" data-smart-days="true"
                                               data-max-year="1999" data-min-year="1917"
                                               data-bind="value: dateOfBirth, combodate"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-sm-3">
                                        <label for="ssn" class="control-label">SSN Last 4:</label>
                                        <input type="password" class="form-control ssn-mask" id="ssn" data-bind="textInput: ssnlast4"
                                               autocomplete="new-password"/>
                                        <br/>
                                        <label>
                                            <input id="showSsn" type="checkbox"/>
                                            Show SSN digits
                                        </label>
                                    </div>
                                    <div class="form-group col-sm-3">
                                        <label for="idPhone" class="control-label">Phone Number</label>
                                        <input type="text" class="form-control" id="idPhone"
                                               data-bind="value: phone, inputmask, inputmaskOptions:{ mask: '999-999-9999', autoUnmask: true},
                                            valueUpdate: 'blur'"/>
                                    </div>
                                </div>
                                <div class="row">
                                    <div class="form-group col-sm-12">
                                        <div class="checkbox">
                                            <label class="control-label"><input type="checkbox" value="1" data-bind="checked: agreeToEsa">
                                                I agree to the
                                                <a role="button" data-toggle="modal" data-target="#esaModal">Electronic Signature Agreement</a>
                                            </label>
                                        </div>
                                    </div>
                                </div>
                                <div id="esaModal" class="modal fade" role="dialog" aria-hidden="true" style="width: auto" data-keyboard="false" data-backdrop="static" tabindex="-1">
                                    <stripes:layout-render name="/static/components/esaModal.jsp" />
                                </div>
                                <iframe hidden width="100%" height="400" id="esaIframe" name="esaIframe" data-bind="iframeContent: $root.esaHtml">
                                </iframe>
                                <div>
                                    <input type="button" id="idProofingSign" class="btn btn-primary" value="Verify and Sign"
                                           data-bind="click: $root.verifySign, enable: errors().length == 0">
                                </div>
                            </form>
                        </div>
                    </div>
                    <br/>
                    <div id="verifyFail" class="row alert alert-danger" role="alert" style="margin-left: 10px; margin-right: 10px"
                         data-bind="fadeVisible: eProofingFailure">
                        <div class="col-sm-1">
                            <span class="glyphicon glyphicon-warning-sign" style="color: red; margin-top: 8px"></span>
                        </div>
                        <div class="col-sm-11" style="margin-top: 5px">
							<span style="font-weight: bold; color: red">
								We were unable to verify your identity with the provided information. Please print, review, sign, and mail your paper Electronic Signature Agreement.
							</span>
                        </div>
                    </div>

                    <div id="paperForm" data-bind="if: !electronicProofing(), fadeVisible: !electronicProofing()">
                        <div id="paperFormMessage" class="row" style="margin-left: 10px; margin-right: 10px">
                            <h5>You have selected to perform identity proofing via the paper based option.
                                Please be aware that the paper based option requires you to mail a wet ink signed version of the ESA and does take longer to process.
                                You will not be able to access the CGP system until this document is received and processed. </h5>
                        </div>
                        <form role="form" class="form" id="paperEsaForm">
                            <h4>Paper ESA</h4>
                            <iframe width="100%" height="400" name="esaIframe" data-bind="iframeContent: $root.esaHtml">
                            </iframe>

                            <br/>
                            <div class="row">
                                <div class="col-sm-1">
                                    <input type="button" id="printEsa" class="btn btn-primary" data-bind="click: printEsa" value="Print">
                                </div>
                                <div class="col-sm-2">
                                    <input type="submit" class="btn btn-primary" value="Continue" data-bind="click: redirectToCdx, enable: esaPrinted()">
                                </div>
                            </div>
                            <br/>
                        </form>
                    </div>

                </div>
            </div>
        </div>

    </stripes:layout-component>
</stripes:layout-render>