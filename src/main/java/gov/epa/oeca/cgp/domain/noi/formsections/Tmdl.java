package gov.epa.oeca.cgp.domain.noi.formsections;

import gov.epa.oeca.common.domain.BaseValueObject;
import org.apache.commons.lang.builder.EqualsBuilder;

/**
 * @author dfladung
 */
public class Tmdl extends BaseValueObject {

    private static final long serialVersionUID = 1L;

    String id;
    String name;

    @Override
    public boolean sameValueAs(BaseValueObject o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Tmdl tmdl = (Tmdl) o;

        return new EqualsBuilder()
                .append(id, tmdl.id)
                .append(name, tmdl.name)
                .isEquals();
    }


    @Override
    public String toString() {
        return "Tmdl{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                "} " + super.toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
