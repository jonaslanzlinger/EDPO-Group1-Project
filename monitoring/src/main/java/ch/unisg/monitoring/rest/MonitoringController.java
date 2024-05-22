package ch.unisg.monitoring.rest;


import ch.unisg.monitoring.kafka.topology.aggregations.ColorStats;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MonitoringController {

    private final ReadOnlyKeyValueStore<String, ColorStats> colorStatsStore;

    @GetMapping("/")
    public String index(Model model) {
        return "index"; // This should match the name of your Thymeleaf template file (updates.html)
    }


    @GetMapping("/updates-page")
    public String updatesPage(Model model) {

        Map<String, ColorStats> initialUpdate = new HashMap<>();
        var range = colorStatsStore.all();
        while (range.hasNext()) {
            var next = range.next();
            String color = next.key;
            var colorStats = next.value;
            initialUpdate.put(color, colorStats);
        }
        range.close();

        model.addAttribute("updates", initialUpdate);
        return "monitoring";
    }
}
