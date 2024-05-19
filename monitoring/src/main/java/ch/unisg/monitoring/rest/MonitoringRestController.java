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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
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

        try {
            emitter.send(SseEmitter.event().name("message").data(mapColors));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }
  
    @GetMapping("/lightSensor")
    public SseEmitter getLightSensor() {
        SseEmitter emitter = new SseEmitter(100L);

        Map<String, ColorStats> mapColors = new HashMap<>();


        var range = lightSensorStore.fetch("i4_light_sensor");

        while(range.hasNext()) {
            var next = range.next();
            System.out.println("Wohooo");
            System.out.println(next.key.key());
            System.out.println(next.value.getTimeDifference());
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

        try {
            emitter.send(SseEmitter.event().name("message").data(mapFactory));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        emitter.complete();
        return emitter;
    }
}