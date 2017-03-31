package gov.epa.oeca.cgp.domain.ref;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author dfladung
 */
public class MgpRuleSqlGenerator extends RefSqlGenerator {

    private static final String sql = "insert into cgp_ref_mgp(id, region_code, state_code, non_fed_fac_eligible, fed_fac_eligible, " +
            "mgp_number, ind_country_mgp_number) values(%s,%s,%s,%s,%s,%s,%s);";

    public static void main(String[] args) {
        try {
            File input = new File(Thread.currentThread().getContextClassLoader().getResource("reference/mgp.csv").toURI());
            int count = 0;

            for (String line : FileUtils.readLines(input, Charset.defaultCharset())) {
                List<String> fields = Lists.newArrayList(Splitter.on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")).split(line));
                String region = fields.get(0);
                String state = fields.get(1);
                String nonFederalfacilityEligibleOutsideIndianCountry = fields.get(2);
                String federalfacilityEligibleOutsideIndianCountry = fields.get(3);
                String mgpNumber = fields.get(4);
                String indianCountryMgpNumber = fields.get(5);
                String sql = String.format(MgpRuleSqlGenerator.sql, ++count, getString(region), getString(state),
                        getString(nonFederalfacilityEligibleOutsideIndianCountry), getString(federalfacilityEligibleOutsideIndianCountry),
                        getString(mgpNumber),getString(indianCountryMgpNumber));
                System.out.println(sql);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
