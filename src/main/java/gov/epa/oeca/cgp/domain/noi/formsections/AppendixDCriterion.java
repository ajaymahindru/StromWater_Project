package gov.epa.oeca.cgp.domain.noi.formsections;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 * @version gov.epa.oeca.cgp.domain.noi.formsections, 1/27/2017 2:04 PM by labieva
 */
public enum AppendixDCriterion {

    Criterion_A("A"), Criterion_B("B"), Criterion_C("C"),
    Criterion_D("D"), Criterion_E("E"), Criterion_F("F");

    private final String label;

    AppendixDCriterion(String label) {
        this.label = label;
    }

    public String getValue() {
        return label;
    }
}
