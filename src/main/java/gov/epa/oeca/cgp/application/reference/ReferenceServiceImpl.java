package gov.epa.oeca.cgp.application.reference;

import gov.epa.oeca.cgp.domain.noi.formsections.Pollutant;
import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.cgp.domain.ref.*;
import gov.epa.oeca.cgp.infrastructure.persistence.ReferenceRepository;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author smckay
 */
@Service("referenceService")
@Transactional(readOnly = true)
public class ReferenceServiceImpl implements ReferenceService {

    private static final Logger logger = LoggerFactory.getLogger(ReferenceServiceImpl.class);

    @Autowired
    ReferenceRepository referenceRepository;

    @Override
    @Cacheable("states")
    public List<State> retrieveStates() throws ApplicationException {
        try {
            return referenceRepository.retrieveStates();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("states")
    public State retrieveState(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required.");
            return referenceRepository.retrieveState(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("states")
    public List<State> retrieveStates(Integer region) throws ApplicationException {
        try {
            Validate.notNull(region, "Region is required.");
            Validate.isTrue(region >= 1 && region <= 11, "Only 1-11 are valid region codes.");
            return referenceRepository.retrieveStates(region);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("tribes")
    public List<Tribe> retrieveTribes() throws ApplicationException {
        try {
            return referenceRepository.retrieveTribes();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("tribes")
    public List<Tribe> retrieveTribes(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required to retrieve tribes.");
            return referenceRepository.retrieveTribes(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Tribe> retriveTribesByLandName(String tribalLandName) throws ApplicationException {
        try {
            Validate.notEmpty(tribalLandName, "Tribal land name is required");
            return referenceRepository.retriveTribesByLandName(tribalLandName);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("biaTribes")
    public List<BiaTribe> retrieveBiaTribes() throws ApplicationException {
        try {
            return referenceRepository.retrieveBiaTribes();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("biaTribes")
    public List<BiaTribe> retrieveBiaTribes(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required to retrieve tribes.");
            return referenceRepository.retrieveBiaTribes(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("counties")
    public List<County> retrieveAllCounties() throws ApplicationException {
        try {
            return referenceRepository.retrieveCounties();
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("counties")
    public List<County> retrieveCounties(String stateCode) throws ApplicationException {
        try {
            Validate.notEmpty(stateCode, "State code is required to retrieve counties.");
            return referenceRepository.retrieveCounties(stateCode);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("chemicals")
    public List<Chemical> retrieveChemicals() throws ApplicationException {
        try {
            return referenceRepository.retrieveActiveChemicals();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("chemicals")
    public List<Chemical> retrieveChemicals(String name) throws ApplicationException {
        try {
            Validate.notEmpty(name, "Chemical name is required.");
            Validate.isTrue(
                    name.length() >= 3,
                    "At least 3 characters are required to search by chemical name.");
            return referenceRepository.retrieveActiveChemicals(name);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Pollutant> retrievePollutants(String name) throws ApplicationException {
        try {
            Validate.notEmpty(name, "Pollutant name is required.");
            Validate.isTrue(
                    name.length() >= 3,
                    "At least 3 characters are required to search by pollutant name.");
            return referenceRepository.retrievePollutants(name);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<ReceivingWater> retrieveReceivingWaters(String name) throws ApplicationException {
        try {
            Validate.notEmpty(name, "Receiving water name is required.");
            Validate.isTrue(
                    name.length() >= 3,
                    "At least 3 characters are required to search by receiving water name.");
            return referenceRepository.retrieveReceivingWaters(name);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("overrides")
    public TribalOverride retrieveOverride(String tribalCode, String stateCode) throws ApplicationException {
        try {
            return referenceRepository.retrieveOverride(tribalCode, stateCode);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("rules")
    public MgpRule retrieveRule(String stateCode) throws ApplicationException {
        try {
            return referenceRepository.retrieveRule(stateCode);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public NpdesSequence retrieveNextNpdesSequence(String mgpNumber) throws ApplicationException {
        try {
            return referenceRepository.retrieveNextNpdesSequence(mgpNumber);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Transactional
    public void updateNpdesSequence(NpdesSequence sequence) throws ApplicationException {
        try {
            referenceRepository.updateNpdesSequence(sequence);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    @Cacheable("subscribers")
    public List<Subscriber> retrieveSubscribers() throws ApplicationException {
        try {
            return referenceRepository.retrieveSubscribers();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Subscriber> retrieveSubscribersByMgp(String mgp) throws ApplicationException {
        try {
            Validate.notEmpty(mgp, "MGP is required");
            return referenceRepository.retrieveSubscribersBySubcategory(mgp);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }

    @Override
    public List<Subscriber> retrieveSubscribersByCatSubcat(String category, String subcategory) throws ApplicationException {
        try {
            Validate.notEmpty(category, "Notification category is required");
            Validate.notEmpty(subcategory, "Notification subcategory is required");
            return referenceRepository.retrieveSubscribersByCatSubcat(category, subcategory);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage(), e);
            throw new ApplicationException(ApplicationErrorCode.E_InvalidArgument, e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw ApplicationException.asApplicationException(e);
        }
    }
}
