package gov.epa.oeca.cgp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Entity
@Table(name = "cgp_service_event")
public class ServiceEvent extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final SimpleDateFormat SDF = new SimpleDateFormat();

	@Column(name = "user_id", nullable = false)
	protected String userId;

    @Column(name = "form_id")
    protected Long formId;

	@Column(name = "service", nullable = false)
	protected String service;

	@Column(name = "method", nullable = false)
	protected String method;

	@Column(name = "status", nullable = false)
	protected String status;

	@Column(name = "error_code")
	protected String errorCode;

	@Column(name = "error_message", length = 4000)
	protected String errorMessage;

	@Column(name = "created_date", nullable = false)
	protected Date createdDate;

	public ServiceEvent() {
		this.createdDate = new Date();
	}

	public ServiceEvent(String userId, String service, String method) {
		this();
		this.userId = userId;
		this.service = service;
		this.method = method;
	}

	@Override
	public String toString() {
		return String
				.format("ServiceEvent{id=%s, userId=%s, service=%s, method=%s, status=%s, errorCode=%s, errorMessage=%s, createdDate=%s}",
						id, userId, service, method, status, errorCode, errorMessage, SDF.format(createdDate));
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

    public Long getFormId() {
        return formId;
    }

    public void setFormId(Long formId) {
        this.formId = formId;
    }

    public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
