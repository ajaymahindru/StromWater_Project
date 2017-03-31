<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<div class="h3">Welcome to the Electronic CGP NOI Form</div>
<div data-bind="ifnot: form() || error()">
	Loading your form.  Please wait...
</div>
<div data-bind="if: error">
	<div class="alert alert-danger">
		We have encountered an error loading your form.  <a href="#!/home">Return to dashboard</a>
	</div>
</div>
<div style="height: 30px;">
	<div class="pull-right form-group required"><label class="control-label"></label> = required</div>
</div>
<div class="panel-group" id="form-accordion" data-bind="if: form">
	<screening-questions params="form: form,
							completeCallback: sections.screening.completeCallback,
							toggle: sections.screening.toggle,
							errors: sections.screening.errors,
							onLoad: sections.screening.loaded"></screening-questions>
	<!-- ko if: sections.operator.show -->
		<operator-information params="form: form,
							completeCallback: sections.operator.completeCallback,
							toggle: sections.operator.toggle,
							errors: sections.operator.errors,
							onLoad: sections.operator.loaded"></operator-information>
	<!-- /ko -->
	<!-- ko if: sections.site.show -->
		<project-site params="form: form,
							completeCallback: sections.site.completeCallback,
							toggle: sections.site.toggle,
							errors: sections.site.errors,
							waitListEdit: waitListEdit,
							onLoad: sections.site.loaded"></project-site>
	<!-- /ko -->
	<!-- ko if: sections.discharge.show -->
		<discharge-information params="form: form,
									completeCallback: sections.discharge.completeCallback,
									toggle: sections.discharge.toggle,
									errors: sections.discharge.errors,
									waitListEdit: waitListEdit,
									onLoad: sections.discharge.loaded"></discharge-information>
	<!-- /ko -->
	<!-- ko if: chemicalInformationSectionValid -->
	<!-- ko if: sections.chemical.show -->
		<chemical-information params="form: form, 
							completeCallback: sections.chemical.completeCallback,
							toggle: sections.chemical.toggle,
							errors: sections.chemical.errors,
							waitListEdit: waitListEdit,
							onLoad: sections.chemical.loaded"></chemical-information>
	<!-- /ko -->
	<!-- /ko -->
	<!-- ko if: sections.swppp.show -->
		<swppp params="form: form, 
							completeCallback: sections.swppp.completeCallback,
							toggle: sections.swppp.toggle,
							errors: sections.swppp.errors,
							onLoad: sections.swppp.loaded"></swppp>
	<!-- /ko -->
	<!-- ko if: sections.endangered.show -->
		<endangered-species-protection params="form: form, 
							completeCallback: sections.endangered.completeCallback,
							toggle: sections.endangered.toggle,
							errors: sections.endangered.errors,
							waitListEdit: waitListEdit,
							onLoad: sections.endangered.loaded"></endangered-species-protection>
	<!-- /ko -->
	<!-- ko if: sections.historic.show -->
		<historic-preservation params="form: form, 
							completeCallback: sections.historic.completeCallback,
							toggle: sections.historic.toggle,
							errors: sections.historic.errors,
							waitListEdit: waitListEdit,
							onLoad: sections.historic.loaded"></historic-preservation>
	<!-- /ko -->
	<!-- ko if: sections.certification.show -->
		<sec:authorize access="hasAnyRole('${actionBean.preparerRole}', '${actionBean.certifierRole}')">
			<certification-information params="form: form,
								completeCallback: sections.certification.completeCallback,
								toggle: sections.certification.toggle,
								errors: sections.certification.errors,
								onLoad: sections.certification.loaded"></certification-information>
		</sec:authorize>
		<sec:authorize access="hasRole('${actionBean.helpdeskRole}')">
			<certification-information-paper params="form: form,
								completeCallback: sections.certification.completeCallback,
								toggle: sections.certification.toggle,
								errors: sections.certification.errors,
								onLoad: sections.certification.loaded"></certification-information-paper>
		</sec:authorize>
	<!-- /ko -->
	<!-- ko if: sections.operator.show -->
	<hr>
	<div class="row">
		<div class="col-sm-12">
			<button class="btn btn-primary" data-bind="click: save">Save and Close</button>
		</div>
	</div>
	<!-- /ko -->
</div>