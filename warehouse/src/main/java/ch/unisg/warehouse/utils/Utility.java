package ch.unisg.warehouse.utils;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class Utility {

    public static void sleepRandom() {
        double randomSleepDuration = Math.random() * 4000;
        try {
            Thread.sleep((long) randomSleepDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> T getFromMap(Map<String, Object> map, String key, Class<T> clazz) {
        Object value = map.get(key);
        if (clazz.isInstance(value)) {
            return clazz.cast(value);
        }
        return null;
    }

    /**
     * Logs a message with the given parameters using the WorkflowLogger.
     *
     * @param methodName The name of the method where the log is being made.
     * @param message The message to log.
     */
    public static void logInfo(String methodName, String message) {
        WorkflowLogger.info(log, methodName, message);
    }
}

