package br.com.battlebits.battlecraft.event.warp;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerWarpQuitEvent extends PlayerWarpEvent {
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    public PlayerWarpQuitEvent(Player who, Warp warp) {
        super(who, warp);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
