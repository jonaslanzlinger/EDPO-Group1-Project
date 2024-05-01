package ch.unisg.domain.stations;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Station {

    String id;

    String station;

    public Station(String id, String station) {
        this.id = id;
        this.station = station;
    }
}
