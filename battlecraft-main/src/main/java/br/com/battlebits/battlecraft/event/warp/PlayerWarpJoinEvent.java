package br.com.battlebits.battlecraft.event.warp;

import br.com.battlebits.battlecraft.warp.Warp;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerWarpJoinEvent extends PlayerWarpEvent {
    public static final HandlerList handlers = new HandlerList();

    public PlayerWarpJoinEvent(Player who, Warp warp) {
        super(who, warp);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
