package gov.epa.oeca.cgp.infrastructure.persistence;

import gov.epa.oeca.cgp.domain.noi.Attachment;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.support.AbstractLobStreamingResultSetExtractor;
import org.springframework.jdbc.core.support.SqlLobValue;
import org.springframework.stereotype.Repository;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

/**
 * @author dfladung
 */
@Repository
public class AttachmentRepository extends BaseRepository<Attachment> {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentRepository.class);

    public AttachmentRepository() {
        super(Attachment.class);
    }

    public List<Attachment> findAll(Long formId) {
        DetachedCriteria cr = DetachedCriteria.forClass(Attachment.class);
        cr.add(Restrictions.eq("form.id", formId));
        return cr.getExecutableCriteria(cgpSessionFactory.getCurrentSession()).list();
    }

    @Override
    public Long save(Attachment attachment) {
        super.save(attachment);
        cgpSessionFactory.getCurrentSession().flush();
        Long id = attachment.getId();
        InputStream dataStream = null;
        try {
            dataStream = new FileInputStream(attachment.getData());
            jdbcTemplate.update(
                    "update cgp_noi_form_attachments set data = ? where id = ?",
                    new Object[]{new SqlLobValue(dataStream, (int) attachment.getData().length(), lobHandler), id},
                    new int[]{Types.BLOB, Types.INTEGER});
            return id;
        } catch (Exception e) {
            throw new DataAccessResourceFailureException("Could not save attachment data.", e);
        } finally {
            IOUtils.closeQuietly(dataStream);
        }
    }

    @Override
    public void update(Attachment attachment) {
        super.update(attachment);
        if (attachment.getData() != null && attachment.getData().exists()) {
            Long id = attachment.getId();
            InputStream dataStream = null;
            try {
                dataStream = new FileInputStream(attachment.getData());
                jdbcTemplate.update(
                        "update cgp_noi_form_attachments set data = ? where id = ?",
                        new Object[]{new SqlLobValue(dataStream, (int) attachment.getData().length(), lobHandler), id},
                        new int[]{Types.BLOB, Types.INTEGER});
            } catch (Exception e) {
                throw new DataAccessResourceFailureException("Could not save attachment data.", e);
            } finally {
                IOUtils.closeQuietly(dataStream);
            }
        }
    }

    public void deleteData(Attachment attachment) {
        cgpSessionFactory.getCurrentSession().flush();
        jdbcTemplate.update("update cgp_noi_form_attachments set data = NULL where id = ?", attachment.getId());
    }

    public File findData(Long id) {
        FileOutputStream contentStream = null;
        File tmp = null;
        try {
            tmp = File.createTempFile("Attachment", ".tmp");
            contentStream = new FileOutputStream(tmp);
            findDataHelper(id, contentStream);
            return tmp;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            FileUtils.deleteQuietly(tmp);
            throw new DataAccessResourceFailureException("Could not retrieve attachment content.", e);
        } finally {
            IOUtils.closeQuietly(contentStream);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    void findDataHelper(final Long id, final OutputStream contentStream) throws DataAccessException {
        jdbcTemplate.query("select data from cgp_noi_form_attachments where id = ?", new Object[]{id},
                new int[]{Types.INTEGER}, new AbstractLobStreamingResultSetExtractor() {
                    public void streamData(ResultSet rs) throws SQLException, IOException {
                        InputStream is = lobHandler.getBlobAsBinaryStream(rs, 1);
                        if (is != null) {
                            FileCopyUtils.copy(is, contentStream);
                        }
                    }
                });
    }
}
