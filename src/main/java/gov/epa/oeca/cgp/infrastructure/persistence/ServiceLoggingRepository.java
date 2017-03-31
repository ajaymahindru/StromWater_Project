package gov.epa.oeca.cgp.infrastructure.persistence;

import gov.epa.oeca.cgp.domain.ServiceEvent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @author ktucker
 */
@Repository
public class ServiceLoggingRepository {

    @Autowired
    SessionFactory cgpSessionFactory;

    public void createServiceEvent(ServiceEvent event) {
        Session session = cgpSessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            session.save(event);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            session.close();
        }
    }

}
