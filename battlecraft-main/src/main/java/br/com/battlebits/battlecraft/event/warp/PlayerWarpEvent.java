package br.com.battlebits.battlecraft.event.warp;

import br.com.battlebits.battlecraft.warp.Warp;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;


@Getter
public abstract class PlayerWarpEvent extends PlayerEvent{

    private Warp warp;

    public PlayerWarpEvent(Player who, Warp warp) {
        super(who);
        this.warp = warp;
    }
}
