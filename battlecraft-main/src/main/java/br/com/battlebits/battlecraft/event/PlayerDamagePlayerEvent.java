package br.com.battlebits.battlecraft.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

@Getter
public class PlayerDamagePlayerEvent extends PlayerEvent implements Cancellable {
    @Getter
    public static final HandlerList handlersList = new HandlerList();
    private Player damaged;
    private double damage;
    private double finalDamage;
    @Setter
    private boolean cancelled;

    public PlayerDamagePlayerEvent(Player damager, Player damaged, double damage, double finalDamage) {
        super(damager);
        this.damaged = damaged;
        this.damage = damage;
        this.finalDamage = finalDamage;
    }

    public HandlerList getHandlers() {
        return handlersList;
    }

}