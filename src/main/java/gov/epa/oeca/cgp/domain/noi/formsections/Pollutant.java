package gov.epa.oeca.cgp.domain.noi.formsections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.*;

/**
 * @author dfladung
 */
@Entity
@Table(name = "cgp_ref_pollutants",
        indexes = {@Index(name = "idx_pollutants", columnList = "pollutant_name, pollutant_srs_name")})
@JsonIgnoreProperties({"id"})
public class Pollutant extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "pollutant_code", nullable = false)
    Integer pollutantCode;
    @Column(name = "pollutant_name", nullable = false)
    String pollutantName;
    @Column(name = "pollutant_srs_name", length = 4000)
    String srsName;
    @Transient
    Boolean impaired;
    @Transient
    Tmdl tmdl;

    public Integer getPollutantCode() {
        return pollutantCode;
    }

    public void setPollutantCode(Integer pollutantCode) {
        this.pollutantCode = pollutantCode;
    }

    public String getPollutantName() {
        return pollutantName;
    }

    public void setPollutantName(String pollutantName) {
        this.pollutantName = pollutantName;
    }

    public Tmdl getTmdl() {
        return tmdl;
    }

    public void setTmdl(Tmdl tmdl) {
        this.tmdl = tmdl;
    }

    public Boolean getImpaired() {
        return impaired;
    }

    public void setImpaired(Boolean impaired) {
        this.impaired = impaired;
    }

    public String getSrsName() {
        return srsName;
    }

    public void setSrsName(String srsName) {
        this.srsName = srsName;
    }
}
