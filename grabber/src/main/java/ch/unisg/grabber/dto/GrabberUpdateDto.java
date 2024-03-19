package ch.unisg.grabber.dto;

import ch.unisg.grabber.domain.VGR_1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrabberUpdateDto implements Serializable {

    private String type;
    private String id;
    private String source;
    private String time;
    private VGR_1 data;
    private String datacontenttype;
    private String specversion;
}
