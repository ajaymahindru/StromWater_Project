package gov.epa.oeca.cgp.infrastructure.persistence.migrations;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import gov.epa.oeca.cgp.domain.noi.CgpNoiForm;
import gov.epa.oeca.cgp.domain.noi.CgpNoiFormData;
import org.apache.commons.lang3.StringUtils;
import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobHandler;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * @author dfladung
 */
public class V1_1_4_2__AddCountyToIndex implements SpringJdbcMigration {

    private static final Logger logger = LoggerFactory.getLogger(V1_1_4_2__AddCountyToIndex.class);

    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        LobHandler lobHandler = new DefaultLobHandler();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        jdbcTemplate.query("select id, index_id, formdata from cgp_noi_forms", resultSet -> {
            while (resultSet.next()) {
                Long id = resultSet.getLong(1);
                Long indexId = resultSet.getLong(2);
                InputStream blobStream = lobHandler.getBlobAsBinaryStream(resultSet, 3);
                try {
                    CgpNoiFormData form = mapper.readValue(blobStream, CgpNoiFormData.class);
                    if (!StringUtils.isEmpty(form.getProjectSiteInformation().getSiteCounty())) {
                        jdbcTemplate.update("update cgp_noi_form_data_index set site_county = ? where id = ?",
                                new Object[]{form.getProjectSiteInformation().getSiteCounty(), indexId});
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                    throw new SQLException("Could not migrate county date to index.", e);
                }
            }
        });

    }
}
