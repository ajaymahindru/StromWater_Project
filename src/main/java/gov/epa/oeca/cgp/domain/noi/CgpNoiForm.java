package gov.epa.oeca.cgp.domain.noi;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.epa.oeca.cgp.domain.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author dfladung
 */
@Entity
@Table(name = "cgp_noi_forms", indexes = {
        @Index(name = "idx_forms_formsets_fk", columnList = "form_set_id"),
        @Index(name = "idx_forms_index_fk", columnList = "index_id")
})
public class CgpNoiForm extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "form_set_id", nullable = false)
    CgpNoiFormSet formSet;

    @Column(name = "created_date", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime createdDate;

    @Column(name = "review_expiration_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime reviewExpiration;

    @Column(name = "certified_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime certifiedDate;

    @Column(name = "submitted_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime submittedDate;

    @Column(name = "last_updated_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    ZonedDateTime lastUpdatedDate;

    @Column(name = "active_record", length = 1, nullable = false)
    @Type(type = "yes_no")
    Boolean activeRecord;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Phase phase;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Status status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    FormType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    Source source;

    @Column(name = "tracking_number", nullable = false)
    String trackingNumber;

    @Column(name = "cromerr_activity_id")
    String cromerrActivityId;

    @Column(name = "node_transaction_id")
    String nodeTransactionId;

    @Column
    @Type(
            type = "gov.epa.oeca.cgp.infrastructure.persistence.JsonBlobType",
            parameters = {@Parameter(name = "classType", value = "gov.epa.oeca.cgp.domain.noi.CgpNoiFormData")}
    )
    CgpNoiFormData formData;

    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "form")
    @Fetch(value = FetchMode.SUBSELECT)
    List<Attachment> attachments;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "index_id")
    @JsonIgnore
    CgpNoiFormDataIndex index;

    public CgpNoiForm() {
        formSet = new CgpNoiFormSet();
        formData = new CgpNoiFormData();
        index = new CgpNoiFormDataIndex();
        attachments = new ArrayList<>();
    }

    public CgpNoiFormSet getFormSet() {
        return formSet;
    }

    public void setFormSet(CgpNoiFormSet formSet) {
        this.formSet = formSet;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCromerrActivityId() {
        return cromerrActivityId;
    }

    public void setCromerrActivityId(String cromerrActivityId) {
        this.cromerrActivityId = cromerrActivityId;
    }

    public String getNodeTransactionId() {
        return nodeTransactionId;
    }

    public void setNodeTransactionId(String nodeTransactionId) {
        this.nodeTransactionId = nodeTransactionId;
    }

    public FormType getType() {
        return type;
    }

    public void setType(FormType type) {
        this.type = type;
    }

    public CgpNoiFormData getFormData() {
        return formData;
    }

    public void setFormData(CgpNoiFormData formData) {
        this.formData = formData;
    }

    public CgpNoiFormDataIndex getIndex() {
        return index;
    }

    public void setIndex(CgpNoiFormDataIndex index) {
        this.index = index;
    }

    public List<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<Attachment> attachments) {
        this.attachments = attachments;
    }

    public ZonedDateTime getReviewExpiration() {
        return reviewExpiration;
    }

    public void setReviewExpiration(ZonedDateTime reviewExpiration) {
        this.reviewExpiration = reviewExpiration;
    }

    public ZonedDateTime getCertifiedDate() {
        return certifiedDate;
    }

    public void setCertifiedDate(ZonedDateTime certifiedDate) {
        this.certifiedDate = certifiedDate;
    }

    public ZonedDateTime getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(ZonedDateTime submittedDate) {
        this.submittedDate = submittedDate;
    }

    public ZonedDateTime getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(ZonedDateTime lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public Source getSource() {
        return source;
    }

    public void setSource(Source source) {
        this.source = source;
    }

    public Boolean getActiveRecord() {
        return activeRecord;
    }

    public void setActiveRecord(Boolean activeRecord) {
        this.activeRecord = activeRecord;
    }

    public Phase getPhase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }
}
