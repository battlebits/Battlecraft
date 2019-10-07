package br.com.battlebits.battlecraft.ability;

import br.com.battlebits.battlecraft.Battlecraft;
import br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.Set;

import static br.com.battlebits.battlecraft.translate.BattlecraftTranslateTag.valueOf;

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

    // Provavelmente não vai ser usado
    public BattlecraftTranslateTag getNameTag() {
        return valueOf("ABILITY_" + getId().toUpperCase() + "_NAME");
    }

    public BattlecraftTranslateTag getDescriptionTag() {
        return valueOf("ABILITY_" + getId().toUpperCase() + "_DESCRIPTION");
    }

    public boolean hasKit(Player player) {
        return players.contains(player);
    }

    /**
     * Register player in the ability listener
     *
     * @param player
     */
    public void registerPlayer(Player player) {
        if (players.isEmpty())
            Bukkit.getPluginManager().registerEvents(this, Battlecraft.getInstance());
        players.add(player);
        if (this instanceof AbilityItem) {
            AbilityItem giveItem = (AbilityItem) this;
            giveItem.onReceiveItems(player);
        }
    }

    /**
     * Unregister player from the listener and unregister listener if empty
     * @param player
     */
    public void unregisterPlayer(Player player) {
        players.remove(player);
        if (players.isEmpty())
            HandlerList.unregisterAll(this);
    }

}
