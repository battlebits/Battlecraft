package br.com.battlebits.battlecraft.event.warp;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;


public abstract class PlayerWarpEvent extends PlayerEvent implements Cancellable {

    @Getter
    private Warp warp;
    @Getter
    @Setter
    private boolean cancelled;

    public PlayerWarpEvent(Player who, Warp warp) {
        super(who);
        this.warp = warp;
    }
}
