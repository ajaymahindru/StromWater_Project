package gov.epa.oeca.cgp.application;

import gov.epa.oeca.cgp.domain.noi.Status;
import gov.epa.oeca.common.ApplicationErrorCode;
import gov.epa.oeca.common.ApplicationException;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

/**
 * @author dfladung
 */
@Component
public class ApplicationUtils {

    public static final List<Status> PUBLIC_STATUSES = Arrays.asList(Status.Submitted, Status.Active, Status.Terminated,
            Status.Discontinued, Status.Archived);
    public static final List<Status> RA_STATUSES = Arrays.asList(Status.Submitted, Status.Active, Status.Denied,
            Status.OnHold, Status.Terminated, Status.Discontinued, Status.Archived);
    public static final List<Status> FINAL_STATUSES = Arrays.asList(Status.Active, Status.Terminated, Status.Discontinued);
    public static final List<Status> DISTRIBUTION_STATUSES =
            Arrays.asList(Status.Submitted, Status.Active, Status.Terminated, Status.Discontinued);

    public static final String DISABLE_DISTRIBUTION_KEY = "disable_distribution";

    public ZonedDateTime fromString(String dateString) {
        try {
            return StringUtils.isEmpty(dateString) ? null : ZonedDateTime.parse(dateString);
        } catch (Exception e) {
            throw new ApplicationException(
                    ApplicationErrorCode.E_InvalidArgument,
                    String.format("%s is not a valid date/time.", dateString));
        }
    }

    public ZonedDateTime getAsStartOfDay(ZonedDateTime date) {
        if (date == null) {
            return null;
        } else {
            return date.toLocalDate().atStartOfDay(date.getZone());
        }
    }
}
