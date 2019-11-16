package br.com.battlebits.battlecraft.status;

import br.com.battlebits.battlecraft.warp.Warp;
import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@Entity(value = "status", noClassnameStored = true)
public class StatusAccount {

    @Indexed(options = @IndexOptions(unique = true))
    private UUID uniqueId;
    @Indexed
    @Setter
    private String name;
    @Embedded
    private Map<Warp, WarpStatus> warpStatus;

    // Completou o tutorial
    private boolean tutorialCompleted;

    public StatusAccount(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
        this.warpStatus = new HashMap<>();
        this.tutorialCompleted = false;
    }

    public boolean containsWarpStatus(Warp warp) {
        return warpStatus.containsKey(warp);
    }

    public WarpStatus getWarpStatus(Warp warp) {
        return warpStatus.get(warp);
    }

    public void putWarpStatus(Warp warp, WarpStatus status) {
        this.warpStatus.put(warp, status);
    }

    public void withWarp(Warp warp, Consumer<WarpStatus> statusConsumer) {
        if (warpStatus.containsKey(warp)) {
            statusConsumer.accept(warpStatus.get(warp));
        } else {
            throw new RuntimeException("");
        }
    }

    public void with(Consumer<StatusAccount> consumer) {
       consumer.accept(this);
    }

    private void save() {

    }
}
