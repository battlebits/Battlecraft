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
    private Map<String, Object> globalValues;
    private Warp actualWarp;

    public <T> boolean containsValue(T value) {
        return globalValues.containsValue(value);
    }

    public boolean containsKey(String key) {
        return globalValues.containsKey(key);
    }

    public <T> void save(String key, T t) {
        globalValues.put(key, t);
    }

    public <T> T get(String key) {
        return (T) globalValues.get(key);
    }

    public WarpStatus getStatus(Warp warp) {
        return warpStatus.get(warp);
    }

    public void withWarp(Warp warp, Consumer<WarpStatus> statusConsumer) {
        if (warpStatus.containsKey(warp)) {
            statusConsumer.accept(warpStatus.get(warp));
        } else {
            WarpStatus status = new WarpStatus();
            statusConsumer.accept(status);
            warpStatus.put(warp, status);
        }
    }

    public void with(Consumer<PvPAccount> consumer) {
       consumer.accept(this);
    }
}
