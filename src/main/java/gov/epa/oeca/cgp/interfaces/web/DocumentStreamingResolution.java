package gov.epa.oeca.cgp.interfaces.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import net.sourceforge.stripes.action.StreamingResolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DocumentStreamingResolution extends StreamingResolution {

	private static final Logger logger = LoggerFactory.getLogger(DocumentStreamingResolution.class);
	
	protected static String OCTET_MIME_TYPE = "application/octet-stream";
	protected URI content;

	public DocumentStreamingResolution(String contentType, URI content) {
		this(contentType,null,content);
	}
	
	public DocumentStreamingResolution(String contentType, String fileName, URI content) {
		super(contentType);
		this.content = content;
		if (fileName == null){
			this.setFilename(FilenameUtils.getName(content.toString()));
		}else{
			this.setFilename(fileName);
		}
	}

	@Override
	protected void stream(HttpServletResponse response) {
		FileInputStream stream = null;
		try {
			stream = new FileInputStream(new File(content));
			IOUtils.copy(new BufferedInputStream(stream), response.getOutputStream());
		} catch (Throwable t) {
			// ignore any errors
			logger.warn("DocumentStreamingResolution stream error: "+t);
		} finally {
			IOUtils.closeQuietly(stream);
			try {
				FileUtils.forceDelete(new File(content));
			}
			catch (Throwable t) {
				logger.warn("failed to delete uri " + content, t);
			}
		}
	}
}
