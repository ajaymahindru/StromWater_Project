package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.builder.EqualsBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dfladung
 */
public class DischargeInformation extends BaseValueObject {

    private static final long serialVersionUID = 1L;
    Boolean dischargeAllowable;
    Boolean dischargeMunicipalSeparateStormSewerSystem;
    Boolean dischargeUSWatersWithin50Feet;
    List<PointOfDischarge> dischargePoints;

    public DischargeInformation() {
        dischargePoints = new ArrayList<>();
    }

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        DischargeInformation that = (DischargeInformation) o;

        return new EqualsBuilder()
                .append(dischargeAllowable, that.dischargeAllowable)
                .append(dischargeMunicipalSeparateStormSewerSystem, that.dischargeMunicipalSeparateStormSewerSystem)
                .append(dischargeUSWatersWithin50Feet, that.dischargeUSWatersWithin50Feet)
                .append(dischargePoints, that.dischargePoints)
                .isEquals();
    }

    @Override
    public String toString() {
        return "DischargeInformation{" +
                "dischargeAllowable=" + dischargeAllowable +
                ", dischargeMunicipalSeparateStormSewerSystem=" + dischargeMunicipalSeparateStormSewerSystem +
                ", dischargeUSWatersWithin50Feet=" + dischargeUSWatersWithin50Feet +
                "} " + super.toString();
    }

    public Boolean hasNewReceivingWater(DischargeInformation previous) {
        if (!CollectionUtils.isEmpty(this.getDischargePoints()) && !CollectionUtils.isEmpty(previous.getDischargePoints())) {
            for (PointOfDischarge pod : this.getDischargePoints()) {
                if (!CollectionUtils.exists(previous.getDischargePoints(), o -> {
                    PointOfDischarge target = (PointOfDischarge) o;
                    ReceivingWater rw = target.getFirstWater();
                    return rw.getReceivingWaterName().equals(pod.getFirstWater().getReceivingWaterName());
                })) {
                    return true;
                }
            }
        }
        return false;
    }

    public Boolean getDischargeAllowable() {
        return dischargeAllowable;
    }

    public void setDischargeAllowable(Boolean dischargeAllowable) {
        this.dischargeAllowable = dischargeAllowable;
    }

    public Boolean getDischargeMunicipalSeparateStormSewerSystem() {
        return dischargeMunicipalSeparateStormSewerSystem;
    }

    public void setDischargeMunicipalSeparateStormSewerSystem(Boolean dischargeMunicipalSeparateStormSewerSystem) {
        this.dischargeMunicipalSeparateStormSewerSystem = dischargeMunicipalSeparateStormSewerSystem;
    }

    public Boolean getDischargeUSWatersWithin50Feet() {
        return dischargeUSWatersWithin50Feet;
    }

    public void setDischargeUSWatersWithin50Feet(Boolean dischargeUSWatersWithin50Feet) {
        this.dischargeUSWatersWithin50Feet = dischargeUSWatersWithin50Feet;
    }

    public List<PointOfDischarge> getDischargePoints() {
        return dischargePoints;
    }

    public void setDischargePoints(List<PointOfDischarge> dischargePoints) {
        this.dischargePoints = dischargePoints;
    }
}
