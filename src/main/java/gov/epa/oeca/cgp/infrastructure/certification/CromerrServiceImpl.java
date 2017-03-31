package gov.epa.oeca.cgp.infrastructure.certification;

import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.cdx.signature.DocumentDataSource;
import gov.epa.oeca.common.infrastructure.cdx.signature.SignatureServiceClient;
import gov.epa.oeca.common.security.ApplicationUser;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentFormatType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureDocumentType;
import net.exchangenetwork.wsdl.register.sign._1.SignatureUserType;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.activation.DataHandler;
import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author dfladung
 */
public class CromerrServiceImpl implements CromerrService {

    private static final Logger logger = LoggerFactory.getLogger(CromerrServiceImpl.class);
    private static final String CROMERR_DATAFLOW = "CGP";

    @Autowired
    SignatureServiceClient signatureServiceClient;

    @Resource(name = "registerSignEndpointConfiguration")
    Map<String, String> registerSignEndpointConfiguration;

    @Override
    public String createActivity(ApplicationUser user) throws ApplicationException {
        try {
            URL signatureServiceUrl = new URL(getSignatureServiceProperty("serviceUrl"));
            String signatureToken = getSignatureToken(signatureServiceUrl);
            SignatureUserType signatureUser = new SignatureUserType();
            signatureUser.setFirstName(user.getFirstName());
            signatureUser.setLastName(user.getLastName());
            signatureUser.setMiddleInitial(user.getMiddleInitial());
            signatureUser.setUserId(user.getUsername());
            signatureUser.setOrganizationName(user.getOrganization());
            return signatureServiceClient.createActivity(signatureServiceUrl, signatureToken, signatureUser, CROMERR_DATAFLOW);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public void signAttachments(CgpNoiForm form, List<Attachment> attachments) throws ApplicationException {
        try {
            URL signatureServiceUrl = new URL(getSignatureServiceProperty("serviceUrl"));
            String signatureToken = getSignatureToken(signatureServiceUrl);
            for (Attachment a : attachments) {
                SignatureDocumentType sigDoc = new SignatureDocumentType();
                sigDoc.setName(a.getName());
                sigDoc.setFormat(SignatureDocumentFormatType.BIN);
                File tmp = File.createTempFile("Attachment", null);
                FileUtils.copyFile(a.getData(), tmp); // copy so original is not automatically deleted
                sigDoc.setContent(new DataHandler(new DocumentDataSource(tmp, "application/octet-stream")));
                String cromerrDocumentId = signatureServiceClient
                        .sign(signatureServiceUrl, signatureToken, form.getCromerrActivityId(), sigDoc);
                a.setCromerrAttachmentId(cromerrDocumentId);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public File retrieveAttachmentData(String activityId, String documentId) throws ApplicationException {
        try {
            URL signatureServiceUrl = new URL(getSignatureServiceProperty("serviceUrl"));
            String signatureToken = getSignatureToken(signatureServiceUrl);
            SignatureDocumentType doc = signatureServiceClient.downloadByDocumentId(
                    signatureServiceUrl,
                    signatureToken,
                    activityId,
                    documentId);
            File tmp = File.createTempFile("Attachment", null);
            FileUtils.copyToFile(doc.getContent().getInputStream(), tmp);
            return tmp;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    String getSignatureServiceProperty(String property) {
        return registerSignEndpointConfiguration.get(property);
    }


    String getSignatureToken(URL url) throws Exception {
        return signatureServiceClient.authenticate(
                url,
                getSignatureServiceProperty("operatorId"),
                getSignatureServiceProperty("operatorPassword"));
    }
}
