package br.com.battlebits.battlecraft.ability;

import br.com.battlebits.battlecraft.Battlecraft;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Ability implements Listener {

    private Set<Player> players;

    private String id;
    private ItemStack icon;
    private int price;

    public Ability(String id, ItemStack icon, int price) {
        this.players = new HashSet<>();
        this.id = id.toLowerCase().trim().replace(" ", ".");
        this.icon = icon;
        this.price = price;
    }

    protected boolean hasAbility(Player player) {
        return players.contains(player);
    }

    /**
     * Register player in the ability listener
     *
     * @param player
     */
    public void registerPlayer(Player player) {
        if (players.isEmpty() && !Disableable.class.isAssignableFrom(getClass()))
            Bukkit.getPluginManager().registerEvents(this, Battlecraft.getInstance());
        players.add(player);
        if (this instanceof AbilityItem) {
            AbilityItem giveItem = (AbilityItem) this;
            giveItem.onReceiveItems(player);
        }
    }

    /**
     * Unregister player from the listener and unregister listener if empty
     *
     * @param player
     */
    public void unregisterPlayer(Player player) {
        players.remove(player);
        if (players.isEmpty() && !Disableable.class.isAssignableFrom(getClass()))
            HandlerList.unregisterAll(this);
    }

}
