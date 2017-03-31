package gov.epa.oeca.cgp.infrastructure.persistence;

import gov.epa.oeca.cgp.domain.BaseEntity;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.LobHandler;

import java.util.List;

/**
 * @author dfladung
 */
public abstract class BaseRepository<T extends BaseEntity> {

    @Autowired
    protected SessionFactory cgpSessionFactory;

    @Autowired
    protected JdbcTemplate jdbcTemplate;

    @Autowired
    protected LobHandler lobHandler;

    private Class<T> clazz;

    public BaseRepository(Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("unchecked")
    public T find(Long id) {
        return (T) cgpSessionFactory.getCurrentSession().get(clazz, id);
    }

    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        return cgpSessionFactory.getCurrentSession().createQuery("from " + clazz.getName()).list();
    }

    public Long save(T entity) {
        cgpSessionFactory.getCurrentSession().save(entity); // save instead of saveOrUpdate: hibernate bug HHH-6776
        return entity.getId();
    }

    public void update(T entity) {
        cgpSessionFactory.getCurrentSession().merge(entity);
    }

    public void delete(T entity) {
        cgpSessionFactory.getCurrentSession().delete(entity);
    }
}
