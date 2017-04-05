<div data-bind="if: showSearch()">
    <div class="h4">Search for <span data-bind="text: type() != null && type() != undefined ? type() : 'user'"></span></div>
        <!-- ko with: searchCriteria -->
    	<div class="row">
    		<div class="col-sm-3 form-group">
    			<label class="control-label" for="user-id">User ID</label>
    			<input id="user-id" class="form-control" type="text" data-bind="value: userId" />
    		</div>
    		<div class="col-sm-3 form-group">
    			<label class="control-label" for="first-name">First Name</label>
    			<input id="first-name" class="form-control" type="text" data-bind="value: firstName" />
    		</div>
    		<div class="col-sm-2 form-group">
    			<label class="control-label" for="middle-initial">Middle Initial</label>
    			<input id="middle-initial" class="form-control" type="text" maxlength="1" data-bind="value: middleInitial" />
    		</div>
    		<div class="col-sm-4 form-group">
    			<label class="control-label" for="last-name">Last Name</label>
    			<input id="last-name" class="form-control" type="text" data-bind="value: lastName" />
    		</div>
    	</div>
    	<div class="row">
   			<div class="col-sm-6 form-group">
   				<label class="control-label" for="contact-organization">Organization</label>
   				<input id="contact-organization" class="form-control" type="text" data-bind="value: organization" />
   			</div>
            <div class="col-sm-6 form-group">
                <label class="control-label" for="email">Email</label>
                <input id="email" class="form-control" type="text" data-bind="value: email" />
            </div>
    	</div>
    	<div class="row">
    	    <div class="col-sm-7 form-group">
    	        <label class="control-label" for="contact-address">Address</label>
    	        <input type="text" id="contact-address" class="form-control" data-bind="value: address"/>
    	    </div>
    	</div>
    	<!-- /ko -->
    <div>
        <button class="btn btn-primary" data-bind="click: function(){search()}">Search</button>
    </div>
</div>
<div data-bind="if: showResults()">
    <p>Select <span data-bind="text: type() != null && type() != undefined ? 'your ' + type() : 'user'"></span> from the list below:</p>
    <table class="table table-bordered table-condensed dataTable responsive no-wrap word-break" style="width: 100%"
    		data-bind="attr: {id: id + '-search-results'}, 
    		datatable: {
    			columns: [
    				{
    					name: 'action',
    					orderable: false,
    					render: $.fn.dataTable.render.ko.action('select', selectUser, 'btn-primary-outline', '#' + id + '-search-results')
    				},
    				{
    				    name: 'userId',
    				    'orderable': true,
    				    'data': 'userId',
    				    render: $.fn.dataTable.render.ko.observable()
    				},
    				{
    					name: 'certifierName',
    					'orderable': true,
    					'data': 'name',
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
    					visible: (showRoleColumn == true),
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
    			]
    		}">
        <thead>
            <tr>
                <th></th>
                <th>User ID</th>
                <th><span data-bind="text: type"></span> Name</th>
                <th>Organization</th>
                <th>Role</th>
                <th>Address</th>
                <th>Email</th>
            </tr>
        </thead>
        <tbody data-bind="datasource: searchResults">
        </tbody>
    </table>
    <!-- ko if: helpText -->
    <p data-bind="template: helpText.results"></p>
    <!-- /ko -->
</div>
<div data-bind="if: showInvite()">
    <p>Enter your <span data-bind="text: type"></span>'s information below:</p>
    <contact-info params="contact: newUser"></contact-info>
    <div>
    	<button class="btn btn-primary" data-bind="click: function() {selectInviteUser(newUser)}">Invite non-CDX <span data-bind="text: type"></span></button>
    </div>
</div>
<div data-bind="if: showConfirm()">
    <p>You have selected:</p>
    <!-- ko if: selectedUser() -->
    <div class="h3">
        <span data-bind="text: selectedUser().name"></span>
        [<span data-bind="text: selectedUser().userId"></span>]
    </div>
    <div class="h4" data-bind="text: selectedUser().organization"></div>
    <div class="h4" data-bind="text: selectedUser().email"></div>
    <!-- /ko -->
    <!-- ko if: helpText -->
    <p data-bind="template: helpText.confirm"></p>
    <!-- /ko -->
    <div>
        <button class="btn btn-primary" data-bind="click: confirmUser">Submit to <span data-bind="text: type"></span></button>
    </div>
</div>
<div data-bind="if: showInviteConfirm()">
    <p>You are going to invite:</p>
    <div class="h3">
        <span data-bind="text: selectedUser().name"></span>
    </div>
    <div class="h4" data-bind="text: selectedUser().organization"></div>
    <div class="h4" data-bind="text: selectedUser().email"></div>
    <!-- ko if: helpText && helpText.inviteConfirm -->
    <p data-bind="template: helpText.inviteConfirm"></p>
    <!-- /ko -->
    <div>
        <button class="btn btn-primary" data-bind="click: inviteUser">Invite <span data-bind="text: type"></span></button>
    </div>
</div>