package br.com.battlebits.battlecraft.event.protection;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerProtectionRemoveEvent extends PlayerProtectionEvent {

    public static final HandlerList handlers = new HandlerList();

    public PlayerProtectionRemoveEvent(Player who) {
        super(who);
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
