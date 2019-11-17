package br.com.battlebits.battlecraft.event.warp;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

@Getter
public class PlayerWarpJoinEvent extends PlayerWarpEvent{
    @Getter
    public static final HandlerList handlerList = new HandlerList();

    public PlayerWarpJoinEvent(Player who, Warp warp) {
        super(who, warp);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }
}
