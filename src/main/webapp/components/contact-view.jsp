<!-- ko with: contact -->
<div class="row">
    <div class="col-sm-12">
    	<div class="form-group">
    		<label class="control-label" for="contact-name">Name:</label>
    		<span class="form-group-static" id="contact-name" data-bind="text: name"></span>
    	</div>

    </div>
</div>
<div class="row">
    <!-- ko if: $data.organization != undefined -->
    <div class="col-sm-7 form-group">
        <label class="control-label" for="contact-organization">Organization:</label>
        <span class="form-control-static" id="contact-organization" data-bind="text: organization"
              style="word-wrap: break-word"></span>
    </div>
    <!-- /ko -->
    <!-- ko if: $data.title != undefined -->
    <div class="col-sm-7 form-group">
        <label class="control-label" for="poc-title">Title:</label>
        <span id="poc-title" class="form-control-static" data-bind="text: title" ></span>
    </div>
    <!-- /ko -->
</div>
<!-- ko if: $data.address != undefined -->
<div class="row">
    <div class="col-sm-6 form-group">
        <label class="control-label" for="contact-address">Address:</label>
        <span id="contact-address" class="form-control" data-bind="text: address"></span>
    </div>
</div>
<!-- /ko -->
<!-- ko if: $data.phone != undefined -->
<div class="row">
    <div class="col-sm-6 col-xs-12 form-group">
        <label class="control-label" for="phone">Phone:</label>
        <span id="phone" class="form-control-static" data-bind="text: $parent.fullPhone"></span>
    </div>
</div>
<!-- /ko -->
<div class="row">
    <div class="col-sm-12 form-group">
        <label class="control-label" for="email">Email:</label>
        <span id="email" class="form-control-static" data-bind="text: email"></span>
    </div>
</div>
<!-- /ko -->
<!-- ko if: $data.ein != undefined -->
<div class="row">
    <div class="col-sm-12 form-group">
        <label class="control-label" for="ein">IRS Employer Identification Number (EIN):</label>
        <span id="ein" class="form-control-static" data-bind="text: ein"></span>
    </div>
</div>
<!-- /ko -->
<!-- /ko -->