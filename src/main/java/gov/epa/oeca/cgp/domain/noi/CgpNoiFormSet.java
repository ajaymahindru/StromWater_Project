package gov.epa.oeca.cgp.domain.noi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.epa.oeca.cgp.domain.BaseEntity;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dfladung
 */
@Entity
@Table(name = "cgp_noi_form_sets")
public class CgpNoiFormSet extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "master_permit_number", nullable = false)
    String masterPermitNumber;

    @Column(name = "npdes_id")
    String npdesId;

    @Column(nullable = false)
    String owner;

    @OneToMany(cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY, mappedBy = "formSet")
    @Fetch(value = FetchMode.SUBSELECT)
    @JsonIgnore
    List<CgpNoiForm> forms;


    public CgpNoiFormSet() {
        forms = new ArrayList<>();
    }

    public CgpNoiFormSet(String masterPermitNumber, String owner) {
        this.masterPermitNumber = masterPermitNumber;
        this.owner = owner;
        forms = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "CgpNoiFormSet{" +
                "masterPermitNumber='" + masterPermitNumber + '\'' +
                ", npdesId='" + npdesId + '\'' +
                ", owner='" + owner + '\'' +
                "} " + super.toString();
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getMasterPermitNumber() {
        return masterPermitNumber;
    }

    public void setMasterPermitNumber(String masterPermitNumber) {
        this.masterPermitNumber = masterPermitNumber;
    }

    public String getNpdesId() {
        return npdesId;
    }

    public void setNpdesId(String npdesId) {
        this.npdesId = npdesId;
    }

    public List<CgpNoiForm> getForms() {
        return forms;
    }

    public void setForms(List<CgpNoiForm> forms) {
        this.forms = forms;
    }
}
