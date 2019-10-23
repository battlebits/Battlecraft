package br.com.battlebits.battlecraft.event;

import br.com.battlebits.battlecraft.ability.Kit;
import br.com.battlebits.battlecraft.event.protection.PlayerProtectionEvent;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerKitEvent extends PlayerEvent {

    @Getter
    public static final HandlerList handlerList = new HandlerList();
    private Kit kit;

    public PlayerKitEvent(Player who, Kit kit) {
        super(who);
        this.kit = kit;
    }

    public HandlerList getHandlers() {
        return handlerList;
    }

}
