package gov.epa.oeca.cgp.domain.ref;

import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;

/**
 * @author dfladung
 */
public abstract class RefSqlGenerator {

    private static final List<String> nullable = Arrays.asList("Ineligible", "NULL");

    protected static String getString(String str) {
        if (!StringUtils.isEmpty(str)) {
            return (nullable.contains(str)) ? "NULL" : "'" + str + "'";
        } else {
            return "NULL";
        }
    }
}
