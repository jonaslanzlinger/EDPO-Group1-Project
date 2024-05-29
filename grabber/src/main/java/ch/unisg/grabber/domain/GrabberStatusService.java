package ch.unisg.grabber.domain;

import org.springframework.stereotype.Service;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is a service that provides the latest status of the Grabber.
 */
@Service
public class GrabberStatusService {

    // The latest status of the Grabber
    private final AtomicReference<HBW_1> latestStatus = new AtomicReference<>(new HBW_1());

    public void setLatestStatus(HBW_1 vgr1) {
        latestStatus.set(vgr1);
    }

    public HBW_1 getLatestStatus() {
        return latestStatus.get();
    }
}
