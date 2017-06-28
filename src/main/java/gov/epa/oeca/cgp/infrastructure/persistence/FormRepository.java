package gov.epa.oeca.cgp.infrastructure.persistence;

import gov.epa.oeca.cgp.application.ApplicationUtils;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchCriteria;
import gov.epa.oeca.cgp.domain.dto.CgpNoiFormSearchResult;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableConfig;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableCriteria;
import gov.epa.oeca.cgp.domain.dto.datatable.DataTableOrder;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.ref.State;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.criterion.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.toIntExact;

/**
 * @author dfladung
 */
@Repository
public class FormRepository extends BaseRepository<CgpNoiForm> {

    private static final Log logger = LogFactory.getLog(FormRepository.class);

    protected static final String RESULT_COUNT_ERROR = "Search criteria returned too many records. Please refine your search.";

    @Autowired
    ApplicationUtils applicationUtils;
    @Autowired
    ReferenceRepository referenceRepository;

    public FormRepository() {
        super(CgpNoiForm.class);
    }

    @SuppressWarnings("unchecked")
    public List<CgpNoiForm> findAll(CgpNoiFormSearchCriteria criteria) {
        DetachedCriteria cr = createCriteria(criteria);
        //order by lastUpdatedDate
        cr.addOrder(Order.desc("lastUpdatedDate"));

        //incur a result limit if specified
        if (criteria.getResultLimit() != null) {
            DetachedCriteria countCr = createCriteria(criteria);
            countCr.setProjection(Projections.countDistinct("id"));
            List<Long> count = (List<Long>) countCr.getExecutableCriteria(cgpSessionFactory.getCurrentSession()).list();
            Long countLimit = criteria.getResultLimit();
            if (count.get(0) > countLimit) {
                throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, RESULT_COUNT_ERROR);
            }
        }

        return cr.getExecutableCriteria(cgpSessionFactory.getCurrentSession()).list();
    }

    @SuppressWarnings("unchecked")
    public CgpNoiFormSearchResult findPaginated(DataTableCriteria<CgpNoiFormSearchCriteria> criteria) throws Exception {
        try {
            CgpNoiFormSearchCriteria sc = criteria.getCriteria();
            DataTableConfig conf = criteria.getConfig();

            DetachedCriteria dcr = createCriteria(sc);
            Criteria cr = dcr.getExecutableCriteria(cgpSessionFactory.getCurrentSession());

            if (conf != null) {
                //ordering
                if (conf.getOrder() != null && !conf.getOrder().isEmpty()) {
                    for (DataTableOrder order : conf.getOrder()) {
                        //figure out the column
                        String columnName = conf.getColumns().get(order.getColumn()).getName();
                        if (columnName != null && !columnName.isEmpty()) {
                            if (DataTableOrder.DataTablesOrderEnum.asc.equals(order.getDir())) {
                                dcr.addOrder(Order.asc(columnName));
                            } else {
                                dcr.addOrder(Order.desc(columnName));
                            }
                        }
                    }
                }

                //pagination
                if (conf.getStart() != null && conf.getLength() != null) {
                    int maxResults = toIntExact(conf.getLength());
                    int firstResult = toIntExact(conf.getStart());
                    cr = dcr.getExecutableCriteria(cgpSessionFactory.getCurrentSession())
                            .setMaxResults(maxResults)
                            .setFirstResult(firstResult);
                }
            } else {
                //order by lastUpdatedDate
                cr.addOrder(Order.desc("lastUpdatedDate"));
            }

            List<CgpNoiForm> data = cr.list();
            DetachedCriteria countCriteria = createCriteria(sc).setProjection(Projections.countDistinct("id"));
            List<Long> count = (List<Long>) countCriteria.getExecutableCriteria(cgpSessionFactory.getCurrentSession()).list();
            return new CgpNoiFormSearchResult(data, conf.getDraw(), count.get(0), count.get(0));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new ApplicationException(ApplicationErrorCode.E_InternalError, e.getMessage());
        }
    }

    DetachedCriteria createCriteria(CgpNoiFormSearchCriteria criteria) {
        DetachedCriteria cr = DetachedCriteria.forClass(CgpNoiForm.class);
        cr.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        if (criteria == null) {
            return cr;
        }

        // formSet properties
        cr.createAlias("formSet", "formSets");
        if (!StringUtils.isEmpty(criteria.getOwner())) {
            cr.add(Restrictions.like("formSets.owner", criteria.getOwner(), MatchMode.ANYWHERE).ignoreCase());
        }
        if (!StringUtils.isEmpty(criteria.getNpdesId())) {
            cr.add(Restrictions.like("formSets.npdesId", criteria.getNpdesId(), MatchMode.ANYWHERE).ignoreCase());
        }
        if (!StringUtils.isEmpty(criteria.getMasterGeneralPermit())) {
            cr.add(Restrictions.like("formSets.masterPermitNumber", criteria.getMasterGeneralPermit(), MatchMode.ANYWHERE).ignoreCase());
        }
        if (criteria.getSiteRegion() != null) {
            // search for all of the permits beginning with the state code of the states in the specified region
            List<String> statesInRegion = getStateCodesForRegion(criteria.getSiteRegion().intValue());
            Disjunction permitStates = Restrictions.or();
            for (String state : statesInRegion) {
                permitStates.add(Restrictions.like("formSets.masterPermitNumber", state, MatchMode.START));
            }
            cr.add(permitStates);
        }


        // form properties
        if (!StringUtils.isEmpty(criteria.getTrackingNumber())) {
            cr.add(Restrictions.eq("trackingNumber", criteria.getTrackingNumber()));
        }
        if (criteria.getType() != null) {
            cr.add(Restrictions.eq("type", criteria.getType()));
        }
        if (BooleanUtils.isTrue(criteria.getPublicSearch())) { // search for only this status
            if (criteria.getStatus() != null && ApplicationUtils.PUBLIC_STATUSES.contains(criteria.getStatus())) {
                cr.add(Restrictions.eq("status", criteria.getStatus()));
            } else { // return all public statuses
                cr.add(Restrictions.in("status", ApplicationUtils.PUBLIC_STATUSES));
            }
        } else if (BooleanUtils.isTrue(criteria.getRegulatoryAuthoritySearch())) {
            cr.add(Restrictions.in("status", ApplicationUtils.RA_STATUSES));
        } else if (!CollectionUtils.isEmpty(criteria.getStatuses())) {
            cr.add(Restrictions.in("status", criteria.getStatuses()));
        } else if (criteria.getStatus() != null) {
            cr.add(Restrictions.eq("status", criteria.getStatus()));
        }
        if (criteria.getActiveRecord() != null) {
            cr.add(Restrictions.eq("activeRecord", criteria.getActiveRecord()));
        }
        if (criteria.getSubmittedFrom() != null) {
            cr.add(Restrictions.ge("submittedDate",
                    applicationUtils.getAsStartOfDay(criteria.getSubmittedFrom())));
        }
        if (criteria.getSubmittedTo() != null) {
            cr.add(Restrictions.le("submittedDate",
                    applicationUtils.getAsStartOfDay(criteria.getSubmittedTo().plusDays(1))));
        }
        if (criteria.getUpdatedFrom() != null) {
            cr.add(Restrictions.ge("lastUpdatedDate",
                    applicationUtils.getAsStartOfDay(criteria.getUpdatedFrom())));
        }
        if (criteria.getUpdatedTo() != null) {
            cr.add(Restrictions.le("lastUpdatedDate",
                    applicationUtils.getAsStartOfDay(criteria.getUpdatedTo().plusDays(1))));
        }
        if (criteria.getCreatedFrom() != null) {
            cr.add(Restrictions.ge("createdDate",
                    applicationUtils.getAsStartOfDay(criteria.getCreatedFrom())));
        }
        if (criteria.getCreatedTo() != null) {
            cr.add(Restrictions.le("createdDate",
                    applicationUtils.getAsStartOfDay(criteria.getCreatedTo())));
        }
        if (criteria.getReviewExpiration() != null) {
            cr.add(Restrictions.lt("reviewExpiration", criteria.getReviewExpiration()));
        }
        if (criteria.getSubmittedToIcis() != null) {
            if (BooleanUtils.isTrue(criteria.getSubmittedToIcis())) {
                cr.add(Restrictions.isNotNull("nodeTransactionId"));
            } else {
                cr.add(Restrictions.isNull("nodeTransactionId"));
            }
        }
        if (criteria.getIcisSubmissionInProgress() != null) {
            if (BooleanUtils.isTrue(criteria.getIcisSubmissionInProgress())) {
                cr.add(Restrictions.not(Restrictions.in("nodeTransactionStatus", ApplicationUtils.TRANSACTION_FINAL_STATUSES)));
            } else {
                cr.add(Restrictions.in("nodeTransactionStatus", ApplicationUtils.TRANSACTION_FINAL_STATUSES));
            }
        }

        // index properties
        cr.createAlias("index", "index");
        if (!StringUtils.isEmpty(criteria.getOperatorName())) {
            cr.add(Restrictions.like("index.operatorName", criteria.getOperatorName(),
                    MatchMode.ANYWHERE).ignoreCase());
        }
        if (!StringUtils.isEmpty(criteria.getSiteName())) {
            cr.add(Restrictions.like("index.siteName", criteria.getSiteName(),
                    MatchMode.ANYWHERE).ignoreCase());
        }
        if (!StringUtils.isEmpty(criteria.getSiteStateCode())) {
            cr.add(Restrictions.eq("index.siteStateCode", criteria.getSiteStateCode()));
        }
        if (!StringUtils.isEmpty(criteria.getSiteCity())) {
            cr.add(Restrictions.like("index.siteCity", criteria.getSiteCity(),
                    MatchMode.ANYWHERE).ignoreCase());
        }
        if (!StringUtils.isEmpty(criteria.getSiteZipCode())) {
            cr.add(Restrictions.like("index.siteZipCode", criteria.getSiteZipCode(),
                    MatchMode.ANYWHERE).ignoreCase());
        }
        if (!StringUtils.isEmpty(criteria.getSiteCounty())) {
            cr.add(Restrictions.eq("index.siteCounty", criteria.getSiteCounty()).ignoreCase());
        }
        if (criteria.getOperatorFederal() != null) {
            cr.add(Restrictions.eq("index.operatorFederal", criteria.getOperatorFederal()));
        }
        if (criteria.getSiteIndianCountry() != null) {
            cr.add(Restrictions.eq("index.siteIndianCountry", criteria.getSiteIndianCountry()));
        }
        if (!StringUtils.isEmpty(criteria.getSiteIndianCountryLands())) {
            cr.add(Restrictions.like("index.siteIndianCountryLands", criteria.getSiteIndianCountryLands(),
                    MatchMode.ANYWHERE).ignoreCase());
        }

        // associated user
        if (!StringUtils.isEmpty(criteria.getAssociatedUser())) {
            cr.add(Restrictions.or(
                    Restrictions.eq("formSets.owner", criteria.getAssociatedUser()),
                    Restrictions.eq("index.preparer", criteria.getAssociatedUser()),
                    Restrictions.eq("index.certifier", criteria.getAssociatedUser())));

        }
        return cr;
    }

    private List<String> getStateCodesForRegion(Integer region) {
        List<State> regionStates = referenceRepository.retrieveStates(region);
        List<String> regionStateCodes = new ArrayList<>();
        for (State rs : regionStates) {
            regionStateCodes.add(rs.getStateCode());
        }
        return regionStateCodes;
    }
}
