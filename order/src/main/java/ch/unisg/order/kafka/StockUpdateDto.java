package ch.unisg.order.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockUpdateDto implements Serializable {
    private Map<String, String> data;

    public static StockUpdateDto fromJson(String message) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(message, StockUpdateDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
