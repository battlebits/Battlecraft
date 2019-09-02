package br.com.battlebits.battlecraft.event;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDamagePlayerEvent extends Event implements Cancellable {

    public static final HandlerList handlers = new HandlerList();
    private Player damager;
    private Player damaged;
    private double damage;
    private double finalDamage;
    private boolean cancelled;

    public PlayerDamagePlayerEvent(Player damager, Player damaged, double damage, double finalDamage) {
        this.damager = damager;
        this.damaged = damaged;
        this.damage = damage;
        this.finalDamage = finalDamage;
    }

    public Player getDamager() {
        return damager;
    }

    public Player getDamaged() {
        return damaged;
    }

    public double getDamage() {
        return damage;
    }

    public double getFinalDamage() {
        return finalDamage;
    }

    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

}