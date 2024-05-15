package ch.unisg.grabber.domain;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;

@Service
public class GrabberStatusService {

    // The latest status of the Grabber
    private final AtomicReference<VGR_1> latestStatus = new AtomicReference<>(new VGR_1());

    public void setLatestStatus(VGR_1 vgr1) {
        latestStatus.set(vgr1);
    }

    public VGR_1 getLatestStatus() {
        return latestStatus.get();
    }

}
