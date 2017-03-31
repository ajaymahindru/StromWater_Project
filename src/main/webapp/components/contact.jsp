<!-- ko with: contact -->
	<div class="row">
		<div class="col-sm-5 form-group">
			<label class="control-label" for="first-name">First Name</label>
			<input id="first-name" class="form-control" type="text" maxlength="30" data-bind="value: firstName" />
		</div>
		<div class="col-sm-2 form-group">
			<label class="control-label" for="middle-initial">Middle Initial</label>
			<input id="middle-initial" class="form-control" type="text" maxlength="1" data-bind="value: middleInitial" />
		</div>
		<div class="col-sm-5 form-group">
			<label class="control-label" for="last-name">Last Name</label>
			<input id="last-name" class="form-control" type="text" maxlength="30" data-bind="value: lastName" />
		</div>
	</div>
	<div class="row">
		<!-- ko if: $data.organization != undefined -->
			<div class="col-sm-7 form-group">
				<label class="control-label" for="contact-organization">Organization</label>
				<input id="contact-organization" class="form-control" type="text" maxlength="100" data-bind="value: organization" />
			</div>
		<!-- /ko -->
		<!-- ko if: $data.title != undefined -->
			<div class="col-sm-7 form-group">
				<label class="control-label" for="poc-title">Title</label>
				<input id="poc-title" class="form-control" type="text" maxlength="40" data-bind="value: title" />
			</div>
		<!-- /ko -->
	</div>
	<!-- ko if: $data.address != undefined -->
	<div class="row">
	    <div class="col-sm-7 form-group">
	        <label class="control-label" for="contact-address">Address</label>
	        <input type="text" id="contact-address" class="form-control" data-bind="value: address"/>
	    </div>
	</div>
	<!-- /ko -->
	<!-- ko if: $data.phone != undefined -->
	<div class="row">
		<div class="col-sm-3 col-xs-7 form-group">
			<label class="control-label" for="phone">Phone</label>
			<input id="phone" class="form-control" type="text" data-bind="maskedPhone: phone" />
		</div>
		<div class="col-sm-2 col-xs-5 form-group">
			<label class="control-label" for="phone-ext">Ext.</label>
			<input id="phone-ext" class="form-control" type="text" maxlength="4" data-bind="maskedPhoneExtension: phoneExtension"/>
		</div>
	</div>
	<!-- /ko -->
	<div class="row">
		<div class="col-sm-12 form-group">
			<label class="control-label" for="email">Email</label>
			<input id="email" class="form-control" type="text" maxlength="100" data-bind="value: email" />
		</div>
	</div>
	<!-- ko if: $data.verifyEmail != undefined -->
	    <div class="row">
            <div class="col-sm-12 form-group">
                <label class="control-label" for="verify-email">Verify Email</label>
                <input id="verify-email" class="form-control" type="text" data-bind="value: verifyEmail" />
            </div>
        </div>
	<!-- /ko -->
	<!-- ko if: $data.ein != undefined -->
		<div class="row">
			<div class="col-sm-12 form-group">
				<label class="control-label" for="poc-ein">IRS Employer Identification Number (EIN)</label>
				<input id="ein" class="form-control" type="text" data-bind="value: ein" />
			</div>
		</div>
	<!-- /ko -->
<!-- /ko -->