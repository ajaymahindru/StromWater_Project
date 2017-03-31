package gov.epa.oeca.cgp.infrastructure.icis;

import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import gov.epa.oeca.common.domain.document.Document;
import gov.epa.oeca.common.infrastructure.node2.NetworkNode2Client;
import gov.epa.oeca.common.util.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author dfladung
 */
public class IcisSubmissionServiceImpl extends AbstractIcisSubmissionServiceImpl {

    private static final Logger logger = LoggerFactory.getLogger(IcisSubmissionServiceImpl.class);

    @Resource(name = "networkNode2EndpointConfiguration")
    Map<String, String> networkNode2EndpointConfiguration;
    @Autowired
    NetworkNode2Client client;

    @Override
    public CgpNoiForm submitToIcisNpdesDataflow(CgpNoiForm form, CgpNoiForm previous) throws ApplicationException {
        File xml = null;
        File zip = null;
        try {
            String npdesId = form.getFormSet().getNpdesId();
            xml = marshallForm(form, previous);
            zip = ZipUtil.zip(xml, npdesId + ".xml");

            URL endpoint = new URL(networkNode2EndpointConfiguration.get("serviceUrl"));
            String user = networkNode2EndpointConfiguration.get("operatorId");
            String credential = networkNode2EndpointConfiguration.get("operatorPassword");
            String token = client.authenticate(endpoint, user, credential, "default", "password");
            String txId = client.submit(endpoint, token, null, getIcisDataflow(), null,
                    asDocuments(npdesId, zip));

            form.setNodeTransactionId(txId);
            return form;
        } catch (Exception e) {
            FileUtils.deleteQuietly(zip);
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        } finally {
            FileUtils.deleteQuietly(xml);
        }
    }

    List<Document> asDocuments(String npdesId, File zip) {
        Document doc = new Document();
        doc.setName(npdesId + ".zip");
        doc.setContent(zip);
        return Collections.singletonList(doc);
    }
}
