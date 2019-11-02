package br.com.battlebits.battlecraft.backend.status;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PvPAccount {

    private UUID uniqueId;
    private String name;
    private Map<Warp, WarpStatus> warpStatus;

    public void with(Warp warp, Consumer<WarpStatus> statusConsumer) {
        if (warpStatus.containsKey(warp)) {
            statusConsumer.accept(warpStatus.get(warp));
        } else {
            WarpStatus status = new WarpStatus();
            statusConsumer.accept(status);
            warpStatus.put(warp, status);
        }
    }

}
