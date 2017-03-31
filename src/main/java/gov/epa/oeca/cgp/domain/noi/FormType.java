package gov.epa.oeca.cgp.domain.noi;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author dfladung
 */
public enum FormType {

    Notice_Of_Intent("Notice of Intent"),
    Low_Erosivity_Waiver("Low Erosivity Waiver");

    private final String label;

    FormType(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
