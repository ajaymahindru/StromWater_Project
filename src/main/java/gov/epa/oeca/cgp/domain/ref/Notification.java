package gov.epa.oeca.cgp.domain.ref;

import gov.epa.oeca.cgp.domain.BaseEntity;

import javax.persistence.*;
import java.util.List;

/**
 * @author Linera Abieva (linera.abieva@cgifederal.com)
 */
@Entity
@Table(name = "cgp_ref_notifications",
        indexes = {@Index(name = "idx_notifications", columnList = "subcategory")})
public class Notification extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "category", nullable = false)
    String category;

    @Column(name = "subcategory")
    String subcategory;

    @Column(name = "subcategory_desc")
    String subDescription;

    @ManyToMany
    @JoinTable(
            name = "cgp_ref_subscriber_notif",
            joinColumns = @JoinColumn(name = "notification_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "subscriber_id", referencedColumnName = "id"),
            uniqueConstraints = {@UniqueConstraint(name = "idx_subscriber_notification", columnNames = {"subscriber_id", "notification_id"})})
    List<Subscriber> subscribers;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getSubDescription() {
        return subDescription;
    }

    public void setSubDescription(String subDescription) {
        this.subDescription = subDescription;
    }

    public List<Subscriber> getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(List<Subscriber> subscribers) {
        this.subscribers = subscribers;
    }
}
