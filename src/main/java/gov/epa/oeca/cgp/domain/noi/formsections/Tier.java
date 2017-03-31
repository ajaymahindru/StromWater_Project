package gov.epa.oeca.cgp.domain.noi.formsections;

/**
 * @author dfladung
 */
public enum Tier {

    Tier_2("Tier 2"), Tier_2_5("Tier 2.5"), Tier_3("Tier 3"), NA("N/A");

    private final String label;

    Tier(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
