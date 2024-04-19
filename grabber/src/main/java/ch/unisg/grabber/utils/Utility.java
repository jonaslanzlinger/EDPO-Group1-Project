package ch.unisg.grabber.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Utility {

    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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

