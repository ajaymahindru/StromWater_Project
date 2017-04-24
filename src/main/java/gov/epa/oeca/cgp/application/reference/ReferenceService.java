package gov.epa.oeca.cgp.application.reference;

import gov.epa.oeca.cgp.domain.noi.formsections.Pollutant;
import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater;
import gov.epa.oeca.cgp.domain.ref.*;
import gov.epa.oeca.common.ApplicationException;

import java.util.List;

/**
 * @author smckay
 */
public interface ReferenceService {
    List<State> retrieveStates() throws ApplicationException;

    List<State> retrieveStates(Integer region) throws ApplicationException;

    State retrieveState(String stateCode) throws ApplicationException;

    List<Tribe> retrieveTribes() throws ApplicationException;

    List<Tribe> retrieveTribes(String stateCode) throws ApplicationException;

    List<Tribe> retrieveTribesByLandName(String tribalLandName) throws ApplicationException;

    Tribe retrieveTribeByLandNameAndStateCode(String tribalLandName, String stateCode) throws ApplicationException;

    List<BiaTribe> retrieveBiaTribes() throws ApplicationException;

    List<BiaTribe> retrieveBiaTribes(String stateCode) throws ApplicationException;

    List<County> retrieveCounties(String stateCode) throws ApplicationException;

    List<County> retrieveAllCounties() throws ApplicationException;

    List<Chemical> retrieveChemicals() throws ApplicationException;

    List<Chemical> retrieveChemicals(String name) throws ApplicationException;

    List<Pollutant> retrievePollutants(String name) throws ApplicationException;

    List<ReceivingWater> retrieveReceivingWaters(String name) throws ApplicationException;

    TribalOverride retrieveOverride(String tribalCode, String stateCode) throws ApplicationException;

    MgpRule retrieveRule(String stateCode) throws ApplicationException;

    NpdesSequence retrieveNextNpdesSequence(String mgpNumber) throws ApplicationException;

    void updateNpdesSequence(NpdesSequence sequence) throws ApplicationException;

    List<Subscriber> retrieveSubscribers() throws ApplicationException;

    List<Subscriber> retrieveSubscribersByMgp(String mgpCategory) throws ApplicationException;

    List<Subscriber> retrieveSubscribersByCatSubcat(String category, String subcategory) throws ApplicationException;

}
