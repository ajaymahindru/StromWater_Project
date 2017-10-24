package gov.epa.oeca.cgp.infrastructure.persistence;


import gov.epa.oeca.cgp.domain.noi.formsections.Pollutant;
import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.cgp.domain.ref.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import javax.persistence.LockModeType;

/**
 * @author dfladung
 */
@Repository
public class ReferenceRepository {

    @Autowired
    SessionFactory cgpSessionFactory;
    
    @Autowired
    JdbcTemplate jdbcTemplate;

    public TribalOverride retrieveOverride(String tribalCode, String stateCode) {
        return (TribalOverride) cgpSessionFactory.getCurrentSession()
                .createQuery("from TribalOverride where tribalCode = ? and stateCode = ?")
                .setParameter(0, tribalCode).setParameter(1, stateCode).uniqueResult();
    }

    public MgpRule retrieveRule(String stateCode) {
        return (MgpRule) cgpSessionFactory.getCurrentSession()
                .createQuery("from MgpRule where stateCode = ?")
                .setParameter(0, stateCode).uniqueResult();
    }
    
    public String generateNpdesId(String masterPermitNumber) {
    	Session session = cgpSessionFactory.getCurrentSession();    	
    	// Retrieve current sequence with FOR UPDATE pessimistic lock
    	NpdesSequence seq = (NpdesSequence) session.createQuery("from NpdesSequence where mgpNumber = ?")
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .setParameter(0, masterPermitNumber).uniqueResult();
    	session.refresh(seq);
    	// Calculate next id
        String result = masterPermitNumber.substring(0, masterPermitNumber.length() - 3) + seq.getNpdesAlphaStart();
        char[] str = seq.getNpdesAlphaStart().toCharArray();
        seq.setNpdesAlphaStart(NpdesSequence.incrementNpdesSeq(str));    	
        // Update sequence in db
    	session.update(seq);
    	session.flush();
    	return result;
    }

    @SuppressWarnings("unchecked")
    public List<State> retrieveStates() {
        return (List<State>) cgpSessionFactory.getCurrentSession().createQuery("from State where stateUsage = 'B' order by stateName").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<State> retrieveStates(Integer region) {
        String regionCode = region < 10 ? "0" + region.toString() : region.toString();
        return (List<State>) cgpSessionFactory.getCurrentSession()
                .createQuery("from State where stateUsage = 'B' and regionCode = ? order by stateName")
                .setParameter(0, regionCode)
                .getResultList();
    }

    public State retrieveState(String stateCode) {
        return (State) cgpSessionFactory.getCurrentSession()
                .createQuery("from State where stateCode = ?")
                .setParameter(0, stateCode)
                .getSingleResult();

    }

    @SuppressWarnings("unchecked")
    public List<County> retrieveCounties() {
        return (List<County>) cgpSessionFactory.getCurrentSession().createQuery("from County order by countyName").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Tribe> retrieveTribes() {
        return (List<Tribe>) cgpSessionFactory.getCurrentSession().createQuery("from Tribe order by tribalName").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Tribe> retrieveTribes(String stateCode) {
        return (List<Tribe>) cgpSessionFactory.getCurrentSession()
                .createQuery("select t from Tribe t join t.states ts where ts.stateCode = ? order by t.tribalName")
                .setParameter(0, stateCode)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Tribe> retrieveTribesByLandName(String tribalLandName) {
        return (List<Tribe>) cgpSessionFactory.getCurrentSession()
                .createQuery("from Tribe t where t.tribalName = ? order by t.tribalName")
                .setParameter(0, tribalLandName)
                .getResultList();
    }

    public Tribe retrieveTribeByLandNameAndStateCode(String tribalLandName, String stateCode) {
        return (Tribe) cgpSessionFactory.getCurrentSession()
                .createQuery("select t from Tribe t join t.states ts where t.tribalName = ? and ts.stateCode = ? order by t.tribalName")
                .setParameter(0, tribalLandName)
                .setParameter(1, stateCode)
                .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<BiaTribe> retrieveBiaTribes() {
        return (List<BiaTribe>) cgpSessionFactory.getCurrentSession()
                .createQuery("from BiaTribe")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<BiaTribe> retrieveBiaTribes(String stateCode) {
        return (List<BiaTribe>) cgpSessionFactory.getCurrentSession()
                .createQuery("from BiaTribe where stateCode = ?")
                .setParameter(0, stateCode)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<County> retrieveCounties(String stateCode) {
        return (List<County>) cgpSessionFactory.getCurrentSession()
                .createQuery("from County where stateCode = ?")
                .setParameter(0, stateCode)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Chemical> retrieveActiveChemicals() {
        return (List<Chemical>) cgpSessionFactory.getCurrentSession()
                .createQuery("from Chemical where status = 'A'")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Chemical> retrieveActiveChemicals(String name) {
        return (List<Chemical>) cgpSessionFactory.getCurrentSession()
                .createQuery("from Chemical where status = 'A' and lower(name) like lower(:chemName)")
                .setParameter("chemName", "%" + name + "%")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Pollutant> retrievePollutants(String name) {
        return (List<Pollutant>) cgpSessionFactory.getCurrentSession()
                .createQuery("from Pollutant where lower(pollutantName) like lower(:pollutantName) or lower(srsName) like lower(:srsName)")
                .setParameter("pollutantName", "%" + name.toLowerCase() + "%")
                .setParameter("srsName", "%" + name.toLowerCase() + "%")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<ReceivingWater> retrieveReceivingWaters(String name) {
        return (List<ReceivingWater>) cgpSessionFactory.getCurrentSession()
                .createQuery("from ReceivingWater where lower(receivingWaterName) like lower(:receivingWaterName)")
                .setParameter("receivingWaterName", "%" + name.toLowerCase() + "%")
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Subscriber> retrieveSubscribers() {
        return (List<Subscriber>) cgpSessionFactory.getCurrentSession().createQuery("from Subscriber").getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Subscriber> retrieveSubscribersBySubcategory(String subcategory) {
        return (List<Subscriber>) cgpSessionFactory.getCurrentSession()
                .createQuery("select s from Subscriber s join s.notifications sn where sn.subcategory = ?")
                .setParameter(0, subcategory)
                .getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<Subscriber> retrieveSubscribersByCatSubcat(String category, String subcategory) {
        return (List<Subscriber>) cgpSessionFactory.getCurrentSession()
                .createQuery("select s from Subscriber s join s.notifications sn where sn.category = ? and sn.subcategory = ?")
                .setParameter(0, category).setParameter(1, subcategory)
                .getResultList();
    }
}
