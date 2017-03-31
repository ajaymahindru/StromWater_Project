<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
    <div class="panel-heading">
        <div data-bind="click: function(){expand(!expand())}">
            <a role="button" href="JavaScript:;">Certification Information</a>
                <!-- ko if: expand -->
                <span class="glyphicon glyphicon-chevron-down pull-right"></span>
                <!-- /ko -->
                <!-- ko ifnot: expand -->
                <span class="glyphicon glyphicon-chevron-up pull-right"></span>
                <!-- /ko -->
            </a>
        </div>
    </div>
    <div class="panel-collapse" id="certificationInformation" data-bind="slideVisible: expand">
        <div class="panel-body">
            <label class="radio"><input type="checkbox" data-bind="checked: certifierSame"/> Certifier Prepared Form</label>
            <!-- ko ifnot: certifierSame -->
            <div class="h4">Preparer</div>

            <contact-info params="contact: {
                firstName: preparer().firstName,
                middleInitial: preparer().middleInitial,
                lastName: preparer().lastName,
                organization: preparer().organization,
                phone: preparer().phone,
                phoneExtension: preparer().phoneExtension,
                email: preparer().email
            }"></contact-info>
            <hr>
            <!-- /ko -->
            <div class="h4">Certifier</div>
            <div class="clear">
                <contact-info params="contact: {
                    firstName: certifier().firstName,
                    middleInitial: certifier().middleInitial,
                    lastName: certifier().lastName,
                    title: certifier().title,
                    email: certifier().email
                }"></contact-info>
            </div>
            <div class="row">
                <div class="col-sm-12">
                    <div class="form-group">
                        <label class="control-label" for="certified-date">Date Signed:</label>
                        <input type="text" class="form-control" id="certified-date" data-bind="date: certifiedDate, datepicker, dateFormats: {from: 'YYYY-MM-DDTHH:mm:ss.SSSZZ', to: 'MM/DD/YYYY'}">
                    </div>
                </div>
            </div>
            <button class="btn btn-primary" data-bind="click: sign">Complete Form</button>
        </div>
    </div>
</div>