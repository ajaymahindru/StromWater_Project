package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchResult;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSetSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.CgpNoiFormSet;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;

import java.io.File;
import java.util.List;

/**
 * @author dfladung
 */
public interface CgpNoiFormService {

    // NOI
    Boolean isEligible(CgpNoiForm form) throws ApplicationException;

    CgpNoiForm createNewNoticeOfIntent(CgpNoiForm form) throws ApplicationException;

    CgpNoiForm changeNoticeOfIntent(Long previousNoiFormId) throws ApplicationException;

    CgpNoiForm terminateNoticeOfIntent(Long previousNoiFormId, CgpNoiForm updatedForm) throws ApplicationException;

    // LEW
    CgpNoiForm createNewLowErosivityWaiver(CgpNoiForm form) throws ApplicationException;

    CgpNoiForm changeLowErosivityWaiver(Long previousLewFormId) throws ApplicationException;

    CgpNoiForm discontinueLowErosivityWaiver(Long previousLewFormId, CgpNoiForm updatedForm) throws ApplicationException;


    // common life-cycle
    void assignFormSet(Long formSetId, String ownerUserId) throws ApplicationException;

    // called by preparer.
    void routeToCertifier(Long formId, String certifierId) throws ApplicationException;

    // called by certifier
    void rejectForm(Long formId, String reason) throws ApplicationException;

    // called by owner
    void updateForm(Long formId, CgpNoiForm form) throws ApplicationException;

    // called by owner
    void withdrawForm(Long formId) throws ApplicationException;

    List<Attachment> retrieveAttachments(Long formId) throws ApplicationException;

    // called by owner
    void addAttachment(Long formId, Attachment attachment) throws ApplicationException;

    // called by owner
    void addAttachments(Long formId, List<Attachment> attachments) throws ApplicationException;

    // called by owner
    void removeAttachment(Long attachmentId) throws ApplicationException;

    // called by certifier
    void certifyForm(Long formId, String activityId) throws ApplicationException;

    // called by RA
    void denyForm(Long formId, String reason) throws ApplicationException;

    // called by RA
    void holdForm(Long formId) throws ApplicationException;

    // called by RA
    void releaseForm(Long formId) throws ApplicationException;

    // called by HD
    void submitForm(Long formId) throws ApplicationException;

    // called by system/job
    void activateForm(Long formId) throws ApplicationException;
    void distributeForm(Long formId) throws ApplicationException;
    void notifyAbandonedDrafts() throws ApplicationException;


    // common retrieval
    CgpNoiForm retrieveForm(Long formId) throws ApplicationException;

    CgpNoiFormSet retrieveFormSet(Long formSetId) throws ApplicationException;

    List<CgpNoiFormSet> retrieveFormSet(CgpNoiFormSetSearchCriteria criteria) throws ApplicationException;

    List<CgpNoiForm> retrieveForms(CgpNoiFormSearchCriteria criteria) throws ApplicationException;

    CgpNoiFormSearchResult retrieveSearchResult(DataTableCriteria<CgpNoiFormSearchCriteria> search) throws ApplicationException;

    File retrieveAttachmentData(Long attachmentId) throws ApplicationException;

    Attachment retrieveAttachment(Long attachmentId) throws ApplicationException;

    // public
    CgpNoiForm retrievePublicForm(Long id) throws ApplicationException;

    List<CgpNoiForm> retrievePublicForms(CgpNoiFormSearchCriteria criteria) throws ApplicationException;

    Attachment retrievePublicAttachment(Long id) throws ApplicationException;

    File retrievePublicAttachmentData(Long formId, Long attachmentId) throws ApplicationException;

    void inviteUser(String role, Contact contact) throws ApplicationException;

    void sendIcisTransactionFailure(CgpNoiForm form, List<Document> documents, String statusDetail) throws  ApplicationException;
}
