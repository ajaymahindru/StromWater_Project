package gov.epa.oeca.cgp.infrastructure.persistence;

import gov.epa.oeca.cgp.domain.noi.CgpNoiFormSet;
import org.springframework.stereotype.Repository;

/**
 * @author dfladung
 */
@Repository
public class FormSetRepository extends BaseRepository<CgpNoiFormSet> {

    public FormSetRepository() {
        super(CgpNoiFormSet.class);
    }
}
