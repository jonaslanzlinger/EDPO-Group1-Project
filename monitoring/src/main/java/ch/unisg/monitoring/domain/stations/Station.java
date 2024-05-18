package ch.unisg.monitoring.domain.stations;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Station {

    String id;

    public Station(String id) {
        this.id = id;
    }
}
