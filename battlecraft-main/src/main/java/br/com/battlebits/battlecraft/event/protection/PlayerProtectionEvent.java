package br.com.battlebits.battlecraft.event.protection;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;

public abstract class PlayerProtectionEvent extends PlayerEvent implements Cancellable {

    @Getter
    @Setter
    private boolean cancelled;

    public PlayerProtectionEvent(Player who) {
        super(who);
    }
}
