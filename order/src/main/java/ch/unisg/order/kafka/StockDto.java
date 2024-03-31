package ch.unisg.order.kafka;

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
public class StockDto implements Serializable {

    // The type of the update
    private String type;

    // The ID of the update
    private String id;

    // The source of the update
    private String source;

    // The time of the update
    private String time;

    // The data of the update
    private Map<String, String> data;

    // The content type of the data
    private String datacontenttype;

    // The version of the specification
    private String specversion;
}
