package gov.epa.oeca.cgp.infrastructure.certification;

import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.security.ApplicationUser;

import java.io.File;
import java.util.List;

/**
 * @author dfladung
 */
public interface CromerrService {

    // this is mostly for testing, as activity will be created externally by widget
    String createActivity(ApplicationUser user) throws ApplicationException;

    void signAttachments(CgpNoiForm form, List<Attachment> attachments) throws ApplicationException;

    File retrieveAttachmentData(String activityId, String documentId) throws ApplicationException;
}
