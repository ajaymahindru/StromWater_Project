//noinspection JSUnusedLocalSymbols,JSUnusedLocalSymbols
var DashboardController = function(data, params) {
	var self = this;
	self.data = ko.observableArray([]);
	self.showFilter = ko.observable(false);
	self.criteria = new NoiSearchCriteria({
		activeRecord: true
	});
    self.criteria.submittedTo.extend({fromDate: {
        params: self.criteria.submittedFrom,
        message: "Invalid date range.",
        onlyIf: function() {
            return self.criteria.submittedTo() !== null && self.criteria.submittedTo() !== "";
        }
    }});
	self.submittedFromSub = self.criteria.submittedFrom.subscribe(function(value) {
		if (value == "") {
			self.criteria.submittedFrom(null);
		}
	});
	self.submittedToSub = self.criteria.submittedTo.subscribe(function(value) {
		if (value == "") {
			self.criteria.submittedTo(null);
		}
	});
    self.criteria.updatedTo.extend({fromDate: {
        params: self.criteria.updatedFrom,
        message: "Invalid date range.",
        onlyIf: function() {
            return self.criteria.updatedTo() !== null && self.criteria.updatedTo() !== "";
        }
    }});
	self.updatedFromSub = self.criteria.updatedFrom.subscribe(function(value) {
		if (value == "") {
			self.criteria.updatedFrom(null);
		}
	});
	self.updatedToSub = self.criteria.updatedTo.subscribe(function(value) {
		if (value == "") {
			self.criteria.updatedTo(null);
		}
	});
	self.applyFilter = function(showFilterAfter) {
		console.log("loading form list");
		if (showFilterAfter !== true) {
			self.showFilter(false);
		}
		dt.ajax.reload(null,false);
	};

	self.clearFilter = function() {
		self.criteria.reset();
		self.applyFilter(true);
	};

	self.exportToCsvLink = function() {
		return config.ctx + "/api/form/v1/csv?" + self.criteriaString();
	}

	self.exportCsvForm = function(form) {
		window.window.open(config.ctx + "/api/form/v1/csv/" + form.id());
	}

	self.editForm = function(form) {
		if(form.submitted()) {
			oeca.cgp.notifications.changeForm(null, function() {
                oeca.cgp.noi.view(form);
            });
		}
		else {
			oeca.cgp.noi.view(form);
		}
	};
	self.changeForm = function(form) {
        oeca.cgp.notifications.reviseForm(form.typeAcronym(), null, function() {
            $.ajax({
                url: config.ctx + "/api/form/v1/" + form.id() + "/change",
                contentType: "application/json",
                type: "post",
                data: ko.toJSON(form.formSet.id()),
                success: function(form) {
                    oeca.cgp.noi.view(form);
                },
                error: function(res) {
                    console.log("error");
                    console.log(res);
                }
            });
        });
	};
	self.deleteForm = function(form) {
		if (form.phase() == 'New') {
			oeca.cgp.notifications.deleteForm(null, function() {
				$.ajax({
					url: config.ctx + "/api/form/v1/" + form.id(),
					type: "delete",
					success: function() {
						oeca.notifications.showSuccessMessage("Your form has been deleted.");
						postal.channel('noi').publish('form.' + form.id() + '.update.deleted');
					},
					error: function(res) {

					}
				});
			});
		} else if (form.phase() == 'Change' || (form.phase() == 'Terminate' && form.status() == 'Draft')) {
			oeca.cgp.notifications.revertForm(null, function() {
				$.ajax({
					url: config.ctx + "/api/form/v1/" + form.id(),
					type: "delete",
					success: function() {
						oeca.notifications.showSuccessMessage("Your form has been reverted.");
						postal.channel('noi').publish('form.' + form.id() + '.update.deleted');
					},
					error: function(res) {

					}
				});
			});
		}

	};
	self.denyForm = function(form) {
		oeca.cgp.notifications.denyForm(null, function(dialogRef) {
            var reason = dialogRef.$modalBody.find('#reject-reason').val();
			$.ajax({
				url: config.ctx + "/api/form/v1/" + form.id() + '/deny',
				type: 'post',
				contentType: 'text/plain',
				data: reason,
				success: function() {
					oeca.notifications.showSuccessMessage("Form has been rejected.");
                    postal.channel('noi').publish('form.' + form.id() + '.update.rejected');
				},
				error: function(res) {

				}
			})
		});
	};
	self.releaseForm = function(form) {
		$.ajax({
			url: config.ctx + "/api/form/v1/" + form.id() + '/release',
			type: 'post',
			success: function() {
				oeca.notifications.showSuccessMessage("Form has been released.");
                postal.channel('noi').publish('form.' + form.id() + '.update.released');
			},
			error: function(res) {

			}
		});
	};
	self.holdForm = function(form) {
		$.ajax({
			url: config.ctx + "/api/form/v1/" + form.id() + '/hold',
			type: 'post',
			success: function() {
				oeca.notifications.showSuccessMessage("Form has been put on hold.");
                postal.channel('noi').publish('form.' + form.id() + '.update.hold');
			},
			error: function() {

			}
		})
	};
	self.signTerminate = function(form) {
		//TODO this returns a transaction id need to dispose of that transaction when done.
		oeca.cgp.noi.signTerminate(form.id());
	};
	self.viewForm = function(form) {
		oeca.cgp.noi.view(form, 'view');
	};
	self.assignForm = function(form) {
		openModal('user-search-modal', null, {
			data: {
				callback: function(user) {
					oeca.cgp.noi.assign(form, user).success(function(){
						oeca.notifications.showSuccessMessage("Successfully assigned form to " + ko.utils.unwrapObservable(user.userId));
					});
				}
			}
		});
	};
    //subscriptions
    self.postalSub = postal.channel('noi').subscribe('form.*.update.*', function() {
    	self.applyFilter();
	});
    self.postalSubNav = postal.channel('nav').subscribe('home.show', function() {
    	self.applyFilter();
	});

	self.dispose = function() {
		postal.unsubscribe(self.postalSub);
		postal.unsubscribe(self.postalSubNav);
		dt.destroy();
		oeca.cgp.utils.dispose(self.submittedFromSub);
		oeca.cgp.utils.dispose(self.submittedToSub);
		oeca.cgp.utils.dispose(self.updatedFromSub);
		oeca.cgp.utils.dispose(self.updatedToSub);
	};

	self.criteriaString = ko.pureComputed(function() {
		return 'owner=' + convertToString(self.criteria.owner())
			+ '&npdesId=' + convertToString(self.criteria.npdesId())
			+ '&masterGeneralPermit=' + convertToString(self.criteria.masterGeneralPermit())
			+ '&trackingNumber=' + convertToString(self.criteria.trackingNumber())
			+ '&type=' + convertToString(self.criteria.type())
			+ '&status=' + convertToString(self.criteria.status())
			+ '&operatorName=' + convertToString(self.criteria.operatorName())
			+ '&siteName=' + convertToString(self.criteria.siteName())
			+ '&siteRegion=' + convertToString(self.criteria.siteRegion())
			+ '&siteStateCode=' + convertToString(self.criteria.siteStateCode())
			+ '&siteZipCode=' + convertToString(self.criteria.siteZipCode())
			+ '&reviewExpiration=' + convertToIso(self.criteria.reviewExpiration())
			+ '&submittedFrom=' + convertToIso(self.criteria.submittedFrom())
			+ '&submittedTo=' + convertToIso(self.criteria.submittedTo())
			+ '&updatedFrom=' + convertToIso(self.criteria.updatedFrom())
			+ '&updatedTo=' + convertToIso(self.criteria.updatedTo())
			+ (self.criteria.activeRecord() !== null ? '&activeRecord=' + self.criteria.activeRecord() : '')
			+ (self.criteria.operatorFederal() !== null ? '&operatorFederal=' + self.criteria.operatorFederal() : '')
			+ (self.criteria.siteIndianCountry() !== null ? '&siteIndianCountry=' + self.criteria.siteIndianCountry() :'')
			+ '&siteIndianCountryLands=' + convertToString(self.criteria.siteIndianCountryLands())
			+ '&resultLimit=' + 1000; //limit results to 1000 max
	});
	var convertToIso = function(val) {
		return val !== null ? moment(val, "MM-DD-YYYY").toISOString() : '';
	};
	var convertToString = function(val) {
		return val !== null ? encodeURIComponent(val) : "";
	}

	self.exportToExcelLink = function() {
		return config.ctx + "/api/form/v1/export/excel?" + self.criteriaString();
	};
	self.exportToHtmlLink = function() {
		return config.ctx + "/api/form/v1/export/html?" + self.criteriaString();
	};

	var dt = $('#forms').DataTable({
		"searching": false,
		"processing": true,
		"serverSide": true,
		responsive: true,
		lengthMenu: [[10, 25, 50, 100], [10, 25, 50, 100]],
		"ajax": function (data, callback, settings) {
			var myData = {
				config: data,
				criteria: self.criteria
			};
			var criteria = ko.mapping.toJSON(myData);
			$.ajax({
				url: config.ctx + "/api/form/v1/search",
				contentType: "application/json",
				type: "post",
				"data": criteria,
				"dataType": "json",
				"beforeSend": oeca.xhrSettings.setJsonAcceptHeader,
				success: function(result) {
					ko.mapping.fromJS(result.data, {
						'': {
							create: function (options) {
								return new NoiForm(options.data);
							}
						}
					}, self.data);
					result.data = self.data();
					callback(result);
				},
				error: function(res) {
					console.log("error");
				}
			});
		},
		columns: [
			{
				className: 'details-control',
				orderable: false,
				data: null,
				responsivePriority: 1,
				render: $.fn.dataTable.render.ko.template('table-details-control')
			},
			{
				name: 'type',
				orderable: true,
				data: 'typeAcronym',
				responsivePriority: 10
			},
			{
				name: 'formSets.npdesId',
				orderable: true,
				data: 'formSet.npdesId()',
				responsivePriority: 10
			},
			{
				name: 'formSets.masterPermitNumber',
				orderable: true,
				data: 'formSet.masterPermitNumber()',
				responsivePriority: 10
			},
			{
				name: 'trackingNumber',
				orderable: true,
				visible: false,
				data: 'trackingNumber()',
				responsivePriority: 30
			},
			{
				name: 'index.siteStateCode',
				orderable: true,
				data: 'formData.projectSiteInformation.siteStateCode',
				width: '140px',
				responsivePriority: 20,
				className: 'desktop',
				render: $.fn.dataTable.render.ko.computed(function(data) {
					var state = lookups.states.lookupByProp(data, 'stateCode');
					return state ? state.stateName : '';
				})
			},
			{
				className: 'word-wrap',
				name: 'index.operatorName',
				orderable: true,
				data: 'formData.operatorInformation.operatorName()',
				width: '200px',
				responsivePriority: 10
			},
			{
				className: 'word-wrap',
				name: 'index.siteName',
				orderable: true,
				data: 'formData.projectSiteInformation.siteName()',
				width: '200px',
				responsivePriority: 10
			},
			{
				name: 'formSets.owner',
				orderable: true,
				data: 'formSet.owner()',
				responsivePriority: 10
			},
			{
				name: 'status',
				orderable: true,
				data: 'statusDisplay',
				responsivePriority: 10
			},
			{
				name: 'lastUpdatedDate',
				orderable: true,
				responsivePriority: 20,
				className: 'desktop',
				data: 'lastUpdatedDate',
				render: $.fn.dataTable.render.ko.computed(function(data) {
					return oeca.cgp.utils.formatDateTime(data);
				})
			},
			{
				name: 'actions',
				orderable: false,
				width: '190px',
				responsivePriority: 1,
				className: 'action-col',
				data: null,
				render: $.fn.dataTable.render.ko.template('table-actions', {
					deleteAction: self.deleteForm,
					editAction: self.editForm,
					changeAction: self.changeForm,
					signTerminateAction: self.signTerminate,
					holdAction: self.holdForm,
					releaseAction: self.releaseForm,
					denyAction: self.denyForm,
					viewAction: self.viewForm,
					assignAction: self.assignForm,
					exportAction: self.exportCsvForm
				})
			}
		],
		responsive: {
			details: false
		},
		order: [[10, 'desc']],
		dom: '<\'pull-left\'B><\'pull-right\'f><t><\'col-sm-8\'i><\'col-sm-2\'l><\'pull-right\'p>',
		buttons: [
			'colvis',
			{
				text: 'CSV',
				action: function ( e, dt, node, config ) {
					window.window.open(self.exportToCsvLink());
				},
				available: function ( dt, config ) {
					return oeca.cgp.currentUser.roleId == 120440;
				}
			},
			{
				text: 'Excel',
				action: function ( e, dt, node, config ) {
					window.window.open(self.exportToExcelLink());
				}
			},
			{
				text: 'Print',
				action: function ( e, dt, node, config ) {
					window.window.open(self.exportToHtmlLink());
				}
			}
		],
		"language": {
			"emptyTable": "There are no forms to display."
		}

	});

};
var DashboardDetailsController = function(data, params) {
	var self = this;
	ko.mapping.fromJS(data, {}, self);
	self.history = ko.observable([]);
	self.loadHistory = function() {
		var npdesId = self.data.formSet.npdesId();
		if(npdesId) {
            return $.ajax({
                url: config.ctx + "/api/form/v1/list",
                contentType: "application/json",
                type: "post",
                data: ko.mapping.toJSON(new NoiSearchCriteria({activeRecord: false, npdesId: npdesId})),
                success: function (forms) {
                    ko.mapping.fromJS(forms, {
                        '': {
                            create: function (options) {
                                return new NoiForm(options.data);
                            }
                        }
                    }, self.history);
                },
                error: function (res) {
                    console.log("error");
                    console.log(res);
                }
            });
        }
	};
	self.loadHistory();
	self.view = function(form) {
        oeca.cgp.noi.view(form, 'view');
	}
}
var NoiFormController = function(data, params) {
	var self = this;
	//load form
	if(!params.id){
		throw "'id' is a required param for the noi form component";
	}
	self.form = ko.observable(null);
	self.error = ko.observable(null);
	self.waitListEdit = ko.observable(false);
    self.selectedPanel = ko.observable(null);
    var baseFormUrl = config.ctx + '/api/form/v1/';
    self.idSubscription = null;
	self.loadForm = function() {
		var id = ko.utils.unwrapObservable(params.id);
		$.getJSON(baseFormUrl + (id || ''), function(data) {
			var form = new NoiForm(data);
			self.form(form);
		})
		.success(function() {
		})
		.error(function() {
			self.error(true);
		});
    };
    self.loadForm();
	if(params.id.subscribe) {
		self.idSubscription = params.id.subscribe(function() {
			self.form(null);
			self.loadForm();
		})
	}
    self.chemicalInformationSectionValid = ko.pureComputed(function() {
        return self.form() && self.form().formData.chemicalTreatmentInformation.polymersFlocculantsOtherTreatmentChemicals() &&
                self.form().formData.chemicalTreatmentInformation.cationicTreatmentChemicals();
    });
	self.save = function() {
	    oeca.cgp.noi.save(self.form()).success(function(){
			oeca.cgp.notifications.saveAndClose();
		});
	};
	function nextIncompleteSection(currentSection) {
		var foundCurrent = false;
		var firstIncompleteSection = null;
		for(var section in self.sections) {
            if(section == currentSection) {
                foundCurrent = true;
            }
			else if(self.sections[section].errors() && self.sections[section].errors()().length > 0) {
				if(foundCurrent) {
					//found the next incomplete section expand it
                    self.sections[section].toggle(true);
                    return;
                }
                else if(!firstIncompleteSection) {
					//there may not be an incomplete section after the current so save this one so we can wrap.
					firstIncompleteSection = section;
				}
			}
		}
		if(firstIncompleteSection) {
			//wrap back to the first incomplete section
			self.sections[firstIncompleteSection].toggle(true);
			return;
		}
		//all sections are complete form is ready to certify
		self.sections.certification.toggle(true);
	}
	self.sections = {
		screening: {
			show: ko.observable(true),
			errors: ko.observable(null),
			toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'screening'),
			loaded: ko.observable(false),
			completeCallback: function() {
				oeca.cgp.noi.save(self.form()).success(function() {
					self.sections.operator.show(true);
					self.sections.operator.toggle(true);
                    oeca.utils.scrollToElement('.panel:last');
				});
			}
		},
		operator: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'operator'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.noi.save(self.form()).success(function() {
                	if(self.sections.site.show()) {
                		nextIncompleteSection('operator');
                    }
                    else {
                		self.sections.site.show(true);
                		self.sections.site.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
					}
                });
            }
		},
		site: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'site'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.noi.save(self.form()).success(function() {
                    if(self.sections.discharge.show()) {
                        nextIncompleteSection('site');
                    }
                    else {
                        self.sections.discharge.show(true);
                        self.sections.discharge.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
                    }
                });
            }
		},
		discharge: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'discharge'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.noi.save(self.form()).success(function() {
                    if(self.sections.chemical.show()) {
                        nextIncompleteSection('discharge');
                    }
                    else {
                    	if(self.chemicalInformationSectionValid()) {
                            self.sections.chemical.show(true);
                            self.sections.chemical.toggle(true);
                        }
                        else {
                    		self.sections.swppp.show(true);
                    		self.sections.swppp.toggle(true);
                            oeca.utils.scrollToElement('.panel:last');
						}
                    }
                });
            }
		},
		chemical: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'chemical'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.noi.save(self.form()).success(function() {
                    if(self.sections.swppp.show()) {
                        nextIncompleteSection('chemical');
                    }
                    else {
                        self.sections.swppp.show(true);
                        self.sections.swppp.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
                    }
                });
            }
		},
		swppp: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'swppp'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.noi.save(self.form()).success(function() {
                    if(self.sections.endangered.show()) {
                        nextIncompleteSection('swppp');
                    }
                    else {
                        self.sections.endangered.show(true);
                        self.sections.endangered.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
                    }
                });
            }
		},
		endangered: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'endangered'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.noi.save(self.form()).success(function() {
                    if(self.sections.historic.show()) {
                        nextIncompleteSection('endangered');
                    }
                    else {
                        self.sections.historic.show(true);
                        self.sections.historic.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
                    }
                });
            }
		},
		historic: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'historic'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.noi.save(self.form()).success(function() {
                    if(self.sections.certification.show()) {
                        nextIncompleteSection('historic');
                    }
                    else {
                        self.sections.certification.show(true);
                        self.sections.certification.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
                    }
                });
            }
		},
		certification: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'certification'),
			loaded: ko.observable(false),
			completeCallback: function(successCallback) {
				oeca.cgp.noi.save(self.form(), successCallback).success(function() {
					return true;
				});
			}
        }
	}
	//set default panel
	if(ko.utils.unwrapObservable(params.id)) {
		self.childrenLoaded = ko.computed(function() {
			if (self.chemicalInformationSectionValid()) {
				return self.sections.screening.loaded() && self.sections.operator.loaded()
					&& self.sections.site.loaded() && self.sections.discharge.loaded()
					&& self.sections.chemical.loaded() && self.sections.swppp.loaded()
					&& self.sections.endangered.loaded() && self.sections.historic.loaded()
					&& self.sections.certification.loaded();
			} else {
				return self.sections.screening.loaded() && self.sections.operator.loaded()
					&& self.sections.site.loaded() && self.sections.discharge.loaded()
					&& self.sections.swppp.loaded() && self.sections.endangered.loaded()
					&& self.sections.historic.loaded() && self.sections.certification.loaded();
			}
		});
		//all child components loaded
		self.childrenLoadedSubscription = self.childrenLoaded.subscribe(function(value) {
			if(value) {
				nextIncompleteSection('screening');
			}
		});
	}
	else {
		self.selectedPanel('screening');
	}
	self.dispose = function() {
		oeca.cgp.utils.dispose(self.idSubscription);
		oeca.cgp.utils.dispose(self.childrenLoadedSubscription);
		oeca.cgp.utils.dispose(self.childrenLoaded);
	}
};
var NoiViewFormController = function(data, params) {
	var self = this;
    self.form = ko.observable(null);
    var baseFormUrl = config.ctx + '/api/form/v1/';
    self.error = ko.observable(false);
    self.waitListEdit = ko.observable(false);
    self.returnToHome = function() {
        pager.navigate('#!/home');
    };
    self.loadForm = function() {
        var id = ko.utils.unwrapObservable(params.id);
        $.getJSON(baseFormUrl + (id || ''), function(data) {
            var form = new NoiForm(data);
            self.form(form);
        }).error(function() {
            self.error(true);
        });
    };
    self.loadForm();
}
var LewFormController = function(data, params) {
	var self = this;
	self.form = ko.observable(null);
	var baseFormUrl = config.ctx + '/api/form/v1/';
	self.error = ko.observable(false);
    self.waitListEdit = ko.observable(false);
    self.selectedPanel = ko.observable(null);
	self.loadForm = function() {
		var id = ko.utils.unwrapObservable(params.id);
		$.getJSON(baseFormUrl + (id || ''), function(data) {
			var form = new LewForm(data);
			self.form(form);
		}).error(function() {
			self.error(true);
		});
	};
	self.loadForm();
	self.screeningComplete = function() {
		oeca.cgp.lew.save(self.form()).success(function() {
            self.erosivityExpand(true);
            self.showErosivity(true);
		});
	};
	self.erosivityComplete = function() {
        oeca.cgp.lew.save(self.form()).success(function() {
            self.operatorInformationExpand(true);
            self.showOperator(true);
        });
	};
	self.operatorComplete = function() {
        oeca.cgp.lew.save(self.form()).success(function() {
            self.projectSiteExpand(true);
            self.showProject(true);
        });
	};
	self.projectComplete = function() {
        oeca.cgp.lew.save(self.form()).success(function() {
            self.certificationExpand(true);
            self.showCertification(true);
        });
	};
    function nextIncompleteSection(currentSection) {
        var foundCurrent = false;
        var firstIncompleteSection = null;
        for(var section in self.sections) {
            if(section == currentSection) {
                foundCurrent = true;
            }
            else if(self.sections[section].errors() && self.sections[section].errors()().length > 0) {
                if(foundCurrent) {
                    //found the next incomplete section expand it
                    self.sections[section].toggle(true);
                    return;
                }
                else if(!firstIncompleteSection) {
                    //there may not be an incomplete section after the current so save this one so we can wrap.
                    firstIncompleteSection = section;
                }
            }
        }
        if(firstIncompleteSection) {
            //wrap back to the first incomplete section
            self.sections[firstIncompleteSection].toggle(true);
            return;
        }
        //all sections are complete form is ready to certify
        self.sections.certification.toggle(true);
    }
	self.sections = {
		screening: {
			show: ko.observable(true),
			errors: ko.observable(null),
			toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'screening'),
			loaded: ko.observable(false),
			completeCallback: function() {
				oeca.cgp.lew.save(self.form()).success(function() {
					self.sections.erosivity.show(true);
					self.sections.erosivity.toggle(true);
                    oeca.utils.scrollToElement('.panel:last');
				});
			}
		},
		erosivity: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'erosivity'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.lew.save(self.form()).success(function() {
                	if(self.sections.operator.show()) {
                        nextIncompleteSection('erosivity');
                    }
                    else {
                        self.sections.operator.show(true);
                        self.sections.operator.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
					}
                });
            }
		},
		operator: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'operator'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.lew.save(self.form()).success(function() {
                    if(self.sections.project.show()) {
                        nextIncompleteSection('operator');
                    }
                    else {
                        self.sections.project.show(true);
                        self.sections.project.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
                    }
                });
            }
		},
		project: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'project'),
			loaded: ko.observable(false),
            completeCallback: function() {
                oeca.cgp.lew.save(self.form()).success(function() {
                    if(self.sections.certification.show()) {
                        nextIncompleteSection('project');
                    }
                    else {
                        self.sections.certification.show(true);
                        self.sections.certification.toggle(true);
                        oeca.utils.scrollToElement('.panel:last');
                    }
                });
            }
		},
		certification: {
            show: ko.observable(params.id() ? true:false),
            errors: ko.observable(null),
            toggle: oeca.cgp.utils.panelComputed(self.selectedPanel, 'certification'),
			loaded: ko.observable(false),
			completeCallback: function(successCallback) {
				oeca.cgp.noi.save(self.form(), successCallback).success(function() {
					return true;
				});
			}
        }
	};
	self.save = function() {
		oeca.cgp.noi.save(self.form()).success(function(){
			oeca.cgp.notifications.saveAndClose();
		});
	};
    //set default panel
    if(ko.utils.unwrapObservable(params.id)) {
        //TODO all the panels haven't loaded yet need to delay this call.
		self.childrenLoaded = ko.computed(function() {
			return self.sections.screening.loaded() && self.sections.operator.loaded()
				&& self.sections.erosivity.loaded() && self.sections.project.loaded()
				&& self.sections.certification.loaded();
		});
	//all child components loaded
	self.childrenLoadedSubscription = self.childrenLoaded.subscribe(function(value) {
		if(value) {
			nextIncompleteSection('screening');
		}
	});
    }
    else {
        self.selectedPanel('screening');
    }
	self.dispose = function() {
    	oeca.cgp.utils.dispose(self.childrenLoadedSubscription);
    	oeca.cgp.utils.dispose(self.childrenLoaded);
	}
};
var LewViewFormController = function(data, params) {
	var self = this;
    self.form = ko.observable(null);
    var baseFormUrl = config.ctx + '/api/form/v1/';
    self.error = ko.observable(false);
    self.returnToHome = function() {
        pager.navigate('#!/home');
    };
    self.loadForm = function() {
        var id = ko.utils.unwrapObservable(params.id);
        $.getJSON(baseFormUrl + (id || ''), function(data) {
            var form = new LewForm(data);
            self.form(form);
        }).error(function() {
            self.error(true);
        });
    };
    self.loadForm();
}
var NotController = function(data, params) {
	var self = this;
	self.form = ko.observable(null);
	var baseFormUrl = config.ctx + '/api/form/v1/';
	self.error = ko.observable(false);
    self.returnToHome = function() {
        pager.navigate('#!/home');
    };
    self.loadForm = function() {
        var id = ko.utils.unwrapObservable(params.id);
        $.getJSON(baseFormUrl + (id || ''), function(data) {
            var form = new NoiForm(data);
            self.form(form);
        }).error(function() {
            self.error(true);
        });
	}
	self.loadForm();
}
var EligibilityChecker = function(data, form) {
	var self = this;
	if(!data.state || !data.bia || !data.biaCode || !data.federalOperator || !data.tribes) {
		throw "Missing required parameter for eligibility check.  Params: {state: " + data.state + ", bia: "
			+ data.bia + ", biaCode: " + data.biaCode  + ", federalOperator: " + data.federalOperator + ", tribes: " +
			data.tribes + "}";
	}
	ko.mapping.fromJS(data, {
		'ignore': ['tribes']
	}, self);
	self.tribes = data.tribes;
    self.isEligibleCheck = ko.observable(null);
    self.isEligible = ko.computed(function() {
    	if(self.state() && self.bia() && self.tribes().length == 0) {
            return false;
        }
        return self.isEligibleCheck();
    });
	self.checkingEligibility = ko.observable(false);
    self.checkEligibility = function() {
        //validate all the fields are filled out
		if(self.state() && (self.bia() != null || self.biaCode()) && self.federalOperator() != null) {
            if(!self.checkingEligibility()) {
                self.checkingEligibility(true);
                $.ajax({
                    url: config.ctx + '/api/form/v1/eligibility',
                    type: 'post',
                    contentType: 'application/json',
                    data: ko.mapping.toJSON(form),
                    success: function(eligible) {
                        self.isEligibleCheck(eligible);
                    },
                    error: function(error) {
                    }
                }).done(function() {
                    self.checkingEligibility(false);
                });
            }
        }
        else {
            self.isEligibleCheck(null);
        }
    };
	/*
	 * Questions that trigger rechecking eligibility
	 */
    //runs when the form is filled out enough to check for eligibility based on location
    self.runningFirstCheck = ko.observable(false);
    self.firstEligibilityCheckSubscription = self.federalOperator.subscribe(function() {
        if(!self.runningFirstCheck()) {
            self.runningFirstCheck(true);
            var recheckElibility = function() {
                self.isEligibleCheck(null);
                self.checkEligibility();
            };
            recheckElibility();
            //set up subscriptions for if the user changes any of the questions
            self.state.subscribe(recheckElibility);
            self.biaCode.subscribe(function() {
                if(self.biaCode() != 'UNKOWN TRIBE') {
                    recheckElibility();
                }
            });
            self.federalOperator.subscribe(recheckElibility);
            //dispose of the original subscription so we don't create duplicate subscriptions
			oeca.cgp.utils.dispose(self.firstEligibilityCheckSubscription);
        }
    });
	self.dispose = function() {
		oeca.cgp.utils.dispose(self.isEligible);
        oeca.cgp.utils.dispose(self.firstEligibilityCheckSubscription);
	}
}
var ScreeningQuestionsController = function(data, params) {
	var self = this;
	self.expand = params.toggle;
    self.form = params.form;
	/*
	 * Aliases to questions on the form
	 */
	self.state = params.form().formData.projectSiteInformation.siteStateCode;
	self.bia = params.form().formData.projectSiteInformation.siteIndianCountry;
	self.biaCode = params.form().formData.projectSiteInformation.siteIndianCountryLands;
	self.federalOperator = params.form().formData.operatorInformation.operatorFederal;
	self.npdesPermitQuestion = params.form().formData.projectSiteInformation.sitePreviousNpdesPermit;
	self.npdesPermit = params.form().formData.projectSiteInformation.sitePreviousNpdesPermitId.extend({
		maxLength: 9, alphaNumericOnly: true
	});
	self.treatmentChemicals = params.form().formData.chemicalTreatmentInformation.polymersFlocculantsOtherTreatmentChemicals;
	self.cationicTreatmentChemicals = params.form().formData.chemicalTreatmentInformation.cationicTreatmentChemicals;
	self.cationicAuthorized = params.form().formData.chemicalTreatmentInformation.cationicTreatmentChemicalsAuthorization;
	self.swppp = params.form().formData.stormwaterPollutionPreventionPlanInformation.preparationInAdvance;
	self.endangeredSpecies = params.form().formData.endangeredSpeciesProtectionInformation.appendixDCriteriaMet;
	self.historicProperties = params.form().formData.historicPreservation.screeningCompleted;
	self.npdesConfirm = params.form().formData.projectSiteInformation.siteCgpAuthorizationConfirmation;
	self.masterPermit = ko.observable(null);//params.form().formData.;
	//make sure the look has been loaded
	loadLookup('tribes');
    self.tribes = ko.pureComputed(function() {
        return lookups.tribes.filter(self.state(), function(item, value) {
            return item.states.filterByProp(value, 'stateCode').length > 0;
        })
    });
    self.federalOperatorText = ko.pureComputed(function() {
        var state = lookups.states.lookupByProp(self.state(), 'stateCode');
        var fedOperatorText = state ? state.stateFedOperatorText() : '';
        return (fedOperatorText !== null && fedOperatorText !== undefined && fedOperatorText !== '' ?
                fedOperatorText : null);
    });
    self.eligibilityChecker = new EligibilityChecker({
        state: self.state,
        bia: self.bia,
        biaCode: self.biaCode,
        federalOperator: self.federalOperator,
        tribes: self.tribes
    }, params.form);
	self.screeningQuestionsLocked = ko.pureComputed(function() {
		return params.form() && params.form().id();
	});
	self.alerts = oeca.cgp.notifications;
	/*
	 * Controls to to show additional questions
	 */
	self.showBia = ko.pureComputed(function() {
		return self.state();
	});
	self.showTribe = ko.pureComputed(function() {
		return self.bia() && self.tribes().length > 0;
	});
	self.showCoverage = ko.pureComputed(function() {
		return self.bia() == false || self.biaCode();
	});
	self.showStormwater = ko.pureComputed(function() {
		return self.federalOperator() != null;
	});
	self.showPermitNumber = ko.pureComputed(function() {
		return self.npdesPermitQuestion();
	});
	self.permitNumberNext = ko.observable(false);
	self.showChemicals = ko.pureComputed(function() {
		return self.npdesPermitQuestion() == false || self.permitNumberNext();
	});
	self.cationicCommentNext = ko.observable(false);
	self.showSwppp = ko.pureComputed(function() {
		return self.treatmentChemicals() == false 
				|| self.cationicTreatmentChemicals() == false
				|| self.cationicAuthorized() == true;
	});
	self.showAgreement = ko.pureComputed(function(){
		return self.historicProperties() == true;
	});
	/*
	 * when questions are changed mark subquestions null
	 */
	var subscriptions = [];
    //noinspection JSUnusedLocalSymbols
    subscriptions.push(self.state.subscribe(function(newVal) {
        self.biaCode(null);
    }));
	subscriptions.push(self.bia.subscribe(function(newVal) {
		if(!newVal) {
			self.biaCode(null);
		}
	}));
	subscriptions.push(self.showCoverage.subscribe(function(newVal) {
		if(!newVal) {
			self.federalOperator(null);
		}
	}));
	subscriptions.push(self.treatmentChemicals.subscribe(function(newVal) {
		if(!newVal) {
			self.cationicTreatmentChemicals(null);
		}
	}));
	subscriptions.push(self.cationicTreatmentChemicals.subscribe(function(newVal) {
		if(!newVal) {
			self.cationicAuthorized(null);
		}
	}));
	subscriptions.push(self.npdesPermitQuestion.subscribe(function(newVal) {
		if(newVal) {
            self.treatmentChemicals(null);
		}
		else {
			self.npdesPermit(null);
			self.permitNumberNext(false);
		}
	}));
	subscriptions.push(self.swppp.subscribe(function(newVal) {
		if(!newVal) {
			self.endangeredSpecies(null);
		}
	}));
	subscriptions.push(self.endangeredSpecies.subscribe(function(newVal) {
		if(!newVal) {
			self.historicProperties(null);
		}
	}));
	subscriptions.push(self.historicProperties.subscribe(function(newVal) {
		if(!newVal) {
			self.npdesConfirm(null);
		}
	}));

	self.componentLoaded = function() {
		params.onLoad(true);
	};
	/*
	 * CRUD operations
	 */
	self.next = function() {
		oeca.cgp.notifications.screeningQuestionsLocked(null, function(){
			params.completeCallback();
		});
	};
	self.dispose = function() {
		oeca.cgp.utils.disposeList(subscriptions);
        oeca.cgp.utils.dispose(self.eligibilityChecker);
	}
};
var LewScreeningQuestionController = function(data, params) {
	var self = this;
	self.expand = params.toggle;
	self.state = params.form().formData.projectSiteInformation.siteStateCode;
	self.bia = params.form().formData.projectSiteInformation.siteIndianCountry;
	self.biaCode = params.form().formData.projectSiteInformation.siteIndianCountryLands;
	self.federalOperator = params.form().formData.operatorInformation.operatorFederal;
	self.acresQuestion = params.form().formData.lowErosivityWaiver.lewLessThan5Acres;
	self.rfactorQuestion = params.form().formData.lowErosivityWaiver.lewRFactorLessThan5;
    loadLookup('tribes');
    self.tribes = ko.pureComputed(function() {
        return lookups.tribes.filter(self.state(), function(item, value) {
            return item.states.filterByProp(value, 'stateCode').length > 0;
        })
    });
    self.federalOperatorText = ko.pureComputed(function() {
        var state = lookups.states.lookupByProp(self.state(), 'stateCode');
        var fedOperatorText = state ? state.stateFedOperatorText() : '';
        return (fedOperatorText !== null && fedOperatorText !== undefined && fedOperatorText !== '' ?
                fedOperatorText : null);
    });
    self.eligibilityChecker = new EligibilityChecker({
        state: self.state,
        bia: self.bia,
        biaCode: self.biaCode,
        federalOperator: self.federalOperator,
        tribes: self.tribes
    }, params.form);
	self.screeningQuestionsLocked = ko.pureComputed(function() {
		return params.form().id();
	});
	/*
	 * Controls to to show additional questions
	 */
	self.showBia = ko.pureComputed(function() {
		return self.state();
	});
	self.showTribe = ko.pureComputed(function() {
		return self.bia() && self.tribes().length > 0;
	});
	self.tribeNotFound = ko.pureComputed(function() {
		return self.biaCode() === "UNKNOWN TRIBE";
	});
	self.tribeNext = ko.observable(false);
	self.showTribeEntry = ko.pureComputed(function(){
		return self.bia() && self.tribeNotFound();
	});
    self.showCoverage = ko.pureComputed(function() {
        return self.bia() == false || self.biaCode();
    });
	self.screeningFinished = ko.pureComputed(function() {
		return self.acresQuestion() && self.rfactorQuestion();
	});
	self.componentLoaded = function() {
		params.onLoad(true);
	};
	/*
	 * CRUD operations
	 */
	self.next = function() {
		oeca.cgp.notifications.screeningQuestionsLocked(null, function(){
			params.completeCallback();
		});
	}
	self.dispose = function() {
		oeca.cgp.utils.dispose(self.eligibilityChecker);
	}
};
var LewErosivityInformationController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.lowErosivityWaiver);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
	self.recalcRFactor = ko.dirtyFlag(self, false, {
		'ignore': ['lewLessThan5Acres', 'lewRFactorLessThan5', 'lewRFactor', 'lewRFactor',
			'lewRFactorCalculationMethod', 'interimSiteStabilizationMeasures']
	});
	self.lewProjectStart.extend({required: true});
	self.lewProjectEnd.extend({required: true, fromDate: {
		params: self.lewProjectStart,
		message: "Project End Date may not be before Project Start Date."
	}});
	var subscriptions = [];
	subscriptions.push(self.lewRFactor.subscribe(function(newVal) {
	    self.recalcRFactor.reset();
    }));
	self.lewAreaDisturbed.extend({
		required: true,
		greaterThan: 0,
		lessThan: {
            params: 5,
			message: 'Area disturbed must be less than 5.'
        },
		step: {
			params: .25,
			message: 'Area disturbed should be estimated to the nearest quarter acre.'
        }
	});
	self.lewRFactor.extend({required: true, number: true,
		lessThan: {
			params: 5,
			message: "R-Factor must be less than 5."
		},
		greaterThan: 0});
	self.lewRFactorCalculationMethod.extend({required: true});
	self.interimSiteStabilizationMeasures.extend({required: true});
	self.errors = ko.validation.group(self, {deep: true});
	params.errors(self.errors);
	self.componentLoaded = function() {
		params.onLoad(true);
	};
	self.next = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
		}
		else {
			params.completeCallback();
		}
	}
	self.dispose = function() {
		oeca.cgp.utils.disposeList(subscriptions);
	}
};
var OperatorInformationController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.operatorInformation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
	self.change = params.form().change;
	self.isChange = ko.pureComputed(function() {
		return params.form().phase() == 'Change';
	});
	self.preparerSame = ko.observable(null);
	/*
	 * validations
	 */
	self.operatorName.extend({required: true, maxLength: 80});
	self.operatorAddress.extend({required: true, maxLength: 50});
	self.operatorCity.extend({required: true, maxLength: 30});
	self.operatorStateCode.extend({required: true});
	self.operatorZipCode.extend({required: true, zipCodeUS: true});
	self.counties = ko.computed(function() {
		return lookups.counties.filterByProp(self.operatorStateCode(), 'stateCode');
	});
	self.operatorCounty.extend({required: {
		onlyIf: function() {
			return self.counties().length > 0;
		}
	}});
	self.operatorPointOfContact.firstName.extend({required: true, maxLength: 30});
	self.operatorPointOfContact.lastName.extend({required: true, maxLength: 30});
	self.operatorPointOfContact.phone.extend({required: true, phoneUS: true});
	self.operatorPointOfContact.phoneExtension.extend({maxLength: 4, phoneExtensionUS: true});
	self.operatorPointOfContact.email.extend({required: true, maxLength: 100, email: true});
	self.operatorPointOfContact.title.extend({required: true, maxLength: 40});
	self.errors = ko.validation.group(self, {deep: true});
	params.errors(self.errors);
	self.componentLoaded = function() {
			params.onLoad(true);
	};
	self.saveAndContinue = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
		}
		else {
			params.completeCallback();
		}
	}
	self.dispose = function() {
		oeca.cgp.utils.dispose(self.counties);
	}
};
var ProjectSiteController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.projectSiteInformation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
    self.change = params.form().change;
	self.draft = params.form().draft;
    self.waitListEdit = params.waitListEdit;
	/*
	 * validations
	 */
	self.siteName.extend({required: true, maxLength: 80});
	self.siteAddress.extend({required: true, maxLength: 50});
	self.siteCity.extend({required: true, maxLength: 60});
	self.siteStateCode.extend({required: true});
	self.siteZipCode.extend({required: true, zipCodeUS: true});
	self.counties = ko.pureComputed(function() {
		return lookups.counties.filterByProp(self.siteStateCode(), 'stateCode');
	});
	self.siteCounty.extend({required: {
		onlyIf: function() {
			return self.counties().length > 0;
		}
	}});
	self.siteLocation.latitude.extend({required: true});
	self.siteLocation.latitudeDisplay.extend({
		required: true,
		number: true,
		min: {
			params: -90,
			message: "Latitude must be between 90 (90 N) and -90 (90 S)."
		},
		max: {
			params: 90,
			message: "Latitude must be between 90 (90 N) and -90 (90 S)."
		}
	});
	self.siteLocation.longitude.extend({required: true});
	self.siteLocation.longitudeDisplay.extend({
		required: true,
		number: true,
		min: {
			params: -180,
			message: "Longitude must be between 180 (180 E) and -180 (180 W)."
		},
		max: {
			params: 180,
			message: "Longitude must be between 180 (180 E) and -180 (180 W)."
		}
	});
	self.siteLocation.latLongDataSource.extend({required: true});
	self.siteLocation.horizontalReferenceDatum.extend({required: true});
	self.siteProjectStart.extend({required: true, date: true});
	self.siteProjectEnd.extend({required: true, date: true, fromDate: {
		params: self.siteProjectStart,
		message: "Project End Date may not be before Project Start Date."
	}});
	self.siteAreaDisturbed.extend({
		required: true,
		min: 0,
		step: {
			params: .25,
			message: 'Area disturbed should be estimated to the nearest quarter acre.'
        }
	});
	self.siteConstructionTypes.extend({required: true, minLength: 1, maxLength: 100});
	self.siteStructureDemolitionBefore1980.extend({required: true});
	self.siteStructureDemolitionBefore198010kSquareFeet.extend({
		required: {
			onlyIf: function() {
				return self.siteStructureDemolitionBefore1980();
			}
		}
	});
	self.sitePreDevelopmentAgricultural.extend({required: true});
	self.siteEarthDisturbingActivitiesOccurrence.extend({required: true});
	self.siteEmergencyRelated.extend({
		required: {
			onlyIf: function() {
				return self.siteEarthDisturbingActivitiesOccurrence();
			}
		}
	});
	self.siteIndianCulturalProperty.extend({required: true});
	self.siteIndianCulturalPropertyTribe.extend({
		required: {
			onlyIf: function() {
				return self.siteIndianCulturalProperty();
			}
		}
	});
	self.errors = ko.validation.group(self, {deep: true});
    params.errors(self.errors);
	self.componentLoaded = function() {
		params.onLoad(true);
	};
	self.saveAndContinue = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
		}
		else {
			params.completeCallback();
		}
	}
};
var LewProjectSiteController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.projectSiteInformation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
	self.change = params.form().change;
	self.draft = params.form().draft;
	/*
	 * validations
	 */
	self.siteName.extend({required: true, maxLength: 80});
	self.siteAddress.extend({required: true, maxLength: 50});
	self.siteCity.extend({required: true, maxLength: 60});
	self.siteStateCode.extend({required: true});
	self.siteZipCode.extend({required: true, zipCodeUS: true});
	self.counties = ko.pureComputed(function() {
		return lookups.counties.filterByProp(self.siteStateCode(), 'stateCode');
	});
	self.siteCounty.extend({required: {
		onlyIf: function() {
			return self.counties().length > 0;
		}
	}});
	self.siteLocation.latitude.extend({required: true});
	self.siteLocation.latitudeDisplay.extend({
		required: true,
		number: true,
		min: {
			params: -90,
			message: "Latitude must be between 90 (90 N) and -90 (90 S)."
		},
		max: {
			params: 90,
			message: "Latitude must be between 90 (90 N) and -90 (90 S)."
		}
	});
	self.siteLocation.longitude.extend({required: true});
	self.siteLocation.longitudeDisplay.extend({
		required: true,
		number: true,
		min: {
			params: -180,
			message: "Longitude must be between 180 (180 E) and -180 (180 W)."
		},
		max: {
			params: 180,
			message: "Longitude must be between 180 (180 E) and -180 (180 W)."
		}
	});
	self.siteLocation.latLongDataSource.extend({required: true, maxLength: 100});
	self.siteLocation.horizontalReferenceDatum.extend({required: true});
	self.errors = ko.validation.group(self, {deep: true});
    params.errors(self.errors);
	self.componentLoaded = function() {
		params.onLoad(true);
	};
	self.saveAndContinue = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
		}
		else {
			params.completeCallback();
		}
	}
};
var DischargeInformationController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.dischargeInformation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
    self.change = params.form().change;
	self.waitListEdit = params.waitListEdit;
	self.newDischarge = ko.observable(null);
    self.newDischargeVM = ko.observable(null);
	if(self.change()) {
		self.originalWaters = ko.observableArray([]);
		ko.utils.arrayForEach(self.dischargePoints(), function(discharge) {
			var water = discharge.firstWater().receivingWaterName();
			if(!self.originalWaters().contains(water)) {
				self.originalWaters.push(water);
			}
		});
	}
	self.subscriptions = {
		tierQuestion: self.dischargeAllowable.subscribe(function(answer) {
			if(answer == false) {
                ko.utils.arrayForEach(self.dischargePoints(), function (discharge) {
					discharge.tier('NA')
                });
            }
		})
	}
	self.waterWillReset = function(discharge) {
		if(self.change()) {
            var water = oeca.cgp.utils.parseProperty(discharge, 'firstWater.receivingWaterName');
            if (water) {
            	var alreadyExisted = false;
				ko.utils.arrayForEach(self.originalWaters(), function(originalWater) {
					if(originalWater === water) {
						alreadyExisted = true;
					}
				});
				return !alreadyExisted;
            }
        }
		return false;
	};
	/*
		Discharge section was changed 2 questions were later added to the model.  Old data will not have these questions
		answered this code will fill in the questions for the user.
	 */
	ko.utils.arrayForEach(self.dischargePoints(), function(discharge) {
        if(discharge.impaired() == null) {
            discharge.impaired(discharge.firstWater().pollutants.filterByProp(true, 'impaired').length > 0);
        }
        if(discharge.tmdlCompleted() == null) {
            discharge.tmdlCompleted(false);
            for (var i = 0; i < discharge.firstWater().pollutants().length; ++i) {
                var pollutant = discharge.firstWater().pollutants()[i];
                if (pollutant.tmdl().id()) {
                    discharge.tmdlCompleted(true);
                    break;
                }
            }
        }
	});
	//validations
	self.dischargeMunicipalSeparateStormSewerSystem.extend({required: true});
	self.dischargeUSWatersWithin50Feet.extend({required: true});
	self.dischargeAllowable.extend({required: true});
	self.dischargePoints.extend({
		minLength: {
			param: 1,
			message: 'At least one discharge point is required'
		}
	});
	var postalSubs = [];
	self.setDischargeValidations= function(discharge) {
		var fields = [];
        discharge.id.extend({
            required:true,
			number: true,
            validation: [{
                validator: function (val) {
                    var discharges = self.dischargePoints.filterByProp(val, 'id');
                    if(discharges.length > 1) {
                        return false;
                    }
                    else if(discharges.length == 1) {
                        return discharge == discharges[0];
                    }
                    return true;
                },
                message: 'Discharge ID must be unique'
            }]
        });
        fields.push(discharge.id);
        discharge.firstWater().receivingWaterName.extend({required: true});
        fields.push(discharge.firstWater().receivingWaterName);
        discharge.impaired.extend({required: true});
        fields.push(discharge.impaired);
        discharge.tmdlCompleted.extend({required: true});
        fields.push(discharge.tmdlCompleted);
        discharge.tier.extend({required: {
            onlyIf: function() {
                return self.dischargeAllowable();
            }
        }});
        fields.push(discharge.tier);
		function setPollutantErrors(pollutant) {
            pollutant.impaired.extend({required: true});
            pollutant.tmdl().id.extend({
                alphaNumericOnly: true,
                minLength: 1,
                maxLength: 6
            });
            pollutant.tmdl().name.extend({
            	maxLength: 100,
				required: {
            		onlyIf: function() {
            			return pollutant.tmdl().id();
					}
				}
			});
		}
        if(discharge.firstWater().pollutants()) {
            ko.utils.arrayForEach(discharge.firstWater().pollutants(), function(pollutant) {
				setPollutantErrors(pollutant);
            });
		}
		discharge.firstWater().pollutants.extend({
            validation: [
                {
                    validator: function(pollutants) {
                        if(discharge.impaired()) {
                            var hasImpairedPollutant = false;
                            ko.utils.arrayForEach(pollutants, function (pollutant) {
                                if(pollutant.impaired()) {
                                    hasImpairedPollutant = true;
                                }
                            });
                            return hasImpairedPollutant;
                        }
                        else {
                            return true;
                        }
                    },
                    message: 'impaired required'
                },
                {
                    validator: function(pollutants) {
                        if(discharge.tmdlCompleted()) {
                            var hasTmdl = false;
                            ko.utils.arrayForEach(pollutants, function (pollutant) {
                                if(pollutant.tmdl().id() && pollutant.tmdl().name()) {
                                    hasTmdl = true;
                                }
                            });
                            return hasTmdl;
                        }
                        else {
                            return true;
                        }
                    },
                    message: 'tmdl required'
                }
            ]
        });
		fields.push(discharge.firstWater().pollutants);
        postalSubs.push(postal.channel('noi').subscribe('pollutant.*', function(pollutant, envelope) {
        	setPollutantErrors(pollutant);
		}));

		discharge.errors = ko.validation.group(fields, {deep: true, live: true});
	};
	ko.utils.arrayForEach(self.dischargePoints(), function(discharge) {
		self.setDischargeValidations(discharge);
	});
	self.mapCenter = {
		city: params.form().formData.projectSiteInformation.siteCity(),
		state: params.form().formData.projectSiteInformation.siteStateCode()
	};
	self.createDischarge = function() {
		var temp = new DischargePoint();
		temp['isNew'] = ko.observable(true);
		if(self.dischargePoints().length > 0) {
			temp.id(Number(self.dischargePoints()[self.dischargePoints().length - 1].id()) + 1);
		}
		else {
			temp.id(1);
		}
        self.setDischargeValidations(temp);
		//temp.errors = ko.validation.group(validationFields, {deep: true, live: true});
		self.newDischarge(temp);
        self.newDischargeVM(new DischargePointChildRow({
            data: self.newDischarge,
            enableTier: self.dischargeAllowable,
            waterWillReset: self.waterWillReset
        }));
	};
	self.addDischarge = function(callback) {
		var pollutantQuestionErrors = ko.validation.group([self.newDischargeVM().waterImpairedQuestion,
			self.newDischargeVM().tmdlQuestion])
		if(self.newDischarge().errors().length > 0 || pollutantQuestionErrors().length > 0) {
			self.newDischarge().errors.showAllMessages();
			pollutantQuestionErrors.showAllMessages();
			callback(false);
		}
		else {
			self.newDischargeVM().checkTmdl(function(result) {
				if(result) {
                    self.newDischarge().isNew(false);
                    if (!self.dischargeAllowable()) {
                        self.newDischarge().tier('NA');
                    }
                    self.dischargePoints.push(self.newDischarge());
                    self.newDischarge(null);
                    oeca.cgp.utils.dispose(self.newDischargeVM());
                    self.newDischargeVM(null);
                    //for some reason the errors gets messed this somehow fixes it.
                    self.dischargePoints.notifySubscribers();
                    oeca.cgp.noi.save(params.form()).success(function() {
                    	if(self.errors().length > 0) {
                    		oeca.notifications.showWarnMessage("Your changes have been saved but the discharge section still has errors these will need to be corrected before the form can be submitted.");
						}
                        if(callback) {
                    		callback(true);
                        }
					});
                }
                else {
					if(callback) {
						callback(false);
                    }
				}
			});
		}
	};
	self.addDischargeAndDuplicate = function() {
		var duplicateId = self.newDischarge().id();
		self.addDischarge(function(result) {
			if(result) {
				self.createDischarge();
				var duplicate = ko.mapping.toJSON(self.dischargePoints.lookupById(duplicateId));
                ko.mapping.fromJSON(duplicate, {}, self.newDischarge)
                self.newDischarge().id(Number(duplicateId) + 1);
                //TODO this is created twice once during self.createDischarge
                self.newDischargeVM(new DischargePointChildRow({
                    data: self.newDischarge,
                    enableTier: self.dischargeAllowable,
                    waterWillReset: self.waterWillReset
                }));
            }
        });
	};
	self.discardDischarge = function() {
		self.newDischarge(null);
        oeca.cgp.utils.dispose(self.newDischargeVM());
		self.newDischargeVM(null);
	}
	self.editDischarge = function(discharge, row) {
		row.find('.details-control').click();
	}
	self.deleteDischarge = function(discharge) {
		self.dischargePoints.remove(discharge);
	}
	self.errors = ko.validation.group(self, {deep: true, live: true});
    params.errors(self.errors);
	self.componentLoaded = function() {
		params.onLoad(true);
	};
	self.saveAndContinue = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
		}
		else if (self.newDischarge()) {
			var dischargeDisplay = self.newDischarge().id();
			if(self.newDischarge().firstWater().receivingWaterName()) {
				dischargeDisplay += ' - ' + self.newDischarge().firstWater().receivingWaterName();
			}
			oeca.cgp.notifications.errorAlert({
				message: 'You have a new discharge (' + dischargeDisplay + ') that you have not added to your form yet would you like to add this discharge to your form and save your form or discard this discharge and save your form?',
                size: BootstrapDialog.SIZE_WIDE,
				buttons: [
					{
						label: 'Cancel',
						action: function(dialogRef) {
							dialogRef.close();
						}
					},
					{
						label: 'Add Discharge and Save Form',
						cssClass: 'btn-danger-outline',
						action: function(dialogRef) {
							self.addDischarge(function(result) {
                                if(result) {
                                    dialogRef.close();
                                    params.completeCallback();
                                }
                                else {
                                    dialogRef.close();
                                    oeca.notifications.showErrorMessage('Your discharge is not complete please finish filling it out and click "Save and Add This Discharge Point" before saving.');
                                }
							});
						}
					},
					{
						label: 'Discard Discharge and Save Form',
                        cssClass: 'btn-danger-outline',
						action: function(dialogRef) {
							dialogRef.close();
							params.completeCallback();
						}
					}
				]
			})
		}
		else {
			params.completeCallback();
		}
	};
    self.dispose = function() {
    	oeca.cgp.utils.disposeList(self.subscriptions);
    	for(var i = 0; i < postalSubs.length; ++i) {
    		postal.unsubscribe(postalSubs[i]);
        }
    };
};
var DischargePointChildRow = function(data, param) {
	var self = this;
	ko.mapping.fromJS(data, {
		ignore: ['mapCenter']
	}, self);
    self.uniqueId = oeca.cgp.utils.nextSequence();
	self.mapCenter = data.mapCenter;
	self.preselectedPollutants = ko.utils.unwrapObservable(data.data).firstWater().pollutants();
	self.pollutants = ko.utils.unwrapObservable(data.data).firstWater().pollutants;
	self.waterImpairedQuestion = ko.utils.unwrapObservable(data.data).impaired;
	var subscriptions = [];
	var postalSubs = [];
	subscriptions.push(self.waterImpairedQuestion.subscribe(function(answer) {
		if(answer == false) {
			for(var i = self.pollutants().length - 1; i >= 0; --i) {
				self.removeImpairedFromPollutant(self.pollutants()[i]);
            }
            //remove all pre selected impaired pollutants
            self.preSelectedImpairedPollutants.length = 0;
		}
	}));
	self.tmdlQuestion = ko.utils.unwrapObservable(data.data).tmdlCompleted;
    subscriptions.push(self.tmdlQuestion.subscribe(function(answer) {
		if(answer == false) {
            for (var i = self.pollutants().length - 1; i >= 0; --i) {
                self.removeTmdlFromPollutant(self.pollutants()[i]);
            }
            self.tmdls.removeAll();
        }
	}));
	//computed to convert from the UI data model to the actual data model.
    self.enteredImpairedPollutants = ko.pureComputed(function() {
    	return self.pollutants.filterByProp(true, 'impaired');
	});
	self.impairedPollutants = ko.observableArray(self.pollutants.filterByProp(true, 'impaired'));
	self.impairedPollutants.extend({
		required: {
			onlyIf: function() {
				return self.waterImpairedQuestion() == true;
			}
		}
	});
    subscriptions.push(self.impairedPollutants.subscribe(function(newPollutants) {
		ko.utils.arrayForEach(newPollutants, function (pollutant) {
			//self.addOrUpdatePollutant(pollutant, true, null);
			var lookupResult = self.findPollutant(pollutant);
			if(lookupResult && lookupResult.impaired() != true) {
				lookupResult.impaired(true);
			}
			else if (!lookupResult) {
				pollutant.impaired(true);
				self.pollutants.push(pollutant);
			}
		});
		ko.utils.arrayForEach(self.enteredImpairedPollutants(), function(pollutant) {
			var removedPollutant = self.impairedPollutants.lookupByProp(pollutant.pollutantCode(), 'pollutantCode');
			if(!removedPollutant) {
				self.removeImpairedFromPollutant(pollutant);
			}
		})
	}));
	self.preSelectedImpairedPollutants = self.impairedPollutants();
	//adds a subscription to the tmdl pollutant list so that when it changes it makes the correct change to add or
	// remove pollutants from the mian list.  Additionally if the pollutant list for the tmdl is cleared out this will
	// delete the tmdl.
    self.syncTmdlPollutants = function(tmdl) {
        subscriptions.push(tmdl.pollutants.subscribe(function(newPollutants) {
            //check if any new pollutants were added
            ko.utils.arrayForEach(newPollutants, function(pollutant) {
                self.addOrUpdatePollutant(pollutant, null, tmdl);
            });
            //check if any pollutants were removed
            ko.utils.arrayForEach(self.pollutants.filterByProp(tmdl.id(), 'tmdl.id'), function(pollutant) {
                if(!tmdl.pollutants.lookupByProp(pollutant.pollutantCode(), 'pollutantCode')) {
                    self.removeTmdlFromPollutant(pollutant);
                }
            });
            if(newPollutants.length == 0) {
            	self.tmdls.remove(tmdl);
			}
        }));
    };
	self.tmdls = ko.observableArray([]);
	self.addTmdl = function() {
        var newTmdlErrors = ko.validation.group(self.newTmdl);
        if(newTmdlErrors().length > 0){
            newTmdlErrors.showAllMessages();
            return;
        }
        ko.utils.arrayForEach(self.newTmdl().pollutants(), function(pollutant) {
            self.addOrUpdatePollutant(pollutant, null, self.newTmdl());
        });
        self.newTmdl().setOriginalPollutants();
        //remove the unique id validation
        ko.utils.arrayForEach(self.newTmdl().id.rules(), function(rule) {
            if(rule.validator) {
                self.newTmdl().id.rules.remove(rule);
            }
        });
        var tmdl = self.newTmdl();
        self.syncTmdlPollutants(tmdl);
        /// /self.newTmdl().pollutants.subscribe(self.syncTmdlPollutants);
        self.tmdls.push(self.newTmdl());
		self.discardTmdl();
        return true;
    };
    self.discardTmdl = function() {
        //set to null to refresh select2
		self.newTmdl(null);
        var newTmdl = new TmdlController(null, self.pollutants);
        self.setNewTmdlErrors(newTmdl);
        self.newTmdl(newTmdl);
        self.showNewTmdl(false);
	};
    self.findPollutant = function(pollutant) {
        return self.pollutants.lookupByProp(pollutant.pollutantCode(), 'pollutantCode');
    };
    self.addOrUpdatePollutant = function(pollutant, impaired, tmdl) {
        var lookupResult = self.findPollutant(pollutant);
        if(!lookupResult) {
            pollutant.tmdl(tmdl == null ? new Tmdl() : new Tmdl(ko.mapping.toJS(tmdl)));
            pollutant.impaired(impaired == null ? false : impaired);
            self.pollutants.push(pollutant);
        }
        else {
            if(impaired != null) {
                lookupResult.impaired(impaired);
            }
            if(tmdl != null) {
                lookupResult.tmdl(new Tmdl(ko.mapping.toJS(tmdl)));
            }
        }
    };
    //user requesting to delete tmdl
    self.removeTmdl = function(tmdl) {
        tmdl.pollutants.removeAll(tmdl.pollutants());
    };
    self.removeTmdlFromPollutant = function(pollutant) {
        var lookupResult = self.findPollutant(pollutant);
        if(lookupResult){
            if(pollutant.impaired() == true) {
                pollutant.tmdl(new Tmdl());
            }
            else {
                self.pollutants.remove(pollutant);
            }
        }
    };
    self.removeImpairedFromPollutant = function(pollutant) {
        var lookupResult = self.findPollutant(pollutant);
        if(lookupResult) {
            if(pollutant.tmdl() && pollutant.tmdl().id()) {
                pollutant.impaired(false);
            }
            else {
                self.pollutants.remove(pollutant);
            }
        }
    };
	ko.utils.arrayForEach(self.pollutants(), function(pollutant) {
		if(pollutant.tmdl().id()) {
			var tmdlInList = self.tmdls.lookupByProp(pollutant.tmdl().id(), 'id');
			if(!tmdlInList) {
				var tmdl = new TmdlController(pollutant, self.pollutants);
                self.tmdls.push(tmdl);
                self.syncTmdlPollutants(tmdl);
            }
            else {
				pollutant.tmdl().id = tmdlInList.id;
				pollutant.tmdl().name = tmdlInList.name;
				tmdlInList.pollutants.push(pollutant);
			}
		}
	});
	//set all the tmdl original pollutants so they show up in select2 as selected
	ko.utils.arrayForEach(self.tmdls(), function(tmdl) {
		tmdl.setOriginalPollutants();
	});
    self.showNewTmdl = ko.observable(self.tmdls().length == 0);
    self.newTmdl = ko.observable(new TmdlController(null, self.pollutants));
    postalSubs.push(postal.channel('noi').subscribe('form.*.presave', function() {
    	if(self.tmdlQuestion() && self.showNewTmdl()) {
    		self.addTmdl();
		}
	}));
	self.setNewTmdlErrors = function(tmdl) {
		tmdl.id.extend({
            alphaNumericOnly: true,
			required: {
            	onlyIf: function() {
            		if(tmdl.name() || tmdl.pollutants().length > 0) {
            			return true;
					}
					else {
            			return false;
					}
				}
			},
			validation: {
				validator: function(val) {
					return self.tmdls.lookupByProp(val, 'id') == null;
				},
				message: 'This TMDL has already been added'
			}
		});
		tmdl.name.extend({required: {
			onlyIf: function() {
				if(tmdl.id() || tmdl.pollutants().length > 0) {
					return true;
				}
				else {
					return false;
				}
			}
		}});
		tmdl.pollutants.extend({required: {
				onlyIf: function() {
					if(tmdl.id() || tmdl.name()) {
						return true;
					}
					else {
						return false;
					}
				}
			},
			minLength: {
				param: 1,
				message: "At least one pollutant is required"
			}
		});
	};
	self.setNewTmdlErrors(self.newTmdl());
	self.done = function() {
		self.checkTmdl(function() {
            self.closeChildRow();
		});
    };
    self.checkTmdl = function(callback) {
		if(self.showNewTmdl() && (self.newTmdl().id() || self.newTmdl().name() || self.newTmdl().pollutants().length > 0)) {
			oeca.cgp.notifications.errorAlert({
				message: 'You have a new TMDL that you have not added to your waterbody yet, would you like to add ' +
				'this TMDL to your waterbody or discard it?',
				buttons: [
                    {
                        label: 'Cancel',
                        action: function(dialogRef) {
                        	callback(false);
                            dialogRef.close();
                        }
                    },
					{
						label: 'Add TMDL to waterbody',
						cssClass: 'btn-danger-outline',
						action: function(dialogRef) {
							if(self.addTmdl()) {
								callback(true)
								dialogRef.close();
							}
							else {
								callback(false);
								dialogRef.close();
								oeca.notifications.showErrorMessage('Your TMDL is not complete please finish filling it out and click "Add TMDL".');
							}
						}
					},
					{
						label: 'Discard TMDL',
						cssClass: 'btn-danger-outline',
						action: function(dialogRef) {
							self.discardTmdl();
							callback(true);
							dialogRef.close();
						}
					}
				]
			});
		}
		else {
			callback(true);
        }
	}
	self.dispose = function() {
    	oeca.cgp.utils.disposeList(subscriptions);
		for(var i = 0; i < postalSubs.length; ++i){
			postal.unsubscribe(postalSubs[i]);
		}
	}
};
var PollutantController = function(data) {
	var self = this;
	ko.utils.extend(self, new Pollutant(data));
	self.impaired.extend({required: true});
	self.tmdl().id.extend({
        alphaNumericOnly: true,
		minLength: 1,
		maxLength: 6
	});
};
var TmdlController = function(data, allPollutants) {
	var self = this;
	var tmdlData = {
		id: null,
		name: null
	}
	if(data == null) {
		self.pollutants = ko.observableArray([]);
	}
	else {
		tmdlData.id = data.tmdl().id;
		tmdlData.name = data.tmdl().name;
		self.pollutants = ko.observableArray([data]);
	}
	ko.mapping.fromJS(tmdlData, {}, self);
    self.originalPollutants = ko.observableArray([]);
    self.setOriginalPollutants = function () {
        self.originalPollutants(self.pollutants());
    }
    self.processPollutants = function(data, params) {
    	return {
            results: $.map(data, function (pollutant) {
                if (allPollutants.filter(pollutant.pollutantCode, function(selectedPollutant, value) {
                		/*
                		Filter out pollutants that have already been added.  Adding a pollutant to two tmdls will
                		delete the pollutant off one of the tmdls which is an unexpected behavior.  This code filters
                		out all pollutants that have been added to a tmdl that is the current tmdl.
                		 */
                		if(selectedPollutant.tmdl().id() && self.pollutants.filterByProp(value, 'pollutantCode').length == 0) {
                			return selectedPollutant.pollutantCode() == value;
						}
					}).length == 0) {
                    pollutant.text = pollutant.pollutantName;
                    pollutant.id = pollutant.pollutantName;
                    return pollutant;
                }
            })
    	}
    }
}
var ChemicalInformationController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.chemicalTreatmentInformation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
    self.change = params.form().change;
    self.waitListEdit = params.waitListEdit;
	self.pendingAttachment = new FileUploader(params.form(), oeca.cgp.constants.attachmentCategories.CHEMICAL_INFO);
	self.showTreatmentChemicals = ko.pureComputed(function() {
		return params.form() && params.form().formData.chemicalTreatmentInformation.cationicTreatmentChemicalsAuthorization();
	});
	self.attachments = params.form().attachmentsByCategory(oeca.cgp.constants.attachmentCategories.CHEMICAL_INFO);
	self.preSelectedChemicals = ko.observableArray(self.treatmentChemicals());
	//validations
	self.treatmentChemicals.extend({
		required: {
			onlyIf: function() {
				return self.showTreatmentChemicals();
			}
		}
	});
	self.attachments.extend({required: true});
	self.errors = ko.validation.group([self.treatmentChemicals, self.attachments]);
    params.errors(self.errors);

	self.componentLoaded = function() {
		params.onLoad(true);
	};

	self.postalSub = postal.channel('noi').subscribe("attachment." + oeca.cgp.constants.attachmentCategories.CHEMICAL_INFO + ".add", function(data, envelope) {
        oeca.cgp.noi.refreshAttachments(params.form());
	});
	self.removeAttachment = function(attachment) {
        oeca.cgp.noi.deleteAttachment(params.form(), attachment);
	}
	self.saveAndContinue = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
			return;
		}
		params.completeCallback();
	}
	self.dispose = function() {
		postal.unsubscribe(self.postalSub);
		oeca.cgp.utils.dispose(self.pendingAttachment);
		/*
		TODO this frees up dependencies on showTreatmentChemicals so it stops throwing an error when a new form is
		loaded.  self.treatmentChemicals should have been disposed of but something is still holding onto it.  Need to
		figure out where the memory leak is.
		*/
		self.treatmentChemicals.extend({validatable: false});
	}
};
var SwpppController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.stormwaterPollutionPreventionPlanInformation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
	
	//validations
	self.contactInformation.firstName.extend({required: true});
	self.contactInformation.lastName.extend({required: true});
	self.contactInformation.phone.extend({required: true, phoneUS: true});
	self.contactInformation.email.extend({required: true, email: true});
	self.contactInformation.title.extend({required: true});
	self.errors = ko.validation.group(self.contactInformation);
    params.errors(self.errors);
	self.componentLoaded = function() {
		params.onLoad(true);
	};
	self.saveAndContinue = function() {
		if(self.errors().length > 0){
			self.errors.showAllMessages();
		}
		else {
			params.completeCallback();
		}
	}
};
var EndangeredSpeciesProtectionController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.endangeredSpeciesProtectionInformation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
    self.change = params.form().change;
    self.waitListEdit = params.waitListEdit;
	self.alerts = oeca.cgp.notifications;
	//TODO find or add this property to the data model.
	self.tempSummary = ko.observable(null);
	self.tempAttachment = ko.observable(null);
	self.pendingAttachment = new FileUploader(params.form(), oeca.cgp.constants.attachmentCategories.ENDANGERED_SPECIES);
	self.attachments = params.form().attachmentsByCategory(oeca.cgp.constants.attachmentCategories.ENDANGERED_SPECIES);

	//validations
	self.criterion.extend({required:true});
	self.criteriaSelectionSummary.extend({required:true});
	self.otherOperatorNpdesId.extend({
		maxLength: 9,
		required: {
			onlyIf: function() {
				return self.criterion() == 'Criterion_B';
			}
		}
	});
	self.speciesAndHabitatInActionArea.extend({
		required: {
			onlyIf: function() {
				return self.criterion() == "Criterion_C";
			}
		}
	});
	self.distanceFromSite.extend({
		required: {
			onlyIf: function() {
				return self.criterion() == "Criterion_C";
			}
		}
	});
	self.attachments.extend({
		required: {
			onlyIf: function() {
				return self.criterion() == "Criterion_C" || self.criterion() == "Criterion_D" || self.criterion() == "Criterion_E" || self.criterion() == "Criterion_F";
			}
		}
	});

	var criterionSubscription = self.criterion.subscribe(function(value) {
		if (self.attachments().length > 0) {
			if (value == "Criterion_A" || value == "Criterion_B") {
				self.removeAllAttachments();
			} else {
				oeca.cgp.noi.criterionAttachmentWarning(params.form(), value, self.removeAllAttachments);
			}
		}
	});

    self.errors = ko.validation.group([self.criterion, self.criteriaSelectionSummary, self.otherOperatorNpdesId,
        self.speciesAndHabitatInActionArea, self.distanceFromSite, self.attachments]);
    params.errors(self.errors);
	self.componentLoaded = function() {
		params.onLoad(true);
	};

	self.postalSub = postal.channel('noi').subscribe('attachment.' + oeca.cgp.constants.attachmentCategories.ENDANGERED_SPECIES + '.add', function() {
		oeca.cgp.noi.refreshAttachments(params.form());
	});
	self.removeAttachment = function(attachment) {
		oeca.cgp.noi.deleteAttachment(params.form(), attachment);
	}
	self.removeAllAttachments = function() {
		ko.utils.arrayForEach(self.attachments(), function(attachment) {
			oeca.cgp.noi.deleteAttachment(params.form(), attachment);
		});
	}
	self.saveAndContinue = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
			return;
		}
		params.completeCallback();
	}
	self.dispose = function() {
		postal.unsubscribe(self.postalSub);
		oeca.cgp.utils.dispose(self.pendingAttachment);
		oeca.cgp.utils.dispose(criterionSubscription);
	}
};
var HistoricPreservationController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form().formData.historicPreservation);
	self.expand = params.toggle;
	self.showSaveButton = params.nextSectionShown;
    self.change = params.form().change;
    self.waitListEdit = params.waitListEdit;

    //TODO figure out where this goes
    self.appendexEStep4OtherResponse = ko.observable(null);

	//clear out subquestions when they are hidden
	var subscriptions = [];
	subscriptions.push(self.appendexEStep1.subscribe(function(newVal) {
	    if(newVal != true) {
	        self.appendexEStep2(null);
	    }
	}));
    subscriptions.push(self.appendexEStep2.subscribe(function(newVal) {
        if(newVal != false) {
            self.appendexEStep3(null);
        }
    }));
    subscriptions.push(self.appendexEStep3.subscribe(function(newVal) {
        if(newVal != false) {
            self.appendexEStep4(null);
        }
    }));
    subscriptions.push(self.appendexEStep4.subscribe(function(newVal) {
        if(newVal != true) {
            self.appendexEStep4Response(null);
        }
    }));

    //validations
	self.appendexEStep1.extend({required: true});
	self.appendexEStep2.extend({
		required: {
			onlyIf: function() {
				return self.appendexEStep1() == true;
			}
		}
	});
	self.appendexEStep3.extend({
		required: {
			onlyIf: function() {
				return self.appendexEStep2() == false;
			}
		}
	});
	self.appendexEStep4.extend({
		required: {
			onlyIf: function() {
				return self.appendexEStep3() == false;
			}
		}
	});
	self.appendexEStep4Response.extend({
		required: {
			onlyIf: function() {
				return self.appendexEStep4() == true;
			}
		}
	});
	self.appendexEStep4OtherResponse.extend({
		required: {
			onlyIf: function() {
				return self.appendexEStep4Response() == 'other';
			}
		}
	})
	self.errors = ko.validation.group([self.appendexEStep1, self.appendexEStep2, self.appendexEStep3,
		self.appendexEStep4, self.appendexEStep4Response, self.appendexEStep4OtherResponse]);
    params.errors(self.errors);
	self.componentLoaded = function() {
		params.onLoad(true);
	};

	self.saveAndContinue = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
			return;
		}
		params.completeCallback();
	}
	self.dispose = function() {
		oeca.cgp.utils.disposeList(subscriptions);
	}
};
var CertificationInformationController = function(data, params) {
	var self = this;
	ko.utils.extend(self, params.form());
	self.expand = params.toggle;
	self.action = ko.observable(null);
	self.panel = ko.observable('options');
	self.errors = ko.validation.group(params.form(), {deep: true});
	self.showOptions = ko.pureComputed({
		read: function() {
			return self.panel() == 'options';
		},
		write: function(val) {
			if(val) {
				self.panel('options');
			}
		}
	});
	self.showRouteCertifier = ko.pureComputed({
		read: function() {
			return self.panel() == 'route-certifier';
		},
		write: function(val) {
			if(val) {
				self.panel('route-certifier');
			}
		}
	});
	self.showRoutePreparer = ko.pureComputed({
		read: function() {
			return self.panel() == 'route-preparer';
		},
		write: function(val) {
			if(val) {
				self.panel('route-preparer');
			}
		}
	});
	self.showCertify = ko.pureComputed({
		read: function() {
			return self.panel() == 'certify';
		},
		write: function(val) {
			if(val) {
				self.panel('certify');
			}
		}
	});
	self.showReject = ko.pureComputed({
		read: function() {
			return self.panel() == 'reject';
		},
		write: function(val) {
			if(val) {
				self.panel('reject');
			}
		}
	});
	self.componentLoaded = function() {
		params.onLoad(true);
	};
	self.saveAndPickAction = function() {
		self.saveAndContinue(function() {
			self.panel(self.action());
		});
	};
	self.pickActionError = ko.observable(null);
	self.validateCertification = function() {
        if(self.errors().length > 0) {
            self.errors.showAllMessages();
            oeca.notifications.showErrorMessage("Your form is not valid please go back and correct the errors.");
            return false;
        }
        return true;
	}
	self.certify = function(data) {
		oeca.cgp.noi.sign(self.id(), data.activityId);
	};
	self.route = function(user) {
		return oeca.cgp.noi.save(params.form(), null).success(function() {
			oeca.cgp.noi.route(params.form(), user);
		});
	};
	self.confirmCertifier = function(user) {
		if(user.userId()) {
			self.route(user).success(function() {
				oeca.cgp.notifications.routeCertifier();
			});
		}
		else {
			oeca.cgp.notifications.inviteCertifier();
		}
	};
	self.confirmPreparer = function(user) {
        if(user.userId()) {
            self.route(user).success(function () {
                oeca.cgp.notifications.routePreparer();
            });
        }
        else {
            oeca.cgp.notifications.invitePreparer();
        }
	};
	self.rejectionReason = ko.observable(null);
	self.rejectionReason.extend({required:true});
	self.rejectionErrors = ko.validation.group([self.rejectionReason]);
	self.reject = function() {
		if(self.rejectionErrors().length > 0) {
			self.rejectionErrors.showAllMessages();
			return;
		}
		oeca.cgp.noi.reject(self.id(), self.rejectionReason()).success(function() {
			oeca.notifications.showSuccessMessage("Form has been sent back to preparer.");
            pager.navigate("#!/home");
		});
	}
	self.saveAndContinue = function(successCallback) {
		params.completeCallback(successCallback);
	};
};
var PaperCertificationInformationController = function(data, params) {
	var self = this;
    self.expand = params.toggle;
	self.preparer = params.form().formData.operatorInformation.preparer;
	self.certifier = params.form().formData.operatorInformation.certifier;
	self.certifiedDate = params.form().certifiedDate;
	self.certifierSame = ko.observable(false);
	var certifierSameSubscription = self.certifierSame.subscribe(function(val) {
		if(val) {
			self.preparer(self.certifier());
		}
		else{
			self.preparer(new Contact({
				organization: null
			}));
			self.setPreparerValidation();
		}
	});
	if(!self.certifier()) {
		self.certifier(new Contact({
			title: null
		}));
		//hide phone field
		self.certifier().phone = undefined;
	}
	//validations
	self.setPreparerValidation = function() {
        self.preparer().firstName.extend({
        	required: {
        		onlyIf: function() {
        			return !self.certifierSame();
                }
			}
        });
        self.preparer().lastName.extend({
            required: {
                onlyIf: function() {
                    return !self.certifierSame();
                }
            }
        });
        self.preparer().organization.extend({
            required: {
                onlyIf: function() {
                    return !self.certifierSame();
                }
            }
        });
        self.preparer().phone.extend({
            required: {
                onlyIf: function() {
                    return !self.certifierSame();
                }
            }
        });
        self.preparer().email.extend({
            required: {
                onlyIf: function() {
                    return !self.certifierSame();
                }
            }
        });
    }
	self.componentLoaded = function() {
		params.onLoad(true);
	};
    self.setPreparerValidation();
    self.certifier().firstName.extend({required: true});
    self.certifier().lastName.extend({required: true});
    self.certifier().title.extend({required: true});
    self.certifier().email.extend({required: true});
    self.certifiedDate.extend({required: true, futureDate: true});
    self.errors = ko.validation.group(self, {deep: true});
	self.sign = function() {
	    if(self.errors().length > 0) {
	        self.errors.showAllMessages();
	        return;
        }
		oeca.cgp.noi.save(params.form(), function() {
            oeca.cgp.noi.signHd(params.form().id(), null);
		});
	};
	self.dispose = function() {
		oeca.cgp.utils.dispose(certifierSameSubscription);
	}
}
var FileUploadController = function(data, params) {
	var self = this;
	self.id = params.id;
	self.value = ko.observable(params.value);
};
var FileUploader = function(form, category) {
	var self = this;
	self.fileArray = ko.observableArray([]);
	self.subscriptions = [];
	var postalSubs = [];
    self.subscriptions.push(self.fileArray.subscribe(function() {
        if(self.fileArray().length == 0) {
            return;
        }
        var formData = new FormData();
		var fileAdded = false;
        formData.append("meta", ko.toJSON({
        	category: category
		}));
        var files = [];
        for(var i = 0; i < self.fileArray().length; ++i) {
        	var file = self.fileArray()[i];
            var meta = new Attachment(self.fileArray()[i]);
            meta.category(category);
            if(!meta.status) {
            	meta.status = ko.observable('pending');
			}
			else {
            	meta.status('pending');
			}
            //TODO this should prob be called multi file uploader and a single version should be made too.
            self.filesUploading.push(meta);
            if(meta.size() > 3<<20) {
            	meta.status('error');
            	meta.errorMessage("File is larger than 3MB.");
			}
			else {
                formData.append("file[]", self.fileArray()[i]);
				fileAdded = true;
            }
            files.push(meta);
        }
        self.fileArray.removeAll();
        if(fileAdded) {
            $.ajax({
                url: config.ctx + '/api/form/v1/' + form.id() + "/attachment",
                contentType: false,
                processData: false,
                type: 'put',
                data: formData,
                success: function () {
                    oeca.notifications.showSuccessMessage("Successfully uploaded " + files.length + " files");
                    for (var i = 0; i < files.length; ++i) {
                        var file = files[i];
                        if(file.status() != 'error') {
                            file.status('uploaded');
                            postalSubs.push(postal.channel('noi').publish("attachment." + category + ".add", file));
                        }
                    }
                    setTimeout(function () {
                        self.filesUploading.removeAll(files);
						$(".custom-file-input-clear-button").click();
                    }, 5000);
                },
                error: function (res) {
                	var errorMessage = "We encountered an issue saving your attachment.";
                	if(res.responseJSON) {
						errorMessage = res.responseJSON.message;
					}
                    for (var i = 0; i < files.length; ++i) {
                        var file = files[i];
                        file.status('error');
                        file.errorMessage(errorMessage);
                    }
                }
            });
        }
	}));
	self.filesUploading = ko.observableArray([]);
	self.locked = ko.pureComputed(function() {
		return self.filesUploading.filterByProp('pending', 'status').length > 0;
	});
	self.dispose = function() {
		oeca.cgp.utils.disposeList(self.subscriptions);
		for (var i = 0; i < postalSubs.length; ++i) {
			postal.unsubscribe(postalSubs[i]);
		}
	}
}
var ContactTemplateController = function(data, params) {
	var self = this;
    self.contact = new Contact(ko.utils.unwrapObservable(params.contact));
	self.labels = $.extend({
		firstName: 'First Name',
		middleInitial: 'Middle Initial',
		lastName: 'Last Name',
		organization: 'Organization',
		title: 'Title',
		phone: 'Phone',
		phoneExt: 'Ext.',
		email: 'Email',
		ein: 'IRS Employer Identification Number (EIN)'
	}, params.titles);
	self.fullPhone = ko.pureComputed(function() {
		var phone = self.contact.phone();
		if(self.contact.phoneExtension()) {
			phone +='x';
			phone += self.contact.phoneExtension();
		}
		return phone;
	})
};
var UserSearchController = function(data, params) {
	var self = this;
	self.panel = ko.observable("search");
	self.showSearch = ko.pureComputed({
		read: function() {
			return self.panel() == 'search';
		},
		write: function(val) {
			if(val) {
				self.panel('search');
			}
			else if(self.panel() == 'search') {
				self.panel(null);
			}
		}
	});
    self.showResults = ko.pureComputed({
        read: function() {
            return self.panel() == 'results';
        },
        write: function(val) {
            if(val) {
                self.panel('results');
            }
            else if(self.panel() == 'results') {
                self.panel(null);
            }
        }
    });
    self.showInvite = ko.pureComputed({
        read: function() {
            return self.panel() == 'invite';
        },
        write: function(val) {
            if(val) {
                self.panel('invite');
            }
            else if(self.panel() == 'invite') {
                self.panel(null);
            }
        }
    });
    self.showConfirm = ko.pureComputed({
        read: function() {
            return self.panel() == 'confirm';
        },
        write: function(val) {
            if(val) {
                self.panel('confirm');
            }
            else if(self.panel() == 'confirm') {
                self.panel(null);
            }
        }
    });
    self.showInviteConfirm = oeca.cgp.utils.panelComputed(self.panel, 'inviteConfirm');
	self.labels = params.titles;
    self.type = ko.observable(params.type);
	self.searchCriteria = ko.observable(params.defaultCriteria || new Contact({
		organization: null,
		address: null,
        dataflow: 'NETEPACGP',
    	roleId: (self.type() === 'Certifier' ? 120410 : (self.type() === 'Preparer' ? 120420 : ''))
	}));
	self.searchCriteria().userId = ko.observable(null).extend({minLength: 3});
	self.searchCriteria().lastName = ko.observable(null).extend({minLength: 3});
	self.searchCriteria().organization = ko.observable(null).extend({minLength: 3});
	self.searchCriteria().address = ko.observable(null).extend({minLength: 3});
	self.searchCriteria.extend({requiresOneOf: [
				self.searchCriteria().userId,
				self.searchCriteria().firstName,
				self.searchCriteria().middleInitial,
				self.searchCriteria().lastName,
        		self.searchCriteria().organization,
        		self.searchCriteria().address,
				self.searchCriteria().email
			]});
	self.searchCriteria().errors = ko.validation.group(self.searchCriteria, {deep: true});
	self.helpText = params.helpText;
	self.showRoleColumn = params.showRoleColumn,
	self.id = params.id;
	if(!self.id) {
		throw "id is required for user search component";
	}
	self.dtConfig = {
        columns: [
            {
                name: 'action',
                orderable: false,
                render: $.fn.dataTable.render.ko.action('select', self.selectUser, 'btn-primary-outline', '#' + self.id + '-search-results')
            },
            {
                name: 'user.userId',
                'orderable': true,
                'data': 'userId',
                render: $.fn.dataTable.render.ko.observable()
            },
            {
                name: 'certifierName',
                'orderable': true,
                'data': 'nameLast',
                render: $.fn.dataTable.render.ko.observable()
            },
            {
                name: 'org',
                'orderable': true,
                'data': 'organization',
                render: $.fn.dataTable.render.ko.observable()
            },
            {
                name: 'role',
                'orderable': true,
                'data': 'role',
                visible: (self.showRoleColumn == true),
                render: $.fn.dataTable.render.ko.observable()
            },
            {
                name: 'address',
                'orderable': true,
                'data': 'address',
                render: $.fn.dataTable.render.ko.observable()
            },
            {
                name: 'email',
                'orderable': true,
                'data': 'email',
                render: $.fn.dataTable.render.ko.observable()
            }
        ],
		searching: false,
		processing: true,
		serverSide: true,
		responsive: true,
		lengthMenu: [[10, 25, 50, 100], [10, 25, 50, 100]],
		ajax: function(data, callback, settings) {
            var dtCriteria = {
        		config: data,
				criteria: self.searchCriteria
			};
        	var criteriaJson = ko.mapping.toJSON(dtCriteria);
        	$.ajax({
        		url: config.registration.ctx + '/api/registration/v1/user/search',
				contentType: 'application/json',
				type: 'post',
				data: criteriaJson,
				dataType: 'json',
				beforeSend: oeca.xhrSettings.setJsonAcceptHeader,
				success: function(results) {
        			ko.mapping.fromJS(results.results, {
                        '': {
                        	create: function(options) {
                                return new Contact({
                                    userId: options.data.user.userId,
                                    firstName: options.data.user.firstName,
                                    middleInitial: options.data.user.middleInitial,
                                    lastName: options.data.user.lastName,
                                    email: options.data.organization.email,
                                    organization: options.data.organization.organizationName,
                                    role: oeca.cgp.constants.roles[options.data.role.type.code],
                                    address:
                                    options.data.organization.mailingAddress1 +
                                    (options.data.organization.mailingAddress2 !== null ? ' ' + options.data.organization.mailingAddress2 : '') +
                                    ', ' + options.data.organization.city +
                                    ', ' + options.data.organization.state.code +
                                    ' ' + options.data.organization.zip +
                                    ', ' + options.data.organization.country.code
                                });
                            }
                        }
					}, self.searchResults);
        			callback({
        				data: self.searchResults() || [],
        				draw: data.draw,
						recordsTotal: results.totalCount,
						recordsFiltered: results.totalCount
					});
				},
				error: function(res) {
        			console.log("error searching for users");
        			console.log(res);
				}
			})
		}
    }
	self.search = function(page) {
        var userSearchErrors = self.searchCriteria().errors;
        if (userSearchErrors().length > 0) {
            userSearchErrors.showAllMessages();
            return;
        }
        self.searchResults.removeAll();
        self.showResults(true);
	};
	self.selectedUser = ko.observable(null);
	self.searchResults = ko.observableArray([]);
	self.selectUser = function(user) {
		self.selectedUser(user);
		self.showConfirm(true);
	};
	self.selectInviteUser = function(user) {
        if(self.inviteErrors().length > 0) {
            self.inviteErrors.showAllMessages();
            return;
        }
		self.selectedUser(user);
		self.showInviteConfirm(true);
	};
	self.newUser = new Contact({
		userId: null,
		firstName: null,
		middleInitial: null,
		lastName: null,
		title: null,
		organization: null,
		email: null,
		verifyEmail: null
	});
    self.newUser.firstName.extend({required: true});
    self.newUser.lastName.extend({required: true});
    self.newUser.email.extend({required: true});
    self.newUser.verifyEmail.extend({required: true,
		equal: {
    		params: self.newUser.email,
			message: 'Email does not match'
        }
	});
    self.inviteErrors = ko.validation.group(self.newUser);
	self.confirmUser = function() {
		params.callback(self.selectedUser());
	}
	self.inviteUser = function() {
		oeca.cgp.noi.inviteUser(self.type().toLowerCase(), self.selectedUser()).success(function() {
			params.callback(self.selectedUser());
		});
	}
};
var UserSearchModalController = function(data, params) {
	var self = this;
	self.callback = null;
	self.confirmUser = function(user) {
		params.modalControl.closeModal('confirmed', user);
		if(self.callback) {
			self.callback(user);
		}
	}
	self.refresh = function(data) {
		self.callback = data.callback;
	}
}
var TerminateModalController = function(data, params) {
	var self = this;
	ko.mapping.fromJS(data, {}, self);
	self.errors = ko.validation.group(self, {deep: false});
	self.panel = ko.observable('warning');
	self.terminationDate = ko.observable().extend({required:true, futureDate: true});
	self.showWarning = ko.pureComputed({
		read: function() {
			return self.panel() == 'warning';
		},
		write: function(val) {
			if(val) {
				self.panel('warning');
			}
		}
	});
	self.showConfirmation = ko.pureComputed({
		read: function() {
			return self.panel() == 'confirmation';
		},
		write: function(val) {
			if(val) {
				self.panel('confirmation')
			}
		}
	});
	self.showUserSearch = ko.pureComputed({
		read: function() {
			return self.panel() == 'user-search';
		},
		write: function(val) {
			if(val) {
				self.panel('user-search');
			}
		}
	});
	self.terminating = ko.observable(false);
	self.continue = function() {
		self.showConfirmation(true);
	}
	self.cromerrTransactionIds = [];
	self.confirmTerminate = function() {
		if(self.errors().length > 0) {
            self.errors.showAllMessages();
            return;
        }
		self.terminating(true);
        oeca.cgp.noi.terminate(self, null, self.terminationDate())
			.success(function(form) {
				ko.mapping.fromJS(form, {}, self);
				//close popup
				params.modalControl.closeModal('confirm', self);
				oeca.cgp.noi.signHd(self.id()).success(function() {
						oeca.notifications.showSuccessMessage("Successfully Terminated your form.");
					}
				);
				self.terminating(false);
			})
			.error(function() {
				self.terminating(false);
			});
	};
	self.confirmSignTerminate = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
			return;
		}
		self.terminating(true);
		oeca.cgp.noi.terminate(self, null, null)
			.success(function(form) {
				ko.mapping.fromJS(form, {}, self);
				//close popup
				params.modalControl.closeModal('confirm', self);
				self.cromerrTransactionIds.push(oeca.cgp.noi.signTerminate(self.id()));
				self.terminating(false);
			})
			.error(function() {
				self.terminating(false);
			});
	};
	self.submitToCertifier = function() {
		if(self.errors().length > 0) {
			self.errors.showAllMessages();
			return;
		}
		self.showUserSearch(true);
	};
	self.confirmCertifier = function(user) {
		oeca.cgp.noi.terminate(self, user.userId(), null).success(function(form) {
			params.modalControl.closeModal('routed', self);
			oeca.notifications.showSuccessMessage('Successfully sent your termination request to ' + user.userId());
		});
	};
	self.refresh = function(data) {
		ko.mapping.fromJS(data, {}, self);
		self.panel("warning");
	}
	self.dispose = function() {
		for(var i = 0; i < self.cromerrTransactionIds.length; ++i) {
			oeca.cromerr.disposeTransaction(self.cromerrTransactionIds[i]);
		}
	}
}
