package br.com.battlebits.battlecraft.event.warp;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerWarpDeathEvent extends PlayerWarpEvent {

    public static HandlerList handlers = new HandlerList();
    @Getter
    private Player killer;
    @Getter
    private String kitName;
    @Getter
    private Warp warp;

    public PlayerWarpDeathEvent(Player p, Player killer, Warp warp) {
        super(p, warp);
        if (killer != null) {
            this.killer = killer;
        }
        this.warp = warp;
    }
    public boolean hasKiller() {
        return killer != null;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}