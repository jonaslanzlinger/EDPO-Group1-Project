package ch.unisg.warehouse.utils;

import java.util.Map;

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
}

