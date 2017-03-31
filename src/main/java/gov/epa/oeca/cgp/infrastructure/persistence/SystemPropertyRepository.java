package gov.epa.oeca.cgp.infrastructure.persistence;

import gov.epa.oeca.cgp.domain.ref.SystemProperty;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by smckay on 3/6/2017.
 *
 * @author smckay
 */
@Repository
public class SystemPropertyRepository extends BaseRepository<SystemProperty> {

    public SystemPropertyRepository() {
        super(SystemProperty.class);
    }

    public SystemProperty retrieve(String key) {
        List<SystemProperty> allProps = findAll();
        return (SystemProperty) DetachedCriteria.forClass(SystemProperty.class)
                .add(Restrictions.eq("key", key))
                .getExecutableCriteria(cgpSessionFactory.getCurrentSession()).uniqueResult();
    }
}
