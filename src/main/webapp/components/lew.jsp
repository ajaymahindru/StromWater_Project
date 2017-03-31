<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<h1>Electronic Low Erosivity Waiver Form</h1>
<p>
	This form provides notice to EPA that you, the project operator, are certifying that construction activity at the
	project site is less than five (5) acres in area and will take place during a period when the rainfall erosivity factor
	is less than five (5) <a href="${actionBean.cgpUrls.cfr12226}" target="_blank">40 CFR 122.26 (b)(15)(i)(A)</a>. By submitting a complete and accurate
	form, the otherwise applicable NPDES permitting requirements for stormwater discharges associated with construction
	activity are waived. Based on your certification, a waiver is granted for the period beginning on the date this Low
	Erosivity Waiver Form is submitted to EPA, or the project start dates specified on this form, whichever shall occur
	last, and ending on the project completion date.
</p>
<div class="panel-group" id="lew-accordion" data-bind="if: form">
	<lew-screening-questions params="form: form,
								toggle: sections.screening.toggle,
								completeCallback: sections.screening.completeCallback,
								errors: sections.screening.errors,
								onLoad: sections.screening.loaded"></lew-screening-questions>
	<!-- ko if: sections.erosivity.show -->
	<lew-erosivity-information params="form: form,
								toggle: sections.erosivity.toggle,
								completeCallback: sections.erosivity.completeCallback,
								errors: sections.erosivity.errors,
								onLoad: sections.erosivity.loaded"></lew-erosivity-information>
	<!-- /ko -->
	<!-- ko if: sections.operator.show -->
	<operator-information params="form: form,
								toggle: sections.operator.toggle,
								completeCallback: sections.operator.completeCallback,
								errors: sections.operator.errors,
								onLoad: sections.operator.loaded"></operator-information>
	<!-- /ko -->
	<!-- ko if: sections.project.show -->
	<lew-project-site params="form: form,
								toggle: sections.project.toggle,
								completeCallback: sections.project.completeCallback,
								errors: sections.project.errors,
								onLoad: sections.project.loaded"></lew-project-site>
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
	<!-- ko if: sections.erosivity.show -->
	<hr>
	<div class="row">
		<div class="col-sm-12">
			<button class="btn btn-primary" data-bind="click: save">Save and Close</button>
		</div>
	</div>
	<!-- /ko -->
</div>