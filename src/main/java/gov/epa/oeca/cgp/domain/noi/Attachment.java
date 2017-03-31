package gov.epa.oeca.cgp.domain.noi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.*;
import java.io.File;
import java.time.ZonedDateTime;

/**
 * @author dfladung
 */

@Entity
@Table(name = "cgp_noi_form_attachments", indexes = {@Index(name = "idx_attachments_forms_fk", columnList = "form_id")})
public class Attachment extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    String name;

    @Column(name = "created_date", nullable = false)
    ZonedDateTime createdDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    AttachmentCategory category;

    @Transient
    File data;

    @Column(name = "cromerr_attachment_id")
    String cromerrAttachmentId;

    @Column(name = "attachment_size")
    Long size;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "form_id", nullable = false)
    @JsonIgnore
    CgpNoiForm form;

    public Attachment() {
    }

    public Attachment(CgpNoiForm form, String name, AttachmentCategory category, File data, ZonedDateTime createdDate) {
        this.name = name;
        this.createdDate = createdDate;
        this.category = category;
        this.data = data;
        this.form = form;
        if (data != null && data.exists()){
            this.size = data.length();
        }
    }

    @Override
    public String toString() {
        return "Attachment{" +
                "name='" + name + '\'' +
                ", createdDate=" + createdDate +
                ", category=" + category +
                ", cromerrAttachmentId='" + cromerrAttachmentId + '\'' +
                ", form=" + form +
                "} " + super.toString();
    }

    public CgpNoiForm getForm() {
        return form;
    }

    public void setForm(CgpNoiForm form) {
        this.form = form;
    }

    public String getCromerrAttachmentId() {
        return cromerrAttachmentId;
    }

    public void setCromerrAttachmentId(String cromerrAttachmentId) {
        this.cromerrAttachmentId = cromerrAttachmentId;
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

    public File getData() {
        return data;
    }

    public void setData(File data) {
        this.data = data;
        if (data != null && data.exists()){
            this.size = data.length();
        }
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
