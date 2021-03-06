package gov.epa.oeca.cgp.domain.ref;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author dfladung
 */
public class PollutantSqlGenerator {

    private static final Logger logger = LoggerFactory.getLogger(PollutantSqlGenerator.class);

    private static final String sql = "insert into cgp_ref_pollutants(id, pollutant_code, pollutant_name, pollutant_srs_name) values(%s,%s,%s,%s);";

    public static void main(String[] args) {
        try {
            File input = new File(Thread.currentThread().getContextClassLoader().getResource("reference/pollutants.csv").toURI());
            int count = 0;

            for (String line : FileUtils.readLines(input, Charset.defaultCharset())) {
                List<String> fields = Lists.newArrayList(Splitter.on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")).split(line));
                String pollutantCode = fields.get(0);
                String pollutantName = StringUtils.remove(fields.get(1), "\"").replace("'", "''").replace(";", ",");
                String pollutantSrsName = StringUtils.remove(fields.get(3), "\"").replace("'", "''").replace(";", ",");
                String sql = String.format(
                        PollutantSqlGenerator.sql,
                        ++count,
                        Integer.parseInt(pollutantCode),
                        getString(pollutantName),
                        getString(pollutantSrsName));
                System.out.println(sql);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static String getString(String str) {
        if (!StringUtils.isEmpty(str)) {
            return "'" + str + "'";
        } else {
            return "NULL";
        }
    }
}
