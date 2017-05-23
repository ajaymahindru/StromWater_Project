<div class="panel panel-info" data-bind="template: {afterRender: componentLoaded() }">
	<div class="panel-heading">
		<div data-bind="click: function(){expand(!expand())}">
			<a role="button" href="JavaScript:;">
				Stormwater Pollution Prevention Plan
			</a>
			<!-- ko if: expand -->
				<span class="glyphicon glyphicon-chevron-down pull-right"></span>
			<!-- /ko -->
			<!-- ko ifnot: expand -->
				<span class="glyphicon glyphicon-chevron-up pull-right"></span>
			<!-- /ko -->
			<!-- ko if: $data.errors && errors.isAnyMessageShown() -->
			<span class="label label-danger" title="This section has validation errors.">
				<span class="glyphicon glyphicon-alert"></span>
				<span data-bind="text: errors().length"></span>
			</span>
			<!-- /ko -->
			<!-- ko if: errors().length == 0-->
			<span class="label label-primary" title="This section has validation errors.">
				<span class="glyphicon glyphicon-ok" title="No errors"></span>
			</span>
			<!-- /ko -->
		</div>
	</div>
	<div class="panel-collapse" id="swppp" data-bind="slideVisible: expand">
		<div class="panel-body">
			<div class="h4">SWPPP Contact Information:</div>
			<contact-info params="contact: contactInformation, labels: {
				title: 'Professional Title',
				middleInitial: 'M.I.',
				phoneExt: 'Extension'
			}"></contact-info>
			<button class="btn btn-primary" data-bind="click: saveAndContinue"
					onclick="oeca.utils.scrollToTop();">
				<!-- ko if: showSaveButton -->
				Save Section
				<!-- /ko -->
				<!-- ko ifnot: showSaveButton -->
				Next Section
				<!-- /ko -->
			</button>
		</div>
	</div>
</div>