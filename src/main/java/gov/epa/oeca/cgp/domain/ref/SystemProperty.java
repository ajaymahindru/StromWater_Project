package gov.epa.oeca.cgp.domain.ref;

import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.*;

/**
 * Created by smckay on 3/6/2017.
 *
 * @author smckay
 */
@Entity
@Table(name = "cgp_system_properties",
        indexes = {@Index(name = "idx_system_prop", columnList =  "prop_key")})
public class SystemProperty extends BaseEntity {
    @Column(name = "prop_key", nullable = false)
    private String key;
    @Column(name = "prop_value", length = 4000, nullable = false)
    private String value;
    @Column(name = "description", length = 4000)
    private String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
