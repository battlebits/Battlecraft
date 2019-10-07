package br.com.battlebits.battlecraft.ability;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Use this if an ability has items
 * TODO Change this name
 */
public interface AbilityItem extends Listener {

    void onReceiveItems(Player player);

}
