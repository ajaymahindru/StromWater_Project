package gov.epa.oeca.cgp.domain.noi;

/**
 * @author dfladung
 */
public enum Status {
    // normal workflow
    Draft("Draft"), Submitted("Submitted"), Active("Active"), ActivePendingChange("Active-Pending Change"), Terminated("Terminated"), Discontinued("Discontinued"), Archived("Archived"),
    // EPA intervention
    OnHold("On Hold"), Denied("Denied");

    private final String label;

    Status(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
