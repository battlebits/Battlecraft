package br.com.battlebits.battlecraft.status.warpstatus;

import br.com.battlebits.battlecraft.status.WarpStatus;
import br.com.battlebits.battlecraft.status.ranking.Queue1v1;
import br.com.battlebits.battlecraft.status.ranking.RankedQueue;
import dev.morphia.annotations.Embedded;

import java.util.HashMap;
import java.util.Map;

@Embedded
public class Status1v1 implements WarpStatus {

    @Embedded
    private Map<Queue1v1, RankedQueue> queueStatus;

    public Status1v1() {
        super();
        this.queueStatus = new HashMap<>();
        for (Queue1v1 queue : Queue1v1.values())
            queueStatus.put(queue, new RankedQueue());
    }

    public RankedQueue getQueueStatus(Queue1v1 queue) {
        return queueStatus.get(queue);
    }
}
