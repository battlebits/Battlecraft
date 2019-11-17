package br.com.battlebits.battlecraft.status;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.warp.Warp;
import dev.morphia.annotations.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

@Getter
@NoArgsConstructor
@Entity(value = "status", noClassnameStored = true)
public class StatusAccount {
    @Id
    @Getter(value = AccessLevel.NONE)
    private ObjectId objectId;

    @Indexed(options = @IndexOptions(unique = true))
    private UUID uniqueId;
    @Indexed
    @Setter
    private String name;
    @Embedded
    private Map<String, WarpStatus> warpStatus = new HashMap<>();

    // Completou o tutorial
    private boolean tutorialCompleted = false;

    public StatusAccount(UUID uniqueId, String name) {
        this.uniqueId = uniqueId;
        this.name = name;
    }

    public boolean containsWarpStatus(Warp warp) {
        return warpStatus.containsKey(warp.getId());
    }

    public WarpStatus getWarpStatus(Warp warp) {
        return warpStatus.get(warp.getId());
    }

    public void putWarpStatus(Warp warp, WarpStatus status) {
        this.warpStatus.put(warp.getId(), status);
    }

    public void withWarp(Warp warp, Consumer<WarpStatus> statusConsumer) {
        if (warpStatus.containsKey(warp)) {
            statusConsumer.accept(warpStatus.get(warp));
        } else {
            throw new RuntimeException("");
        }
    }

    public void save(Consumer<StatusAccount> consumer) {
        consumer.accept(this);
        Battlecraft.getInstance().getStatusManager().dataStatus().saveAccount(this);
    }
}
