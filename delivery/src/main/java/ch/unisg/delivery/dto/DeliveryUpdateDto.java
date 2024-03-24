package ch.unisg.delivery.dto;

import ch.unisg.delivery.domain.VGR_1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryUpdateDto implements Serializable {

    private String type;
    private String id;
    private String source;
    private String time;
    private VGR_1 data;
    private String datacontenttype;
    private String specversion;
}
