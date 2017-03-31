package gov.epa.oeca.cgp.domain.ref;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
@Entity
@Table(name = "cgp_ref_subscribers",
        indexes = {@Index(name = "idx_subscribers", columnList = "subscriber_email")})
public class Subscriber extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "subscriber_email", nullable = false)
    String email;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "description")
    String description;

    @JsonIgnore
    @ManyToMany(mappedBy = "subscribers")
    List<Notification> notifications;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }
}
