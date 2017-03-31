package gov.epa.oeca.cgp.infrastructure.cor;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.oeca.cgp.domain.noi.Attachment;
import gov.epa.oeca.cgp.domain.noi.AttachmentCategory;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.common.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.ZonedDateTime;

/**
 * @author dfladung
 */
public class MockCopyOfRecordGeneratorServiceImpl implements CopyOfRecordGeneratorService {

    private static final Logger logger = LoggerFactory.getLogger(MockCopyOfRecordGeneratorServiceImpl.class);

    @Override
    public Attachment generateCorFromForm(CgpNoiForm form) throws ApplicationException {
        try {
            File tmp = File.createTempFile("CgpCoR", ".json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.findAndRegisterModules();
            mapper.writeValue(tmp, form);
            Attachment cor = new Attachment();
            cor.setCategory(AttachmentCategory.CoR);
            cor.setData(tmp);
            cor.setName(form.getFormSet().getNpdesId() + "CopyOfRecord.json");
            cor.setCreatedDate(ZonedDateTime.now());
            cor.setForm(form);
            return cor;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
