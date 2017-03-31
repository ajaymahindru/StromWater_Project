function scrollToElement(id) {
    $('html, body').animate({
        scrollTop: $(id).offset().top - 50
    }, 500);
}
function scrollToTop() {
	$("html, body").animate({
        scrollTop: $('.panel-group').offset().top - 50
    }, "slow");
}
//lookup code
var lookups = {};
var LookupViewModel = function (settings) {
    var self = this;
    self.value = settings.lookupArray;
    self.loadDeferred = null;
    self.load = settings.load || function () {
            if (!self.loadDeferred) {
                self.loadDeferred = $.Deferred();
                $.getJSON(settings.url, function (data) {
                    var options = settings.viewModel ? {
                            '': {
                                create: function (options) {
                                    return new settings.viewModel(options.data);
                                }
                            }
                        } : {};
                    ko.mapping.fromJS(data, options, self.value);
                    self.loadDeferred.resolve();
                });
            }
            return self.loadDeferred;
        }
}
function registerLookupWithSettings(name, settings) {
    if (lookups[name]) {
        throw name + " is already a registered lookup";
    }
    if (lookups[name + 'VM']) {
        throw name
        + "VM is already registed this name must be free to register a lookup.  This name holds the control for the lookup.";
    }
    lookups[name] = ko.observableArray([]);
    settings = $.extend({
        lookupArray: lookups[name],
        name: name
    }, settings);
    lookups[name + 'VM'] = new LookupViewModel(settings);
}
function registerLookup(name, url, viewModel) {
    registerLookupWithSettings(name, {
        url: url,
        viewModel: viewModel
    });
}
function registerLookupCustomLoad(name, loadFunc) {
    registerLookupWithSettings(name, {
        load: loadFunc
    });
}
function loadLookup(name) {
    var vm = lookups[name + 'VM'];
    if (vm) {
        return vm.load();
    } else {
        throw name + ' is not a registered lookup';
    }
}

//utility functions
function parseProperty(object, prop) {
    var props = prop.split('.');
    var target = object;
    for (var i = 0; i < props.length; ++i) {
        if (!target) {
            return target;
        }
        target = ko.utils.unwrapObservable(target[props[i]]);
    }
    return target;
}

//knockout array functions
ko.observableArray.fn.lookup = function (value, compareFunc) {
    return ko.utils.arrayFirst(this(), function (item) {
        return compareFunc(item, value);
    });
}

ko.observableArray.fn.lookupByProp = function (value, property) {
    return this.lookup(value, function (item, value) {
        return ko.utils.unwrapObservable(item[property]) == ko.utils
                .unwrapObservable(value);
    });
}

ko.observableArray.fn.lookupById = function (value) {
    return this.lookup(value, function (item, value) {
        return ko.utils.unwrapObservable(item.id) == ko.utils
                .unwrapObservable(value);
    });
}

ko.observableArray.fn.filter = function (value, compareFunc) {
    return ko.utils.arrayFilter(this(), function (item) {
        return compareFunc(item, value);
    });
}

ko.observableArray.fn.filterByProp = function (value, property) {
    return this.filter(value, function (item, value) {
        return ko.utils.unwrapObservable(parseProperty(item, property)) == ko.utils.unwrapObservable(value);
    })
}

//bindings
ko.bindingHandlers.select2 = {
    after: ['options', 'lookup', 'value', 'lookupValue', 'selectedOptions'],
    init: function (element, valueAccessor, allBindings) {
        var options = ko.toJS(valueAccessor()) || {};
        options = $.extend({}, options);
        var subscriptions = [];
        // timeout fixes a problem with select2 not showing the intial value.
        setTimeout(function () {
            $(element).select2(options);
            var subscribeToBinding = function (binding) {
                if (allBindings.has(binding) && allBindings()[binding]) {
                    subscriptions.push(allBindings()[binding].subscribe(function (newVal) {
                        setTimeout(function () {
                            // use another thread so all other subscriptions
                            // finish first
                            $(element).trigger("change.select2");
                        }, 0);
                    }));
                }
            }
            subscribeToBinding('options');
            subscribeToBinding('value');
            subscribeToBinding('lookupValue');
            subscribeToBinding('selectedOptions');
            //Workaround to fix tabbing resetting after using select2 https://github.com/select2/select2/issues/4384
            $(element).on('select2:close', function() {
                $(this).focus();
            });
        }, 0);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            for (var i = 0; i < subscriptions.length; ++i) {
                subscriptions[i].dispose();
            }
            if ($(element).data('select2'))
            {
                $(element).select2('destroy');
            }
            $(element).off();
        });
    },
    update: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var observable = allBindingsAccessor().value || {};
        if (typeof allBindingsAccessor().selectedOptions == "function") {
            observable = allBindingsAccessor().selectedOptions;
        }
        var value = ko.utils.unwrapObservable(observable);
        $(element).trigger("change.select2");
    }
};
/**
 * When using select2's ajax functionality it will only assign the value of the select to a primitive.  Instead of the
 * value binding you may use this binding which will pull the full object from select2 and set it as your value.
 * Usually the value comes back as a generic js object and will not have its property set up as observables if you want
 * the properties to also be observable you should provide a model object.
 *
 * The value you provide must be an observable.  You may provide the value either as the value of the binding or in
 * object as the value property
 *
 * Example: as value
 * <select data-bind="select2:{}, select2AjaxValue: myObservable"></select>
 *
 * As an object
 * <select data-bind="select2:{},
 *                    select2AjaxValue: {
 *                        value: myObservable,
 *                        model: myModel
 *                    }"></select>
 *
 * Multi Select Support
 * If you select is set to multiple your observable must be an observable array.  You will also need to provide a
 * compare function.  When the user removes a value from select2 the binding will loop through the selected options and
 * call your compare function passing in the value to be removed as the first arg and the current array item as the
 * second arg.  If you return true this binding will remove that value from the selected options.  Note: This will only
 * remove from the selected options if you return false always in your compare function the value will be removed from
 * select2 but not your selected options.  Alternatively if you always true all your values in your selected options
 * will be removed but select2 will still display them as selected.  Since user's can only remove items one at time
 * this will stop removing items after the first result returned is true.
 *
 * Example:
 * <select data-bind="select2:{},
 *                    select2AjaxValue: {
 *                        value: myObservableArray,
 *                        model: myModel,
 *                        compare: function(toRemove, item) {
                                return toRemove.id == item.code();
                            }
 *                    }" multiple></select>
 *
 * Make sure you do not use the value binding with this binding as the value binding will overwrite this one with the
 * wrong value.  It will end up being a primitive instead of the object.  If you only need a primitive then don't bother
 * with this binding and use the normal value binding.
 *
 * Note: when using this binding if you reload the page this value will be set to your observable but not in the select2
 * list.  Since your value is a complex object there is no way to set it as selected in select2.  So to display the
 * value as selected in select2 you should edit the placeholder to show your selected value.
 * For example:
 * <select data-bind="select2:{
 *                      placeholder: ko.pureComputed(function() {
 *                          if(myObservable() && myObservable().name()) {
 *                              return myObservable().name();
 *                          }
 *                          return 'Select a value';
 *                      })
 *                  }, select2AjaxValue: myObservable"></select>
 *
 */
ko.bindingHandlers.select2AjaxValue = {
    init: function (element, valueAccessor, allBindings, viewModel,
                    bindingContext) {
        var bindingValue = valueAccessor();
        var value = bindingValue.value || bindingValue;
        var model = bindingValue.model;
        var compare = bindingValue.compare;
        var selectedPlaceHolder = bindingValue.selectedPlaceHolder;
        var multi = $(element).prop('multiple');
        if (!ko.isWriteableObservable(value)) {
            console.log(value);
            console.log(element);
            throw "select2AjaxValue value option must be a writeable observable";
        }
        if(multi && !value.push) {
            throw "Select is a multi select koValue must be an observable array";
        }
        if(multi && (!compare || typeof compare != "function")) {
            throw "Select is a multi select you must provide a compare function otherwise the select will not be able to remove values."
        }
        $(element).on('select2:select', function (evt) {
            var selectedValue = model ? new model(evt.params.data) : evt.params.data;
            if(multi) {
                value.push(selectedValue);
            }
            else {
                value(selectedValue);
            }
            return true;
        });
        $(element).on('select2:unselect', function(evt) {
            if(multi) {
                ko.utils.arrayForEach(value(), function(item) {
                    if(compare(evt.params.data, item)) {
                        value.remove(item);
                   }
                });
            }
            else {
                value(null);
            }
            return true;
        });
        //make this binding validatable.  We can't use the normal function because the observable value is wrapped in
        // an object.
        return ko.bindingHandlers['validationCore'].init(element, function() {
                return value;
            }, allBindings, viewModel, bindingContext);
    }
}
/*
 Binding from https://github.com/select2/select2/wiki/Knockout.js-Integration
 */
/*ko.bindingHandlers.select2 = {
 init: function(el, valueAccessor, allBindingsAccessor, viewModel) {
 ko.utils.domNodeDisposal.addDisposeCallback(el, function() {
 $(el).select2('destroy');
 });

 var allBindings = allBindingsAccessor(),
 select2 = ko.utils.unwrapObservable(allBindings.select2);

 $(el).select2(select2);
 },
 update: function (el, valueAccessor, allBindingsAccessor, viewModel) {
 var allBindings = allBindingsAccessor();

 if ("value" in allBindings) {
 if ((allBindings.select2.multiple || el.multiple) && allBindings.value().constructor != Array) {
 $(el).val(allBindings.value().split(',')).trigger('change');
 }
 else {
 $(el).val(allBindings.value()).trigger('change');
 }
 } else if ("selectedOptions" in allBindings) {
 var converted = [];
 var textAccessor = function(value) { return value; };
 if ("optionsText" in allBindings) {
 textAccessor = function(value) {
 var valueAccessor = function (item) { return item; }
 if ("optionsValue" in allBindings) {
 valueAccessor = function (item) { return item[allBindings.optionsValue]; }
 }
 var items = $.grep(allBindings.options(), function (e) { return valueAccessor(e) == value});
 if (items.length == 0 || items.length > 1) {
 return "UNKNOWN";
 }
 return items[0][allBindings.optionsText];
 }
 }
 $.each(allBindings.selectedOptions(), function (key, value) {
 converted.push({id: value, text: textAccessor(value)});
 });
 $(el).select2("data", converted);
 }
 $(el).trigger("change");
 }
 };*/
function extractOptions(value) {
    var options = value.options || value;
    if (typeof options === 'string') {
        if (lookups[options].length == 0) {
            loadLookup(options);
        }
        options = lookups[options];
    }
    var optionsComputed;
    if (value.filter) {
        if (!(value.filter.value && value.filter.by)) {
            throw "The value and by parameters are required to use the filter.  value: " + value.filter.prop + ", by: " + value.filter.value;
        }
        if (typeof value.filter.value == "function" && !ko.isObservable(value.filter.value)) {
            optionsComputed = ko.pureComputed(function () {
                return options.filter(value.filter.by, value.filter.value);
            });
        }
        else {
            optionsComputed = ko.pureComputed(function () {
                return options.filterByProp(value.filter.by, value.filter.value);
            });
        }
    }
    return optionsComputed || options;
}
/**
 * This binding is a wrapper for the options binding. If you provide a string
 * this will look for the options in the global lookup variable. Otherwise this
 * works exactly like the options binding.
 */
ko.bindingHandlers.lookup = {
    init: function (element, valueAccessor, allBindings, viewModel,
                    bindingContext) {
        var value = valueAccessor();
        var options = extractOptions(value);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            if (options.dispose) {
                options.dispose();
            }
        });
        return ko.bindingHandlers.options.init(element, function () {
            return options
        }, allBindings, viewModel, bindingContext);
    },
    update: function (element, valueAccessor, allBindings, viewModel,
                      bindingContext) {
        var value = valueAccessor();
        var options = extractOptions(value);
        var result = ko.bindingHandlers.options.update(element, function () {
            return options
        }, allBindings, viewModel, bindingContext);
        $(element).trigger('change.select2');
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            if (options.dispose) {
                options.dispose();
            }
        });
        return result;
    }
}
function retrieveOptionsFromBindings(allBindings) {
    if (allBindings().options) {
        return allBindings().options;
    }
    else {
        return extractOptions(allBindings().lookup);
    }
}
ko.bindingHandlers.lookupValue = {
    init: function (element, valueAccessor, allBindings, viewModel,
                    bindingContext) {
        var value = valueAccessor();
        var unwrappedValue = ko.utils.unwrapObservable(value);
        //stuff to clean up
        var toDispose = [];
        // We will need a value binding on the element so see if there is one if
        // not create it.
        var selectValue;
        // make sure we won't get any errors before trying to initialize it so
        // we can provide a better message to the user.
        if (allBindings.has('optionsValue')) {
            // TODO initialize this
            selectValue = ko.observable();
        } else {// friendly error the user so they can fix the problem
            throw "When using the lookupValue binding you must provide the 'optionsValue' binding";
        }
        // get the lookup array for this select from the options binding and set
        // up a function so we can find values in the lookup.
        var selectArray = retrieveOptionsFromBindings(allBindings);
        var comparisionFunc = function (item, value) {
            return ko.utils.unwrapObservable(item[allBindings().optionsValue]) == ko.utils
                    .unwrapObservable(value);
        };
        // see if we have a property already if so we will need to update the
        // value attribute so the select list gets updated correctly.
        if (unwrappedValue
            && unwrappedValue[allBindings().optionsValue]
            && unwrappedValue[allBindings().optionsValue]()) {
            selectValue(unwrappedValue[allBindings().optionsValue]());
        }
        // the select value may also be defaulted if so update the lookupValue
        else if (ko.utils.unwrapObservable(selectValue)) {
            var val = ko.utils.arrayFirst(selectArray(), function (item) {
                return comparisionFunc(item, ko.utils
                    .unwrapObservable(selectValue));
            });
            value(val);
        }
        // set up the subscribe event so we can keep the main object updated
        // when the select changes
        var selectValSubscription = selectValue.subscribe(function (newVal) {
            if (newVal) {
                /*
                 * TODO we should only have one computed observable per a
                 * filtered array. For some reason storing this in select array
                 * does not trigger notifications on change even though the ui
                 * updates correctly when the array filter changes the select
                 * stops working. The select value updates properly but then
                 * this bit of code runs and it is searching for the id of the
                 * selected option in the select array which is not updated
                 * correctly. Need to figure out why it won't update properly.
                 * Maybe we should use the raw options not the filtered array?
                 * Note: this may be creating multiple filtered arrays and may
                 * have memory leaks
                 */
                var val = ko.utils.arrayFirst(retrieveOptionsFromBindings(allBindings)(), function (item) {
                    return comparisionFunc(item, ko.utils
                        .unwrapObservable(newVal));
                });
                value(val);
            }
        });
        toDispose.push(selectValSubscription);
        // if the lookupValue property changes we need to copy the property to
        // the select value so the drop down updates
        if (!(value)) {
            throw "The value your provided to the lookupValue binding is "
            + value
            + ".  This must be defined for the lookupValue to work.";
        }
        if (!value.subscribe) {
            throw "The value you provided must be an observable for the lookupValue binding to work.";
        }
        var valSubscription = value.subscribe(function (newVal) {
            if (newVal) {
                selectValue(ko.utils
                    .unwrapObservable(newVal[allBindings().optionsValue]));
            } else {
                selectValue(undefined);
            }
        });
        toDispose.push(valSubscription);
        // if the options change we need to look up the id again
        var options = retrieveOptionsFromBindings(allBindings);
        if (!(options)) {
            throw "The options provided in your select is "
            + allBindings().options
            + ".  This must be defined for the lookupValue to work.  Did you forget to add the options or lookup binding to your select?  If you are mapping it asynchronously try setting it to ko.observableArray([])";
        }
        var optionsSubscription = options.subscribe(function (newOpts) {
            var val = ko.utils.arrayFirst(selectArray(), function (item) {
                return comparisionFunc(item, ko.utils
                    .unwrapObservable(selectValue));
            });
            value(val);
        });
        toDispose.push(optionsSubscription);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            for (var i = 0; i < options.length; ++i) {
                toDispose[i].dispose();
            }
        });
        // bind the temporary variable to the value binding so it gets updated.
        return ko.bindingHandlers.value.init(element, function () {
            return selectValue
        }, allBindings, viewModel, bindingContext);
    }
}
ko.bindingHandlers.BSModal = {
    init: function (element, valueAccessor) {
        var value = valueAccessor();
        $(element).modal({
            keyboard: false,
            show: ko.unwrap(value),
            backdrop: 'static'
        });
    },
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).modal('show') : $(element).modal('hide');
    }
};
ko.bindingHandlers.collapse = {
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        ko.utils.unwrapObservable(value) ? $(element).collapse('show') : $(element).collapse('hide');
    }
}
ko.bindingHandlers.modal = {
    init: function (element, valueAccessor, allbindings, viewModel, bindingContext) {
        var settings = valueAccessor();
        return ko.bindingHandlers.click.init(element, function () {
            return function () {
                openModal(settings.name, settings.id, settings.params);
            }
        }, allbindings, viewModel, bindingContext)
    }
}
ko.bindingHandlers.close = {
    init: function (element, valueAccessor, allbindings, viewModel, bindingContext) {
        return ko.bindingHandlers.click.init(element, function () {
            return function () {
                bindingContext.$data.closeModal(valueAccessor());
            }
        }, allbindings, viewModel, bindingContext)
    }
}
ko.bindingHandlers.fadeVisible = {
    init: function (element, valueAccessor) {
        // Initially set the element to be instantly visible/hidden depending on the value
        var value = valueAccessor();
        $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we can handle values that may or may not be observable
    },
    update: function (element, valueAccessor) {
        // Whenever the value subsequently changes, slowly fade the element in or out
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).fadeIn() : $(element).fadeOut();
    }
};
ko.bindingHandlers.slideVisible = {
    init: function (element, valueAccessor) {
        // Initially set the element to be instantly visible/hidden depending on the value
        var value = valueAccessor();
        $(element).toggle(ko.unwrap(value)); // Use "unwrapObservable" so we can handle values that may or may not be observable
    },
    update: function (element, valueAccessor) {
        // Whenever the value subsequently changes, slowly fade the element in or out
        var value = valueAccessor();
        ko.unwrap(value) ? $(element).slideDown() : $(element).slideUp();
    }
};
ko.bindingHandlers.scrollTo = {
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        if (ko.utils.unwrapObservable(value)) {
            if ($(element).is(':visible')) {
                scrollToElement(element);
            }
        }
    }
};
/**
 * Initializes the table as a data table. Takes in an object with the options to
 * pass in to initialize the table.
 */
ko.bindingHandlers.datatable = {
    init: function (element, valueAccessor, allBindings) {
        var options = ko.utils.unwrapObservable(valueAccessor());
        var row = $(element).closest('table').find('tbody > tr');
        row.remove();
        var table = $(element).closest('table').DataTable(options);
        //TODO hack to remove problem with child tables figure out how to customize the css used in the wrapper element.
        $(element).closest('.dataTables_wrapper').removeClass('form-inline');
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            table.destroy();
        });
    }
}
/**
 * Hooks up an observable array to the datatable data source. Adding or removing
 * from the array will add or remove the item from the data table array.
 */
ko.bindingHandlers.datasource = {
    init: function (element, valueAccessor, allBindings) {
        var data = ko.utils.unwrapObservable(valueAccessor());
        var table = $(element).closest('table').DataTable();
        var refresh = function () {
            var data = ko.utils.unwrapObservable(valueAccessor());
            var rowsToRemove = [];
            table.rows().every(function (rowIndex) {
                var row = table.row(rowIndex);
                if (data.indexOf(row.data()) == -1) {
                    rowsToRemove.push(rowIndex);
                }
            });
            table.rows(rowsToRemove).remove()
            for (var i = 0; i < data.length; ++i) {
                var item = data[i];
                if (item && table.data().indexOf(item) == -1) {
                    table.row.add(item);
                }
            }
            table.draw();
        }
        refresh();
        var subscription;
        if (valueAccessor().subscribe) {
            subscription = valueAccessor().subscribe(function () {
                refresh();
            });
        }
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            if (subscription) {
                subscription.dispose();
            }
        });
        return {controlsDescendantBindings: true};
    }
}
/**
 * Renders the template passed in as a child row for each row in the table. In
 * order for this to work you will need an element with the class
 * 'details-control' this will update a span to toggle between a plus and minus
 * for when the row is shown and hidden. This element acts as the click tigger
 * to hide/show the child row.
 */
ko.bindingHandlers.childRow = {
    init: function (element, valueAccessor, allBindings) {
        var template = ko.utils.unwrapObservable(valueAccessor());
        var data = null;
        var vm = null;
        if (typeof template != 'String') {
            data = template.data;
            vm = template.vm;
            template = template.name;
        }
        var table = $(element).closest('table').DataTable();
        var templateHtml = $('#' + template).html();
        $(element).on('click', '.details-control', function () {
            var table = $(element).closest('table').DataTable();
            var row = table.row($(this).closest('tr'));
            var applyBindings = false;
            if (!row.child()) {
                row.child(templateHtml);
                applyBindings = true;
            }
            var elem = $(this);
            var hideRow = function() {
                row.child.hide();
                elem.children('span.glyphicon').removeClass("glyphicon-minus-sign");
                elem.children('span.glyphicon').addClass('glyphicon-plus-sign');
            }
            var showRow = function() {
                row.child.show();
                elem.children('span.glyphicon').addClass('glyphicon-minus-sign');
                elem.children('span.glyphicon').removeClass('glyphicon-plus-sign');
            }
            if (row.child.isShown()) {
                hideRow();
                hideRow();
            }
            else {
                showRow();
            }
            if (applyBindings) {
                //delay applying bindings until after the row is shown so the map shows up
                var closeChildRowFunc = function() {
                    hideRow();
                }
                var ChildRowVm = vm || function (data) {
                        var self = this;
                        ko.mapping.fromJS(data, {}, self);
                        self.closeChildRow = closeChildRowFunc;
                    }
                ko.applyBindings(new ChildRowVm($.extend({
                    data: row.data(),
                    closeChildRow: closeChildRowFunc
                }, data)), $(row.child()[0]).get(0));
            }
        });
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            $(element).off();
        });
    }
}
/*
 * Sets up some javascript events to work around a bug in firefox where the css
 * selector '.file-input:focus + label' does not work.
 */
ko.bindingHandlers.fileUploadButton = {
    init: function (element, valueAccessor, allBindings) {
        $(element).focus(function () {
            $(element).siblings('label').addClass('file-input-focused');
        })
        $(element).blur(function () {
            $(element).siblings('label').removeClass('file-input-focused');
        });
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            $(element).off();
        });
    }
}
ko.bindingHandlers.leaflet = {
    init: function (element, valueAccessor, allBindings) {
        var options = valueAccessor();
        //TODO dispose?
        var noWrapOption = {
            noWrap: true
        };
        var map = L.map(element);
        L.esri.basemapLayer('Imagery', noWrapOption).addTo(map);
        L.esri.basemapLayer('ImageryLabels', noWrapOption).addTo(map);
        if (options.latLongMarker) {
            var latLongObj = options.latLongMarker.latLong;
            var marker;

            function setMarker(latlng) {
                //validate lat and long are present
                if (latlng) {
                    if (!marker) {
                        marker = L.marker(latlng).addTo(map);
                    }
                    else {
                        marker.setLatLng(latlng);
                    }
                }
                else {
                    //if there is a marker remove it.
                    map.removeLayer(marker);
                    marker = null;
                }
            }

            if (latLongObj.getLatLong) {
                if (latLongObj.getLatLong()) {
                    setMarker(latLongObj.getLatLong());
                }
                latLongObj.getLatLong.subscribe(function () {
                    setMarker(latLongObj.getLatLong());
                });
            }
            function onMapClick(e) {
                setMarker(e.latlng);
                latLongObj.setLatLong(e.latlng);
            }

            map.on('click', onMapClick);
        }
        if (options.center) {
            if (options.center.city && options.center.state) {
                oeca.cgp.map.centerCityState(map, options.center.city, options.center.state);
            }
            else if (options.center.state) {
                oeca.cgp.map.center(map, oeca.cgp.map.decodeState(options.center.state));
            }
            else {
                oeca.cgp.map.center(map, "US");
            }
        }
        else {
            oeca.cgp.map.center(map, "US");
        }
    }
}
ko.bindingHandlers.maskedLatLong = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var mask = allBindingsAccessor().mask || "[-]999[.999]";
        //TODO add disposal logic?
        $(element).inputmask('numeric', {
            digits: 4,
            integerDigits: 3,
            digitsOptional: true,
            placeholder: '',
            showMaskOnHover: true,
            rightAlign: false,
            showMaskOnFocus: true,
            allowMinus: true
        });
        ko.bindingHandlers.value.init(element, valueAccessor, allBindingsAccessor);
    },
    update: function (element, valueAccessor) {
        ko.bindingHandlers.maskedInput.update(element, valueAccessor);
    }
};
ko.bindingHandlers.maskedDischargeId = {
    init: function (element, valueAccessor, allBindings, viewModel) {
        $(element).inputmask('numeric', {
            digits: 0,
            integerDigits: 3,
            min: 1,
            max: 999,
            mask: '999',
            numericInput: true,
            placeholder: '000',
            showMaskOnHover: true,
            rightAlign: false,
            showMaskOnFocus: true,
            allowMinus: false
        });
        ko.bindingHandlers.value.init(element, valueAccessor, allBindings, viewModel);
    },
    update: function (element, valueAccessor) {
        var value = valueAccessor();
        value($(element).val());
        value();
    }
}
ko.bindingHandlers.disableSection = {
    update: function (element, valueAccessor, allBindingsAccessor, viewModel) {
        var value = valueAccessor();
        $(element).find(':input').prop('disabled', ko.utils.unwrapObservable(value));
        //special case to disable the file input widget button
        var fileInputButtons = $(element).find('.file-input-component label.btn');
        if (ko.utils.unwrapObservable(value)) {
            fileInputButtons.addClass('disabled');
        }
        else {
            fileInputButtons.removeClass('disabled');
        }
    }
};
oeca.cromerr = {
    sequence: 1,
    createTransaction: function (callback) {
        var transactionId = ++oeca.cromerr.sequence;
        if (callback) {
            postal.channel('cromerr-widget').subscribe('success.' + transactionId, function (data, envelope) {
                callback(data);
            });
        }
        return transactionId;
    },
    startTransaction: function (transactionId) {
        if (!transactionId) {
            throw "Transaction ID is required to start cromerr widget.  Call oeca.cromerr.createTransaction to get a Transaction ID.";
        }
        var cromerrButton = $('#cromerr-widget-init');
        cromerrButton.attr('data-cromerr-transaction-id', transactionId);
        cromerrButton.click();
    },
    disposeTransaction: function (transactionId) {
        var allWidgetSubscriptions = postal.subscriptions['cromerr-widget'];
        if (allWidgetSubscriptions) {
            var subscriptions = allWidgetSubscriptions['success.' + transactionId];
            if (subscriptions) {
                for (var i = 0; i < subscriptions.length; ++i) {
                    subscriptions[i].unsubscribe();
                }
            }
        }
    }
}
ko.bindingHandlers.cromerr = {
    init: function (element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
        var params = valueAccessor();
        var callback;
        var validate;
        if (typeof params == "function") {
            callback = params;
        }
        else if (typeof params == "object") {
            callback = params.callback;
            validate = params.validate;
        }
        else {
            throw "Value for cromerr binding must be an object or function";
        }
        var transactionId = oeca.cromerr.createTransaction(callback);
        ko.utils.domNodeDisposal.addDisposeCallback(element, function () {
            oeca.cromerr.disposeTransaction(transactionId);
        });
        return ko.bindingHandlers.click.init(element, function () {
            return function () {
                if (validate && !validate()) {
                    //failed validation don't start widget yet.
                    return;
                }
                oeca.cromerr.startTransaction(transactionId);
            }
        }, allBindingsAccessor, viewModel, bindingContext)
    }
}
/**
 * Prints 'Yes' if the value evaulates to true other wise if the value is false it prints 'No' and nothing if the value
 * is null or undefined.
 * @type {{}}
 */
ko.bindingHandlers.yesNoBlank = {
   update: function(element, valueAccessor, allBindingsAccessor, viewModel, bindingContext) {
       var value = valueAccessor();
       return ko.bindingHandlers.text.update(element, function() {
           if(ko.utils.unwrapObservable(value)) {
               return 'Yes';
           }
           else if (ko.utils.unwrapObservable(value) == false) {
               return 'No';
           }
           return '';
       }, allBindingsAccessor, viewModel, bindingContext);
   }
}
/*
 * Extenders
 */
/**
 * Pads the value to given length.  You provide the length you would like your string to be as the value and this will
 * pad it with 0s.  Otherwise you can provide an object to set other parameters.  Example options:
 * {
 *      padLength: 3,
 *      padString: '0'
 * }
 *
 * Note: this will not prevent your string from being longer than the length provided.  If the length of the string
 * exceeds the length passed in this will return the string passed in.
 *
 * @param target
 * @param option
 * @returns {KnockoutComputed<T>|KnockoutSubscribable<T>}
 */
ko.extenders.pad = function(target, option) {
    var length = option.padLength || option;
    if(length < 0) {
        throw "Length cannot be negative";
    }
    var padString = option.padString || '0';
    var paddedString = '';
    for(var i = 0; i < length; ++i) {
        paddedString += String(padString);
    }
    var computed = ko.pureComputed({
        read: target,
        write: function(newVal) {
            var formatted = newVal ? oeca.cgp.utils.padWithPaddedString(newVal, length, paddedString) : null;
            if(formatted !== target()) {
                target(formatted);
            }
            else if (newVal !== target()) {
                //value was changed but formatted the same notify subscribers.
                target.notifySubscribers(formatted);
            }
        }
    }).extend({notify: 'always'});
    computed(target());
    return computed;
};
/**
 * Rounds value to the specified precision.  You may pass in a number as the value to the extender and that number will
 * be used as the precision or you may pass in some options in the format of:
 * {
 *      precision: 2,
 *      decimal: true
 * }
 * options:
 * precision - specifies to what digit to round to
 * decimal - if true the precision will round to the decimal digit otherwise it will round to the whole number digit.
 *
 * Examples:
 * var observable = ko.observable(null).extend({round: 2});
 * observable(1234.56789)
 * observable will be 1234.57
 * ---------
 * var observable = ko.observable(null).extend({round: {precision: 2, decimal: false}});
 * observable(1234.56789)
 * observable will be 1200.
 *
 * @param target
 * @param option
 * @returns {KnockoutComputed<T>|KnockoutSubscribable<T>}
 */
ko.extenders.round = function(target, option) {
    if(option == null || option == undefined) {
        throw "You must provide a value for the round extender"
    }
    var precision = option.precision == null || option.precision == undefined ? option : option.precision;
    if(precision == null || precision == undefined || isNaN(precision)) {
        throw "Precision is required and must be a number for the round extender"
    }
    var decimal = option.decimal == null || option.decimal == undefined ? true : option.decimal;
    var scale = Math.pow(10, precision);
    var computed = ko.pureComputed({
        read: target,
        write: function(newVal) {
            if(newVal == null || newVal == undefined || newVal == '') {
                if(target() == newVal) {
                    return;
                }
                else {
                    target(newVal);
                    return;
                }
            }
            var rounded = null;
            if(scale == 0) {
                //avoid divide by 0
                rounded = Math.round(newVal);
            }
            else {
                var rounded = decimal ? Math.round(newVal * scale)/scale : Math.round(newVal/scale) * scale;
            }
            if(rounded !== target()) {
                target(rounded);
            }
            else if(newVal !== target()) {
                //value was changed but formatted the same notify subscribers.
                target.notifySubscribers(rounded);
            }
        }
    }).extend({notify: 'always'});
    computed(target());
    return computed;
};
/**
 * Allows you to pass in an observable as the data for a datatable column. This
 * will unwrap the observable and update the cell in datatable whenever the data
 * changes.
 */
$.fn.dataTable.render.ko = {
    observable: function () {
        return function (data, type, full, meta) {
            if (ko.isObservable(data)) {
                var subscription = data.subscribe(function (newVal) {
                    $(meta.settings.nTable).DataTable().cell(meta.row, meta.col).invalidate();
                })
                $(meta.settings.nTable).on("destroy.dt", function () {
                    subscription.dispose();
                });
                return data();
            }
            return data;
        }
    },
    computed: function (func) {
        return function (data, type, full, meta) {
            var computed = ko.pureComputed(function () {
                return func(data);
            });
            var subscription = computed.subscribe(function () {
                $(meta.settings.nTable).DataTable().cell(meta.row, meta.col).invalidate();
            });
            $(meta.settings.nTable).on("destroy.dt", function () {
                subscription.dispose();
                computed.dispose();
            });
            return computed();
        }
    },
    template: function (template, params) {
        var templateHtml = $('#' + template).html();
        if (!templateHtml) {
            throw "Error template " + template + " was not found.";
        }
        return $.fn.dataTable.render.ko.templateInline(templateHtml, params);
    },
    templateInline: function (template, params) {
        return function (data, type, full, meta) {
            if (type == 'display') {
                setTimeout(function () {
                    var cell = $(meta.settings.nTable).DataTable().cell(meta.row, meta.col).node();
                    ko.cleanNode(cell);
                    ko.applyBindings($.extend({
                        data: data
                    }, params), cell);
                }, 0);
                return template;
            }
            return null;
        }
    },
    truncated: function (length) {
        return function (data, type, full, meta) {
            var wordBreak = true;
            var text = $.fn.dataTable.render.ko.observable()(data, type, full, meta);
            return oeca.cgp.utils.truncateSpan(text);
        };
    },
    action: function (name, action, cssClass, table) {
        $(table).on('click', '[data-ko-action-name="' + name + '"]', function (event) {
            var row = $(event.target).closest('tr');
            var dtRow = $(table).DataTable().row(row);
            var data = dtRow.data();
            action(data, row);
        });
        $(table).on("destroy.dt", function () {
            $(table).off();
        });
        return function (data, type, full, meta) {
            btnClass = cssClass || 'btn-primary';
            return '<button class="btn ' + btnClass + '" data-ko-action-name="' + name + '">' + name + '</button>';
        }
    },
    actions: function (actions, table) {
        var html = '';
        for (var i = 0; i < actions.length; ++i) {
            var action = actions[i];
            html += $.fn.dataTable.render.ko.action(action.name, action.action, action.cssClass, table)();
        }
        return function (data, type, full, meta) {
            return html;
        }
    }
};
ko.dirtyFlag = function(root, isInitiallyDirty, settings) {
    if(settings && settings.initDirty) {
        isInitiallyDirty = true;
    }
    var result = function() {},
        _initialState = ko.observable(ko.mapping.toJSON(root, settings)),
        _isInitiallyDirty = ko.observable(isInitiallyDirty);

    result.isDirty = ko.computed(function() {
        return _isInitiallyDirty() || _initialState() !== ko.mapping.toJSON(root, settings);
    });

    result.reset = function() {
        _initialState(ko.mapping.toJSON(root, settings));
        _isInitiallyDirty(false);
    };

    result.debug = function() {
        console.log("settings");
        console.log(settings);
        console.log("original data");
        console.log(_initialState());
        console.log("current data");
        console.log(ko.mapping.toJSON(root, settings));
        console.log("is dirty? " + result.isDirty());
    }

    return result;
};