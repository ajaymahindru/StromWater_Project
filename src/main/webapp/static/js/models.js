var BaseLookupModel = function(data) {
	var self = this;
	data = $.extend({
		code: null,
		description: null
	}, data);
	ko.mapping.fromJS(data, {}, self);
	self.display = ko.pureComputed(function() {
		try {
			return self.code() + '-' + self.description();
		}
		catch (error) {
			console.log("error computing display for lookup: " + ko.toJSON(self));
			throw error;
		}
	});
}
var NoiSearchCriteria = function(data) {
	var self = this;
	data = $.extend({
		owner: null,
		npdesId: null,
		masterGeneralPermit: null,
		trackingNumber: null,
		type: null,
		status: null,
		operatorName: null,
		siteName: null,
		siteRegion: null,
		siteStateCode: null,
		siteCity: null,
		siteZipCode: null,
		reviewExpiration: null,
		submittedFrom: null,
		submittedTo: null,
		updatedFrom: null,
		updatedTo: null,
		activeRecord: null,
		operatorFederal: null,
		siteIndianCountry: null,
		siteIndianCountryLands: null,
        source: null
	}, data);
	ko.mapping.fromJS(data, {}, self);
	self.reset = function() {
		ko.mapping.fromJS(data, {}, self);
	}
}
var NoiForm = function(data) {
	var self = this;
	self.update = function(data) {
        ko.mapping.fromJS(data, {
            state: {
                create: function(options) {
                    if(options.data) {
                        return ko.observable(new BaseLookupModel(options.data));
                    }
                    else {
                        return ko.observable(null);
                    }
                }
            },
            bia: {
                create: function(options) {
                    if(options.data) {
                        return ko.observable(new BaseLookupModel(options.data));
                    }
                    else {
                        return ko.observable(null);
                    }
                }
            },
            formData: {
                create: function(options) {
                    return new NoiFormData(options.data);
                }
            },
			attachments: {
            	create: function(options) {
            		return new Attachment(options.data);
				}
			}
        }, self);
	}
	self.update(data);
	self.submitted = ko.pureComputed(function() {
	    return self.status() == "Submitted";
    });
	self.active = ko.pureComputed(function() {
		return self.status() == "Active";
	})
	self.draft = ko.pureComputed(function() {
		return self.status() == "Draft";
	});
	self.change = ko.pureComputed(function() {
		return self.phase() == 'Change';
	});
	self.typeDisplay = ko.pureComputed(function() {
        var type = lookups.formTypes.lookupByProp(self.type(), 'code')
		if(type) {
        	return ko.utils.unwrapObservable(type.description);
		}
		return '';
	});
	self.typeAcronym = ko.pureComputed(function() {
        if (self.type() === oeca.cgp.constants.LEW_TYPE) {
            return 'LEW';
        }
        else if (self.type() === oeca.cgp.constants.NOI_TYPE) {
            return 'NOI';
        }
        else {
            return self.typeDisplay();
        }
	});
	self.statusDisplay = ko.pureComputed(function() {
		var status = lookups.formStatuses.lookupByProp(self.status(), 'code')
		if(status) {
			return ko.utils.unwrapObservable(status.description);
		}
		return '';
	});
	self.attachmentsByCategory = function(category) {
		return ko.pureComputed(function() {
			var unwrappedCategory = ko.utils.unwrapObservable(category);
			return self.attachments.filterByProp(unwrappedCategory, 'category');
		});
	}
	self.daysTillExpired = ko.pureComputed(function() {
        //moment(reviewDate).diff(moment(certifiedDate), 'days')
		if(self.reviewExpiration()) {
			return moment(self.reviewExpiration()).diff(moment(), 'days');
		}
		else {
			return null;
		}
	});
	self.tillExpired = ko.pureComputed(function() {
        if(self.reviewExpiration()) {
            return moment(self.reviewExpiration()).from(moment());
        }
        else {
            return null;
        }
	});
	self.holdPeriodPercent = ko.pureComputed(function() {
		var review = moment(self.reviewExpiration()).format('X');
		var certified = moment(self.certifiedDate()).format('X');
		if(review - certified <= 0) {
			//avoid divide by 0 and negative values
			return 100;
		}
		else {
			var result = (moment().format('X') - certified)/(review-certified) * 100;
			return result > 100 ? 100:result;
		}
	});
	self.showOnHold = ko.observable(false);
	self.progressDisplay = ko.pureComputed(function() {
	   var review = moment(self.reviewExpiration()).format('X');
	   var current = moment().format('X');
       if(current >= review) {
		   self.showOnHold(false);
	       return 'Processing';
       }
       else {
		   self.showOnHold(true);
	       return 'Ends ' + self.tillExpired();
       }
    });
}
var NoiFormData = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		operatorInformation: {
			create: function(options) {
				return new OperatorInformation(options.data);
			}
		},
		projectSiteInformation: {
			create: function(options) {
				return new ProjectSite(options.data);
			}
		},
        dischargeInformation: {
            create: function(options) {
                return new DischargeInformation(options.data);
            }
        },
		stormwaterPollutionPreventionPlanInformation: {
			create: function(options) {
				return new Swppp(options.data);
			}
		},
        historicPreservation: {
			create: function(options) {
				return new Historic(options.data);
			}
		}
	}, self);
}
var LewForm = function(data) {
	var self = this;
	self.update = function(data) {
        ko.mapping.fromJS(data, {
            formData: {
                create: function (options) {
                    return new LewFormData(options.data);
                }
            }
        }, self);
    }
    self.update(data);
	self.submitted = ko.pureComputed(function() {
		return self.status() == "Submitted";
	});
	self.active = ko.pureComputed(function() {
		return self.status() == "Active";
	})
	self.draft = ko.pureComputed(function() {
		return self.status() == "Draft";
	});
}
var LewFormData = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		operatorInformation: {
			create: function(options) {
				return new OperatorInformation(options.data);
			}
		},
		projectSiteInformation: {
			create: function(options) {
				return new ProjectSite(options.data);
			}
		},
        lowErosivityWaiver: {
			create: function(options) {
				return new LowErosivityWaiver(options.data);
			}
		}
	}, self);
}
var OperatorInformation = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		operatorPointOfContact: {
			create: function(options) {
				if(!options.data) {
					options.data = {
						title: null
					}
				}
				return new Contact(options.data);
			}
		},
		preparer: {
			create: function(options) {
				if(options.data) {
					return ko.observable(new Contact(options.data));
				}
				else {
					return ko.observable(null);
				}
			}
		},
		certifier: {
			create: function(options) {
                if(options.data) {
                    return ko.observable(new Contact(options.data));
                }
                else {
                    return ko.observable(null);
                }
            }
		}
	}, self);
}
var ProjectSite = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		address: {
			create: function(options) {
				return new Address(options.data);
			}
		},
		siteLocation: {
			create: function(options) {
				return new Location(options.data);
			}
		},
		siteConstructionTypes: {
			create: function(options) {
				return options.data ? options.data : ko.observableArray([]);
			}
		},
        siteAreaDisturbed: {
			create: function(options) {
				return ko.observable(options.data).extend({round: 2});
			}
		}
	}, self);
	//TODO need properties for these in the form.
	self.otherCheckedValue = ko.observable(null);
	oeca.cgp.utils.setOtherCheckedValue(self.siteConstructionTypes, self.otherCheckedValue,
		oeca.cgp.constants.constructionTypes, 'OTHER');
	self.otherCheckedValue.extend({
		required: {
			onlyIf: function() {
				return self.siteConstructionTypes() && self.siteConstructionTypes().contains(self.otherCheckedValue());
			}
		}
	});
}
var LowErosivityWaiver = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		'lewAreaDisturbed': {
			create: function(options) {
				return ko.observable(options.data).extend({round: 2});
			}
		}
	}, self);
    self.lewRFactorCalculationMethodDisplay = ko.pureComputed(function () {
		switch (self.lewRFactorCalculationMethod()) {
            case 'fact-sheet':
                return 'EPA Fact Sheet 3.1';
			case 'usda-handbook':
                return 'USDA Handbook 703';
			case 'online-calculator':
                return 'Online Calculator';
        }
    })
};
var Swppp = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
		contactInformation: {
			create: function(options) {
				var contact = new Contact($.extend({
					title: null
				},options.data));
				//fields not applicable to section
				contact.organization = undefined;
				return contact;
			} 
		}
	}, self);
}
var DischargeInformation = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {
        dischargePoints: {
			create: function(options) {
				return new DischargePoint(options.data);
			}
		}
	}, self);
}
var DischargePoint = function(data) {
	var self = this;
	data = $.extend({
		id: null,
		description: null,
		firstWater: null,
		tier: null,
		impaired: null,
		tmdlCompleted: null
	}, data);
	ko.mapping.fromJS(data, {
		id: {
			create: function(options) {
				return new ko.observable(options.data).extend({pad: 3});
			}
		},
		firstWater: {
			create: function(options) {
				return ko.observable(new ReceivingWater(options.data));
			}
		}
	}, self);
	self.tierDisplay = ko.pureComputed(function() {
		return oeca.cgp.constants.tier[self.tier()];
	});
}
var ReceivingWater = function(data) {
	var self = this;
	data = $.extend({
		receivingWaterId: null,
		receivingWaterName: null,
		pollutants: []
	}, data);
	//ignore property filters this out from the tmdl object and causes issue saving set it to undefined so it is skipped
	data.id = undefined;
	ko.mapping.fromJS(data, {
		pollutants: {
			create: function(options) {
				return new Pollutant(options.data);
			}
		},
		ignore: ['text', 'selected']
	}, self);
};
var Pollutant = function(data) {
	var self = this;
	data = $.extend({
		pollutantCode: null,
		pollutantName: null,
		srsName: null,
		impaired: null,
        tmdl: null
	}, data);
	ko.mapping.fromJS(data, {
        tmdl: {
			create: function(options) {
				return ko.observable(new Tmdl(options.data));
			}
		}
	}, self);
}
var Tmdl = function(data) {
	var self = this;
	data = $.extend({
		id: null,
		name: null
	}, data);
	ko.mapping.fromJS(data, {}, self);
}
var Location = function(data) {
	var self = this;
	data = $.extend({
		latitude: null,
		longitude: null,
		latLongDataSource: null,
		horizontalReferenceDatum: null
	}, data);
	ko.mapping.fromJS(data, {
        latitude: {
			create: function(options) {
				return ko.observable(options.data).extend({round: 4});
			}
		},
		longitude: {
			create: function(options) {
				return ko.observable(options.data).extend({round: 4});
			}
		}
	}, self);
	//if the checked value is anything other than the available options check Other.
    self.otherCheckedValue = ko.observable(null);
    oeca.cgp.utils.setOtherCheckedValue(self.latLongDataSource, self.otherCheckedValue, 
		oeca.cgp.constants.latLongDataSource, '');
	self.latitudeDisplay = ko.pureComputed({
		read: function() {
			if (self.latitude() !== null && self.latitude() !== undefined) {
				return Math.abs(self.latitude());
			}
            return null;
        },
		write: function(val) {
			self.latitude(val);
			if(self.latitudeDir == 'S') {
				self.latitude(val * -1)
			}
			else {
				self.latitude(val);
			}
		}
    });
	self.longitudeDisplay = ko.pureComputed({
		read: function () {
			if (self.longitude() !== null && self.longitude() !== undefined) {
				return Math.abs(self.longitude());
			}
			return null;
		},
		write: function(val) {
			if(self.longitudeDir == 'W') {
				self.longitude(val*-1);
			}
			else {
				self.longitude(val);
			}
		}
    });
	//TODO add these to model or figure out where they are
	self.otherLatLongDataSource = ko.observable(null);
	self.latitudeDir = ko.pureComputed({
		read: function() {
            return self.latitude() < 0 ? 'S' : 'N'
        },
		write: function(dir) {
			if(dir != self.latitudeDir()) {
				self.latitude(self.latitude() * -1)
			}
		}
    });
	self.longitudeDir = ko.pureComputed({
		read: function () {
            return self.longitude() < 0 ? 'W' : 'E';
        },
		write: function(dir) {
			if(dir != self.longitudeDir()) {
				self.longitude(self.longitude() * -1)
			}
		}
    });
	self.getLatLong = ko.pureComputed(function() {
		if(self.latitude() && self.longitude()) {
			return {
				lat: self.latitude(),
				lng: self.longitude()
			};
		}
		else {
			return null;
		}
	});
	self.setLatLong = function(latLong) {
		self.latitude(latLong.lat);
		self.longitude(latLong.lng);
		self.latLongDataSource('Map');
	    self.horizontalReferenceDatum('WGS 84');
	}
	self.display = ko.pureComputed(function() {
		if(self.latitude() !== null && self.latitude() !== undefined && self.longitude() !== null && self.longitude() !== undefined) {
			return Math.abs((Math.round(self.latitude() * 10000) / 10000)) + '°' + self.latitudeDir() + ', ' +
				Math.abs((Math.round(self.longitude() * 10000) / 10000)) + '°' + self.longitudeDir();
		}
		else {
			return '';
		}
	});
};
var Historic = function(data) {
	var self = this;
	ko.mapping.fromJS(data, {}, self);
	self.otherCheckedValue = ko.observable(null);
	oeca.cgp.utils.setOtherCheckedValue(self.appendexEStep4Response, self.otherCheckedValue,
		oeca.cgp.constants.appendexEStep4Responses, '');
}
var Address = function(data) {
	var self = this;
	data = $.extend({
		address1: null,
		address2: null,
		city: null,
		state: null,
		zip: null,
		zip4: null,
		county: null
	}, data);
	ko.mapping.fromJS(data, {
		'county': {
			create: function(options) {
				if(options.data) {
					return ko.observable(new BaseLookupModel(options.data));
				}
				else {
					return ko.observable(null);
				}
			}
		}
	}, self);
}
var Contact = function(data) {
	var self = this;
	data = $.extend({
		userId: null,
		firstName: null,
		middleInitial: null,
		lastName: null,
		phone: null,
		phoneExtension: null,
		email: null
	}, data);
	ko.mapping.fromJS(data, {}, self);
	self.name = ko.pureComputed(function() {
		var firstName = self.firstName() || '';
		var lastName = self.lastName() || '';
		var mi = self.middleInitial() || '';
		if(self.firstName() && self.lastName() && self.middleInitial()) {
			mi += '. ';
		}
		return firstName + ' ' + mi + lastName;
	});
	self.nameLast = ko.pureComputed(function() {
        var firstName = self.firstName() || '';
        var lastName = self.lastName() || '';
        var mi = self.middleInitial() || '';
        if(self.firstName() && self.lastName() && self.middleInitial()) {
            mi += '.';
        }
        return lastName + ', ' + firstName + ' ' + mi;
	})
}
var Attachment = function(data) {
	var self = this;
	data = $.extend({
		name: null,
		createdDate: null,
		category: "Default",
		size: null
	}, data);
	ko.mapping.fromJS(data, {}, self);
	self.errorMessage = ko.observable(null);
	self.formData = function() {
		var formData = new FormData();
		formData.append("file", self.file());
		formData.append("meta", ko.mapping.toJSON(self, {
			ignore: ["createdDate", "file", "lastModified", "lastModifiedDate", "type", "webkitRelativePath", "status"]
		}));
		return formData;
	}
	self.sizeDisplay = ko.pureComputed(function() {
		if(self.size()) {
			if(self.size() > 1<<20) {
				return (self.size()/(1<<20)).toFixed(2) + ' MB';
			}
			else if(self.size() > 1<<10) {
				return (self.size()/(1<<10)).toFixed(2) + ' KB';
			}
			return self.size() + ' B';
		}
	});
}