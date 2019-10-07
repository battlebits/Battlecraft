package br.com.battlebits.battlecraft.event.warp;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

/**
 *  Usado para alterar as kills/deaths da warp
 */

@Getter
public class PlayerWarpDeathEvent extends PlayerWarpEvent {

    @Getter
    public static HandlerList handlerList = new HandlerList();
    private Player killer;
    private String kitName;
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
        return handlerList;
    }


}