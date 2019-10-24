package br.com.battlebits.battlecraft.ability;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.manager.CooldownManager;
import br.com.battlebits.battlecraft.manager.CooldownManager.Cooldown;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Ability implements Listener {

    private Set<Player> players;

    public Ability() {
        this.players = new HashSet<>();
    }

    protected boolean hasAbility(Player player) {
        return players.contains(player);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (this instanceof AbilityItem) {
            AbilityItem giveItem = (AbilityItem) this;
            if (giveItem.getItems().contains(event.getItemDrop().getItemStack()))
                event.setCancelled(true);
        }
    }

    /**
     * Register player in the ability listener
     *
     * @param player jogador que vai poder usar as habilidades
     */
    public void registerPlayer(Player player) {
        if (players.isEmpty() && !Disableable.class.isAssignableFrom(getClass())) {
            Bukkit.getPluginManager().registerEvents(this, Battlecraft.getInstance());
        }
        players.add(player);
    }

    /**
     * Unregister player from the listener and unregister listener if empty
     *
     * @param player
     */
    public void unregisterPlayer(Player player) {
        players.remove(player);
        CooldownManager.removeAllCooldowns(player);
        if (players.isEmpty() && !Disableable.class.isAssignableFrom(getClass()))
            HandlerList.unregisterAll(this);
    }

    /**
     * Checka se o jogador possui o cooldown
     *
     * @param player
     * @param cooldown
     * @return if player has the cooldown
     */
    protected boolean hasCooldown(Player player, String cooldown) {
        return CooldownManager.hasCooldown(player, cooldown);
    }

    /**
     * Adiciona um cooldown ao jogador
     * Caso ele já possua esse Cooldown, atualiza o tempo
     *
     * @param player
     * @param cooldownName
     * @param time
     */
    protected void addCooldown(Player player, String cooldownName, long time) {
        if (CooldownManager.hasCooldown(player, cooldownName)) {
            CooldownManager.removeCooldown(player, cooldownName);
        }
        CooldownManager.addCooldown(new Cooldown(player, cooldownName,
                System.currentTimeMillis() + time));

    }

}
