package gov.epa.oeca.cgp.infrastructure.certification;

import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.security.ApplicationUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;
import java.util.UUID;

/**
 * @author dfladung
 */
public class MockCromerrServiceImpl implements CromerrService {

    private static final Logger logger = LoggerFactory.getLogger(MockCromerrServiceImpl.class);

    public MockCromerrServiceImpl() {
        logger.warn("Using mock ceritfication services.");
    }


    @Override
    public String createActivity(ApplicationUser user) throws ApplicationException {
        return UUID.randomUUID().toString();
    }

    @Override
    public void signAttachments(CgpNoiForm form, List<Attachment> attachments) throws ApplicationException {
        for (Attachment a : attachments){
            a.setCromerrAttachmentId(UUID.randomUUID().toString());
        }
    }

    @Override
    public File retrieveAttachmentData(String activityId, String documentId) throws ApplicationException {
        return null;
    }
}
