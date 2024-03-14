package ch.unisg.warehouse.dto;

import ch.unisg.warehouse.domain.HBW_1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WarehouseUpdateDto implements Serializable {

    private String type;
    private String id;
    private String source;
    private String time;
    private HBW_1 data;
    private String datacontenttype;
    private String specversion;
}
