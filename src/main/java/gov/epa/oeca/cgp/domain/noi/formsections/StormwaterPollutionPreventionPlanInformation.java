package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.cgp.domain.Contact;
import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * Maps to Stormwater Pollution Prevention Plan (SWPPP) Information.
 *
 * @author dfladung
 */
public class StormwaterPollutionPreventionPlanInformation extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    Boolean preparationInAdvance;
    Contact contactInformation;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        StormwaterPollutionPreventionPlanInformation that = (StormwaterPollutionPreventionPlanInformation) o;

        return new EqualsBuilder()
                .append(preparationInAdvance, that.preparationInAdvance)
                .append(contactInformation, that.contactInformation)
                .isEquals();
    }

    @Override
    public String toString() {
        return "SwwppInformation{" +
                "preparationInAdvance=" + preparationInAdvance +
                ", contactInformation=" + contactInformation +
                "} " + super.toString();
    }

    public Boolean getPreparationInAdvance() {
        return preparationInAdvance;
    }

    public void setPreparationInAdvance(Boolean preparationInAdvance) {
        this.preparationInAdvance = preparationInAdvance;
    }

    public Contact getContactInformation() {
        return contactInformation;
    }

    public void setContactInformation(Contact contactInformation) {
        this.contactInformation = contactInformation;
    }
}
