package gov.epa.oeca.cgp.infrastructure.attains

import gov.epa.oeca.cgp.domain.noi.formsections.Pollutant
import gov.epa.oeca.cgp.domain.noi.formsections.ReceivingWater
import gov.epa.oeca.cgp.domain.noi.formsections.Tmdl
import gov.epa.oeca.common.ApplicationErrorCode
import gov.epa.oeca.common.ApplicationException
import groovy.json.JsonSlurper

/**
 * @author dfladung
 */
class CatchmentImpairmentReponseParserImpl implements CatchmentImpairmentReponseParser {


    @Override
    List<ReceivingWater> parse(String json) {
        List<ReceivingWater> results = new ArrayList<>()
        def slurper = new JsonSlurper()
        def waters = slurper.parseText(json)
        def status = waters.status
        if (!status || status.status_code != 0) {
            throw new ApplicationException(ApplicationErrorCode.E_RemoteServiceError, status?.status_message)
        }

        waters.output.catchments.each() { catchment ->
            catchment.nhd_waters.each() { nhd_water ->
                nhd_water?.listed_waters?.each() { listedWater ->
                    ReceivingWater rw = new ReceivingWater()
                    rw.receivingWaterId = listedWater.listed_water_id
                    rw.receivingWaterName = listedWater.listed_water_name

                    def pollutants = listedWater?.listed_water_causes
                    pollutants.each() { pollutant ->
                        Pollutant p = new Pollutant()
                        p.pollutantCode = pollutant.lw_detailed_cause_id
                        p.pollutantName = pollutant.lw_detailed_cause_name
                        rw.pollutants.add(p)
                    }
                    results.add(rw)
                }
                nhd_water?.listed_waters_with_tmdls?.each() { listedTmdl ->
                    def tmdlWaterId = listedTmdl.listed_water_id
                    def listedWater = results.find { it.receivingWaterId == tmdlWaterId }
                    if (listedWater) {
                        def tmdls = listedTmdl?.tmdls
                        tmdls.each() { tmdl ->
                            def tmdlCauses = tmdl?.tmdl_lw_causes
                            tmdlCauses.each() { tmdlCause ->
                                def pollutant = listedWater.pollutants.find {
                                    it.pollutantCode == tmdlCause.lw_detailed_cause_id
                                }
                                if (pollutant) {
                                    def pollutantTmdl = new Tmdl()
                                    pollutantTmdl.id = tmdl.tmdl_id
                                    pollutantTmdl.name = tmdl.tmdl_name
                                    pollutant.tmdl = pollutantTmdl
                                }
                            }
                        }
                    }
                }
            }
        }

        return results
    }
}
