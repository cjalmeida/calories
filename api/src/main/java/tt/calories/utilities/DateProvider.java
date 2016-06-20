package tt.calories.utilities;

import com.fasterxml.jackson.databind.util.ISO8601Utils;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.TimeZone;

/**
 * Class abstracting system date/time for testing and providing date-related utilities.
 *
 * @author Cloves Almeida
 * @version 1.0.0
 */
@Component
public class DateProvider {

    /**
     * Parse a date in JSON (ISO8601) format.
     */
    public Date fromJson(String s) throws ParseException {
        return ISO8601Utils.parse(s, new ParsePosition(0));
    }

    /**
     * Get current time. This should be used instead of "new Date()" so it
     * can be overriden in tests.
     */
    public Date now() {
        return new Date();
    }

    /**
     * Convenience method for returning now().
     */
    public Date nowPlusDays(Integer days) {
        return Date.from(now().toInstant().plus(days, ChronoUnit.DAYS));
    }

    /**
     * Format a date into JSON (ISO8601) format in UTC.
     */
    public String toJsonUTC(Date date) {
        return ISO8601Utils.format(date, false, TimeZone.getTimeZone("UTC"));
    }

    /**
     * Return a date in "seconds-since-epoch" format per POSIX specs.
     */
    public double toJsonNumericDate(Date date) {
        return (double)date.getTime() / 1000.0;
    }
}
