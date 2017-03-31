package gov.epa.oeca.cgp.domain.dto.publik;

import com.fasterxml.jackson.annotation.JsonFormat;
import gov.epa.oeca.cgp.domain.noi.AttachmentCategory;
import gov.epa.oeca.common.domain.BaseValueObject;

import java.time.ZonedDateTime;

/**
 * @author dfladung
 */
public class PublicAttachment extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Long id;
    String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime createdDate;
    AttachmentCategory category;
    Long size;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public AttachmentCategory getCategory() {
        return category;
    }

    public void setCategory(AttachmentCategory category) {
        this.category = category;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }
}
