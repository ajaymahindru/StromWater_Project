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
public class TribalOverrideSqlGenerator extends RefSqlGenerator {

    private static final String sql = "insert into cgp_ref_tribal_overrides(id, tribal_code, state_code, mgp_number) " +
            "values(%s,%s,%s,%s);";

    public static void main(String[] args) {
        try {
            File input = new File(Thread.currentThread().getContextClassLoader().getResource("reference/tribal_overrides.csv").toURI());
            int count = 0;

            for (String line : FileUtils.readLines(input, Charset.defaultCharset())) {
                List<String> fields = Lists.newArrayList(Splitter.on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")).split(line));
                String tribalCode = fields.get(0);
                String state = fields.get(1);
                String mgpNumber = fields.get(2);
                String sql = String.format(TribalOverrideSqlGenerator.sql, ++count, getString(tribalCode), getString(state),
                        getString(mgpNumber));
                System.out.println(sql);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
