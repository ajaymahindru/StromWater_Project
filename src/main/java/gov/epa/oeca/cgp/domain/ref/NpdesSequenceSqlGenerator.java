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
public class NpdesSequenceSqlGenerator extends RefSqlGenerator {

    private static final String sql = "insert into cgp_ref_npdes_seq(id, mgp_num, npdes_alpha_start) values(%s,%s,%s);";

    public static void main(String[] args) {
        try {
            File input = new File(Thread.currentThread().getContextClassLoader().getResource("reference/npdes.csv").toURI());
            int count = 0;

            for (String line : FileUtils.readLines(input, Charset.defaultCharset())) {
                List<String> fields = Lists.newArrayList(Splitter.on(Pattern.compile(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)")).split(line));
                String mgpNum = fields.get(0);
                String npdesAlphaStart = fields.get(1);
                String sql = String.format(NpdesSequenceSqlGenerator.sql, ++count, getString(mgpNum), getString(npdesAlphaStart));
                System.out.println(sql);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
