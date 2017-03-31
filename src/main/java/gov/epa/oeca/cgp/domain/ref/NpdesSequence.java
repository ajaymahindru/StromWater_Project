package gov.epa.oeca.cgp.domain.ref;

import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author dfladung
 */
@Entity
@Table(name = "cgp_ref_npdes_seq",
        indexes = {@Index(name = "idx_npdes_seq", columnList = "mgp_num")})
public class NpdesSequence extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "mgp_num", length = 9, nullable = false)
    String mgpNumber;

    @Column(name = "npdes_alpha_start", length = 3, nullable = false)
    String npdesAlphaStart;

    public static String incrementNpdesSeq(char[] str) {
        for (int pos = str.length - 1; pos >= 0; pos--) {
            if (Character.toUpperCase(str[pos]) != 'Z') {
                if (Character.toUpperCase(str[pos]) == '9') {
                    str[pos] = 'A';
                } else {
                    str[pos]++;
                }
                break;
            } else
                str[pos] = '0';
        }
        return new String(str);
    }

    @Override
    public String toString() {
        return "NpdesSequence{" +
                "mgpNumber='" + mgpNumber + '\'' +
                ", npdesAlphaStart='" + npdesAlphaStart + '\'' +
                "} " + super.toString();
    }

    public String getMgpNumber() {
        return mgpNumber;
    }

    public void setMgpNumber(String mgpNumber) {
        this.mgpNumber = mgpNumber;
    }

    public String getNpdesAlphaStart() {
        return npdesAlphaStart;
    }

    public void setNpdesAlphaStart(String npdesAlphaStart) {
        this.npdesAlphaStart = npdesAlphaStart;
    }
}
