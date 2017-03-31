//components
ko.components.register('dashboard', {
	viewModel: {
		viewModelClass: DashboardController
	},
	template: {
		component: 'dashboard'
	}
});
ko.components.register('noi', {
	viewModel: {
		viewModelClass: NoiFormController
	},
	template: {
		component: 'noi-form'
	}
});
ko.components.register('noi-view', {
	viewModel: {
		viewModelClass: NoiViewFormController
	},
	template: {
		component: 'noi-view'
	}
});
ko.components.register('lew', {
	viewModel: {
		viewModelClass: LewFormController
	},
	template: {
		component: 'lew'
	}
});
ko.components.register('lew-view', {
	viewModel: {
		viewModelClass: LewViewFormController
	},
	template: {
		component: 'lew-view'
	}
});
ko.components.register('contact-view', {
	viewModel: {
		viewModelClass: ContactTemplateController
	},
	template: {
		component: 'contact-view'
	}
});
ko.components.register('address-view', {
	template: {
		component: 'address-view'
	}
});
ko.components.register('location-view', {
	template: {
		component: 'location-view'
	}
});
ko.components.register('screening-questions', {
	viewModel: {
		viewModelClass: ScreeningQuestionsController
	},
	template: {
		component: 'screening-questions'
	}
});
ko.components.register('noi-screening-questions-view', {
    template: {
        component: 'noi-screening-questions-view'
    }
});
ko.components.register('lew-screening-questions', {
	viewModel: {
		viewModelClass: LewScreeningQuestionController
	},
	template: {
		component: 'lew-screening-questions'
	}
});
ko.components.register('lew-screening-questions-view', {
    template: {
        component: 'lew-screening-questions-view'
    }
});
ko.components.register('lew-erosivity-information', {
	viewModel: {
		viewModelClass: LewErosivityInformationController
	},
	template: {
		component: 'lew-erosivity-information'
	}
});
ko.components.register('operator-information', {
	viewModel: {
		viewModelClass: OperatorInformationController
	},
	template: {
		component: 'operator-information'
	}
});
ko.components.register('project-site', {
	viewModel: {
		viewModelClass: ProjectSiteController,
	},
	template: {
		component: 'project-site'
	}
});
ko.components.register('lew-project-site', {
	viewModel: {
		viewModelClass: LewProjectSiteController,
	},
	template: {
		component: 'lew-project-site'
	}
});
ko.components.register('discharge-information', {
	viewModel: {
		viewModelClass: DischargeInformationController
	},
	template: {
		component: 'discharge-information'
	}
});
ko.components.register('chemical-information', {
	viewModel: {
		viewModelClass: ChemicalInformationController
	},
	template: {
		component: 'chemical-information'
	}
});
ko.components.register('swppp', {
	viewModel: {
		viewModelClass: SwpppController
	},
	template: {
		component: 'swppp'
	}
});
ko.components.register('endangered-species-protection', {
	viewModel: {
		viewModelClass: EndangeredSpeciesProtectionController
	},
	template: {
		component: 'endangered-species-protection'
	}
});
ko.components.register('historic-preservation', {
	viewModel: {
		viewModelClass: HistoricPreservationController
	},
	template: {
		component: 'historic-preservation'
	}
});
ko.components.register('certification-information', {
	viewModel: {
		viewModelClass: CertificationInformationController
	},
	template: {
		component: 'certification-information'
	}
});
ko.components.register('certification-information-paper',{
	viewModel: {
		viewModelClass: PaperCertificationInformationController
	},
	template: {
		component: 'certification-information-paper'
	}
});
//templates
ko.components.register('address-info', {
	template: {
		component: 'address'
	}
});
ko.components.register('contact-info', {
	viewModel: {
		viewModelClass: ContactTemplateController
	},
	template: {
		component: 'contact'
	}
});
ko.components.register('file-input', {
	viewModel: {
		viewModelClass: FileUploadController
	},
	template: {
		component: 'file-input'
	}
});
ko.components.register('location', {
	template: {
		component: 'location'
	}
});
ko.components.register('user-search', {
	viewModel: {
		viewModelClass: UserSearchController
	},
	template: {
		component: 'user-search'
	}
});
ko.components.register('not', {
	viewModel: {
		viewModelClass: NotController
	},
	template: {
		component: 'not'
	}
});
//modals
ko.components.register('terminate-modal', {
	viewModel: {
		viewModelClass: TerminateModalController,
		modal: true
	},
	template: {
		modal: 'terminate-modal'
	}
});
ko.components.register('user-search-modal', {
	viewModel: {
		viewModelClass: UserSearchModalController,
		modal: true,
		settings: {

        }
	},
	template: {
		modal: 'user-search-modal'
	}
});
ko.components.register('action-area-modal', {
	viewModel: {
		modal: true
	},
	template: {
		modal: 'action-area-modal'
	}
})
//lookups
registerLookup('states', config.ctx + '/api/lookup/v1/states', BaseLookupModel);
registerLookup('tribes', config.ctx + '/api/lookup/v1/tribes', BaseLookupModel);
registerLookup('tmdls', config.ctx + '/static/json/tmdl.json', BaseLookupModel);
registerLookup('pollutants', config.ctx + '/static/json/pollutants.json', null);
registerLookup('formTypes', config.ctx + '/api/lookup/v1/formType', null);
registerLookup('formStatuses', config.ctx + '/api/lookup/v1/formStatus', null);
registerLookup("counties", config.ctx + '/api/lookup/v1/counties', BaseLookupModel);
registerLookup('yesNoRadio', config.ctx + '/static/json/yesNoRadio.json', null);
registerLookup('constructionTypes', config.ctx + '/static/json/siteConstructionTypes.json', null);
registerLookup('receivingWater', config.ctx + '/static/json/receivingWater.json', null);
registerLookup('biaTribes', config.ctx + '/api/lookup/v1/biaTribes')