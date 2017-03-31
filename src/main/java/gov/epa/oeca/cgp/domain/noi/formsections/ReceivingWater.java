package gov.epa.oeca.cgp.domain.noi.formsections;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author dfladung
 */
@Entity
@Table(name = "cgp_ref_waters",
        indexes = {@Index(name = "idx_waters", columnList = "receiving_water_name")})
@JsonIgnoreProperties({"id"})
public class ReceivingWater extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "receiving_water_id", nullable = false)
    String receivingWaterId;
    @Column(name = "receiving_water_name", nullable = false)
    String receivingWaterName;
    @Transient
    List<Pollutant> pollutants;

    public ReceivingWater() {
        pollutants = new ArrayList<>();
    }


    public String getReceivingWaterId() {
        return receivingWaterId;
    }

    public void setReceivingWaterId(String receivingWaterId) {
        this.receivingWaterId = receivingWaterId;
    }

    public String getReceivingWaterName() {
        return receivingWaterName;
    }

    public void setReceivingWaterName(String receivingWaterName) {
        this.receivingWaterName = receivingWaterName;
    }

    public List<Pollutant> getPollutants() {
        return pollutants;
    }

    public void setPollutants(List<Pollutant> pollutants) {
        this.pollutants = pollutants;
    }


}
