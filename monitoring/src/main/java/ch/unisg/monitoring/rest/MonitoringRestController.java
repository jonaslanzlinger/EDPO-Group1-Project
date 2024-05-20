package ch.unisg.monitoring.rest;

import ch.unisg.monitoring.domain.MonitoringStore;
import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
import ch.unisg.monitoring.kafka.topology.aggregations.FactoryStats;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import ch.unisg.monitoring.kafka.topology.aggregations.TimeDifferenceAggregation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlySessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * This is a REST controller class for the Shop.
 * It uses Spring's @RestController annotation to indicate that it is a REST controller.
 * It uses Lombok's @AllArgsConstructor to generate a constructor with all properties.
 */
@RestController
@AllArgsConstructor
@Slf4j
public class MonitoringRestController {

    private final MonitoringStore monitoringStore;
    private final ReadOnlyKeyValueStore<String, ColorStats> colorStatsStore;
    private final ReadOnlySessionStore<String, TimeDifferenceAggregation> lightSensorStore;
    private final ReadOnlyKeyValueStore<String, FactoryStats> factoryStatsStore;

    //TODO: Status codes
    @RequestMapping(path = "/api/monitoring/{orderId}", method = GET, produces = "application/json")
    public String getOrderId(@PathVariable String orderId) {

        // Check if the color is in stock
        if (!monitoringStore.containsOrder(orderId)) {
            return "{\"error\": \"OrderId not found\"}";
        }
        return "[" + monitoringStore.getMessages(orderId).stream().map(MonitorUpdateDto::toJson).collect(Collectors.joining(",")) + "]";

    }

    @RequestMapping(path = "/api/monitoring/orders", method = GET, produces = "application/json")
    public String getOrders() {
        if (monitoringStore.isEmpty()) {
            return "{\"error\": \"No orders available\"}";
        }
        // Check if the color is in stock
        return "[" + monitoringStore.getAllMessages().values().stream().flatMap(l -> l.stream().map(MonitorUpdateDto::toJson)).collect(Collectors.joining(",")) + "]";
    }

    @GetMapping("/api/updates")
    public SseEmitter sendUpdates() {
        SseEmitter emitter = new SseEmitter(100L);

        String jsonString = "[" + monitoringStore.getAllMessages().values().stream().flatMap(l -> l.stream().map(MonitorUpdateDto::toJson)).collect(Collectors.joining(",")) + "]";

        try {
            emitter.send(SseEmitter.event().name("message").data(jsonString));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }
    @GetMapping("/colorStats")
    public SseEmitter getColorStats() {
        SseEmitter emitter = new SseEmitter(100L);

        Map<String, ColorStats> mapColors = new HashMap<>();

        var range = colorStatsStore.all();

        while(range.hasNext()) {
            var next = range.next();
            String color = next.key;
            var colorStats = next.value;
            mapColors.put(color,colorStats);
        }

        range.close();
        try {
            emitter.send(SseEmitter.event().name("message").data(mapColors));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }

    @GetMapping("/test")
    public String getTest() {
        Logger logger = LoggerFactory.getLogger(MonitoringRestController.class);

        long fetchStartTime = System.currentTimeMillis();
        var range = lightSensorStore.backwardFetch("i4_light_sensor");
        range.close();
        long fetchEndTime = System.currentTimeMillis();

        logger.info("Fetch and process time: {} ms", (fetchEndTime - fetchStartTime));

        range.forEachRemaining(n -> {
            System.out.println(n.key.key());
            System.out.println(n.value.getFirstTimestamp());
            System.out.println(n.value.getLastTimestamp());
        });

        return "successs";
    }

    @GetMapping("/test2")
    public String getTest2() {
        Logger logger = LoggerFactory.getLogger(MonitoringRestController.class);

        long fetchStartTime = System.currentTimeMillis();
        var range = lightSensorStore.fetch("i4_light_sensor");
        range.close();
        long fetchEndTime = System.currentTimeMillis();

        logger.info("Fetch and process time: {} ms", (fetchEndTime - fetchStartTime));

        while(range.hasNext()) {
            var next = range.next();
            System.out.println(next.key.key());
            System.out.println(next.value.getFirstTimestamp());
            System.out.println(next.value.getLastTimestamp());
        }

        return "successs";
    }


    @GetMapping("/lightSensor")
    public SseEmitter getLightSensor() {
        Logger logger = LoggerFactory.getLogger(MonitoringRestController.class);

        SseEmitter emitter = new SseEmitter(100L);

        Map<String, ColorStats> mapColors = new HashMap<>();

        long fetchStartTime = System.currentTimeMillis();
        var range = lightSensorStore.fetch("i4_light_sensor");
        long fetchEndTime = System.currentTimeMillis();
        // Logging system resource utilization (example using OperatingSystemMXBean)
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        if (osBean instanceof com.sun.management.OperatingSystemMXBean sunOsBean) {
            logger.info("System CPU load: {}%", sunOsBean.getSystemCpuLoad() * 100);
            logger.info("Process CPU load: {}%", sunOsBean.getProcessCpuLoad() * 100);
            logger.info("Free physical memory: {} MB", sunOsBean.getFreePhysicalMemorySize() / (1024 * 1024));
            logger.info("Total physical memory: {} MB", sunOsBean.getTotalPhysicalMemorySize() / (1024 * 1024));
        }
        while(range.hasNext()) {
            var next = range.next();
            System.out.println(next.key.key());
            System.out.println(next.value.getFirstTimestamp());
            System.out.println(next.value.getLastTimestamp());
        }
        range.close();

        logger.info("Fetch and process time: {} ms", (fetchEndTime - fetchStartTime));

        try {
            emitter.send(SseEmitter.event().name("message").data(mapColors));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }

    @GetMapping("/factoryStats")
    public SseEmitter getFactoryStats() {
        SseEmitter emitter = new SseEmitter(100L);

        Map<String, FactoryStats> mapFactory = new HashMap<>();

        var range = factoryStatsStore.all();

        while(range.hasNext()) {
            var next = range.next();
            String factory = next.key;
            var factoryStats = next.value;
            mapFactory.put(factory,factoryStats);
        }
        range.close();
        try {
            emitter.send(SseEmitter.event().name("message").data(mapFactory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }
}