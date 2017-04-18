<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="stripes"
           uri="http://stripes.sourceforge.net/stripes-dynattr.tld"%>
<stripes:layout-definition>
    <script type="text/html" id="underlined-field">
        <span class="cor-underline">
            <span class="form-control-static">
                <span data-bind="text: field"></span>
            </span>
        </span>
    </script>
    <script type="text/html" id="underlined-field-group">
        <!-- ko foreach: fields -->
            <span class="cor-underline" data-bind="css: css">
                <span class="form-control-static">
                    <span data-bind="text: field"></span>
                </span>
            </span>
        <!-- /ko -->
    </script>
    <script type="text/html" id="view-checkbox">
        <label>
            <input type="radio" class="sr-only" disabled/>
            <span class="fa" data-bind="css: {'fa-check-square-o': isChecked, 'fa-square-o': !isChecked}"></span>
            <span data-bind="text: text"></span>
        </label>
    </script>
    <script type="text/html" id="cor-address">
        <div class="row">
            <div class="col-xs-12 form-group cor-underline-group">
                <label class="control-label">Street/Location:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: address1
                                 }
                            }"></span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6 form-group cor-underline-group">
                <label class="control-label">City:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: city
                                }
                            }"></span>
            </div>
            <div class="col-xs-2 form-group cor-underline-group">
                <label class="control-label">State:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: state
                                }
                            }"></span>
            </div>
            <div class="col-xs-4 form-group cor-underline-group">
                <label class="control-label">Zip Code:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: zip
                                }
                            }"></span>
            </div>
        </div>
        <div class="row">
            <div class="col-xs-12 form-group cor-underline-group">
                <label class="control-label">County or Similar Government Subdivision:</label>
                <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: county
                                }
                            }"></span>
            </div>
        </div>
    </script>
    <script type="text/html" id="cor-contact">
        <!-- ko if: $data.firstName != undefined -->
        <div class="form-group cor-underline-group">
            <label class="control-label">First Name, Middle Initial, LastName:</label>
            <!-- ko template: {
                        name: 'underlined-field-group',
                        data: {
                            fields: [
                                {
                                    field: firstName,
                                    css: ''
                                },
                                {
                                    field: middleInitial,
                                    css: 'xs'
                                },
                                {
                                    field: lastName,
                                    css: ''
                                }
                            ]
                        }
                    } --><!-- /ko -->
        </div>
        <!-- /ko -->
        <!-- ko if: $data.title != undefined -->
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Title:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: title
                                }
                            }"></span>
                    </div>
                </div>
            </div>
        <!-- /ko -->
        <!-- ko if: $data.phone != undefined -->
            <div class="row">
                <div class="col-xs-4">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Phone:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: phone
                                }
                            }"></span>
                    </div>
                </div>
                <div class="col-xs-2">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Ext.</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: phoneExtension
                                }
                            }"></span>
                    </div>
                </div>
            </div>
        <!-- /ko -->
        <!-- ko if: $data.email != undefined -->
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group cor-underline-group">
                        <label class="control-label">Email:</label>
                        <span data-bind="template: {
                                name: 'underlined-field',
                                data: {
                                    field: email
                                }
                            }"></span>
                    </div>
                </div>
            </div>
        <!-- /ko -->
    </script>
</stripes:layout-definition>