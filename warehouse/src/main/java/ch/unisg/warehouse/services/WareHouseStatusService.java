package ch.unisg.warehouse.services;

import ch.unisg.warehouse.domain.HBW_1;
import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class WareHouseStatusService {

    private final AtomicReference<HBW_1> latestStatus = new AtomicReference<>(new HBW_1());

    public void updateWarehouseStatus(HBW_1 hbw_1) {
        latestStatus.set(hbw_1);
    }

    public HBW_1 getLatestStatus() {
        return latestStatus.get();
    }

}

