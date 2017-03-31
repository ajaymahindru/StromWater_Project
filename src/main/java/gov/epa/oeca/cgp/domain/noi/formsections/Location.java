package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.math.BigDecimal;

/**
 * @author dfladung
 */
public class Location extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    BigDecimal latitude;
    BigDecimal longitude;
    String latLongDataSource;
    String horizontalReferenceDatum;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        return new EqualsBuilder()
                .append(latitude, location.latitude)
                .append(longitude, location.longitude)
                .append(latLongDataSource, location.latLongDataSource)
                .append(horizontalReferenceDatum, location.horizontalReferenceDatum)
                .isEquals();
    }

    @Override
    public String toString() {
        return "Location{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", latLongDataSource='" + latLongDataSource + '\'' +
                ", horizontalReferenceDatum='" + horizontalReferenceDatum + '\'' +
                "} " + super.toString();
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public String getLatLongDataSource() {
        return latLongDataSource;
    }

    public void setLatLongDataSource(String latLongDataSource) {
        this.latLongDataSource = latLongDataSource;
    }

    public String getHorizontalReferenceDatum() {
        return horizontalReferenceDatum;
    }

    public void setHorizontalReferenceDatum(String horizontalReferenceDatum) {
        this.horizontalReferenceDatum = horizontalReferenceDatum;
    }
}
