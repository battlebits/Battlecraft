package br.com.battlebits.battlecraft.event;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;


public abstract class PlayerWarpEvent extends PlayerEvent implements Cancellable {

    @Getter
    private Warp warp;
    private boolean cancelled;

    public PlayerWarpEvent(Player who, Warp warp) {
        super(who);
        this.warp = warp;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
