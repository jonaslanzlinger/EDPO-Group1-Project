package ch.unisg.order.utils;

import org.slf4j.Logger;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @implSpec Logger to trace the flow using keyword workflow-service-info
 */
public final class WorkflowLogger {

    private static final String INFO_LOGGER = "{} - order-info: {} - {}";
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());


    private static final String ERROR_LOGGER = "Timestamp:{}:workflow-service-error:{}:{}";

    private WorkflowLogger() {
        throw new IllegalStateException("Logger Utility Class. Cannot be instantiated.");
    }

    /**
     * INFO log statement
     *
     * @param logger  : logger used in the calling class
     * @param method  : method description
     * @param message : processing details
     */
    public static void info(Logger logger, String method, String message) {
        String formattedTimestamp = DATE_TIME_FORMATTER.format(Instant.now());
        logger.info(INFO_LOGGER, formattedTimestamp, method, message);
    }


    /**
     * ERROR log statement with exception trace
     *
     * @param logger    : logger used in the calling class
     * @param method    : method description
     * @param message   : processing details
     * @param exception : exception trace details
     */
    public static void error(Logger logger, String method, String message, Exception exception) {
        logger.error(ERROR_LOGGER, System.currentTimeMillis(), method, message, exception);
    }


    /**
     * ERROR log statement
     *
     * @param logger  : logger used in the calling class
     * @param method  : method description
     * @param message : processing details
     */
    public static void error(Logger logger, String method, String message) {
        logger.error(ERROR_LOGGER, System.currentTimeMillis(), method, message);
    }
}
