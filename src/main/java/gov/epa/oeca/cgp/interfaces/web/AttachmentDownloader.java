package gov.epa.oeca.cgp.interfaces.web;

import gov.epa.oeca.cgp.application.CgpNoiFormService;
import gov.epa.oeca.cgp.domain.noi.Attachment;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.integration.spring.SpringBean;

import java.io.File;

/**
 * Created by smckay on 1/9/2017.
 */
@UrlBinding("/action/secured/attachment/{id}")
public class AttachmentDownloader extends BaseCgpAction {
    static final String OCTET_MIME_TYPE = "application/octet-stream";

    @SpringBean
    CgpNoiFormService cgpNoiFormService;

    private Long id;

    @DefaultHandler
    public Resolution download() {
        Attachment attachment = cgpNoiFormService.retrieveAttachment(id);
        File file = cgpNoiFormService.retrieveAttachmentData(id);
        return new DocumentStreamingResolution(OCTET_MIME_TYPE, attachment.getName(), file.toURI());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
