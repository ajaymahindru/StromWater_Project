package gov.epa.oeca.cgp.domain.ref;

import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author dfladung
 */
@Entity
@Table(name = "cgp_ref_tribal_overrides",
        indexes = {@Index(name = "idx_overrides", columnList = "tribal_code, state_code")})
public class TribalOverride extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "tribal_code", nullable =  false)
    String tribalCode;

    @Column(name = "state_code", length = 2, nullable = false)
    String stateCode;

    @Column(name = "mgp_number")
    String mgpNumber;


    @Override
    public String toString() {
        return "TribalOverride{" +
                "tribalCode='" + tribalCode + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", mgpNumber='" + mgpNumber + '\'' +
                "} " + super.toString();
    }

    public String getTribalCode() {
        return tribalCode;
    }

    public void setTribalCode(String tribalCode) {
        this.tribalCode = tribalCode;
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getMgpNumber() {
        return mgpNumber;
    }

    public void setMgpNumber(String mgpNumber) {
        this.mgpNumber = mgpNumber;
    }
}
