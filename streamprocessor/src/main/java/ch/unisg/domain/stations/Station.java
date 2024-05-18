package ch.unisg.domain.stations;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Station {

    String id;
    String station;

    public Station(String id, String station) {
        this.id = id;
        this.station = station;
    }
}
