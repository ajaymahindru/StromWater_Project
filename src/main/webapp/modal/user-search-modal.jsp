<%@ taglib prefix="stripes" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<stripes:layout-render name="/layout/modal-layout.jsp" type="info" addClass="modal-lg"
                       title='Search for user'></span>">
    <stripes:layout-component name="body">
        <!-- ko if: $parent.open() -->
        <user-search params="id: 'search-modal',
                            callback: confirmUser,
                            showRoleColumn: true,
                            helpText: {
                                results: 'user-search-help-desk-results-help',
                                confirm: 'user-search-help-desk-confirm-help'
                            }"></user-search>
        <!-- /ko -->
    </stripes:layout-component>
</stripes:layout-render>