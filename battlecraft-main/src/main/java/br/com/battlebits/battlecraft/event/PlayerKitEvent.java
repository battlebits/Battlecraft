package br.com.battlebits.battlecraft.event;

import br.com.battlebits.battlecraft.event.protection.PlayerProtectionEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerKitEvent extends PlayerProtectionEvent {

    @Getter
    public static final HandlerList handlerList = new HandlerList();

    public PlayerKitEvent(Player who) {
        super(who);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

}
