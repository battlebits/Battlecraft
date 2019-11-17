package br.com.battlebits.battlecraft.event.protection;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerProtectionRemoveEvent extends PlayerProtectionEvent {

    @Getter
    public static final HandlerList handlerList = new HandlerList();

    public PlayerProtectionRemoveEvent(Player who) {
        super(who);
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

}
