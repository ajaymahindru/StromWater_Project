oeca.cgp = {
	/*
	 * Defines all the alerts for the noi form.  This is to help keep the controllers cleaner.
	 */
	constants: {
		LEW_TYPE: 'Low_Erosivity_Waiver',
		NOI_TYPE: 'Notice_Of_Intent',
		status: {
			DRAFT: 'Draft',
			TERMINATED: 'Terminated',
			ACTIVE: 'Active',
			SUBMITTED: 'Submitted',
			DISCONTINUED: 'Discontinued',
			ARCHIVED: 'Archived',
			ONHOLD: 'OnHold',
			DENIED: 'Denied'
		},
		attachmentCategories: {
			CHEMICAL_INFO: 'Chemical_Treatment_Information',
			ENDANGERED_SPECIES: 'Endangered_Species_Protection'
		},
		tier: {
			Tier_2: "Tier 2",
			Tier_2_5: "Tier 2.5",
			Tier_3: "Tier 3",
			NA: "N/A"
		},
		latLongDataSource: {
			Map: 'Map',
			GPS: 'GPS'
		},
		appendexEStep4Responses: {
			WRITTEN_NO: 'Written Indication that no historic properties ' +
						'will be affected by the installation of storm water controls.',
			WRITTEN_YES: 'Written indication that adverse effects to historic properties ' +
						'from the installation of stormwater controls can be mitigated by agreed upon actions.',
			NO_AGREEMENT: 'No agreement has been reached regarding measures to mitigate affects ' +
						'to historic properties from the installation of stormwater controls.'
		},
		constructionTypes: {
			SINGLE: 'Single-Family Residential',
			MULTI: 'Multi-Family Residential',
			COMMERCIAL: 'Commercial',
			INDUSTRIAL: 'Industrial',
			INSTITUTIONAL: 'Institutional',
			ROAD: 'Highway or Road',
			UTILITY: 'Utility'
		},
		roles: {
		    120410: 'Certifier',
		    120420: 'Preparer',
		    120430: 'Regulatory Authority',
		    120440: 'Help Desk'
		},
		appendixDCriteria: {
			Criterion_A: 'Criterion A',
			Criterion_B: 'Criterion B',
			Criterion_C: 'Criterion C',
			Criterion_D: 'Criterion D',
			Criterion_E: 'Criterion E',
			Criterion_F: 'Criterion F'
		}
	},
	notifications: {
		successAlert: function(msg) {
			if(typeof msg == "string") {
				msg = {message: msg};
			}
			msg = $.extend({
				type: BootstrapDialog.TYPE_SUCCESS,
				title: '<span class="glyphicon glyphicon-ok-sign center-block"></span>',
				bodyTitle: 'Success!',
				message: msg,
				cssClass: "oeca-alert oeca-success-alert",
				buttons: [{
					label: 'Ok',
					action: function(dialogRef) {
						dialogRef.close();
					}
				}],
				id: null
			}, msg);
			var dialog = new BootstrapDialog(msg);
			dialog.realize();
			if(msg.bodyTitle) {
				dialog.getModalBody().prepend($('<h1>').append(msg.bodyTitle));
			}
			if(msg.helpText) {
				dialog.getModalFooter().append($('<span>').addClass('help-block').append(msg.helpText));
			}
			if(msg.id) {
				dialog.setId(msg.id);
			}
			dialog.open();
			return dialog;
		},
		infoAlert: function(msg) {
			if(typeof msg == "string") {
				msg = {message: msg};
			}
			msg = $.extend({
				type: BootstrapDialog.TYPE_INFO,
				title: '<span class="glyphicon glyphicon-question-sign center-block"></span>',
                bodyTitle: 'Alert!',
				message: msg,
				cssClass: "oeca-alert oeca-info-alert",
				buttons: [{
					label: 'Ok',
					action: function(dialogRef) {
						dialogRef.close();
					}
				}]
			}, msg);
			var dialog = new BootstrapDialog(msg);
			dialog.realize();
			if(msg.bodyTitle) {
				dialog.getModalBody().prepend($('<h1>').append(msg.bodyTitle));
			}
			if(msg.helpText) {
				dialog.getModalFooter().append($('<span>').addClass('help-block').append(msg.helpText));
			}
			dialog.open();
			return dialog;
		},
		errorAlert: function(msg) {
			if(typeof msg == "string") {
				msg = {message: msg};
			}
			msg = $.extend({
				type: BootstrapDialog.TYPE_DANGER,
				title: '<span class="glyphicon glyphicon-remove-sign center-block"></span>',
                bodyTitle: 'Warning!',
				message: msg,
				cssClass: "oeca-alert oeca-error-alert",
				buttons: [{
					label: 'Ok',
					action: function(dialogRef) {
						dialogRef.close();
					}
				}]
			}, msg);
			var dialog = new BootstrapDialog(msg);
			dialog.realize();
			if(msg.bodyTitle) {
				dialog.getModalBody().prepend($('<h1>').append(msg.bodyTitle));
			}
			if(msg.helpText) {
				dialog.getModalFooter().append($('<span>').addClass('help-block').append(msg.helpText));
			}
			dialog.open();
			return dialog;
		},
		certification: function() {
			oeca.cgp.notifications.successAlert({
				message: 'You have certified this form and submitted it to the EPA for approval. You will receive an ' +
					'Email notification once your form is approved.',
				buttons: [{
					label: 'Close',
					action: function(dialogRef) {
						dialogRef.close();
						pager.navigate("#!/home");
					}
				}]
			});
		},
		routePreparer: function() {
			oeca.cgp.notifications.successAlert({
				message: 'Your form has been routed.',
				helpText: 'The preparer must now route the form to an approved Certifier to sign and submit this document for approval by the EPA.',
				buttons: [{
					label: 'Ok',
					action: function(dialogRef) {
						dialogRef.close();
						pager.navigate("#!/home");
					}
				}]
			});
		},
		routeCertifier: function() {
			oeca.cgp.notifications.successAlert({
				 message: 'Your form has been routed.  You will received an email notification once your form has ' + 
					'been certified.',
				 buttons: [{
					label: 'Ok',
					action: function(dialogRef) {
						dialogRef.close();
						pager.navigate("#!/home");
					}
				}]
			});
		},
		inviteCertifier: function() {
			oeca.cgp.notifications.successAlert({
				message: 'We\'ll send your Certifier an invitation to register for CGP. You will need to route your ' +
					'form to your Certifier once they have registered.',
                helpText: 'You may still edit and resubmit your form for certification from the home page.',
				buttons: [{
					label: 'Ok',
					action: function(dialogRef) {
						dialogRef.close();
						pager.navigate("#!/home");
					}
				}]
			});
		},
		invitePreparer: function() {
			oeca.cgp.notifications.successAlert({
				message: 'We\'ll send your Preparer an invitation to register for CGP. You will need to route your ' +
						'form to your Preparer once they have registered.',
				helpText: 'You may still edit and resubmit your form for certification from the home page.',
				buttons: [{
                    label: 'Ok',
                    action: function (dialogRef) {
                        dialogRef.close();
                        pager.navigate("#!/home");
                    }
                }]
			})
		},
		lewQualification: function() {
		    oeca.cgp.notifications.infoAlert({
		        bodyTitle: 'Do I qualify for a LEW?',
		        cssClass: 'oeca-alert oeca-info-alert oeca-lew-qualification',
		        size: BootstrapDialog.SIZE_WIDE,
		        message: '<p>EPA may waive permit requirements for stormwater discharges from construction activities that ' +
                    'disturb less than five (5) acres if the construction activity will take place during a period ' +
                    'when the rain fall erosivity factor (R-Factor) is less than five (5).  To assist you in ' +
                    'calculating your R Factor, please use either of the following links:</p> ' +
                    '<div style="margin-bottom: 7px">' +
                    '<a href="' + oeca.cgp.urls.rfactorCalculator + '" target="_blank" class="btn btn-primary">R-Factor Calculator</a>' +
                    '<a href="' + oeca.cgp.urls.factSheet + '" target="_blank" class="btn btn-primary" style="margin-left: 20px">EPA Fact Sheet 3.1</a>' +
                    '</div>' +
                    '<p>If your project is located in one of the areas where EPA is the NPDES permitting authority, ' +
                    'and if your project disturbs less than 5 acres and has a R-Factor of less than 5 then you ' +
                    'qualify for an LEW.</p><p>If you do not qualify for an LEW, you must fill out a CGP NOI ' +
                    'application.</p>'
            });
		},
		endangeredAppendixA: function() {
			oeca.notifications.showAlertDialog("Criterion A", '<span class="criterion-description">No ESA-listed ' +
					'species and/or designated critical habitat present in action area.</span> Using the process ' +
					'outlined in Appendix D of this permit, you certify that ESA-listed species and designated ' +
					'critical habitat(s) under the jurisdiction of the USFWS or NMFS are not likely to occur in ' +
					'your site\'s "action area" as defined in Appendix A of this permit. <strong>[A basis statement ' +
					'supporting the selection of this criterion should identify the USFWS and NMFS information ' +
					'sources used. Attaching aerial image(s) of the site to this NOI is helpful to EPA, USFWS, and ' +
					'NMFS in confirming eligibility under this criterion. Please Note: NMFS\' jurisdiction includes ' +
					'ESA-listed marine and estuarine species that spawn in inland rivers.]</strong>', {
				closeButtonText: "Ok"
			});
		},
		endangeredAppendixB: function() {
			oeca.notifications.showAlertDialog("Criterion B", '<span class="criterion-description">Eligibility ' +
					'requirements met by another operator under the 2017 CGP.</span> The construction site\'s ' +
					'discharges and discharge-related activities were already addressed in another operator\'s ' +
					'valid certification of eligibility for your "action area" under eligibility Criterion A, C, D, ' +
					'E, or F of the 2017 CGP and you have confirmed that no additional ESA-listed species and/or ' +
					'designated critical habitat under the jurisdiction of USFWS and/or NMFS not considered in the ' +
					'that certification may be present or located in the "action area."  To certify your ' +
					'eligibility under this criterion, there must be no lapse of NPDES permit coverage in the other ' +
					'CGP operator\'s certification.  By certifying eligibility under this criterion, you agree to ' +
					'comply with any conditions upon which the other CGP operator\'s certification was based.  You ' +
					'must include in your NOI the NPDES ID from the other 2017 CGP operator\'s notification of ' +
					'authorization under this permit. If your certification is based on another 2017 CGP ' +
					'operator\'s certification under criterion C, you must provide EPA with the relevant supporting ' +
					'information required of existing dischargers in criterion C in your NOI form. <strong>[A basis ' +
					'statement supporting the selection of this criterion should identify the eligibility criterion ' +
					'of the other CGP NOI, the authorization date, and confirmation that the authorization is ' +
					'effective.]</strong>', {
				closeButtonText: "Ok"
			});
		},
		endangeredAppendixC: function() {
			oeca.notifications.showAlertDialog("Criterion C", '<span class="criterion-description">Discharges not' +
					' likely to adversely affect ESA-listed species and/or designated critical habitat.</span>  ' +
					'ESA-listed species and/or designated critical habitat(s) under the jurisdiction of the USFWS ' +
					'and/or NMFS are likely to occur in or near your site\'s "action area," and you certify that ' +
					'your site\'s discharges and discharge-related activities are not likely to adversely affect ' +
					'ESA-listed threatened or endangered species and/or designated critical habitat.  This ' +
					'certification may include consideration of any stormwater controls and/or management practices ' +
					'you will adopt to ensure that your discharges and discharge-related activities are not likely ' +
					'to adversely affect ESA-listed species and/or designated critical habitat.  To certify your ' +
					'eligibility under this criterion, indicate 1) the ESA-listed species and/or designated habitat ' +
					'located in your "action area" using the process outlined in Appendix D of this permit;  2) the ' +
					'distance between the site and the listed species and/or designated critical habitat in the ' +
					'action area (in miles); and 3) a rationale describing specifically how adverse effects to ' +
					'ESA-listed species will be avoided from the discharges and discharge-related activities.  You ' +
					'must also include a copy of your site map from your SWPPP showing the upland and in-water ' +
					'extent of your "action area" with this NOI. <strong>[A basis statement supporting the ' +
					'selection of this criterion should identify the information resources and expertise (e.g., ' +
					'state or federal biologists) used to arrive at this conclusion. Any supporting documentation ' +
					'should explicitly state that both ESA-listed species and designated critical habitat under the ' +
					'jurisdiction of the USFWS and/or NMFS were considered in the evaluation. Attaching aerial ' +
					'image(s) of the site to this NOI is helpful to EPA, USFWS, and NMFS in confirming eligibility ' +
					'under this criterion.]</strong>', {
				closeButtonText: "Ok"
			});
		},
		endangeredAppendixD: function() {
			oeca.notifications.showAlertDialog("Criterion D", '<span class="criterion-description">Coordination ' +
					'with USFWS and/or NMFS has successfully concluded.</span>  The coordination must have addressed ' +
					'the effects of your site\'s discharges and discharge-related activities on ESA-listed species ' +
					'and/or designated critical habitat under the jurisdiction of USFWS and/or NMFS, and resulted ' +
					'in a written concurrence from USFWS and/or NMFS that your site\'s discharges and ' +
					'discharge-related activities are not likely to adversely affect listed species and/or critical ' +
					'habitat.  You must include copies of the correspondence with the participating agencies in ' +
					'your SWPPP and this NOI. <strong>[A basis statement supporting the selection of this criterion ' +
					'should identify whether USFWS or NMFS or both agencies participated in coordination, the field ' +
					'office/regional office(s) providing that coordination, and the date that coordination ' +
					'concluded.]</strong>', {
				closeButtonText: "Ok"
			});
		},
		endangeredAppendixE: function() {
			oeca.notifications.showAlertDialog("Criterion E", '<span class="criterion-description">ESA Section 7 ' +
					'consultation between a Federal Agency and the USFWS and/or NMFS has successfully concluded.' +
					'</span>  The consultation must have addressed the effects of the construction site\'s ' +
					'discharges and discharge-related activities on ESA-listed species and/or designated critical ' +
					'habitat under the jurisdiction of USFWS and/or NMFS.  To certify eligibility under this ' +
					'criterion, Indicate the result of the consultation:' +
					'<ul><li>biological opinion from USFWS and/or NMFS that concludes that the action in question ' +
					'(taking into account the effects of your site\'s discharges and discharge-related activities) ' +
					'is not likely to jeopardize the continued existence of listed species, nor the destruction or ' +
					'adverse modification of critical habitat; or</li>' +
					'<li>written concurrence from USFWS and/or NMFS with a finding that the site\'s discharges and ' +
					'discharge-related activities are not likely to adversely affect ESA-listed species and/or ' +
					'designated critical habitat.</li></ul>' +
					'You must include copies of the correspondence between yourself and the USFWS and/or NMFS in ' +
					'your SWPPP and this NOI. <strong>[A basis statement supporting the selection of this criterion ' +
					'should identify the federal action agencie(s) involved, the field office/regional office(s) ' +
					'providing that consultation, any tracking numbers of identifiers associated with that ' +
					'consultation (e.g., IPaC number, PCTS number), and the date the consultation was completed.]' +
					'</strong>', {
				closeButtonText: "Ok"
			});
		},
		endangeredAppendixF: function() {
			oeca.notifications.showAlertDialog("Criterion F", '<span class="criterion-description">Issuance of ' +
					'section 10 permit.</span> Potential take is authorized through the issuance of a permit under ' +
					'section 10 of the ESA by the USFWS and/or NMFS, and this authorization addresses the effects ' +
					'of the site\'s discharges and discharge-related activities on ESA-listed species and ' +
					'designated critical habitat.  You must include copies of the correspondence between yourself ' +
					'and the participating agencies in your SWPPP and your NOI. <strong>[A basis statement ' +
					'supporting the selection of this criterion should identify whether USFWS or NMFS or both ' +
					'agencies provided a section 10 permit, the field office/regional office(s) providing ' +
					'permit(s), any tracking numbers of identifiers associated with that consultation (e.g., IPaC ' +
					'number, PCTS number), and the date the permit was granted.]</strong>', {
				closeButtonText: "Ok"
			});
		},
		screeningQuestionsLocked: function(cancelCallback, nextCallback) {
			oeca.cgp.notifications.infoAlert({
				bodyTitle: 'Warning',
				message: 'After clicking next, you will not be able to edit any of these questions and will need to' +
					' start over if the answers must be changed.',
				buttons: [
				          {
				        	  label: 'Return',
				        	  cssClass: 'btn-primary-outline',
				        	  action: function(dialog) {
				        		  dialog.close();
				        		  if(cancelCallback) {
                                      cancelCallback();
				        		  }
				        	  }
				          },
				          {
				        	  label: 'Next',
				        	  cssClass: 'btn-primary',
				        	  action: function(dialogRef) {
				        		  dialogRef.close();
								  if(nextCallback) {
									  nextCallback();
								  }
				        	  }
				          }
				],
			});
		},
		noiCreated: function(trackingNumber) {
			oeca.cgp.notifications.successAlert({
				message: 'You have successfully created a draft Notice of Intent permit.  Your Tracking Number is '+ trackingNumber +'.',
				helpText: 'Copy/paste or print your Tracking Number for your records. You can use it to identify your permit prior to NPDES ID assignment.',
				id: 'noiCreated',
				buttons: [
					{
						label: 'Continue',
						cssClass: 'btn-success-outline',
						action: function(dialog) {
							dialog.close();
							return true;
						}
					},
					{
						label: 'Print',
						cssClass: 'btn-primary-outline',
						action: function() {
							$("#noiCreated").printThis();
							return false;
						}
					}
				]
			})
		},
		lewCreated: function(trackingNumber) {
			oeca.cgp.notifications.successAlert({
				message: 'You have successfully created a draft Low Erosivity Waiver.  Your Tracking Number is '+ trackingNumber +'.',
				helpText: 'Copy/paste or print your Tracking Number for your records. You can use it to identify your permit prior to NPDES ID assignment.',
				id: 'lewCreated',
				buttons: [
					{
						label: 'Continue',
						cssClass: 'btn-success-outline',
						action: function(dialog) {
							dialog.close();
							return true;
						}
					},
					{
						label: 'Print',
						cssClass: 'btn-primary-outline',
						action: function() {
							$("#lewCreated").printThis();
							return false;
						}
					}
				]
			})
		},
		csvIncludeDischarge: function(runCsvExport) {
			oeca.cgp.notifications.infoAlert({
				bodyTitle: 'CSV Extract Options',
				message: 'Would you like to include Discharge Information Details in CSV extract?',
				buttons: [
					{
						label: 'Yes',
						cssClass: 'btn-success-outline',
						action: function(dialog) {
							dialog.close();
							if (runCsvExport) {
								runCsvExport(true);
							}
						}
					},
					{
						label: 'No',
						cssClass: 'btn-primary-outline',
						action: function(dialog) {
							dialog.close();
							if (runCsvExport) {
								runCsvExport(false);
							}
						}
					}
				]
			})
		},
        fieldLocked: function(field, cancelAction, terminateAction) {
		    oeca.cgp.notifications.errorAlert({
		        bodyTitle: 'Not Allowed',
                message: 'Since this form has already been submitted to EPA, you must resubmit this form to change ' + field + '.',
                buttons: [
                    {
                        label: 'Return',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(cancelAction) {
                                cancelAction(dialogRef);
                            }
                        },
                        cssClass: 'btn-danger-outline'
                    },
                    {
                        label: 'Terminate Form',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(terminateAction) {
                                terminateAction(dialogRef);
                            }
                        },
                        cssClass: 'btn-danger'
                    }
                ],
                helpText: 'If you have any questions, please contact the <a href="JavaScript:">Help Desk</a>'
            })
        },
        operatorNameLocked: function(field, cancelAction, terminateAction) {
		    oeca.cgp.notifications.errorAlert({
		        bodyTitle: 'Not Allowed',
                message: 'Since this form has already been submitted to EPA, you must submit a new form to change the Operator Name.',
                buttons: [
                    {
                        label: 'Return',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(cancelAction) {
                                cancelAction(dialogRef);
                            }
                        },
                        cssClass: 'btn-danger-outline'
                    }
                ]
            })
        },
		projectStateLocked: function(cancelAction) {
			oeca.cgp.notifications.errorAlert({
				bodyTitle: 'Not Allowed',
				message: 'The Project/Site Address State must be the same as the State under Permit Information.',
				buttons: [
					{
						label: 'Ok',
						action: function(dialogRef) {
							dialogRef.close();
							if(cancelAction) {
								cancelAction(dialogRef);
							}
						},
						cssClass: 'btn-danger-outline'
					}
				],
				helpText: 'Your Master Permit number was assigned based on the state reported in the permit information section. To change your project/site state you must create a new form.'
			})
		},
        waitListReset: function(field, cancelAction, continueAction) {
		    oeca.cgp.notifications.infoAlert({
		        bodyTitle: '14-Day Waitlist Reset',
                message: 'Any change to ' + field + ' will result in a reset of the 14-day hold period.',
                buttons: [
                    {
                        label: 'Nevermind',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(cancelAction) {
                                cancelAction(dialogRef);
                            }
                        },
                        cssClass: 'btn-primary-outline'
                    },
                    {
                        label: 'Edit Anyway',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(continueAction) {
                                continueAction(dialogRef)
                            }
                        },
                        cssClass: 'btn-primary-outline'
                    }
                ]
            });
        },
		criterionAttachment: function(newCriterion, cancelAction, continueAction) {
			oeca.cgp.notifications.infoAlert({
				bodyTitle: 'Endangered Species Protection Attachment(s)',
				message: 'You have changed Appendix D criterion to ' + oeca.cgp.constants.appendixDCriteria[newCriterion] + '. Would you like to keep previously uploaded attachments?',
				buttons: [
					{
						label: 'Remove',
						action: function(dialogRef) {
							dialogRef.close();
							if(continueAction) {
								continueAction()
							}
						},
						cssClass: 'btn-danger-outline'
					},
					{
						label: 'Keep',
						action: function(dialogRef) {
							dialogRef.close();
							if(cancelAction) {
								cancelAction(dialogRef);
							}
						},
						cssClass: 'btn-primary-outline'
					}
				]
			});
		},
		changeForm: function(cancelAction, changeAction) {
			oeca.cgp.notifications.infoAlert({
				bodyTitle: 'Change Form?',
				message: 'You have selected Change.  Editing any form criteria will result in a reset of your 14 day hold period and may cause your NPDES ID to change.',
				buttons: [
					{
						  label: 'Return',
						  action: function(dialogRef) {
							  dialogRef.close();
							  if(cancelAction) {
                                  cancelAction();
							  }
						  }
					},
					{
						  label: 'Change Form',
						  cssClass: 'btn-primary',
						  action: function(dialog) {
							  if(changeAction) {
								  changeAction();
							  }
							  dialog.close();
						  }
					}

				],
				helpText: 'For more information on eligibility for a LEW <a href="JavaScript:;" data-bind="click: function(){oeca.cgp.notifications.lewQualification()}">click here</a>.  Note, if you do not quality for a LEW you must fill out a NOI application.'
			});
		},
		reviseForm: function(typeAcronym, cancelAction, reviseAction) {
            oeca.cgp.notifications.infoAlert({
                bodyTitle: 'Change Form?',
                message: 'Clicking continue will create a draft change ' + typeAcronym + '. To submit any changes, you must certify your form.',
                buttons: [
                    {
                        label: 'Return',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(cancelAction) {
                                cancelAction();
                            }
                        }
                    },
                    {
                        label: 'Continue',
                        cssClass: 'btn-primary',
                        action: function(dialog) {
                            if(reviseAction) {
                                reviseAction();
                            }
                            dialog.close();
                        }
                    }

                ]
            });
		},
		terminate: function(cancelAction, terminateAction) {
			oeca.cgp.notifications.errorAlert({
				message: 'You have selected Terminate.  Are you sure you want to delete this form and all of its contents',
				buttons: [
					{
						label: 'Return',
						cssClass: 'btn-danger-outline',
						action: function(dialogRef) {
							dialogRef.close();
							if(cancelAction) {
								cancelAction(dialogRef);
							}
						}
					},
					{
						label: 'Terminate Form',
						cssClass: 'btn-danger',
						action: function(dialogRef) {
							dialogRef.close();
							if(terminateAction) {
								terminateAction(dialogRef);
							}
						}
					}
				],
				helpText: 'If you have legal authority to sign and submit this document to the EPA and would like to become a Certifier, please contact the <a href="JavaScript:">Help Desk</a>'
			})
		},
		saveAndClose: function() {
			oeca.cgp.notifications.successAlert({
				message: 'Your form has been saved.',
				buttons: [{
					label: 'Return to Home',
					action: function(dialogRef) {
						dialogRef.close();
						pager.navigate("#!/home");
					}
				}]
			});
		},
        denyForm: function(cancelAction, rejectAction) {
            oeca.cgp.notifications.errorAlert({
                bodyTitle: 'Reason for Rejection',
                message: '<textarea id="reject-reason"></textarea>',
                buttons: [
                    {
                        label: 'Cancel',
                        cssClass: 'btn-default',
                        action: function(dialogRef) {
                            /*dialogRef.close();
                            if(cancelAction) {
                                cancelAction(dialogRef);
                            }*/
                        }
                    },
                    {
                        label: 'Reject Form',
                        cssClass: 'btn-danger',
                        action: function(dialogRef) {
                        	console.log("reject form confirmed");
                        	console.log(dialogRef);
                            dialogRef.close();
                            if(rejectAction) {
                                rejectAction(dialogRef);
                            }
                        }
                    }
                ]
            })
        },
        deleteForm: function(cancelAction, terminateAction) {
            oeca.cgp.notifications.errorAlert({
                message: 'You have selected Delete.  Are you sure you want to delete this form and all of its contents',
                buttons: [
                    {
                        label: 'Return',
                        cssClass: 'btn-danger-outline',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(cancelAction) {
                                cancelAction(dialogRef);
                            }
                        }
                    },
                    {
                        label: 'Delete Form',
                        cssClass: 'btn-danger',
                        action: function(dialogRef) {
                            dialogRef.close();
                            if(terminateAction) {
                                terminateAction(dialogRef);
                            }
                        }
                    }
                ]
            });
        },
		revertForm: function(cancelAction, terminateAction) {
			oeca.cgp.notifications.errorAlert({
				bodyTitle: 'Revert?',
				message: 'You have selected Revert.  Are you sure you want to revert this form to its previous state?',
				buttons: [
					{
						label: 'Return',
						cssClass: 'btn-danger-outline',
						action: function(dialogRef) {
							dialogRef.close();
							if(cancelAction) {
								cancelAction(dialogRef);
							}
						}
					},
					{
						label: 'Revert Form',
						cssClass: 'btn-danger',
						action: function(dialogRef) {
							dialogRef.close();
							if(terminateAction) {
								terminateAction(dialogRef);
							}
						}
					}
				]
			});
		}
	},
	definitions: {
	    federalOperator: {
            options: {
                title: 'Federal Operator',
                placement: 'top',
                content: 'A "Federal Operator" is an entity that meets the definition of “Operator” in this permit and is either any ' +
                'department, agency or instrumentality of the executive, legislative, and judicial branches of the ' +
                'Federal government of the United States, or another entity, such as a private contractor, ' +
                'performing construction activity for any such department, agency, or instrumentality.'
            }
        },
        agriculturalLand: {
            options: {
                title: 'Agricultural Land',
                placement: 'top',
                container: 'body',
                content: 'cropland, grassland, rangeland, pasture, and other agricultural land, on which ' +
                'agricultural and forest-related products or livestock are produced and resource concerns may be ' +
                'addressed.  Agricultural lands include cropped woodland, marshes, incidental areas included in the ' +
                'agricultural operation, and other types of agricultural land used for the production of livestock.'
            }
        },
        emergencyRelatedProject: {
            options: {
                title: 'Emergency-Related Project',
                placement: 'top',
                content: 'A project initiated in response to a public emergency (e.g., mud slides, earthquake, ' +
                'extreme flooding conditions, disruption in essential public services), for which the related work ' +
                'requires immediate authorization to avoid imminent endangerment to human health or the ' +
                'environment, or to reestablish essential public services.'
            }
        },
		actionArea: {
	    	options: {
	    		title: 'Action Area',
				placement: 'top',
				container: 'body',
				content: 'What federally-listed species or federally-designated critical habitat are located in your "action area"? '
			}
		},
		latLong: {
			options: {
				placement: 'top',
				container: 'body',
				content: 'Use Decimal Degrees to four decimals'
			}
		},
		mapWidget: {
			options: {
				placement: 'top',
				container: 'body',
				content: 'Map widget uses WGS 84'
			}
		},
		receivingWater: {
			options: {
				placement: 'top',
				container: 'body',
				content: 'Provide the name of the first water of the U.S. that receives stormwater ' +
				'directly from the point of discharge and/or from the MS4 that the point of discharge discharges to.'
			}
		},
		tierNA: {
			options: {
				placement: 'top',
				container: 'body',
				content: 'You did not specify that your waters were under a tier earlier in the form. ' +
				'If this is incorrect go back and correct the answer.'
			}
		}
	},
	nav: {
		setContext: function(text, contextClass) {
			$('#nav-bar-context').html('<p class="navbar-text">' + text + '</p>');
		}
	},
	noi: {
		view: function(form, view) {
			var type = ko.utils.unwrapObservable(form.type);
			var status = ko.utils.unwrapObservable(form.status);
			var id = ko.utils.unwrapObservable(form.id);
			view = view ? '/' + view : '';
			if(status == oeca.cgp.constants.status.TERMINATED) {
				pager.navigate('#!/not?formId=' + id);
			}
			else if(type == oeca.cgp.constants.LEW_TYPE) {
				pager.navigate('#!/lew?formId=' + id + view);
			}
			else {
				pager.navigate('#!/noi?formId=' + id + view);
			}
		},
	    save: function(form, successCallback) {
            postal.channel('noi').publish('form.' + form.id() + '.presave', form);
            if (form.id()) {
                return $.ajax({
                    url: config.ctx + "/api/form/v1/" + form.id(),
                    contentType: "application/json",
                    type: "post",
                    data: ko.mapping.toJSON(form),
                    success: function (data) {
                        form.update(data);
                        oeca.notifications.showSuccessMessage("Successfully saved your form.");
                        if(successCallback) {
                            successCallback(data);
						}
                    },
                    error: function (res) {
                        console.log("error");
                        console.log(res);
                    }
                });
            }
            else {
                console.log("creating");
                return $.ajax({
                    url: config.ctx + "/api/form/v1/noi",
                    contentType: "application/json",
                    type: "put",
                    data: ko.mapping.toJSON(form, {
                    	ignore: 'attachments.status'
					}),
                    success: function (data) {
                        form.update(data);
						oeca.cgp.notifications.noiCreated(ko.toJS(form.trackingNumber));
						if(successCallback) {
                            successCallback(data);
						}
                    },
                    error: function (res) {
                        console.log("error");
                        console.log(res);
                    }
                });
            }
        },
		refreshAttachments: function(form) {
            $.getJSON(oeca.cgp.ctx + '/api/form/v1/' + form.id() + '/attachment', function(attachments) {
                ko.mapping.fromJS(attachments, {
                    create: function(options) {
                        return new Attachment(options.data);
                    }
                }, form.attachments);
            });
		},
		deleteAttachment: function(form, attachment) {
            return $.ajax(oeca.cgp.ctx + '/api/form/v1/' + form.id() + '/attachment/' + attachment.id(), {
                type: 'delete',
                success: function() {
                    form.attachments.remove(attachment);
                },
                error: function() {
                    oeca.notifications.showErrorMessage("We encountered an issue deleting your attachment.");
                }
            });
		},
        waitListWarning: function(data, event, section) {
	        console.log(data);
	        oeca.cgp.notifications.waitListReset(section, null, function(dialogRef) {
	            data.waitListEdit(true);
            });
	        event.stopPropagation();
        },
		criterionAttachmentWarning: function(data, newCriterion, removeAction) {
			console.log(data);
			oeca.cgp.notifications.criterionAttachment(newCriterion, null, removeAction);
		},
		route: function(form, user) {
            return $.ajax({
                url: config.ctx + "/api/form/v1/" + form.id() + "/route",
                contentType: "application/json",
                type: "post",
                data: user.userId(),
				success: function() {
                    postal.channel('noi').publish('form.' + form.id() + '.update.routed', user);
				}
            });
		},
        assign: function(form, user) {
            return $.ajax({
                url: config.ctx + "/api/form/v1/" + form.formSet.id() + "/assign",
                contentType: "application/json",
                type: "post",
                data: user.userId(),
				success: function() {
                    postal.channel('noi').publish('form.' + form.id() + '.update.reassigned', user);
				}
            });
        },
		sign: function(formId, activityId) {
	    	return $.ajax({
	    		url: config.ctx + '/api/form/v1/' + formId + '/sign',
				type: 'post',
				contentType: 'application/json',
				data: activityId,
				success: function() {
	    			oeca.cgp.notifications.certification();
	    			postal.channel('noi').publish('form.' + formId + '.update.signed');
				}
			});
		},
		signHd: function(formId) {
            return $.ajax({
                url: config.ctx + '/api/form/v1/' + formId + '/submit',
                type: 'post',
                contentType: 'application/json',
                success: function() {
                    oeca.notifications.showSuccessMessage('Successfully Certified form.');
                    postal.channel('noi').publish('form.' + formId + '.update.signed');
                    pager.navigate("#!/home");
                }
            });
		},
        reject: function(formId, reason) {
			return $.ajax({
				url: config.ctx + '/api/form/v1/' + formId + '/reject',
				type: 'post',
				contentType: 'text/plain',
				data: reason
			});
        },
		terminate: function(form, certifierId, terminationDate) {
			form.certifiedDate(terminationDate);
			var formJson = ko.toJSON(ko.mapping.toJS(form));
            return $.ajax({
                url: config.ctx + "/api/form/v1/" + form.id() + "/terminate/" + certifierId,
                contentType: "application/json",
                type: "post",
                data: formJson,
				success: function() {
                	postal.channel('noi').publish('form.' + form.id() + '.update.terminateRequest');
				},
                error: function (res) {
                    console.log("error");
                    console.log(res);
                }
            });
		},
        signTerminate: function(formId) {
            var transactionId = oeca.cromerr.createTransaction(function(data) {
                $.ajax({
                    url: config.ctx + '/api/form/v1/' + formId + '/sign',
                    type: 'post',
                    contentType: 'application/json',
                    data: data.activityId,
                    success: function() {
                        oeca.notifications.showSuccessMessage("Successfully Terminated your form.");
                        postal.channel('noi').publish('form.' + formId + '.update.terminated', formId);
                    }
                });
            });
            oeca.cromerr.startTransaction(transactionId);
            return transactionId;
        },
		inviteUser: function(type, user) {
			return $.ajax({
				url: config.ctx + '/api/form/v1/invite/'+type,
				type: 'post',
				contentType: 'application/json',
				data: ko.mapping.toJSON(user, {ignore: ['verifyEmail']})
			})
		}
	},
	lew: {
        save: function(form) {
        	console.log("publishing something");
            postal.channel('noi').publish('form.' + form.id() + 'presave', form);
            if (form.id()) {
                return $.ajax({
                    url: config.ctx + "/api/form/v1/" + form.id(),
                    contentType: "application/json",
                    type: "post",
                    data: ko.mapping.toJSON(form),
                    success: function (data) {
                        form.update(data);
                        oeca.notifications.showSuccessMessage("Successfully saved your form.");
                    },
                    error: function (res) {
                        console.log("error");
                        console.log(res);
                    }
                });
            }
            else {
                console.log("creating");
                return $.ajax({
                    url: config.ctx + "/api/form/v1/lew",
                    contentType: "application/json",
                    type: "put",
                    data: ko.mapping.toJSON(form),
                    success: function (data) {
                        form.update(data);
						oeca.cgp.notifications.lewCreated(ko.toJS(form.trackingNumber));
                    },
                    error: function (res) {
                        console.log("error");
                        console.log(res);
                    }
                });
            }
        }
	},
	map: {
		geocode: function(address, callback) {
            L.esri.Geocoding.geocodeService().geocode().region(address).run(callback);
		},
		geocodeCityState: function(city, state, callback) {
			L.esri.Geocoding.geocodeService().geocode().city(city).region(state).run(callback);
		},
		center: function(map, address) {
			oeca.cgp.map.geocode(address, function(error, response) {
				if(response.results[0]) {
					map.fitBounds(response.results[0].bounds);
                }
			})
		},
		centerCityState: function(map, city, state) {
			if(state) {
				state = oeca.cgp.map.decodeState(state);
			}
			if(city && state) {
				oeca.cgp.map.geocodeCityState(city, state, function(error, response) {
					if(error) {
						console.log(error);
					}
					else if(response.results[0]) {
						map.fitBounds(response.results[0].bounds);
					}
					else {
						oeca.cgp.map.center(map, state);
					}
				});
			}
			else if (state) {
				oeca.cgp.map.center(map, state);
			}
			else {
				oeca.cgp.map.center(map, "US");
			}
		},
		/*
		Some states don't geocode correctly this will adjust the state name to improve the geocoding.
		 */
		decodeState: function(state) {
			if(state == 'AS') {
				return "American Samoa"
			}
			if(state == 'GU') {
				return "Hagatna, GU";
			}
			if(state == 'JA') {
				return "Johnston Atoll";
			}
			if(state == 'MW') {
				return 'Midway Island';
			}
			if(state == 'MP') {
				return 'Northern Mariana Islands';
			}
			if(state == 'PR') {
				return 'Puerto Rico';
			}
			return state;
		}
	},
	utils: {
		truncate: function(text, length) {
            text = text.toString();//cast numbers
            if(text.length < length) {
                return text;
            }
            var trimmed = text.substr(0, length-1);
            //break at word
			trimmed = trimmed.replace(/\s([^\s]*)$/, '');
            return trimmed;
		},
		truncateSpan: function(text, length) {
			var truncatedText = oeca.cgp.utils.truncate(text, length);
            var span = $('<span>')
			if(truncatedText !== text) {
				span.attr('title', text).append(truncatedText + '...')
			}
			else{
            	span.append(text);
			}
            return $('<div>').append(span).html();
		},
		formatDateTime: function(date) {
			var unwrappedDate = ko.utils.unwrapObservable(date);
            return unwrappedDate ? moment(unwrappedDate).local().format('MM/DD/YYYY h:mm A') : '';
		},
		panelComputed: function(selectedPanel, panelName) {
			return ko.pureComputed({
				read: function() {
					return selectedPanel() == panelName;
				},
				write: function(show) {
					if(show) {
						selectedPanel(panelName);
					}
					else if(selectedPanel() == panelName) {
						selectedPanel(null);
					}
				}
			})
		},
        /**
		 * This is a helper function to help set up values to deal with free text entries in a radio button or checkbox
		 * list.  If you want to provide the user a list of options but also allow them to provide any answer in a free
		 * text like:
		 * some question...
		 * * Yes
		 * * No
		 * * Other
		 * If the user selects other they would get a free text entry say they entered maybe.  In your model you want
		 * to store the user's answer in a field answer.  you would set it up like this:
		 * viewModel  = {
		 * 	self.answer = ko.observable(null)
		 * }
		 * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'yes'>
		 * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'no'>
		 * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'other'>
		 * <input type="text" data-bind="value: answer">
		 *
		 * Once the user enters something into the free text entry it would uncheck other.  You can get around this
		 * issue by using a dynamic checkedValue that the text field enters when the checked value changes knockout will
		 * change the checked property and keep the other option checked.  So you would set it up like:
		 * viewModel  = {
		 * 	self.answer = ko.observable(null)
		 * 	self.otherAnswerCheckedValue = ko.observable('other');
		 * }
         * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'yes'>
         * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: 'no'>
         * <input type="radio" name="answer" data-bind="checked: answer, checkedValue: otherAnswerCheckedValue>
         * <input type="text" data-bind="value: otherAnswerCheckedValue">
		 *
		 * This works fine until you have to load the data back from the server the value of answer would be 'maybe'
		 * but otherAnswerCheckedValue would be reset to 'other' so other will not be checked like you would want.
		 * This function will initialize the value of otherCheckedValue so that when the data backs from the server the
		 * other option is selected.
		 *
		 * This function will loop all your possible values defined in valueList and if it finds a value that is not in
		 * valueList it will set that as the default value for your otherValue object.  If value matches an item in
		 * valueList it will set otherValue to defaultOtherValue.
		 *
		 * If you value object is an array it is assumed you have a checkbox list so the user can select multiple things.
		 * In that case this will loop through all checked values and if it finds one that is not in the list of
		 * available options it will set that as the other checked value.  Note: if for some reason there are multiple
		 * values in your selected list that are not in the valueList it will set otherValue to the last item not found
		 * in valueList.
		 *
         * @param value - the value the radio or checkboxes should be writing to
         * @param otherValue - placeholder to hold checkedValue of the other option
         * @param valueList - Map of all possible values for the radio buttons or checkboxes.  Should not include the
		 * 		other option.
         * @param defaultOtherValue - The default value for other if the other is not selected this value will be used.
         */
        setOtherCheckedValue: function(value, otherValue, valueList, defaultOtherValue) {
			var unwrappedValue = ko.utils.unwrapObservable(value);
			if(unwrappedValue) {
                if(unwrappedValue.push) {
                    //this is an array loop through all the values
                    ko.utils.arrayForEach(unwrappedValue, function(arrayValue) {
                        var matched = false;
                    	for(var v in valueList) {
							if (typeof arrayValue === 'string' || arrayValue instanceof String) {
								if (arrayValue.toLowerCase() == v.toLowerCase()) {
									matched = true;
									break;
								}
							} else {
								if (arrayValue == v) {
									matched = true;
									break;
								}
							}
                        }
                        if(!matched) {
                    		//found a value not in the list must be the value for other.
                    		otherValue(arrayValue);
						}
                    });
                }
                else {
                    for (var v in valueList) {
                        if (unwrappedValue.toLowerCase() == v.toLowerCase()) {
                            //checked value is an actual option.
                            otherValue(defaultOtherValue);
                            return;
                        }
                    }
                    otherValue(unwrappedValue);
                }
			}
			else {
				otherValue(defaultOtherValue);
			}
		},
        /**
		 * Left pads the string s to length.  Will return the string if its length is greater than or equal to the
		 * length passed in.
         * @param s
         * @param length
         * @param padChar
         * @returns {*}
         */
        pad: function(s, length, padChar) {
			var paddedString = '';
            for(var i = 0; i < length; ++i) {
                paddedString += padChar;
            }
            return oeca.cgp.utils.padWithPaddedString;
		},
        /**
		 * Lefts pads string s to length using part of the padded string.  Padded string should be a string with the
		 * same length as length and the characters you want to pad with.   This will slice the padded string and
		 * append as many characters as needed to s.
         * @param s
         * @param length
         * @param paddedString
         * @returns {*}
         */
		padWithPaddedString: function(s, length, paddedString) {
            if(s == null || s == undefined) {
                return paddedString;
            }
            else if(s.length >= length) {
                return s;
            }
            else {
                return paddedString.slice(String(s).length, length) + s
            }
		},
        /**
		 * Parses the prop passed in walking through each level of the object and unwrapping that level to find the
		 * object.  This is a safe way to get the value of a property when you are not sure if one of the fields along
		 * the path actually exists.
		 *
		 * Example:
		 * obj : {
		 * 	prop1: 1,
		 * 	prop2: ko.observable({
		 * 		prop1: ko.observable(1),
		 * 		prop2: ko.observable(null)
		 * 	});
		 * }
		 * Given the above object if you pass in
		 * oeca.cgp.utils.parseProperty(obj, 'prop1') - returns 1
		 * oeca.cgp.utils.parseProperty(obj, 'prop2.prop1') - returns 1,
		 * oeca.cgp.utils.parseProperty(obj, 'prop2.prop2.prop1') - reutrns null (because prop2 is null)
		 *
         * @param object root object to find the property on
         * @param prop a string with a path to the property you want.  Each level should be separated with a '.'
         * @returns {*} null if some propery along the path was null or undefined other wise returns the value of the
		 * property (which could also be null)
         */
        parseProperty: function(object, prop) {
            var props = prop.split('.');
            var target = object;
            for(var i = 0; i < props.length; ++i) {
                target = ko.unwrap(target[props[i]]);
                if(target == null || target == undefined) {
                    return null;
                }
            }
            return target;
        },
		sequence: 1,
		nextSequence: function() {
        	return ++oeca.cgp.utils.sequence;
		},
		dispose: function(property) {
        	try {
        		if(property) {
        			if(property.dispose) {
        				property.dispose();
					}
				}
			}
			catch (e) {
        		if(console.log()) {
        			console.log("caught error while trying to dispose property");
        			console.log(property);
        			console.log(e);
				}
			}
		},
		disposeList: function(list) {
        	try {
        		ko.utils.arrayForEach(ko.utils.unwrapObservable(list), function(item) {
        			oeca.cgp.utils.dispose(item);
				});
			}
			catch (e) {
                if(console.log) {
                    console.log("caught error while trying to dispose list");
                    console.log(list);
                    console.log(e);
                }
			}
		}
	},
	defaults: {
		cromerrSettings: function(){
			return {
				esignButtonId: 'cromerr-widget-init',
				widgetParams: {
					dataflow: 'CGP',
						userId: oeca.cgp.currentUser.username,
						userRoleId: oeca.cgp.currentUser.userRoleId,
						disclaimerText: 'I certify under penalty of law that this document and all attachments were prepared under my direction or supervision in accordance with a system designed to assure that qualified personnel properly gathered and evaluated the information submitted. Based on my inquiry of the person or persons who manage the system, or those persons directly responsible for gathering the information, the information submitted is, to the best of my knowledge and belief, true, accurate, and complete. I have no personal knowledge that the information submitted is other than true, accurate, and complete. I am aware that there are significant penalties for submitting false information, including the possibility of fine and imprisonment for knowing violations. Signing an electronic document on behalf of another person is subject to criminal, civil, administrative, or other lawful action.',
						props: {
						activityDescription: 'CGP Submission'
					}
				},
				success: function (event) {
					var transactionId = $('#cromerr-widget-init').attr('data-cromerr-transaction-id');
					postal.channel('cromerr-widget').publish('success.' + transactionId, event);
				},
				error: function (error) {
					oeca.notifications.showErrorMessage("We have encountered an issue trying to sign.")
				},
				cancel: function() {}
			}
		}
	}
}
