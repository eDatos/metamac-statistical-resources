package org.siemac.metamac.statistical_resources.rest.external.interceptor.cache;

import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Component
public class CacheHelper {

    public Date parseHttpDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }

        try {
            return Date.from(ZonedDateTime.parse(dateString, DateTimeFormatter.RFC_1123_DATE_TIME).toInstant());
        } catch (Exception e) {
            return null;
        }
    }

    public boolean isModifiedSince(Date lastModified, Date clientDate) {
        if (lastModified == null || clientDate == null) {
            return true; // if we can't determine, assume modified
        }

        // truncate to seconds precision (HTTP dates don't include milliseconds)
        long lastModifiedSeconds = lastModified.getTime() / 1000;
        long clientDateSeconds = clientDate.getTime() / 1000;

        return lastModifiedSeconds > clientDateSeconds;
    }
}
