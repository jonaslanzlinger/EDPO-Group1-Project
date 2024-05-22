package ch.unisg.monitoring.rest;

import ch.unisg.monitoring.domain.MonitoringStore;
import ch.unisg.monitoring.kafka.dto.MonitorUpdateDto;
import ch.unisg.monitoring.kafka.topology.aggregations.FactoryStats;
import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import ch.unisg.monitoring.kafka.topology.aggregations.TimeDifferenceAggregation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlySessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.*;
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
@CrossOrigin
public class MonitoringRestController {

    private final MonitoringStore monitoringStore;
    private final ReadOnlyKeyValueStore<String, ColorStats> colorStatsStore;
    private final ReadOnlySessionStore<String, TimeDifferenceAggregation> lightSensorStore;
    private final ReadOnlyKeyValueStore<String, FactoryStats> factoryStatsStore;
    private static final Logger logger = LoggerFactory.getLogger(MonitoringRestController.class);

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

    @GetMapping("/api/monitoring/colors")
    public ResponseEntity<String> getColorStats() {
        Map<String, ColorStats> mapColors = new HashMap<>();

        var range = colorStatsStore.all();

        while (range.hasNext()) {
            var next = range.next();
            String color = next.key;
            var colorStats = next.value;
            mapColors.put(color, colorStats);
        }
        range.close();
        try {
            ObjectMapper mapper = new ObjectMapper();
            return ResponseEntity.ok(mapper.writeValueAsString(mapColors));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/api/monitoring/hbw/{sensor}")
    public String getTest(@PathVariable String sensor) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        List<List<String>> timestamps = new ArrayList<>();


        var range = lightSensorStore.backwardFetch(sensor);

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
        try {
            while (true) {
                var n = fetchNextWithTimeout(range, executorService);
                if (n == null) {
                    range.close();
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonArray;
                    try {
                        jsonArray = mapper.writeValueAsString(timestamps);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return jsonArray;
                }
                List<String> currentTimeFrame = new ArrayList<>();
                currentTimeFrame.add(formatter.format(n.value.getFirstTimestamp()));
                currentTimeFrame.add(formatter.format(n.value.getLastTimestamp()));
                timestamps.add(currentTimeFrame);
            }
        } finally {
            shutdownExecutorService(executorService);
        }
    }

    /**
     * Because there is some unique kafka related issue, where it takes a long time for the iterator to
     * find the end of the iteration, we force quit the iteration once were at that point.
     * -> All actual sessions have been iterated over at that point
     *
     * @param iterator the iterator over the sessionStore
     * @param executorService the executor service
     * @return either null or the TimeDifferenceAggregation
     * @param <T> Generic -> could just be TimeDifferenceAggregation in this case
     */
    private static <T> T fetchNextWithTimeout(Iterator<T> iterator, ScheduledExecutorService executorService) {
        Future<T> future = executorService.submit(() -> {
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return null;
            }
        });
        try {
            return future.get(50L, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            return null;
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Fetching next element was interrupted or failed.", e);
            return null;
        }
    }

    /**
     *  Because of some kafka related issue the executor service might not shutdown as wanted
     *  To prevent longer wait periods at the endpoint force shutdown
     *
     * @param executorService the executor Service
     */
    private void shutdownExecutorService(ScheduledExecutorService executorService) {
        executorService.shutdownNow();
        Thread.currentThread().interrupt();
    }

    @GetMapping("/factoryStats")
    public SseEmitter getFactoryStats() {
        SseEmitter emitter = new SseEmitter(100L);

        Map<String, FactoryStats> mapFactory = new HashMap<>();

        var range = factoryStatsStore.all();

        while (range.hasNext()) {
            var next = range.next();
            String factory = next.key;
            var factoryStats = next.value;
            mapFactory.put(factory, factoryStats);
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