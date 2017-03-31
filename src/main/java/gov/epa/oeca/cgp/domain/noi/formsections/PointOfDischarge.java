package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author dfladung
 */
public class PointOfDischarge extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String id;
    String description;
    Location location;
    ReceivingWater firstWater;
    Tier tier;
    Boolean impaired;
    Boolean tmdlCompleted;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        PointOfDischarge that = (PointOfDischarge) o;

        return new EqualsBuilder()
                .append(id, that.id)
                .append(description, that.description)
                .append(location, that.location)
                .append(firstWater, that.firstWater)
                .append(tier, that.tier)
                .append(impaired, that.impaired)
                .append(tmdlCompleted, that.tmdlCompleted)
                .isEquals();
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ReceivingWater getFirstWater() {
        return firstWater;
    }

    public void setFirstWater(ReceivingWater firstWater) {
        this.firstWater = firstWater;
    }

    public Tier getTier() {
        return tier;
    }

    public void setTier(Tier tier) {
        this.tier = tier;
    }

    public Boolean getImpaired() {
        return impaired;
    }

    public void setImpaired(Boolean impaired) {
        this.impaired = impaired;
    }

    public Boolean getTmdlCompleted() {
        return tmdlCompleted;
    }

    public void setTmdlCompleted(Boolean tmdlCompleted) {
        this.tmdlCompleted = tmdlCompleted;
    }
}
