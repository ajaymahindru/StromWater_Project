package gov.epa.oeca.cgp.domain.dto;

import java.io.Serializable;

/**
 * @author dfladung
 */
public class CgpNoiFormSetSearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    String owner;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        return "CgpNoiFormSetSearchCriteria{" +
                "owner='" + owner + '\'' +
                "}";
    }
}
