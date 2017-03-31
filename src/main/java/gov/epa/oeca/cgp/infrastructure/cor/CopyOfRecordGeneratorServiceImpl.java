package gov.epa.oeca.cgp.infrastructure.cor;

import gov.epa.oeca.cgp.domain.noi.*;
import gov.epa.oeca.cgp.security.ApplicationSecurityUtils;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.infrastructure.cdx.pdf.RegisterPdfClient;
import gov.epa.oeca.common.security.ApplicationUser;
import net.exchangenetwork.wsdl.register.pdf._1.HandoffType;
import net.exchangenetwork.wsdl.register.pdf._1.HandoffUserType;
import net.exchangenetwork.wsdl.register.pdf._1.PdfDocumentType;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.time.ZonedDateTime;
import java.util.Map;

/**
 * @author dfladung
 */
public class CopyOfRecordGeneratorServiceImpl implements CopyOfRecordGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(CopyOfRecordGeneratorServiceImpl.class);

    @Autowired
    RegisterPdfClient pdfClient;
    @Resource(name = "registerPdfEndpointConfiguration")
    Map<String, String> registerPdfEndpointConfiguration;
    @Resource(name = "oecaGeneralConfiguration")
    Map<String, String> oecaGeneralConfiguration;
    @Autowired
    ApplicationSecurityUtils applicationSecurityUtils;

    @Override
    public Attachment generateCorFromForm(CgpNoiForm form) throws ApplicationException {
        try {
            // setup the call
            URL pdfServiceUrl = new URL(getPdfServiceProperty("serviceUrl"));
            String pdfToken = getPdfServiceToken(pdfServiceUrl);
            String formType = null;
            if(form.getPhase() == Phase.Terminate) {
                formType = "not";
            }
            else {
                formType = FormType.Notice_Of_Intent.equals(form.getType()) ? "noi" : "lew";
            }
            String targetUrl = String.format("%s?formType=%s&formId=%s&npdesId=%s",
                    oecaGeneralConfiguration.get("corUrl"), formType, form.getId(), form.getFormSet().getNpdesId());
            HandoffUserType handoffUser = new HandoffUserType();
            ApplicationUser user = applicationSecurityUtils.getCurrentApplicationUser();
            BeanUtils.copyProperties(user, handoffUser);
            handoffUser.setUserId(user.getUsername());
            PdfDocumentType result = pdfClient.renderUrlAsPdf(pdfServiceUrl, pdfToken,
                    targetUrl, HandoffType.RSO, handoffUser);
            // build an attachment from the result
            File tmp = File.createTempFile("CgpCoR", ".pdf");
            FileUtils.copyToFile(result.getContent().getInputStream(), tmp);
            Attachment cor = new Attachment();
            cor.setCategory(AttachmentCategory.CoR);
            cor.setData(tmp);
            cor.setName(form.getFormSet().getNpdesId() + "CopyOfRecord.pdf");
            cor.setCreatedDate(ZonedDateTime.now());
            cor.setForm(form);
            return cor;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    String getPdfServiceProperty(String property) {
        return registerPdfEndpointConfiguration.get(property);
    }


    String getPdfServiceToken(URL url) throws Exception {
        return pdfClient.authenticate(
                url,
                getPdfServiceProperty("operatorId"),
                getPdfServiceProperty("operatorPassword"));
    }
}
